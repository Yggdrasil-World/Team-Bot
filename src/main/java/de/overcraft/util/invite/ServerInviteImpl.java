package de.overcraft.util.invite;

import de.overcraft.util.Section;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.Set;

public record ServerInviteImpl(User requestingUser, Section section, Set<User> approvalsNeededFrom) implements ServerInvite {
}
