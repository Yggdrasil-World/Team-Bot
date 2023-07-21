package de.overcraft.command;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.function.Supplier;

public interface SlashCommandRegister extends Supplier<SlashCommandBuilder>, SlashCommandCreateListener {
}
