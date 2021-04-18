package com.devpol.listener;

import com.devpol.exceptions.DateParseException;
import com.devpol.service.StatusService;
import com.devpol.service.TimerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.Status;
import twitter4j.User;

import java.util.Date;

import static org.mockito.Mockito.*;

public class StatusListenerTest {

    private static final long EXAMPLE_STATUS_ID = 1l;
    private static final String EXAMPLE_USERNAME = "exampleUsername";
    private static final String EXAMPLE_STATUS_TEXT = "@reminder in 2 minutes";

    private StatusListener statusListener;

    @Mock
    private TimerService timerService;

    @Mock
    private StatusService statusService;

    @Mock
    private Status status;

    @Mock
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.statusListener = new StatusListener(timerService, statusService);
        when(status.getId()).thenReturn(EXAMPLE_STATUS_ID);
        when(status.getUser()).thenReturn(user);
        when(status.getText()).thenReturn(EXAMPLE_STATUS_TEXT);
        when(user.getScreenName()).thenReturn(EXAMPLE_USERNAME);
    }

    @Test
    public void onStatus() throws DateParseException {
        Date now = new Date();
        when(timerService.scheduleAndSave(status)).thenReturn(now);

        statusListener.onStatus(status);

        verify(timerService, times(1)).scheduleAndSave(status);
        verify(statusService, times(1)).replyInTheSameThread(EXAMPLE_STATUS_ID,
                "Sure, @" + EXAMPLE_USERNAME + ". \uD83E\uDD73 I will remind you about this tweet at " + now + ". \uD83D\uDCCB ");
    }

    @Test
    public void onStatus_failedToParseDate() throws DateParseException {
        final String failMessage = "Ups..";
        when(timerService.scheduleAndSave(status)).thenThrow(new DateParseException(failMessage));

        statusListener.onStatus(status);

        verify(timerService, times(1)).scheduleAndSave(status);
        verify(statusService, times(1)).replyInTheSameThread(EXAMPLE_STATUS_ID, failMessage);
    }

}
