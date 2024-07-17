package com.rntgroup.collectiontostream.entities;

import java.util.List;

public class Author {
    private String name;
    private short age;
    private List<Book> books;

    public Author(String name, short age, List<Book> books) {
        this.name = name;
        this.age = age;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
            book.addAuthor(this);
        }
    }

    @Override
    public String toString() {
        return "Author{" +
               "name='" + name + '\'' +
               ", age=" + age +
               ", books=" + books.stream().map(Book::getTitle).toList() +
               '}';
    }
}
