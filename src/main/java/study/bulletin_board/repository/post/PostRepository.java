package study.bulletin_board.repository.post;

import study.bulletin_board.domain.Post;
import study.bulletin_board.dto.PostSearchCond;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    void update(Long postId, Post post);

    void delete(Long postId);

    Optional<Post> findById(Long id);

    List<Post> findByMember(Long memberId);

    List<Post> findAll(PostSearchCond postSearch);
}
