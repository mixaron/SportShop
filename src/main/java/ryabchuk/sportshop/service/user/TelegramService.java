package ryabchuk.sportshop.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ryabchuk.sportshop.bot.TelegramBot;
import ryabchuk.sportshop.model.user.User;

@Service
@RequiredArgsConstructor
public class TelegramService {
    private final TelegramBot telegramBot;
    private final UserService userService;

    public void notifyUserByEmail(Long userId, String message) {
        User user = userService.getUserById(userId);
        String chatId = user.getTelegramChatId();
        if (chatId != null && !chatId.isBlank()) {
            try {
                telegramBot.sendMessage(chatId, message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void unlinkTelegram(Long id) {
        User user = userService.getUserById(id);
        user.setTelegramChatId(null);
        userService.saveUser(user);
    }
}
