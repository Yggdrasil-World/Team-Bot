package de.overcraft.command;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlashCommandCreateListenerHandlerImpl implements SlashCommandCreateListenerHandler {

    private Map<Long, SlashCommandCreateListener> handles;

    public SlashCommandCreateListenerHandlerImpl() {
        this.handles = new ConcurrentHashMap<>();
    }

    @Override
    public void addSlashCommandCreateListener(long commandId, SlashCommandCreateListener listener) {
        handles.put(commandId, listener);
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        if(!handles.containsKey(interaction.getCommandId()))
            return;
        handles.get(interaction.getCommandId()).onSlashCommandCreate(event);
    }
}
