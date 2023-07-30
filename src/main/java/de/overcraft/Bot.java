package de.overcraft;

import de.overcraft.command.CommandFinder;
import de.overcraft.command.SlashCommandHandler;
import de.overcraft.message.MessageHandler;

import org.javacord.api.DiscordApi;

public class Bot {

    private static Bot bot;
    private static final long DefaultServer = 1021108286302453821L;
    private final DiscordApi api;
    private final SlashCommandHandler slashCommandHandler;
    private final MessageHandler messageHandler;
    private final Sections sections;

    public Bot(DiscordApi api) {
        bot = this;
        this.api = api;
        this.sections = new Sections(Section.Of(new long[]{503603263862734858L, 415544998558433280L}), Section.Of(new long[]{351264499124273152L}), Section.Of(new long[]{1082920396724121600L}));
        this.messageHandler = new MessageHandler(api);
        this.slashCommandHandler = new SlashCommandHandler(api);

        registerCommands();
    }

    private void registerCommands() {
        slashCommandHandler.registerSlashCommands(CommandFinder.FindCommands(), DefaultServer);
    }

    public DiscordApi getApi() {
        return api;
    }

    public Sections getSections() {
        return sections;
    }

    public static Bot get() {
        return bot;
    }

    public SlashCommandHandler getSlashCommandHandler() {
        return slashCommandHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
