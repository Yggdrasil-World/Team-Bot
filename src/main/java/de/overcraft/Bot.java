package de.overcraft;

import de.overcraft.command.SlashCommandHandler;
import de.overcraft.util.Sections;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.util.function.Supplier;

public interface Bot {
    DiscordApi getApi();
    long getServerId();
    Server getServer();
    SlashCommandHandler getSlashCommandHandler();
    Sections getSections();
}
