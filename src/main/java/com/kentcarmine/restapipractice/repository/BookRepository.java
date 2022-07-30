package com.kentcarmine.restapipractice.repository;

import com.kentcarmine.restapipractice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findAllByTitle(String title);

    Book findAllByAuthor(String author);

    Book findBookById(Long id);
}
