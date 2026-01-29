package boot_security.service;

import boot_security.dao.UserRepository;
import boot_security.exception.UserNotFoundException;
import boot_security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User " + id + " is not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User " + email + " is not found"));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private void updateEntity(User existingUser, User newUser) {
        Optional.ofNullable(newUser.getName()).ifPresent(existingUser::setName);
        Optional.ofNullable(newUser.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(newUser.getAge()).ifPresent(existingUser::setAge);
        Optional.ofNullable(newUser.getPassword()).ifPresent
                (pwd -> existingUser.setPassword(passwordEncoder.encode(pwd)));
    }

    @Override
    @Transactional
    public User updateUser(Long id, User newUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " is not found"));
        updateEntity(existingUser, newUser);
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " is not found"));
        userRepository.delete(user);
    }
}