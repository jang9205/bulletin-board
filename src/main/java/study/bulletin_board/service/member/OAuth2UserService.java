package study.bulletin_board.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import study.bulletin_board.auth.OAuthAttributes;
import study.bulletin_board.domain.Member;
import study.bulletin_board.repository.member.MemberRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = findOrCreateMember(attributes);

        Map<String, Object> userAttributes = new HashMap<>(attributes.getAttributes());
        if (!userAttributes.containsKey("response")) {
            userAttributes.put("response", attributes.getAttributes());
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getGrade().name())),
                userAttributes,
                userNameAttributeName);
    }

    private Member findOrCreateMember(OAuthAttributes attributes) {
        return memberRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> {
                    Member newMember = attributes.toEntity();
                    return memberRepository.save(newMember);
                });
    }
}
