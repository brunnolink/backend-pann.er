package com.trainee.planner.controllers;

import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.activity.ActivityData;
import com.trainee.planner.dto.activity.ActivityRequestDTO;
import com.trainee.planner.dto.activity.ActivityResponseDTO;
import com.trainee.planner.dto.link.LinkData;
import com.trainee.planner.dto.link.LinkRequestDTO;
import com.trainee.planner.dto.link.LinkResponseDTO;
import com.trainee.planner.dto.participant.ParticipantCreateResponseDTO;
import com.trainee.planner.dto.participant.ParticipantData;
import com.trainee.planner.dto.participant.ParticipantRequestDTO;
import com.trainee.planner.dto.trip.TripCreateResponse;
import com.trainee.planner.dto.trip.TripRequestDTO;
import com.trainee.planner.repositories.TripRepository;
import com.trainee.planner.services.activity.ActivityService;
import com.trainee.planner.services.link.LinkService;
import com.trainee.planner.services.participant.ParticipantService;
import com.trainee.planner.services.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @Autowired
    private ActivityService activityService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private TripService tripService;
    //TRIPS

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestDTO payload) {
        TripCreateResponse newTrip = this.tripService.createTrip(payload);
        return ResponseEntity.ok(newTrip);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> tripDetails = this.tripService.findById(id);

        return tripDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestDTO payload) {
        try {
            Trip updatedTrip = this.tripService.updateTrip(id, payload);
            return ResponseEntity.ok(updatedTrip);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        try {
            Trip confirmTrip = this.tripService.confirmTrip(id);
            return ResponseEntity.ok(confirmTrip);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //ACTIVITIES
    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponseDTO> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestDTO payload) {
        try{
            ActivityResponseDTO newActivity = this.tripService.registerActivity(id, payload);
            return ResponseEntity.ok(newActivity);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){

        List<ActivityData> activitiesList = this.activityService.getAllActivities(id);

        return ResponseEntity.ok(activitiesList);
    }

    //PARTICIPANTS
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponseDTO> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestDTO payload) {
        try{
            ParticipantCreateResponseDTO ParticipantCreateResponseDTO = this.tripService.inviteParticipant(id, payload);
            return ResponseEntity.ok(ParticipantCreateResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){

        List<ParticipantData> participantList = this.participantService.getAllParticipants(id);

        return ResponseEntity.ok(participantList);
    }

    //LINKS

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponseDTO> registerLink(@PathVariable UUID id, @RequestBody LinkRequestDTO payload) {
        try{
            LinkResponseDTO linkResponseDTO = this.tripService.registerLink(id, payload);
            return ResponseEntity.ok(linkResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id){

        List<LinkData> linkList = this.linkService.getAllLinks(id);

        return ResponseEntity.ok(linkList);
    }

}
