package com.trainee.planner.controllers;

import com.trainee.planner.domain.participant.Participant;
import com.trainee.planner.dto.participant.ParticipantRequestDTO;
import com.trainee.planner.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestDTO payload) {
        Optional<Participant> participant = this.participantRepository.findById(id);

        if(participant.isPresent()) {
            Participant rawParticipant = participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());
            rawParticipant.setEmail(payload.email());

            this.participantRepository.save(rawParticipant);

            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }

}
