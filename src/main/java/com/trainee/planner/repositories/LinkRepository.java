package com.trainee.planner.repositories;

import com.trainee.planner.domain.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {
}
