package com.zibert.validation;

import com.zibert.DAO.exceptions.DateCheckException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidation {

    private static final Logger log = LogManager.getLogger(DateValidation.class);
    public boolean checkIfSecondDateIsBigger(String date1, String date2) throws DateCheckException {
        Date firstDate = null;
        Date secondDate = null;
        if (date1 != null && date2 != null) {
            try {
                firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
                secondDate = new SimpleDateFormat("yyyy-MM-dd").parse(date2);
            } catch (ParseException e) {
                log.debug("Cannot parse dates");
                throw new DateCheckException("Date check error");
            }
        } else {
            return true;
        }
        if (firstDate != null && secondDate != null) {
            return secondDate.compareTo(firstDate) >= 0;
        } else {
            return true;
        }
    }
}
