package com.kentcarmine.restapipractice.service;

import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.dto.CreateOrUpdateBookDto;

import javax.validation.Valid;
import java.util.Set;

public interface BookService {

    Set<BookDto> getAllBooks();

    Set<BookDto> getAllBooksByTitle(String title);

    Set<BookDto> getAllBooksByAuthor(String author);

    BookDto getBookById(Long id);

    BookDto createNewBook(@Valid CreateOrUpdateBookDto createOrUpdateBookDto);

    BookDto deleteBookById(Long id);

    boolean isBookWithIdExists(Long id);

    BookDto updateBookWithId(Long id, CreateOrUpdateBookDto bookDto);
}
