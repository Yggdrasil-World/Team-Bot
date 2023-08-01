package de.overcraft.util.userinfo;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;

public interface UserInfoFactory {
    static UserInfo CreateUserInfo(UserMessageInfo info) {
        return new UserInfoImpl(info);
    }
    interface Meta {
        static UserMessageInfo CreateUserMessageInfo(Message message) {
            return new UserMessageInfoImpl(message);
        }
        static UserMessageInfo CreateUserMessageInfo(String string, DiscordApi api) {
            String[] split = string.split(",");
            return new UserMessageInfoImpl(api.getChannelById(split[1]).get().asTextChannel().get().getMessageById(split[0]).join());
        }
    }
}
