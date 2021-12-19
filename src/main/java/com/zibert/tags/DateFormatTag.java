package com.zibert.tags;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom tag formatting the date
 */

public class DateFormatTag extends SimpleTagSupport {

    Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void doTag() throws IOException {
        String result = new SimpleDateFormat("yyyy-MM-dd").format(date);
        getJspContext().getOut().print(result);
    }
}
