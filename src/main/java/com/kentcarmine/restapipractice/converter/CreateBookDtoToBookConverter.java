package com.kentcarmine.restapipractice.converter;

import com.kentcarmine.restapipractice.dto.CreateOrUpdateBookDto;
import com.kentcarmine.restapipractice.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateBookDtoToBookConverter implements Converter<CreateOrUpdateBookDto, Book> {

    @Override
    public Book convert(CreateOrUpdateBookDto source) {
        if (source == null) {
            return null;
        }

        return new Book(source.getTitle(), source.getAuthor());
    }

}
