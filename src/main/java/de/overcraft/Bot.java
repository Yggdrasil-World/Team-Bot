package de.overcraft;

import de.overcraft.command.SlashCommandHandler;
import de.overcraft.util.Sections;
import de.overcraft.util.userinfo.UserInfoManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.util.function.Supplier;

public interface Bot {
    DiscordApi getApi();
    long getServerId();
    Server getServer();
    UserInfoManager getUserInfoManager();
    SlashCommandHandler getSlashCommandHandler();
    Sections getSections();
}
