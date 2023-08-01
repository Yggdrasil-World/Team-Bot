package de.overcraft.util.userinfo;

import de.overcraft.Bot;
import de.overcraft.logger.file.console.DefaultLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserInfoManagerImpl implements UserInfoManager {

    private final Map<Long, UserInfo> infoMap;

    {
        this.infoMap = new ConcurrentHashMap<>();
    }

    public UserInfoManagerImpl(Bot bot) {
        addListeners(bot);
        addShutdownHooks();
    }

    public UserInfoManagerImpl(Bot bot, File file) {

    }

    private Map<Long, UserInfo> loadMapFromFile(Bot bot, File file) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        return reader.lines().map(s -> {
            String[] split = s.split("=");
            return Map.entry(Long.parseLong(split[0]), UserInfo.GetOf(split[1], bot.getApi()));
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void addShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                saveData(new File("logs/userinfo.data"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    private void addListeners(Bot bot) {
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
    }

    public void saveData(File file) throws IOException {
        Logger logger = LogManager.getLogger(DefaultLogger.class);
        logger.info("Saving user data");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Map.Entry<Long, UserInfo> entry : infoMap.entrySet()) {
            logger.info("Saving data for user with id: " + entry.getKey());
            String builder = entry.getKey() +
                    "=" +
                    entry.getValue().toString();
            writer.write(builder);
            writer.newLine();
        }
        writer.close();
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
