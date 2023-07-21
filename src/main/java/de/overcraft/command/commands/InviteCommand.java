package de.overcraft.command.commands;

import de.overcraft.Bot;
import de.overcraft.Sections;
import de.overcraft.command.RegisterCommand;
import de.overcraft.command.SlashCommandRegister;
import de.overcraft.command.SlashCommandTemplates;
import de.overcraft.strings.packages.InviteCommandStrings;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.server.invite.InviteBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.*;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;

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
        Bot bot = Bot.get();
        DiscordApi api = bot.getApi();
        User requestingUser = event.getInteraction().getUser();
        Sections.SectionEnum section = Sections.SectionEnum.values()[event.getSlashCommandInteraction().getArgumentDecimalValueByIndex(0).get().intValue()];
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        StringBuilder mentionString = new StringBuilder();
        Arrays.stream(bot.getSections().getSection(section).sectionManagers())
                .mapToObj(manager -> api.getUserById(manager).join().getMentionTag())
                .forEach(mentionString::append);
        Message inviteResponder = interaction.createImmediateResponder()
                .setContent(InviteCommandStrings.REQUEST.RESPONDER_REQUESTING_MESSAGE.formatted(section.toString(), mentionString.toString()))
                .addComponents(ActionRow.of(
                        Button.success(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_ALLOW.ID, InviteCommandStrings.REQUEST.COMPONENT.BUTTON_ALLOW.LABEL),
                        Button.danger(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_DENY.ID, InviteCommandStrings.REQUEST.COMPONENT.BUTTON_DENY.LABEL)
                )
        ).respond().join().update().join();
        System.out.println("Invite approval message created. Id: " + inviteResponder.getId());

        // Component handling aka denying and accepting
        bot.getMessageHandler().addMessageComponentCreateListener(inviteResponder, e -> {
            User interactingUser = e.getInteraction().getUser();
            System.out.println("User " + interactingUser.getName() + " tried to approve invite");

            if(bot.getSections().getSection(section).isManager(interactingUser.getId())) {
                // Deny interaction
                if(!e.getMessageComponentInteraction().getCustomId().equals(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_ALLOW.ID)) {
                    e.getInteraction().createImmediateResponder().setContent(InviteCommandStrings.REQUEST.RESPONDER_DENY_MESSAGE.formatted(section.toString(), requestingUser.getMentionTag())).respond();
                    System.out.println("Invite denied");
                }
                // Allow interaction
                else {
                    Invite invite = new InviteBuilder(e.getApi().getChannelById(1021108286692528277L).get().asServerChannel().get()).setMaxUses(1).setUnique(true).create().join();
                    // Create button with invite only for the invite requester
                    Message link = e.getInteraction().createImmediateResponder()
                            .setContent(InviteCommandStrings.REQUEST.RESPONDER_ALLOW_MESSAGE.formatted(section.toString(), requestingUser.getMentionTag()))
                            .addComponents(ActionRow.of(
                                Button.secondary(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_LINK.ID, InviteCommandStrings.REQUEST.COMPONENT.BUTTON_LINK.LABEL)
                            ))
                            .respond().join().update().join();

                    bot.getMessageHandler().addMessageComponentCreateListener(link, ev -> {
                        if(!ev.getInteraction().getUser().equals(requestingUser)) {
                            ev.getInteraction().createImmediateResponder()
                                    .setContent(InviteCommandStrings.REQUEST.RESPONDER_NOT_ALLOWED_TO_VIEW_INVITE)
                                    .setFlags(MessageFlag.EPHEMERAL)
                                    .respond();
                            return;
                        }

                        ev.getInteraction().createImmediateResponder()
                                .setContent(InviteCommandStrings.REQUEST.RESPONDER_INVITE_LINK.formatted(invite.getUrl()))
                                .setFlags(MessageFlag.EPHEMERAL)
                                .respond();
                    });

                    System.out.println("Invite approved");
                }

                inviteResponder.createUpdater().removeAllComponents().applyChanges();
            }else {
                e.getInteraction().createImmediateResponder().setContent(InviteCommandStrings.REQUEST.RESPONDER_INSUFFICIENT_RIGHTS).setFlags(MessageFlag.EPHEMERAL).respond();
            }

        });
    }


}
