package com.kentcarmine.restapipractice.service;

import com.kentcarmine.restapipractice.converter.BookDtoToBookConverter;
import com.kentcarmine.restapipractice.converter.BookListToBookDtoSetConverter;
import com.kentcarmine.restapipractice.converter.BookToBookDtoConverter;
import com.kentcarmine.restapipractice.converter.CreateBookDtoToBookConverter;
import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.dto.CreateBookDto;
import com.kentcarmine.restapipractice.model.Book;
import com.kentcarmine.restapipractice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookToBookDtoConverter bookToBookDtoConverter;
    private final BookDtoToBookConverter bookDtoToBookConverter;
    private final BookListToBookDtoSetConverter bookListToBookDtoSetConverter;
    private final CreateBookDtoToBookConverter createBookDtoToBookConverter;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookToBookDtoConverter bookToBookDtoConverter,
                           BookDtoToBookConverter bookDtoToBookConverter,
                           BookListToBookDtoSetConverter bookListToBookDtoSetConverter,
                           CreateBookDtoToBookConverter createBookDtoToBookConverter) {
        this.bookRepository = bookRepository;
        this.bookToBookDtoConverter = bookToBookDtoConverter;
        this.bookDtoToBookConverter = bookDtoToBookConverter;
        this.bookListToBookDtoSetConverter = bookListToBookDtoSetConverter;
        this.createBookDtoToBookConverter = createBookDtoToBookConverter;
    }

    @Override
    public Set<BookDto> getAllBooks() {
        return bookListToBookDtoSetConverter.convert(bookRepository.findAll());
    }

    @Override
    public Set<BookDto> getAllBooksByTitle(String title) {
        return bookListToBookDtoSetConverter.convert(bookRepository.findAllByTitle(title));
    }

    @Override
    public Set<BookDto> getAllBooksByAuthor(String author) {
        return bookListToBookDtoSetConverter.convert(bookRepository.findAllByAuthor(author));
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookToBookDtoConverter.convert(bookRepository.findBookById(id));
    }

    @Override
    public BookDto createNewBook(CreateBookDto createBookDto) {
        Book newBook = createBookDtoToBookConverter.convert(createBookDto);
        newBook = bookRepository.save(newBook);

        return bookToBookDtoConverter.convert(newBook);
    }
}
