package com.example.autotests_docker.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.autotests_docker.model.Link;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    Link findByLink(String link);

    List<Link> findAllByUpdatedAtBefore(OffsetDateTime dateTime);

    @Query("SELECT l FROM Link l JOIN FETCH l.chats WHERE l.id = :linkId")
    Optional<Link> findByIdWithChats(long linkId);
}
