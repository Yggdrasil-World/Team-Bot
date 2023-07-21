package de.overcraft.strings.packages;

import java.util.SplittableRandom;

public interface InviteCommandStrings {
    String COMMAND_DISPLAYNAME = "invite";
    String COMMAND_DESCRIPTION = "Invite people to this server";
    interface REQUEST {
        String RESPONDER_REQUESTING_MESSAGE = "Requesting invite for **%s**, %s:";
        String RESPONDER_DENY_MESSAGE = "Invite for for %s, %s: **Denied!**";
        String RESPONDER_ALLOW_MESSAGE = "Invite for %s, %s: **Approved!**";
        String RESPONDER_INVITE_LINK = "Invite: %s";
        String RESPONDER_INSUFFICIENT_RIGHTS = "You have not the sufficient rights to approve or deny an invite";
        String RESPONDER_NOT_ALLOWED_TO_VIEW_INVITE = "You are not allowed to view the invite! If you need one you have to request it yourself";

        interface COMPONENT {
            interface BUTTON_ALLOW {
                String LABEL = "Allow";
                String ID = "allow-invite";
            }
            interface BUTTON_DENY {
                String LABEL = "Deny";
                String ID = "deny-invite";
            }

            interface BUTTON_LINK {
                String LABEL = "Link";
                String ID = "invite-link";
            }

        }

    }
}
