package boot_security.service;

import boot_security.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User findByEmail(String email);

    User createUser(User user);

    Optional<User> createAdmin(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}