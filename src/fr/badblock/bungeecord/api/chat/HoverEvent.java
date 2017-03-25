package fr.badblock.bungeecord.api.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.md_5.bungee.api.chat.Action;
import net.md_5.bungee.api.chat.BaseComponent;

@Getter
@ToString
@RequiredArgsConstructor
final public class HoverEvent
{

    private final Action action;
    private final BaseComponent[] value;

    public enum Action
    {

        SHOW_TEXT,
        SHOW_ACHIEVEMENT,
        SHOW_ITEM,
        SHOW_ENTITY
    }
}
