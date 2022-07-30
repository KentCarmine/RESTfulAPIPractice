package com.kentcarmine.restapipractice.dto;

public class CreateBookDto {

    private String title;
    private String author;

    public CreateBookDto() {
        this.title = null;
        this.author = null;
    }

    public CreateBookDto(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "CreateBookDto{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
