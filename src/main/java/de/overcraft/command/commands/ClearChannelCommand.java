package de.overcraft.command.commands;

import de.overcraft.command.RegisterCommand;
import de.overcraft.command.SlashCommandRegister;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.Arrays;

@RegisterCommand
public class ClearChannelCommand implements SlashCommandRegister {

    @Override
    public SlashCommandBuilder get() {
        return new SlashCommandBuilder()
                .setName("clear")
                .setDescription("clear multiple things")
                .setOptions(Arrays.asList(
                        SlashCommandOption.createChannelOption("channel", "channel to clear. USE WITH CAUTION", false, Arrays.asList(ChannelType.getTextChannelTypes()))
                ))
                .setEnabledInDms(false)
                .setDefaultEnabledForPermissions(PermissionType.MANAGE_MESSAGES, PermissionType.MANAGE_CHANNELS, PermissionType.ADMINISTRATOR);
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        TextChannel channel = interaction.getArgumentChannelValueByIndex(0).orElse(interaction.getChannel().get().asServerChannel().get()).asTextChannel().get();
        interaction.createImmediateResponder().setContent("Deleting all messages").respond().join();
        MessageSet messages = channel.getMessages(0xFFFFFFFF).join();
        if(messages.isEmpty())
            return;
        channel.bulkDelete(messages);
    }
}
