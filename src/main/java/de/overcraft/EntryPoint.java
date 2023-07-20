package de.overcraft;

import de.overcraft.command.CommandFinder;
import de.overcraft.command.SlashCommandRegister;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.Set;

public class EntryPoint {
    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken("MTEzMTU4NTQ0MTY3ODc2NjIyMQ.GUB12y.cfeYBW7x901JAf1Up8oqYAiZOlvwGG9beduVD4").login().join();
        new Bot(api);
    }

}
