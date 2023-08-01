package de.overcraft.util.userinfo;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class UserInfoGsonTypeAdapterFactory extends TypeAdapterSingleFactory<UserInfo> {


    public UserInfoGsonTypeAdapterFactory() {
        super(UserInfo.class);
    }

    @Override
    protected TypeAdapter<UserInfo> createAdapter(Gson gson, TypeToken<UserInfo> typeToken) {
        return new UserInfoGsonTypeAdapter(gson, gson.getDelegateAdapter(this, TypeToken.get(UserMessageInfo.class)));
    }

    public static class UserInfoGsonTypeAdapter extends GsonTypeAdapter<UserInfo> {
        private static final String MessageInfoMetaName = "messageInfoMeta";
        private TypeAdapter<UserMessageInfo> messageInfoDelegate;

        public UserInfoGsonTypeAdapter(Gson gson, TypeAdapter<UserMessageInfo> messageInfoDelegate) {
            super(gson);
            this.messageInfoDelegate = messageInfoDelegate;
        }

        @Override
        public void write(JsonWriter out, UserInfo value) throws IOException {
            out.beginObject();
            out.name(MessageInfoMetaName);
            messageInfoDelegate.write(out, value.messageInfo());
            out.endObject();
        }

        @Override
        public UserInfo read(JsonReader in) throws IOException {
            in.beginObject();
            while (in.hasNext()) {
                if(in.nextName().equals(MessageInfoMetaName)) {
                    return UserInfoFactory.CreateUserInfo(messageInfoDelegate.read(in));
                }
            }
            in.endObject();
            throw new IOException("Invalid json");
        }
    }
}
