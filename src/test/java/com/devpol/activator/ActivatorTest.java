package com.devpol.activator;

import com.devpol.entity.Reminder;
import com.devpol.service.ReminderService;
import com.devpol.service.TimerService;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

public class ActivatorTest {

    @Mock
    private ReminderService reminderService;

    @Mock
    private TimerService timerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifyAllRemindersAreScheduled() {
        List<Reminder> reminders = ImmutableList.of(new Reminder(), new Reminder());
        when(reminderService.findAllFutureReminders()).thenReturn(reminders);
        new Activator(reminderService, timerService);

        verify(timerService, times(2)).schedule(any(Reminder.class));

    }
}
