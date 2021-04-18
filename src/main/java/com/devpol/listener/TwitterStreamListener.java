package com.devpol.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TwitterStreamListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterStreamListener.class);

    @Inject
    public TwitterStreamListener(StatusListener statusListener) throws TwitterException {
        User currentUser = TwitterFactory.getSingleton().users().verifyCredentials();
        String filter = "@" + currentUser.getScreenName();
        LOGGER.info("Starting status listener and filtering by `{}`", filter);
        new TwitterStreamFactory().getInstance().addListener(statusListener)
                .sample().filter(filter);
    }
}
