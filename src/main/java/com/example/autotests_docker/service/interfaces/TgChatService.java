package com.example.autotests_docker.service.interfaces;

public interface TgChatService {
    void register(long tgChatId);
    void unregister(long tgChatId);
}
