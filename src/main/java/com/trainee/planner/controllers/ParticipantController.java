package com.trainee.planner.controllers;

import com.trainee.planner.domain.participant.Participant;
import com.trainee.planner.dto.participant.ParticipantCreateResponseDTO;
import com.trainee.planner.dto.participant.ParticipantRequestDTO;
import com.trainee.planner.services.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestDTO payload) {
        try{
            Participant participantConfirmed = this.participantService.participantConfirm(id, payload);
            return ResponseEntity.ok(participantConfirmed);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
