package com.devpol.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.User;
import twitter4j.api.UsersResources;

import static org.mockito.Mockito.*;

public class TwitterStreamListenerTest {

    @Mock
    private StatusListener statusListener;

    @Mock
    private Twitter twitter;

    @Mock
    private TwitterStream twitterStream;

    @Mock
    private UsersResources users;

    @Mock
    private User user;

    private TwitterStreamListener twitterStreamListener;

    @BeforeEach
    public void setup() throws TwitterException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifyListenerIsRegistered() throws TwitterException {
        when(twitter.users()).thenReturn(users);
        when(users.verifyCredentials()).thenReturn(user);
        when(user.getScreenName()).thenReturn("name");
        when(twitterStream.addListener(statusListener)).thenReturn(twitterStream);
        when(twitterStream.sample()).thenReturn(twitterStream);

        this.twitterStreamListener = new TwitterStreamListener(statusListener, twitter, twitterStream);

        verify(twitterStream, times(1)).addListener(statusListener);
        verify(twitterStream, times(1)).sample();
        verify(twitterStream, times(1)).filter("@name");
    }

}
