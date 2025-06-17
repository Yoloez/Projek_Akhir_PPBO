// StudentViewPanel.java
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

    // --- MODIFIKASI: Tambahkan komponen UI sebagai field ---
    private JTextField tfNIMEntry;
    private JButton btnCheckAttendance;
    private JLabel lblStudentNameResult, lblStudentNIMResult, lblRegistrationDateResult, lblAttendanceStatusResult, lblStatusDateInfo;
    private JLabel lblInfoMessage;

    // BARU: Komponen untuk memilih tanggal
    private LocalDate tanggalYangDipilih;
    private JTextField tfTanggalDisplay;
    private JButton btnPilihTanggal;

    public StudentViewPanel(MainPanel mainPanel, ArrayList<String[]> dataSemuaMahasiswa, ArrayList<String[]> dataPresensi) {
        this.mainPanel = mainPanel;
        this.dataSemuaMahasiswa = dataSemuaMahasiswa;
        this.dataPresensi = dataPresensi;
        this.tanggalYangDipilih = LocalDate.now(); // Default ke hari ini

        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);

        int centerX = 700 / 2;
        int currentY = 30; // Posisi Y awal

        JLabel lblTitle = new JLabel("Cek Status Kehadiran Anda");
        lblTitle.setBounds(centerX - 200, currentY, 400, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        Theme.applyLabelStyles(lblTitle);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitle);
        currentY += 50;

        // Input NIM
        JLabel lblNIMPrompt = new JLabel("Masukkan NIM Anda:");
        lblNIMPrompt.setBounds(centerX - 220, currentY, 150, 25);
        Theme.applyLabelStyles(lblNIMPrompt);
        add(lblNIMPrompt);
        tfNIMEntry = new JTextField();
        tfNIMEntry.setBounds(centerX - 60, currentY, 240, 30);
        Theme.applyTextFieldStyles(tfNIMEntry);
        add(tfNIMEntry);
        currentY += 40;

        // --- BARU: UI Pilih Tanggal ---
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

        // Tombol Cek
        btnCheckAttendance = new JButton("Cek Kehadiran");
        btnCheckAttendance.setBounds(centerX - 100, currentY, 200, 35);
        Theme.applyButtonStyles(btnCheckAttendance);
        add(btnCheckAttendance);
        currentY += 50;


        // Area untuk menampilkan hasil pencarian
        int labelWidth = 180;
        int valueWidth = 250;
        int fieldHeight = 25;
        int xPosLabel = centerX - 220;
        int xPosValue = centerX - 30;

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

        // MODIFIKASI: Label lebih generik
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

        JButton btnBackToLogin = new JButton("Kembali ke Login");
        btnBackToLogin.setBounds(centerX - 100, currentY, 200, 35);
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
            }
        });

        btnCheckAttendance.addActionListener(e -> checkStudentAttendance());
        btnBackToLogin.addActionListener(e -> mainPanel.showPage("login"));

        clearStudentInfoDisplay();
    }

    // MODIFIKASI: Logika pengecekan menggunakan tanggal yang dipilih
    private void checkStudentAttendance() {
        String nimToFind = tfNIMEntry.getText().trim();
        if (nimToFind.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan NIM Anda.", "Input Diperlukan", JOptionPane.WARNING_MESSAGE);
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

            // Gunakan tanggalYangDipilih untuk mencari status
            String dateString = this.tanggalYangDipilih.format(DateTimeFormatter.ISO_LOCAL_DATE);
            Optional<String[]> presensiOpt = dataPresensi.stream()
                    .filter(p -> p[0].equals(nimToFind) && p[1].equals(dateString))
                    .findFirst();

            String status = presensiOpt.map(p -> p[2]).orElse("Belum Ada Data");
            lblAttendanceStatusResult.setText(status);

            if ("Hadir".equalsIgnoreCase(status)) {
                lblAttendanceStatusResult.setForeground(new Color(0x28A745));
            } else {
                lblAttendanceStatusResult.setForeground(new Color(0xDC3545));
            }

            lblInfoMessage.setText("Data ditemukan.");
            lblInfoMessage.setForeground(new Color(0x28A745));
        } else {
            clearStudentInfoDisplay();
            lblInfoMessage.setText("NIM " + nimToFind + " tidak ditemukan.");
            lblInfoMessage.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this, "NIM " + nimToFind + " tidak ditemukan.", "Pencarian Gagal", JOptionPane.ERROR_MESSAGE);
        }

        // Update label info dengan tanggal yang dicek
        lblStatusDateInfo.setText("(Untuk tanggal: " + this.tanggalYangDipilih.toString() + ")");
    }

    private void clearStudentInfoDisplay() {
        lblStudentNIMResult.setText("-");
        lblStudentNameResult.setText("-");
        lblRegistrationDateResult.setText("-");
        lblAttendanceStatusResult.setText("-");
        lblAttendanceStatusResult.setForeground(Theme.FOREGROUND_LIGHT);
        lblInfoMessage.setText("Masukkan NIM, pilih tanggal, lalu klik Cek Kehadiran.");
        lblInfoMessage.setForeground(Theme.ACCENT_BLUE);
    }

    // Metode ini dipanggil saat panel ini ditampilkan
    public void refreshView() {
        tfNIMEntry.setText("");
        // Reset tanggal ke hari ini
        this.tanggalYangDipilih = LocalDate.now();
        this.tfTanggalDisplay.setText(this.tanggalYangDipilih.toString());
        clearStudentInfoDisplay();
        lblStatusDateInfo.setText("(Untuk tanggal: " + this.tanggalYangDipilih.toString() + ")");
    }
}