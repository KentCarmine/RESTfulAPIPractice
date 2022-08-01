package com.kentcarmine.restapipractice.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PreAuthorize("isAnonymous() OR hasAuthority('book.read')")
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadBooksPermission {
}
