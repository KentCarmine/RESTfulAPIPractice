package com.kentcarmine.restapipractice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.dto.CreateOrUpdateBookDto;
import com.kentcarmine.restapipractice.exception.InvalidBookInputException;
import com.kentcarmine.restapipractice.helper.JsonConverterHelper;
import com.kentcarmine.restapipractice.model.Book;
import com.kentcarmine.restapipractice.repository.BookRepository;
import com.kentcarmine.restapipractice.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setUp() {

        bookList = new LinkedList<>();
        bookList.add(new Book(1L, "Title 1", "Author 1"));
        bookList.add(new Book(2L, "Title 2", "Author 2"));

        bookDto1 = new BookDto(1L, "Title 1", "Author 1");
        bookDto2 = new BookDto(2L, "Title 2", "Author 2");
        bookDtoSet = new HashSet<>();
        bookDtoSet.add(bookDto1);
        bookDtoSet.add(bookDto2);


        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void getBookById_existingId() throws Exception {
        when(bookService.getBookById(any())).thenReturn(bookDto1);
        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getBookById(any());
    }

    @Test
    void getBookById_noSuchId() throws Exception {
        when(bookService.getBookById(any())).thenReturn(null);
        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isNotFound());
        verify(bookService, times(1)).getBookById(any());
    }

    @Test
    void getAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(bookDtoSet);
        mockMvc.perform(get("/api/v1/books/"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getAllBooksByTitle_existingTitle() throws Exception {
        when(bookService.getAllBooksByTitle(any())).thenReturn(Set.of(bookDto1));
        mockMvc.perform(get("/api/v1/books/title/" + bookDto1.getTitle()))
                .andExpect(status().isOk());
        verify(bookService, times(1)).getAllBooksByTitle(anyString());
    }

    @Test
    void getAllBooksByTitle_noSuchTitle() throws Exception {
        when(bookService.getAllBooksByTitle(any())).thenReturn(Set.of());
        mockMvc.perform(get("/api/v1/books/title/argleblargle"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).getAllBooksByTitle(anyString());
    }

    @Test
    void getAllBooksByAuthor_existingAuthor() throws Exception {
        when(bookService.getAllBooksByAuthor(any())).thenReturn(Set.of(bookDto1));
        mockMvc.perform(get("/api/v1/books/author/" + bookDto1.getAuthor()))
                .andExpect(status().isOk());
        verify(bookService, times(1)).getAllBooksByAuthor(anyString());
    }

    @Test
    void getAllBooksByAuthor_noSuchAuthor() throws Exception {
        when(bookService.getAllBooksByAuthor(any())).thenReturn(Set.of());
        mockMvc.perform(get("/api/v1/books/author/fargle"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).getAllBooksByAuthor(anyString());
    }

    @Test
    void createNewBook_valid() throws Exception {
        when(bookService.createNewBook(any())).thenReturn(new BookDto(100L, "Test Title 3", "Test Author 3"));

        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");

        mockMvc.perform(post("/api/v1/books/new")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(bookService, times(1)).createNewBook(any());
    }

    @Test
    void createNewBook_nullBook() throws Exception {
        CreateOrUpdateBookDto newBook = null;

        mockMvc.perform(post("/api/v1/books/new")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(bookService, times(0)).createNewBook(any());

    }

    @Test
    void createNewBook_invalidBook() throws Exception {
        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto(null, "");

        mockMvc.perform(post("/api/v1/books/new")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(bookService, times(0)).createNewBook(any());

    }

    @Test
    void updateBook_validInput() throws Exception {
        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");
        BookDto updatedBookDto = new BookDto(bookDto1.getId(), newBook.getTitle(), newBook.getAuthor());

        when(bookService.updateBookWithId(anyLong(), any())).thenReturn(updatedBookDto);

        MvcResult result =  mockMvc.perform(put("/api/v1/books/1")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(newBook.getTitle()));
        assertTrue(result.getResponse().getContentAsString().contains(newBook.getAuthor()));
        assertTrue(result.getResponse().getContentAsString().contains(updatedBookDto.getId().toString()));

        verify(bookService, times(1)).updateBookWithId(anyLong(), any());
    }

    @Test
    void updateBook_invalidId() throws Exception {
        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");
        BookDto updatedBookDto = new BookDto(1337L, newBook.getTitle(), newBook.getAuthor());
        when(bookService.updateBookWithId(anyLong(), any())).thenThrow(new InvalidBookInputException());

        MvcResult result =  mockMvc.perform(put("/api/v1/books/1337")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        verify(bookService, times(1)).updateBookWithId(anyLong(), any());
    }

    @Test
    void updateBook_nullBook() throws Exception {
        CreateOrUpdateBookDto newBook = null;

        MvcResult result =  mockMvc.perform(put("/api/v1/books/1")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("", result.getResponse().getContentAsString());

        verify(bookService, times(0)).updateBookWithId(anyLong(), any());
    }

    @Test
    void updateBook_invalidBook() throws Exception {
        CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("", null);

        MvcResult result =  mockMvc.perform(put("/api/v1/books/1")
                .content(JsonConverterHelper.asJsonString(newBook))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("author cannot be blank"));
        assertTrue(result.getResponse().getContentAsString().contains("title cannot be blank"));

        verify(bookService, times(0)).updateBookWithId(anyLong(), any());
    }


    @Test
    void deleteBook_validId() throws Exception {
        when(bookService.deleteBookById(anyLong())).thenReturn(bookDto1);

        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBookById(anyLong());
    }

    @Test
    void deleteBook_invalidId() throws Exception {
        when(bookService.deleteBookById(anyLong())).thenThrow(new InvalidBookInputException());

        mockMvc.perform(delete("/api/v1/books/1337"))
                .andExpect(status().isBadRequest());

        verify(bookService, times(1)).deleteBookById(anyLong());
    }


}