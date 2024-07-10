package study.bulletin_board.dto;

import lombok.Data;

@Data
public class MemberSaveDto {

    private String nickname;
    private String email;

    public MemberSaveDto() {
    }

    public MemberSaveDto(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
