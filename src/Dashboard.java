import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.time.LocalDate;

public class Dashboard extends JPanel {
    private JTextField tfNIM, tfNama, tfTanggal;
    private DefaultTableModel model;

    public Dashboard(MainPanel mainPanel, ArrayList<String[]> dataMahasiswa) {
        setLayout(null);

        JLabel lblNIM = new JLabel("NIM:");
        lblNIM.setBounds(30, 30, 80, 25);
        add(lblNIM);
        tfNIM = new JTextField();
        tfNIM.setBounds(120, 30, 150, 25);
        add(tfNIM);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(30, 60, 80, 25);
        add(lblNama);
        tfNama = new JTextField();
        tfNama.setBounds(120, 60, 150, 25);
        add(tfNama);

        JLabel lblTanggal = new JLabel("Tanggal:");
        lblTanggal.setBounds(30, 90, 80, 25);
        add(lblTanggal);
        tfTanggal = new JTextField(LocalDate.now().toString()); // otomatis isi tanggal hari ini
        tfTanggal.setEditable(false); // tidak bisa diubah manual
        tfTanggal.setBounds(120, 90, 150, 25);
        add(tfTanggal);

        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBounds(120, 130, 100, 30);
        add(btnTambah);

        JButton btnPresensi = new JButton("Halaman Presensi");
        btnPresensi.setBounds(250, 130, 160, 30);
        add(btnPresensi);

        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Tanggal"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 180, 600, 250);
        add(scrollPane);

        btnTambah.addActionListener(e -> {
            String nim = tfNIM.getText();
            String nama = tfNama.getText();
            String tanggal = LocalDate.now().toString();


            if (!nim.isEmpty() && !nama.isEmpty()) {
                String[] data = {nim, nama, tanggal, "Belum Hadir"};
                dataMahasiswa.add(data);
                model.addRow(new String[]{nim, nama, tanggal});
                tfNIM.setText(""); tfNama.setText("");
                // tidak perlu set tanggal lagi karena akan diatur ulang otomatis
            } else {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi.");
            }
        });

        btnPresensi.addActionListener(e -> mainPanel.showPage("presensi"));
    }
}
