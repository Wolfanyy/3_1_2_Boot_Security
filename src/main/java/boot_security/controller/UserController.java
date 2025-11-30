package boot_security.controller;

import boot_security.model.User;
import boot_security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/users")
    public String printUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping(value = "/users/new")
    public String newUsers(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping(value = "/users/new")
    public String newUser(@Valid @ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/users";
    }

    @PatchMapping(value = "/users/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/users";
    }

    @DeleteMapping(value = "/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}