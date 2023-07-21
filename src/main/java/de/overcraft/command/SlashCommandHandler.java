package de.overcraft.command;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SlashCommandHandler{
    private final Map<Long, SlashCommandCreateListener> handles;
    private final DiscordApi api;

    public SlashCommandHandler(DiscordApi api) {
        this.handles = new ConcurrentHashMap<>();
        this.api = api;
        api.addSlashCommandCreateListener(this::rootHandle);
    }

    private void rootHandle(SlashCommandCreateEvent event) {
        if(!handles.containsKey(event.getSlashCommandInteraction().getCommandId()))
            return;
        handles.get(event.getSlashCommandInteraction().getCommandId()).onSlashCommandCreate(event);
    }

    public SlashCommand registerSlashCommand(SlashCommandRegister register, long serverId) {
        if (api.getServerById(serverId).isEmpty())
            return null;
        SlashCommand command = register.get().createForServer(api.getServerById(serverId).get()).join();
        handles.put(command.getId(), register);
        return command;
    }

    public Set<SlashCommand> registerSlashCommands(Collection<SlashCommandRegister> registers, long serverId) {
        return registers.stream().map(register -> registerSlashCommand(register, serverId)).collect(Collectors.toUnmodifiableSet());
    }
}
