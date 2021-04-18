package com.devpol.repository;

import com.devpol.entity.Reminder;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Date;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Iterable<Reminder> findAllByDateAfter(Date date);
}
