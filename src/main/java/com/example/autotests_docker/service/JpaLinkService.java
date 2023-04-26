package com.example.autotests_docker.service;

import java.net.URI;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import com.example.autotests_docker.model.Link;
import com.example.autotests_docker.repository.JpaLinkRepository;
import com.example.autotests_docker.service.interfaces.LinkService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaSubscriptionService subscriptionService;

    @Override
    @Transactional
    public Link add(long tgChatId, URI url) {
        Link link = linkRepository.findByLink(url.toString());
        if (link == null) {
            link = new Link();
            link.setLink(url.toString());

            link = linkRepository.save(link);
            log.info("add link: " + link.getLink());
        }
        subscriptionService.addLinkToChat(link.getId(), tgChatId);
        log.info("bind link " + link.getLink() + " to user " + tgChatId);
        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) {
        Link link = linkRepository.findByLink(url.toString());
        if (link == null) {
            return null;
        }
        subscriptionService.removeLinkFromChat(link.getId(), tgChatId);
        log.info("unbind link " + link.getLink() + " from " + tgChatId);
        if (subscriptionService.findChatsByLinkId(link.getId()).isEmpty()) {
            linkRepository.deleteById(link.getId());
            log.info("remove link: " + link.getLink());
        }
        return link;
    }

    @Override
    @Transactional
    public Collection<Link> listAll(long tgChatId) {
        return subscriptionService.findLinksByChatId(tgChatId);
    }
}
