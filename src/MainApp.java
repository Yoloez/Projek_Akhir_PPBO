// Di dalam MainApp.java
import javax.swing.*;
import java.awt.event.WindowAdapter; // Import
import java.awt.event.WindowEvent;   // Import

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aplikasi Presensi Mahasiswa");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Tetap EXIT_ON_CLOSE
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            MainPanel mainPanel = new MainPanel(); // Instance MainPanel
            frame.setContentPane(mainPanel);

            // Tambahkan WindowListener untuk menyimpan data saat aplikasi ditutup
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Panggil metode penyimpanan di MainPanel sebelum aplikasi benar-benar keluar
                    System.out.println("Menyimpan data sebelum keluar...");
                    mainPanel.saveDataMahasiswaToCSV();
                    // Tidak perlu System.exit(0) di sini karena default close operation sudah EXIT_ON_CLOSE
                }
            });

            frame.setVisible(true);
        });
    }
}