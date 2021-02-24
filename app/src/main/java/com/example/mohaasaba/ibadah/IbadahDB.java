package com.example.mohaasaba.ibadah;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mohaasaba.database.DataConverter;
import com.example.mohaasaba.ibadah.bookshelf.Book;
import com.example.mohaasaba.ibadah.bookshelf.BookDao;
import com.example.mohaasaba.ibadah.tasbih.Tasbih;
import com.example.mohaasaba.ibadah.tasbih.TasbihDao;

@Database(entities = {Book.class, Tasbih.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class IbadahDB extends RoomDatabase {
    private static IbadahDB bookDatabaseInstance;

    public abstract BookDao bookDao();
    public abstract TasbihDao tasbihDao();

    public static synchronized IbadahDB getInstance(Context context) {
        if (bookDatabaseInstance == null) {
            bookDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), IbadahDB.class, "bookDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return bookDatabaseInstance;
    }
}
