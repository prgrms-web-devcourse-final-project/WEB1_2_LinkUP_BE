package dev_final_team10.GoodBuyUS.domain.user.entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String key;
    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.key));
    }
}
