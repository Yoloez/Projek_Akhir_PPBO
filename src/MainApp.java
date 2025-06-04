import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aplikasi Presensi Mahasiswa");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            MainPanel mainPanel = new MainPanel();
            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
