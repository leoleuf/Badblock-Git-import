package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class AchievementSet extends ForwardingSet<String> implements IJsonStatistic {

    private final Set<String> a = Sets.newHashSet();

    public AchievementSet() {}

    @Override
	public void a(JsonElement jsonelement) {
        if (jsonelement.isJsonArray()) {
            Iterator iterator = jsonelement.getAsJsonArray().iterator();

            while (iterator.hasNext()) {
                JsonElement jsonelement1 = (JsonElement) iterator.next();

                this.add(jsonelement1.getAsString());
            }
        }

    }

    @Override
	public JsonElement a() {
        JsonArray jsonarray = new JsonArray();
        Iterator iterator = this.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            jsonarray.add(new JsonPrimitive(s));
        }

        return jsonarray;
    }

    @Override
	protected Set<String> delegate() {
        return this.a;
    }
}
