package boot_security.init;

import boot_security.model.RoleName;
import boot_security.model.User;
import boot_security.service.RoleService;
import boot_security.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final RoleService roleService;
    private final UserService userService;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.age}")
    private Integer adminAge;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        roleService.ensureRoleExists(RoleName.ADMIN);
        roleService.ensureRoleExists(RoleName.USER);

        User admin = User.builder()
                .name(adminUsername)
                .email(adminEmail)
                .password(adminPassword)
                .age(adminAge)
                .build();

        userService.createAdmin(admin)
                .ifPresent(a -> System.out.println("Admin created: " + a.getEmail()));
    }
}