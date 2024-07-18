package study.bulletin_board.repository.post;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import study.bulletin_board.domain.Post;
import study.bulletin_board.dto.PostSearchCond;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    void save(Post post);

    void update(@Param("postId") Long postId, @Param("post") Post post);

    void delete(Long postId);

    Optional<Post> findById(Long id);

    List<Post> findByMember(Long memberId);

    List<Post> findAll(PostSearchCond postSearch);
}
