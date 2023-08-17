package com.example.dummy_name;

import java.util.Calendar;

import kotlin.Pair;

public class DNDSetter {

    private final Pair<Integer, Integer> startTime;
    private final Pair<Integer, Integer> endTime;

    DNDSetter(Pair<Integer, Integer> startTime, Pair<Integer, Integer> endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartAsMilliseconds(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, startTime.getFirst());
        calendar.set(Calendar.MINUTE, startTime.getSecond());
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public long getEndAsMilliseconds(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, endTime.getFirst());
        calendar.set(Calendar.MINUTE, endTime.getSecond());
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }


}
