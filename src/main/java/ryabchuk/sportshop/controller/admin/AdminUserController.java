package ryabchuk.sportshop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ryabchuk.sportshop.model.User;
import ryabchuk.sportshop.service.UserService;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsersDto());
        return "admin/users/view";
    }

    @PostMapping("/change-role")
    public String changeRole(@RequestParam String email, @RequestParam User.Role role) {
        userService.changeRole(email, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam String email) {
        userService.deleteUserByEmail(email);
        return "redirect:/admin/users";
    }
}
