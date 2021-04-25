package com.devpol.activator;

import com.devpol.service.DbReminderService;
import com.devpol.service.TimerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Activator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    private DbReminderService dbReminderService;
    private TimerService timerService;

    @Inject
    public Activator(DbReminderService dbReminderService, TimerService timerService) {
        this.dbReminderService = dbReminderService;
        this.timerService = timerService;

        scheduleRemindersFromDb();
    }

    private void scheduleRemindersFromDb() {
        LOGGER.info("Reading all reminders from database");
        dbReminderService.findAllFutureReminders().forEach(timerService::schedule);
        LOGGER.info("Finished reading all reminders from database");
    }

}
