package ryabchuk.sportshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryabchuk.sportshop.controller.auth.AuthController;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.service.user.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldReturnRegisterPage() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .with(csrf()) //
                        .param("email", "test@example.com")
                        .param("password", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void shouldRegisterUserAndRedirect() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .with(csrf()) //
                        .param("email", "test@example.com")
                        .param("password", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));

        verify(userService).register(any(UserDto.class));
    }

    @Test
    void shouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/auth/login")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void shouldReturnForgotPasswordPage() throws Exception {
        mockMvc.perform(get("/auth/forgot-password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password"));
    }

    @Test
    void shouldSendResetLinkSuccessfully() throws Exception {
        mockMvc.perform(post("/auth/forgot-password")
                        .with(csrf())
                        .param("email", "reset@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password"))
                .andExpect(model().attributeExists("message"));

        verify(userService).sendPasswordResetLink("reset@example.com");
    }

    @Test
    void shouldHandleEmailNotFoundOnReset() throws Exception {
        doThrow(new UsernameNotFoundException("not found"))
                .when(userService).sendPasswordResetLink("fail@example.com");

        mockMvc.perform(post("/auth/forgot-password")
                        .with(csrf())
                        .param("email", "fail@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void shouldReturnResetPasswordPageWithToken() throws Exception {
        mockMvc.perform(get("/auth/reset-password")
                        .with(csrf())
                        .param("token", "abc123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-password"))
                .andExpect(model().attribute("token", "abc123"));
    }

    @Test
    void shouldResetPasswordSuccessfully() throws Exception {
        mockMvc.perform(post("/auth/reset-password")
                        .with(csrf())
                        .param("token", "abc")
                        .param("password", "newpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?reset=success"));

        verify(userService).resetPassword("abc", "newpass");
    }

    @Test
    void shouldHandleInvalidResetToken() throws Exception {
        doThrow(new IllegalArgumentException("Invalid token"))
                .when(userService).resetPassword("invalid", "new");

        mockMvc.perform(post("/auth/reset-password")
                        .with(csrf())
                        .param("token", "invalid")
                        .param("password", "new"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-password"))
                .andExpect(model().attribute("error", "Invalid token"));
    }
}
