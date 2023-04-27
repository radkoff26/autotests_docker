package com.example.autotests_docker.jpa;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.autotests_docker.model.Chat;
import com.example.autotests_docker.model.Link;
import com.example.autotests_docker.repository.JpaChatRepository;
import com.example.autotests_docker.repository.JpaLinkRepository;
import com.example.autotests_docker.service.JpaSubscriptionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JpaSubscriptionServiceTest extends JpaChatServiceTest {
    private static final long NOT_EXISTING_ID = -1L;
    private static final int EXPECTED_FILLED_TABLE_SIZE = 3;
    private static final int EXPECTED_EMPTY_TABLE_SIZE = 0;

    @Autowired
    private JpaSubscriptionService subscriptionService;
    @Autowired
    private JpaLinkRepository linkRepository;
    @Autowired
    private JpaChatRepository chatRepository;

    @Sql(scripts = {"/sql/chat/add_chat_with_id_0.sql", "/sql/link/add_test_link.sql"})
    @Test
    @Transactional
    @Rollback
    public void addLinkToChat_chatExist_returnTrue() {
        long chatId = chatRepository.findAll().get(0).getId();
        long linkId = linkRepository.findAll().get(0).getId();
        assertTrue(subscriptionService.addLinkToChat(linkId, chatId));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    @Transactional
    @Rollback
    public void addLinkToChat_chatNotExist_returnFalse() {
        long linkId = linkRepository.findAll().get(0).getId();
        assertFalse(subscriptionService.addLinkToChat(linkId, NOT_EXISTING_ID));
    }

    @Sql(scripts = {"/sql/chat/add_chat_with_id_0.sql", "/sql/link/add_test_link.sql"})
    @Test
    @Transactional
    @Rollback
    public void removeLinkFromChat_chatExist_returnTrue() {
        long chatId = chatRepository.findAll().get(0).getId();
        long linkId = linkRepository.findAll().get(0).getId();
        subscriptionService.addLinkToChat(linkId, chatId);
        assertTrue(subscriptionService.removeLinkFromChat(linkId, chatId));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    @Transactional
    @Rollback
    public void removeLinkFromChat_chatNotExist_returnFalse() {
        long linkId = linkRepository.findAll().get(0).getId();
        assertFalse(subscriptionService.removeLinkFromChat(linkId, NOT_EXISTING_ID));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    public void addLinkToChat_linkNotExist_returnFalse() {
        long chatId = chatRepository.findAll().get(0).getId();
        assertFalse(subscriptionService.addLinkToChat(NOT_EXISTING_ID, chatId));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    public void removeLinkFromChat_linkNotExist_returnFalse() {
        long chatId = chatRepository.findAll().get(0).getId();
        assertFalse(subscriptionService.removeLinkFromChat(NOT_EXISTING_ID, chatId));
    }


    @Sql(scripts = {"/sql/chat/add_three_chats.sql", "/sql/link/add_test_link.sql"})
    @Test
    @Transactional
    @Rollback
    public void linkId_findChatsByLinkId_listOfChats() {
        long linkId = linkRepository.findAll().get(0).getId();
        List<Long> listChatId = chatRepository.findAll().stream().map(Chat::getId).toList();
        for (var chatId : listChatId) {
            subscriptionService.addLinkToChat(linkId, chatId);
        }
        assertEquals(EXPECTED_FILLED_TABLE_SIZE, subscriptionService.findChatsByLinkId(linkId).size());
    }

    @Sql(scripts = {"/sql/chat/add_chat_with_id_0.sql", "/sql/link/add_three_links.sql"})
    @Test
    @Transactional
    @Rollback
    public void chatId_findLinksByChatId_listOfLinks() {
        long chatId = chatRepository.findAll().get(0).getId();
        List<Long> listLinkId = linkRepository.findAll().stream().map(Link::getId).toList();
        for (var linkId : listLinkId) {
            subscriptionService.addLinkToChat(linkId, chatId);
        }
        assertEquals(EXPECTED_FILLED_TABLE_SIZE, subscriptionService.findLinksByChatId(chatId).size());
    }

    @Sql(scripts = {"/sql/chat/add_three_chats.sql", "/sql/link/add_test_link.sql"})
    @Test
    @Transactional
    @Rollback
    public void linkId_findChatsByLinkId_emptyListOfChats() {
        long linkId = linkRepository.findAll().get(0).getId();
        assertEquals(EXPECTED_EMPTY_TABLE_SIZE, subscriptionService.findChatsByLinkId(linkId).size());
    }

    @Sql(scripts = {"/sql/chat/add_chat_with_id_0.sql", "/sql/link/add_three_links.sql"})
    @Test
    @Transactional
    @Rollback
    public void chatId_findLinksByChatId_emptyListOfLinks() {
        long chatId = chatRepository.findAll().get(0).getId();
        assertEquals(EXPECTED_EMPTY_TABLE_SIZE, subscriptionService.findLinksByChatId(chatId).size());
    }
}
