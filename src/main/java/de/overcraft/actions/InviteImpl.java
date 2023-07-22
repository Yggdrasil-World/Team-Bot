package de.overcraft.actions;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import java.util.Set;

public record InviteImpl(User requestingUser, Role section, Set<User> approvalUsers) implements Invite{
}
