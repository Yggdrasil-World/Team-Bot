package de.overcraft;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.util.Map;
import java.util.Set;

public class BotManager {

    private static BotManager manager;
    private Map<Long, Bot> botMap;

    public BotManager(Set<Server> servers) {
        for (Server server : servers) {
            BotFactory.CreateBot(server.getApi(), server.getId());
        }
    }

    public Bot getBot(long id) {
        return botMap.get(id);
    }

    public static BotManager get() {
        return manager;
    }
}
