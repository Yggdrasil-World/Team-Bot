package de.overcraft.command.commands;

import de.overcraft.Bot;
import de.overcraft.BotManager;
import de.overcraft.command.RegisterCommand;
import de.overcraft.command.SlashCommandRegister;
import de.overcraft.logging.logger.file.console.DefaultLogger;
import de.overcraft.logging.messages.SlashCommandInteractionMessage;
import de.overcraft.util.userinfo.UserInfo;
import de.overcraft.util.userinfo.UserInfoManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.*;

import java.util.List;
import java.util.Optional;

@RegisterCommand
public class UserInfoCommand implements SlashCommandRegister {
    private static final Logger logger = LogManager.getLogger(DefaultLogger.class);
    private static final Marker marker = MarkerManager.getMarker("SC_INTERACTION");

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
        DiscordApi api = interaction.getApi();
        SlashCommandInteractionOption action = interaction.getOptionByName("actions").orElseThrow().getOptionByIndex(0).orElseThrow();
        BotManager manager;
        Optional<Server> server;
        Bot bot;
        UserInfoManager infoManager;
        UserInfo info;
        String responseMessage = "Encountered an error";
        if(action.getName().equals("activity")) {
            logger.trace(marker, new SlashCommandInteractionMessage(interaction, "Confirmed activity command"));
            manager = BotManager.get();
            if(manager == null) {
                logger.error(marker, new SlashCommandInteractionMessage(interaction, "Bot manager not assigned! Exiting"));
                return;
            }
            logger.trace(marker, new SlashCommandInteractionMessage(interaction, "Getting server of interaction"));
            server = interaction.getServer();
            if(server.isEmpty()) {
                logger.error(marker, new SlashCommandInteractionMessage(interaction, "Server not found! Exiting"));
                return;
            }
            logger.trace(marker, new SlashCommandInteractionMessage(interaction, "Getting bot instance"));
            bot = manager.getBot(server.get().getId());
            if(bot == null) {
                logger.error(marker, new SlashCommandInteractionMessage(interaction, "No bot instance found! Exiting"));
                return;
            }
            logger.trace(marker, new SlashCommandInteractionMessage(interaction, "Getting info manager"));
            infoManager = bot.getUserInfoManager();
            if(infoManager == null) {
                logger.error(marker, new SlashCommandInteractionMessage(interaction, "No info manager found! Exiting"));
                return;
            }
            logger.trace(marker, new SlashCommandInteractionMessage(interaction, "Constructing response for command"));
            info = infoManager.getInfo(user);
            if(info == null) {
                responseMessage = "No records for this user";
                return;
            }else {
                Message message = api.getTextChannelById(info.messageInfo().channelId()).orElseThrow().getMessageById(info.messageInfo().lastMessageId()).join();
                responseMessage = user.getName() + " last activity: " + info.lastActivity().toString() + "\nMessage: " + message.getLink() + " on " + info.messageInfo().lastActivity().toString();
            }

            logger.trace(marker, new SlashCommandInteractionMessage(interaction, "Responding"));
            interaction.createImmediateResponder().setContent(responseMessage).respond();
        }
    }
}
