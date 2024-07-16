package study.bulletin_board.service.comment;

import study.bulletin_board.domain.Comment;

public interface CommentService {

    Comment saveComment(Long memberId, Long postId, String content);

    void deleteCommentByPostId(Long postId);
}
