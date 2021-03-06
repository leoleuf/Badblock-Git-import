package net.minecraft.server.v1_8_R3;

import java.util.Iterator;

public class ChatComponentText extends ChatBaseComponent {

    private final String b;

    public ChatComponentText(String s) {
        this.b = s;
    }

    public String g() {
        return this.b;
    }

    @Override
	public String getText() {
        return this.b;
    }

    public ChatComponentText h() {
        ChatComponentText chatcomponenttext = new ChatComponentText(this.b);

        chatcomponenttext.setChatModifier(this.getChatModifier().clone());
        Iterator iterator = this.a().iterator();

        while (iterator.hasNext()) {
            IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

            chatcomponenttext.addSibling(ichatbasecomponent.f());
        }

        return chatcomponenttext;
    }

    @Override
	public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChatComponentText)) {
            return false;
        } else {
            ChatComponentText chatcomponenttext = (ChatComponentText) object;

            return this.b.equals(chatcomponenttext.g()) && super.equals(object);
        }
    }

    @Override
	public String toString() {
        return "TextComponent{text=\'" + this.b + '\'' + ", siblings=" + this.a + ", style=" + this.getChatModifier() + '}';
    }

    @Override
	public IChatBaseComponent f() {
        return this.h();
    }
}
