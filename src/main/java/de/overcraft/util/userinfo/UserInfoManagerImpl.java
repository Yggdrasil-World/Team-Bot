package de.overcraft.util.userinfo;

import de.overcraft.Bot;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.util.HashMap;
import java.util.Map;

public class UserInfoManagerImpl implements UserInfoManager {

    private final Map<Long, UserInfo> infoMap;

    public UserInfoManagerImpl(Bot bot) {
        this.infoMap = new HashMap<>();
        bot.getServer().addMessageCreateListener(event -> {
           if(!event.getMessageAuthor().isUser())
               return;
            long id = event.getMessageAuthor().asUser().get().getId();
            Message message = event.getMessage();
            if(infoMap.containsKey(id)) {
               infoMap.get(id).setLastMessage(message);
           }else {
               infoMap.put(id, UserInfo.CreateNew(message));
           }
        });
    }

    /**
     * @param user user
     * @return {@link UserInfo} about the user or null if there is none
     */
    @Override
    public UserInfo getInfo(User user) {
        return getInfo(user.getId());
    }

    /**
     * @param userId id of the user
     * @return {@link UserInfo} about the user or null if there is none
     */
    @Override
    public UserInfo getInfo(long userId) {
        return infoMap.get(userId);
    }
}
