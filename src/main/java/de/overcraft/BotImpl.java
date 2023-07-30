package de.overcraft;

import de.overcraft.command.CommandFinder;
import de.overcraft.command.SlashCommandHandlerImpl;

import de.overcraft.util.Section;
import de.overcraft.util.Sections;
import org.javacord.api.DiscordApi;

public class BotImpl {

    private static BotImpl botImpl;
    private final long serverId;
    private final DiscordApi api;
    private final SlashCommandHandlerImpl slashCommandHandlerInterface;
    private final Sections sections;

    public BotImpl(DiscordApi api, long serverId) {
        botImpl = this;
        this.serverId = serverId;
        this.api = api;
        this.sections = new Sections(Section.Of(new long[]{503603263862734858L, 415544998558433280L}), Section.Of(new long[]{351264499124273152L}), Section.Of(new long[]{1082920396724121600L}));
        this.slashCommandHandlerInterface = new SlashCommandHandlerImpl(api.getServerById(serverId).get());

        registerCommands();
    }

    private void registerCommands() {
        slashCommandHandlerInterface.registerSlashCommands(CommandFinder.FindCommands());
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

    public SlashCommandHandlerImpl getSlashCommandHandler() {
        return slashCommandHandlerInterface;
    }

}
