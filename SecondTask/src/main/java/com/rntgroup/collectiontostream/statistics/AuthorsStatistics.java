package com.rntgroup.collectiontostream.statistics;

import com.rntgroup.collectiontostream.entities.Author;
import com.rntgroup.collectiontostream.entities.Book;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record AuthorsStatistics(Author[] authors) {

    // Определение названия книги с наибольшим количеством страниц для каждого автора.
    public Map<Author, Optional<Book>> maxBookForAuthor() {
        Map<Author, Optional<Book>> authorMaxBookMap = new HashMap<>();

        for (Author author : authors) {
            System.out.println("Обработка автора: " + author.getName());
            Optional<Book> maxBook = author.getBooks().stream()
                    .peek(book -> System.out.println("Обработка книги: " + book.getTitle()))
                    .max(Comparator.comparingInt(Book::getNumberOfPages));
            authorMaxBookMap.put(author, maxBook);
        }

        return authorMaxBookMap;
    }
}
