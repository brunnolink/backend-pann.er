package com.trainee.planner.services.participant;

import com.trainee.planner.domain.participants.Participant;
import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.participant.ParticipantCreateResponseDTO;
import com.trainee.planner.dto.participant.ParticipantData;
import com.trainee.planner.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;


    //Resgitrar convidados a partir da criação da viagem
    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
       List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
        this.participantRepository.saveAll(participants);

//        System.out.println(participants.get(0).getId());
//        System.out.println(participants.get(1).getId());

    }

    //Convidar participantes, após criação da viagem
    public ParticipantCreateResponseDTO registerParticipantToEvent(String email, Trip trip){
        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);
        return new ParticipantCreateResponseDTO(newParticipant.getId());
    }

    //acionar confirmação pro email dos participantes que foram convidados a partir da criaçao da viagem
    public void triggerConfirmationEmailToParticipants(UUID tripId){}

    //acionar confirmação pro email dos participantes que foram convidados após a viagem ser criada
    public void triggerConfirmationEmailToParticipant(String email){}

    //Retorna uma lista com todos os participantes que estão convidados para a viagem
    public List<ParticipantData> getAllParticipants(UUID tripId) {
        return this.participantRepository.findByTripId(tripId).stream().map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed())).toList();
    }
}
