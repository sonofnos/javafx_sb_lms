package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Book operations
 */
@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*") 
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * GET /api/books - Fetch all books
     */
    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search) {
        
        try {
            // If search parameter is provided, perform search
            if (search != null && !search.trim().isEmpty()) {
                List<Book> books = bookService.searchBooks(search);
                return ResponseEntity.ok(books);
            }

            // If pagination parameters are provided, return paginated results
            if (page != null && size != null) {
                Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
                Page<Book> bookPage = bookService.getAllBooks(pageable);
                return ResponseEntity.ok(bookPage);
            }

            // Otherwise, return all books
            List<Book> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching books: " + e.getMessage());
        }
    }

    /**
     * GET /api/books/{id} - Get a single book by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            return bookService.getBookById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching book: " + e.getMessage());
        }
    }

    /**
     * POST /api/books - Add a new book
     */
    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating book: " + e.getMessage());
        }
    }

    /**
     * PUT /api/books/{id} - Update a book's details
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating book: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/books/{id} - Delete a book by its ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting book: " + e.getMessage());
        }
    }
}
