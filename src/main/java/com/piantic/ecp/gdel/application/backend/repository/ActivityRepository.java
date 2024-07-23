package com.piantic.ecp.gdel.application.backend.repository;

import com.piantic.ecp.gdel.application.backend.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByDateActivityAfter(ZonedDateTime dateTime);

    List<Activity> findByDateActivityBefore(ZonedDateTime dateTime);
}
