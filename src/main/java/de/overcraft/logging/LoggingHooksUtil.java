package de.overcraft.logging;

import de.overcraft.logging.logger.file.console.DefaultLogger;
import de.overcraft.logging.messages.SlashCommandInteractionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;

public class LoggingHooksUtil {
    static Logger logger = LogManager.getLogger(DefaultLogger.class);
    public static void AddHooks(DiscordApi api) {
        api.addSlashCommandCreateListener(event -> logger.info(new SlashCommandInteractionMessage(event.getSlashCommandInteraction())));
    }
}
