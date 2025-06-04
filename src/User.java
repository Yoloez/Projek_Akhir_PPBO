public class User {
    private String username;
    private String password; // In a real app, this should be a hashed password

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Optional: for checking credentials
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}