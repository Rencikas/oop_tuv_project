package models;

public class User {
    private String username;
    private String passwordHash;
    private UserRole role;

    public enum UserRole {
        USER, ADMIN
    }

    public User(String username, String passwordHash, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String toCsvString() {
        return String.format("%s %s %s", username, passwordHash, role.name());
    }

    public static User fromCsvString(String csvLine) {
        String[] parts = csvLine.split(" ");
        if (parts.length != 3) {
            return null;
        }

        try {
            UserRole role = UserRole.valueOf(parts[2]);
            return new User(parts[0], parts[1], role);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("User(username=%s, role=%s)", username, role);
    }
}
