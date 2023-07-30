package de.overcraft;

import de.overcraft.command.CommandFinder;
import de.overcraft.command.SlashCommandHandler;
import de.overcraft.command.SlashCommandHandlerFactory;

import de.overcraft.util.Section;
import de.overcraft.util.Sections;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.security.InvalidParameterException;

public class BotImpl implements Bot{
    private final long serverId;
    private final DiscordApi api;
    private final SlashCommandHandler slashCommandHandler;
    private final Sections sections;

    public BotImpl(DiscordApi api, long serverId) {
        if(api.getServerById(serverId).isEmpty())
            throw new InvalidParameterException("Invalid server id");
        this.serverId = serverId;
        this.api = api;
        this.sections = new Sections(
                Section.Of(new long[]{503603263862734858L, 415544998558433280L}, api.getRoleById(1021111128308318248L).get()), // Builder
                Section.Of(new long[]{351264499124273152L}, api.getRoleById(1026589866328346705L).get()), // Developer
                Section.Of(new long[]{1082920396724121600L}, api.getRoleById(1021111184667189288L).get())); // Storywriter
        this.slashCommandHandler = SlashCommandHandlerFactory.CreateSlashCommandHandler(this::getServer);
        registerCommands();
    }

    private void registerCommands() {
        slashCommandHandler.registerSlashCommands(CommandFinder.FindCommands());
    }

    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getServerId() {
        return serverId;
    }

    @Override
    public Server getServer() {
        return api.getServerById(serverId).get();
    }
    public Sections getSections() {
        return sections;
    }

    public SlashCommandHandler getSlashCommandHandler() {
        return slashCommandHandler;
    }

}
