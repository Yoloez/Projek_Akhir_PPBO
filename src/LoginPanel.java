import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends BasePanel {
    private JTextField tfUser;
    private JPasswordField tfPass;
    private MainPanel mainPanel;

    public LoginPanel(MainPanel mainPanel) {
        super(mainPanel);

        int centerX = 700 / 2;

        JLabel lblTitle = new JLabel("Student Attendance Login");
        lblTitle.setBounds(centerX - 150, 100, 300, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        Theme.applyLabelStyles(lblTitle);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(centerX - 150, 150, 80, 25);
        Theme.applyLabelStyles(lblUser);
        add(lblUser);
        tfUser = new JTextField(); // Default dihilangkan agar bersih
        tfUser.setBounds(centerX - 70, 150, 200, 30);
        Theme.applyTextFieldStyles(tfUser);
        add(tfUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(centerX - 150, 190, 80, 25);
        Theme.applyLabelStyles(lblPass);
        add(lblPass);
        tfPass = new JPasswordField(); // Default dihilangkan agar bersih
        tfPass.setBounds(centerX - 70, 190, 200, 30);
        Theme.applyPasswordFieldStyles(tfPass);
        add(tfPass);

        // Posisikan tombol di tengah
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(centerX - 100, 240, 200, 35);
        Theme.applyButtonStyles(btnLogin);
        add(btnLogin);

        JButton btnGoToRegister = new JButton("Create Student Account");
        btnGoToRegister.setBounds(centerX - 100, 285, 200, 35);
        Theme.applyButtonStyles(btnGoToRegister);
        add(btnGoToRegister);

        // --- PERUBAHAN: Tombol "Cek Kehadiran (Tanpa Login)" dihilangkan ---
        // JButton btnGoToStudentView = new JButton("Cek Kehadiran (Tanpa Login)");
        // ... baris-baris terkait tombol ini dihapus ...

        // Action Listener untuk Login dengan pengalihan berbasis peran
        btnLogin.addActionListener(e -> {
            String user = tfUser.getText().trim();
            String pass = new String(tfPass.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password harus diisi.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Panggil metode autentikasi yang mengembalikan objek User
            User loggedInUser = mainPanel.authenticateUser(user, pass);

            if (loggedInUser != null) {
                // Autentikasi berhasil, cek peran (role)
                String role = loggedInUser.getRole();

                if ("admin".equals(role)) {
                    // Jika admin, arahkan ke dashboard
                    mainPanel.showPage("dashboard");
                } else if ("mahasiswa".equals(role)) {
                    // Jika mahasiswa, ambil NIM-nya
                    String nim = loggedInUser.getNim();
                    if (nim != null) {
                        // Tampilkan data mahasiswa di StudentViewPanel
                        mainPanel.getStudentViewPanel().showStudentData(nim);
                        // Arahkan ke halaman mahasiswa
                        mainPanel.showPage("studentView");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: Akun mahasiswa tidak memiliki NIM.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Peran pengguna tidak dikenal.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }

                // Kosongkan field setelah login berhasil atau gagal
                tfUser.setText("");
                tfPass.setText("");

            } else {
                // Autentikasi gagal
                JOptionPane.showMessageDialog(this, "Login gagal! Username atau password salah.", "Login Error", JOptionPane.ERROR_MESSAGE);
                tfPass.setText(""); // Hanya kosongkan password
            }
        });

        btnGoToRegister.addActionListener(e -> mainPanel.showPage("register"));
    }
}