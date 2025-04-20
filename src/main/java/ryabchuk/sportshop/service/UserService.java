package ryabchuk.sportshop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.mapper.UserMapper;
import ryabchuk.sportshop.model.User;
import ryabchuk.sportshop.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${spring.reset.password-link}")
    private String resetPasswordBaseLink;

    public void register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Пользователь уже существует");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public void sendPasswordResetLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        String resetLink = resetPasswordBaseLink + token;
        sendEmail(user.getEmail(), "Password Reset", "Click to reset: " + resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserDto getUserDtoById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        updateEmailIfChanged(user, userDto);
        updatePasswordIfChanged(user, userDto);
        updateName(user, userDto);

        userRepository.save(user);
    }

    private void updateEmailIfChanged(User user, UserDto userDto) {
        if (!user.getEmail().equals(userDto.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email is already in use");
            }
            user.setEmail(userDto.getEmail());
        }
    }

    private void updatePasswordIfChanged(User user, UserDto userDto) {
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            if (!passwordEncoder.matches(user.getPassword(), userDto.getCurrentPassword())) {
                throw new IllegalArgumentException("Password not the same");
            }
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
    }

    private void updateName(User user, UserDto userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
