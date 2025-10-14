package com.library.config;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Data initializer to populate the database with sample books on startup
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Autowired
    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if database is already populated
        if (bookRepository.count() > 0) {
            System.out.println("Database already contains books. Skipping initialization.");
            return;
        }

        System.out.println("Initializing database with sample books...");

        // Create sample books
        Book book1 = new Book(
                "Clean Code: A Handbook of Agile Software Craftsmanship",
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
        );

        Book book2 = new Book(
                "Effective Java",
                "Joshua Bloch",
                "978-0134685991",
                LocalDate.of(2017, 12, 27)
        );

        Book book3 = new Book(
                "Design Patterns: Elements of Reusable Object-Oriented Software",
                "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
                "978-0201633610",
                LocalDate.of(1994, 10, 31)
        );

        Book book4 = new Book(
                "The Pragmatic Programmer",
                "Andrew Hunt, David Thomas",
                "978-0135957059",
                LocalDate.of(2019, 9, 13)
        );

        Book book5 = new Book(
                "Head First Design Patterns",
                "Eric Freeman, Elisabeth Robson",
                "978-0596007126",
                LocalDate.of(2004, 10, 25)
        );

        Book book6 = new Book(
                "Spring in Action",
                "Craig Walls",
                "978-1617294945",
                LocalDate.of(2018, 10, 9)
        );

        Book book7 = new Book(
                "Java: The Complete Reference",
                "Herbert Schildt",
                "978-1260440232",
                LocalDate.of(2018, 11, 1)
        );

        Book book8 = new Book(
                "Introduction to Algorithms",
                "Thomas H. Cormen, Charles E. Leiserson",
                "978-0262033848",
                LocalDate.of(2009, 7, 31)
        );

        Book book9 = new Book(
                "Refactoring: Improving the Design of Existing Code",
                "Martin Fowler",
                "978-0134757599",
                LocalDate.of(2018, 11, 19)
        );

        Book book10 = new Book(
                "You Don't Know JS: Scope & Closures",
                "Kyle Simpson",
                "978-1449335588",
                LocalDate.of(2014, 3, 24)
        );

        Book book11 = new Book(
                "Head First Java",
                "Kathy Sierra, Bert Bates",
                "978-0596009205",
                LocalDate.of(2005, 2, 9)
        );

        Book book12 = new Book(
                "Code Complete",
                "Steve McConnell",
                "978-0735619678",
                LocalDate.of(2004, 6, 9)
        );

        Book book13 = new Book(
                "The Mythical Man-Month",
                "Frederick P. Brooks Jr.",
                "978-0201835953",
                LocalDate.of(1995, 8, 12)
        );

        Book book14 = new Book(
                "Domain-Driven Design",
                "Eric Evans",
                "978-0321125215",
                LocalDate.of(2003, 8, 30)
        );

        Book book15 = new Book(
                "Microservices Patterns",
                "Chris Richardson",
                "978-1617294549",
                LocalDate.of(2018, 11, 19)
        );

        Book book16 = new Book(
                "Building Microservices",
                "Sam Newman",
                "978-1492034025",
                LocalDate.of(2021, 9, 28)
        );

        Book book17 = new Book(
                "Continuous Delivery",
                "Jez Humble, David Farley",
                "978-0321601919",
                LocalDate.of(2010, 7, 27)
        );

        // Save all books to database
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
        bookRepository.save(book6);
        bookRepository.save(book7);
        bookRepository.save(book8);
        bookRepository.save(book9);
        bookRepository.save(book10);
        bookRepository.save(book11);
        bookRepository.save(book12);
        bookRepository.save(book13);
        bookRepository.save(book14);
        bookRepository.save(book15);
        bookRepository.save(book16);
        bookRepository.save(book17);

        System.out.println("Database initialized with " + bookRepository.count() + " sample books.");
    }
}
