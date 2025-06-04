import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.Font; // <-- IMPORT ADDED HERE
import java.awt.Color; // Import for Color if used directly (e.g. for btnChecklistAbsen)


public class PresensiPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private MainPanel mainPanel;
    private ArrayList<String[]> dataMahasiswa; // Shared data

    public PresensiPanel(MainPanel mainPanel, ArrayList<String[]> dataMahasiswa) {
        this.mainPanel = mainPanel;
        this.dataMahasiswa = dataMahasiswa;

        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);

        JLabel lblTitle = new JLabel("Daftar Presensi Mahasiswa - " + LocalDate.now().toString());
        lblTitle.setBounds(30, 10, 600, 30);
        Theme.applyLabelStyles(lblTitle);
        // The error was likely here if you were using new Font() directly instead of Theme.FONT_LABEL
        // For example, if you had: lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        // Using Theme.applyLabelStyles which uses Theme.FONT_LABEL is correct.
        // If Theme.FONT_LABEL was not used and Font was directly instantiated, the import is crucial.
        // The provided code already uses Theme.applyLabelStyles for lblTitle,
        // but the error indicates Font class was not found, meaning it was likely used directly somewhere
        // or a component within Theme.java needs it and the PresensiPanel itself tried to use it.
        // My previous response for PresensiPanel already uses applyLabelStyles, which is good.
        // The explicit error points to PresensiPanel, so let's ensure direct Font usage here is covered.
        // For instance, if there was a line like this (which wasn't in my generated code but might be in user's version):
        // lblTitle.setFont(new Font("Arial", Font.BOLD, 16)); // This would need java.awt.Font
        // My generated code for lblTitle in PresensiPanel correctly uses:
        // Theme.applyLabelStyles(lblTitle);
        // lblTitle.setFont(new Font("Arial", Font.BOLD, 16)); // This was in my generated code for PresensiPanel's lblTitle AFTER applyLabelStyles
        // So, the import is indeed necessary.
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16)); // This line was present and needs the import.


        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Tanggal Terdaftar", "Status Presensi"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 50, 625, 280);
        Theme.applyTableStyles(table, scrollPane);
        add(scrollPane);

        JButton btnChecklist = new JButton("Tandai Hadir");
        btnChecklist.setBounds(150, 350, 180, 35);
        Theme.applyButtonStyles(btnChecklist);
        add(btnChecklist);

        JButton btnChecklistAbsen = new JButton("Tandai Tidak Hadir");
        btnChecklistAbsen.setBounds(350, 350, 180, 35);
        Theme.applyButtonStyles(btnChecklistAbsen);
        btnChecklistAbsen.setBackground(new Color(0xAA0000)); // A reddish color for Absen
        add(btnChecklistAbsen);


        JButton btnKembali = new JButton("Kembali ke Dashboard");
        btnKembali.setBounds(250, 400, 200, 35);
        Theme.applyButtonStyles(btnKembali);
        add(btnKembali);

        // Initial table population
        refreshTable();

        btnChecklist.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1 && selectedRow < dataMahasiswa.size()) { // Tambahkan pengecekan batas
                dataMahasiswa.get(selectedRow)[3] = "Hadir";
                refreshTable();

                // PENTING: Beritahu MainPanel untuk menyimpan perubahan ke CSV
                mainPanel.notifyDataMahasiswaChanged();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu atau data tidak valid.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnChecklistAbsen.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1 && selectedRow < dataMahasiswa.size()) { // Tambahkan pengecekan batas
                dataMahasiswa.get(selectedRow)[3] = "Tidak Hadir";
                refreshTable();

                // PENTING: Beritahu MainPanel untuk menyimpan perubahan ke CSV
                mainPanel.notifyDataMahasiswaChanged();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu atau data tidak valid.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnKembali.addActionListener(e -> mainPanel.showPage("dashboard"));
    }

    // Refreshes the table with current data from dataMahasiswa
    public void refreshTable() {
        model.setRowCount(0); // Clear table before repopulating
        // To safely get the title label, it's better to store it as a field if frequently accessed
        // or iterate components and check instanceof. Assuming it's the first JLABEL by order of addition:
        for (java.awt.Component comp : getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Daftar Presensi Mahasiswa")) {
                ((JLabel) comp).setText("Daftar Presensi Mahasiswa - " + LocalDate.now().toString());
                break;
            }
        }


        for (String[] data : dataMahasiswa) {
            if (data.length == 4) { // Ensure data array has status
                model.addRow(data);
            }
        }
    }
}