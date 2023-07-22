package de.overcraft.actions;

import org.javacord.api.DiscordApi;

public interface Action<T, V>{
    V handle(ActionData<T> data);
}
