package com.devpol.service;

import com.devpol.entity.Reminder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DbReminderService {

    void save(Reminder reminder);

    Iterable<Reminder> findAllFutureReminders();

    Optional<Reminder> findByRepliedId(long id);

    void deleteById(long id);

    long countByCreationDateAfterAndUser(Date date, String username);

    List<Reminder> findAllByUsername(String username);

}
