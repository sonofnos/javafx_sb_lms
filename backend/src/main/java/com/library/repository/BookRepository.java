package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entity

 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find a book by its ISBN
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Search books by title or author (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
           "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> searchByTitleOrAuthor(@Param("title") String title, @Param("author") String author);

    /**
     * Find books by title containing the search term (case-insensitive)
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find books by author containing the search term (case-insensitive)
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);
}
