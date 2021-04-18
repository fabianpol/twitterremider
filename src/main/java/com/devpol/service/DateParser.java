package com.devpol.service;

import com.devpol.exceptions.DateParseException;

import java.util.Date;

public interface DateParser {

    Date parseText(String text) throws DateParseException;

}
