package com.devpol.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StatusServiceTest {

    private StatusService statusService;

    @Mock
    private Twitter twitter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.statusService = new StatusService(twitter);
    }

    @Test
    public void replyInTheSameThread() throws TwitterException {
        final long id = 1l;
        final String message = "This is the reminder.";

        statusService.replyInTheSameThread(id, message);
        StatusUpdate update = new StatusUpdate(message);
        update.inReplyToStatusId(id);

        verify(twitter, times(1)).updateStatus(eq(update));
    }

}
