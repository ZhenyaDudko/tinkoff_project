package ru.tinkoff.edu.java.scrapper.domain.jdbc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRowMapper;

import java.net.URI;
import java.util.List;

//@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper linkRowMapper = new LinkRowMapper();

    private static final String INSERT_LINK_IF_NOT_EXISTS_QUERY = """
            insert into link (url, answer_count, comment_count) select ?, ?, ?
            where not exists (select from link where url = ?);
            """;

    private static final String INSERT_CHAT_LINK_GETTING_ID_FROM_LINK_QUERY = """
            insert into chat_link (chat_id, link_id)
            values (?, (select id from link where url = ?));
            """;

    private static final String DELETE_CHAT_LINK_GETTING_ID_FROM_LINK_QUERY = """
            delete from chat_link where chat_id = ? and link_id in
            (select id from link where url = ?);
            """;

    private static final String DELETE_LINK_IF_NO_ONE_REFERS_QUERY = """
            with count_refs as (select count(*) cnt from chat_link join link
            on chat_link.link_id=link.id where link.url = ?)
            delete from link where (select cnt from count_refs) = 0 and url = ?;
            """;

    private static final String SELECT_ALL_FROM_LINK_QUERY = "select * from link where url = ?";

    private static final String SELECT_ALL_LINKS_BY_CHAT_ID_QUERY = """
            select link.id as id, link.url as url, link.last_update as last_update,
            link.answer_count as answer_count, link.comment_count as comment_count " +
            from chat_link cl join link on cl.link_id = link.id where cl.chat_id = ?;
            """;

    @Override
    @Transactional
    public void add(long chatId, URI url) {
        addAnswerComment(chatId, url, null, null);
    }

    @Override
    @Transactional
    public void addAnswerComment(long chatId, URI url, Integer answerCount, Integer commentCount) {
        String urlString = url.toString();
        jdbcTemplate.update(INSERT_LINK_IF_NOT_EXISTS_QUERY, urlString, answerCount, commentCount, urlString);
        jdbcTemplate.update(INSERT_CHAT_LINK_GETTING_ID_FROM_LINK_QUERY, chatId, urlString);
    }

    @Override
    @Transactional
    public void remove(long chatId, URI url) {
        String urlString = url.toString();
        jdbcTemplate.update(DELETE_CHAT_LINK_GETTING_ID_FROM_LINK_QUERY, chatId, urlString);
        jdbcTemplate.update(DELETE_LINK_IF_NO_ONE_REFERS_QUERY, urlString, urlString);
    }

    @Override
    public List<Link> getLinksByUrl(URI url) {
        return jdbcTemplate.query(SELECT_ALL_FROM_LINK_QUERY, linkRowMapper, url.toString());
    }

    @Override
    public List<Link> getAll(long chatId) {
        return jdbcTemplate.query(SELECT_ALL_LINKS_BY_CHAT_ID_QUERY, linkRowMapper, chatId);
    }
}
