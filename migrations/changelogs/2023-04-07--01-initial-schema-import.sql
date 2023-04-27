--liquibase formatted sql

--changeset Alexander-1703:add-link-table
CREATE TABLE IF NOT EXISTS link
(
    id        BIGSERIAL PRIMARY KEY,
    link      VARCHAR(512) UNIQUE       NOT NULL,
    updatedAt TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

--changeset Alexander-1703:add-chat-table
CREATE TABLE IF NOT EXISTS chat
(
    id BIGINT PRIMARY KEY
);

--changeset Alexander-1703:add-link_chat-table
CREATE TABLE IF NOT EXISTS link_chat
(
    linkId BIGINT,
    chatId BIGINT,
    PRIMARY KEY (linkId, chatId),
    FOREIGN KEY (linkId) REFERENCES link,
    FOREIGN KEY (chatId) REFERENCES chat
);