import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class PresensiPanel extends JPanel {
    private DefaultTableModel model;

    public PresensiPanel(MainPanel mainPanel, ArrayList<String[]> dataMahasiswa) {
        setLayout(null);

        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Tanggal", "Status"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 30, 600, 300);
        add(scrollPane);

        JButton btnChecklist = new JButton("Checklist Hadir");
        btnChecklist.setBounds(200, 350, 150, 30);
        add(btnChecklist);

        JButton btnKembali = new JButton("Kembali");
        btnKembali.setBounds(370, 350, 150, 30);
        add(btnKembali);

        refreshTable(dataMahasiswa);

        btnChecklist.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                dataMahasiswa.get(selected)[3] = "Hadir";
                refreshTable(dataMahasiswa);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu.");
            }
        });

        btnKembali.addActionListener(e -> mainPanel.showPage("dashboard"));
    }

    public void refreshTable(ArrayList<String[]> dataMahasiswa) {
        model.setRowCount(0); // hapus semua isi sebelumnya
        for (String[] data : dataMahasiswa) {
            // pastikan data memiliki 4 elemen
            if (data.length == 4) {
                model.addRow(data);
            }
        }

    }
}

