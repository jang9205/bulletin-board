package study.bulletin_board.repository.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.bulletin_board.domain.Post;
import study.bulletin_board.dto.PostSearchCond;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostMapper postMapper;

    @Override
    public Post save(Post post) {
        postMapper.save(post);
        return post;
    }

    @Override
    public void update(Long postId, Post post) {
        postMapper.update(postId, post);
    }

    @Override
    public void delete(Long postId) {
        postMapper.delete(postId);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postMapper.findById(id);
    }

    @Override
    public List<Post> findByMember(Long memberId) {
        return postMapper.findByMember(memberId);
    }

    @Override
    public List<Post> findAll(PostSearchCond postSearch) {
        return postMapper.findAll(postSearch);
    }
}
