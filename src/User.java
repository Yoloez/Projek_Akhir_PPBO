public class User {
    private String username;
    private String password;
    // In a real app, this should be a hashed password
    private String role; // contoh: "admin", "mahasiswa"
    private String nim;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nim = null;
    }

    public User(String username, String password, String role, String nim) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nim = nim;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getNim() {
        return nim;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}