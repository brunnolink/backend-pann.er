package com.trainee.planner.repositories;

import com.trainee.planner.domain.activities.Activity;
import com.trainee.planner.domain.participants.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByTripId(UUID tripId);
}
