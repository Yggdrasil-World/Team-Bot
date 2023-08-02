package de.overcraft.util.userinfo;

import de.overcraft.Bot;
import de.overcraft.exceptions.InvalidParameterFormatException;
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
    private final UserInfoPipeline pipeline;

    {
        pipeline = UserInfoFactory.Pipelines.CreateUserInfoGsonPipeline();
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
        return new ConcurrentHashMap<>(pipeline.convert(reader).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private void addShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                saveData(new File("logs/userinfo.json"));
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
                infoMap.put(id, UserInfoFactory.CreateUserInfo(UserInfoFactory.Metas.CreateUserMessageInfo(message)));
            }
        });
    }

    public void saveData(File file) throws IOException {
        logger.info("Saving user data");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(pipeline.convert(infoMap.entrySet()));
        writer.close();
    }

    @Override
    public UserInfo getInfo(long userId) {
        return infoMap.get(userId);
    }
}
