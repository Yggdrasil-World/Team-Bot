package de.overcraft.logging.messages;

import org.javacord.api.interaction.SlashCommandInteraction;

public class SlashCommandInteractionMessage extends InteractionMessage<SlashCommandInteraction>{

    protected String message;

    public SlashCommandInteractionMessage(SlashCommandInteraction interaction) {
        super(interaction);
    }
    public SlashCommandInteractionMessage(SlashCommandInteraction interaction, String message) {
        super(interaction);
        this.message = message;
    }

    @Override
    public String getFormattedMessage() {
        StringBuilder builder = new StringBuilder(super.getFormattedMessage());
        builder.append(", ");
        builder.append(String.format("SlashCommandInteraction (fullCmdName: %s)", interaction.getFullCommandName()));
        builder.append(" - ");
        builder.append(message);
        return builder.toString();
    }

    @Override
    public String getFormat() {
        return "";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{interaction};
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }
}
