package com.kentcarmine.restapipractice.converter;

import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.model.Book;
import org.springframework.core.convert.converter.Converter;

public class BookToBookDtoConverter implements Converter<Book, BookDto> {

    @Override
    public BookDto convert(Book source) {
        return new BookDto(source.getId(), source.getTitle(), source.getAuthor());
    }
}
