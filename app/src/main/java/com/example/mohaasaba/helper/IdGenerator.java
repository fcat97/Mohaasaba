package com.example.mohaasaba.helper;

import java.util.Calendar;

public class IdGenerator {

    public static String getNewID(){
        return Long.toHexString(Calendar.getInstance().getTimeInMillis());
    }
}
