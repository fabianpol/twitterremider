package com.devpol.service.impl;

import com.devpol.entity.Reminder;
import com.devpol.exceptions.CancellationReminderException;
import com.devpol.exceptions.DateParseException;
import com.devpol.service.DateParser;
import com.devpol.service.StatusService;
import com.devpol.service.TimerService;
import com.devpol.timer.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

@Singleton
class TimerServiceImpl implements TimerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerTask.class);

    final Timer timer;
    private final DateParser dateParser;
    private final StatusService statusService;
    private Map<Long, TimerTask> scheduled;

    @Inject
    public TimerServiceImpl(DateParser dateParser, StatusService statusService) {
        this.dateParser = dateParser;
        this.timer = new Timer();
        this.statusService = statusService;
        this.scheduled = new HashMap<>();
    }

    @Override
    public Date schedule(Status status) throws DateParseException {
        Date date = dateParser.parseText(status.getText());
        schedule(status.getId(), status.getInReplyToStatusId(), status.getUser().getScreenName(), date);
        return date;
    }

    @Override
    public void schedule(Reminder reminder) {
        schedule(reminder.getId(), reminder.getParentId(), reminder.getUser(), reminder.getDate());
    }

    @Override
    public void cancel(long statusId, String username) throws CancellationReminderException {
        if (scheduled.containsKey(statusId)) {
            TimerTask timerTask = scheduled.get(statusId);
            if (timerTask.getUser().equals(username)) {
                scheduled.get(statusId).cancel();
                LOGGER.info("Timer task with id = {} has been canceled", statusId);
            } else {
                LOGGER.info("Failed to cancel timer task with id = {}. Username doesn't match.", statusId);
                throw new CancellationReminderException("Failed to cancel reminder. Username doesn't match.");
            }
        } else {
            LOGGER.info("Failed to cancel timer task with id = {}. Didn't find matching task.", statusId);
            throw new CancellationReminderException("Failed to cancel reminder. Couldn't find scheduled reminder.");
        }
    }

    private void schedule(long statusId, long parentStatusId, String user, Date date) {
        LOGGER.info("Scheduling timer task. Scheduled date = {}", date);
        TimerTask timerTask = new TimerTask(statusId, parentStatusId, user, statusService);
        timer.schedule(timerTask, date);
        scheduled.put(statusId, timerTask);
    }

}
