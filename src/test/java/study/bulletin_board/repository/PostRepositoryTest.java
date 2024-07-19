package study.bulletin_board.repository;

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
import study.bulletin_board.dto.MemberSaveDto;
import study.bulletin_board.dto.PostDto;
import study.bulletin_board.dto.PostSearchCond;
import study.bulletin_board.repository.category.CategoryRepository;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.repository.post.PostRepository;
import study.bulletin_board.service.post.PostService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @MockBean
    PostService postService;

    @Test
    void savePost() {
        //given
        //가짜 파일 데이터 생성
        byte[] fileBytes = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("testFile", "test.txt",
                "text/plain", fileBytes);

        MemberSaveDto saveDto = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member = memberRepository.save(Member.saveMember(saveDto));
        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        //when
        PostDto postDto = new PostDto("hi", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        Post post = postRepository.save(Post.createPost(member, postDto, category));

        //then
        assertThat(post.getMember().getEmail()).isEqualTo(saveDto.getEmail());
        assertThat(post.getMember().getNickname()).isEqualTo(saveDto.getNickname());
        assertThat(post.getCategory().getCategory()).isEqualTo(category.getCategory());
        assertThat(post.getTitle()).isEqualTo(postDto.getTitle());
        assertThat(post.getContent()).isEqualTo(postDto.getContent());
        assertThat(post.getPicturePath()).isEqualTo(postDto.getPicturePath());
    }

    @Test
    void updatePost() {
        //given
        byte[] fileBytes = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("testFile", "test.txt",
                "text/plain", fileBytes);

        MemberSaveDto saveDto = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member = memberRepository.save(Member.saveMember(saveDto));
        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        PostDto postDto1 = new PostDto("hi", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        Post post = postRepository.save(Post.createPost(member, postDto1, category));

        PostDto postDto2 = new PostDto("hi111", "aaaaaa", "유머", "/img/aaa.jpg", multipartFile);
        Post changePost = post.createPost(member, postDto2, category);

        //when
        postRepository.update(post.getId(), changePost);
        Post searchPost = postRepository.findById(post.getId()).get();

        //then
        assertThat(searchPost.getTitle()).isEqualTo(changePost.getTitle());
        assertThat(searchPost.getContent()).isEqualTo(changePost.getContent());
    }

    @Test
    void findAllPosts() {
        byte[] fileBytes = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("testFile", "test.txt",
                "text/plain", fileBytes);

        MemberSaveDto saveDto1 = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member1 = memberRepository.save(Member.saveMember(saveDto1));
        MemberSaveDto saveDto2 = new MemberSaveDto("jang", "ab1234@naver.com");
        Member member2 = memberRepository.save(Member.saveMember(saveDto2));

        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        PostDto postDto1 = new PostDto("안녕하세요", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        PostDto postDto2 = new PostDto("안녕", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        PostDto postDto3 = new PostDto("아아아", "aaa", "유머", "/img/aaa.jpg", multipartFile);

        Post post1 = postRepository.save(Post.createPost(member1, postDto1, category));
        Post post2 = postRepository.save(Post.createPost(member2, postDto2, category));
        Post post3 = postRepository.save(Post.createPost(member1, postDto3, category));

        postSearch(null, null, post1, post2, post3);
        postSearch("", "", post1, post2, post3);

        postSearch("an", null, post2);
        postSearch("ki", "", post1, post2);

        postSearch(null, "안녕", post1, post2);
        postSearch("", "녕", post1, post2);

        postSearch("jang", "안녕", post2);
        postSearch("kim", "아", post3);
        postSearch("kim", "안녕", post2);
    }

    @Test
    void findByMember() {
        byte[] fileBytes = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("testFile", "test.txt",
                "text/plain", fileBytes);

        MemberSaveDto saveDto1 = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member1 = memberRepository.save(Member.saveMember(saveDto1));
        MemberSaveDto saveDto2 = new MemberSaveDto("jang", "ab1234@naver.com");
        Member member2 = memberRepository.save(Member.saveMember(saveDto2));

        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        PostDto postDto1 = new PostDto("안녕하세요", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        PostDto postDto2 = new PostDto("안녕", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        PostDto postDto3 = new PostDto("아아아", "aaa", "유머", "/img/aaa.jpg", multipartFile);

        Post post1 = postRepository.save(Post.createPost(member1, postDto1, category));
        Post post2 = postRepository.save(Post.createPost(member2, postDto2, category));
        Post post3 = postRepository.save(Post.createPost(member1, postDto3, category));

        List<Post> result1 = postRepository.findByMember(member1.getId());
        List<Post> result2 = postRepository.findByMember(member2.getId());

        assertThat(result1).hasSize(2);
        assertThat(result2).hasSize(1);
    }

    @Test
    void deletePost() {
        // given
        byte[] fileBytes = "Test file content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("testFile", "test.txt", "text/plain", fileBytes);

        MemberSaveDto saveDto = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member = memberRepository.save(Member.saveMember(saveDto));
        Category category = Category.createCategory("유머");
        categoryRepository.save(category);

        PostDto postDto = new PostDto("안녕하세요", "aaa", "유머", "/img/aaa.jpg", multipartFile);
        Post post = postRepository.save(Post.createPost(member, postDto, category));

        // when
        postRepository.delete(post.getId());

        // then
        assertThat(postRepository.findById(post.getId()).isEmpty()).isTrue();
    }

    void postSearch(String memberName, String postTitle, Post... posts) {
        PostSearchCond postSearch = new PostSearchCond(memberName, postTitle);
        List<Post> result = postRepository.findAll(postSearch, 10, 0);
        assertThat(result).hasSize(posts.length);
    }
}
