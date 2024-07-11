package com.trainee.planner.services.trip;

import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.trip.TripCreateResponse;
import com.trainee.planner.dto.trip.TripRequestDTO;
import com.trainee.planner.repositories.TripRepository;
import com.trainee.planner.services.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantService participantService;

    public TripCreateResponse createTrip(TripRequestDTO payload) {
        Trip newTrip = new Trip(payload);
        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);
        return new TripCreateResponse(newTrip.getId());
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

    public Optional<Trip> findById(UUID id) {
        return this.tripRepository.findById(id);
    }
}
