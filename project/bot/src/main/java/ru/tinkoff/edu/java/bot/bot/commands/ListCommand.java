package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.web.LinkResponse;
import ru.tinkoff.edu.java.bot.exceptions.web.IncorrectRequestParametersException;
import ru.tinkoff.edu.java.bot.service.LinkManager;

@Component
@Slf4j
public class ListCommand extends AbstractCommand {

    private final static String COMMAND = "/list";

    private final static String DESCRIPTION = "show a list of tracked links";

    private final static String NO_LINKS_FOUND = "There are no tracked links yet";

    private final static String NOT_REGISTERED = "You are not registered. Use /start";

    private final LinkManager linkManager;

    public ListCommand(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public String handleImpl(final Update update) {
        long chatId = update.message().chat().id();
        try {
            List<LinkResponse> links = linkManager.getLinks(chatId).links();
            if (links.isEmpty()) {
                return NO_LINKS_FOUND;
            } else {
                return links.stream()
                        .map(linkResponse -> linkResponse.url().toString())
                        .collect(Collectors.joining("\n"));
            }
        } catch (IncorrectRequestParametersException ignored) {
            return NOT_REGISTERED;
        } catch (Throwable e) {
            log.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
}
