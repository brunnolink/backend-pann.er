package com.trainee.planner.services.trip;

import com.trainee.planner.domain.trip.Trip;

import com.trainee.planner.domain.trip.exception.TripCreationException;
import com.trainee.planner.domain.trip.exception.TripNotFoundException;
import com.trainee.planner.dto.activity.ActivityRequestDTO;
import com.trainee.planner.dto.activity.ActivityResponseDTO;
import com.trainee.planner.dto.link.LinkRequestDTO;
import com.trainee.planner.dto.link.LinkResponseDTO;
import com.trainee.planner.dto.participant.ParticipantCreateResponseDTO;
import com.trainee.planner.dto.participant.ParticipantRequestDTO;
import com.trainee.planner.dto.trip.TripCreateResponse;
import com.trainee.planner.dto.trip.TripRequestDTO;
import com.trainee.planner.repositories.TripRepository;
import com.trainee.planner.services.activity.ActivityService;
import com.trainee.planner.services.link.LinkService;
import com.trainee.planner.services.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.PostMapping;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    ActivityService activityService;
    @Autowired
    LinkService linkService;

    //TRIPS
    public TripCreateResponse createTrip(TripRequestDTO payload){
        try {
            LocalDateTime startsAt = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime endsAt = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
            Trip newTrip = new Trip(payload);
            newTrip.setStartsAt(startsAt);
            newTrip.setEndsAt(endsAt);
            this.tripRepository.save(newTrip);
            return new TripCreateResponse(newTrip.getId());
        } catch (DateTimeParseException e) {
            throw new TripCreationException("Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            throw new TripCreationException("Reveja se os campos foram preenchidos corretamente");
        }

    }

    public Trip updateTrip(UUID id, TripRequestDTO payload) throws Exception {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());

            return this.tripRepository.save(rawTrip);
        } else {
            throw new Exception("Trip not found");
        }
    }

    public Trip confirmTrip(UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);

            this.tripRepository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);
            return rawTrip;
        } else{
            throw new TripNotFoundException("Trip not found with id: " + id);
        }
    }

    public Optional<Trip> findById(UUID id) {
        return this.tripRepository.findById(id);
    }

    //ACTIVITIES
    @PostMapping("/{id}/activities")
    public ActivityResponseDTO registerActivity(UUID id, ActivityRequestDTO payload) throws Exception {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            ActivityResponseDTO activityResponseDTO = this.activityService.saveActivity(payload, rawTrip);

            return activityResponseDTO;
        } else {
            throw new Exception("Trip not found");
        }
    }
    //PARTICIPANTS
    public ParticipantCreateResponseDTO inviteParticipant(UUID id, ParticipantRequestDTO payload) throws Exception {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            ParticipantCreateResponseDTO participantResponseDTO = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if(rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());


            return participantResponseDTO;
        } else {
            throw new Exception("Trip not found");
        }
    }
    //LINKS

    public LinkResponseDTO registerLink(UUID id, LinkRequestDTO payload) throws Exception {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            LinkResponseDTO linkResponseDTO = this.linkService.registerLink(payload, rawTrip);

            return linkResponseDTO;
        }else {
            throw new Exception("Trip not found");
        }
    }
}
