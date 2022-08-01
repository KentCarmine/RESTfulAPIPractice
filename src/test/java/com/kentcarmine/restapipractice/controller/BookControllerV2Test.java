package com.kentcarmine.restapipractice.controller;

import com.kentcarmine.restapipractice.controller.errorhandling.CustomRestExceptionHandler;
import com.kentcarmine.restapipractice.helper.security.AuthenticationAnonymousVerificationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Profile("test")
@ExtendWith(MockitoExtension.class)
public class BookControllerV2Test extends BookControllerTest {

    @InjectMocks
    BookControllerV2 bookControllerV2;

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    protected void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookControllerV2)
                .setControllerAdvice(new CustomRestExceptionHandler(new AuthenticationAnonymousVerificationHelper()))
                .build();
    }

    protected String configBaseApiUrl() {
        return "/api/v2/books/";
    }

}
