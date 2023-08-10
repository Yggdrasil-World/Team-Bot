package de.overcraft.util.invite;

import de.overcraft.strings.commands.InviteCommandStrings;
import de.overcraft.util.Section;
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
            if (!approvalsNeededFrom().contains(interactingUser)) {
                interaction.createImmediateResponder().setContent(InviteCommandStrings.REQUEST.RESPONDER_NOT_ALLOWED_TO_APPROVE_INVITE).setFlags(MessageFlag.EPHEMERAL).respond();
                return;
            }
            String id = interaction.getCustomId();
            if (id.equals(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_ALLOW.ID)) {
                interaction.createImmediateResponder().setContent(InviteCommandStrings.REQUEST.RESPONDER_INVITE_APPROVED_CONFIRMATION).setFlags(MessageFlag.EPHEMERAL);
                approve(interactingUser);
            } else if (id.equals(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_DENY.ID)) {
                interaction.createImmediateResponder()
                        .setContent(InviteCommandStrings.REQUEST.RESPONDER_DENY_MESSAGE.formatted(
                                section().role().getMentionTag(),
                                requestingUser().getMentionTag(),
                                interaction.getUser())
                        ).respond();
            }
            // If invite is approved create invite link message
            if (approvalsNeededFrom().isEmpty()) {
                interaction.createImmediateResponder()
                        .setContent(InviteCommandStrings.REQUEST.RESPONDER_ALLOW_MESSAGE.formatted(
                                section().role().getMentionTag(),
                                requestingUser().getMentionTag()
                        )).respond();
                new MessageBuilder()
                        .setContent(InviteCommandStrings.REQUEST.RESPONDER_INVITE_LINK.formatted(requestingUser().getMentionTag()))
                        .addComponents(ActionRow.of(
                                InviteCommandStrings.REQUEST.COMPONENT.BUTTON_LINK.COMPONENT
                        )).send(interaction.getChannel().get().asServerTextChannel().get()).thenApply(message -> message.addMessageComponentCreateListener(linkMessageComponentCreateListener()));
            }
        };
    }

    default MessageComponentCreateListener linkMessageComponentCreateListener() {
        return  e -> {
            MessageComponentInteraction inviteInteraction = e.getMessageComponentInteraction();
            User inviteInteractingUser = requestingUser();
            Invite invite = new InviteBuilder(inviteInteraction.getChannel().get().asServerChannel().get()).setMaxUses(1).setUnique(true).create().join();
            if (!inviteInteraction.getCustomId().equals(InviteCommandStrings.REQUEST.COMPONENT.BUTTON_LINK.ID))
                return;
            if (!inviteInteractingUser.equals(requestingUser())) {
                inviteInteraction.createImmediateResponder().setContent(InviteCommandStrings.REQUEST.RESPONDER_NOT_ALLOWED_TO_VIEW_INVITE);
                return;
            }
            inviteInteraction.createImmediateResponder().setContent(invite.getUrl().toString()).setFlags(MessageFlag.EPHEMERAL).respond();
        };
    }

    /**
     * @param user user to approve invite
     * @return this
     */
    default void approve(User user) {
        if (!approvalsNeededFrom().contains(user))
            return;
        approvalsNeededFrom().remove(user);
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
