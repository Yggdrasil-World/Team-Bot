package de.overcraft.actions;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import java.util.Set;

public sealed interface Invite permits InviteImpl {
    User requestingUser();
    Role section();
    Set<User> approvalUsers();

    static Invite Create(User requestingUser, Role section, Set<User> approvalUsers) {
        return new InviteImpl(requestingUser, section, approvalUsers);
    }
}
