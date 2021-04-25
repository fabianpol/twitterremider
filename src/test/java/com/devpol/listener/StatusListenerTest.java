package com.devpol.listener;

import com.devpol.entity.Reminder;
import com.devpol.exceptions.DateParseException;
import com.devpol.service.DbReminderService;
import com.devpol.service.TwitterService;
import com.devpol.service.TimerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.Status;
import twitter4j.User;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class StatusListenerTest {

    private static final long EXAMPLE_STATUS_ID = 1l;
    private static final String EXAMPLE_USERNAME = "exampleUsername";
    private static final String EXAMPLE_STATUS_TEXT = "@reminder in 2 minutes";

    private StatusListener statusListener;

    @Mock
    private TimerService timerService;

    @Mock
    private TwitterService twitterService;

    @Mock
    private Status status;

    @Mock
    private User user;

    @Mock
    private DbReminderService dbReminderService;

    @Mock
    private Status repliedStatus;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.statusListener = new StatusListener(timerService, twitterService, dbReminderService);
        when(status.getId()).thenReturn(EXAMPLE_STATUS_ID);
        when(status.getUser()).thenReturn(user);
        when(status.getText()).thenReturn(EXAMPLE_STATUS_TEXT);
        when(user.getScreenName()).thenReturn(EXAMPLE_USERNAME);
    }

    @Test
    public void onStatus() throws DateParseException {
        Date now = new Date();
        when(timerService.schedule(status)).thenReturn(now);
        when(twitterService.replyInTheSameThread(EXAMPLE_STATUS_ID,
                "Sure, @" + EXAMPLE_USERNAME + ". \uD83E\uDD73 I will remind you about this tweet at " + now + ". \uD83D\uDCCB ")).thenReturn(repliedStatus);
        when(repliedStatus.getId()).thenReturn(3l);

        statusListener.onStatus(status);

        verify(timerService, times(1)).schedule(status);
        verify(twitterService, times(1)).replyInTheSameThread(EXAMPLE_STATUS_ID,
                "Sure, @" + EXAMPLE_USERNAME + ". \uD83E\uDD73 I will remind you about this tweet at " + now + ". \uD83D\uDCCB ");
    }

    @Test
    public void onStatus_failedToParseDate() throws DateParseException {
        final String failMessage = "Ups..";
        when(timerService.schedule(status)).thenThrow(new DateParseException(failMessage));

        statusListener.onStatus(status);

        verify(timerService, times(1)).schedule(status);
        verify(twitterService, times(0)).replyInTheSameThread(eq(EXAMPLE_STATUS_ID), any());
    }

    @Test
    public void onStatus_cancelReminder() {
        when(status.getText()).thenReturn("@reminder /cancel");
        when(status.getInReplyToStatusId()).thenReturn(2l);
        when(dbReminderService.findByRepliedId(2l)).thenReturn(Optional.of(new Reminder(EXAMPLE_STATUS_ID, 0l, new Date(), EXAMPLE_USERNAME, 2l)));
        statusListener.onStatus(status);

        verify(twitterService, times(1)).replyInTheSameThread(eq(EXAMPLE_STATUS_ID), any());
    }

}
