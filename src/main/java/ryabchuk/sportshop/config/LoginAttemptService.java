package ryabchuk.sportshop.config;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration BLOCK_TIME = Duration.ofMinutes(15);

    private final Map<String, AttemptInfo> attemptsCache = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        AttemptInfo info = attemptsCache.getOrDefault(username, new AttemptInfo(0, null));

        int newAttempts = info.attempts + 1;
        LocalDateTime lockUntil = null;

        if (newAttempts >= MAX_ATTEMPTS) {
            lockUntil = LocalDateTime.now().plus(BLOCK_TIME);
        }

        attemptsCache.put(username, new AttemptInfo(newAttempts, lockUntil));
    }

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }

    public boolean isBlocked(String username) {
        AttemptInfo info = attemptsCache.get(username);
        if (info == null) return false;

        if (info.lockUntil != null && info.lockUntil.isAfter(LocalDateTime.now())) {
            return true;
        }

        if (info.lockUntil != null && info.lockUntil.isBefore(LocalDateTime.now())) {
            attemptsCache.remove(username);
        }

        return false;
    }

    private static class AttemptInfo {
        int attempts;
        LocalDateTime lockUntil;

        AttemptInfo(int attempts, LocalDateTime lockUntil) {
            this.attempts = attempts;
            this.lockUntil = lockUntil;
        }
    }
}
