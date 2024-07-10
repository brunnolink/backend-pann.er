package com.trainee.planner.services.participant;

import com.trainee.planner.domain.participant.Participant;
import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.participant.ParticipantCreateResponseDTO;
import com.trainee.planner.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
       List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
        this.participantRepository.saveAll(participants);

        System.out.println(participants.get(0).getId());

    }

    public ParticipantCreateResponseDTO registerParticipantToEvent(String email, Trip trip){
        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);
        return new ParticipantCreateResponseDTO(newParticipant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId){}

    public void triggerConfirmationEmailToParticipant(String email){}

    public List<Participant> getParticipants(UUID tripId) {
        return this.participantRepository.findByTripId(tripId);
    }
}
