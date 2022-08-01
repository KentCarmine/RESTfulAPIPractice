package com.kentcarmine.restapipractice.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PreAuthorize("hasAuthority('book.update')")
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateBookPermission {
}
