package de.overcraft.command.commands;

import de.overcraft.Bot;
import de.overcraft.BotManager;
import de.overcraft.command.RegisterCommand;
import de.overcraft.command.SlashCommandRegister;
import de.overcraft.logger.file.console.DefaultLogger;
import de.overcraft.util.userinfo.UserInfo;
import de.overcraft.util.userinfo.UserInfoManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RegisterCommand
public class UserInfoCommand implements SlashCommandRegister {
    Logger logger = LogManager.getLogger(DefaultLogger.class);
    @Override
    public SlashCommandBuilder get() {
        return SlashCommand.with("userinfo", "access info about user",
                List.of(
                        SlashCommandOption.createSubcommandGroup("actions", "action to do",
                                List.of(
                                        SlashCommandOption.createSubcommand("activity", "shows activity status of user", List.of(
                                                SlashCommandOption.createUserOption("user", "which user to show info about", true)
                                        ))
                                ))
                )
        ).setEnabledInDms(false);
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        User user = interaction.getArgumentUserValueByName("user").orElseThrow();
        DiscordApi api = user.getApi();
        logger.info("Full command name: " + interaction + "   Command name: " + interaction.getCommandName());
        SlashCommandInteractionOption action = interaction.getOptionByName("actions").orElseThrow().getOptionByIndex(0).orElseThrow();
        if(action.getName().equals("activity")) {
            logger.info("Getting user info about activity from " + user.getName());
            BotManager manager = BotManager.get();
            if(manager == null) {
                logger.error("BotManager not assigned");
                return;
            }
            logger.info("Getting user info");
            Optional<Server> server = interaction.getServer();
            if(server.isEmpty()) {
                logger.error("Command not executed in a server");
                return;
            }
            logger.info("Trying to get bot");
            Bot bot = manager.getBot(server.get().getId());
            if(bot == null) {
                logger.error("No bot instance found for this server");
                return;
            }
            logger.info("Trying to get info manager");
            UserInfoManager infoManager = bot.getUserInfoManager();
            if(infoManager == null) {
                logger.error("No info manager provided by bot");
                return;
            }
            UserInfo info = infoManager.getInfo(user);
            logger.info("Getting last send message");
            Message message = api.getTextChannelById(info.messageInfo().channelId()).orElseThrow().getMessageById(info.messageInfo().lastMessageId()).join();
            String msg = user.getName() + " last activity: " + info.lastActivity().toString() + "\nMessage: " + message.getLink() + " on " + info.messageInfo().lastActivity().toString();
            logger.info("Responding");
            interaction.createImmediateResponder().setContent(msg).respond();
        }
        interaction.createImmediateResponder().setContent("Encountered an error").respond();
    }
}
