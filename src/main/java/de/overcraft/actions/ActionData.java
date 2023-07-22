package de.overcraft.actions;

import org.javacord.api.DiscordApi;

public interface ActionData<T>{
    T data();
    DiscordApi api();
}
