package de.overcraft;


import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;

public class EntryPoint {
    public static void main(String[] args) {
        Dotenv dotenv = new DotenvBuilder().load();
        DiscordApi api = new DiscordApiBuilder().setToken(dotenv.get("DISCORD_API_KEY")).login().join();
        for (Server server : api.getServers()) {
            BotFactory.CreateBot(api, server.getId());
        }
    }

}
