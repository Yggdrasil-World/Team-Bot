package de.overcraft.logging.messages;

import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.SlashCommandInteraction;

public class SlashCommandInteractionMessage extends InteractionMessage<SlashCommandInteraction>{
    public SlashCommandInteractionMessage(SlashCommandInteraction interaction) {
        super(interaction);
    }

    @Override
    public String getFormattedMessage() {
        StringBuilder builder = new StringBuilder(super.getFormattedMessage());
        builder.append(", ");
        builder.append(String.format("SlashCommandInteraction (fullCmdName: %s)", interaction.getFullCommandName()));
        return builder.toString();
    }

    @Override
    public String getFormat() {
        return "";
    }

    @Override
    public Object[] getParameters() {
        return null;
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }
}
