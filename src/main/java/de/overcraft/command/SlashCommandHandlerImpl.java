package de.overcraft.command;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class SlashCommandHandlerImpl implements SlashCommandHandler {
    private final SlashCommandCreateListenerHandler handler;
    private final Server server;

    public SlashCommandHandlerImpl(@Nonnull Server server) {
        this.handler = SlashCommandHandlerFactory.CreateSlashCommandCreateListener();
        this.server = server;
        server.addSlashCommandCreateListener(handler);

    }

    @Override
    public CompletableFuture<SlashCommand> registerSlashCommand(SlashCommandRegister register) {
        CompletableFuture<SlashCommand> command = register.get().createForServer(server);
        command.thenAccept(command1 -> handler.addSlashCommandCreateListener(command1, register));
        return command;
    }

}
