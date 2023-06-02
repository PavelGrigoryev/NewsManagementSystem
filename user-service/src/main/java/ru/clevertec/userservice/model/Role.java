package ru.clevertec.userservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.clevertec.userservice.model.Permissions.COMMENT_OPERATIONS;
import static ru.clevertec.userservice.model.Permissions.NEWS_OPERATIONS;

@RequiredArgsConstructor
public enum Role {

    ADMIN(Set.of(NEWS_OPERATIONS, COMMENT_OPERATIONS)),
    JOURNALIST(Set.of(NEWS_OPERATIONS)),
    SUBSCRIBER(Set.of(COMMENT_OPERATIONS));

    @Getter
    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }

}
