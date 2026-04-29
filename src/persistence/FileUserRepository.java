package persistence;

import models.User;
import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final String USERS_FILE = "data/users.txt";

    @Override
    public List<User> loadAll() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);

        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromCsvString(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }

        return users;
    }

    @Override
    public boolean saveAll(List<User> users) {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(user.toCsvString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User findByUsername(String username) {
        for (User user : loadAll()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
