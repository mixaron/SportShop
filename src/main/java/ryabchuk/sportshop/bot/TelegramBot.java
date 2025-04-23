package ryabchuk.sportshop.bot;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ryabchuk.sportshop.repository.user.UserRepository;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String chatId = update.getMessage().getChatId().toString();
        String messageText = update.getMessage().getText();

        try {
            if (messageText.equals("/start")) {
                sendMessage(chatId, "Добро пожаловать! Чтобы привязать аккаунт, используйте /link <email>.");
            } else if (messageText.startsWith("/link")) {
                String[] parts = messageText.split(" ");
                if (parts.length < 2) {
                    sendMessage(chatId, "Пожалуйста, укажите email: /link your@email.com");
                    return;
                }
                String email = parts[1];
                userRepository.findByEmail(email)
                        .ifPresentOrElse(user -> {
                            user.setTelegramChatId(chatId);
                            userRepository.save(user);
                            try {
                                sendMessage(chatId, "Аккаунт успешно привязан!");
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }, () -> {
                            try {
                                sendMessage(chatId, "Пользователь с email " + email + " не найден.");
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        });
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }
}