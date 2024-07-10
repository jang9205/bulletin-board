package study.bulletin_board.repository.member;

import study.bulletin_board.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByEmail(String email);
}
