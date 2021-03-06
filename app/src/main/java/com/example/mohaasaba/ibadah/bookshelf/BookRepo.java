package com.example.mohaasaba.ibadah.bookshelf;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mohaasaba.ibadah.IbadahDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BookRepo {
    private IbadahDB ibadahDB;
    private BookDao bookDao;

    public BookRepo(Context context) {
        this.ibadahDB = IbadahDB.getInstance(context);
        this.bookDao = ibadahDB.bookDao();
    }


    private Book getBook(String bookID) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Book> result = executorService.submit(new Callable<Book>() {
            @Override
            public Book call() throws Exception {
                return bookDao.getBook(bookID);
            }
        });

        Book book = null;
        try {
            book = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return book;
    }

    public void updateBook(Book book) {
        Book b = getBook(book.bookID);
        Thread thread;
        if (b != null) {
            thread = new Thread(() -> {
                bookDao.update(book);
            });
        } else {
            thread = new Thread(() -> {
                bookDao.insert(book);
            });
        }
        thread.start();

    }

    public void deleteBook(Book book) {
        Thread thread = new Thread(() -> {
            bookDao.delete(book);
        });

        thread.start();
    }

    public LiveData<List<Book>> getAllReadingBooks() {
        return bookDao.getAllReadingBooks();
    }
    public LiveData<List<Book>> getAllReadBooks() {
        return bookDao.getAllCollectedBooks();
    }
    public LiveData<List<Book>> getAllWishListedBooks() {
        return bookDao.getAllWishListedBooks();
    }
    public LiveData<List<Book>> getAllBooks() {
        return bookDao.getAllBooks();
    }
    public List<Book> getAllBook() {
        List<Book> books = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Book>> result = executorService.submit(() -> bookDao.getAllBook());

        try {
            books.addAll(result.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return books;
    }
}
