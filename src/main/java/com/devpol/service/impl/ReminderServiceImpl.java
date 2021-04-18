package com.devpol.service.impl;

import com.devpol.entity.Reminder;
import com.devpol.repository.ReminderRepository;
import com.devpol.service.ReminderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class ReminderServiceImpl implements ReminderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderServiceImpl.class);

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
}
