package com.kentcarmine.restapipractice.controller;

import com.kentcarmine.restapipractice.dto.CreateOrUpdateBookDto;
import com.kentcarmine.restapipractice.helper.JsonConverterHelper;
import com.kentcarmine.restapipractice.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
public class BookControllerV2IT extends BaseIT {

    private static final String API_BASE_URL = "/api/v2/books/";

    @Autowired
    BookRepository bookRepository;

    @DisplayName("Read Books")
    @Nested
    class ReadBooks {

        @DisplayName("Get All Books")
        @Nested
        class getAllBooks {
            @Test
            void getAllBooks_noAuth() throws Exception {
                mockMvc.perform(get(API_BASE_URL))
                        .andExpect(status().isOk());
            }

            @ParameterizedTest(name = "#{index} with [{arguments}]")
            @MethodSource("com.kentcarmine.restapipractice.controller.BookControllerV2IT#getStreamAllUsers")
            void getAllBooks_AuthUsers(String user, String pwd) throws Exception {
                mockMvc.perform(get(API_BASE_URL)
                        .with(httpBasic(user, pwd)))
                        .andExpect(status().isOk());
            }
        }

        @DisplayName("Get Book By Id")
        @Nested
        class GetBookById {
            @Test
            void getBookById_noAuth() throws Exception {
                mockMvc.perform(get(API_BASE_URL + 1))
                        .andExpect(status().isOk());
            }

            @Test
            void getBookById_noAuth_noSuchBook() throws Exception {
                mockMvc.perform(get(API_BASE_URL + 1337))
                        .andExpect(status().isNotFound());
            }

            @ParameterizedTest(name = "#{index} with [{arguments}]")
            @MethodSource("com.kentcarmine.restapipractice.controller.BookControllerV2IT#getStreamAllUsers")
            void getBookById_AuthUsers(String user, String pwd) throws Exception {
                mockMvc.perform(get(API_BASE_URL + 1)
                        .with(httpBasic(user, pwd)))
                        .andExpect(status().isOk());
            }
        }

        @DisplayName("Get Books By Title")
        @Nested
        class GetBooksByTitle {
            @Test
            void getBooksByTitle_noAuth() throws Exception {
                mockMvc.perform(get(API_BASE_URL + "title/Scarecrow"))
                        .andExpect(status().isOk());
            }

            @Test
            void getBooksByTitle_noAuth_empty() throws Exception {
                 mockMvc.perform(get(API_BASE_URL + "title/arglefargleblargle"))
                        .andExpect(status().isOk()).
                         andExpect(content().string("[]"));
            }

            @ParameterizedTest(name = "#{index} with [{arguments}]")
            @MethodSource("com.kentcarmine.restapipractice.controller.BookControllerV2IT#getStreamAllUsers")
            void getBooksByTitle_AuthUsers(String user, String pwd) throws Exception {
                mockMvc.perform(get(API_BASE_URL + "title/Scarecrow")
                        .with(httpBasic(user, pwd)))
                        .andExpect(status().isOk());
            }
        }

        @DisplayName("Get Books By Author")
        @Nested
        class GetBooksByAuthor {
            @Test
            void getBooksByAuthor_noAuth() throws Exception {
                mockMvc.perform(get(API_BASE_URL + "author/Matthew Reilly"))
                        .andExpect(status().isOk());
            }

            @Test
            void getBooksByAuthor_noAuth_empty() throws Exception {
                mockMvc.perform(get(API_BASE_URL + "author/arglefargleblargle"))
                        .andExpect(status().isOk()).
                        andExpect(content().string("[]"));
            }

            @ParameterizedTest(name = "#{index} with [{arguments}]")
            @MethodSource("com.kentcarmine.restapipractice.controller.BookControllerV2IT#getStreamAllUsers")
            void getBooksByAuthor_AuthUsers(String user, String pwd) throws Exception {
                mockMvc.perform(get(API_BASE_URL + "author/Matthew Reilly")
                        .with(httpBasic(user, pwd)))
                        .andExpect(status().isOk());
            }
        }
    }

    @DisplayName("Create Book")
    @Nested
    class CreateBook {
        @Test
        void createBook_noAuth() throws Exception {
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");

            mockMvc.perform(post(API_BASE_URL + "new")
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithUserDetails("testUser")
        @Test
        void createBook_asUser() throws Exception {
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");

            mockMvc.perform(post(API_BASE_URL + "new")
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithUserDetails("testAdmin")
        @Test
        void createBook_asAdmin() throws Exception {
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Test Title 3", "Test Author 3");

            mockMvc.perform(post(API_BASE_URL + "new")
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        @WithUserDetails("testAdmin")
        @Test
        void createBook_asAdmin_nullBook() throws Exception {
            CreateOrUpdateBookDto newBook = null;

            mockMvc.perform(post(API_BASE_URL + "new")
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @WithUserDetails("testAdmin")
        @Test
        void createBook_invalidBook() throws Exception {
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("", null);

            mockMvc.perform(post(API_BASE_URL + "new")
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }


    }

    @DisplayName("Update Book")
    @Nested
    class UpdateBook {
        @Test
        void updateBook_noAuth() throws Exception {
            Long id = 1L;
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Title Updated 1", "Author Updated 1");

            mockMvc.perform(put(API_BASE_URL + id)
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithUserDetails("testUser")
        @Test
        void updateBook_asUser() throws Exception {
            Long id = 1L;
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Title Updated 1", "Author Updated 1");

            mockMvc.perform(put(API_BASE_URL + id)
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithUserDetails("testAdmin")
        @Test
        void updateBook_asAdmin() throws Exception {
            Long id = 1L;
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Title Updated 1", "Author Updated 1");

            mockMvc.perform(put(API_BASE_URL + id)
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @WithUserDetails("testAdmin")
        @Test
        void updateBook_asAdmin_nullBook() throws Exception {
            Long id = 1L;
            CreateOrUpdateBookDto newBook = null;

            mockMvc.perform(put(API_BASE_URL + id)
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @WithUserDetails("testAdmin")
        @Test
        void updateBook_asAdmin_invalidBook() throws Exception {
            Long id = 1L;
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("", " ");

            mockMvc.perform(put(API_BASE_URL + id)
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @WithUserDetails("testAdmin")
        @Test
        void updateBook_asAdmin_nonExistingId() throws Exception {
            Long id = 1337L;
            CreateOrUpdateBookDto newBook = new CreateOrUpdateBookDto("Title Updated 1", "Author Updated 1");

            mockMvc.perform(put(API_BASE_URL + id)
                    .content(JsonConverterHelper.asJsonString(newBook))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @DisplayName("Delete Book")
    @Nested
    class DeleteBook {

        @Test
        void deleteBook_noAuth() throws Exception {
            Long id = 1L;

            mockMvc.perform(delete(API_BASE_URL + id))
                    .andExpect(status().isUnauthorized());
        }

        @WithUserDetails("testUser")
        @Test
        void deleteBook_asUser() throws Exception {
            Long id = 1L;

            mockMvc.perform(delete(API_BASE_URL + id))
                    .andExpect(status().isForbidden());
        }

        @WithUserDetails("testAdmin")
        @Test
        void deleteBook_asAdmin() throws Exception {
            Long id = 1L;

            mockMvc.perform(delete(API_BASE_URL + id))
                    .andExpect(status().isOk());
        }

        @WithUserDetails("testAdmin")
        @Test
        void deleteBook_asAdmin_noSuchId() throws Exception {
            Long id = 1337L;

            mockMvc.perform(delete(API_BASE_URL + id))
                    .andExpect(status().isNotFound());
        }


    }



}
