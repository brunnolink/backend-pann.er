package com.trainee.planner.services.activity;

import com.trainee.planner.domain.activity.Activity;
import com.trainee.planner.domain.activity.exception.ActivityDateException;
import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.activity.ActivityData;
import com.trainee.planner.dto.activity.ActivityRequestDTO;
import com.trainee.planner.dto.activity.ActivityResponseDTO;
import com.trainee.planner.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    public ActivityResponseDTO saveActivity(ActivityRequestDTO payload, Trip trip) throws ActivityDateException {
        LocalDateTime activityStart = LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME);

        if(activityStart.isBefore(trip.getStartsAt())) {
            throw new ActivityDateException("A atividade não pode ser criad antes do início da viagem.");
        }

        if(activityStart.isAfter(trip.getEndsAt())) {
            throw new ActivityDateException("A atividade não pode ser criada depois do final da viagem.");
        }

        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

        this.activityRepository.save(newActivity);

        return new ActivityResponseDTO(newActivity.getId());
    }

    public List<ActivityData> getAllActivities(UUID tripId) {
        return this.activityRepository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
