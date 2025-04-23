package ryabchuk.sportshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.mapper.UserMapper;
import ryabchuk.sportshop.model.user.User;
import ryabchuk.sportshop.repository.user.UserRepository;
import ryabchuk.sportshop.service.user.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JavaMailSender mailSender;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("pass");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");

        userService.register(dto);

        verify(userRepository).save(user);
        assertEquals("hashed", user.getPassword());
    }

    @Test
    void shouldThrowExceptionIfEmailExists() {
        UserDto dto = new UserDto();
        dto.setEmail("exists@example.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.register(dto));
    }

    @Test
    void shouldSendPasswordResetLinkSuccessfully() {
        String email = "reset@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.sendPasswordResetLink(email);

        assertNotNull(user.getResetToken());
        assertNotNull(user.getResetTokenExpiry());
        verify(userRepository).save(user);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldThrowIfEmailNotFoundForReset() {
        String email = "unknown@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.sendPasswordResetLink(email));
    }

    @Test
    void shouldResetPasswordSuccessfully() {
        String token = "valid-token";
        User user = new User();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10));

        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("hashed");

        userService.resetPassword(token, "newpass");

        assertEquals("hashed", user.getPassword());
        assertNull(user.getResetToken());
        assertNull(user.getResetTokenExpiry());
        verify(userRepository).save(user);
    }

    @Test
    void shouldFailResetIfTokenExpired() {
        String token = "expired";
        User user = new User();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().minusMinutes(1));

        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.resetPassword(token, "any"));
    }

    @Test
    void shouldFailResetIfTokenInvalid() {
        when(userRepository.findByResetToken("invalid")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.resetPassword("invalid", "any"));
    }
}
