package com.devpol.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TwitterStreamListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterStreamListener.class);

    @Inject
    public TwitterStreamListener(StatusListener statusListener, Twitter twitter, TwitterStream twitterStream) throws TwitterException {
        User currentUser = twitter.users().verifyCredentials();
        String filter = "@" + currentUser.getScreenName();
        LOGGER.info("Starting status listener and filtering by `{}`", filter);
        twitterStream.addListener(statusListener).sample().filter(filter);
    }
}
