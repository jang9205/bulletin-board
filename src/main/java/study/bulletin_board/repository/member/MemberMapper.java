package study.bulletin_board.repository.member;

import org.apache.ibatis.annotations.Mapper;
import study.bulletin_board.domain.Member;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    void save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);
}
