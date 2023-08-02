package de.overcraft.util.userinfo;

import de.overcraft.BotManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;

public interface UserInfoFactory {
    static UserInfo CreateUserInfo(UserMessageInfo info) {
        return new UserInfoImpl(info);
    }
    interface Metas {
        static UserMessageInfo CreateUserMessageInfo(Message message) {
            return new UserMessageInfoImpl(message);
        }
        static UserMessageInfo CreateUserMessageInfo(long messageId, long channelId) {
            DiscordApi api = BotManager.getApi();
            return new UserMessageInfoImpl(api.getChannelById(channelId).get().asTextChannel().get().getMessageById(messageId).join());
        }
    }

    interface Pipelines {
        static UserInfoPipeline CreateUserInfoGsonPipeline() {
            return new UserInfoGsonPipeline();
        }
    }
}
