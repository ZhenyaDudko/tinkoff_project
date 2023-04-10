package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.service.LinkManager;

@Component
public class StartCommand extends AbstractCommand {

    private final static String COMMAND = "/start";

    private final static String DESCRIPTION = "registration";

    private final static String REGISTERED = "Now you can send me a link to start tracking updates";

    private final LinkManager linkManager;

    public StartCommand(LinkManager linkManager) {
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
            linkManager.registerChat(chatId);
            return REGISTERED;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
