import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {
    // BARU: Tambahkan field untuk NIM dan Nama
    private JTextField tfNIM;
    private JTextField tfNama;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private MainPanel mainPanel;

    public RegisterPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);

        int centerX = 700 / 2;
        int currentY = 80; // Sesuaikan posisi awal

        JLabel lblTitle = new JLabel("Registrasi Akun Mahasiswa");
        lblTitle.setBounds(centerX - 150, currentY, 300, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        Theme.applyLabelStyles(lblTitle);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle);
        currentY += 50;

        // --- BARU: Input untuk NIM ---
        JLabel lblNIM = new JLabel("NIM:");
        lblNIM.setBounds(centerX - 150, currentY, 100, 25);
        Theme.applyLabelStyles(lblNIM);
        add(lblNIM);
        tfNIM = new JTextField();
        tfNIM.setBounds(centerX - 50, currentY, 200, 30);
        Theme.applyTextFieldStyles(tfNIM);
        add(tfNIM);
        currentY += 40;

        // --- BARU: Input untuk Nama ---
        JLabel lblNama = new JLabel("Nama Lengkap:");
        lblNama.setBounds(centerX - 150, currentY, 100, 25);
        Theme.applyLabelStyles(lblNama);
        add(lblNama);
        tfNama = new JTextField();
        tfNama.setBounds(centerX - 50, currentY, 200, 30);
        Theme.applyTextFieldStyles(tfNama);
        add(tfNama);
        currentY += 40;

        // --- Input untuk Akun ---
        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(centerX - 150, currentY, 100, 25);
        Theme.applyLabelStyles(lblUser);
        add(lblUser);
        tfUsername = new JTextField();
        tfUsername.setBounds(centerX - 50, currentY, 200, 30);
        Theme.applyTextFieldStyles(tfUsername);
        add(tfUsername);
        currentY += 40;

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(centerX - 150, currentY, 100, 25);
        Theme.applyLabelStyles(lblPass);
        add(lblPass);
        pfPassword = new JPasswordField();
        pfPassword.setBounds(centerX - 50, currentY, 200, 30);
        Theme.applyPasswordFieldStyles(pfPassword);
        add(pfPassword);
        currentY += 40;

        JLabel lblConfirmPass = new JLabel("Confirm PW:");
        lblConfirmPass.setBounds(centerX - 150, currentY, 100, 25);
        Theme.applyLabelStyles(lblConfirmPass);
        add(lblConfirmPass);
        pfConfirmPassword = new JPasswordField();
        pfConfirmPassword.setBounds(centerX - 50, currentY, 200, 30);
        Theme.applyPasswordFieldStyles(pfConfirmPassword);
        add(pfConfirmPassword);
        currentY += 50;

        // Tombol
        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(centerX - 100, currentY, 200, 35);
        Theme.applyButtonStyles(btnRegister);
        add(btnRegister);
        currentY += 45;

        JButton btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setBounds(centerX - 100, currentY, 200, 35);
        Theme.applyButtonStyles(btnBackToLogin);
        add(btnBackToLogin);

        // Action Listeners
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // MODIFIKASI: Ambil data NIM dan Nama
                String nim = tfNIM.getText().trim();
                String nama = tfNama.getText().trim();
                String username = tfUsername.getText().trim();
                String password = new String(pfPassword.getPassword());
                String confirmPassword = new String(pfConfirmPassword.getPassword());

                // MODIFIKASI: Validasi semua field
                if (nim.isEmpty() || nama.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Semua field harus diisi.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Passwords do not match.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // MODIFIKASI: Panggil metode registrasi yang baru di MainPanel
                String result = mainPanel.registerMahasiswa(nim, nama, username, password);

                if (result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Registrasi berhasil!", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    // Kosongkan field setelah berhasil
                    tfNIM.setText("");
                    tfNama.setText("");
                    tfUsername.setText("");
                    pfPassword.setText("");
                    pfConfirmPassword.setText("");
                    // Navigasi akan diurus oleh MainPanel
                } else {
                    JOptionPane.showMessageDialog(RegisterPanel.this, result, "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBackToLogin.addActionListener(e -> mainPanel.showPage("login"));
    }
}