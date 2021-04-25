package com.devpol.listener;

import com.devpol.entity.Reminder;
import com.devpol.exceptions.CancellationReminderException;
import com.devpol.exceptions.DateParseException;
import com.devpol.service.DbReminderService;
import com.devpol.service.TimerService;
import com.devpol.service.TwitterService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class StatusListener implements twitter4j.StatusListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusListener.class);

    private TimerService timerService;
    private TwitterService twitterService;
    private DbReminderService dbReminderService;

    @Inject
    public StatusListener(TimerService timerService, TwitterService twitterService, DbReminderService dbReminderService) {
        this.timerService = timerService;
        this.twitterService = twitterService;
        this.dbReminderService = dbReminderService;
    }

    @Override
    public void onStatus(Status status) {
        String user = status.getUser().getScreenName();
        LOGGER.info("Detected mention - @{} - {}", user, status.getText());
        if (status.getText().contains("/cancel")) {
            cancelReminder(status, user);
        } else if (!status.isRetweeted()) {
            if (isSpamDetected(user)) {
                handleSpam(status);
            } else {
                saveReminder(status, user);
            }
        }
    }

    private void handleSpam(Status status) {
        String user = status.getUser().getScreenName();
        LOGGER.info("Detected SPAM for a user = {}", user);
        twitterService.blockUser(status.getUser().getId());
        for (Reminder r : dbReminderService.findAllByUsername(user)) {
            dbReminderService.deleteById(r.getId());
            if(r.getRepliedId() != null) {
                twitterService.deleteTweet(r.getRepliedId());
            }
            try {
                timerService.cancel(r.getId(), user);
            } catch (CancellationReminderException e) {
                LOGGER.error("Failed to remove SPAM records", e);
            }
        }
    }

    private boolean isSpamDetected(String user) {
        Date date = DateUtils.addMinutes(new Date(), -10);
        long times = dbReminderService.countByCreationDateAfterAndUser(date, user);
        return times > 2;
    }

    private void cancelReminder(Status status, String user) {
        if (status.getInReplyToStatusId() > 0) {
            dbReminderService.findByRepliedId(status.getInReplyToStatusId()).ifPresent(r -> {
                try {
                    timerService.cancel(r.getId(), user);
                    twitterService.replyInTheSameThread(status.getId(), "OK. @" + user + ", your reminder has  been canceled.");
                } catch (CancellationReminderException e) {
                    twitterService.replyInTheSameThread(status.getId(), "@" + user + ", something went wrong. :(");
                }
            });
        }
    }


    private void saveReminder(Status status, String user) {
        try {
            Date scheduled = timerService.schedule(status);
            String message = "Sure, @" + user + ". \uD83E\uDD73 I will remind you about this tweet at " + scheduled + ". \uD83D\uDCCB ";
            Status repliedStatus = twitterService.replyInTheSameThread(status.getId(), message);
            dbReminderService.save(new Reminder(status.getId(), status.getInReplyToStatusId(), scheduled, user, repliedStatus.getId()));
        } catch (DateParseException e) {
            LOGGER.warn("Failed to parse tweet: {}", status.getText());
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
