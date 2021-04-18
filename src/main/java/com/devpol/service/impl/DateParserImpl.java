package com.devpol.service.impl;

import com.devpol.exceptions.DateParseException;
import com.devpol.service.DateParser;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class DateParserImpl implements DateParser {

    private Parser parser;

    public DateParserImpl() {
        this.parser = new Parser();
    }

    @Override
    public Date parseText(String text) throws DateParseException {
        DateGroup dateGroup = parser.parse(text).stream().findFirst().orElse(null);
        if(dateGroup == null || dateGroup.getDates().size() < 1) {
            throw new DateParseException("Sorry. We couldn't extract the date from text.");
        }
        Date date = dateGroup.getDates().get(0);
        if(date.before(new Date())) {
            throw new DateParseException("Sorry. Only futures date are supported.");
        }
        return date;
    }
}
