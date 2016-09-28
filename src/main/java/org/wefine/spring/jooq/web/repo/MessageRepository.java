package org.wefine.spring.jooq.web.repo;


import org.wefine.spring.jooq.web.dto.Message;

public interface MessageRepository {

    Iterable<Message> findAll();

    Message save(Message message);

    Message findMessage(Long id);

    void deleteMessage(Long id);

}
