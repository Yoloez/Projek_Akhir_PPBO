import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;

public class Dashboard extends JPanel {
    private JTextField tfNIM, tfNama;
    // MODIFIKASI: Ganti UI untuk memilih tanggal
    private JTextField tfTanggalDisplay; // Hanya untuk menampilkan tanggal yang dipilih
    private JButton btnPilihTanggal;

    private DefaultTableModel model;
    private MainPanel mainPanel;
    private ArrayList<String[]> dataSemuaMahasiswa;
    private ArrayList<String[]> dataPresensi;

    public Dashboard(MainPanel mainPanel, ArrayList<String[]> dataSemuaMahasiswa, ArrayList<String[]> dataPresensi) {
        this.mainPanel = mainPanel;
        this.dataSemuaMahasiswa = dataSemuaMahasiswa;
        this.dataPresensi = dataPresensi;

        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);

        // --- Input Mahasiswa Baru ---
        JLabel lblNIM = new JLabel("NIM:");
        lblNIM.setBounds(30, 20, 80, 25);
        Theme.applyLabelStyles(lblNIM);
        add(lblNIM);
        tfNIM = new JTextField();
        tfNIM.setBounds(120, 20, 200, 30);
        Theme.applyTextFieldStyles(tfNIM);
        add(tfNIM);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(30, 60, 80, 25);
        Theme.applyLabelStyles(lblNama);
        add(lblNama);
        tfNama = new JTextField();
        tfNama.setBounds(120, 60, 200, 30);
        Theme.applyTextFieldStyles(tfNama);
        add(tfNama);

        JButton btnTambah = new JButton("Tambah Mahasiswa");
        btnTambah.setBounds(30, 105, 180, 35);
        Theme.applyButtonStyles(btnTambah);
        add(btnTambah);

        JButton btnPresensi = new JButton("Halaman Presensi");
        btnPresensi.setBounds(230, 105, 180, 35);
        Theme.applyButtonStyles(btnPresensi);
        add(btnPresensi);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(550, 20, 100, 30);
        Theme.applyButtonStyles(btnLogout);
        add(btnLogout);

        // --- MODIFIKASI: Fitur Cek Riwayat Presensi dengan DatePicker ---
        JSeparator separator = new JSeparator();
        separator.setBounds(30, 155, 625, 2);
        add(separator);

        JLabel lblCekTanggal = new JLabel("Riwayat Presensi untuk Tanggal:");
        lblCekTanggal.setBounds(30, 170, 250, 25);
        Theme.applyLabelStyles(lblCekTanggal);
        add(lblCekTanggal);

        tfTanggalDisplay = new JTextField(LocalDate.now().toString());
        tfTanggalDisplay.setBounds(270, 170, 150, 30);
        tfTanggalDisplay.setEditable(false); // Buat agar tidak bisa diedit manual
        Theme.applyTextFieldStyles(tfTanggalDisplay);
        tfTanggalDisplay.setBackground(Theme.TEXT_FIELD_BACKGROUND.darker());
        add(tfTanggalDisplay);

        btnPilihTanggal = new JButton("Pilih Tanggal");
        btnPilihTanggal.setBounds(440, 170, 150, 30);
        Theme.applyButtonStyles(btnPilihTanggal);
        add(btnPilihTanggal);

        // --- Tabel untuk Menampilkan Data ---
        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Status Kehadiran"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 215, 625, 260);
        Theme.applyTableStyles(table, scrollPane);
        add(scrollPane);

        // Listeners
        btnTambah.addActionListener(e -> tambahMahasiswa());
        btnPresensi.addActionListener(e -> mainPanel.showPage("presensi"));
        btnLogout.addActionListener(e -> mainPanel.showPage("login"));

        // BARU: Listener untuk tombol "Pilih Tanggal"
        btnPilihTanggal.addActionListener(e -> {
            // Dapatkan frame utama sebagai parent dialog
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            DatePicker datePicker = new DatePicker(parentFrame);
            datePicker.setVisible(true); // Ini akan menghentikan eksekusi sampai dialog ditutup

            String selectedDate = datePicker.getSelectedDate();
            if (selectedDate != null && !selectedDate.isEmpty()) {
                tfTanggalDisplay.setText(selectedDate);
                // Langsung refresh tabel dengan tanggal yang baru dipilih
                try {
                    refreshDashboardView(LocalDate.parse(selectedDate));
                } catch (DateTimeParseException ex) {
                    // Seharusnya tidak terjadi karena format sudah benar dari DatePicker
                    System.err.println("Error parsing date dari DatePicker: " + ex.getMessage());
                }
            }
        });

        // Tampilkan data hari ini saat panel pertama kali dibuat
        refreshDashboardView(LocalDate.now());
    }

    private void tambahMahasiswa() {
        String nim = tfNIM.getText().trim();
        String nama = tfNama.getText().trim();

        if (!nim.isEmpty() && !nama.isEmpty()) {
            boolean nimExists = dataSemuaMahasiswa.stream().anyMatch(m -> m[0].equals(nim));

            if (nimExists) {
                JOptionPane.showMessageDialog(this, "NIM " + nim + " sudah terdaftar.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String[] data = {nim, nama, LocalDate.now().toString()};
                dataSemuaMahasiswa.add(data);
                mainPanel.saveDataMahasiswaToCSV();

                tfNIM.setText("");
                tfNama.setText("");

                // Refresh tabel untuk menampilkan mahasiswa baru
                refreshDashboardView(LocalDate.parse(tfTanggalDisplay.getText()));
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "NIM dan Nama harus diisi.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refreshDashboardView(LocalDate tanggal) {
        if (model == null) return;

        model.setRowCount(0);
        tfTanggalDisplay.setText(tanggal.toString());

        Map<String, String> presensiPadaTanggal = mainPanel.getPresensiForDate(tanggal);

        for (String[] mahasiswa : dataSemuaMahasiswa) {
            String nim = mahasiswa[0];
            String nama = mahasiswa[1];
            String status = presensiPadaTanggal.getOrDefault(nim, "Belum Ada Data");

            model.addRow(new String[]{nim, nama, status});
        }
    }
}