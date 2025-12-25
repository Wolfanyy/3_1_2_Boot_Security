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
@RequestMapping("/admin/users")
public class AdminController {
    private final UserService userService;

    @GetMapping
    public String printUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping(value = "/new")
    public String newUsers(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping(value = "/new")
    public String newUser(@Valid @ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/users";
    }

    @PatchMapping(value = "/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/users";
    }

    @DeleteMapping(value = "/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}