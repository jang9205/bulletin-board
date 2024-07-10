package study.bulletin_board.repository.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.bulletin_board.domain.Member;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberMapper memberMapper;

    @Override
    public Member save(Member member) {
        memberMapper.save(member);
        return member;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }
}
