package com.kentcarmine.restapipractice.repository;

import com.kentcarmine.restapipractice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByTitle(String title);

    List<Book> findAllByAuthor(String author);

    Book findBookById(Long id);
}
