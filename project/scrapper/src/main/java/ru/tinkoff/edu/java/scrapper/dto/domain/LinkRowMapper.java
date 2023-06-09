package ru.tinkoff.edu.java.scrapper.dto.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;

public class LinkRowMapper implements RowMapper<Link> {

    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new Link()
                    .setId(rs.getLong("id"))
                    .setUrl(new URI(rs.getString("url")))
                    .setLastUpdate(rs.getObject("last_update", Timestamp.class)
                            .toInstant().atOffset(ZoneOffset.UTC))
                    .setAnswerCount(rs.getInt("answer_count"))
                    .setCommentCount(rs.getInt("comment_count"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
