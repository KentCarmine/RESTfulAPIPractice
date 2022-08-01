package com.kentcarmine.restapipractice.helper.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationAnonymousVerificationHelper {

    public boolean isAnonymous(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return true;
        }

        return authentication.getAuthorities().stream()
                .anyMatch((GrantedAuthority ga) -> ga.getAuthority().equals("ROLE_ANONYMOUS"));
    }
}
