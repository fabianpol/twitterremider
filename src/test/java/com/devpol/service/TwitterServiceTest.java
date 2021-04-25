package com.devpol.service;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.IDs;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.api.UsersResources;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class TwitterServiceTest {

    private TwitterService twitterService;

    @Mock
    private Twitter twitter;

    @Mock
    private UsersResources users;

    @Mock
    private IDs ids;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.twitterService = new TwitterService(twitter);
    }

    @Test
    public void replyInTheSameThread() throws TwitterException {
        final long id = 1l;
        final String message = "This is the reminder.";

        twitterService.replyInTheSameThread(id, message);
        StatusUpdate update = new StatusUpdate(message);
        update.inReplyToStatusId(id);

        verify(twitter, times(1)).updateStatus(eq(update));
    }

    @Test
    public void deleteTweet() throws TwitterException {
        final long id = 1l;
        twitterService.deleteTweet(id);
        verify(twitter, times(1)).destroyStatus(id);
    }

    @Test
    public void blockUser() throws TwitterException {
        when(twitter.users()).thenReturn(users);
        final long id = 1l;
        twitterService.blockUser(id);
        verify(users, times(1)).createBlock(id);
    }

    @Test
    public void isUserBlocked() throws TwitterException {
        long[] blocked = {1l};
        when(twitter.users()).thenReturn(users);
        when(users.getBlocksIDs()).thenReturn(ids);
        when(ids.getIDs()).thenReturn(blocked);
        Assertions.assertEquals(true, twitterService.isUserBlocked(1l));
        Assertions.assertEquals(false, twitterService.isUserBlocked(3l));
    }

}
