package org.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookServiceTest {

    @Mock
    private List<Book> bookDatabaseMock;
    private BookService bookService;
    private User user;
    private Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookDatabaseMock = new ArrayList<>();
        bookService = new BookService(bookDatabaseMock);
        user = mock(User.class);

    }

    // Test searching for book by title
    @Test
    public void testSearchBookByTitle() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        bookDatabaseMock.add(book);

        // Act
        List<Book> results = bookService.searchBook("Green Eggs and Ham");

        // Assert
        assertFalse(results.isEmpty());
    }

    // Test searching for book by title that is not in the library
    @Test
    public void testSearchBookByTitleNotThere() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        bookDatabaseMock.add(book);

        // Act
        List<Book> results = bookService.searchBook("Where the Wild Things Are");

        // Assert
        assertTrue(results.isEmpty());
    }

    //Test searching multiple books by the same author
    @Test
    public void testSearchByAuthorOfMultipleBooks() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        Book book2 = new Book("Hop On Pop", "Dr Seuss", "Children", 15);
        bookDatabaseMock.add(book);
        bookDatabaseMock.add(book2);

        // Act
        List<Book> results = bookService.searchBook("Dr Seuss");

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(2, results.size());
        assertEquals(book, results.get(0));
        assertEquals(book2, results.get(1));
    }

    // Test to purchase book successfully
    @Test
    public void testPurchaseBookSuccess() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        bookDatabaseMock.add(book);

        // Act
        boolean purchaseSuccess = bookService.purchaseBook(user, book);

        // Assert
        assertTrue(purchaseSuccess);
    }

    // Test to fail purchasing book not in library
    @Test
    public void testPurchaseBookFailure() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);

        // Act
        boolean purchaseSuccess = bookService.purchaseBook(user, book);

        // Assert
        assertFalse(purchaseSuccess);
    }

    //test to ensure that a user must exist to buy books
    @Test
    public void testPurchaseBookWithNullUser() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);

        // Act
        boolean purchaseSuccess = bookService.purchaseBook(null, book);

        // Assert
        assertFalse(purchaseSuccess);
    }

    // Tests for addBookReview method
    @Test
    public void testAddBookReviewSuccess() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        when(user.getPurchasedBooks()).thenReturn(List.of(book));
        String review = "Made me hungry...";

        // Act
        boolean reviewSuccess = bookService.addBookReview(user, book, review);

        // Assert
        assertTrue(reviewSuccess);
        assertTrue(book.getReviews().contains(review));
    }

    //test to make sure user owns a book before reviewing it
    @Test
    public void testAddBookReviewFailureWhenBookNotPurchased() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        when(user.getPurchasedBooks()).thenReturn(List.of());
        String review = "this book is flat earth propaganda!!!";

        // Act
        boolean reviewSuccess = bookService.addBookReview(user, book, review);

        // Assert
        assertFalse(reviewSuccess);
    }

    //test to ensure the user actually reviewed the book
    @Test
    public void testAddBookReviewWithNullReview() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        when(user.getPurchasedBooks()).thenReturn(List.of(book));

        // Act
        boolean reviewSuccess = bookService.addBookReview(user, book, null);

        // Assert
        assertFalse(reviewSuccess);
    }

    // Test adding book successfully
    @Test
    public void testAddBookSuccess() {
        //Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);

        // Act
        boolean addSuccess = bookService.addBook(book);

        // Assert
        assertTrue(addSuccess);
        assertTrue(bookDatabaseMock.contains(book));
    }

    //test to fail adding duplicate books
    @Test
    public void testAddBookFailureWhenBookAlreadyExists() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        bookDatabaseMock.add(book);

        // Act
        boolean addSuccess = bookService.addBook(book);

        // Assert
        assertFalse(addSuccess);
    }

    //test to fail adding a null book
    @Test
    public void testAddBookWithNullBook() {
        // Act
        boolean addSuccess = bookService.addBook(null);

        // Assert
        assertFalse(addSuccess);
    }

    // Test removing a book in the library
    @Test
    public void testRemoveBookSuccess() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);
        bookDatabaseMock.add(book);

        // Act
        boolean removeSuccess = bookService.removeBook(book);

        // Assert
        assertTrue(removeSuccess);
        assertFalse(bookDatabaseMock.contains(book));
    }

    //test failing to remove a book when not in library
    @Test
    public void testRemoveBookFailureWhenBookNotInDatabase() {
        // Arrange
        book = new Book("Green Eggs and Ham", "Dr Seuss", "Children", 10);

        // Act
        boolean removeSuccess = bookService.removeBook(book);

        // Assert
        assertFalse(removeSuccess);
    }

    //test you cannot remove a null book
    @Test
    public void testRemoveBookWithNullBook() {
        // Act
        boolean removeSuccess = bookService.removeBook(null);

        // Assert
        assertFalse(removeSuccess);
    }

}
