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

    public ArrayList<String[]> dataSemuaMahasiswa = new ArrayList<>();
    public ArrayList<String[]> dataPresensi = new ArrayList<>();
    public ArrayList<String[]> dataLaporan = new ArrayList<>();
    private HashMap<String, User> registeredUsers = new HashMap<>();

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private Dashboard dashboardPanel;
    private PresensiPanel presensiPanel;
    private StudentViewPanel studentViewPanel;

    private static final String DATA_MAHASISWA_CSV_FILE = "datamahasiswa.csv";
    private static final String DATA_PRESENSI_CSV_FILE = "presensi.csv";
    private static final String DATA_LAPORAN_CSV_FILE = "laporan.csv";
    private static final String USERS_CSV_FILE = "users.csv";

    public MainPanel() {

        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(Theme.BACKGROUND_DARK);

        loadUsersFromCSV();

        if (!registeredUsers.containsKey("admin")) {
            registeredUsers.put("admin", new User("admin", "123", "admin"));
            saveUsersToCSV();
        }

        loadDataMahasiswaFromCSV();
        loadDataPresensiFromCSV();
        loadDataLaporanFromCSV();

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        dashboardPanel = new Dashboard(this, dataSemuaMahasiswa, dataPresensi, dataLaporan);
        presensiPanel = new PresensiPanel(this, dataSemuaMahasiswa, dataPresensi);
        studentViewPanel = new StudentViewPanel(this, dataSemuaMahasiswa, dataPresensi);

        add(loginPanel, "login");
        add(registerPanel, "register");
        add(dashboardPanel, "dashboard");
        add(presensiPanel, "presensi");
        add(studentViewPanel, "studentView");

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
        saveUsersToCSV();

        dataSemuaMahasiswa.add(new String[]{nim, nama, LocalDate.now().toString()});
        saveDataMahasiswaToCSV();

        studentViewPanel.showStudentData(nim);
        cardLayout.show(this, "studentView");

        return "SUCCESS";
    }

    /**
     * BARU: Metode untuk menghapus mahasiswa dari semua data terkait.
     * @param nim NIM mahasiswa yang akan dihapus.
     */
    public void hapusMahasiswa(String nim) {
        // 1. Hapus dari data mahasiswa utama
        dataSemuaMahasiswa.removeIf(m -> m[0].equals(nim));
        saveDataMahasiswaToCSV();

        // 2. Hapus dari semua catatan presensi
        dataPresensi.removeIf(p -> p[0].equals(nim));
        saveDataPresensiToCSV();

        // 3. Hapus dari semua catatan laporan
        // Ingat, NIM ada di indeks ke-1 pada dataLaporan
        dataLaporan.removeIf(l -> l[1].equals(nim));
        saveDataLaporanToCSV();

        // 4. Hapus dari akun pengguna yang terdaftar
        registeredUsers.values().removeIf(user -> nim.equals(user.getNim()));
        saveUsersToCSV();

        System.out.println("Mahasiswa dengan NIM " + nim + " telah dihapus dari semua data.");
    }

    public StudentViewPanel getStudentViewPanel() {
        return studentViewPanel;
    }

    // --- Metode untuk Load dan Save Pengguna Terdaftar ---

    public void saveUsersToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_CSV_FILE, false))) {
            writer.println("username,password,role,nim");
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
            return;
        }
        registeredUsers.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Lewati header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 4) {
                    User user = new User(data[0], data[1], data[2], data[3].isEmpty() ? null : data[3]);
                    registeredUsers.put(data[0], user);
                }
            }
            System.out.println("Data pengguna berhasil dimuat dari " + USERS_CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error saat memuat data pengguna: " + e.getMessage());
        }
    }

    // --- Metode Lainnya (Tidak ada perubahan) ---
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
        } catch (IOException e) { /* ... */ }
    }

    public void saveDataMahasiswaToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_MAHASISWA_CSV_FILE, false))) {
            for (String[] data : dataSemuaMahasiswa) {
                writer.println(String.join(",", data));
            }
        } catch (IOException e) { /* ... */ }
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
        } catch (IOException e) { /* ... */ }
    }

    public void saveDataPresensiToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_PRESENSI_CSV_FILE, false))) {
            for (String[] data : dataPresensi) {
                writer.println(String.join(",", data));
            }
        } catch (IOException e) { /* ... */ }
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
        } catch (IOException e) { /* ... */ }
    }

    public void saveDataLaporanToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_LAPORAN_CSV_FILE, false))) {
            for (String[] data : dataLaporan) {
                writer.println(String.join(",", data));
            }
        } catch (IOException e) { /* ... */ }
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