package study.bulletin_board.service.post;

import org.springframework.web.multipart.MultipartFile;
import study.bulletin_board.domain.Post;
import study.bulletin_board.dto.PostDto;
import study.bulletin_board.dto.PostSearchCond;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post savePost(Long memberId, PostDto postDto);

    Post updatePost(Long postId, Long memberId, Post post);

    void deletePost(Long postId);

    Optional<Post> findPostById(Long id);

    List<Post> findPostByMember(Long memberId);

    List<Post> findAllPosts(PostSearchCond postSearch);
}
