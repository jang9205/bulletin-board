package study.bulletin_board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.bulletin_board.domain.Category;
import study.bulletin_board.domain.Member;
import study.bulletin_board.domain.Post;
import study.bulletin_board.domain.PostLike;
import study.bulletin_board.dto.MemberSaveDto;
import study.bulletin_board.dto.PostDto;
import study.bulletin_board.repository.category.CategoryRepository;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.repository.post.PostRepository;
import study.bulletin_board.repository.postlike.PostLikeRepository;
import study.bulletin_board.service.post.PostService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class PostLikeRepositoryTest {

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @MockBean
    PostService postService;

    @Test
    void savePostLike() {
        //given
        byte[] fileBytes = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("testFile", "test.txt",
                "text/plain", fileBytes);

        MemberSaveDto saveDto = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member = memberRepository.save(Member.saveMember(saveDto));
        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        PostDto postDto = new PostDto("hi", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        Post post = postRepository.save(Post.createPost(member, postDto, category));

        //when
        PostLike postLike = PostLike.createPostLike(member, post);
        postLikeRepository.save(postLike);
        int likeCount = postLikeRepository.countByPost(post.getId());

        //then
        assertThat(likeCount).isEqualTo(1);
    }

    @Test
    void deletePostLike() {
        //given
        byte[] fileBytes = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("testFile", "test.txt",
                "text/plain", fileBytes);

        MemberSaveDto saveDto = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member = memberRepository.save(Member.saveMember(saveDto));
        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        PostDto postDto = new PostDto("hi", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        Post post = postRepository.save(Post.createPost(member, postDto, category));

        //when
        PostLike postLike = PostLike.createPostLike(member, post);
        postLikeRepository.save(postLike);
        int firstCount = postLikeRepository.countByPost(post.getId());

        postLikeRepository.deleteByPostAndMember(post.getId(), member.getId());
        int secondCount = postLikeRepository.countByPost(post.getId());

        //then
        assertThat(firstCount).isEqualTo(1);
        assertThat(secondCount).isEqualTo(0);
    }

    @Test
    void findByPostAndMember() {
        //given
        byte[] fileBytes = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("testFile", "test.txt",
                "text/plain", fileBytes);

        MemberSaveDto saveDto = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member = memberRepository.save(Member.saveMember(saveDto));
        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        PostDto postDto = new PostDto("hi", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        Post post = postRepository.save(Post.createPost(member, postDto, category));

        //when
        Optional<PostLike> firstFind = postLikeRepository.findByPostAndMember(post.getId(), member.getId());

        PostLike postLike = PostLike.createPostLike(member, post);
        postLikeRepository.save(postLike);

        Optional<PostLike> secondFind = postLikeRepository.findByPostAndMember(post.getId(), member.getId());

        //then
        assertThat(firstFind).isEmpty();
        assertThat(secondFind).isPresent();
    }
}
