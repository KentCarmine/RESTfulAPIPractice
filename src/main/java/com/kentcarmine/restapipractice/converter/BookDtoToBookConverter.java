package com.kentcarmine.restapipractice.converter;

import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookDtoToBookConverter implements Converter<BookDto, Book> {

    @Override
    public Book convert(BookDto source) {
        if (source == null) {
            return null;
        }

        return new Book(source.getId(), source.getTitle(), source.getAuthor());
    }

}
