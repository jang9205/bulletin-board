package study.bulletin_board.repository.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.bulletin_board.domain.Comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentMapper commentMapper;


    @Override
    public Comment save(Comment comment) {
        commentMapper.save(comment);
        return comment;
    }

    @Override
    public void deleteByPostId(Long postId) {
        commentMapper.deleteByPostId(postId);
    }
}
