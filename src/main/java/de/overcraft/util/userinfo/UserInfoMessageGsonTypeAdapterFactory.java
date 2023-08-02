package de.overcraft.util.userinfo;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UserInfoMessageGsonTypeAdapterFactory extends TypeAdapterSingleFactory<UserMessageInfo> {


    public UserInfoMessageGsonTypeAdapterFactory() {
        super(UserMessageInfo.class);
    }

    @Override
    protected TypeAdapter<UserMessageInfo> createAdapter(Gson gson, TypeToken<UserMessageInfo> typeToken) {
        return new UserMessageInfoGsonTypeAdapter();
    }

    public static class UserMessageInfoGsonTypeAdapter extends TypeAdapter<UserMessageInfo> {
        private static final String MessageId = "lastMessageId";
        private static final String ChannelId = "channelId";
        @Override
        public void write(JsonWriter out, UserMessageInfo value) throws IOException {
            out.beginObject();
            out.name(MessageId);
            out.value(value.lastMessageId());
            out.name(ChannelId);
            out.value(value.channelId());
            out.endObject();
        }

        @Override
        public UserMessageInfo read(JsonReader in) throws IOException {
            long messageId = 0;
            long channelId = 0;
            in.beginObject();
            while (in.hasNext()) {
                if (in.nextName().equals(MessageId))
                    messageId = in.nextLong();
                if (in.nextName().equals(ChannelId))
                    channelId = in.nextLong();
            }
            in.endObject();
            if(messageId == 0 || channelId == 0)
                return null;
            return UserInfoFactory.Metas.CreateUserMessageInfo(messageId, channelId);
        }
    }
}
