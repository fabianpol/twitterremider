package com.devpol.service.impl;

import com.devpol.entity.Reminder;
import com.devpol.exceptions.CancellationReminderException;
import com.devpol.service.DateParser;
import com.devpol.service.TwitterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TimerServiceImplTest {

    private TimerServiceImpl timerService;

    @Mock
    private DateParser dateParser;

    @Mock
    private TwitterService twitterService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.timerService = new TimerServiceImpl(dateParser, twitterService);
    }

    @Test
    public void schedule() throws InterruptedException {
        final String username = "user";
        final long statusId = 1l;
        Reminder reminder = new Reminder(statusId, 0, new Date(new Date().getTime() + (1000 * 2)), username, 0);

        timerService.schedule(reminder);

        verify(twitterService, times(0)).replyInTheSameThread(eq(statusId), any());
        TimeUnit.SECONDS.sleep(3);
        verify(twitterService, times(1)).replyInTheSameThread(eq(statusId), any());
    }

    @Test
    public void cancel() throws InterruptedException, CancellationReminderException {
        final String username = "user";
        final long statusId = 1l;
        Reminder reminder = new Reminder(statusId,0, new Date(new Date().getTime() + (500)), username, 0);
        timerService.schedule(reminder);
        timerService.cancel(statusId, username);

        TimeUnit.SECONDS.sleep(1);
        verify(twitterService, times(0)).replyInTheSameThread(eq(statusId), any());
    }

    @Test
    public void cancel_noMatchingTask() {
        final String username = "user";
        final long statusId = 1l;
        Reminder reminder = new Reminder(statusId,0, new Date(new Date().getTime() + (500)), username, 0);
        timerService.schedule(reminder);
        try {
            timerService.cancel(2l, username);
            Assertions.fail("Exception expected");
        } catch (CancellationReminderException e) {
            Assertions.assertEquals("Failed to cancel reminder. Couldn't find scheduled reminder.", e.getMessage());
        }
    }

    @Test
    public void cancel_noMatchingUser() {
        final String username = "user";
        final long statusId = 1l;
        Reminder reminder = new Reminder(statusId,0, new Date(new Date().getTime() + (500)), username, 0);
        timerService.schedule(reminder);
        try {
            timerService.cancel(statusId, "differentUser");
            Assertions.fail("Exception expected");
        } catch (CancellationReminderException e) {
            Assertions.assertEquals("Failed to cancel reminder. Username doesn't match.", e.getMessage());
        }
    }
}
