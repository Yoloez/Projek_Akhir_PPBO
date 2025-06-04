import javax.swing.*;
import java.awt.*;
import java.io.*; // Import untuk operasi file
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays; // Untuk Arrays.asList jika diperlukan untuk debugging

public class MainPanel extends JPanel {
    private CardLayout cardLayout;
    public ArrayList<String[]> dataMahasiswa = new ArrayList<>(); // Student attendance data
    private HashMap<String, User> registeredUsers = new HashMap<>();

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private Dashboard dashboardPanel;
    private PresensiPanel presensiPanel;
    private StudentViewPanel studentViewPanel;

    // Nama file CSV untuk menyimpan data mahasiswa
    private static final String DATA_MAHASISWA_CSV_FILE = "datamahasiswa.csv";

    public MainPanel() {
        registeredUsers.put("admin", new User("admin", "123")); // Default admin

        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(Theme.BACKGROUND_DARK);

        // PENTING: Muat data mahasiswa dari CSV saat aplikasi dimulai
        loadDataMahasiswaFromCSV();

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        // Pastikan dataMahasiswa yang sudah di-load dari CSV diteruskan ke Dashboard dan PresensiPanel
        dashboardPanel = new Dashboard(this, dataMahasiswa);
        presensiPanel = new PresensiPanel(this, dataMahasiswa);
        studentViewPanel = new StudentViewPanel(this, dataMahasiswa);

        add(loginPanel, "login");
        add(registerPanel, "register");
        add(dashboardPanel, "dashboard");
        add(presensiPanel, "presensi");
        add(studentViewPanel, "studentView");

        cardLayout.show(this, "login");
    }

    public void showPage(String name) {
        if (name.equals("presensi")) {
            presensiPanel.refreshTable();
        } else if (name.equals("dashboard")) {
            dashboardPanel.refreshDashboardTable();
        } else if (name.equals("studentView")) { // <-- Tambahkan kondisi untuk studentView
            studentViewPanel.refreshView();
        }
        cardLayout.show(this, name);
    }

    public boolean authenticateUser(String username, String password) {
        User user = registeredUsers.get(username);
        return user != null && user.checkPassword(password);
    }

    public boolean registerUser(String username, String password) {
        if (registeredUsers.containsKey(username)) {
            return false;
        }
        registeredUsers.put(username, new User(username, password));
        // Anda mungkin juga ingin menyimpan data pengguna ke file terpisah jika diperlukan
        return true;
    }

    // --- Metode untuk Operasi CSV Data Mahasiswa ---

    private void loadDataMahasiswaFromCSV() {
        File file = new File(DATA_MAHASISWA_CSV_FILE);
        if (!file.exists()) {
            System.out.println("File " + DATA_MAHASISWA_CSV_FILE + " tidak ditemukan, akan dibuat saat ada data baru.");
            return; // Tidak ada data untuk dimuat jika file tidak ada
        }

        dataMahasiswa.clear(); // Kosongkan list sebelum memuat data baru
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split berdasarkan koma, tapi hati-hati jika ada koma di dalam data itu sendiri
                // Untuk kasus sederhana, ini cukup. Untuk kasus kompleks, gunakan library CSV.
                String[] data = line.split(",", -1); // -1 agar tidak membuang empty string di akhir
                if (data.length == 4) { // Pastikan formatnya NIM, Nama, Tanggal, Status
                    dataMahasiswa.add(data);
                } else {
                    System.err.println("Baris data tidak valid di CSV: " + line + " (Jumlah kolom: " + data.length + ")");
                }
            }
            System.out.println("Data mahasiswa berhasil dimuat dari " + DATA_MAHASISWA_CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error saat memuat data mahasiswa dari CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveDataMahasiswaToCSV() {
        // Menggunakan try-with-resources untuk memastikan PrintWriter ditutup secara otomatis
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_MAHASISWA_CSV_FILE, false))) { // false untuk menimpa file
            for (String[] data : dataMahasiswa) {
                // Gabungkan array String menjadi satu string dengan pemisah koma
                // String.join aman untuk kasus di mana data tidak mengandung koma
                writer.println(String.join(",", data));
            }
            System.out.println("Data mahasiswa berhasil disimpan ke " + DATA_MAHASISWA_CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error saat menyimpan data mahasiswa ke CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metode ini bisa dipanggil dari Dashboard atau PresensiPanel
     * setiap kali ada perubahan pada dataMahasiswa yang perlu disimpan.
     */
    public void notifyDataMahasiswaChanged() {
        saveDataMahasiswaToCSV();
    }
}