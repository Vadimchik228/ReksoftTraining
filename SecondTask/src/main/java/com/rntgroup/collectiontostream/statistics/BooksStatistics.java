package com.rntgroup.collectiontostream.statistics;

import com.rntgroup.collectiontostream.entities.Author;
import com.rntgroup.collectiontostream.entities.Book;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record BooksStatistics(Book[] books) {

    // Проверка, не содержит ли какая-либо книга более 200 страниц
    public boolean anyBookOver200Pages() {
        return Arrays.stream(books)
                .peek(book -> System.out.println("Проверка книги: " + book.getTitle()))
                .anyMatch(book -> book.getNumberOfPages() > 200);
    }

    // Поиск книг с максимальным количеством страниц
    public Optional<Book> maxPagesBook() {
        return Arrays.stream(books)
                .peek(book -> System.out.println("Обработка книги: " + book.getTitle()))
                .max(Comparator.comparingInt(Book::getNumberOfPages));
    }

    // Поиск книг с минимальным количеством страниц
    public Optional<Book> minPagesBook() {
        return Arrays.stream(books)
                .peek(book -> System.out.println("Обработка книги: " + book.getTitle()))
                .min(Comparator.comparingInt(Book::getNumberOfPages));
    }

    // Фильтрация книг, в которых указан только один автор
    public List<Book> singleAuthorBooks() {
        return Arrays.stream(books)
                .peek(book -> System.out.println("Фильтрация книги: " + book.getTitle()))
                .filter(book -> book.getAuthors().size() == 1)
                .collect(Collectors.toList());
    }

    // Сортировка книг по количеству страниц/названию
    public List<Book> sortedBooks() {
        return Arrays.stream(books)
                .peek(book -> System.out.println("Сортировка книги: " + book.getTitle()))
                .sorted(Comparator.comparingInt(Book::getNumberOfPages)
                        .thenComparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    // Получение списка всех названий
    public List<String> bookTitles() {
        return Arrays.stream(books)
                .peek(book -> System.out.println("Получение названия книги: " + book.getTitle()))
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    // Вывод на печать с помощью метода forEach
    public void printBookTitles() {
        bookTitles().forEach(System.out::println);
    }

    // Получение списка всех авторов
    public List<Author> allAuthors() {
        return Arrays.stream(books)
                .peek(book -> System.out.println("Обработка книги для авторов: " + book.getTitle()))
                .flatMap(book -> book.getAuthors().stream())
                .distinct()
                .peek(author -> System.out.println("Автор: " + author.getName()))
                .collect(Collectors.toList());
    }
}
