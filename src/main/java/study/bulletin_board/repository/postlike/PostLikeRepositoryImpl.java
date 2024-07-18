package study.bulletin_board.repository.postlike;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.bulletin_board.domain.PostLike;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {

    private final PostLikeMapper postLikeMapper;

    @Override
    public void save(PostLike postLike) {
        postLikeMapper.save(postLike);
    }

    @Override
    public void deleteByPostAndMember(Long postId, Long memberId) {
        postLikeMapper.deleteByPostAndMember(postId, memberId);
    }

    @Override
    public Optional<PostLike> findByPostAndMember(Long postId, Long memberId) {
        return postLikeMapper.findByPostAndMember(postId, memberId);
    }

    @Override
    public int countByPost(Long postId) {
        return postLikeMapper.countByPost(postId);
    }
}
