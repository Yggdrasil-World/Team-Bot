package de.overcraft.command;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

public interface SlashCommandCreateListenerHandler extends SlashCommandCreateListener {
    default void addSlashCommandCreateListener(SlashCommand command, SlashCommandCreateListener listener) {
        addSlashCommandCreateListener(command.getId(), listener);
    }
    void addSlashCommandCreateListener(long commandId, SlashCommandCreateListener listener);
}
