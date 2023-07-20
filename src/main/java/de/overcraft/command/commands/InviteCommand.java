package de.overcraft.command.commands;

import de.overcraft.Bot;
import de.overcraft.Sections;
import de.overcraft.command.RegisterCommand;
import de.overcraft.command.SlashCommandRegister;
import de.overcraft.command.SlashCommandTemplates;
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

    private static final String DisplayName = "invite";
    @Override
    public SlashCommandBuilder get() {
        return new SlashCommandBuilder()
                .setName("invite")
                .setDescription("Invite people to this server")
                .setOptions(Arrays.asList(
                        SlashCommandTemplates.COMMAND_OPTION_SECTIONS
                    ))
                .setEnabledInDms(false);
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        Sections.SectionEnum section = Sections.SectionEnum.values()[event.getSlashCommandInteraction().getArgumentDecimalValueByIndex(0).get().intValue()];
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        Message inviteResponder = interaction.createImmediateResponder().setContent("Requesting invite:")
                .addComponents(ActionRow.of(
                        Button.success("allow-invite", "Allow"),
                        Button.danger("deny-invite", "Deny")
                )
        ).respond().join().update().join();
        System.out.println("Invite approval message created. Id: " + inviteResponder.getId());
        // Component handling aka denying and accepting
        Bot.get().getMessageHandler().addMessageComponentCreateListener(inviteResponder, e -> {
            User user = e.getInteraction().getUser();
            System.out.println("User " + user.getName() + " tried to approve invite");
            if(Bot.get().getSections().getSection(section).isManager(user.getId())) {
                if(!e.getMessageComponentInteraction().getCustomId().equals("allow-invite")) {
                    e.getInteraction().createImmediateResponder().setContent("Requesting invite: **Denied!**");
                    System.out.println("Invite denied");
                }else {
                    Invite invite = new InviteBuilder(e.getApi().getChannelById(1021108286692528277L).get().asServerChannel().get()).setMaxUses(1).create().join();
                    e.getInteraction().createImmediateResponder().setContent("Requesting invite: **Approved!**").addComponents(ActionRow.of(
                            Button.secondary("invite-link", "Link")
                    )).respond().join().update().join();
                    System.out.println("Invite approved");
                }
                inviteResponder.delete();
            }else {
                e.getInteraction().createImmediateResponder().setContent("You have not the sufficient rights to approve or deny an invite").setFlags(MessageFlag.EPHEMERAL).respond();
            }

        });
    }
}
