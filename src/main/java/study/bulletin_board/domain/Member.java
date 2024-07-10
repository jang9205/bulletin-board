package study.bulletin_board.domain;

import lombok.Getter;
import lombok.Setter;
import study.bulletin_board.dto.MemberSaveDto;

import java.time.LocalDateTime;

@Getter @Setter
public class Member {

    private Long id;
    private String nickname;
    private String email;
    private LocalDateTime createdDate;
    private MemberGrade grade;

    //생성 메서드
    public static Member saveMember(MemberSaveDto saveDto) {
        Member member = new Member();

        member.setNickname(saveDto.getNickname());
        member.setEmail(saveDto.getEmail());
        member.setCreatedDate(LocalDateTime.now());
        member.setGrade(MemberGrade.BASIC);
        return member;
    }
}
