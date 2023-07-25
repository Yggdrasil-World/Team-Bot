package de.overcraft.util.builder;

import de.overcraft.exceptions.BuilderIncompleteException;
import de.overcraft.strings.packages.InviteCommandStrings;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import java.util.Collection;


public interface Invite {

    MessageBuilder requestMessage();
    MessageBuilder linkMessage();

    static Invite CreateNew(User requestingUser, Collection<User> approvalUsers, Role section) throws BuilderIncompleteException {
        if(requestingUser == null || section == null || approvalUsers == null)
            throw new BuilderIncompleteException();
        if(approvalUsers.isEmpty())
            throw new BuilderIncompleteException();

        StringBuilder approvalUsersMentionBuilder = new StringBuilder();
        for (User approvalUser : approvalUsers) {
            approvalUsersMentionBuilder.append(approvalUser.getMentionTag());
            approvalUsersMentionBuilder.append("\n");
        }

        MessageBuilder requestMessage = new MessageBuilder()
                .setContent(InviteCommandStrings.REQUEST.RESPONDER_REQUESTING_MESSAGE.formatted(
                        requestingUser.getMentionTag(),
                        section.getMentionTag(),
                        approvalUsersMentionBuilder.toString())
                )
                .addComponents(ActionRow.of(
                        InviteCommandStrings.REQUEST.COMPONENT.BUTTON_ALLOW.COMPONENT,
                        InviteCommandStrings.REQUEST.COMPONENT.BUTTON_DENY.COMPONENT
                ));
        MessageBuilder linkMessage = new MessageBuilder()
                .setContent(InviteCommandStrings.REQUEST.RESPONDER_INVITE_LINK.formatted(requestingUser.getMentionTag()))
                .addComponents(ActionRow.of(
                        InviteCommandStrings.REQUEST.COMPONENT.BUTTON_LINK.COMPONENT
                ));
        return new Invite() {
            @Override
            public MessageBuilder requestMessage() {
                return requestMessage;
            }

            @Override
            public MessageBuilder linkMessage() {
                return linkMessage;
            }
        };
    }

}
