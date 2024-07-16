package com.rntgroup.collectiontostream.statistics;

import com.rntgroup.collectiontostream.entities.Author;
import com.rntgroup.collectiontostream.entities.Book;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooksStatisticsTest extends StatisticsTestBase {

    @Test
    public void testAnyBookOver200Pages() {
        assertTrue(bookStatistics.anyBookOver200Pages());
    }

    @Test
    public void testMaxPagesBook() {
        Optional<Book> maxPagesBook = bookStatistics.maxPagesBook();
        assertTrue(maxPagesBook.isPresent());
        assertEquals("Идиот", maxPagesBook.get().getTitle());
    }

    @Test
    public void testMinPagesBook() {
        Optional<Book> minPagesBook = bookStatistics.minPagesBook();
        assertTrue(minPagesBook.isPresent());
        assertEquals("Война миров", minPagesBook.get().getTitle());
    }

    @Test
    public void testSingleAuthorBooks() {
        List<Book> singleAuthorBooks = bookStatistics.singleAuthorBooks();
        assertEquals(4, singleAuthorBooks.size());
        assertTrue(singleAuthorBooks.stream().anyMatch(book -> book.getTitle().equals("Идиот")));
        assertTrue(singleAuthorBooks.stream().anyMatch(book -> book.getTitle().equals("Скверный анекдот")));
        assertTrue(singleAuthorBooks.stream().anyMatch(book -> book.getTitle().equals("Портрет Дориана Грея")));
        assertTrue(singleAuthorBooks.stream().anyMatch(book -> book.getTitle().equals("Война миров")));
    }

    @Test
    public void testSortedBooks() {
        List<Book> sortedBooks = bookStatistics.sortedBooks();
        assertEquals(5, sortedBooks.size());
        assertEquals("Война миров", sortedBooks.get(0).getTitle());
        assertEquals("Портрет Дориана Грея", sortedBooks.get(1).getTitle());
        assertEquals("Скверный анекдот", sortedBooks.get(2).getTitle());
        assertEquals("Двенадцать стульев", sortedBooks.get(3).getTitle());
        assertEquals("Идиот", sortedBooks.get(4).getTitle());
    }

    @Test
    public void testBookTitles() {
        List<String> bookTitles = bookStatistics.bookTitles();
        assertEquals(5, bookTitles.size());
        assertTrue(bookTitles.contains("Двенадцать стульев"));
        assertTrue(bookTitles.contains("Идиот"));
        assertTrue(bookTitles.contains("Скверный анекдот"));
        assertTrue(bookTitles.contains("Портрет Дориана Грея"));
        assertTrue(bookTitles.contains("Война миров"));
    }

    @Test
    public void testAllAuthors() {
        List<Author> allAuthors = bookStatistics.allAuthors();
        assertEquals(5, allAuthors.size());
        assertTrue(allAuthors.stream().anyMatch(author -> author.getName().equals("Илья Ильф")));
        assertTrue(allAuthors.stream().anyMatch(author -> author.getName().equals("Евгений Петров")));
        assertTrue(allAuthors.stream().anyMatch(author -> author.getName().equals("Федор Достоевский")));
        assertTrue(allAuthors.stream().anyMatch(author -> author.getName().equals("Оскар Уальд")));
        assertTrue(allAuthors.stream().anyMatch(author -> author.getName().equals("Герберт Уэллс")));
    }
}
