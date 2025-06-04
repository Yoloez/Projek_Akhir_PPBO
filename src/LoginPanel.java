import javax.swing.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainPanel mainPanel) {
        setLayout(null);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(220, 150, 80, 25);
        add(lblUser);
        JTextField tfUser = new JTextField();
        tfUser.setBounds(300, 150, 150, 25);
        add(tfUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(220, 180, 80, 25);
        add(lblPass);
        JPasswordField tfPass = new JPasswordField();
        tfPass.setBounds(300, 180, 150, 25);
        add(tfPass);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(300, 220, 150, 30);
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String user = tfUser.getText();
            String pass = new String(tfPass.getPassword());

            if (user.equals("admin") && pass.equals("123")) {
                mainPanel.showPage("dashboard");
            } else {
                JOptionPane.showMessageDialog(this, "Login gagal!");
            }
        });
    }
}
