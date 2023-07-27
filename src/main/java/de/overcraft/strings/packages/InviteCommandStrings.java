package de.overcraft.strings.packages;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;

public interface InviteCommandStrings {
    String COMMAND_DISPLAYNAME = "invite";
    String COMMAND_DESCRIPTION = "Invite people to this server";
    interface REQUEST {

        /**
         * 1: Who is requesting
         * 2: Which section for
         * 3: All the approvals needed
         */
        String RESPONDER_REQUESTING_MESSAGE = "%s requesting invite for a new **%s**\nApprove needed from:\n%s";
        /**
         * 1: Section mention
         * 2: User who requested mention
         * 3: User who denied mention
         */
        String RESPONDER_DENY_MESSAGE = "Invite for %s requested by %s got denied by %s: **Denied!**";
        /**
         * 1: Section mention
         * 2: User who requested mention
         */
        String RESPONDER_ALLOW_MESSAGE = "Invite for %s requested by %s: **Approved!**";
        String RESPONDER_INVITE_LINK = "%s invite ready:";
        String RESPONDER_NOT_ALLOWED_TO_APPROVE_INVITE = "Your are not capable of approving this invite";
        String RESPONDER_NOT_ALLOWED_TO_VIEW_INVITE = "You are not allowed to view the invite as you are not the one who requested it!";
        String RESPONDER_INVITE_APPROVED_CONFIRMATION = "You've successfully approved the invite";

        interface COMPONENT {

            ActionRow BUTTON_ALLOW_AND_DENY_ACTION_ROW = ActionRow.of(BUTTON_ALLOW.COMPONENT, BUTTON_DENY.COMPONENT);

            interface BUTTON_ALLOW {
                String LABEL = "Allow";
                String ID = "allow-invite";
                Button COMPONENT = Button.success(ID, LABEL);
            }
            interface BUTTON_DENY {
                String LABEL = "Deny";
                String ID = "deny-invite";
                Button COMPONENT = Button.danger(ID, LABEL);
            }

            interface BUTTON_LINK {
                String LABEL = "Link";
                String ID = "invite-link";
                Button COMPONENT = Button.secondary(ID, LABEL);
            }

        }

    }
}
