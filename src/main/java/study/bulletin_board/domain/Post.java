package study.bulletin_board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import study.bulletin_board.dto.PostDto;
import study.bulletin_board.util.TimeAgoUtil;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private Long id;
    private Member member;
    private Category category;
    private String title;
    private String content;
    private LocalDateTime postDate;
    private String picturePath;
    private MultipartFile picture;
    private List<Comment> comments;

    //생성 메서드
    public static Post createPost(Member member, PostDto postDto, Category category) {
        Post post = new Post();
        post.setMember(member);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setPicturePath(postDto.getPicturePath());
        post.setPostDate(LocalDateTime.now());
        post.setCategory(category);
        return post;
    }

    //상대 시간 반환
    public String getRelativePostDate() {
        return TimeAgoUtil.formatTimeAgo(postDate);
    }
}
