package study.bulletin_board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.bulletin_board.domain.Member;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.service.postlike.PostLikeService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final MemberRepository memberRepository;

    @PostMapping("post/like/{postId}")
    public String togglePostLike(@PathVariable("postId") Long postId, RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            if (email != null) {
                Member member = memberRepository.findByEmail(email).orElse(null);

                if (member != null) {
                    postLikeService.togglePostLike(postId, member.getId());
                    redirectAttributes.addAttribute("postId", postId);
                    return "redirect:/posts/{postId}";
                } else {
                    redirectAttributes.addFlashAttribute("error", "사용자를 찾을 수 없습니다.");
                    return "redirect:/";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "이메일 정보를 찾을 수 없습니다.");
                return "redirect:/";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "로그인 정보가 없습니다.");
            return "redirect:/login";
        }
    }
}