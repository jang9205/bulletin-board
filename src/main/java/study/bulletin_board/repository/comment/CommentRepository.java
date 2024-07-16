package study.bulletin_board.repository.comment;

import study.bulletin_board.domain.Comment;

public interface CommentRepository {
    Comment save(Comment comment);

    void deleteByPostId(Long postId);
}
