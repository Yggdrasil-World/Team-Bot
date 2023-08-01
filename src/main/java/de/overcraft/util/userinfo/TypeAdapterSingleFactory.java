package de.overcraft.util.userinfo;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public abstract class TypeAdapterSingleFactory<T> implements TypeAdapterFactory {

    private final Class<T> type;

    public TypeAdapterSingleFactory(Class<T> type) {
        this.type = type;
    }
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> typeToken) {
        if(!this.type.isAssignableFrom(typeToken.getRawType()))
            return null;
        return (TypeAdapter<R>) createAdapter(gson, ((TypeToken<T>) typeToken));
    }

    protected abstract TypeAdapter<T> createAdapter(Gson gson, TypeToken<T> typeToken);
}
