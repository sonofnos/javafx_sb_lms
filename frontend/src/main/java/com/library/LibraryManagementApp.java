package com.library;

import com.library.model.Book;
import com.library.service.BookService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Main JavaFX Application for CBA LMS Test
 */
public class LibraryManagementApp extends Application {


    private final BookService bookService = new BookService();


    private final ObservableList<Book> bookList = FXCollections.observableArrayList();


    private Stage primaryStage;

    // UI Components
    private TableView<Book> bookTableView;
    private TextField titleField;
    private TextField authorField;
    private TextField isbnField;
    private DatePicker publishedDatePicker;
    private TextField searchField;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button refreshButton;
    private Button clearButton;
    private Button searchButton;

    // Currently selected book
    private Book selectedBook;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("CBA LMS Test");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Create header
        VBox headerBox = new VBox();
        headerBox.getChildren().addAll(createHeader(), createSearchPanel());
        
        // Create and add components
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(createTableView());
        mainLayout.setRight(createFormPanel());
        mainLayout.setBottom(createStatusBar());

        // Create scene
        Scene scene = new Scene(mainLayout, 1400, 800);
        

        scene.getStylesheets().add(getClass().getResource("/styles.css") != null ? 
            getClass().getResource("/styles.css").toExternalForm() : "");

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
        primaryStage.show();

        
        new Thread(() -> {
            try {
                Thread.sleep(1000); 
                Platform.runLater(() -> loadBooks());
            } catch (InterruptedException e) {
                // Ignore
            }
        }, "BookLoader").start();
    }

 
    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%); " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        Label titleLabel = new Label("CBA LMS Test");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; " +
                           "-fx-text-fill: white; -fx-font-family: 'System';");

        Label subtitleLabel = new Label("Test for CBA Java");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9); " +
                              "-fx-font-family: 'System';");

        VBox titleBox = new VBox(5);
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statsLabel = new Label("Total Books: 0");
        statsLabel.setId("statsLabel");
        statsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-background-color: rgba(255,255,255,0.2); " +
                           "-fx-padding: 10 20; -fx-background-radius: 20;");

        header.getChildren().addAll(titleBox, spacer, statsLabel);
        return header;
    }


    private HBox createSearchPanel() {
        HBox searchPanel = new HBox(15);
        searchPanel.setPadding(new Insets(20, 30, 20, 30));
        searchPanel.setAlignment(Pos.CENTER_LEFT);
        searchPanel.setStyle("-fx-background-color: white; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        Label searchLabel = new Label("Search:");
        searchLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        searchField = new TextField();
        searchField.setPromptText("Search by title or author...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 10; " +
                            "-fx-background-radius: 5; -fx-border-color: #ddd; " +
                            "-fx-border-radius: 5; -fx-border-width: 1;");
        
        // Search on Enter key
        searchField.setOnAction(e -> performSearch());

        searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                             "-fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 5; " +
                             "-fx-cursor: hand; -fx-font-weight: bold;");
        searchButton.setOnMouseEntered(e -> searchButton.setStyle(
            "-fx-background-color: #5568d3; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 5; " +
            "-fx-cursor: hand; -fx-font-weight: bold;"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle(
            "-fx-background-color: #667eea; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 5; " +
            "-fx-cursor: hand; -fx-font-weight: bold;"));
        searchButton.setOnAction(e -> performSearch());

        Button clearSearchButton = new Button("Clear");
        clearSearchButton.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333; " +
                                  "-fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 5; " +
                                  "-fx-cursor: hand;");
        clearSearchButton.setOnMouseEntered(e -> clearSearchButton.setStyle(
            "-fx-background-color: #d0d0d0; -fx-text-fill: #333; " +
            "-fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 5; " +
            "-fx-cursor: hand;"));
        clearSearchButton.setOnMouseExited(e -> clearSearchButton.setStyle(
            "-fx-background-color: #e0e0e0; -fx-text-fill: #333; " +
            "-fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 5; " +
            "-fx-cursor: hand;"));
        clearSearchButton.setOnAction(e -> {
            searchField.clear();
            loadBooks();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                              "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5; " +
                              "-fx-cursor: hand; -fx-font-weight: bold;");
        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle(
            "-fx-background-color: #45a049; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5; " +
            "-fx-cursor: hand; -fx-font-weight: bold;"));
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle(
            "-fx-background-color: #4CAF50; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5; " +
            "-fx-cursor: hand; -fx-font-weight: bold;"));
        refreshButton.setOnAction(e -> loadBooks());

        searchPanel.getChildren().addAll(searchLabel, searchField, searchButton, 
                                         clearSearchButton, spacer, refreshButton);
        return searchPanel;
    }


    private VBox createTableView() {
        VBox tableBox = new VBox(15);
        tableBox.setPadding(new Insets(20, 30, 20, 30));
        tableBox.setStyle("-fx-background-color: white; " +
                         "-fx-background-radius: 10; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        VBox.setMargin(tableBox, new Insets(20, 20, 20, 20));

        Label tableLabel = new Label("Book Collection");
        tableLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

        bookTableView = new TableView<>();
        bookTableView.setItems(bookList);
        bookTableView.setStyle("-fx-background-color: white; " +
                              "-fx-table-cell-border-color: #e0e0e0;");
        VBox.setVgrow(bookTableView, Priority.ALWAYS);

        TableColumn<Book, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(60);
        idColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(300);
        titleColumn.setStyle("-fx-font-weight: bold;");

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(220);

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setPrefWidth(150);
        isbnColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Book, String> publishedDateColumn = new TableColumn<>("Published Date");
        publishedDateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getPublishedDate();
            return new SimpleStringProperty(date != null ? date.toString() : "");
        });
        publishedDateColumn.setPrefWidth(140);
        publishedDateColumn.setStyle("-fx-alignment: CENTER;");

        bookTableView.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, publishedDateColumn);

        bookTableView.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f5f5ff; -fx-cursor: hand;");
                }
            });
            row.setOnMouseExited(event -> {
                row.setStyle("");
            });
            return row;
        });

        bookTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleBookSelection(newValue));

        tableBox.getChildren().addAll(tableLabel, bookTableView);
        VBox.setVgrow(bookTableView, Priority.ALWAYS);

        return tableBox;
    }

    private VBox createFormPanel() {
        VBox formPanel = new VBox(20);
        formPanel.setPadding(new Insets(20, 30, 20, 30));
        formPanel.setPrefWidth(400);
        formPanel.setStyle("-fx-background-color: white; " +
                          "-fx-background-radius: 10; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        VBox.setMargin(formPanel, new Insets(20, 20, 20, 0));

        Label formLabel = new Label("Book Details");
        formLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");


        VBox fieldsBox = new VBox(15);

        // Title field
        VBox titleBox = new VBox(8);
        Label titleLabel = new Label("Title");
        titleLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555;");
        titleField = new TextField();
        titleField.setPromptText("Enter book title");
        titleField.setStyle("-fx-font-size: 14px; -fx-padding: 12; " +
                           "-fx-background-radius: 5; -fx-border-color: #ddd; " +
                           "-fx-border-radius: 5; -fx-border-width: 1;");
        titleBox.getChildren().addAll(titleLabel, titleField);

        // Author field
        VBox authorBox = new VBox(8);
        Label authorLabel = new Label("Author");
        authorLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555;");
        authorField = new TextField();
        authorField.setPromptText("Enter author name");
        authorField.setStyle("-fx-font-size: 14px; -fx-padding: 12; " +
                            "-fx-background-radius: 5; -fx-border-color: #ddd; " +
                            "-fx-border-radius: 5; -fx-border-width: 1;");
        authorBox.getChildren().addAll(authorLabel, authorField);

        // ISBN field
        VBox isbnBox = new VBox(8);
        Label isbnLabel = new Label("ISBN");
        isbnLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555;");
        isbnField = new TextField();
        isbnField.setPromptText("Enter ISBN");
        isbnField.setStyle("-fx-font-size: 14px; -fx-padding: 12; " +
                          "-fx-background-radius: 5; -fx-border-color: #ddd; " +
                          "-fx-border-radius: 5; -fx-border-width: 1;");
        isbnBox.getChildren().addAll(isbnLabel, isbnField);

        // Published Date picker
        VBox dateBox = new VBox(8);
        Label dateLabel = new Label("Published Date");
        dateLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555;");
        publishedDatePicker = new DatePicker();
        publishedDatePicker.setPromptText("Select date");
        publishedDatePicker.setStyle("-fx-font-size: 14px;");
        publishedDatePicker.setMaxWidth(Double.MAX_VALUE);
        dateBox.getChildren().addAll(dateLabel, publishedDatePicker);

        fieldsBox.getChildren().addAll(titleBox, authorBox, isbnBox, dateBox);

        VBox buttonBox = new VBox(12);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        addButton = new Button("Add Book");
        styleButton(addButton, "#667eea", "#5568d3", true);
        addButton.setOnAction(e -> addBook());

        updateButton = new Button("Update Book");
        styleButton(updateButton, "#FF9800", "#e68900", true);
        updateButton.setDisable(true);
        updateButton.setOnAction(e -> updateBook());

        deleteButton = new Button("Delete Book");
        styleButton(deleteButton, "#f44336", "#da190b", true);
        deleteButton.setDisable(true);
        deleteButton.setOnAction(e -> deleteBook());

        clearButton = new Button("Clear Form");
        styleButton(clearButton, "#607D8B", "#546E7A", false);
        clearButton.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

        // Add separator
        Separator separator = new Separator();
        separator.setStyle("-fx-padding: 10 0;");

        formPanel.getChildren().addAll(formLabel, fieldsBox, separator, buttonBox);

        return formPanel;
    }

    private void styleButton(Button button, String normalColor, String hoverColor, boolean bold) {
        String baseStyle = "-fx-background-color: " + normalColor + "; " +
                          "-fx-text-fill: white; -fx-font-size: 14px; " +
                          "-fx-padding: 12 20; -fx-background-radius: 5; " +
                          "-fx-cursor: hand; -fx-max-width: infinity;" +
                          (bold ? " -fx-font-weight: bold;" : "");
        
        String hoverStyle = "-fx-background-color: " + hoverColor + "; " +
                           "-fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-padding: 12 20; -fx-background-radius: 5; " +
                           "-fx-cursor: hand; -fx-max-width: infinity;" +
                           (bold ? " -fx-font-weight: bold;" : "");
        
        button.setStyle(baseStyle);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(15, 30, 15, 30));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-background-color: #2c3e50; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, -2);");

        Label statusLabel = new Label("Connected to Backend");
        statusLabel.setId("statusLabel");
        statusLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label backendLabel = new Label("API: http://localhost:8080");
        backendLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-font-size: 12px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label versionLabel = new Label("v1.0.0");
        versionLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");

        statusBar.getChildren().addAll(statusLabel, backendLabel, spacer, versionLabel);
        return statusBar;
    }

    private void loadBooks() {
        
        Label statsLabel = (Label) primaryStage.getScene().lookup("#statsLabel");
        if (statsLabel != null) {
            statsLabel.setText("Loading books...");
        }
        
        new Thread(() -> {
            try {
                var books = bookService.getAllBooks();
                Platform.runLater(() -> {
                    bookList.clear();
                    bookList.addAll(books);
                    updateBookCount();
                    
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Failed to load books: " + e.getMessage());
                    if (statsLabel != null) {
                        statsLabel.setText("Total Books: 0 (Backend offline)");
                    }
                });
            }
        }, "BookFetcher").start();
    }

    /**
     * Update the book count in the header
     */
    private void updateBookCount() {
        Label statsLabel = (Label) bookTableView.getScene().lookup("#statsLabel");
        if (statsLabel != null) {
            statsLabel.setText("Total Books: " + bookList.size());
        }
    }

    private void performSearch() {
        String searchTerm = searchField.getText();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadBooks();
            return;
        }

        new Thread(() -> {
            try {
                var books = bookService.searchBooks(searchTerm);
                Platform.runLater(() -> {
                    bookList.clear();
                    bookList.addAll(books);
                    updateBookCount();
                    showInfo("Search completed. Found: " + books.size() + " book(s)");
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Search failed: " + e.getMessage()));
            }
        }).start();
    }

    private void handleBookSelection(Book book) {
        selectedBook = book;
        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            publishedDatePicker.setValue(book.getPublishedDate());

            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            addButton.setDisable(true);
        } else {
            clearForm();
        }
    }

    private void addBook() {
        if (!validateForm()) {
            return;
        }

        Book newBook = new Book(
                titleField.getText(),
                authorField.getText(),
                isbnField.getText(),
                publishedDatePicker.getValue()
        );

        new Thread(() -> {
            try {
                Book createdBook = bookService.createBook(newBook);
                Platform.runLater(() -> {
                    bookList.add(createdBook);
                    clearForm();
                    showInfo("Book added successfully!");
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Failed to add book: " + e.getMessage()));
            }
        }).start();
    }

    private void updateBook() {
        if (selectedBook == null || !validateForm()) {
            return;
        }

        selectedBook.setTitle(titleField.getText());
        selectedBook.setAuthor(authorField.getText());
        selectedBook.setIsbn(isbnField.getText());
        selectedBook.setPublishedDate(publishedDatePicker.getValue());

        new Thread(() -> {
            try {
                Book updatedBook = bookService.updateBook(selectedBook);
                Platform.runLater(() -> {
                    // Refresh the table
                    bookTableView.refresh();
                    clearForm();
                    showInfo("Book updated successfully!");
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Failed to update book: " + e.getMessage()));
            }
        }).start();
    }

    private void deleteBook() {
        if (selectedBook == null) {
            return;
        }

        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Book");
        confirmAlert.setContentText("Are you sure you want to delete: " + selectedBook.getTitle() + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Long bookId = selectedBook.getId();

            new Thread(() -> {
                try {
                    bookService.deleteBook(bookId);
                    Platform.runLater(() -> {
                        bookList.remove(selectedBook);
                        clearForm();
                        showInfo("Book deleted successfully!");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showError("Failed to delete book: " + e.getMessage()));
                }
            }).start();
        }
    }

    /**
     * Clear the form and reset buttons
     */
    private void clearForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        publishedDatePicker.setValue(null);

        selectedBook = null;
        bookTableView.getSelectionModel().clearSelection();

        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    /**
     * Validate form fields
     */
    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            showError("Title is required");
            return false;
        }
        if (authorField.getText().trim().isEmpty()) {
            showError("Author is required");
            return false;
        }
        if (isbnField.getText().trim().isEmpty()) {
            showError("ISBN is required");
            return false;
        }
        if (publishedDatePicker.getValue() == null) {
            showError("Published date is required");
            return false;
        }
        return true;
    }

    /**
     * Show error alert
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show information alert
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
