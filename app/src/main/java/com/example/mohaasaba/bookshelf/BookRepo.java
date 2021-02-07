package com.example.mohaasaba.bookshelf;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BookRepo {
    private BookDB bookDB;
    private BookDao bookDao;

    public BookRepo(Context context) {
        this.bookDB = BookDB.getInstance(context);
        this.bookDao = bookDB.bookDao();
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
    public LiveData<List<Book>> getAllCollectedBooks() {
        return bookDao.getAllCollectedBooks();
    }
    public LiveData<List<Book>> getAllWishListedBooks() {
        return bookDao.getAllWishListedBooks();
    }
}
