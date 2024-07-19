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
import study.bulletin_board.domain.Comment;
import study.bulletin_board.domain.Member;
import study.bulletin_board.domain.Post;
import study.bulletin_board.dto.MemberSaveDto;
import study.bulletin_board.dto.PostDto;
import study.bulletin_board.repository.category.CategoryRepository;
import study.bulletin_board.repository.comment.CommentRepository;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.repository.post.PostRepository;
import study.bulletin_board.service.post.PostService;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class CommentRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @MockBean
    PostService postService;

    @Test
    void saveComment() {
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
        Comment comment = commentRepository.save(Comment.createComment(member, post, "하하하하"));

        //then
        assertThat(comment.getMember().getNickname()).isEqualTo(member.getNickname());
        assertThat(comment.getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(comment.getContent()).isEqualTo("하하하하");
        assertThat(comment.getPost().getTitle()).isEqualTo(post.getTitle());
        assertThat(comment.getMember().getId()).isEqualTo(member.getId());
        assertThat(comment.getPost().getId()).isEqualTo(post.getId());
    }

    @Test
    void deleteComment() {
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
        Comment comment = commentRepository.save(Comment.createComment(member, post, "하하하하"));

        //when
        commentRepository.deleteByPostId(post.getId());

        //then
        Post searchPost = postRepository.findById(post.getId()).get();
        assertThat(searchPost.getComments()).doesNotContain(comment);
    }
}
