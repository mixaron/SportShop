package ryabchuk.sportshop.controller.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ryabchuk.sportshop.bot.TelegramBot;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.service.user.TelegramService;

@Controller
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramController {
    private final TelegramService telegramService;
    private final TelegramBot telegramBot;

    @PostMapping("/webhook")
    @ResponseBody
    public BotApiMethod<?> handleWebhook(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }

    @PostMapping("/unlink-telegram")
    public String unlinkTelegram(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        telegramService.unlinkTelegram(customUserDetails.getId());
        return "redirect:/profile";
    }
}
