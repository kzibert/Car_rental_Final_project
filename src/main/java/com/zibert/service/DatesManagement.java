package com.zibert.service;

import java.util.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Class with method for counting days between two dates
 */

public class DatesManagement {

    public int countDaysBetweenTwoDates(String rentBeg, String rentFin) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(rentBeg, dtf);
        LocalDate date2 = LocalDate.parse(rentFin, dtf);
        Period daysBetween = Period.between(date1, date2);
        return daysBetween.getDays() + 1;
    }

    public int countDaysBetweenTwoDates(Date rentBeg, Date rentFin) {
        LocalDate date1 = new java.sql.Date(rentBeg.getTime()).toLocalDate();
        LocalDate date2 = new java.sql.Date(rentFin.getTime()).toLocalDate();
        Period daysBetween = Period.between(date1, date2);
        return daysBetween.getDays() + 1;
    }
}
