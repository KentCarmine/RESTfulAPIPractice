package com.kentcarmine.restapipractice.controller;

import com.kentcarmine.restapipractice.controller.errorhandling.CustomRestExceptionHandler;
import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.dto.CreateOrUpdateBookDto;
import com.kentcarmine.restapipractice.helper.JsonConverterHelper;
import com.kentcarmine.restapipractice.helper.security.AuthenticationAnonymousVerificationHelper;
import com.kentcarmine.restapipractice.model.Book;
import com.kentcarmine.restapipractice.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ConstraintViolationException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    BookService bookService;

    @InjectMocks
    BookController bookController;

    List<Book> bookList;
    BookDto bookDto1;
    BookDto bookDto2;
    Set<BookDto> bookDtoSet;

    MockMvc mockMvc;

    String baseApiUrl;

    @BeforeEach
    void setUp() {
        baseApiUrl = configBaseApiUrl();

        bookList = new LinkedList<>();
        bookList.add(new Book(1L, "Title 1", "Author 1"));
        bookList.add(new Book(2L, "Title 2", "Author 2"));

        bookDto1 = new BookDto(1L, "Title 1", "Author 1");
        bookDto2 = new BookDto(2L, "Title 2", "Author 2");
        bookDtoSet = new HashSet<>();
        bookDtoSet.add(bookDto1);
        bookDtoSet.add(bookDto2);

        setupMockMvc();
    }

    protected void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new CustomRestExceptionHandler(new AuthenticationAnonymousVerificationHelper()))
                .build();
    }

    protected String configBaseApiUrl() {
        return "/api/v1/books/";
    }

    @Test
    void getBookById_existingId() throws Exception {
        when(bookService.getBookById(any())).thenReturn(bookDto1);
        mockMvc.perform(get(baseApiUrl + 1))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getBookById(any());
    }

    @Test
    void getBookById_noSuchId() throws Exception {
        when(bookService.getBookById(any())).thenReturn(null);
        mockMvc.perform(get(baseApiUrl + 1))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(any());
    }

    @Test
    void getBookById_generalError() throws Exception {
        when(bookService.getBookById(any())).thenThrow(new RuntimeException());
        mockMvc.perform(get(baseApiUrl + 1))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).getBookById(any());
    }

    @Test
    void getAllBooks_success() throws Exception {
        when(bookService.getAllBooks()).thenReturn(bookDtoSet);
        mockMvc.perform(get(baseApiUrl))
                .andExpect(status().isOk());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getAllBooks_generalError() throws Exception {
        when(bookService.getAllBooks()).thenThrow(new RuntimeException());
        mockMvc.perform(get(baseApiUrl))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getAllBooksByTitle_existingTitle() throws Exception {
        when(bookService.getAllBooksByTitle(any())).thenReturn(Set.of(bookDto1));
        mockMvc.perform(get(baseApiUrl + "title/" + bookDto1.getTitle()))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getAllBooksByTitle(anyString());
    }

    @Test
    void getAllBooksByTitle_noSuchTitle() throws Exception {
        when(bookService.getAllBooksByTitle(any())).thenReturn(Set.of());
        mockMvc.perform(get(baseApiUrl + "title/argleblargle"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getAllBooksByTitle(anyString());
    }

    @Test
    void getAllBooksByTitle_generalError() throws Exception {
        when(bookService.getAllBooksByTitle(any())).thenThrow(new RuntimeException());
        mockMvc.perform(get(baseApiUrl + "title/" + bookDto1.getTitle()))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).getAllBooksByTitle(anyString());
    }

    @Test
    void getAllBooksByAuthor_existingAuthor() throws Exception {
        when(bookService.getAllBooksByAuthor(any())).thenReturn(Set.of(bookDto1));
        mockMvc.perform(get(baseApiUrl + "author/" + bookDto1.getAuthor()))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getAllBooksByAuthor(anyString());
    }

    @Test
    void getAllBooksByAuthor_noSuchAuthor() throws Exception {
        when(bookService.getAllBooksByAuthor(any())).thenReturn(Set.of());
        mockMvc.perform(get(baseApiUrl + "author/fargle"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getAllBooksByAuthor(anyString());
    }

    @Test
    void getAllBooksByAuthor_generalError() throws Exception {
        when(bookService.getAllBooksByAuthor(any())).thenThrow(new RuntimeException());
        mockMvc.perform(get(baseApiUrl + "author/" + bookDto1.getAuthor()))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).getAllBooksByAuthor(anyString());
    }

    @Test
    void createNewBook_valid() throws Exception {
        when(bookService.createNewBook(any())).thenReturn(new BookDto(100L, "Test Title 3", "Test Author 3"));

        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");

        mockMvc.perform(post(baseApiUrl + "new")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(bookService, times(1)).createNewBook(any());
    }

    @Test
    void createNewBook_nullBook() throws Exception {
        CreateOrUpdateBookDto newBook = null;

        mockMvc.perform(post(baseApiUrl + "new")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(bookService, times(0)).createNewBook(any());
    }

    @Test
    void createNewBook_invalidBook() throws Exception {
        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto(null, "");
        when(bookService.createNewBook(any())).thenThrow(new ConstraintViolationException("test constraint violation ex", Set.of()));

        mockMvc.perform(post(baseApiUrl + "new")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(bookService, times(1)).createNewBook(any());
    }

    @Test
    void createNewBook_generalError() throws Exception {
        when(bookService.createNewBook(any())).thenThrow(new RuntimeException());

        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");

        mockMvc.perform(post(baseApiUrl + "new")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).createNewBook(any());
    }

    @Test
    void updateBook_validInput() throws Exception {
        when(bookService.isBookWithIdExists(anyLong())).thenReturn(true);
        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");
        BookDto updatedBookDto = new BookDto(bookDto1.getId(), newBook.getTitle(), newBook.getAuthor());

        when(bookService.updateBookWithId(anyLong(), any())).thenReturn(updatedBookDto);

        MvcResult result =  mockMvc.perform(put(baseApiUrl + 1)
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        verify(bookService, times(1)).isBookWithIdExists(anyLong());
        verify(bookService, times(1)).updateBookWithId(anyLong(), any());
    }

    @Test
    void updateBook_invalidId() throws Exception {
        when(bookService.isBookWithIdExists(anyLong())).thenReturn(false);
        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");

        MvcResult result =  mockMvc.perform(put(baseApiUrl + 1337)
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        verify(bookService, times(1)).isBookWithIdExists(anyLong());
        verify(bookService, times(0)).updateBookWithId(anyLong(), any());
    }

    @Test
    void updateBook_nullBook() throws Exception {
        CreateOrUpdateBookDto newBook = null;

        MvcResult result =  mockMvc.perform(put(baseApiUrl + 1)
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("", result.getResponse().getContentAsString());

        verify(bookService, times(0)).isBookWithIdExists(anyLong());
        verify(bookService, times(0)).updateBookWithId(anyLong(), any());
    }

    @Test
    void updateBook_invalidBook() throws Exception {
        when(bookService.updateBookWithId(any(), any()))
                .thenThrow(new ConstraintViolationException("test constraint violation ex", Set.of()));
        when(bookService.isBookWithIdExists(anyLong())).thenReturn(true);
        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("", null);

        MvcResult result =  mockMvc.perform(put(baseApiUrl + 1)
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        verify(bookService, times(1)).isBookWithIdExists(anyLong());
        verify(bookService, times(1)).updateBookWithId(anyLong(), any());
    }

    @Test
    void updateBook_generalError() throws Exception {
        when(bookService.isBookWithIdExists(anyLong())).thenReturn(true);

        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");
        BookDto updatedBookDto = new BookDto(bookDto1.getId(), newBook.getTitle(), newBook.getAuthor());

        when(bookService.updateBookWithId(anyLong(), any())).thenThrow(new RuntimeException());

        mockMvc.perform(put(baseApiUrl + 1)
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).isBookWithIdExists(anyLong());
        verify(bookService, times(1)).updateBookWithId(anyLong(), any());
    }

    @Test
    void deleteBook_validId() throws Exception {
        when(bookService.isBookWithIdExists(anyLong())).thenReturn(true);
        when(bookService.deleteBookById(anyLong())).thenReturn(bookDto1);

        mockMvc.perform(delete(baseApiUrl + 1))
                .andExpect(status().isOk());

        verify(bookService, times(1)).isBookWithIdExists(anyLong());
        verify(bookService, times(1)).deleteBookById(anyLong());
    }

    @Test
    void deleteBook_invalidId() throws Exception {
        when(bookService.isBookWithIdExists(anyLong())).thenReturn(false);

        mockMvc.perform(delete(baseApiUrl + 1337))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).isBookWithIdExists(anyLong());
        verify(bookService, times(0)).deleteBookById(anyLong());
    }

    @Test
    void deleteBook_generalError() throws Exception {
        when(bookService.isBookWithIdExists(anyLong())).thenReturn(true);
        when(bookService.deleteBookById(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(delete(baseApiUrl + 1))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).isBookWithIdExists(anyLong());
        verify(bookService, times(1)).deleteBookById(anyLong());
    }

}