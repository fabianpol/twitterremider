package com.devpol.service;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

@Singleton
public class TwitterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterService.class);

    private Twitter twitter;

    @Inject
    public TwitterService(Twitter twitter) {
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

    public void deleteTweet(long id) {
        try {
            twitter.destroyStatus(id);
            LOGGER.info("Destroying status with id = {}", id);
        } catch (TwitterException e) {
            LOGGER.error("Failed to delete tweet with id = {}", id, e);
        }
    }

    public void blockUser(long id)  {
        try {
            twitter.users().createBlock(id);
            LOGGER.info("User with id = {} has been blocked", id);
        } catch (TwitterException e) {
            LOGGER.error("Failed to block user with id = {}", id, e);
        }
    }

    public boolean isUserBlocked(long id) {
        try {
            return Arrays.asList(ArrayUtils.toObject(twitter.users().getBlocksIDs().getIDs())).contains(id);
        } catch (TwitterException e) {
            LOGGER.error("Failed to check is user blocked", e);
            return false;
        }

    }
}
