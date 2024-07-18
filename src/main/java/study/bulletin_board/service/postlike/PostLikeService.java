package study.bulletin_board.service.postlike;

public interface PostLikeService {

    void togglePostLike(Long postId, Long memberId);

    int countLikes(Long postId);
}
