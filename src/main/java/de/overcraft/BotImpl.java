package de.overcraft;

import de.overcraft.command.CommandFinder;
import de.overcraft.command.SlashCommandHandler;
import de.overcraft.message.MessageHandler;

import de.overcraft.util.Section;
import de.overcraft.util.Sections;
import org.javacord.api.DiscordApi;

public class BotImpl {

    private static BotImpl botImpl;
    private final long serverId;
    private final DiscordApi api;
    private final SlashCommandHandler slashCommandHandler;
    private final MessageHandler messageHandler;
    private final Sections sections;

    public BotImpl(DiscordApi api, long serverId) {
        botImpl = this;
        this.serverId = serverId;
        this.api = api;
        this.sections = new Sections(Section.Of(new long[]{503603263862734858L, 415544998558433280L}), Section.Of(new long[]{351264499124273152L}), Section.Of(new long[]{1082920396724121600L}));
        this.messageHandler = new MessageHandler(api);
        this.slashCommandHandler = new SlashCommandHandler(api);

        registerCommands();
    }

    private void registerCommands() {
        slashCommandHandler.registerSlashCommands(CommandFinder.FindCommands(), serverId);
    }

    public DiscordApi getApi() {
        return api;
    }

    public Sections getSections() {
        return sections;
    }

    public static BotImpl get() {
        return botImpl;
    }

    public SlashCommandHandler getSlashCommandHandler() {
        return slashCommandHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
