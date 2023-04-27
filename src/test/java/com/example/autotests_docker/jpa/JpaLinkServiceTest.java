package com.example.autotests_docker.jpa;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.autotests_docker.environment.JpaIntegrationEnvironment;
import com.example.autotests_docker.model.Link;
import com.example.autotests_docker.repository.JpaLinkRepository;
import com.example.autotests_docker.service.JpaLinkService;
import com.example.autotests_docker.service.JpaSubscriptionService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JpaLinkServiceTest extends JpaIntegrationEnvironment {
    private static final int EXPECTED_FILLED_LINK_SIZE = 3;
    private static final int EXPECTED_EMPTY_LINK_SIZE = 0;
    private static final long TEST_CHAT_ID = 0L;
    private static final String TEST_LINK = "http://test-link.ru";

    @Autowired
    private JpaLinkRepository linkRepository;
    @Autowired
    private JpaLinkService linkService;
    @Autowired
    private JpaSubscriptionService subscriptionService;

    @Test
    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Transactional
    @Rollback
    void addLink_notInDB_save() {
        linkService.add(TEST_CHAT_ID, URI.create(TEST_LINK));
        assertAll(
                () -> assertNotNull(linkRepository.findByLink(TEST_LINK)),
                () -> assertEquals(TEST_LINK, subscriptionService.findLinksByChatId(TEST_CHAT_ID).get(0).getLink())
        );
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    @Transactional
    @Rollback
    void addLink_existInDB_returnThisLink() {
        assertEquals(TEST_LINK, linkService.add(TEST_CHAT_ID, URI.create(TEST_LINK)).getLink());
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    @Transactional
    @Rollback
    void removeLink_existsInDB_success() {
        Link link = linkRepository.findAll().get(0);
        subscriptionService.addLinkToChat(link.getId(), TEST_CHAT_ID);
        linkService.remove(TEST_CHAT_ID, URI.create(link.getLink()));
        assertNull(linkRepository.findByLink(link.getLink()));
    }

    @Test
    @Transactional
    @Rollback
    void removeLink_notInDB_returnNull() {
        assertNull(linkService.remove(TEST_CHAT_ID, URI.create(TEST_LINK)));
    }

    @Sql(scripts = "/sql/subscription/add_three_links_to_id_0.sql")
    @Test
    @Transactional
    @Rollback
    void findAll_returnAllLinks() {
        assertEquals(EXPECTED_FILLED_LINK_SIZE, linkService.listAll(TEST_CHAT_ID).size());
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    void findAll_emptyList() {
        assertEquals(EXPECTED_EMPTY_LINK_SIZE, linkService.listAll(TEST_CHAT_ID).size());
    }
}
