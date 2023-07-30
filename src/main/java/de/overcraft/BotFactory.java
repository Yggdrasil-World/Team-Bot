package de.overcraft;

import org.javacord.api.DiscordApi;

public interface BotFactory {
    static Bot CreateBot(DiscordApi api, long serverId) {
        return new BotImpl(api, serverId);
    }
}
