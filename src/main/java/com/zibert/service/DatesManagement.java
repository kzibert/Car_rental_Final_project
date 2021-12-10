package com.zibert.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DatesManagement {

    public int countDaysBetweenTwoDates(String rentBeg, String rentFin) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(rentBeg, dtf);
        LocalDate date2 = LocalDate.parse(rentFin, dtf);
        Period daysBetween = Period.between(date1, date2);
        return daysBetween.getDays() + 1;
    }

    public int countDaysBetweenTwoDates(Date rentBeg, Date rentFin) {
        LocalDate date1 = rentBeg.toLocalDate();
        LocalDate date2 = rentFin.toLocalDate();
        Period daysBetween = Period.between(date1, date2);
        return daysBetween.getDays() + 1;
    }

    public Date parseDate(String date) throws ParseException {
        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        Date result = new java.sql.Date(date1.getTime());
        return result;
    }
}
