package ryabchuk.sportshop.controller.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.service.UserService;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userService.getUserDtoById(userDetails.getId()));
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editUser(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userService.getUserDtoById(userDetails.getId()));
        return "profile/edit";
    }

    @PostMapping("/edit")
    public String updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute UserDto userDto) {
        userService.updateUser(userDetails.getId(), userDto);
        return "redirect:/profile";
    }
}
