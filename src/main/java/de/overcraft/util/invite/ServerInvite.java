package de.overcraft.util.invite;

import de.overcraft.Bot;
import de.overcraft.Section;
import de.overcraft.strings.packages.InviteCommandStrings;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.server.invite.InviteBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface ServerInvite {
    User requestingUser();

    Section section();

    Set<User> approvalsNeededFrom();

    /**
     * @return a string containing the formatted request message
     */
    default String requestContent() {
        return InviteCommandStrings.REQUEST.RESPONDER_REQUESTING_MESSAGE.formatted(
                requestingUser().getMentionTag(),
                section().role().getMentionTag(),
                String.join("\n", approvalsNeededFrom().stream().map(User::getMentionTag).collect(Collectors.toUnmodifiableSet())));
    }

    /**
     * @return a listener which should be attached to a message with the context of {@link #requestContent()} and the components of {@link InviteCommandStrings.REQUEST.COMPONENT#BUTTON_ALLOW_AND_DENY_ACTION_ROW}
     */
    default MessageComponentCreateListener requestMessageComponentCreateListener() {
        return event -> {
            MessageComponentInteraction interaction = event.getMessageComponentInteraction();
            User interactingUser = interaction.getUser();
            if (!approvalsNeededFrom().contains(interactingUser))
                return;
            String id = interaction.getCustomId();
            if (id.equals(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_ALLOW.ID)) {
                approve(interactingUser);
            } else if (id.equals(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_DENY.ID)) {
                interaction.createImmediateResponder()
                        .setContent(InviteCommandStrings.REQUEST.RESPONDER_DENY_MESSAGE.formatted(
                                section().role().getMentionTag(),
                                requestingUser().getMentionTag(),
                                interaction.getUser())
                        ).respond();
            }
            if (approvalsNeededFrom().isEmpty()) {
                Invite invite = new InviteBuilder(Bot.get().getWelcomeChannel()).setUnique(true).setMaxUses(1).create().join();
                MessageComponentCreateListener linkMessageComponentCreateListener = e -> {
                    MessageComponentInteraction inviteInteraction = e.getMessageComponentInteraction();
                    User inviteInteractingUser = interaction.getUser();
                    if (!inviteInteractingUser.equals(requestingUser()))
                        return;
                    if (!inviteInteraction.getCustomId().equals(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_LINK.ID))
                        return;
                    inviteInteraction.createImmediateResponder().setContent(invite.getUrl().toString()).setFlags(MessageFlag.EPHEMERAL).respond();
                };

                interaction.createImmediateResponder()
                        .setContent(InviteCommandStrings.REQUEST.RESPONDER_ALLOW_MESSAGE.formatted(
                                section().role().getMentionTag(),
                                requestingUser().getMentionTag()
                        )).respond();
                new MessageBuilder()
                        .setContent(InviteCommandStrings.REQUEST.RESPONDER_INVITE_LINK.formatted(requestingUser().getMentionTag()))
                        .addComponents(ActionRow.of(
                                InviteCommandStrings.REQUEST.COMPONENT.BUTTON_LINK.COMPONENT
                        )).send(interaction.getChannel().get().asServerTextChannel().get()).thenApply(message -> message.addMessageComponentCreateListener(linkMessageComponentCreateListener));
            }
        };
    }

    /**
     * @param user user to approve invite
     * @return this
     */
    default ServerInvite approve(User user) {
        if (!approvalsNeededFrom().contains(user))
            return this;
        approvalsNeededFrom().remove(user);
        return this;
    }

    /**
     * @return whether this invite is approved
     */
    default boolean isApproved() {
        return approvalsNeededFrom().isEmpty();
    }

    /**
     * @param requestingUser the user requesting the invite
     * @param section the section the request is linked to
     * @param approvalsNeededFrom who's approvals are needed for the invite to be allowed
     * @return a new ServerInvite
     */
    static ServerInvite CreateNew(User requestingUser, Section section, Set<User> approvalsNeededFrom) {
        return new ServerInviteImpl(requestingUser, section, new HashSet<>(approvalsNeededFrom));
    }

}
