package de.overcraft;

import de.overcraft.command.CommandFinder;
import de.overcraft.command.SlashCommandHandler;
import de.overcraft.command.SlashCommandHandlerFactory;

import de.overcraft.logging.logger.file.console.DefaultLogger;
import de.overcraft.util.Section;
import de.overcraft.util.Sections;
import de.overcraft.util.userinfo.UserInfoManager;
import de.overcraft.util.userinfo.UserInfoManagerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

public class BotImpl implements Bot{
    private final Logger logger;
    private final long serverId;
    private final DiscordApi api;
    private final SlashCommandHandler slashCommandHandler;
    private final UserInfoManager userInfoManager;
    private final Sections sections;

    public BotImpl(DiscordApi api, long serverId) {
        logger = LogManager.getLogger(DefaultLogger.class);
        if(api.getServerById(serverId).isEmpty())
            throw new InvalidParameterException("Invalid server id");
        this.serverId = serverId;
        this.api = api;
        this.sections = new Sections(
                Section.Of(new long[]{503603263862734858L, 415544998558433280L}, api.getRoleById(1021111128308318248L).get()), // Builder
                Section.Of(new long[]{351264499124273152L}, api.getRoleById(1026589866328346705L).get()), // Developer
                Section.Of(new long[]{1082920396724121600L}, api.getRoleById(1021111184667189288L).get())); // Storywriter
        this.slashCommandHandler = SlashCommandHandlerFactory.CreateSlashCommandHandler(this::getServer);
        UserInfoManager userInfoManager1;
        File file = new File("logs/userinfo.json");
        if(file.exists()) {
            logger.info("Loading info manager");
            try {
                userInfoManager1 = new UserInfoManagerImpl(this, file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            logger.info("Creating new info manager");
            userInfoManager1 = new UserInfoManagerImpl(this);
        }
        this.userInfoManager = userInfoManager1;
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

    @Override
    public UserInfoManager getUserInfoManager() {
        return userInfoManager;
    }

    public Sections getSections() {
        return sections;
    }

    public SlashCommandHandler getSlashCommandHandler() {
        return slashCommandHandler;
    }

}
