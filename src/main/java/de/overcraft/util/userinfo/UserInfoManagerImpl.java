package de.overcraft.util.userinfo;

import de.overcraft.Bot;
import de.overcraft.logger.file.console.DefaultLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
                LogManager.getLogger(DefaultLogger.class).info("Adding new user info to map");
                infoMap.put(id, UserInfo.CreateNew(message));
           }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Logger logger = LogManager.getLogger(DefaultLogger.class);
                logger.info("Saving user data");
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File("logs/data.txt")));
                for (Map.Entry<Long, UserInfo> entry : infoMap.entrySet()) {
                    logger.info("Saving user data: " + entry.getKey());
                    StringBuilder builder = new StringBuilder();
                    builder.append(entry.getKey());
                    builder.append("=");
                    builder.append(entry.getValue().toString());
                    writer.write(builder.toString());
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void saveData(File file) throws IOException {
        LogManager.getLogger(DefaultLogger.class).info("Saving user data");
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
