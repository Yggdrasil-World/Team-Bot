package de.overcraft.util;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.util.function.Supplier;

public interface ServerSupplier extends Supplier<Server>{

    static ServerSupplier CreateNew(long serverId, DiscordApi api) {
        return () -> api.getServerById(serverId).get();
    }
}
