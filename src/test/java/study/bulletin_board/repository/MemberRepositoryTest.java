package study.bulletin_board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import study.bulletin_board.domain.Member;
import study.bulletin_board.domain.MemberGrade;
import study.bulletin_board.dto.MemberSaveDto;
import study.bulletin_board.repository.member.MemberRepository;
import study.bulletin_board.service.post.PostService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @MockBean
    PostService postService;

    @Test
    void saveMember() {
        //given
        MemberSaveDto saveDto = new MemberSaveDto("kim", "ab1234@naver.com");

        //when
        Member member = Member.saveMember(saveDto);
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(member.getNickname()).isEqualTo(saveDto.getNickname());
        assertThat(member.getEmail()).isEqualTo(saveDto.getEmail());
        assertThat(member.getGrade()).isEqualTo(MemberGrade.BASIC);
        assertThat(member.getCreatedDate()).isEqualTo(savedMember.getCreatedDate());
        assertThat(member.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    void findMemberByEmail() {
        //given
        MemberSaveDto saveDto = new MemberSaveDto("kim", "ab1234@naver.com");
        Member member = Member.saveMember(saveDto);
        memberRepository.save(member);

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail("ab1234@naver.com");

        //then
        assertThat(optionalMember).isPresent();
        Member findMember = optionalMember.get();
        assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getId()).isEqualTo(member.getId());
    }
}
