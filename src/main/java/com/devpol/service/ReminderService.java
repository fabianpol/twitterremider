package com.devpol.service;

import com.devpol.entity.Reminder;

public interface ReminderService {

    void save(Reminder reminder);

    Iterable<Reminder> findAllFutureReminders();

}
