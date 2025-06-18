import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;

public class Dashboard extends BasePanel {
    // Komponen UI utama
    private JTextField tfNIM, tfNama;
    private JTextField tfTanggalDisplay;
    private JButton btnPilihTanggal;
    private JButton btnLihatLaporan; // Tombol baru

    // Tabel dan model untuk riwayat presensi
    private DefaultTableModel model;
    private JTable table;

    // Referensi data dan panel utama
    private MainPanel mainPanel;
    private ArrayList<String[]> dataSemuaMahasiswa;
    private ArrayList<String[]> dataPresensi;
    private ArrayList<String[]> dataLaporan;

    public Dashboard(MainPanel mainPanel, ArrayList<String[]> dataSemuaMahasiswa, ArrayList<String[]> dataPresensi, ArrayList<String[]> dataLaporan) {
        super(mainPanel);
        this.dataSemuaMahasiswa = dataSemuaMahasiswa;
        this.dataPresensi = dataPresensi;
        this.dataLaporan = dataLaporan;

        // --- UI Input Mahasiswa Baru ---
        JPanel panelMahasiswa = new JPanel(null);
        panelMahasiswa.setBackground(Theme.BACKGROUND_DARK);
        panelMahasiswa.setBorder(BorderFactory.createTitledBorder(null, "Manajemen Mahasiswa", 0, 0, null, Theme.FOREGROUND_LIGHT));
        panelMahasiswa.setBounds(20, 20, 410, 140);
        add(panelMahasiswa);

        JLabel lblNIM = new JLabel("NIM:");
        lblNIM.setBounds(20, 30, 80, 25);
        Theme.applyLabelStyles(lblNIM);
        panelMahasiswa.add(lblNIM);
        tfNIM = new JTextField();
        tfNIM.setBounds(110, 30, 280, 30);
        Theme.applyTextFieldStyles(tfNIM);
        panelMahasiswa.add(tfNIM);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(20, 70, 80, 25);
        Theme.applyLabelStyles(lblNama);
        panelMahasiswa.add(lblNama);
        tfNama = new JTextField();
        tfNama.setBounds(110, 70, 280, 30);
        Theme.applyTextFieldStyles(tfNama);
        panelMahasiswa.add(tfNama);

        // --- Panel Tombol Aksi ---
        JPanel panelAksi = new JPanel(null);
        panelAksi.setBackground(Theme.BACKGROUND_DARK);
        panelAksi.setBorder(BorderFactory.createTitledBorder(null, "Aksi Cepat", 0, 0, null, Theme.FOREGROUND_LIGHT));
        panelAksi.setBounds(440, 20, 225, 140);
        add(panelAksi);

        JButton btnTambah = new JButton("Tambah Mahasiswa");
        btnTambah.setBounds(20, 30, 185, 35);
        Theme.applyButtonStyles(btnTambah);
        panelAksi.add(btnTambah);

        JButton btnPresensi = new JButton("Halaman Presensi");
        btnPresensi.setBounds(20, 80, 185, 35);
        Theme.applyButtonStyles(btnPresensi);
        panelAksi.add(btnPresensi);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(565, 5, 100, 25);
        Theme.applyButtonStyles(btnLogout);
        add(btnLogout);

        // --- Panel Riwayat & Laporan ---
        JPanel panelKontrolBawah = new JPanel(null);
        panelKontrolBawah.setBackground(Theme.BACKGROUND_DARK);
        panelKontrolBawah.setBorder(BorderFactory.createTitledBorder(null, "Kontrol & Laporan", 0, 0, null, Theme.FOREGROUND_LIGHT));
        panelKontrolBawah.setBounds(20, 170, 645, 75);
        add(panelKontrolBawah);

        JLabel lblCekTanggal = new JLabel("Lihat Riwayat Tanggal:");
        lblCekTanggal.setBounds(20, 30, 150, 25);
        Theme.applyLabelStyles(lblCekTanggal);
        panelKontrolBawah.add(lblCekTanggal);

        tfTanggalDisplay = new JTextField(LocalDate.now().toString());
        tfTanggalDisplay.setBounds(180, 30, 120, 30);
        tfTanggalDisplay.setEditable(false);
        Theme.applyTextFieldStyles(tfTanggalDisplay);
        panelKontrolBawah.add(tfTanggalDisplay);

        btnPilihTanggal = new JButton("Pilih");
        btnPilihTanggal.setBounds(310, 30, 70, 30);
        Theme.applyButtonStyles(btnPilihTanggal);
        panelKontrolBawah.add(btnPilihTanggal);

        btnLihatLaporan = new JButton("Lihat Laporan");
        btnLihatLaporan.setBounds(450, 30, 175, 30);
        Theme.applyButtonStyles(btnLihatLaporan);
        panelKontrolBawah.add(btnLihatLaporan);

        // --- Tabel Riwayat Presensi ---
        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Status Kehadiran"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 255, 645, 220);
        Theme.applyTableStyles(table, scrollPane);
        add(scrollPane);

        // --- Action Listeners ---
        btnTambah.addActionListener(e -> tambahMahasiswa());
        btnPresensi.addActionListener(e -> mainPanel.showPage("presensi"));
        btnLogout.addActionListener(e -> mainPanel.showPage("login"));

        btnPilihTanggal.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            DatePicker datePicker = new DatePicker(parentFrame);
            datePicker.setVisible(true);
            String selectedDate = datePicker.getSelectedDate();
            if (selectedDate != null && !selectedDate.isEmpty()) {
                tfTanggalDisplay.setText(selectedDate);
                refreshDashboardView(LocalDate.parse(selectedDate));
            }
        });

        btnLihatLaporan.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            LihatLaporanDialog dialog = new LihatLaporanDialog(parentFrame, mainPanel, dataLaporan);
            dialog.setVisible(true);
            // Setelah dialog ditutup, update status tombol
            updateLaporanButton();
        });

        // Tampilkan semua data saat panel pertama kali dibuat
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
        updateLaporanButton();
    }

    // Metode untuk update tampilan tombol laporan
    private void updateLaporanButton() {
        boolean adaLaporanBaru = dataLaporan.stream().anyMatch(l -> l[5].equalsIgnoreCase("Baru"));
        if (adaLaporanBaru) {
            long jumlahBaru = dataLaporan.stream().filter(l -> l[5].equalsIgnoreCase("Baru")).count();
            btnLihatLaporan.setText("Laporan Masuk (" + jumlahBaru + " Baru)");
            btnLihatLaporan.setBackground(new Color(0xFFC107)); // Warna kuning
        } else {
            btnLihatLaporan.setText("Lihat Semua Laporan");
            Theme.applyButtonStyles(btnLihatLaporan); // Kembalikan ke style default
        }
    }
}