package de.overcraft.command.commands;

import de.overcraft.command.SlashCommandRegister;
import org.javacord.api.interaction.SlashCommand;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public interface SlashCommandHandler {

    CompletableFuture<SlashCommand> registerSlashCommand(SlashCommandRegister register);

    default Set<CompletableFuture<SlashCommand>> registerSlashCommands(Collection<SlashCommandRegister> registers) {
        return registers.stream().map(this::registerSlashCommand).collect(Collectors.toUnmodifiableSet());
    }

}
