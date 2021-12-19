package com.zibert.service;

import org.junit.Assert;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataManagementTest {

    @Test
    public void countDaysBetweenTwoDatesTest1() {
        String date1 = "2021-12-01";
        String date2 = "2021-12-05";
        DatesManagement datesManagement = new DatesManagement();
        int actual = datesManagement.countDaysBetweenTwoDates(date1, date2);
        int expected = 5;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void countDaysBetweenTwoDatesTest2() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 01);
        Date date1 = new Date(calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 05);
        Date date2 = new Date(calendar.getTimeInMillis());
        DatesManagement datesManagement = new DatesManagement();
        int actual = datesManagement.countDaysBetweenTwoDates(date1, date2);
        int expected = 5;
        Assert.assertEquals(expected, actual);
    }
}
