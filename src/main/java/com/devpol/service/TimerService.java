package com.devpol.service;

import com.devpol.entity.Reminder;
import com.devpol.exceptions.DateParseException;
import twitter4j.Status;

import java.util.Date;

public interface TimerService {

    Date scheduleAndSave(Status status) throws DateParseException;

    void schedule(Reminder reminder);

}
