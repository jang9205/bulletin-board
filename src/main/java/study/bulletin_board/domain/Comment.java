package study.bulletin_board.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Comment {

    private Long id;
    private Member member;
    private Post post;
    private String content;
    private LocalDateTime commentDate;

    //생성 메서드
    public static Comment createComment(Member member, Post post, String content) {
        Comment comment = new Comment();
        comment.setMember(member);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCommentDate(LocalDateTime.now());
        return comment;
    }
}
