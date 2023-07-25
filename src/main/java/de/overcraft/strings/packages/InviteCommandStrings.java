package de.overcraft.strings.packages;

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
        String RESPONDER_REQUESTING_MESSAGE = "%s requesting invite for a new @**%s**\nApprove needed from:\n%s";
        String RESPONDER_DENY_MESSAGE = "Invite for for %s, %s: **Denied!**";
        String RESPONDER_ALLOW_MESSAGE = "Invite for %s, %s: **Approved!**";
        String RESPONDER_INVITE_LINK = "%s invite ready:";
        String RESPONDER_INSUFFICIENT_RIGHTS = "You have not the sufficient rights to approve or deny an invite";
        String RESPONDER_NOT_ALLOWED_TO_VIEW_INVITE = "You are not allowed to view the invite! If you need one you have to request it yourself";

        interface COMPONENT {
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
