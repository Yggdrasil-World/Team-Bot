package de.overcraft.util.userinfo;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

public abstract class GsonTypeAdapter<T> extends TypeAdapter<T> {
    protected Gson gson;
    public GsonTypeAdapter(Gson gson) {
        this.gson = gson;
    }
}
