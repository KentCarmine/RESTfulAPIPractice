package com.kentcarmine.restapipractice.repository.security;

import com.kentcarmine.restapipractice.model.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
