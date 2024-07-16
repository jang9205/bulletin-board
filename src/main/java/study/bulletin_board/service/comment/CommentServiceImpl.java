package study.bulletin_board.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.bulletin_board.domain.Comment;
import study.bulletin_board.domain.Member;
import study.bulletin_board.domain.Post;
import study.bulletin_board.repository.comment.CommentRepository;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.repository.post.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public Comment saveComment(Long memberId, Long postId, String content) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));

        Comment comment = Comment.createComment(member, post, content);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteCommentByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }
}
