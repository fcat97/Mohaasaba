package com.example.mohaasaba.bookshelf;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM bookshelf WHERE readingStatus LIKE '%READING%'")
    LiveData<List<Book>> getAllReadingBooks();

    @Query("SELECT * FROM bookshelf WHERE readingStatus LIKE '%READ%'")
    LiveData<List<Book>> getAllCollectedBooks();

    @Query("SELECT * FROM bookshelf WHERE readingStatus LIKE '%WISH_LISTED%'")
    LiveData<List<Book>> getAllWishListedBooks();

    @Query("SELECT * FROM bookshelf WHERE bookID IS :bookID")
    Book getBook(String bookID);
}
