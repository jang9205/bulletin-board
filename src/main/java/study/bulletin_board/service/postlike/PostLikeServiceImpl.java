package study.bulletin_board.service.postlike;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.bulletin_board.domain.Member;
import study.bulletin_board.domain.Post;
import study.bulletin_board.domain.PostLike;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.repository.post.PostRepository;
import study.bulletin_board.repository.postlike.PostLikeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    public void togglePostLike(Long postId, Long memberId) {
        Optional<PostLike> existingLike = postLikeRepository.findByPostAndMember(postId, memberId);
        if (existingLike.isPresent()) {
            postLikeRepository.deleteByPostAndMember(postId, memberId);
        } else {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다."));
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

            PostLike postLike = PostLike.createPostLike(member, post);
            postLikeRepository.save(postLike);
        }
    }

    @Override
    public int countLikes(Long postId) {
        return postLikeRepository.countByPost(postId);
    }
}
