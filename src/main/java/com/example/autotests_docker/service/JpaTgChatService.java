package com.example.autotests_docker.service;

import org.springframework.transaction.annotation.Transactional;

import com.example.autotests_docker.model.Chat;
import com.example.autotests_docker.repository.JpaChatRepository;
import com.example.autotests_docker.service.interfaces.TgChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            log.info(tgChatId + " already registered");
        } else {
            Chat chat = new Chat();
            chat.setId(tgChatId);

            chatRepository.save(chat);
            log.info(tgChatId + " successfully registered");
        }
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        if (chatRepository.findById(tgChatId).isEmpty()) {
            log.info(tgChatId + " was not registered");
        } else {
            chatRepository.deleteById(tgChatId);
            log.info(tgChatId + " successfully unregistered");
        }
    }
}
