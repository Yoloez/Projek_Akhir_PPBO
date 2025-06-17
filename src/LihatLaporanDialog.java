import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class LihatLaporanDialog extends JDialog {
    private JTable laporanTable;
    private DefaultTableModel laporanModel;
    private MainPanel mainPanel;
    private ArrayList<String[]> dataLaporan;

    public LihatLaporanDialog(JFrame parent, MainPanel mainPanel, ArrayList<String[]> dataLaporan) {
        super(parent, "Daftar Laporan Mahasiswa", true);
        this.mainPanel = mainPanel;
        this.dataLaporan = dataLaporan;

        setSize(650, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        laporanModel = new DefaultTableModel(new String[]{"Tgl Lapor", "NIM", "Nama", "Tgl Dikeluhkan", "Isi Laporan", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        laporanTable = new JTable(laporanModel);
        JScrollPane scrollPane = new JScrollPane(laporanTable);
        Theme.applyTableStyles(laporanTable, scrollPane);

        // Atur lebar kolom
        laporanTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Tgl Lapor
        laporanTable.getColumnModel().getColumn(1).setPreferredWidth(70);  // NIM
        laporanTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Isi Laporan
        laporanTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // Status


        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Theme.BACKGROUND_DARK);

        JButton btnTandaiSelesai = new JButton("Tandai Selesai");
        Theme.applyButtonStyles(btnTandaiSelesai);

        JButton btnTutup = new JButton("Tutup");
        Theme.applyButtonStyles(btnTutup);

        buttonPanel.add(btnTandaiSelesai);
        buttonPanel.add(btnTutup);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnTandaiSelesai.addActionListener(e -> tandaiSelesai());
        btnTutup.addActionListener(e -> dispose());

        // Muat data ke tabel saat dialog dibuat
        refreshLaporanTable();
    }

    private void refreshLaporanTable() {
        laporanModel.setRowCount(0);
        for (String[] laporan : dataLaporan) {
            laporanModel.addRow(laporan);
        }
    }

    private void tandaiSelesai() {
        int selectedRow = laporanTable.getSelectedRow();
        if (selectedRow != -1) {
            // Dapatkan index yang benar dari model jika ada filter/sort, tapi di sini sama
            int modelRow = laporanTable.convertRowIndexToModel(selectedRow);

            int response = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin menandai laporan ini sebagai 'Selesai'?",
                    "Konfirmasi Tindakan",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                mainPanel.updateStatusLaporan(modelRow, "Selesai");
                refreshLaporanTable(); // Refresh tabel untuk menampilkan status baru
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih laporan dari tabel terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}