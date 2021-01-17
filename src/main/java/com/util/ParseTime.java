package com.util;

import org.apache.log4j.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ParseTime {

    private static final Logger logger = Logger.getLogger(ParseTime.class);
    //
    // 3001 -> [00:50:01], 3665 -> [01:01:05]
    public static String se2Time(Long microSecond){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Long second = microSecond / 1000;
        Long hour = second / 3600;
        Long minute = (second - (hour * 3600)) / 60;
        Long otherSecond = second - (hour * 3600) - (minute * 60);
        String result = hour + ":" + minute + ":" + otherSecond; // h:m:s
        String returnTime = "";

        try{
            returnTime = "[" + format.format(format.parse(result)) + "]";
        }catch (ParseException pe){
            logger.error(pe);
            logger.error("Time Parsing error");
        }
        return returnTime;
    }
}
