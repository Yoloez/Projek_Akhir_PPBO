import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

// Menggunakan BasePanel sebagai parent class
public class PresensiPanel extends BasePanel {
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<String[]> dataSemuaMahasiswa;
    private ArrayList<String[]> dataPresensi;

    private LocalDate tanggalYangDitampilkan;
    private JLabel lblTitle;
    private JTextField tfTanggalDisplay;
    private JButton btnPilihTanggal;
    private JButton btnChecklist;
    private JButton btnChecklistAbsen;

    public PresensiPanel(MainPanel mainPanel, ArrayList<String[]> dataSemuaMahasiswa, ArrayList<String[]> dataPresensi) {
        super(mainPanel); // Memanggil constructor parent (BasePanel)

        this.dataSemuaMahasiswa = dataSemuaMahasiswa;
        this.dataPresensi = dataPresensi;
        this.tanggalYangDitampilkan = LocalDate.now();

        lblTitle = new JLabel("Daftar Presensi Mahasiswa - " + tanggalYangDitampilkan.toString());
        lblTitle.setBounds(30, 10, 600, 30);
        Theme.applyLabelStyles(lblTitle);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitle);

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

        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Status Presensi"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 95, 625, 240);
        Theme.applyTableStyles(table, scrollPane);
        add(scrollPane);

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

        btnPilihTanggal.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            DatePicker datePicker = new DatePicker(parentFrame);
            datePicker.setVisible(true);
            String selectedDateStr = datePicker.getSelectedDate();
            if (selectedDateStr != null && !selectedDateStr.isEmpty()) {
                this.tanggalYangDitampilkan = LocalDate.parse(selectedDateStr);
                refreshTable();
            }
        });

        btnChecklist.addActionListener(e -> updateStatus("Hadir"));
        btnChecklistAbsen.addActionListener(e -> updateStatus("Tidak Hadir"));
        btnKembali.addActionListener(e -> mainPanel.showPage("dashboard"));

        refreshTable();
    }

    private void updateStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String nim = (String) model.getValueAt(selectedRow, 0);
            // Metode updatePresensi sudah menerima tanggal, jadi ini akan bekerja untuk tanggal manapun
            mainPanel.updatePresensi(nim, this.tanggalYangDitampilkan, status);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refreshTable() {
        if (tanggalYangDitampilkan == null) {
            tanggalYangDitampilkan = LocalDate.now();
        }
        model.setRowCount(0);

        String formattedDate = tanggalYangDitampilkan.toString();
        lblTitle.setText("Daftar Presensi Mahasiswa - " + formattedDate);
        tfTanggalDisplay.setText(formattedDate);
        model.setColumnIdentifiers(new String[]{"NIM", "Nama", "Status Presensi (" + formattedDate + ")"});

        Map<String, String> presensiPadaTanggal = mainPanel.getPresensiForDate(tanggalYangDitampilkan);

        for (String[] mahasiswa : dataSemuaMahasiswa) {
            String nim = mahasiswa[0];
            String nama = mahasiswa[1];
            String status = presensiPadaTanggal.getOrDefault(nim, "Belum Ada Data");
            model.addRow(new String[]{nim, nama, status});
        }

        // --- PERUBAHAN: Blok kode yang menonaktifkan tombol DIHAPUS ---
        // Dengan tidak adanya kode di sini, tombol akan selalu aktif.
    }

    public void resetToToday() {
        this.tanggalYangDitampilkan = LocalDate.now();
        refreshTable();
    }
}