package study.bulletin_board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@Slf4j
public class MemberController {

    @GetMapping("/join")
    public String joinPage() {
        return "members/join";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "members/login";
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response != null) {
                model.addAttribute("nickname", response.get("nickname"));
                model.addAttribute("email", response.get("email"));
            } else {
                log.warn("No response attribute found in OAuth2User");
            }
        } else {
            log.warn("OAuth2User is null");
        }
        return "members/profile";
    }
}
