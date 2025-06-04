import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.time.LocalDate;

public class Dashboard extends JPanel {
    private JTextField tfNIM, tfNama, tfTanggal;
    private DefaultTableModel model;
    private MainPanel mainPanel; // Store reference if needed for other actions
    private ArrayList<String[]> dataMahasiswa; // Store reference

    public Dashboard(MainPanel mainPanel, ArrayList<String[]> dataMahasiswa) {
        this.mainPanel = mainPanel;
        this.dataMahasiswa = dataMahasiswa;

        // Set a modern Look and Feel (Nimbus is a good built-in option)
        // This should ideally be set once at the start of your application (e.g., in main method)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to the default L&F
            System.err.println("Nimbus L&F not found, using default.");
        }

        setLayout(new BorderLayout(10, 10)); // Main layout with gaps
        setBorder(new EmptyBorder(15, 15, 15, 15)); // Add padding around the whole panel

        // --- Input Form Panel (Top) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Input Data Mahasiswa"),
                new EmptyBorder(10, 10, 10, 10) // Padding inside the titled border
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // NIM
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1; // Label column width
        formPanel.add(new JLabel("NIM:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9; // Text field column width
        tfNIM = new JTextField(15); // Suggest a preferred size
        formPanel.add(tfNIM, gbc);

        // Nama
        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Nama:"), gbc);

        gbc.gridx = 1;
        tfNama = new JTextField(15);
        formPanel.add(tfNama, gbc);

        // Tanggal
        gbc.gridy = 2;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Tanggal:"), gbc);

        gbc.gridx = 1;
        tfTanggal = new JTextField(LocalDate.now().toString());
        tfTanggal.setEditable(false);
        tfTanggal.setFocusable(false); // Also prevent focus if not editable
        tfTanggal.setBackground(UIManager.getColor("TextField.disabledBackground")); // Visual cue
        formPanel.add(tfTanggal, gbc);

        add(formPanel, BorderLayout.NORTH);

        // --- Table Panel (Center) ---
        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Tanggal"}, 0);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true); // Table uses entire height of scroll pane
        table.setRowHeight(25); // Increase row height for better readability
        // Optional: table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        // Optional: table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Panel (South) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Align buttons to the right
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Top padding for button panel

        JButton btnTambah = new JButton("Tambah");
        // Optional: btnTambah.setIcon(new ImageIcon(getClass().getResource("/path/to/add_icon.png"))); // Add icon
        btnTambah.setToolTipText("Tambah data mahasiswa baru");
        btnTambah.setPreferredSize(new Dimension(120, 30));

        JButton btnPresensi = new JButton("Halaman Presensi");
        // Optional: btnPresensi.setIcon(new ImageIcon(getClass().getResource("/path/to/presensi_icon.png")));
        btnPresensi.setToolTipText("Buka halaman presensi mahasiswa");
        btnPresensi.setPreferredSize(new Dimension(180, 30));

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnPresensi);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnTambah.addActionListener(e -> {
            String nim = tfNIM.getText().trim();
            String nama = tfNama.getText().trim();
            // Tanggal is always current, no need to get from tfTanggal for logic if it's always today
            String tanggal = LocalDate.now().toString();

            if (!nim.isEmpty() && !nama.isEmpty()) {
                // Check for duplicate NIM in the table model before adding
                boolean nimExists = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(nim)) {
                        nimExists = true;
                        break;
                    }
                }

                if (nimExists) {
                    JOptionPane.showMessageDialog(this, "NIM " + nim + " sudah ada dalam daftar.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String[] dataForList = {nim, nama, tanggal, "Belum Hadir"};
                    this.dataMahasiswa.add(dataForList); // Add to the shared list

                    String[] dataForTable = {nim, nama, tanggal};
                    model.addRow(dataForTable); // Add to the table display

                    tfNIM.setText("");
                    tfNama.setText("");
                    // tfTanggal will remain as today's date, no need to reset unless logic changes
                    JOptionPane.showMessageDialog(this, "Data mahasiswa berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "NIM dan Nama harus diisi.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPresensi.addActionListener(e -> {
            if (this.mainPanel != null) {
                this.mainPanel.showPage("presensi");
            } else {
                System.err.println("MainPanel reference is null in Dashboard.");
            }
        });

        // Populate table with existing data from dataMahasiswa (if any)
        // This ensures that if Dashboard is re-created or data existed before, it's shown
        // Ensure this logic aligns with how dataMahasiswa is managed application-wide
        for (String[] rowData : this.dataMahasiswa) {
            // Assuming the first 3 elements are NIM, Nama, Tanggal for the table
            if (rowData.length >= 3) {
                model.addRow(new String[]{rowData[0], rowData[1], rowData[2]});
            }
        }
    }

    // Optional: Method to refresh table data if dataMahasiswa is modified externally
    public void refreshTableData() {
        // Clear existing rows
        model.setRowCount(0);
        // Repopulate
        for (String[] rowData : this.dataMahasiswa) {
            if (rowData.length >= 3) { // Ensure it has enough data for table columns
                model.addRow(new String[]{rowData[0], rowData[1], rowData[2]});
            }
        }
    }

    // Example of how to run this panel in a JFrame for testing
    public static void main(String[] args) {
        // It's good practice to set L&F at the very beginning of the app
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dashboard Mahasiswa Modern");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Dummy MainPanel for testing, replace with your actual MainPanel
            MainPanel dummyMainPanel = new MainPanel() {
                // Minimal MainPanel implementation for the sake of Dashboard
                @Override
                public void showPage(String pageName) {
                    System.out.println("Switching to page: " + pageName);
                    if ("presensi".equals(pageName)) {
                        // You could show a different panel or dialog here for testing
                        JOptionPane.showMessageDialog(frame, "Ini Halaman Presensi (Contoh)");
                    }
                }
            };
            ArrayList<String[]> dummyData = new ArrayList<>();
            // Add some initial dummy data to see it populate
            dummyData.add(new String[]{"101", "Adi", LocalDate.now().minusDays(1).toString(), "Hadir"});
            dummyData.add(new String[]{"102", "Budi", LocalDate.now().toString(), "Belum Hadir"});

            Dashboard dashboardPanel = new Dashboard(dummyMainPanel, dummyData);
            frame.add(dashboardPanel);
            frame.pack(); // Sizes the frame to fit preferred sizes of components
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });
    }

    // Dummy MainPanel class for compilation if you don't have it in the same context
    // In your actual project, you'd have your own MainPanel class.
    static abstract class MainPanel extends JPanel {
        public abstract void showPage(String pageName);
    }
}