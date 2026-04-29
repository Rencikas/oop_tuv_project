package services;

import models.User;
import models.Session;
import persistence.UserRepository;
import persistence.FileUserRepository;
import util.EncryptionUtil;
import java.util.List;

public class AuthenticationService {
    private UserRepository userRepository;

    public AuthenticationService() {
        this.userRepository = new FileUserRepository();
    }

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            return false;
        }

        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        String encryptedUsername = EncryptionUtil.encrypt(username);
        String encryptedPassword = EncryptionUtil.encrypt(password);

        User newUser = new User(encryptedUsername, encryptedPassword, User.UserRole.USER);

        List<User> users = userRepository.loadAll();
        users.add(newUser);

        return userRepository.saveAll(users);
    }

    public Session login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String encryptedUsername = EncryptionUtil.encrypt(username);
        String encryptedPassword = EncryptionUtil.encrypt(password);

        List<User> users = userRepository.loadAll();
        for (User user : users) {
            if (user.getUsername().equals(encryptedUsername) &&
                user.getPasswordHash().equals(encryptedPassword)) {
                return new Session(username, user.getRole());
            }
        }

        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.loadAll();
    }

    public boolean updateUserRole(String encryptedUsername, User.UserRole newRole) {
        List<User> users = userRepository.loadAll();
        for (User user : users) {
            if (user.getUsername().equals(encryptedUsername)) {
                user.setRole(newRole);
                return userRepository.saveAll(users);
            }
        }
        return false;
    }

    public String getDisplayUsername(String encryptedUsername) {
        return EncryptionUtil.decrypt(encryptedUsername);
    }
}
