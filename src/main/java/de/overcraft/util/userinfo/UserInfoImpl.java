package de.overcraft.util.userinfo;

import org.javacord.api.entity.message.Message;

public class UserInfoImpl implements UserInfo {

    private Message lastMessage;

    public UserInfoImpl(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
    @Override
    public Message lastMessage() {
        return lastMessage;
    }
    @Override
    public void setLastMessage(Message message) {
        this.lastMessage = message;
    }

    @Override
    public String toString( ) {
        return lastMessage.getIdAsString();
    }
}
