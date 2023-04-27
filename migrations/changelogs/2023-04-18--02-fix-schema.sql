--liquibase formatted sql

--changeset Alexander-1703:add-columns-to-link-table
ALTER TABLE link
    ADD COLUMN ghForks INT,
    ADD COLUMN ghBranches INT,
    ADD COLUMN soAnswers INT;