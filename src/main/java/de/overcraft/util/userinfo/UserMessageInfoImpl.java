package de.overcraft.util.userinfo;

import org.javacord.api.entity.message.Message;

import java.util.Date;

public class UserMessageInfoImpl implements UserMessageInfo {

    private long lastMessageId;
    private long channelId;
    private Date lastActivity;
    public UserMessageInfoImpl(Message lastMessage) {
        setLastMessage(lastMessage);
    }

    @Override
    public long lastMessageId() {
        return lastMessageId;
    }

    @Override
    public long channelId() {
        return channelId;
    }

    @Override
    public void setLastMessage(Message message) {
        this.lastMessageId = message.getId();
        this.channelId = message.getChannel().getId();
        this.lastActivity = Date.from(message.getCreationTimestamp());
    }

    @Override
    public Date lastActivity() {
        return lastActivity;
    }

    @Override
    public String toString( ) {
        return lastMessageId + "," + channelId;
    }


}
