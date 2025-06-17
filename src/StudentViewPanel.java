import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class StudentViewPanel extends JPanel {
    private MainPanel mainPanel;
    private ArrayList<String[]> dataSemuaMahasiswa;
    private ArrayList<String[]> dataPresensi;

    // Komponen UI
    private JTextField tfNIMEntry; // Tetap ada tapi disembunyikan, untuk menyimpan NIM internal
    private JButton btnCheckAttendance;
    private JLabel lblNIMPrompt;
    private JLabel lblStudentNameResult, lblStudentNIMResult, lblRegistrationDateResult, lblAttendanceStatusResult, lblStatusDateInfo;
    private JLabel lblInfoMessage;
    private JButton btnLaporkan;
    private JButton btnBackToLogin;

    private LocalDate tanggalYangDipilih;
    private JTextField tfTanggalDisplay;
    private JButton btnPilihTanggal;

    public StudentViewPanel(MainPanel mainPanel, ArrayList<String[]> dataSemuaMahasiswa, ArrayList<String[]> dataPresensi) {
        this.mainPanel = mainPanel;
        this.dataSemuaMahasiswa = dataSemuaMahasiswa;
        this.dataPresensi = dataPresensi;
        this.tanggalYangDipilih = LocalDate.now();

        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);

        int centerX = 700 / 2;
        int currentY = 30;

        JLabel lblTitle = new JLabel("Status Kehadiran Anda"); // Judul lebih personal
        lblTitle.setBounds(centerX - 200, currentY, 400, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        Theme.applyLabelStyles(lblTitle);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitle);
        currentY += 50;

        // PERBAIKAN: Komponen input NIM disembunyikan karena data didapat otomatis
        lblNIMPrompt = new JLabel("Masukkan NIM Anda:");
        lblNIMPrompt.setBounds(centerX - 220, currentY, 150, 25);
        Theme.applyLabelStyles(lblNIMPrompt);
        lblNIMPrompt.setVisible(false); // Sembunyikan
        add(lblNIMPrompt);

        tfNIMEntry = new JTextField();
        tfNIMEntry.setBounds(centerX - 60, currentY, 240, 30);
        Theme.applyTextFieldStyles(tfNIMEntry);
        tfNIMEntry.setVisible(false); // Sembunyikan
        add(tfNIMEntry);

        // Tombol Cek disembunyikan
        btnCheckAttendance = new JButton("Cek Kehadiran");
        btnCheckAttendance.setBounds(centerX - 100, currentY, 200, 35);
        Theme.applyButtonStyles(btnCheckAttendance);
        btnCheckAttendance.setVisible(false); // Sembunyikan
        add(btnCheckAttendance);
        // Posisi Y tidak perlu ditambah karena komponen disembunyikan

        // UI Pilih Tanggal (tetap ada)
        JLabel lblTanggalPrompt = new JLabel("Pilih Tanggal Cek:");
        lblTanggalPrompt.setBounds(centerX - 220, currentY, 150, 25);
        Theme.applyLabelStyles(lblTanggalPrompt);
        add(lblTanggalPrompt);

        tfTanggalDisplay = new JTextField(this.tanggalYangDipilih.toString());
        tfTanggalDisplay.setBounds(centerX - 60, currentY, 140, 30);
        tfTanggalDisplay.setEditable(false);
        Theme.applyTextFieldStyles(tfTanggalDisplay);
        tfTanggalDisplay.setBackground(Theme.TEXT_FIELD_BACKGROUND.darker());
        add(tfTanggalDisplay);

        btnPilihTanggal = new JButton("Pilih");
        btnPilihTanggal.setBounds(centerX + 90, currentY, 90, 30);
        Theme.applyButtonStyles(btnPilihTanggal);
        add(btnPilihTanggal);
        currentY += 45;

        // Area untuk menampilkan hasil pencarian
        int labelWidth = 180;
        int valueWidth = 250;
        int fieldHeight = 25;
        int xPosLabel = centerX - 220;
        int xPosValue = centerX - 30;

        // ... (semua JLabel untuk hasil pencarian tetap sama)
        JLabel lblDisplayNIM = new JLabel("NIM:");
        lblDisplayNIM.setBounds(xPosLabel, currentY, labelWidth, fieldHeight);
        Theme.applyLabelStyles(lblDisplayNIM);
        add(lblDisplayNIM);
        lblStudentNIMResult = new JLabel("-");
        lblStudentNIMResult.setBounds(xPosValue, currentY, valueWidth, fieldHeight);
        Theme.applyLabelStyles(lblStudentNIMResult);
        add(lblStudentNIMResult);
        currentY += fieldHeight + 5;
        JLabel lblDisplayName = new JLabel("Nama:");
        lblDisplayName.setBounds(xPosLabel, currentY, labelWidth, fieldHeight);
        Theme.applyLabelStyles(lblDisplayName);
        add(lblDisplayName);
        lblStudentNameResult = new JLabel("-");
        lblStudentNameResult.setBounds(xPosValue, currentY, valueWidth, fieldHeight);
        Theme.applyLabelStyles(lblStudentNameResult);
        add(lblStudentNameResult);
        currentY += fieldHeight + 5;
        JLabel lblDisplayRegDate = new JLabel("Tanggal Terdaftar:");
        lblDisplayRegDate.setBounds(xPosLabel, currentY, labelWidth, fieldHeight);
        Theme.applyLabelStyles(lblDisplayRegDate);
        add(lblDisplayRegDate);
        lblRegistrationDateResult = new JLabel("-");
        lblRegistrationDateResult.setBounds(xPosValue, currentY, valueWidth, fieldHeight);
        Theme.applyLabelStyles(lblRegistrationDateResult);
        add(lblRegistrationDateResult);
        currentY += fieldHeight + 5;
        JLabel lblDisplayStatus = new JLabel("Status Kehadiran:");
        lblDisplayStatus.setBounds(xPosLabel, currentY, labelWidth, fieldHeight);
        Theme.applyLabelStyles(lblDisplayStatus);
        add(lblDisplayStatus);
        lblAttendanceStatusResult = new JLabel("-");
        lblAttendanceStatusResult.setBounds(xPosValue, currentY, valueWidth, fieldHeight);
        Theme.applyLabelStyles(lblAttendanceStatusResult);
        lblAttendanceStatusResult.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblAttendanceStatusResult);
        lblStatusDateInfo = new JLabel("(Untuk tanggal: " + LocalDate.now().toString() + ")");
        lblStatusDateInfo.setBounds(xPosLabel, currentY + fieldHeight, 300, fieldHeight);
        Theme.applyLabelStyles(lblStatusDateInfo);
        lblStatusDateInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        add(lblStatusDateInfo);
        currentY += (fieldHeight * 2) + 10;
        lblInfoMessage = new JLabel(" ");
        lblInfoMessage.setBounds(centerX - 200, currentY, 400, 25);
        lblInfoMessage.setHorizontalAlignment(SwingConstants.CENTER);
        Theme.applyLabelStyles(lblInfoMessage);
        add(lblInfoMessage);
        currentY += 40;

        btnLaporkan = new JButton("Laporkan Ketidaksesuaian");
        Theme.applyButtonStyles(btnLaporkan);
        btnLaporkan.setBackground(new Color(0xFFC107));
        btnLaporkan.setBounds(centerX - 125, currentY, 250, 35);
        add(btnLaporkan);

        btnBackToLogin = new JButton("Kembali ke Halaman Login");
        btnBackToLogin.setBounds(centerX - 125, currentY + 45, 250, 35);
        Theme.applyButtonStyles(btnBackToLogin);
        add(btnBackToLogin);

        // --- Action Listeners ---
        btnPilihTanggal.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            DatePicker datePicker = new DatePicker(parentFrame);
            datePicker.setVisible(true);

            String selectedDateStr = datePicker.getSelectedDate();
            if (selectedDateStr != null && !selectedDateStr.isEmpty()) {
                this.tanggalYangDipilih = LocalDate.parse(selectedDateStr);
                tfTanggalDisplay.setText(selectedDateStr);
                // Setelah memilih tanggal, langsung cek kehadiran untuk NIM yang sudah tersimpan
                checkStudentAttendance();
            }
        });

        // Listener untuk btnCheckAttendance bisa dihapus karena tombolnya disembunyikan
        btnBackToLogin.addActionListener(e -> mainPanel.showPage("login"));

        btnLaporkan.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            String nim = lblStudentNIMResult.getText();
            String nama = lblStudentNameResult.getText();
            String tanggalDikeluhkan = tanggalYangDipilih.toString();

            LaporPanel laporPanel = new LaporPanel(parentFrame, mainPanel, nim, nama, tanggalDikeluhkan);
            laporPanel.setVisible(true);
        });

        clearStudentInfoDisplay();
    }

    /**
     * BARU: Metode ini dipanggil oleh MainPanel setelah registrasi berhasil.
     * Ini akan mengisi panel dengan data mahasiswa yang sesuai secara otomatis.
     */
    public void showStudentData(String nim) {
        // Simpan NIM di textfield yang tersembunyi
        tfNIMEntry.setText(nim);
        // Langsung panggil checkStudentAttendance untuk menampilkan data
        checkStudentAttendance();
    }

    private void checkStudentAttendance() {
        // Ambil NIM dari textfield internal, bukan dari input manual
        String nimToFind = tfNIMEntry.getText().trim();
        if (nimToFind.isEmpty()) {
            // Ini seharusnya tidak terjadi di alur baru, tapi sebagai pengaman
            lblInfoMessage.setText("Tidak ada data mahasiswa untuk ditampilkan.");
            return;
        }

        Optional<String[]> studentOpt = dataSemuaMahasiswa.stream()
                .filter(m -> m[0].equals(nimToFind))
                .findFirst();

        if (studentOpt.isPresent()) {
            String[] studentData = studentOpt.get();
            lblStudentNIMResult.setText(studentData[0]);
            lblStudentNameResult.setText(studentData[1]);
            lblRegistrationDateResult.setText(studentData[2]);

            String dateString = this.tanggalYangDipilih.format(DateTimeFormatter.ISO_LOCAL_DATE);
            Optional<String[]> presensiOpt = dataPresensi.stream()
                    .filter(p -> p[0].equals(nimToFind) && p[1].equals(dateString))
                    .findFirst();

            String newStatus = presensiOpt.map(p -> p[2]).orElse("Belum Ada Data");
            lblAttendanceStatusResult.setText(newStatus);

            if ("Hadir".equalsIgnoreCase(newStatus)) {
                lblAttendanceStatusResult.setForeground(new Color(0x28A745));
            } else {
                lblAttendanceStatusResult.setForeground(new Color(0xDC3545));
            }

            lblInfoMessage.setText("Selamat datang! Pilih tanggal untuk melihat riwayat.");
            lblInfoMessage.setForeground(Theme.ACCENT_BLUE);
            btnLaporkan.setVisible(true);
        } else {
            clearStudentInfoDisplay();
            lblInfoMessage.setText("Data untuk NIM " + nimToFind + " tidak ditemukan.");
            lblInfoMessage.setForeground(Color.RED);
        }

        lblStatusDateInfo.setText("(Untuk tanggal: " + this.tanggalYangDipilih.toString() + ")");
    }

    private void clearStudentInfoDisplay() {
        lblStudentNIMResult.setText("-");
        lblStudentNameResult.setText("-");
        lblRegistrationDateResult.setText("-");
        lblAttendanceStatusResult.setText("-");
        lblAttendanceStatusResult.setForeground(Theme.FOREGROUND_LIGHT);
        lblInfoMessage.setText("Selamat datang di halaman kehadiran.");
        lblInfoMessage.setForeground(Theme.ACCENT_BLUE);

        if (btnLaporkan != null) {
            btnLaporkan.setVisible(false);
        }
    }

    public void refreshView() {
        tfNIMEntry.setText("");
        this.tanggalYangDipilih = LocalDate.now();
        this.tfTanggalDisplay.setText(this.tanggalYangDipilih.toString());
        clearStudentInfoDisplay();
        lblStatusDateInfo.setText("(Untuk tanggal: " + this.tanggalYangDipilih.toString() + ")");
    }
}