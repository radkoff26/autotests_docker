package com.example.autotests_docker.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.autotests_docker.environment.JpaIntegrationEnvironment;
import com.example.autotests_docker.repository.JpaChatRepository;
import com.example.autotests_docker.service.JpaTgChatService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JpaChatServiceTest extends JpaIntegrationEnvironment {
    private static final long NOT_EXISTING_CHAT_ID = -1L;
    private static final long EXISTING_CHAT_ID = 0L;

    @Autowired
    private JpaTgChatService chatService;
    @Autowired
    private JpaChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addChat_notInDB_save() {
        chatService.register(NOT_EXISTING_CHAT_ID);
        assertNotNull(chatRepository.findById(NOT_EXISTING_CHAT_ID));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    void addChat_existInDB_returnThisChat() {
        chatService.register(EXISTING_CHAT_ID);
        assertEquals(EXISTING_CHAT_ID, chatRepository.findById(EXISTING_CHAT_ID).get().getId());
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    void removeChat_existsInDB_returnTrue() {
        chatService.unregister(EXISTING_CHAT_ID);
        assertTrue(chatRepository.findById(EXISTING_CHAT_ID).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void removeLink_notInDB_success() {
        chatService.unregister(NOT_EXISTING_CHAT_ID);
    }
}
