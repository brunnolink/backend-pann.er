package com.trainee.planner.services.activity;

import com.trainee.planner.domain.activities.Activity;
import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.activities.ActivityData;
import com.trainee.planner.dto.activities.ActivityRequestDTO;
import com.trainee.planner.dto.activities.ActivityResponseDTO;
import com.trainee.planner.dto.participant.ParticipantData;
import com.trainee.planner.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    public ActivityResponseDTO saveActivity(ActivityRequestDTO payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);
        this.activityRepository.save(newActivity);

        return new ActivityResponseDTO(newActivity.getId());
    }

    public List<ActivityData> getAllActivities(UUID tripId) {
        return this.activityRepository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
