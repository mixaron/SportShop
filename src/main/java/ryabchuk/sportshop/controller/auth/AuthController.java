package ryabchuk.sportshop.controller.auth;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.service.user.UserService;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserDto());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userDto") @Valid UserDto userDto,
                           BindingResult result,
                           @RequestParam String confirmPassword,
                           Model model,
                           RedirectAttributes redirect) {

        if (!userDto.getPassword().equals(confirmPassword)) {
            result.rejectValue("password", "error.userDto", "Пароли не совпадают");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(userDto);
            redirect.addFlashAttribute("message", "Регистрация успешна, войдите");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "error.userDto", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model) {
        try {
            userService.sendPasswordResetLink(email);
            model.addAttribute("message", "Ссылка для восстановления отправлена на почту");
        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", "Пользователь с таким email не найден");
        }
        return "auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PatchMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("token", token);
            return "auth/reset-password";
        }
        if (password.length() < 8) {
            model.addAttribute("error", "Пароль должен быть больше 8 символов");
            model.addAttribute("token", token);
            return "auth/reset-password";
        }
        try {
            userService.resetPassword(token, password);
            return "redirect:/auth/login?reset=success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            return "auth/reset-password";
        }
    }
}
