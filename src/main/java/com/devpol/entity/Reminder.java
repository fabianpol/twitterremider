package com.devpol.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Reminder")
public class Reminder {

    // Default constructor required by Hibernate
    public Reminder() {
    }

    public Reminder(long id, Date date, String user, long repliedId) {
        this.id = id;
        this.date = Objects.requireNonNull(date);
        this.user = Objects.requireNonNull(user);
        this.repliedId = repliedId;
    }

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @NotNull
    @Column(name = "date", nullable = false)
    public Date date;

    @NotNull
    @Column(name = "username", nullable = false)
    public String user;

    @Column(name = "repliedId")
    public long repliedId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getRepliedId() {
        return repliedId;
    }

    public void setRepliedId(long repliedId) {
        this.repliedId = repliedId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("date", date).add("user", user).add("repliedId", repliedId).toString();
    }
}
