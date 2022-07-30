package com.kentcarmine.restapipractice.converter;

import com.kentcarmine.restapipractice.dto.CreateBookDto;
import com.kentcarmine.restapipractice.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateBookDtoToBookConverter implements Converter<CreateBookDto, Book> {

    @Override
    public Book convert(CreateBookDto source) {
        if (source == null) {
            return null;
        }

        return new Book(source.getTitle(), source.getAuthor());
    }

}
