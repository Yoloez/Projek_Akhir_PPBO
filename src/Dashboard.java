import javax.swing.*;
import javax.swing.table.DefaultTableModel;
// import java.awt.event.*; // Not strictly necessary if only using lambdas for ActionListeners
import java.time.LocalDate;
import java.util.ArrayList;

// Removed: import static javax.swing.text.html.HTML.Tag.HEAD; // This was an incorrect import

public class Dashboard extends JPanel {
    private JTextField tfNIM, tfNama, tfTanggal;
    private DefaultTableModel model;
    private MainPanel mainPanel;
    private ArrayList<String[]> dataMahasiswa; // Reference to the shared list in MainPanel

    public Dashboard(MainPanel mainPanel, ArrayList<String[]> dataMahasiswa) {
        this.mainPanel = mainPanel;
        this.dataMahasiswa = dataMahasiswa; // Store the reference

        // Assuming Theme class and its methods/constants are defined elsewhere
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
        // Assuming Theme.BACKGROUND_LIGHT and Theme.FOREGROUND_LIGHT are defined
        btnLogout.setBackground(Theme.BACKGROUND_LIGHT.brighter());
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
        Theme.applyTableStyles(table, scrollPane); // Assuming this method exists in Theme
        add(scrollPane);

        // Populate table with existing data if any (e.g. on panel switch)
        refreshDashboardTable();

        btnTambah.addActionListener(e -> {
            String nim = tfNIM.getText().trim(); // Added trim()
            String nama = tfNama.getText().trim(); // Added trim()
            String tanggal = LocalDate.now().toString(); // Date when student is added to dashboard

            if (!nim.isEmpty() && !nama.isEmpty()) {
                boolean nimExists = false;
                for (String[] existingData : this.dataMahasiswa) { // this.dataMahasiswa is the reference to the list in MainPanel
                    if (existingData[0].equals(nim)) {
                        nimExists = true;
                        break;
                    }
                }

                if (nimExists) {
                    JOptionPane.showMessageDialog(this, "NIM " + nim + " sudah terdaftar.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // The 4th element "Belum Hadir" is for presensi data, not directly shown in this dashboard table
                    String[] data = {nim, nama, tanggal, "Belum Hadir"};
                    this.dataMahasiswa.add(data); // Menambahkan ke list bersama
                    model.addRow(new String[]{nim, nama, tanggal}); // Add to this panel's table model
                    tfNIM.setText("");
                    tfNama.setText("");

                    // PENTING: Beritahu MainPanel untuk menyimpan perubahan (e.g., to CSV)
                    if (mainPanel != null) { // Good practice to check for null
                        mainPanel.notifyDataMahasiswaChanged();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Semua field (NIM dan Nama) harus diisi.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPresensi.addActionListener(e -> {
            if (mainPanel != null) {
                mainPanel.showPage("presensi");
            }
        });

        btnLogout.addActionListener(e -> {
            if (mainPanel != null) {
                mainPanel.showPage("login");
            }
        });
    }

    // Method to refresh the table view, e.g., when returning to this dashboard
    public void refreshDashboardTable() {
        if (model == null || dataMahasiswa == null) return; // Defensive check

        model.setRowCount(0); // Clear existing rows in the model
        tfTanggal.setText(LocalDate.now().toString()); // Update date field on refresh

        for (String[] data : dataMahasiswa) {
            // Dashboard table only shows NIM, Nama, Tanggal (when added)
            // Assuming data array always has at least 3 elements if it's valid
            if (data.length >= 3) {
                model.addRow(new String[]{data[0], data[1], data[2]});
            }
        }
    }
}

// Placeholder for Theme class - You need to have this defined elsewhere
// class Theme {
// public static final java.awt.Color BACKGROUND_DARK = new java.awt.Color(60, 63, 65);
// public static final java.awt.Color TEXT_FIELD_BACKGROUND = new java.awt.Color(75,78,80);
// public static final java.awt.Color BACKGROUND_LIGHT = new java.awt.Color(100,103,105);
// public static final java.awt.Color FOREGROUND_LIGHT = java.awt.Color.WHITE;

// public static void applyLabelStyles(JLabel label) {/*...styling...*/}
// public static void applyTextFieldStyles(JTextField textField) {/*...styling...*/}
// public static void applyButtonStyles(JButton button) {/*...styling...*/}
// public static void applyTableStyles(JTable table, JScrollPane scrollPane) {/*...styling...*/}
//}

// Placeholder for MainPanel class - You need to have this defined elsewhere
// class MainPanel extends JPanel {
// public void showPage(String pageName) {/*...logic to switch panels...*/}
// public void notifyDataMahasiswaChanged() {/*...logic to save data or update other components...*/}
// // It would also manage the ArrayList<String[]> dataMahasiswa
//}