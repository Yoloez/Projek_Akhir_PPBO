import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class PresensiPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private MainPanel mainPanel;
    private ArrayList<String[]> dataSemuaMahasiswa;
    private ArrayList<String[]> dataPresensi;

    // --- MODIFIKASI: Tambahkan komponen UI sebagai field ---
    private LocalDate tanggalYangDitampilkan;
    private JLabel lblTitle;
    private JTextField tfTanggalDisplay;
    private JButton btnPilihTanggal;
    private JButton btnChecklist;
    private JButton btnChecklistAbsen;

    public PresensiPanel(MainPanel mainPanel, ArrayList<String[]> dataSemuaMahasiswa, ArrayList<String[]> dataPresensi) {
        this.mainPanel = mainPanel;
        this.dataSemuaMahasiswa = dataSemuaMahasiswa;
        this.dataPresensi = dataPresensi;
        this.tanggalYangDitampilkan = LocalDate.now(); // Default ke hari ini

        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);

        // Jadikan lblTitle sebagai field agar bisa diubah
        lblTitle = new JLabel("Daftar Presensi Mahasiswa - " + tanggalYangDitampilkan.toString());
        lblTitle.setBounds(30, 10, 600, 30);
        Theme.applyLabelStyles(lblTitle);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitle);

        // --- BARU: UI untuk memilih tanggal ---
        JLabel lblPilihTanggalInfo = new JLabel("Tampilkan untuk tanggal:");
        lblPilihTanggalInfo.setBounds(30, 50, 180, 25);
        Theme.applyLabelStyles(lblPilihTanggalInfo);
        add(lblPilihTanggalInfo);

        tfTanggalDisplay = new JTextField(tanggalYangDitampilkan.toString());
        tfTanggalDisplay.setBounds(210, 50, 150, 30);
        tfTanggalDisplay.setEditable(false);
        Theme.applyTextFieldStyles(tfTanggalDisplay);
        tfTanggalDisplay.setBackground(Theme.TEXT_FIELD_BACKGROUND.darker());
        add(tfTanggalDisplay);

        btnPilihTanggal = new JButton("Pilih Tanggal");
        btnPilihTanggal.setBounds(380, 50, 150, 30);
        Theme.applyButtonStyles(btnPilihTanggal);
        add(btnPilihTanggal);

        // Tabel presensi
        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Status Presensi"}, 0) { // Kolom disesuaikan
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        // MODIFIKASI: Geser tabel ke bawah untuk memberi ruang UI baru
        scrollPane.setBounds(30, 95, 625, 240);
        Theme.applyTableStyles(table, scrollPane);
        add(scrollPane);

        // Jadikan tombol sebagai field agar bisa di-enable/disable
        btnChecklist = new JButton("Tandai Hadir");
        btnChecklist.setBounds(150, 350, 180, 35);
        Theme.applyButtonStyles(btnChecklist);
        add(btnChecklist);

        btnChecklistAbsen = new JButton("Tandai Tidak Hadir");
        btnChecklistAbsen.setBounds(350, 350, 180, 35);
        Theme.applyButtonStyles(btnChecklistAbsen);
        btnChecklistAbsen.setBackground(new Color(0xFF4B4B));
        add(btnChecklistAbsen);

        JButton btnKembali = new JButton("Kembali ke Dashboard");
        btnKembali.setBounds(250, 400, 200, 35);
        Theme.applyButtonStyles(btnKembali);
        add(btnKembali);

        // --- Action Listeners ---
        btnPilihTanggal.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            DatePicker datePicker = new DatePicker(parentFrame);
            datePicker.setVisible(true);

            String selectedDateStr = datePicker.getSelectedDate();
            if (selectedDateStr != null && !selectedDateStr.isEmpty()) {
                this.tanggalYangDitampilkan = LocalDate.parse(selectedDateStr);
                refreshTable(); // Refresh tabel dengan tanggal baru
            }
        });

        btnChecklist.addActionListener(e -> updateStatus("Hadir"));
        btnChecklistAbsen.addActionListener(e -> updateStatus("Tidak Hadir"));
        btnKembali.addActionListener(e -> mainPanel.showPage("dashboard"));

        // Panggil refresh table saat inisialisasi
        refreshTable();
    }

    private void updateStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String nim = (String) model.getValueAt(selectedRow, 0);

            // Gunakan tanggal yang sedang ditampilkan (seharusnya selalu hari ini karena tombol aktif)
            mainPanel.updatePresensi(nim, this.tanggalYangDitampilkan, status);

            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // MODIFIKASI TOTAL: Logika refresh table untuk menangani tanggal yang dipilih
    public void refreshTable() {
        if (tanggalYangDitampilkan == null) {
            tanggalYangDitampilkan = LocalDate.now(); // Failsafe
        }
        model.setRowCount(0);

        // Update UI dengan tanggal yang dipilih
        String formattedDate = tanggalYangDitampilkan.toString();
        lblTitle.setText("Daftar Presensi Mahasiswa - " + formattedDate);
        tfTanggalDisplay.setText(formattedDate);
        model.setColumnIdentifiers(new String[]{"NIM", "Nama", "Status Presensi ("+formattedDate+")"});


        // Dapatkan data presensi untuk tanggal yang dipilih
        Map<String, String> presensiPadaTanggal = mainPanel.getPresensiForDate(tanggalYangDitampilkan);

        for (String[] mahasiswa : dataSemuaMahasiswa) {
            String nim = mahasiswa[0];
            String nama = mahasiswa[1];
            String status = presensiPadaTanggal.getOrDefault(nim, "Belum Ada Data");
            model.addRow(new String[]{nim, nama, status});
        }

        // --- LOGIKA PENTING: Aktifkan tombol hanya untuk hari ini ---
        boolean isToday = tanggalYangDitampilkan.equals(LocalDate.now());
        btnChecklist.setEnabled(isToday);
        btnChecklistAbsen.setEnabled(isToday);

        // Beri petunjuk jika tombol dinonaktifkan
        if (!isToday) {
            btnChecklist.setToolTipText("Hanya bisa mengubah presensi untuk hari ini.");
            btnChecklistAbsen.setToolTipText("Hanya bisa mengubah presensi untuk hari ini.");
        } else {
            btnChecklist.setToolTipText(null);
            btnChecklistAbsen.setToolTipText(null);
        }
    }

    /**
     * BARU: Metode untuk mereset tampilan ke hari ini.
     * Akan dipanggil dari MainPanel saat beralih halaman.
     */
    public void resetToToday() {
        this.tanggalYangDitampilkan = LocalDate.now();
        refreshTable();
    }
}