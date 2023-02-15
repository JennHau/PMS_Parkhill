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
    // format date to format yyyy-MM-dd
    public LocalDate formatDate(String date) {
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        
        return localDate;
    }
    
    // format date to format dd/MM/yyyy
    public LocalDate formatDate2(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);

        return localDate;
    }
    
    // get current date and time
    public String currentDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateTime = LocalDateTime.now().format(dateTimeFormatter);
        return dateTime;
    }
    
    // change date format from dd/MM/yyyy to yyyy-MM-dd
    public String changeFormatDate(String dateString) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = originalFormat.parse(dateString);
        
        return targetFormat.format(date);
    }
    
    // change date format from yyyy-MM-dd to dd/MM/yyyy
    public String changeFormatDate2(String dateString) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = originalFormat.parse(dateString);
        
        return targetFormat.format(date);
    }
    
    // fornmat time to HH:mm
    public LocalTime formatTime(String time) {
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        
        LocalTime localTime = LocalTime.parse(LocalTime.parse(time).format(timeFormatter));
        
        return localTime;
    }
    
    // get the time category according to input time
    public LocalTime getTimeCategory(LocalTime time) {
        int timeMin = time.getMinute();
        if (timeMin > 30) {
            time = time.withMinute(00);
            time = time.plusHours(1);
        }
        else {
            time = time.withMinute(30);
        }
        
        return time;
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
