package de.overcraft.util.userinfo;

import org.javacord.api.entity.message.Message;

public interface UserMessageInfo extends UserInfoMeta {

    long lastMessageId();
    long channelId();
    void setLastMessage(Message message);
}
