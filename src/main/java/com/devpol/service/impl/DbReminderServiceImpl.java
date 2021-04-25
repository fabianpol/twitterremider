package com.devpol.service.impl;

import com.devpol.entity.Reminder;
import com.devpol.repository.ReminderRepository;
import com.devpol.service.DbReminderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class DbReminderServiceImpl implements DbReminderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbReminderServiceImpl.class);

    @Inject
    private ReminderRepository reminderRepository;

    @Override
    public void save(Reminder reminder) {
        reminderRepository.save(reminder);
        LOGGER.info("Saving reminder {}", reminder);
    }

    @Override
    public Iterable<Reminder> findAllFutureReminders() {
        return reminderRepository.findAllByDateAfter(new Date());
    }

    @Override
    public Optional<Reminder> findByRepliedId(long id) {
        return reminderRepository.findByRepliedId(id);
    }

    @Override
    public void deleteById(long id) {
        LOGGER.debug("Deleting reminder with id = {}", id);
        reminderRepository.deleteById(id);
    }

    @Override
    public long countByCreationDateAfterAndUser(Date date1, String username) {
        return reminderRepository.countByCreationDateAfterAndUser(date1, username);
    }

    @Override
    public List<Reminder> findAllByUsername(String username) {
        return reminderRepository.findAllByUser(username);
    }


}
