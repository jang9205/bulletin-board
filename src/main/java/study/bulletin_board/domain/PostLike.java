package study.bulletin_board.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostLike {

    private Long id;
    private Member member;
    private Post post;
    private LocalDateTime likeDate;

    //생성 메서드
    public static PostLike createPostLike(Member member, Post post) {
        PostLike postLike = new PostLike();
        postLike.setMember(member);
        postLike.setPost(post);
        postLike.setLikeDate(LocalDateTime.now());
        return postLike;
    }
}
