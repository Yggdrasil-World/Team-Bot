package de.overcraft;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BotManager {

    private static DiscordApi api;
    private static BotManager manager;
    private Map<Long, Bot> botMap;

    public BotManager(Set<Server> servers, DiscordApi inApi) {
        manager = this;
        api = inApi;
        botMap = new ConcurrentHashMap<>();
        for (Server server : servers) {
            botMap.put(server.getId(),BotFactory.CreateBot(server.getApi(), server.getId()));
        }
    }

    public Bot getBot(long id) {
        return botMap.get(id);
    }

    public static BotManager get() {
        return manager;
    }

    public static DiscordApi getApi() {
        return api;
    }
}
