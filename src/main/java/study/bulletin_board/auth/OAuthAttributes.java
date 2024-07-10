package study.bulletin_board.auth;

import lombok.Getter;
import study.bulletin_board.domain.Member;
import study.bulletin_board.dto.MemberSaveDto;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;

    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver(userNameAttributeName, attributes);
        }
        throw new IllegalArgumentException("Only Naver is supported for now.");
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        //JSON 형태이기 때문에 Map을 통해 데이터를 가져옴
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return new OAuthAttributes(response, userNameAttributeName,
                (String) response.get("nickname"),
                (String) response.get("email"));
    }

    public Member toEntity() {
        MemberSaveDto saveDto = new MemberSaveDto(nickname, email);
        return Member.saveMember(saveDto);
    }
}
