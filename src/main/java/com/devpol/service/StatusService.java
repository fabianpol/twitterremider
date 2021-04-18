package com.devpol.service;

import org.slf4j.LoggerFactory;
import twitter4j.*;

import javax.inject.Singleton;

@Singleton
public class StatusService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

    public void replyInTheSameThread(long statusId, String message) {
        Twitter twitter = TwitterFactory.getSingleton();
        StatusUpdate statusUpdate = new StatusUpdate(message);
        statusUpdate.inReplyToStatusId(statusId);
        sendReply(twitter, statusUpdate);
    }

    private void sendReply(Twitter twitter, StatusUpdate statusUpdate) {
        try {
            twitter.updateStatus(statusUpdate);
        } catch (TwitterException e) {
            LOGGER.warn("Failed to send reply", e);
        }
    }
}
