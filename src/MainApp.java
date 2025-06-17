// Di dalam MainApp.java
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aplikasi Presensi Mahasiswa");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 550); // MODIFIKASI: Sedikit lebih tinggi untuk UI baru
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            MainPanel mainPanel = new MainPanel();
            frame.setContentPane(mainPanel);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("Menyimpan semua data sebelum keluar...");
                    // MODIFIKASI: Simpan kedua file
                    mainPanel.saveDataMahasiswaToCSV();
                    mainPanel.saveDataPresensiToCSV();
                }
            });

            frame.setVisible(true);
        });
    }
}