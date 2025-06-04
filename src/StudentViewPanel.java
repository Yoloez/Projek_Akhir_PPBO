// StudentViewPanel.java
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentViewPanel extends JPanel {
    private MainPanel mainPanel;
    private ArrayList<String[]> dataMahasiswa;

    private JTextField tfNIMEntry;
    private JButton btnCheckAttendance;
    private JLabel lblStudentNameResult, lblStudentNIMResult, lblRegistrationDateResult, lblAttendanceStatusResult, lblStatusDateInfo;
    private JLabel lblInfoMessage;

    public StudentViewPanel(MainPanel mainPanel, ArrayList<String[]> dataMahasiswa) {
        this.mainPanel = mainPanel;
        this.dataMahasiswa = dataMahasiswa; // Menggunakan data mahasiswa yang sama

        setBackground(Theme.BACKGROUND_DARK); // Mengatur warna latar belakang
        setLayout(null);

        int centerX = 700 / 2; // Asumsi lebar frame adalah 700

        JLabel lblTitle = new JLabel("Cek Status Kehadiran Anda");
        lblTitle.setBounds(centerX - 200, 30, 400, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        Theme.applyLabelStyles(lblTitle); // Menerapkan gaya ke label
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22)); // Mengatur font label
        add(lblTitle);

        JLabel lblNIMPrompt = new JLabel("Masukkan NIM Anda:");
        lblNIMPrompt.setBounds(centerX - 200, 90, 150, 25);
        Theme.applyLabelStyles(lblNIMPrompt); // Menerapkan gaya ke label
        add(lblNIMPrompt);

        tfNIMEntry = new JTextField();
        tfNIMEntry.setBounds(centerX - 40, 90, 200, 30);
        Theme.applyTextFieldStyles(tfNIMEntry); // Menerapkan gaya ke field teks
        add(tfNIMEntry);

        btnCheckAttendance = new JButton("Cek Kehadiran");
        btnCheckAttendance.setBounds(centerX - 100, 135, 200, 35);
        Theme.applyButtonStyles(btnCheckAttendance); // Menerapkan gaya ke tombol
        add(btnCheckAttendance);

        // Area untuk menampilkan hasil pencarian
        int currentY = 200;
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

        JLabel lblDisplayStatus = new JLabel("Status Kehadiran:");
        lblDisplayStatus.setBounds(xPosLabel, currentY, labelWidth, fieldHeight);
        Theme.applyLabelStyles(lblDisplayStatus);
        add(lblDisplayStatus);
        lblAttendanceStatusResult = new JLabel("-");
        lblAttendanceStatusResult.setBounds(xPosValue, currentY, valueWidth, fieldHeight);
        Theme.applyLabelStyles(lblAttendanceStatusResult);
        lblAttendanceStatusResult.setFont(new Font("Arial", Font.BOLD, 16)); // Status lebih menonjol
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

        JButton btnBackToLogin = new JButton("Kembali ke Login");
        btnBackToLogin.setBounds(centerX - 100, 380, 200, 35);
        Theme.applyButtonStyles(btnBackToLogin); // Menerapkan gaya ke tombol
        add(btnBackToLogin);

        // Action Listeners
        btnCheckAttendance.addActionListener(e -> checkStudentAttendance());
        btnBackToLogin.addActionListener(e -> mainPanel.showPage("login")); // Kembali ke halaman login

        clearStudentInfoDisplay(); // Membersihkan tampilan awal
    }

    private void checkStudentAttendance() {
        String nimToFind = tfNIMEntry.getText().trim();
        if (nimToFind.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan NIM Anda.", "Input Diperlukan", JOptionPane.WARNING_MESSAGE);
            clearStudentInfoDisplay();
            lblInfoMessage.setText("NIM tidak boleh kosong.");
            lblInfoMessage.setForeground(Color.ORANGE);
            return;
        }

        boolean found = false;
        for (String[] studentData : dataMahasiswa) { // Iterasi melalui data mahasiswa
            // Memastikan data memiliki setidaknya 4 elemen (NIM, Nama, Tanggal, Status)
            if (studentData.length >= 4 && studentData[0].equals(nimToFind)) {
                lblStudentNIMResult.setText(studentData[0]); // Menampilkan NIM mahasiswa
                lblStudentNameResult.setText(studentData[1]); // Menampilkan nama mahasiswa
                lblRegistrationDateResult.setText(studentData[2]); // Menampilkan tanggal pendaftaran
                lblAttendanceStatusResult.setText(studentData[3]); // Menampilkan status kehadiran

                // Mengatur warna teks status untuk visualisasi yang lebih baik
                if ("Hadir".equalsIgnoreCase(studentData[3])) {
                    lblAttendanceStatusResult.setForeground(new Color(0x28A745)); // Hijau
                } else if ("Tidak Hadir".equalsIgnoreCase(studentData[3]) || "Belum Hadir".equalsIgnoreCase(studentData[3])) {
                    lblAttendanceStatusResult.setForeground(new Color(0xDC3545)); // Merah
                } else {
                    lblAttendanceStatusResult.setForeground(Theme.FOREGROUND_LIGHT); // Warna default
                }

                lblInfoMessage.setText("Data ditemukan.");
                lblInfoMessage.setForeground(new Color(0x28A745)); // Hijau
                found = true;
                break;
            }
        }

        if (!found) {
            clearStudentInfoDisplay();
            lblInfoMessage.setText("NIM " + nimToFind + " tidak ditemukan.");
            lblInfoMessage.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this, "NIM " + nimToFind + " tidak ditemukan dalam sistem.", "Pencarian Gagal", JOptionPane.ERROR_MESSAGE);
        }
        lblStatusDateInfo.setText("(Untuk tanggal: " + LocalDate.now().toString() + ")"); // Memperbarui tanggal status
    }

    private void clearStudentInfoDisplay() {
        lblStudentNIMResult.setText("-");
        lblStudentNameResult.setText("-");
        lblRegistrationDateResult.setText("-");
        lblAttendanceStatusResult.setText("-");
        lblAttendanceStatusResult.setForeground(Theme.FOREGROUND_LIGHT); // Kembali ke warna default
        lblInfoMessage.setText("Masukkan NIM Anda untuk melihat status kehadiran.");
        lblInfoMessage.setForeground(Theme.ACCENT_BLUE); // Warna default untuk pesan info
    }

    // Metode ini dipanggil saat panel ini ditampilkan
    public void refreshView() {
        tfNIMEntry.setText(""); // Mengosongkan field input NIM
        clearStudentInfoDisplay(); // Membersihkan info mahasiswa yang ditampilkan sebelumnya
        lblStatusDateInfo.setText("(Untuk tanggal: " + LocalDate.now().toString() + ")"); // Memperbarui tanggal
    }
}