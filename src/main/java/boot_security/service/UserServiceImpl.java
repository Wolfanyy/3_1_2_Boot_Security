package boot_security.service;

import boot_security.dao.RoleRepository;
import boot_security.dao.UserRepository;
import boot_security.exception.UserNotFoundException;
import boot_security.model.Role;
import boot_security.model.RoleName;
import boot_security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
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
        checkEmailUnique(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleService.getOrCreateRole(RoleName.USER)));
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> createAdmin(User user) {
        if (!existsByEmail(user.getEmail())) {
            Role adminRole = roleService.getOrCreateRole(RoleName.ADMIN);
            Role userRole = roleService.getOrCreateRole(RoleName.USER);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Set.of(adminRole, userRole));
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    private void checkEmailUnique(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
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
        if (newUser.getEmail() != null && !newUser.getEmail().equals(existingUser.getEmail())) {
            checkEmailUnique(newUser.getEmail());
        }
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