package com.kentcarmine.restapipractice.controller;

import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.dto.CreateOrUpdateBookDto;
import com.kentcarmine.restapipractice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/v2/books")
public class BookControllerV2 extends BookController {

    @Autowired
    public BookControllerV2(BookService bookService) {
        super(bookService);
    }

    @Override
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return super.getBookById(id);
    }

    @Override
    @GetMapping({"", "/"})
    public Set<BookDto> getAllBooks() {
        return super.getAllBooks();
    }

    @Override
    @GetMapping("/title/{bookTitle}")
    public Set<BookDto> getAllBooksByTitle(@PathVariable String bookTitle) {
        return super.getAllBooksByTitle(bookTitle);
    }

    @Override
    @GetMapping("/author/{bookAuthor}")
    public Set<BookDto> getAllBooksByAuthor(@PathVariable String bookAuthor) {
        return super.getAllBooksByAuthor(bookAuthor);
    }

    @Override
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createNewBook(@Valid @RequestBody CreateOrUpdateBookDto newBook) {
        return super.createNewBook(newBook);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateBookDto updateBook) {
        return super.updateBook(id, updateBook);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto deleteBook(@PathVariable Long id) {
        return super.deleteBook(id);
    }
}
