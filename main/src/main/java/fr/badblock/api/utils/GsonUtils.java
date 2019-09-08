package fr.badblock.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class GsonUtils
{

    private static Gson	gson		= new Gson();
    private static Gson	prettyGson	= new GsonBuilder().setPrettyPrinting().create();

    public static JsonElement toJsonElement(String string)
    {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(string);
        return element;
    }

}

