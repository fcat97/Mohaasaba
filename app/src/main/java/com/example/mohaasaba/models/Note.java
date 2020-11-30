package com.example.mohaasaba.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;

import java.util.Calendar;

@Entity(tableName = "note_table")
public class Note implements Parcelable {

    @PrimaryKey
    @NonNull
    private String noteID;

    private String title;
    private String detail;

    private Calendar dateCreated;
    private Calendar dateModified;

    public Note(String detail) {
        this.noteID = IdGenerator.getNewID();
        this.detail = detail;
        this.dateCreated = Calendar.getInstance();
        this.dateModified = dateCreated;
    }

    public String getNoteID() {
        return noteID;
    }
    public void setNoteID(String ID) {
        this.noteID = ID;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public Calendar getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }
    public Calendar getDateModified() {
        return dateModified;
    }
    public void setDateModified(Calendar dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.noteID);
        dest.writeString(this.title);
        dest.writeString(this.detail);
        dest.writeSerializable(this.dateCreated);
        dest.writeSerializable(this.dateModified);
    }

    protected Note(Parcel in) {
        this.noteID = in.readString();
        this.title = in.readString();
        this.detail = in.readString();
        this.dateCreated = (Calendar) in.readSerializable();
        this.dateModified = (Calendar) in.readSerializable();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
