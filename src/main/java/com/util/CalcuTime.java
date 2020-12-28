package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CalcuTime {

    // 3001 -> [00:50:01], 3665 -> [01:01:05]
    public static String se2Time(Long second){
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        Long hour = second / 3600;
        Long minute = (second - (hour * 3600)) / 60;
        Long otherSecond = second - (hour * 3600) - (minute * 60);
        String result = hour + ":" + minute + ":" + otherSecond; // h:m:s
        String returnTime = "";

        try{
            returnTime = "[" + format.format(format.parse(result)) + "]";
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return returnTime;
    }
}
