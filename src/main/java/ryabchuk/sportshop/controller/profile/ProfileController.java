package ryabchuk.sportshop.controller.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.service.user.UserService;
import ryabchuk.sportshop.util.exception.EmailAlreadyUsedException;
import ryabchuk.sportshop.util.exception.InvalidPasswordException;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("userDto", userService.getUserDtoById(userDetails.getId()));
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editUser(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("userDto", userService.getUserDtoById(userDetails.getId()));
        return "profile/edit";
    }

    @PatchMapping("/edit")
    public String updateUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @ModelAttribute @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "profile/edit";
        }

        try {
            userService.updateUser(userDetails.getId(), userDto);
        } catch (EmailAlreadyUsedException e) {
            result.rejectValue("email", "error.email", e.getMessage());
            return "profile/edit";
        } catch (InvalidPasswordException e) {
            result.rejectValue("currentPassword", "error.password", e.getMessage());
            return "profile/edit";
        }

        return "redirect:/profile";
    }
}
