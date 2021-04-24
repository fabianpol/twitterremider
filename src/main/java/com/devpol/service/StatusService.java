package com.devpol.service;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StatusService {

    private Twitter twitter;

    @Inject
    public StatusService(Twitter twitter) {
        this.twitter = twitter;
    }

    public Status replyInTheSameThread(long statusId, String message) {
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
