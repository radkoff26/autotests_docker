INSERT INTO chat
VALUES (0);

WITH inserted_links AS (
    INSERT INTO link (link)
        VALUES ('github.com'),
               ('tinkoff.ru'),
               ('google.com')
        RETURNING id
),

     link_chat_mapping AS (
         SELECT id AS link_id, 0 AS chat_id
         FROM inserted_links
     )

INSERT
INTO link_chat (linkId, chatId)
SELECT link_id, chat_id
FROM link_chat_mapping;