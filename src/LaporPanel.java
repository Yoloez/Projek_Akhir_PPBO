import javax.swing.*;
import java.awt.*;

public class LaporPanel extends JDialog {
    private JTextArea taLaporan;
    private MainPanel mainPanel;
    private String nim, nama, tanggalKehadiran;

    public LaporPanel(JFrame parent, MainPanel mainPanel, String nim, String nama, String tanggalKehadiran) {
        super(parent, "Buat Laporan Ketidaksesuaian", true);
        this.mainPanel = mainPanel;
        this.nim = nim;
        this.nama = nama;
        this.tanggalKehadiran = tanggalKehadiran;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel info
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(new JLabel("NIM:"));
        infoPanel.add(new JLabel(nim));
        infoPanel.add(new JLabel("Nama:"));
        infoPanel.add(new JLabel(nama));
        infoPanel.add(new JLabel("Tanggal yang Dilaporkan:"));
        infoPanel.add(new JLabel(tanggalKehadiran));
        add(infoPanel, BorderLayout.NORTH);

        // Area teks untuk laporan
        taLaporan = new JTextArea();
        taLaporan.setLineWrap(true);
        taLaporan.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(taLaporan);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tuliskan laporan Anda di sini:"));
        add(scrollPane, BorderLayout.CENTER);

        // Tombol Kirim
        JButton btnKirim = new JButton("Kirim Laporan");
        btnKirim.addActionListener(e -> kirimLaporan());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnKirim);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void kirimLaporan() {
        String isiLaporan = taLaporan.getText().trim().replace(",", ";"); // Ganti koma agar tidak merusak CSV
        if (isiLaporan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Laporan tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mainPanel.addLaporan(nim, nama, tanggalKehadiran, isiLaporan);

        JOptionPane.showMessageDialog(this, "Laporan berhasil dikirim!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        dispose(); // Tutup dialog
    }
}