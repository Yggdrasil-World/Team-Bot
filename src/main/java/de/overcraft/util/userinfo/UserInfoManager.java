package de.overcraft.util.userinfo;

import org.javacord.api.entity.user.User;

public interface UserInfoManager {
    /**
     * @param user user
     * @return {@link UserInfo} about the user or null if there is none
     */
    default UserInfo getInfo(User user) {
        return getInfo(user.getId());
    }
    /**
     * @param userId id of the user
     * @return {@link UserInfo} about the user or null if there is none
     */
    UserInfo getInfo(long userId);
}
