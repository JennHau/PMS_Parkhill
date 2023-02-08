/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Winson
 */
public class PMS_DateTimeFormatter{
    public LocalDate formatDate(String date) {
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        
        return localDate;
    }
    
    public LocalDate formatDate2(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);

        return localDate;
    }
    
    public String currentDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateTime = LocalDateTime.now().format(dateTimeFormatter);
        return dateTime;
    }
    
    public String changeFormatDate(String dateString) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = originalFormat.parse(dateString);
        
        return targetFormat.format(date);
    }
    
    public String changeFormatDate2(String dateString) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = originalFormat.parse(dateString);
        
        return targetFormat.format(date);
    }
    
    public LocalTime formatTime(String time) {
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        
        LocalTime localTime = LocalTime.parse(LocalTime.parse(time).format(timeFormatter));
        
        return localTime;
    }
    
    public LocalTime getTimeCategory(LocalTime time) {
        int timeMin = time.getMinute();
        if (timeMin < 30) {
            time = time.withMinute(00);
        }
        else {
            time = time.withMinute(30);
        }
        
        return time;
    }
    
    public String getDateTimeNow() {
        LocalDate dateNow = formatDate(String.valueOf(LocalDate.now()));
        LocalTime timeNow = formatTime(String.valueOf(LocalTime.now()));
        
        return String.valueOf(dateNow + " " + timeNow);
    }
    
    public LocalDateTime combineStringDateTime(String date, String time) {
        LocalDate dateLocal = formatDate(date);
        LocalTime timeLocal = formatTime(time);
        
        LocalDateTime combinedLocalDT = LocalDateTime.of(dateLocal, timeLocal);
        
        return combinedLocalDT;
    }
    
    public String formatStatementMonth(String monthYear) {
        String[] splitted = monthYear.split("/");
        if (splitted[0].length() != 2) {
            monthYear = "0" + monthYear;
        }
        
        return monthYear;
    }
}
