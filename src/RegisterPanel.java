import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private MainPanel mainPanel; // Reference to MainPanel to call registration logic and switch pages

    public RegisterPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null); // Using null layout as per existing structure

        int centerX = 700 / 2; // Assuming frame width is 700

        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setBounds(centerX - 75, 100, 150, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        Theme.applyLabelStyles(lblTitle);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20)); // Larger title
        add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(centerX - 150, 150, 80, 25);
        Theme.applyLabelStyles(lblUser);
        add(lblUser);
        tfUsername = new JTextField();
        tfUsername.setBounds(centerX - 70, 150, 200, 30);
        Theme.applyTextFieldStyles(tfUsername);
        add(tfUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(centerX - 150, 190, 80, 25);
        Theme.applyLabelStyles(lblPass);
        add(lblPass);
        pfPassword = new JPasswordField();
        pfPassword.setBounds(centerX - 70, 190, 200, 30);
        Theme.applyPasswordFieldStyles(pfPassword);
        add(pfPassword);

        JLabel lblConfirmPass = new JLabel("Confirm PW:");
        lblConfirmPass.setBounds(centerX - 150, 230, 80, 25);
        Theme.applyLabelStyles(lblConfirmPass);
        add(lblConfirmPass);
        pfConfirmPassword = new JPasswordField();
        pfConfirmPassword.setBounds(centerX - 70, 230, 200, 30);
        Theme.applyPasswordFieldStyles(pfConfirmPassword);
        add(pfConfirmPassword);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(centerX - 100, 280, 200, 35);
        Theme.applyButtonStyles(btnRegister);
        add(btnRegister);

        JButton btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setBounds(centerX - 100, 325, 200, 35);
        Theme.applyButtonStyles(btnBackToLogin);
        add(btnBackToLogin);

        // Action Listeners
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText();
                String password = new String(pfPassword.getPassword());
                String confirmPassword = new String(pfConfirmPassword.getPassword());

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                            "All fields must be filled.",
                            "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                            "Passwords do not match.",
                            "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Attempt registration via MainPanel
                boolean success = mainPanel.registerUser(username, password);
                if (success) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                            "Registration successful! Please login.",
                            "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    tfUsername.setText("");
                    pfPassword.setText("");
                    pfConfirmPassword.setText("");
                    mainPanel.showPage("login");
                } else {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                            "Username already exists.",
                            "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBackToLogin.addActionListener(e -> mainPanel.showPage("login"));
    }
}