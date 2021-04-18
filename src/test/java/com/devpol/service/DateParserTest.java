package com.devpol.service;

import com.devpol.exceptions.DateParseException;
import com.devpol.service.impl.DateParserImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParserTest {

    private DateParser dateParser = new DateParserImpl();

    @Test
    public void parseText_noDateProvided(){
        try {
            dateParser.parseText("text without any date");
            Assertions.fail("Exception was expected");
        } catch (DateParseException e) {
            Assertions.assertEquals(e.getMessage(), "Sorry. We couldn't extract the date from text.");
        }
    }

    @Test
    public void parseText_noFutureDate(){
        try {
            dateParser.parseText("Yesterday");
            Assertions.fail("Exception was expected");
        } catch (DateParseException e) {
            Assertions.assertEquals(e.getMessage(), "Sorry. Only futures date are supported.");
        }
    }

    @Test
    public void parseText(){
        try {
            Date date = dateParser.parseText("Tomorrow");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Assertions.assertEquals(sdf.format(date), sdf.format(new Date(new Date().getTime() + (1000 * 60 * 60 * 24))));
        } catch (DateParseException e) {
            Assertions.fail("Exception was not expected");
        }
    }
}
