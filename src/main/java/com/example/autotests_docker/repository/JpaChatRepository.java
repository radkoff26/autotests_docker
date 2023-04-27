package com.example.autotests_docker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.autotests_docker.model.Chat;


@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN FETCH c.links WHERE c.id = :chatId")
    Optional<Chat> findByIdWithLinks(long chatId);
}
