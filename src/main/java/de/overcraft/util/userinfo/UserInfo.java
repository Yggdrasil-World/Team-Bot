package de.overcraft.util.userinfo;

import org.javacord.api.entity.message.Message;

import java.util.Date;

public interface UserInfo {
    default Date lastActivity() {
        return Date.from(lastMessage().getCreationTimestamp());
    }
    Message lastMessage();
    void setLastMessage(Message message);

    static UserInfo CreateNew(Message message) {
        return new UserInfoImpl(message);
    }

}
