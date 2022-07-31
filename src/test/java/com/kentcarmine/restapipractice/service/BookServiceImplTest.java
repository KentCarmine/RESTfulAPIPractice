package com.kentcarmine.restapipractice.service;

import com.kentcarmine.restapipractice.converter.BookDtoToBookConverter;
import com.kentcarmine.restapipractice.converter.BookListToBookDtoSetConverter;
import com.kentcarmine.restapipractice.converter.BookToBookDtoConverter;
import com.kentcarmine.restapipractice.converter.CreateBookDtoToBookConverter;
import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.dto.CreateOrUpdateBookDto;
import com.kentcarmine.restapipractice.model.Book;
import com.kentcarmine.restapipractice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Profile;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Profile("test")
class BookServiceImplTest {

    private List<Book> testBooks;

    BookService bookService;

    BookToBookDtoConverter bookToBookDtoConverter;
    BookDtoToBookConverter bookDtoToBookConverter;
    BookListToBookDtoSetConverter bookListToBookDtoSetConverter;
    CreateBookDtoToBookConverter createBookDtoToBookConverter;

    @Mock
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        bookToBookDtoConverter = new BookToBookDtoConverter();
        bookDtoToBookConverter = new BookDtoToBookConverter();
        bookListToBookDtoSetConverter = new BookListToBookDtoSetConverter(new BookToBookDtoConverter());
        createBookDtoToBookConverter = new CreateBookDtoToBookConverter();

        bookService = new BookServiceImpl(bookRepository, bookToBookDtoConverter, bookDtoToBookConverter,
                bookListToBookDtoSetConverter, createBookDtoToBookConverter);

        testBooks = new LinkedList<>();
        testBooks.add(new Book(1L, "Title 1", "Author 1"));
        testBooks.add(new Book(2L, "Title 2", "Author 2"));

        when(bookRepository.findAll()).thenReturn(testBooks);
    }

    @Test
    void getAllBooks_success() {
        Set<BookDto> result = bookService.getAllBooks();
        assertEquals(testBooks.size(), result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getAllBooks_empty() {
        when(bookRepository.findAll()).thenReturn(List.of());
        Set<BookDto> result = bookService.getAllBooks();
        assertEquals(0, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getAllBooksByTitle_existingTitle() {
        when(bookRepository.findAllByTitle(anyString())).thenReturn(List.of(testBooks.get(0)));

        Set<BookDto> result = bookService.getAllBooksByTitle(testBooks.get(0).getTitle());
        assertEquals(1, result.size());

        String resAuthor = ((BookDto)(result.toArray()[0])).getAuthor();
        assertTrue(testBooks.get(0).getAuthor().equals(resAuthor));

        verify(bookRepository, times(1)).findAllByTitle(anyString());
    }

    @Test
    void getAllBooksByTitle_noSuchTitle() {
        when(bookRepository.findAllByTitle(anyString())).thenReturn(List.of());
        Set<BookDto> result = bookService.getAllBooksByTitle(testBooks.get(0).getTitle());
        assertEquals(0, result.size());
        verify(bookRepository, times(1)).findAllByTitle(anyString());
    }

    @Test
    void getAllBooksByAuthor_existingAuthor() {
        when(bookRepository.findAllByAuthor(anyString())).thenReturn(List.of(testBooks.get(1)));

        Set<BookDto> result = bookService.getAllBooksByAuthor(testBooks.get(1).getAuthor());
        assertEquals(1, result.size());

        String resAuthor = ((BookDto)(result.toArray()[0])).getAuthor();
        assertTrue(testBooks.get(1).getAuthor().equals(resAuthor));

        verify(bookRepository, times(1)).findAllByAuthor(anyString());
    }

    @Test
    void getAllBooksByAuthor_noSuchAuthor() {
        when(bookRepository.findAllByAuthor(anyString())).thenReturn(List.of());
        Set<BookDto> result = bookService.getAllBooksByAuthor(testBooks.get(0).getTitle());
        assertEquals(0, result.size());
        verify(bookRepository, times(1)).findAllByAuthor(anyString());
    }

    @Test
    void getBookById_existingId() {
        when(bookRepository.findBookById(anyLong())).thenReturn(testBooks.get(1));
        BookDto result = bookService.getBookById(1L);
        assertEquals(result.getId(), testBooks.get(1).getId());
        assertEquals(result.getTitle(), testBooks.get(1).getTitle());
        assertEquals(result.getAuthor(), testBooks.get(1).getAuthor());
        verify(bookRepository, times(1)).findBookById(anyLong());
    }

    @Test
    void getBookById_noSuchId() {
        when(bookRepository.findBookById(anyLong())).thenReturn(null);
        BookDto result = bookService.getBookById(1L);
        assertNull(result);
        verify(bookRepository, times(1)).findBookById(anyLong());
    }

    @Test
    void createNewBook() {
        Long newId = 5L;
        String newTitle = "Title 3";
        String newAuthor = "Author 3";
        when(bookRepository.save(any())).thenReturn(new Book(newId, newTitle, newAuthor));

        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Title 3", "Author 3");

        BookDto result = bookService.createNewBook(newBook);
        assertEquals(newId, result.getId());
        assertEquals(newTitle, result.getTitle());
        assertEquals(newAuthor, result.getAuthor());

        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void updateBookWithId_existingId() {
        when(bookRepository.findBookById(any())).thenReturn(testBooks.get(1));

        Long bookId = testBooks.get(1).getId();
        String updatedTitle = "Title Updated";
        String bookAuthor = testBooks.get(1).getAuthor();

        when(bookRepository.save(any())).thenReturn(new Book(bookId, updatedTitle, bookAuthor));

        CreateOrUpdateBookDto updatedBookDto = new CreateOrUpdateBookDto(updatedTitle, bookAuthor);

        BookDto result = bookService.updateBookWithId(bookId, updatedBookDto);

        assertEquals(bookId, result.getId());
        assertEquals(updatedTitle, result.getTitle());
        assertEquals(bookAuthor, result.getAuthor());
        verify(bookRepository, times(1)).save(any());
    }

//    @Test
//    void updateBookWithId_noSuchId() {
//        when(bookRepository.findBookById(any())).thenReturn(null);
//
//        Long bookId = 15L;
//        String updatedTitle = "Title Updated";
//        String updatedAuthor = "Author Updated";
//
//        CreateOrUpdateBookDto updatedBookDto = new CreateOrUpdateBookDto(updatedTitle, updatedAuthor);
//
//        Exception e = assertThrows(BookNotFoundException.class, () -> {
//            BookDto result = bookService.updateBookWithId(bookId, updatedBookDto);
//        });
//
//        assertTrue(((BookNotFoundException)e).getMessage().contains(Long.toString(bookId)));
//
//        verify(bookRepository, times(0)).save(any());
//    }

//    @Test
//    void updateBookWithId_invalidReplacement() {
//        when(bookRepository.findBookById(any())).thenReturn(testBooks.get(0));
//
//        Exception e = assertThrows(InvalidBookInputException.class, () -> {
//            BookDto result = bookService.updateBookWithId(testBooks.get(0).getId(), null);
//        });
//
//        verify(bookRepository, times(0)).save(any());
//    }

    @Test
    void deleteBookById_existingId() {
        when(bookRepository.findBookById(any())).thenReturn(testBooks.get(0));
        when(bookRepository.existsById(any())).thenReturn(true);

        BookDto result = bookService.deleteBookById(testBooks.get(0).getId());

        assertEquals(testBooks.get(0).getId(), result.getId());
        assertEquals(testBooks.get(0).getTitle(), result.getTitle());
        assertEquals(testBooks.get(0).getAuthor(), result.getAuthor());

        verify(bookRepository, times(1)).findBookById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
    }

//    @Test
//    void deleteBookById_noSuchId() {
//        when(bookRepository.findBookById(any())).thenReturn(null);
//
//        Long bookId = 15L;
//
//        Exception e = assertThrows(BookNotFoundException.class, () -> {
//            BookDto result = bookService.deleteBookById(bookId);
//        });
//
//        assertTrue(e instanceof BookNotFoundException);
//        assertTrue(((BookNotFoundException)e).getMessage().contains(Long.toString(bookId)));
//
//        verify(bookRepository, times(0)).save(any());
//    }

    @Test
    void isBookWithIdExists_exists() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);

        boolean result = bookService.isBookWithIdExists(1L);

        assertTrue(result);

        verify(bookRepository, times(1)).existsById(anyLong());
    }

    @Test
    void isBookWithIdExists_notExists() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        boolean result = bookService.isBookWithIdExists(1337L);

        assertFalse(result);

        verify(bookRepository, times(1)).existsById(anyLong());
    }

    @Test
    void isBookWithIdExists_generalError() {
        when(bookRepository.existsById(anyLong())).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> {
            boolean result = bookService.isBookWithIdExists(1337L);
        });

        verify(bookRepository, times(1)).existsById(anyLong());
    }
}