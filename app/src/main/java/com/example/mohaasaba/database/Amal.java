package com.example.mohaasaba.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mohaasaba.helper.IdGenerator;

import java.util.Calendar;

@Entity
public class Amal {

    enum AmalType {
        Farz,  //70-700
        Wazib, //10-70
        Sunnah, //1-10
        Mustahab, // 1
        Mubah, // base 0
        Maqruh_tanzihi, // -0
        Maqruh_tahrimi, // -0
        Gunah_Sagira,  // -1
        Gunah_Kabira,  // -10
    }

    @PrimaryKey
    private String amalID;

    public Long amalCompletionTime;
    public AmalType type;
    public boolean amalCompleted;
    public int perfection; //perfection in %
    public String title;


    public Amal (String title, AmalType type) {
        this.amalID = IdGenerator.getNewID();
        this.type = type;
        this.title = title;
    }

    public void done() {
        amalCompletionTime = Calendar.getInstance().getTimeInMillis();
        amalCompleted = true;
    }
    public void undo() {
        amalCompleted = false;
        amalCompletionTime = null;
    }
    public String getType() {
        return type.getClass().getName();
    }

    public String getAmalID() { return amalID; }

    public void setAmalID(String amalID) {
        this.amalID = amalID;
    }
}
