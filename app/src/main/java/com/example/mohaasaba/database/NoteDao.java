package com.example.mohaasaba.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {
    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("DELETE FROM note_table WHERE noteID is :noteId")
    void deleteNote(String noteId);

    @Query("SELECT * FROM note_table WHERE noteID IS :noteID")
    LiveData<Note> getNote(String noteID);
}
