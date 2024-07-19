package com.trainee.planner.services.participant;

import com.trainee.planner.domain.participant.Participant;
import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.participant.ParticipantCreateResponseDTO;
import com.trainee.planner.dto.participant.ParticipantData;
import com.trainee.planner.dto.participant.ParticipantRequestDTO;
import com.trainee.planner.repositories.ParticipantRepository;
import com.trainee.planner.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private TripRepository tripRepository;

    //Resgitrar convidados a partir da criação da viagem
    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
        Trip savedTrip = this.tripRepository.save(trip);


        List<Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, savedTrip))
                .collect(Collectors.toList());


        this.participantRepository.saveAll(participants);


        if (!participants.isEmpty()) {
            System.out.println("IDs dos participantes:");
            participants.forEach(participant -> System.out.println(participant.getId()));
        }
    }

    //Convidar participantes, após criação da viagem
    public ParticipantCreateResponseDTO registerParticipantToEvent(String name, String email, Trip trip){
        Participant newParticipant = new Participant(name, email, trip);


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

    public Participant participantConfirm(UUID id, ParticipantRequestDTO payload) throws Exception {
        Optional<Participant> participant = this.participantRepository.findById(id);

        if(participant.isPresent()) {
            Participant rawParticipant = participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());
            rawParticipant.setEmail(payload.email());

            this.participantRepository.save(rawParticipant);

            return rawParticipant;
        } else {
            throw new Exception("Participant not found");
        }

    }
}
