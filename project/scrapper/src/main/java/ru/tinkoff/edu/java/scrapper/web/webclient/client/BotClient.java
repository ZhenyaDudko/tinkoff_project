package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import java.net.URI;
import java.util.List;

public interface BotClient {
    void LinkUpdate(long id, URI url, String description, List<Long> tgChatIds);
}