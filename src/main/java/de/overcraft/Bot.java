package de.overcraft;

import de.overcraft.command.SlashCommandHandlerImpl;
import de.overcraft.command.commands.SlashCommandHandler;
import de.overcraft.message.MessageHandler;
import de.overcraft.util.Sections;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

public interface Bot {
    DiscordApi getApi();
    long serverId();
    Server server();
    SlashCommandHandler slashCommandHandler();
    MessageHandler messageHandler();
    Sections sections();
}
