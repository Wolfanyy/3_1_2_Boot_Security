package boot_security.controller;

import boot_security.model.User;
import boot_security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping
    public String printUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping(value = "/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping(value = "/new")
    public String createUser(@Valid @ModelAttribute("user") User user,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new";
        }
        userService.createUser(user);
        return "redirect:/admin/users";
    }

    @PatchMapping(value = "/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "users";
        }
        userService.updateUser(id, user);
        return "redirect:/admin/users";
    }

    @DeleteMapping(value = "/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}