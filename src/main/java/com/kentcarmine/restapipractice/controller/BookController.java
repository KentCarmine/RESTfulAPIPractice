package com.kentcarmine.restapipractice.controller;

import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.dto.CreateOrUpdateBookDto;
import com.kentcarmine.restapipractice.exception.BookNotFoundException;
import com.kentcarmine.restapipractice.exception.InvalidBookInputException;
import com.kentcarmine.restapipractice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
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
            throw new BookNotFoundException(id);
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
    public BookDto createNewBook(@Valid @RequestBody CreateOrUpdateBookDto newBook) {
        if (newBook == null) {
            throw new InvalidBookInputException(); // redundant, but can be updated later if needed for validations
        }

        return bookService.createNewBook(newBook);
    }

    // Update book
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody(required = false) CreateOrUpdateBookDto updateBook) {
        if (!bookService.isBookWithIdExists(id)) {
            throw new BookNotFoundException(id);
        } else if (updateBook == null) {
            throw new InvalidBookInputException();
        }

        return bookService.updateBookWithId(id, updateBook);
    }

    // Delete book by id
    @DeleteMapping("/{id}")
    public BookDto deleteBook(@PathVariable Long id) {
        if (!bookService.isBookWithIdExists(id)) {
            throw new BookNotFoundException(id);
        }

        return bookService.deleteBookById(id);
    }

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
