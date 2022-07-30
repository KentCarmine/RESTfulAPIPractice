package com.kentcarmine.restapipractice.bootstrap;

import com.kentcarmine.restapipractice.model.Book;
import com.kentcarmine.restapipractice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Bootstrap implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Autowired
    public Bootstrap(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        setupBooks();
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
}
