package de.overcraft.util.userinfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserInfoGsonPipeline implements UserInfoPipeline{

    private final Gson gson;

    public UserInfoGsonPipeline() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(new UserInfoMessageGsonTypeAdapterFactory())
                .registerTypeAdapterFactory(new UserInfoGsonTypeAdapterFactory())
                .create();
    }
    @Override
    public String convert(Set<Map.Entry<Long, UserInfo>> userInfos) {
        JsonObject object = new JsonObject();
        for (Map.Entry<Long, UserInfo> userInfo : userInfos) {
            object.add(userInfo.getKey().toString(), gson.toJsonTree(userInfo.getValue(), UserInfo.class));
        }
        return gson.toJson(object);
    }

    @Override
    public Set<Map.Entry<Long, UserInfo>> convert(Reader reader) {
        JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
        return object.entrySet().stream().map(element -> Map.entry(Long.parseLong(element.getKey()), gson.fromJson(element.getValue().getAsJsonObject(), UserInfo.class))).collect(Collectors.toUnmodifiableSet());
    }
}
