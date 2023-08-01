package de.overcraft.util.userinfo;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public interface UserInfo {
    default Date lastActivity() {
        return Date.from(lastMessage().getCreationTimestamp());
    }
    Message lastMessage();
    default long channelId() {
        return lastMessage().getChannel().getId();
    }
    void setLastMessage(Message message);

    static UserInfo CreateNew(Message message) {
        return new UserInfoImpl(message);
    }
    static UserInfo GetOf(String string, DiscordApi api) {
        String[] split = string.split(",");
        return new UserInfoImpl(api.getChannelById(split[1]).get().asTextChannel().get().getMessageById(split[0]).join());
    }
}
