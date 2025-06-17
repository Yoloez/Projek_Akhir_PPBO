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
    // MODIFIKASI: Pisahkan data mahasiswa dengan data presensi
    public ArrayList<String[]> dataSemuaMahasiswa = new ArrayList<>(); // [NIM, Nama, TanggalDaftar]
    public ArrayList<String[]> dataPresensi = new ArrayList<>(); // [NIM, Tanggal, Status]

    private HashMap<String, User> registeredUsers = new HashMap<>();

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private Dashboard dashboardPanel;
    private PresensiPanel presensiPanel;
    private StudentViewPanel studentViewPanel;

    // MODIFIKASI: Nama file CSV dipisahkan
    private static final String DATA_MAHASISWA_CSV_FILE = "datamahasiswa.csv";
    private static final String DATA_PRESENSI_CSV_FILE = "presensi.csv";

    public MainPanel() {
        registeredUsers.put("admin", new User("admin", "123")); // Default admin

        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(Theme.BACKGROUND_DARK);

        // MODIFIKASI: Muat kedua jenis data dari CSV
        loadDataMahasiswaFromCSV();
        loadDataPresensiFromCSV();

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        // MODIFIKASI: Teruskan kedua list data ke panel yang memerlukan
        dashboardPanel = new Dashboard(this, dataSemuaMahasiswa, dataPresensi);
        presensiPanel = new PresensiPanel(this, dataSemuaMahasiswa, dataPresensi);
        // StudentViewPanel sekarang juga butuh data presensi
        studentViewPanel = new StudentViewPanel(this, dataSemuaMahasiswa, dataPresensi);


        add(loginPanel, "login");
        add(registerPanel, "register");
        add(dashboardPanel, "dashboard");
        add(presensiPanel, "presensi");
        add(studentViewPanel, "studentView");

        cardLayout.show(this, "login");
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
        return true;
    }

    // --- Metode untuk Operasi CSV ---

    // MODIFIKASI: Hanya memuat data dasar mahasiswa
    private void loadDataMahasiswaFromCSV() {
        File file = new File(DATA_MAHASISWA_CSV_FILE);
        if (!file.exists()) {
            System.out.println("File " + DATA_MAHASISWA_CSV_FILE + " tidak ditemukan.");
            return;
        }
        dataSemuaMahasiswa.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 3) { // NIM, Nama, TanggalDaftar
                    dataSemuaMahasiswa.add(data);
                }
            }
            System.out.println("Data mahasiswa berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Error saat memuat data mahasiswa dari CSV: " + e.getMessage());
        }
    }

    // BARU: Metode untuk memuat data presensi
    private void loadDataPresensiFromCSV() {
        File file = new File(DATA_PRESENSI_CSV_FILE);
        if (!file.exists()) {
            System.out.println("File " + DATA_PRESENSI_CSV_FILE + " tidak ditemukan.");
            return;
        }
        dataPresensi.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 3) { // NIM, Tanggal, Status
                    dataPresensi.add(data);
                }
            }
            System.out.println("Data presensi berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Error saat memuat data presensi dari CSV: " + e.getMessage());
        }
    }

    // MODIFIKASI: Hanya menyimpan data dasar mahasiswa
    public void saveDataMahasiswaToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_MAHASISWA_CSV_FILE, false))) {
            for (String[] data : dataSemuaMahasiswa) {
                writer.println(String.join(",", data));
            }
            System.out.println("Data mahasiswa berhasil disimpan.");
        } catch (IOException e) {
            System.err.println("Error saat menyimpan data mahasiswa ke CSV: " + e.getMessage());
        }
    }

    // BARU: Metode untuk menyimpan data presensi
    public void saveDataPresensiToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_PRESENSI_CSV_FILE, false))) {
            for (String[] data : dataPresensi) {
                writer.println(String.join(",", data));
            }
            System.out.println("Data presensi berhasil disimpan.");
        } catch (IOException e) {
            System.err.println("Error saat menyimpan data presensi ke CSV: " + e.getMessage());
        }
    }

    // BARU: Metode untuk memperbarui atau menambahkan data presensi
    public void updatePresensi(String nim, LocalDate date, String status) {
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        boolean found = false;
        // Cari jika sudah ada data presensi untuk nim dan tanggal tsb
        for (String[] presensi : dataPresensi) {
            if (presensi[0].equals(nim) && presensi[1].equals(dateString)) {
                presensi[2] = status; // Update status
                found = true;
                break;
            }
        }
        // Jika tidak ditemukan, tambahkan data baru
        if (!found) {
            dataPresensi.add(new String[]{nim, dateString, status});
        }
        // Simpan perubahan ke CSV
        saveDataPresensiToCSV();
    }

    // BARU: Mendapatkan status presensi untuk tanggal tertentu dalam bentuk Map
    public Map<String, String> getPresensiForDate(LocalDate date) {
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return dataPresensi.stream()
                .filter(p -> p[1].equals(dateString))
                .collect(Collectors.toMap(p -> p[0], p -> p[2]));
    }
    public void showPage(String name) {
        if (name.equals("presensi")) {
            // MODIFIKASI: Panggil metode resetToToday agar tampilan selalu fresh
            presensiPanel.resetToToday();
        } else if (name.equals("dashboard")) {
            dashboardPanel.refreshDashboardView(LocalDate.now());
        } else if (name.equals("studentView")) {
            studentViewPanel.refreshView();
        }
        cardLayout.show(this, name);
    }


}