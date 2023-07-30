package de.overcraft.command;

import de.overcraft.util.ServerSupplier;

public interface SlashCommandHandlerFactory {
    static SlashCommandCreateListenerHandler CreateSlashCommandCreateListener() {
        return new SlashCommandCreateListenerHandlerImpl();
    }
    static SlashCommandHandler CreateSlashCommandHandler(ServerSupplier server) {
        return new SlashCommandHandlerImpl(server);
    }
}
