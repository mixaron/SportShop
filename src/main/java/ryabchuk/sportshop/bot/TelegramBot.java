package ryabchuk.sportshop.bot;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ryabchuk.sportshop.repository.user.UserRepository;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramWebhookBot {

    private final UserRepository userRepository;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.webhook-path}")
    private String webhookPath;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webhookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return null;
        }

        String chatId = update.getMessage().getChatId().toString();
        String messageText = update.getMessage().getText();

        try {
            if (messageText.equals("/start")) {
                return new SendMessage(chatId, "Добро пожаловать! Чтобы привязать аккаунт, используйте /link <email>.");
            } else if (messageText.startsWith("/link")) {
                String[] parts = messageText.split(" ");
                if (parts.length < 2) {
                    return new SendMessage(chatId, "Пожалуйста, укажите email: /link your@email.com");
                }
                String email = parts[1];
                return userRepository.findByEmail(email)
                        .map(user -> {
                            user.setTelegramChatId(chatId);
                            userRepository.save(user);
                            return new SendMessage(chatId, "Аккаунт успешно привязан!");
                        })
                        .orElse(new SendMessage(chatId, "Пользователь с email " + email + " не найден."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SendMessage(chatId, "Произошла ошибка, попробуйте позже.");
        }
        return null;
    }

    public void sendMessage(String chatId, String text) throws TelegramApiException {
        execute(new SendMessage(chatId, text));
    }
}
