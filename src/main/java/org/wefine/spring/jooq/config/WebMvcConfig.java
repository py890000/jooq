package org.wefine.spring.jooq.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wefine.spring.jooq.web.repo.InMemoryMessageRepository;
import org.wefine.spring.jooq.web.repo.MessageRepository;

@Configuration
public class MvcConfig {

    @Bean
    public MessageRepository messageRepository(){
        return new InMemoryMessageRepository();
    }
}
