import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.time.LocalDate;

public class Dashboard extends JPanel {
    private JTextField tfNIM, tfNama, tfTanggal;
    private DefaultTableModel model;
    private MainPanel mainPanel;
    private ArrayList<String[]> dataMahasiswa;

    public Dashboard(MainPanel mainPanel, ArrayList<String[]> dataMahasiswa) {
        this.mainPanel = mainPanel;
        this.dataMahasiswa = dataMahasiswa; // Store the reference

        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);

        JLabel lblNIM = new JLabel("NIM:");
        lblNIM.setBounds(30, 30, 80, 25);
        Theme.applyLabelStyles(lblNIM);
        add(lblNIM);
        tfNIM = new JTextField();
        tfNIM.setBounds(120, 30, 200, 30);
        Theme.applyTextFieldStyles(tfNIM);
        add(tfNIM);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(30, 70, 80, 25);
        Theme.applyLabelStyles(lblNama);
        add(lblNama);
        tfNama = new JTextField();
        tfNama.setBounds(120, 70, 200, 30);
        Theme.applyTextFieldStyles(tfNama);
        add(tfNama);

        JLabel lblTanggal = new JLabel("Tanggal:");
        lblTanggal.setBounds(30, 110, 80, 25);
        Theme.applyLabelStyles(lblTanggal);
        add(lblTanggal);
        tfTanggal = new JTextField(LocalDate.now().toString());
        tfTanggal.setEditable(false);
        tfTanggal.setBounds(120, 110, 200, 30);
        Theme.applyTextFieldStyles(tfTanggal);
        tfTanggal.setBackground(Theme.TEXT_FIELD_BACKGROUND.darker()); // Visually indicate non-editable
        add(tfTanggal);

        JButton btnTambah = new JButton("Tambah Mahasiswa");
        btnTambah.setBounds(30, 160, 180, 35);
        Theme.applyButtonStyles(btnTambah);
        add(btnTambah);

        JButton btnPresensi = new JButton("Halaman Presensi");
        btnPresensi.setBounds(230, 160, 180, 35);
        Theme.applyButtonStyles(btnPresensi);
        add(btnPresensi);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(550, 20, 100, 30);
        Theme.applyButtonStyles(btnLogout);
        btnLogout.setBackground(Theme.BACKGROUND_LIGHT.brighter()); // Different color for logout
        btnLogout.setForeground(Theme.FOREGROUND_LIGHT);
        add(btnLogout);


        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Tanggal Ditambahkan"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 210, 625, 220);
        Theme.applyTableStyles(table, scrollPane);
        add(scrollPane);

        // Populate table with existing data if any (e.g. on panel switch)
        refreshDashboardTable();


        // Di dalam Dashboard.java


        btnTambah.addActionListener(e -> {
            String nim = tfNIM.getText();
            String nama = tfNama.getText();
            String tanggal = LocalDate.now().toString();

            if (!nim.isEmpty() && !nama.isEmpty()) {
                boolean nimExists = false;
                for (String[] existingData : this.dataMahasiswa) { // this.dataMahasiswa adalah referensi ke list di MainPanel
                    if (existingData[0].equals(nim)) {
                        nimExists = true;
                        break;
                    }
                }

                if (nimExists) {
                    JOptionPane.showMessageDialog(this, "NIM " + nim + " sudah terdaftar.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String[] data = {nim, nama, tanggal, "Belum Hadir"};
                    this.dataMahasiswa.add(data); // Menambahkan ke list bersama
                    model.addRow(new String[]{nim, nama, tanggal});
                    tfNIM.setText("");
                    tfNama.setText("");

                    // PENTING: Beritahu MainPanel untuk menyimpan perubahan ke CSV
                    mainPanel.notifyDataMahasiswaChanged();
                }
            } else {
                JOptionPane.showMessageDialog(this, "NIM dan Nama harus diisi.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPresensi.addActionListener(e -> mainPanel.showPage("presensi"));
        btnLogout.addActionListener(e -> mainPanel.showPage("login"));
    }

    // Method to refresh the table view, e.g., when returning to this dashboard
    public void refreshDashboardTable() {
        model.setRowCount(0); // Clear existing rows in the model
        tfTanggal.setText(LocalDate.now().toString()); // Update date on refresh
        for (String[] data : dataMahasiswa) {
            // Dashboard table only shows NIM, Nama, Tanggal (when added)
            model.addRow(new String[]{data[0], data[1], data[2]});
        }
    }
}