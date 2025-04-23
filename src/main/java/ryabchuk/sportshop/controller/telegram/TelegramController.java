package ryabchuk.sportshop.controller.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.service.user.TelegramService;

@Controller
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramController {
    private final TelegramService telegramService;

    @PostMapping("/unlink-telegram")
    public String unlinkTelegram(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        telegramService.unlinkTelegram(customUserDetails.getId());
        return "redirect:/profile";
    }
}
