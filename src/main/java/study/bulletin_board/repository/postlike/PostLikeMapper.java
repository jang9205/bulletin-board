package study.bulletin_board.repository.postlike;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import study.bulletin_board.domain.PostLike;

import java.util.Optional;

@Mapper
public interface PostLikeMapper {

    void save(PostLike postLike);

    void deleteByPostAndMember(@Param("postId") Long postId, @Param("memberId") Long memberId);

    Optional<PostLike> findByPostAndMember(@Param("postId") Long postId, @Param("memberId") Long memberId);

    int countByPost(Long postId);
}
