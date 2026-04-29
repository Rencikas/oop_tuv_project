package persistence;

import models.User;
import java.util.List;

public interface UserRepository {
    List<User> loadAll();
    boolean saveAll(List<User> users);
    User findByUsername(String username);
}
