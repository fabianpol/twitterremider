package com.devpol.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

    private Twitter twitter;

    @Inject
    public StatusService(Twitter twitter) {
        this.twitter = twitter;
    }

    public Status replyInTheSameThread(long statusId, String message) {
        LOGGER.info("Replying to status [{}] with message [{}] ", statusId, message);
        StatusUpdate statusUpdate = new StatusUpdate(message);
        statusUpdate.inReplyToStatusId(statusId);
        return sendReply(twitter, statusUpdate);
    }

    private Status sendReply(Twitter twitter, StatusUpdate statusUpdate) {
        try {
            return twitter.updateStatus(statusUpdate);
        } catch (TwitterException e) {
            throw new RuntimeException("Failed to send reply", e);
        }
    }
}
