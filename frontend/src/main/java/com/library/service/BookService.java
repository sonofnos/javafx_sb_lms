package com.library.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.library.model.Book;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    private static final String BASE_URL = "http://localhost:8080/api/books";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BookService() {

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();


        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Fetch all books from the backend API
     */
    public List<Book> getAllBooks() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parse JSON array of books
            return objectMapper.readValue(response.body(), new TypeReference<List<Book>>() {});
        } else {
            throw new Exception("Failed to fetch books. Status code: " + response.statusCode());
        }
    }

    /**
     * Search books by title or author
     */
    public List<Book> searchBooks(String searchTerm) throws Exception {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllBooks();
        }

        String url = BASE_URL + "?search=" + searchTerm;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Book>>() {});
        } else {
            throw new Exception("Failed to search books. Status code: " + response.statusCode());
        }
    }

    /**
     * Create a new book
     */
    public Book createBook(Book book) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(book);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Book.class);
        } else {
            throw new Exception("Failed to create book. Status code: " + response.statusCode() + 
                              ". Response: " + response.body());
        }
    }

    /**
     * Update an existing book
     */
    public Book updateBook(Book book) throws Exception {
        if (book.getId() == null) {
            throw new IllegalArgumentException("Book ID cannot be null for update operation");
        }

        String jsonBody = objectMapper.writeValueAsString(book);
        String url = BASE_URL + "/" + book.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Book.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("Book not found with ID: " + book.getId());
        } else {
            throw new Exception("Failed to update book. Status code: " + response.statusCode() + 
                              ". Response: " + response.body());
        }
    }

    /**
     * Delete a book by ID
     */
    public void deleteBook(Long bookId) throws Exception {
        if (bookId == null) {
            throw new IllegalArgumentException("Book ID cannot be null for delete operation");
        }

        String url = BASE_URL + "/" + bookId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204 || response.statusCode() == 200) {
            // Successfully deleted
            return;
        } else if (response.statusCode() == 404) {
            throw new Exception("Book not found with ID: " + bookId);
        } else {
            throw new Exception("Failed to delete book. Status code: " + response.statusCode() + 
                              ". Response: " + response.body());
        }
    }
}
