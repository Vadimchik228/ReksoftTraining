package com.rntgroup.collectiontostream.entities;

import java.util.List;

public class Book {
    private String title;
    private List<Author> authors;
    private int numberOfPages;

    public Book(String title, List<Author> authors, int numberOfPages) {
        this.title = title;
        this.authors = authors;
        this.numberOfPages = numberOfPages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void addAuthor(Author author) {
        if (!authors.contains(author)) {
            authors.add(author);
            author.addBook(this);
        }
    }

    @Override
    public String toString() {
        return "Book{" +
               "title='" + title + '\'' +
               ", authors=" + authors.stream().map(Author::getName).toList() +
               ", numberOfPages=" + numberOfPages +
               '}';
    }
}