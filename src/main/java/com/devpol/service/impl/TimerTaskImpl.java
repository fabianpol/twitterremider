package com.devpol.service.impl;

import com.devpol.entity.Reminder;
import com.devpol.exceptions.DateParseException;
import com.devpol.service.DateParser;
import com.devpol.service.ReminderService;
import com.devpol.service.StatusService;
import com.devpol.service.TimerService;
import com.devpol.timer.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.Timer;

@Singleton
class TimerServiceImpl implements TimerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerTask.class);

    private final Timer timer;
    private final DateParser dateParser;
    private final StatusService statusService;
    private final ReminderService reminderService;

    @Inject
    public TimerServiceImpl(DateParser dateParser, StatusService statusService, ReminderService reminderService) {
        this.dateParser = dateParser;
        this.timer = new Timer();
        this.statusService = statusService;
        this.reminderService = reminderService;
    }

    @Override
    public Date scheduleAndSave(Status status) throws DateParseException {
        Date date = dateParser.parseText(status.getText());
        reminderService.save(new Reminder(status.getId(), date, status.getUser().getScreenName()));
        schedule(status.getId(), status.getUser().getScreenName(), date);
        return date;
    }

    @Override
    public void schedule(Reminder reminder) {
        schedule(reminder.getId(), reminder.getUser(), reminder.getDate());
    }

    private void schedule(long statusId, String user, Date date) {
        LOGGER.info("Scheduling timer task. Scheduled date = {}", date);
        timer.schedule(new TimerTask(statusId, user, statusService), date);
    }

}
