package com.devpol.listener;

import com.devpol.exceptions.DateParseException;
import com.devpol.service.StatusService;
import com.devpol.service.TimerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class StatusListener implements twitter4j.StatusListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusListener.class);

    @Inject
    private TimerService timerService;

    @Inject
    private StatusService statusService;

    @Override
    public void onStatus(Status status) {
        String user = status.getUser().getScreenName();
        LOGGER.info("Detected mention - @{} - {}", user, status.getText());
        try {
            Date scheduled = timerService.scheduleAndSave(status);
            String message = "Sure, @" + user +". \uD83E\uDD73 I will remind you about this tweet at " + scheduled + ". \uD83D\uDCCB ";
            statusService.replyInTheSameThread(status.getId(), message);
        } catch (DateParseException e) {
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
