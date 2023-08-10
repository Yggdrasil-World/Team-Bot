package de.overcraft;


import de.overcraft.logging.LoggingHooksUtil;
import de.overcraft.logging.logger.file.console.DefaultLogger;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.apache.logging.log4j.LogManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class EntryPoint {
    public static void main(String[] args) {
        LogManager.getLogger(DefaultLogger.class).info("Starting");
        Dotenv dotenv = new DotenvBuilder().load();
        DiscordApi api = new DiscordApiBuilder().setToken(dotenv.get("DISCORD_API_KEY")).login().join();
        LoggingHooksUtil.AddHooks(api);
        BotManager botManager = new BotManager(api.getServers(), api);
    }
}
