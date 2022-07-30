package com.kentcarmine.restapipractice.converter;

import com.kentcarmine.restapipractice.dto.BookDto;
import com.kentcarmine.restapipractice.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookListToBookDtoSetConverter implements Converter<List<Book>, Set<BookDto>> {

    private final BookToBookDtoConverter bookToBookDtoConverter;

    public BookListToBookDtoSetConverter(BookToBookDtoConverter bookToBookDtoConverter) {
        this.bookToBookDtoConverter = bookToBookDtoConverter;
    }

    @Override
    public Set<BookDto> convert(List<Book> source) {
        return source.stream().map(book -> bookToBookDtoConverter.convert(book)).collect(Collectors.toSet());
    }

}
