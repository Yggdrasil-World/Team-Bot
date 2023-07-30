package de.overcraft.command;

import org.javacord.api.entity.server.Server;

public interface SlashCommandHandlerFactory {
    static SlashCommandCreateListenerHandler CreateSlashCommandCreateListener() {
        return new SlashCommandCreateListenerHandlerImpl();
    }
    static SlashCommandHandler CreateSlashCommandHandler(Server server) {
        return new SlashCommandHandlerImpl(server);
    }
}
