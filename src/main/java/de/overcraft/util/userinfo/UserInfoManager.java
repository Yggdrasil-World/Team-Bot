package de.overcraft.util.userinfo;

import org.javacord.api.entity.user.User;

public interface UserInfoManager {
    UserInfo getInfo(User user);
    UserInfo getInfo(long userId);
}
