package de.overcraft.command.commands;

import de.overcraft.command.SlashCommandRegister;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandBuilder;


public class TestCommand implements SlashCommandRegister {
    @Override
    public SlashCommandBuilder get() {
        return null;
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {

    }
}
