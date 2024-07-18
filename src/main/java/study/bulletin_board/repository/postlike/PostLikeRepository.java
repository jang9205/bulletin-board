package study.bulletin_board.repository.postlike;

import study.bulletin_board.domain.PostLike;

import java.util.Optional;

public interface PostLikeRepository {

    void save(PostLike postLike);

    void deleteByPostAndMember(Long postId, Long memberId);

    Optional<PostLike> findByPostAndMember(Long postId, Long memberId);

    int countByPost(Long postId);
}
