package com.maveric.balances.utils;

import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class Utils {
    public static java.sql.Date convertDateUtilToSql(java.util.Date date){
        LocalDateTime conv= LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate convDate=conv.toLocalDate();
        return java.sql.Date.valueOf(convDate);
    }

    public  static  java.sql.Date getCurrentDate(){
        java.util.Date currentDate = new java.util.Date();
        return convertDateUtilToSql(currentDate);
    }

    public  static java.util.Date convertSqlToUtilDate(java.sql.Date date){
        return  new java.util.Date(date.getTime());
    }

    public  static  java.util.Date getCurrentDateUtil(){
        java.util.Date currentDate = new java.util.Date();
        return currentDate;
    }
//change method name
    public  static  String convertDateToString(java.sql.Date sqlDate) {
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // converting the util date into string format
        final String stringDate = dateFormat.format(utilDate);
        return stringDate;
    }
}
