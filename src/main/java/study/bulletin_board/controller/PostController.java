package study.bulletin_board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.bulletin_board.domain.Member;
import study.bulletin_board.domain.Post;
import study.bulletin_board.dto.PostDto;
import study.bulletin_board.dto.PostSearchCond;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.service.category.CategoryService;
import study.bulletin_board.service.comment.CommentService;
import study.bulletin_board.service.post.PostService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final MemberRepository memberRepository;
    private final CommentService commentService;

    @GetMapping("/")
    public String index(@RequestParam(value = "searchType", required = false) String searchType,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @ModelAttribute("postSearch") PostSearchCond postSearch, Model model) {
        if ("memberName".equals(searchType)) {
            postSearch.setMemberName(keyword);
        } else if ("postTitle".equals(searchType)) {
            postSearch.setPostTitle(keyword);
        }

        int pageSize = 20;
        int offset = (page - 1) * pageSize;

        List<Post> posts = postService.findAllPosts(postSearch, pageSize, offset);
        int totalPosts = postService.countAllPosts(postSearch);
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "index";
    }

    @GetMapping("/posts/{postId}")
    public String post(@PathVariable("postId") Long postId, Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        Post post = postService.findPostById(postId).orElse(null);
        if (post == null) {
            throw new IllegalArgumentException("Invalid post ID: " + postId);
        }

        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            if (email != null) {
                Member member = memberRepository.findByEmail(email).orElse(null);
                if (member != null) {
                    model.addAttribute("loggedInMemberId", member.getId());
                }
            }
        }
        model.addAttribute("post", post);
        return "posts/post";
    }

    @GetMapping("/write/new")
    public String savePostForm(@ModelAttribute("postDto") PostDto postDto, Model model) {
        List<String> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "posts/saveForm";
    }

    @PostMapping("/write/new")
    public String savePost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes, @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (result.hasErrors()) {
            List<String> categories = categoryService.findAllCategories();
            model.addAttribute("categories", categories);
            return "posts/saveForm";
        }
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            if (email != null) {
                Member member = memberRepository.findByEmail(email).orElse(null);

                if (member != null) {
                    Post post = postService.savePost(member.getId(), postDto);
                    redirectAttributes.addAttribute("postId", post.getId());
                    redirectAttributes.addFlashAttribute("successMessage", "게시물이 등록되었습니다.");
                    log.info("New post- title: {}, content: {}, by member ID: {}",
                            post.getTitle(), post.getContent(), post.getMember().getId());
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

    @GetMapping("/posts/update/{postId}")
    public String updateForm(@PathVariable("postId") Long postId,
                             Model model, @AuthenticationPrincipal OAuth2User oAuth2User, RedirectAttributes redirectAttributes) {
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            if (email != null) {
                Member member = memberRepository.findByEmail(email).orElse(null);

                if (member != null) {
                    Post post = postService.findPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));

                    if (post.getMember().getId().equals(member.getId())) {
                        model.addAttribute("post", post);
                        return "posts/updateForm";
                    } else {
                        redirectAttributes.addFlashAttribute("error", "게시물 수정 권한이 없습니다.");
                        return "redirect:/";
                    }
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

    @PostMapping("/posts/update/{postId}")
    public String updatePost(@Valid @ModelAttribute("post") Post post, BindingResult result, @PathVariable("postId") Long postId,
                             RedirectAttributes redirectAttributes, @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (result.hasErrors()) {
            return "posts/updateForm";
        }
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            if (email != null) {
                Member member = memberRepository.findByEmail(email).orElse(null);

                if (member != null) {
                    Post findPost = postService.findPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));

                    if (findPost.getMember().getId().equals(member.getId())) {
                        postService.updatePost(findPost.getId(), member.getId(), post);
                        redirectAttributes.addAttribute("postId", findPost.getId());
                        redirectAttributes.addFlashAttribute("successMessage", "게시물이 수정되었습니다.");
                        log.info("Post updated- new title: {}, new content: {}, by member ID: {}",
                                post.getTitle(), post.getContent(), post.getMember().getId());
                        return "redirect:/posts/{postId}";
                    } else {
                        redirectAttributes.addFlashAttribute("error", "게시물 수정 권한이 없습니다.");
                        return "redirect:/";
                    }
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

    @PostMapping("/posts/delete/{postId}")
    public String deletePost(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("postId") Long postId,
                             RedirectAttributes redirectAttributes) {
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            if (email != null) {
                Member member = memberRepository.findByEmail(email).orElse(null);

                if (member != null) {
                    Post findPost = postService.findPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));

                    if (findPost.getMember().getId().equals(member.getId())) {
                        commentService.deleteCommentByPostId(postId);
                        postService.deletePost(postId);
                        redirectAttributes.addFlashAttribute("successMessage", "게시물이 삭제되었습니다.");
                    } else {
                        redirectAttributes.addFlashAttribute("error", "게시물 삭제 권한이 없습니다.");
                    }
                } else {
                    redirectAttributes.addFlashAttribute("error", "사용자를 찾을 수 없습니다.");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "이메일 정보를 찾을 수 없습니다.");
            }
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "로그인 정보가 없습니다.");
            return "redirect:/login";
        }
    }

    @GetMapping("/posts/list")
    public String postLis(@AuthenticationPrincipal OAuth2User oAuth2User, Model model, RedirectAttributes redirectAttributes) {
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");

            if (email != null) {
                Member member = memberRepository.findByEmail(email).orElse(null);

                if (member != null) {
                    List<Post> posts = postService.findPostByMember(member.getId());
                    model.addAttribute("posts", posts);
                    return "posts/postList";
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


