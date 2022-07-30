package com.kentcarmine.restapipractice.converter;

import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookToBookDtoConverter implements Converter<Book, BookDto> {

    @Override
    public BookDto convert(Book source) {
        if (source == null) {
            return null;
        }
        return new BookDto(source.getId(), source.getTitle(), source.getAuthor());
    }

}
