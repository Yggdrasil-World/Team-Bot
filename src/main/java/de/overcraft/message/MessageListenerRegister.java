package de.overcraft.message;

import org.javacord.api.entity.message.Message;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.function.Supplier;

public interface MessageListenerRegister extends Supplier<Message>, MessageCreateListener {
}
