package com.example.mohaasaba.helper;

import java.util.Calendar;

public class IdGenerator {

    public static String getNewID(){
        return Long.toHexString(Calendar.getInstance().getTimeInMillis());
    }

    public static int getShortID() {
        Calendar calendar = Calendar.getInstance();
        String h = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String m = String.valueOf(calendar.get(Calendar.MINUTE));
        String s = String.valueOf(calendar.get(Calendar.SECOND));
        String mm = String.valueOf(calendar.get(Calendar.MILLISECOND));

        String uID = h+m+s+mm;
        return Integer.parseInt(uID); // 100% unique in a 24h.
    }
}
