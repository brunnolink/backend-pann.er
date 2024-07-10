package com.trainee.planner.controllers;

import com.trainee.planner.domain.participant.Participant;
import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.participant.ParticipantCreateResponseDTO;
import com.trainee.planner.dto.participant.ParticipantRequestDTO;
import com.trainee.planner.dto.trips.TripCreateResponse;
import com.trainee.planner.dto.trips.TripRequestDTO;
import com.trainee.planner.repositories.TripRepository;
import com.trainee.planner.services.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestDTO payload) {
        Trip newTrip = new Trip(payload);
        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);
        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
        Optional<Trip> tripDetails = this.tripRepository.findById(id);

        return tripDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestDTO payload){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());

            this.tripRepository.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);

            this.tripRepository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponseDTO> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestDTO payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            ParticipantCreateResponseDTO participantResponseDTO = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if(rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());


            return ResponseEntity.ok(participantResponseDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<Participant>> getAllParticipants(@PathVariable UUID id){

        List<Participant> participantList = this.participantService.getParticipants(id);

        return ResponseEntity.ok(participantList);
    }
}
