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


}
