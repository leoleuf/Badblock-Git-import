package net.minecraft.server.v1_8_R3;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class OpListEntry extends JsonListEntry<GameProfile> {

    private final int a;
    private final boolean b;

    public OpListEntry(GameProfile gameprofile, int i, boolean flag) {
        super(gameprofile);
        this.a = i;
        this.b = flag;
    }

    public OpListEntry(JsonObject jsonobject) {
        super(b(jsonobject), jsonobject);
        this.a = jsonobject.has("level") ? jsonobject.get("level").getAsInt() : 0;
        this.b = jsonobject.has("bypassesPlayerLimit") && jsonobject.get("bypassesPlayerLimit").getAsBoolean();
    }

    public int a() {
        return this.a;
    }

    public boolean b() {
        return this.b;
    }

    @Override
	protected void a(JsonObject jsonobject) {
        if (this.getKey() != null) {
            jsonobject.addProperty("uuid", this.getKey().getId() == null ? "" : this.getKey().getId().toString());
            jsonobject.addProperty("name", this.getKey().getName());
            super.a(jsonobject);
            jsonobject.addProperty("level", Integer.valueOf(this.a));
            jsonobject.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.b));
        }
    }

    private static GameProfile b(JsonObject jsonobject) {
        if (jsonobject.has("uuid") && jsonobject.has("name")) {
            String s = jsonobject.get("uuid").getAsString();

            UUID uuid;

            try {
                uuid = UUID.fromString(s);
            } catch (Throwable throwable) {
                return null;
            }

            return new GameProfile(uuid, jsonobject.get("name").getAsString());
        } else {
            return null;
        }
    }
}
