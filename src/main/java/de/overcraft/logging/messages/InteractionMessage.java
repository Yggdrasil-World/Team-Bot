package de.overcraft.logging.messages;

import org.apache.logging.log4j.message.Message;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.InteractionBase;

public abstract class InteractionMessage<T extends InteractionBase> implements Message {
    protected final T interaction;

    public InteractionMessage(T interaction) {
        this.interaction = interaction;
    }

    @Override
    public String getFormattedMessage() {
        StringBuilder message = new StringBuilder("Interaction received. ");
        if(interaction.getServer().isPresent()) {
            message.append(interaction.getServer().get());
        }
        message.append(", ");
        message.append(interaction.getUser().toString());
        message.append(", ");
        message.append(String.format("Interaction (id: %s)", interaction.getId()));
        return message.toString();
    }

}
