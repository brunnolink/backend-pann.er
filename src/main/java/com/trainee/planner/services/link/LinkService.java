package com.trainee.planner.services.link;


import com.trainee.planner.domain.link.Link;
import com.trainee.planner.domain.trip.Trip;
import com.trainee.planner.dto.activity.ActivityData;
import com.trainee.planner.dto.link.LinkData;
import com.trainee.planner.dto.link.LinkRequestDTO;
import com.trainee.planner.dto.link.LinkResponseDTO;
import com.trainee.planner.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;

    public LinkResponseDTO registerLink(LinkRequestDTO payload, Trip trip) {
        Link newLink = new Link(payload.title(), payload.url(), trip);
        this.linkRepository.save(newLink);

        return new LinkResponseDTO(newLink.getId());
    }

    public List<LinkData> getAllLinks(UUID tripId) {
        return this.linkRepository.findByTripId(tripId).stream().map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
