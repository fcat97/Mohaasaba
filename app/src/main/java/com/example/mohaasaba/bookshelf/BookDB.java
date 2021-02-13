package com.example.mohaasaba.bookshelf;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mohaasaba.database.DataConverter;

@Database(entities = Book.class, version = 1)
@TypeConverters({DataConverter.class})
public abstract class BookDB extends RoomDatabase {
    private static BookDB bookDatabaseInstance;

    public abstract BookDao bookDao();

    public static synchronized BookDB getInstance(Context context) {
        if (bookDatabaseInstance == null) {
            bookDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), BookDB.class, "bookDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return bookDatabaseInstance;
    }
}
