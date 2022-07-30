package com.kentcarmine.restapipractice.controller;

import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.dto.CreateBookDto;
import com.kentcarmine.restapipractice.exception.BookNotFoundException;
import com.kentcarmine.restapipractice.exception.InvalidBookInputException;
import com.kentcarmine.restapipractice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // List one book by id
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        BookDto result = bookService.getBookById(id);

        if (result == null) {
            throw new BookNotFoundException();
        }

        return result;
    }

    // List all books
    @GetMapping({"", "/"})
    public Set<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    // List all books by title
    @GetMapping("/title/{bookTitle}")
    public Set<BookDto> getAllBooksByTitle(@PathVariable String bookTitle) {
        return bookService.getAllBooksByTitle(bookTitle);
    }

    // List all books by author
    @GetMapping("/author/{bookAuthor}")
    public Set<BookDto> getAllBooksByAuthor(@PathVariable String bookAuthor) {
        return bookService.getAllBooksByAuthor(bookAuthor);
    }

    // Create book
    @PostMapping("/new")
    public BookDto createNewBook(@RequestBody CreateBookDto newBook) {
        if (newBook == null) {
            throw new InvalidBookInputException();
        }

        return bookService.createNewBook(newBook);
    }

    // Update book

    // Delete book by id


    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException bnfe) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bnfe.getMessage());
    }

    @ExceptionHandler(InvalidBookInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidBookInputException(InvalidBookInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
