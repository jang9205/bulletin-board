package study.bulletin_board.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.bulletin_board.domain.Category;
import study.bulletin_board.domain.Member;
import study.bulletin_board.domain.Post;
import study.bulletin_board.dto.PostDto;
import study.bulletin_board.dto.PostSearchCond;
import study.bulletin_board.exception.FileStorageException;
import study.bulletin_board.repository.category.CategoryRepository;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.repository.post.PostRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public Post savePost(Long memberId, PostDto postDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        Category category = categoryRepository.findCategory(postDto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + postDto.getCategory()));

        MultipartFile picture = postDto.getPicture();
        try {
            if (picture != null && !picture.isEmpty()) {
                String originalFilename = picture.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

                String fullPath = fileDir + fileName;
                picture.transferTo(new File(fullPath));

                postDto.setPicturePath("/img/post/" + fileName);
            }

            Post post = Post.createPost(member, postDto, category);
            return postRepository.save(post);

        } catch (IOException e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }

    @Override
    public Post updatePost(Long postId, Long memberId, Post updatedPost) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));

        try {
            if (updatedPost.getPicture() != null && !updatedPost.getPicture().isEmpty()) {
                String originalFilename = updatedPost.getPicture().getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

                String fullPath = fileDir + fileName;
                updatedPost.getPicture().transferTo(new File(fullPath));

                updatedPost.setPicturePath("/img/post/" + fileName);
            } else {
                //기존의 이미지 경로를 유지
                updatedPost.setPicturePath(existingPost.getPicturePath());
            }

            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            existingPost.setPicturePath(updatedPost.getPicturePath());

            postRepository.update(postId, existingPost);
            return existingPost;

        } catch (IOException e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.delete(postId);
    }

    @Override
    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findPostByMember(Long memberId) {
        return postRepository.findByMember(memberId);
    }

    @Override
    public List<Post> findAllPosts(PostSearchCond postSearch) {
        return postRepository.findAll(postSearch);
    }
}
