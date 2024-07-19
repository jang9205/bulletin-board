package study.bulletin_board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.bulletin_board.domain.Member;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.service.comment.CommentService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @PostMapping("/comment/save/{postId}")
    public String saveComment(@PathVariable("postId") Long postId, @RequestParam("content") String content,
                              RedirectAttributes redirectAttributes, @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (content.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "댓글 내용을 입력해주세요.");
            return "redirect:/posts/{postId}";
        }
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            if (email != null) {
                Member member = memberRepository.findByEmail(email).orElse(null);

                if (member != null) {
                    commentService.saveComment(member.getId(), postId, content);
                    redirectAttributes.addAttribute("postId", postId);
                    redirectAttributes.addFlashAttribute("successMessage", "댓글이 등록되었습니다.");
                    log.info("New Comment- content : {}, member ID : {}, post ID : {}",
                            content, member.getId(), postId);
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
