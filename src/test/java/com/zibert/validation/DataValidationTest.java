package com.zibert.validation;

import com.zibert.DAO.exceptions.DateCheckException;
import com.zibert.service.DatesManagement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class DataValidationTest {

    @Before
    public void setUp() {
        String path = "C:\\WEB-INF\\log4j2.log";
        System.setProperty("logFile", path);
    }

    @Test
    public void checkIfSecondDateIsBiggerTest() throws DateCheckException {
        String date1 = "2021-12-01";
        String date2 = "2021-12-05";
        DateValidation dateValidation = new DateValidation();
        boolean actualTrue = dateValidation.checkIfSecondDateIsBigger(date1, date2);
        assertTrue(actualTrue);

        boolean actualFalse = dateValidation.checkIfSecondDateIsBigger(date2, date1);
        Assert.assertFalse(actualFalse);

        String date3 = "202";
        String date4 = "20";
        Exception exception = assertThrows(DateCheckException.class, () -> {
            dateValidation.checkIfSecondDateIsBigger(date3, date4);
        });
        String expectedMessage = "Date check error";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        String date5 = null;
        String date6 = null;
        boolean actualNull = dateValidation.checkIfSecondDateIsBigger(date5, date6);
        assertTrue(actualNull);
    }
}
