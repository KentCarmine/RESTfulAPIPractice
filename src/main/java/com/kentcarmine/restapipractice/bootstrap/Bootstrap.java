package com.kentcarmine.restapipractice.bootstrap;

import com.kentcarmine.restapipractice.model.Book;
import com.kentcarmine.restapipractice.model.security.Authority;
import com.kentcarmine.restapipractice.model.security.Role;
import com.kentcarmine.restapipractice.model.security.User;
import com.kentcarmine.restapipractice.repository.BookRepository;
import com.kentcarmine.restapipractice.repository.security.AuthorityRepository;
import com.kentcarmine.restapipractice.repository.security.RoleRepository;
import com.kentcarmine.restapipractice.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class Bootstrap implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Bootstrap(BookRepository bookRepository, UserRepository userRepository, RoleRepository roleRepository,
                     AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        setupBooks();
        setupUsersRolesAndAuthorities();
    }

    private void setupBooks() {
        Book book1 = new Book("The One Impossible Labyrinth", "Matthew Reilly");
        book1 = bookRepository.save(book1);

        Book book2 = new Book("Scarecrow", "Matthew Reilly");
        book2 = bookRepository.save(book2);

        Book book3 = new Book("The Sum of All Fears", "Tom Clancy");
        book3 = bookRepository.save(book3);

        Book book4 = new Book("Fate/Stay Night", "Kinoko Nasu");
        book4 = bookRepository.save(book4);
    }

    private void setupUsersRolesAndAuthorities() {
        Role userRole = roleRepository.save(new Role("USER"));
        Role adminRole = roleRepository.save(new Role("ADMIN"));

        Authority readBooksAuthority = authorityRepository.save(new Authority("book.read"));
        Authority createBookAuthority = authorityRepository.save(new Authority("book.create"));
        Authority updateBookAuthority = authorityRepository.save(new Authority("book.update"));
        Authority deleteBookAuthority = authorityRepository.save(new Authority("book.delete"));

        // Extra (unused) granular permissions
        Authority readAllBooksAuthority = authorityRepository.save(new Authority("book.read.all"));
        Authority readBookByIdAuthority = authorityRepository.save(new Authority("book.read.byId"));
        Authority readBooksByTitleAuthority = authorityRepository.save(new Authority("book.read.byTitle"));
        Authority readBooksByAuthorAuthority = authorityRepository.save(new Authority("book.read.byAuthor"));
        Authority readBookExistsById = authorityRepository.save(new Authority("book.read.verifyExistsById"));

        adminRole.setAuthorities(new HashSet<>(Set.of(readBooksAuthority, readAllBooksAuthority, readBookByIdAuthority,
                readBooksByTitleAuthority, readBooksByAuthorAuthority, readBookExistsById, createBookAuthority,
                updateBookAuthority, deleteBookAuthority)));
        roleRepository.save(adminRole);

        userRole.setAuthorities(new HashSet<>(Set.of(readBooksAuthority, readAllBooksAuthority, readBookByIdAuthority,
                readBooksByTitleAuthority, readBooksByAuthorAuthority, readBookExistsById)));
        roleRepository.save(userRole);

        User basicUser = new User("testUser", passwordEncoder.encode("password"));
        basicUser.addRole(userRole);
        basicUser = userRepository.save(basicUser);

        User adminUser = new User("testAdmin", passwordEncoder.encode("adminpassword"));
        adminUser.addRole(adminRole);
        adminUser.addRole(userRole);
        adminUser = userRepository.save(adminUser);
    }

    public void resetBooks() {
        bookRepository.deleteAll();
        setupBooks();
    }
}
