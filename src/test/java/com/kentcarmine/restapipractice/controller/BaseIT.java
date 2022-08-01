package com.kentcarmine.restapipractice.controller;

import com.kentcarmine.restapipractice.bootstrap.Bootstrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class BaseIT {
    @Autowired
    WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Autowired
    Bootstrap bootstrap;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
//        bootstrap.resetBooks();
    }

    public static Stream<Arguments> getStreamAllUsers() {
        return Stream.of(Arguments.of("testUser" , "password"),
                Arguments.of("testAdmin", "adminpassword"));
    }
}
