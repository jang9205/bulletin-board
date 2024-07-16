package study.bulletin_board.repository.comment;

import org.apache.ibatis.annotations.Mapper;
import study.bulletin_board.domain.Comment;

@Mapper
public interface CommentMapper {

    void save(Comment comment);

    void deleteByPostId(Long postId);
}
