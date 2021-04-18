package com.devpol.timer;

import com.devpol.service.StatusService;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TimerTask extends java.util.TimerTask {

    private static final List<String> MESSAGE_TEMPLATES = ImmutableList.of("Ring Ring! ⏰ Here is your reminder, @%s",
            "Hey, @%s. Here is your reminder ⏰");

    private long statusId;
    private String user;
    private StatusService statusService;

    public TimerTask(long statusId, String user, StatusService statusService){
        this.statusId = statusId;
        this.statusService = Objects.requireNonNull(statusService);
        this.user = Objects.requireNonNull(user);
    }

    @Override
    public void run() {
        statusService.replyInTheSameThread(statusId, getMessage());
    }

    private String getMessage(){
        int randomIndex = new Random().nextInt(MESSAGE_TEMPLATES.size());
        return String.format(MESSAGE_TEMPLATES.get(randomIndex), user);
    }

}
