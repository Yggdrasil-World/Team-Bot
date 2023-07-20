package de.overcraft.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler {

    private final Map<Long, MessageCreateListener> messageHandles;
    private final Map<Long, MessageComponentCreateListener> messageComponentHandles;

    private final DiscordApi api;

    public MessageHandler(DiscordApi api) {
        this.messageHandles = new ConcurrentHashMap<>();
        this.messageComponentHandles = new ConcurrentHashMap<>();
        this.api = api;
        api.addMessageCreateListener(this::rootMessageHandle);
        api.addMessageComponentCreateListener(this::rootMessageComponentHandle);
    }

    private void rootMessageHandle(MessageCreateEvent event) {
        if(!messageHandles.containsKey(event.getMessageId()))
            return;
        messageHandles.get(event.getMessageId()).onMessageCreate(event);
    }
    private void rootMessageComponentHandle(MessageComponentCreateEvent event) {
        System.out.println("MessageComponentEvent dispatched with id: " + event.getInteraction().getId() + " and message id: " + event.getMessageComponentInteraction().getMessage().getId());
        if(!messageComponentHandles.containsKey(event.getMessageComponentInteraction().getMessage().getId()))
            return;
        System.out.println("Listener found");
        messageComponentHandles.get(event.getMessageComponentInteraction().getMessage().getId()).onComponentCreate(event);
    }

    public void addMessageCreateListener(Message message, MessageCreateListener listener) {
        messageHandles.put(message.getId(), listener);
    }
    public void addMessageComponentCreateListener(Message message, MessageComponentCreateListener listener) {
        messageComponentHandles.put(message.getId(), listener);
    }

}
