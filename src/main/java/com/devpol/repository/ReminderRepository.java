package com.devpol.repository;

import com.devpol.entity.Reminder;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Iterable<Reminder> findAllByDateAfter(Date date);

    Optional<Reminder> findByRepliedId(long id);


}
