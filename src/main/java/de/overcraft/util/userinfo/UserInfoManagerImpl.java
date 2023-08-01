package de.overcraft.util.userinfo;

import de.overcraft.Bot;
import de.overcraft.logger.file.console.DefaultLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.Message;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserInfoManagerImpl implements UserInfoManager {

    private final Logger logger;
    private final Map<Long, UserInfo> infoMap;

    {
        logger = LogManager.getLogger(DefaultLogger.class);
        addShutdownHooks();
    }

    public UserInfoManagerImpl(Bot bot) {
        this.infoMap = new ConcurrentHashMap<>();
        addListeners(bot);

    }

    public UserInfoManagerImpl(Bot bot, File file) throws FileNotFoundException {
        this.infoMap = loadMapFromFile(bot, file);
        addListeners(bot);
    }

    private Map<Long, UserInfo> loadMapFromFile(Bot bot, File file) throws FileNotFoundException {
        logger.info("Loading user info map from file");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Map<Long, UserInfo> collect = reader.lines().map(s -> {
            String[] split = s.split("=");
            return Map.entry(Long.parseLong(split[0]), UserInfoFactory.CreateUserInfo(UserInfoFactory.Meta.CreateUserMessageInfo(split[1], bot.getApi())));
        }).collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue()));
        logger.info("Loaded " + collect.size() + " user infos from file");
        return collect;
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
                logger.info("Updating user info data for user with id: " + id);
                UserInfo userInfo = infoMap.get(id);
                userInfo.messageInfo().setLastMessage(message);
            }else {
               logger.info("Adding new user info to map");
                infoMap.put(id, UserInfoFactory.CreateUserInfo(UserInfoFactory.Meta.CreateUserMessageInfo(message)));
            }
        });
    }

    public void saveData(File file) throws IOException {
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

    @Override
    public UserInfo getInfo(long userId) {
        return infoMap.get(userId);
    }
}
