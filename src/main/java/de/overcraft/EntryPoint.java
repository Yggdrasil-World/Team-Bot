package de.overcraft;


import de.overcraft.logger.file.console.DefaultLogger;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.apache.logging.log4j.LogManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;

public class EntryPoint {
    public static void main(String[] args) {
        LogManager.getLogger(DefaultLogger.class).trace("Test trace message");
        LogManager.getLogger(DefaultLogger.class).debug("Test debug message");
        Dotenv dotenv = new DotenvBuilder().load();
        DiscordApi api = new DiscordApiBuilder().setToken(dotenv.get("DISCORD_API_KEY")).login().join();
        BotManager botManager = new BotManager(api.getServers(), api);
    }
}
