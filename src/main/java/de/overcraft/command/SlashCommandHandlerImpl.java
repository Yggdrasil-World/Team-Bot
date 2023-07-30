package de.overcraft.command;

import de.overcraft.util.ServerSupplier;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class SlashCommandHandlerImpl implements SlashCommandHandler {
    private final SlashCommandCreateListenerHandler handler;
    private final ServerSupplier server;

    public SlashCommandHandlerImpl(ServerSupplier server) {
        this.handler = SlashCommandHandlerFactory.CreateSlashCommandCreateListener();
        this.server = server;
        server.get().addSlashCommandCreateListener(handler);

    }

    @Override
    public CompletableFuture<SlashCommand> registerSlashCommand(SlashCommandRegister register) {
        CompletableFuture<SlashCommand> command = register.get().createForServer(server.get());
        command.thenAccept(command1 -> handler.addSlashCommandCreateListener(command1, register));
        return command;
    }

}
