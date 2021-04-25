package com.devpol.service.impl;

import com.devpol.entity.Reminder;
import com.devpol.service.DbReminderService;
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
    private final Reminder newReminder = new Reminder(999l, 9l, customDate(60), "author2", 8l);

    private DbReminderService dbReminderService;

    @Inject
    public ReminderServiceImplTest(DbReminderService dbReminderService) {
        this.dbReminderService = dbReminderService;
    }

    @Test
    void findAllFutureReminders() {
        dbReminderService.save(oldReminder);
        dbReminderService.save(newReminder);
        List<Reminder> reminders = Lists.newArrayList(dbReminderService.findAllFutureReminders());
        Assertions.assertEquals(1, reminders.size());
    }

    @Test
    void findByRepliedId() {
        dbReminderService.save(newReminder);
        Assertions.assertEquals(newReminder, dbReminderService.findByRepliedId(newReminder.getRepliedId()).get());
    }

    @Test
    void deleteById() {
        dbReminderService.save(newReminder);
        dbReminderService.deleteById(newReminder.getId());
        List<Reminder> reminders = Lists.newArrayList(dbReminderService.findAllFutureReminders());
        Assertions.assertEquals(0, reminders.size());
    }

    @Test
    void countByCreationDateAfterAndUser() {
        Reminder beforeSpamDetection = new Reminder(999l, 9l, customDate(-16), "author", 8l);
        beforeSpamDetection.setCreationDate(customDate(-16));
        dbReminderService.save(oldReminder);
        dbReminderService.save(beforeSpamDetection);
        long count = dbReminderService.countByCreationDateAfterAndUser(customDate(-15), "author");
        Assertions.assertEquals(1, count);
    }

    @Test
    void findAllByUsername() {
        dbReminderService.save(oldReminder);
        dbReminderService.save(newReminder);

        List<Reminder> reminders = Lists.newArrayList(dbReminderService.findAllByUsername("author"));
        Assertions.assertEquals(1, reminders.size());
        Assertions.assertEquals(oldReminder, reminders.get(0));
    }

    private Date customDate(int minutes) {
        return new Date(new Date().getTime() + minutes * 1000 * 60);
    }

}

