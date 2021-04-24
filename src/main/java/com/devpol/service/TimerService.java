package com.devpol.service;

import com.devpol.entity.Reminder;
import com.devpol.exceptions.CancellationReminderException;
import com.devpol.exceptions.DateParseException;
import twitter4j.Status;

import java.util.Date;

public interface TimerService {

    Date schedule(Status status) throws DateParseException;

    void schedule(Reminder reminder);

    void cancel(long statusId, String username) throws CancellationReminderException;

}
