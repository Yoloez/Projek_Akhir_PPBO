import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MainPanel extends JPanel {

    private CardLayout cardLayout;

    // Kumpulan data aplikasi
    public ArrayList<String[]> dataSemuaMahasiswa = new ArrayList<>();
    public ArrayList<String[]> dataPresensi = new ArrayList<>();
    public ArrayList<String[]> dataLaporan = new ArrayList<>();
    private HashMap<String, User> registeredUsers = new HashMap<>();

    // Panel-panel (halaman)
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private Dashboard dashboardPanel;
    private PresensiPanel presensiPanel;
    private StudentViewPanel studentViewPanel;

    // Nama file CSV
    private static final String DATA_MAHASISWA_CSV_FILE = "datamahasiswa.csv";
    private static final String DATA_PRESENSI_CSV_FILE = "presensi.csv";
    private static final String DATA_LAPORAN_CSV_FILE = "laporan.csv";
    // BARU: Nama file untuk menyimpan data pengguna
    private static final String USERS_CSV_FILE = "users.csv";

    public MainPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(Theme.BACKGROUND_DARK);

        // MODIFIKASI: Muat data pengguna dari CSV, bukan hardcode
        loadUsersFromCSV();

        // Jika setelah load, admin tidak ada (misal: aplikasi jalan pertama kali), maka buat admin
        if (!registeredUsers.containsKey("admin")) {
            registeredUsers.put("admin", new User("admin", "123", "admin"));
            saveUsersToCSV(); // Langsung simpan admin agar ada untuk selanjutnya
        }

        // Muat data-data lainnya
        loadDataMahasiswaFromCSV();
        loadDataPresensiFromCSV();
        loadDataLaporanFromCSV();

        // Inisialisasi setiap panel
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        dashboardPanel = new Dashboard(this, dataSemuaMahasiswa, dataPresensi, dataLaporan);
        presensiPanel = new PresensiPanel(this, dataSemuaMahasiswa, dataPresensi);
        studentViewPanel = new StudentViewPanel(this, dataSemuaMahasiswa, dataPresensi);

        // Tambahkan semua panel ke CardLayout
        add(loginPanel, "login");
        add(registerPanel, "register");
        add(dashboardPanel, "dashboard");
        add(presensiPanel, "presensi");
        add(studentViewPanel, "studentView");

        // Tampilkan halaman login sebagai halaman awal
        cardLayout.show(this, "login");
    }

    // --- Metode Inti Aplikasi ---

    public void showPage(String name) {
        if (name.equals("presensi")) {
            presensiPanel.resetToToday();
        } else if (name.equals("dashboard")) {
            dashboardPanel.refreshDashboardView(LocalDate.now());
        }
        cardLayout.show(this, name);
    }

    public User authenticateUser(String username, String password) {
        User user = registeredUsers.get(username);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }

    public String registerMahasiswa(String nim, String nama, String username, String password) {
        if (dataSemuaMahasiswa.stream().anyMatch(m -> m[0].equals(nim))) {
            return "NIM " + nim + " sudah terdaftar.";
        }
        if (registeredUsers.containsKey(username)) {
            return "Username '" + username + "' sudah digunakan.";
        }

        registeredUsers.put(username, new User(username, password, "mahasiswa", nim));
        // MODIFIKASI: Langsung simpan data user baru ke CSV agar permanen
        saveUsersToCSV();

        dataSemuaMahasiswa.add(new String[]{nim, nama, LocalDate.now().toString()});
        saveDataMahasiswaToCSV();

        studentViewPanel.showStudentData(nim);
        cardLayout.show(this, "studentView");

        return "SUCCESS";
    }

    public StudentViewPanel getStudentViewPanel() {
        return studentViewPanel;
    }

    // --- BARU: Metode untuk Load dan Save Pengguna Terdaftar ---

    public void saveUsersToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_CSV_FILE, false))) {
            writer.println("username,password,role,nim"); // Header CSV
            for (User user : registeredUsers.values()) {
                String nim = (user.getNim() == null) ? "" : user.getNim();
                String line = String.join(",", user.getUsername(), user.getPassword(), user.getRole(), nim);
                writer.println(line);
            }
            System.out.println("Data pengguna berhasil disimpan ke " + USERS_CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error saat menyimpan data pengguna: " + e.getMessage());
        }
    }

    private void loadUsersFromCSV() {
        File file = new File(USERS_CSV_FILE);
        if (!file.exists()) {
            System.out.println("File " + USERS_CSV_FILE + " tidak ditemukan. Akan dibuat baru.");
            return;
        }

        registeredUsers.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Lewati baris header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 4) {
                    String username = data[0];
                    String password = data[1];
                    String role = data[2];
                    String nim = data[3];

                    User user = new User(username, password, role, nim.isEmpty() ? null : nim);
                    registeredUsers.put(username, user);
                }
            }
            System.out.println("Data pengguna berhasil dimuat dari " + USERS_CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error saat memuat data pengguna: " + e.getMessage());
        }
    }

    // --- Metode Lainnya untuk Operasi CSV (Tidak ada perubahan) ---
    private void loadDataMahasiswaFromCSV() {
        File file = new File(DATA_MAHASISWA_CSV_FILE);
        if (!file.exists()) return;
        dataSemuaMahasiswa.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 3) dataSemuaMahasiswa.add(data);
            }
            System.out.println("Data mahasiswa berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Error saat memuat data mahasiswa: " + e.getMessage());
        }
    }

    public void saveDataMahasiswaToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_MAHASISWA_CSV_FILE, false))) {
            for (String[] data : dataSemuaMahasiswa) {
                writer.println(String.join(",", data));
            }
            System.out.println("Data mahasiswa berhasil disimpan.");
        } catch (IOException e) {
            System.err.println("Error saat menyimpan data mahasiswa: " + e.getMessage());
        }
    }

    private void loadDataPresensiFromCSV() {
        File file = new File(DATA_PRESENSI_CSV_FILE);
        if (!file.exists()) return;
        dataPresensi.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 3) dataPresensi.add(data);
            }
            System.out.println("Data presensi berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Error saat memuat data presensi: " + e.getMessage());
        }
    }

    public void saveDataPresensiToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_PRESENSI_CSV_FILE, false))) {
            for (String[] data : dataPresensi) {
                writer.println(String.join(",", data));
            }
            System.out.println("Data presensi berhasil disimpan.");
        } catch (IOException e) {
            System.err.println("Error saat menyimpan data presensi: " + e.getMessage());
        }
    }

    public void updatePresensi(String nim, LocalDate date, String status) {
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        boolean found = false;
        for (String[] presensi : dataPresensi) {
            if (presensi[0].equals(nim) && presensi[1].equals(dateString)) {
                presensi[2] = status;
                found = true;
                break;
            }
        }
        if (!found) {
            dataPresensi.add(new String[]{nim, dateString, status});
        }
        saveDataPresensiToCSV();
    }

    public Map<String, String> getPresensiForDate(LocalDate date) {
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return dataPresensi.stream()
                .filter(p -> p[1].equals(dateString))
                .collect(Collectors.toMap(p -> p[0], p -> p[2], (v1, v2) -> v1));
    }

    private void loadDataLaporanFromCSV() {
        File file = new File(DATA_LAPORAN_CSV_FILE);
        if (!file.exists()) return;
        dataLaporan.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 6) dataLaporan.add(data);
            }
            System.out.println("Data laporan berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Error saat memuat data laporan: " + e.getMessage());
        }
    }

    public void saveDataLaporanToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_LAPORAN_CSV_FILE, false))) {
            for (String[] data : dataLaporan) {
                writer.println(String.join(",", data));
            }
            System.out.println("Data laporan berhasil disimpan.");
        } catch (IOException e) {
            System.err.println("Error saat menyimpan data laporan: " + e.getMessage());
        }
    }

    public void addLaporan(String nim, String nama, String tanggalKehadiran, String isiLaporan) {
        String tanggalLapor = LocalDate.now().toString();
        String status = "Baru";
        dataLaporan.add(new String[]{tanggalLapor, nim, nama, tanggalKehadiran, isiLaporan, status});
        saveDataLaporanToCSV();
    }

    public void updateStatusLaporan(int rowIndex, String newStatus) {
        if (rowIndex >= 0 && rowIndex < dataLaporan.size()) {
            dataLaporan.get(rowIndex)[5] = newStatus;
            saveDataLaporanToCSV();
        }
    }
}