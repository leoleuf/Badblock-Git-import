package fr.badblock.bungeecord.ladder.mojang.profiles;

public interface ProfileRepository {
    public Profile[] findProfilesByNames(String... names);
}
