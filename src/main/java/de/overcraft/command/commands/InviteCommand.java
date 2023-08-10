package de.overcraft.command.commands;

import de.overcraft.Bot;
import de.overcraft.BotManager;
import de.overcraft.command.RegisterCommand;
import de.overcraft.command.SlashCommandRegister;
import de.overcraft.command.SlashCommandTemplates;
import de.overcraft.strings.commands.InviteCommandStrings;

import de.overcraft.util.Sections;
import de.overcraft.util.invite.ServerInvite;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RegisterCommand
public class InviteCommand implements SlashCommandRegister {

    @Override
    public SlashCommandBuilder get() {
        return new SlashCommandBuilder()
                .setName(InviteCommandStrings.COMMAND_DISPLAYNAME)
                .setDescription(InviteCommandStrings.COMMAND_DESCRIPTION)
                .setOptions(Arrays.asList(
                        SlashCommandTemplates.COMMAND_OPTION_SECTIONS
                    ))
                .setEnabledInDms(false);
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        Bot bot = BotManager.get().getBot(interaction.getServer().get().getId());
        DiscordApi api = bot.getApi();
        User requestingUser = event.getInteraction().getUser();
        Sections.SectionEnum section = Sections.SectionEnum.values()[event.getSlashCommandInteraction().getArgumentDecimalValueByIndex(0).get().intValue()];
        Set<User> approvalUsers = Arrays.stream(bot.getSections().getSection(section).sectionManagers())
                .mapToObj(manager -> api.getUserById(manager).join()).collect(Collectors.toUnmodifiableSet());
        ServerInvite invite = ServerInvite.CreateNew(requestingUser, bot.getSections().getSection(section), approvalUsers);
        interaction.createImmediateResponder().setContent(invite.requestContent()).addComponents(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_ALLOW_AND_DENY_ACTION_ROW).respond()
                .thenAccept(interactionOriginalResponseUpdater -> interactionOriginalResponseUpdater.update()
                        .thenAccept(message -> message.addMessageComponentCreateListener(invite.requestMessageComponentCreateListener())));
    }


}
