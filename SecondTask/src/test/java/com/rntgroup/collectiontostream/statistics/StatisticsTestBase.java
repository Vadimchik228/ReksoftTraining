package com.rntgroup.collectiontostream.statistics;

import com.rntgroup.collectiontostream.entities.Author;
import com.rntgroup.collectiontostream.entities.Book;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public class StatisticsTestBase {

    public Author ilf;
    public Author petrov;
    public Author dostoevsky;
    public Author wilde;
    public Author wells;
    public Author remark;

    public Book twelveChairs;
    public Book idiot;
    public Book nastyJoke;
    public Book thePictureOfDorianGrey;
    public Book theWarOfTheWorlds;

    public BooksStatistics bookStatistics;
    public AuthorsStatistics authorStatistics;

    @BeforeEach
    public void setUp() {
        ilf = new Author("Илья Ильф", (short) 39, new ArrayList<>());
        petrov = new Author("Евгений Петров", (short) 39, new ArrayList<>());
        dostoevsky = new Author("Федор Достоевский", (short) 59, new ArrayList<>());
        wilde = new Author("Оскар Уальд", (short) 46, new ArrayList<>());
        wells = new Author("Герберт Уэллс", (short) 79, new ArrayList<>());
        remark = new Author("Эрих Ремарк", (short) 72, new ArrayList<>());

        twelveChairs = new Book("Двенадцать стульев", List.of(ilf, petrov), 384);
        ilf.addBook(twelveChairs);
        petrov.addBook(twelveChairs);

        idiot = new Book("Идиот", List.of(dostoevsky), 640);
        dostoevsky.addBook(idiot);

        nastyJoke = new Book("Скверный анекдот", List.of(dostoevsky), 352);
        dostoevsky.addBook(nastyJoke);

        thePictureOfDorianGrey = new Book("Портрет Дориана Грея", List.of(wilde), 320);
        wilde.addBook(thePictureOfDorianGrey);

        theWarOfTheWorlds = new Book("Война миров", List.of(wells), 224);
        wells.addBook(theWarOfTheWorlds);

        Author[] authors = {ilf, petrov, dostoevsky, wilde, wells, remark};
        Book[] books = {twelveChairs, idiot, nastyJoke, thePictureOfDorianGrey, theWarOfTheWorlds};

        bookStatistics = new BooksStatistics(books);
        authorStatistics = new AuthorsStatistics(authors);
    }

}
