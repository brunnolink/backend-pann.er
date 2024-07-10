package com.trainee.planner.repositories;

import com.trainee.planner.domain.activities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
}
