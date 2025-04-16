package ryabchuk.sportshop.config.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ryabchuk.sportshop.model.User;
import ryabchuk.sportshop.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oauthUser.getAttribute("sub");

        String email;

        if ("google".equals(provider)) {
            email = oauthUser.getAttribute("email");
            providerId = oauthUser.getAttribute("sub");
        } else if ("github".equals(provider)) {
            email = oauthUser.getAttribute("email");
            if (email == null) {
                email = oauthUser.getAttribute("login") + "@github.local";
            }
            providerId = oauthUser.getAttribute("id").toString();
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setRole(User.Role.USER);
            user.setCreatedAt(LocalDateTime.now());

            userRepository.save(user);
        }

        return new CustomUserDetails(user, oauthUser.getAttributes());
    }
    private String getNameAttributeKey(String provider, OAuth2User user) {
        return switch (provider) {
            case "github" -> user.getAttribute("email") != null ? "email" : "login";
            default -> "email";
        };
    }
}
