package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.repository.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.jdbc.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.ChatServiceImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.LinkServiceImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.LinkUpdaterImpl;
import ru.tinkoff.edu.java.scrapper.domain.service.ChatService;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkService;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public JdbcChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public JdbcChatLinkRepository jdbcChatLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatLinkRepository(jdbcTemplate);
    }

    @Bean
    public ChatService chatService(
            JdbcChatRepository chatRepository,
            JdbcLinkRepository linkRepository,
            JdbcChatLinkRepository chatLinkRepository) {
        return new ChatServiceImpl(chatRepository, linkRepository, chatLinkRepository);
    }

    @Bean
    public LinkService linkService(
            JdbcLinkRepository linkRepository,
            JdbcChatLinkRepository chatLinkRepository,
            StackOverflowClient stackOverflowClient) {
        return new LinkServiceImpl(linkRepository, chatLinkRepository, stackOverflowClient);
    }

    @Bean
    public LinkUpdater linkUpdater(
            JdbcLinkRepository linkRepository,
            JdbcChatLinkRepository chatLinkRepository) {
        return new LinkUpdaterImpl(linkRepository, chatLinkRepository);
    }
}
