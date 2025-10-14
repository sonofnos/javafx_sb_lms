package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Get all books with pagination support
     */
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    /**
     * Get a single book by its ID
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Create a new book
     */
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Update an existing book
     */
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        // Update the book fields
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublishedDate(bookDetails.getPublishedDate());

        return bookRepository.save(book);
    }

    /**
     * Delete a book by its ID
     */
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    /**
     * Search books by title or author
     */
    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.searchByTitleOrAuthor(searchTerm, searchTerm);
    }
}
