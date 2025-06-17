import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aplikasi Presensi Mahasiswa");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 550); // Ukuran bisa disesuaikan lagi jika perlu
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            MainPanel mainPanel = new MainPanel();
            frame.setContentPane(mainPanel);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("Menyimpan semua data sebelum keluar...");
                    mainPanel.saveDataMahasiswaToCSV();
                    mainPanel.saveDataPresensiToCSV();
                    mainPanel.saveDataLaporanToCSV();
                    // BARU: Panggil save untuk data pengguna
                    mainPanel.saveUsersToCSV();
                }
            });

            frame.setVisible(true);
        });
    }
}