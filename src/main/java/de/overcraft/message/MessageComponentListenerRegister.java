package de.overcraft.message;

import org.javacord.api.entity.message.Message;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;

import java.util.function.Supplier;

public interface MessageComponentListenerRegister extends Supplier<Message>, MessageComponentCreateListener {
}
