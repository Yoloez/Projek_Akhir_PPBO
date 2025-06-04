import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private JTextField tfUser;
    private JPasswordField tfPass;
    private MainPanel mainPanel;

    public LoginPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);

        int centerX = 700 / 2; // Assuming frame width is 700

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
        tfUser = new JTextField("admin"); // Default for convenience
        tfUser.setBounds(centerX - 70, 150, 200, 30);
        Theme.applyTextFieldStyles(tfUser);
        add(tfUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(centerX - 150, 190, 80, 25);
        Theme.applyLabelStyles(lblPass);
        add(lblPass);
        tfPass = new JPasswordField("123"); // Default for convenience
        tfPass.setBounds(centerX - 70, 190, 200, 30);
        Theme.applyPasswordFieldStyles(tfPass);
        add(tfPass);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(centerX - 100, 240, 200, 35);
        Theme.applyButtonStyles(btnLogin);
        add(btnLogin);

        JButton btnGoToRegister = new JButton("Create Account");
        btnGoToRegister.setBounds(centerX - 100, 285, 200, 35);
        Theme.applyButtonStyles(btnGoToRegister);
        add(btnGoToRegister);

        btnLogin.addActionListener(e -> {
            String user = tfUser.getText();
            String pass = new String(tfPass.getPassword());

            if (mainPanel.authenticateUser(user, pass)) {
                mainPanel.showPage("dashboard");
            } else {
                JOptionPane.showMessageDialog(this, "Login failed! Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnGoToRegister.addActionListener(e -> mainPanel.showPage("register"));
    }
}