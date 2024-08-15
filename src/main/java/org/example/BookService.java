package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private List<Book> bookDatabase = new ArrayList<>(); // A list to simulate a book database

    //added overloaded constructors
    public BookService() {
    }


    public BookService(List<Book> bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    public List<Book> searchBook(String keyword) {
        // Search book by title, author, or genre
        return bookDatabase.stream()
                .filter(book -> book.getTitle().contains(keyword) ||
                        book.getAuthor().contains(keyword) ||
                        book.getGenre().contains(keyword))
                .collect(Collectors.toList());
    }

    public boolean purchaseBook(User user, Book book) {
        // In real world, this should check user's balance, availability of the book, and then make a transaction
        // But for now, we just check if the book exists in our "database"
        return bookDatabase.contains(book);
    }

    //adjusted code so a review must be written in order to be submitted
    public boolean addBookReview(User user, Book book, String review) {
        // Logic to add book review
        if (!user.getPurchasedBooks().contains(book) || review == null) {
            return false; // User has not purchased this book or review is null
        }

        book.getReviews().add(review);
        return true; // Review added successfully
    }

    //changed so cannot add null book
    public boolean addBook(Book book) {
        if (book==null || bookDatabase.contains(book)) {
            return false; // Book is already in the database
        }

        bookDatabase.add(book);
        return true; // Book added successfully
    }

    //adjusted code slightly so you cannot remove null books
    public boolean removeBook(Book book) {
        if (book == null || !bookDatabase.contains(book)) {
            return false;
        }
        return bookDatabase.remove(book); // Book removed successfully if it was in the database
    }
}
