package fr.badblock.api.utils;

import java.lang.reflect.ParameterizedType;

public abstract class Callback<T>
{

    public abstract void done(T result, Throwable error);

    @SuppressWarnings("unchecked")
    public Class<T> getGenericPacketClass()
    {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
