package models;

public class Session {
    private String username;
    private User.UserRole role;

    public Session(String username, User.UserRole role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public User.UserRole getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == User.UserRole.ADMIN;
    }

    @Override
    public String toString() {
        return String.format("Session(user=%s, role=%s)", username, role);
    }
}
