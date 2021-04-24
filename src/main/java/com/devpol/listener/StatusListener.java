package com.devpol.listener;

import com.devpol.entity.Reminder;
import com.devpol.exceptions.CancellationReminderException;
import com.devpol.exceptions.DateParseException;
import com.devpol.service.ReminderService;
import com.devpol.service.StatusService;
import com.devpol.service.TimerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.Optional;

@Singleton
public class StatusListener implements twitter4j.StatusListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusListener.class);

    private TimerService timerService;
    private StatusService statusService;
    private ReminderService reminderService;

    @Inject
    public StatusListener(TimerService timerService, StatusService statusService, ReminderService reminderService) {
        this.timerService = timerService;
        this.statusService = statusService;
        this.reminderService = reminderService;
    }

    @Override
    public void onStatus(Status status) {
        String user = status.getUser().getScreenName();
        LOGGER.info("Detected mention - @{} - {}", user, status.getText());
        if (status.getText().contains("/cancel")) {
            cancelReminder(status, user);
        } else {
            saveReminder(status, user);
        }
    }

    private void cancelReminder(Status status, String user) {
        if(status.getInReplyToStatusId() > 0) {
            Optional<Reminder> reminder = reminderService.findByRepliedId(status.getInReplyToStatusId());
            reminder.ifPresent(r -> {
                try {
                    timerService.cancel(r.getId(), user);
                    statusService.replyInTheSameThread(status.getId(), "OK. @" + user + ", your reminder has  been canceled.");
                    reminderService.deleteById(r.getId());
                } catch (CancellationReminderException e) {
                    statusService.replyInTheSameThread(status.getId(), "@" + user + ", something went wrong. :(");
                }
            });
        }
    }


    private void saveReminder(Status status, String user) {
        try {
            Date scheduled = timerService.schedule(status);
            String message = "Sure, @" + user + ". \uD83E\uDD73 I will remind you about this tweet at " + scheduled + ". \uD83D\uDCCB ";
            Status repliedStatus = statusService.replyInTheSameThread(status.getId(), message);
            reminderService.save(new Reminder(status.getId(), scheduled, user, repliedStatus.getId()));
        } catch (DateParseException e) {
            LOGGER.warn("Failed to parse tweet: {}", status.getText());
            statusService.replyInTheSameThread(status.getId(), e.getMessage());
        }
    }


    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        // DO NOTHING
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        // DO NOTHING
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        // DO NOTHING
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        // DO NOTHING
    }

    @Override
    public void onException(Exception e) {
        LOGGER.error("Exception from status listener", e);
    }

}
