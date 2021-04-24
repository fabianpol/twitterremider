package com.devpol.service.impl;

import com.devpol.entity.Reminder;
import com.devpol.service.ReminderService;
import com.google.common.collect.Lists;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@MicronautTest
public class ReminderServiceImplTest {

    private final Reminder oldReminder = new Reminder(99l, 1l, new Date(), "author", 2l);
    private final Reminder newReminder = new Reminder(999l, 9l, customDate(1), "author2", 8l);

    private ReminderService reminderService;

    @Inject
    public ReminderServiceImplTest(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Test
    void findAllFutureReminders() {
        reminderService.save(oldReminder);
        reminderService.save(newReminder);
        List<Reminder> reminders = Lists.newArrayList(reminderService.findAllFutureReminders());
        Assertions.assertEquals(1, reminders.size());
    }

    @Test
    void findByRepliedId() {
        reminderService.save(newReminder);
        Assertions.assertEquals(newReminder, reminderService.findByRepliedId(newReminder.getRepliedId()).get());
    }

    @Test
    void deleteById() {
        reminderService.save(newReminder);
        reminderService.deleteById(newReminder.getId());
        List<Reminder> reminders = Lists.newArrayList(reminderService.findAllFutureReminders());
        Assertions.assertEquals(0, reminders.size());
    }


    private Date customDate(int hours) {
        return new Date(new Date().getTime() + hours * 1000 * 60 * 60);
    }

}

