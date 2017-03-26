package fr.badblock.bungeecord.plugins.ladder.mojang.profiles;

public interface ProfileRepository {
    public Profile[] findProfilesByNames(String... names);
}
