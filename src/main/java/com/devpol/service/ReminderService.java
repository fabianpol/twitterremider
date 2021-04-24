package com.devpol.service;

import com.devpol.entity.Reminder;

import java.util.Optional;

public interface ReminderService {

    void save(Reminder reminder);

    Iterable<Reminder> findAllFutureReminders();

    Optional<Reminder> findByRepliedId(long id);

    void deleteById(long id);
}
