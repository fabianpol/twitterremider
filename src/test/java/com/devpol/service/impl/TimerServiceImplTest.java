package com.devpol.service.impl;

import com.devpol.entity.Reminder;
import com.devpol.service.DateParser;
import com.devpol.service.ReminderService;
import com.devpol.service.StatusService;
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
    private StatusService statusService;

    @Mock
    private ReminderService reminderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.timerService = new TimerServiceImpl(dateParser, statusService, reminderService);
    }

    @Test
    public void schedule() throws InterruptedException {
        Reminder reminder = new Reminder(1l, new Date(new Date().getTime() + (1000 * 5)), "user");

        timerService.schedule(reminder);

        verify(statusService, times(0)).replyInTheSameThread(eq(1l), any());
        TimeUnit.SECONDS.sleep(5);
        verify(statusService, times(1)).replyInTheSameThread(eq(1l), any());
    }
}
