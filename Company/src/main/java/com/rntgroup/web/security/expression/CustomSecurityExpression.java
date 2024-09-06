package com.rntgroup.web.security.expression;

import com.rntgroup.database.entity.Role;
import com.rntgroup.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("cse")
@RequiredArgsConstructor
public class CustomSecurityExpression {
    public boolean hasAdminRights() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return hasAnyRole(authentication, Role.ADMIN);
    }

    public boolean canAccessUser(final Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var user = (JwtEntity) authentication.getPrincipal();
        var userId = user.getId();

        return userId.equals(id) || hasAnyRole(authentication, Role.ADMIN);
    }

    private boolean hasAnyRole(final Authentication authentication, final Role... roles) {
        for (Role role : roles) {
            var authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }

        return false;
    }
}
