package com.devpol.timer;

import com.devpol.service.StatusService;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TimerTask extends java.util.TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerTask.class);

    private static final List<String> MESSAGE_TEMPLATES = ImmutableList.of("Ring Ring! ⏰ Here is your reminder, @%s %s",
            "Hey, @%s. Here is your reminder ⏰ %s");

    private long statusId;
    private long parentStatusId;
    private String user;
    private StatusService statusService;

    public TimerTask(long statusId, long parentStatusId, String user, StatusService statusService) {
        this.statusId = statusId;
        this.parentStatusId = parentStatusId;
        this.statusService = Objects.requireNonNull(statusService);
        this.user = Objects.requireNonNull(user);
    }

    @Override
    public void run() {
        LOGGER.info("Executing scheduled task for id = {}", statusId);
        statusService.replyInTheSameThread(statusId, getMessage());
    }

    public String getUser() {
        return user;
    }

    private String getMessage() {
        int randomIndex = new Random().nextInt(MESSAGE_TEMPLATES.size());
        String tweetToRemindUrl = parentStatusId > 0 ? String.format("https://twitter.com/%s/status/%s", user, parentStatusId) : "";
        return String.format(MESSAGE_TEMPLATES.get(randomIndex), user, tweetToRemindUrl);
    }

}
