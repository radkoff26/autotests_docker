package com.example.autotests_docker.service.interfaces;

import java.net.URI;
import java.util.Collection;

import com.example.autotests_docker.model.Link;

public interface LinkService {
    Link add(long tgChatId, URI url);
    Link remove(long tgChatId, URI url);
    Collection<Link> listAll(long tgChatId);
}
