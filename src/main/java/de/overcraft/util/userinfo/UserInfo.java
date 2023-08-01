package de.overcraft.util.userinfo;

import java.util.Date;

public interface UserInfo {
    UserMessageInfo messageInfo();
    default Date lastActivity() {
        return messageInfo().lastActivity();
    }
    default UserInfo with(UserMessageInfo messageInfo) {
        return UserInfoFactory.CreateUserInfo(messageInfo);
    }
}
