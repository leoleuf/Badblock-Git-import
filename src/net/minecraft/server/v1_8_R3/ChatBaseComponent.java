package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public abstract class ChatBaseComponent implements IChatBaseComponent {

    protected List<IChatBaseComponent> a = Lists.newArrayList();
    private ChatModifier b;

    public ChatBaseComponent() {}

    @Override
	public IChatBaseComponent addSibling(IChatBaseComponent ichatbasecomponent) {
        ichatbasecomponent.getChatModifier().setChatModifier(this.getChatModifier());
        this.a.add(ichatbasecomponent);
        return this;
    }

    @Override
	public List<IChatBaseComponent> a() {
        return this.a;
    }

    @Override
	public IChatBaseComponent a(String s) {
        return this.addSibling(new ChatComponentText(s));
    }

    @Override
	public IChatBaseComponent setChatModifier(ChatModifier chatmodifier) {
        this.b = chatmodifier;
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

            ichatbasecomponent.getChatModifier().setChatModifier(this.getChatModifier());
        }

        return this;
    }

    @Override
	public ChatModifier getChatModifier() {
        if (this.b == null) {
            this.b = new ChatModifier();
            Iterator iterator = this.a.iterator();

            while (iterator.hasNext()) {
                IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

                ichatbasecomponent.getChatModifier().setChatModifier(this.b);
            }
        }

        return this.b;
    }

    @Override
	public Iterator<IChatBaseComponent> iterator() {
        return Iterators.concat((Iterator<ChatBaseComponent>) Iterators.forArray(new ChatBaseComponent[] { this}), (Iterator<IChatBaseComponent>)  a(this.a));
    }

    @Override
	public final String c() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.iterator();

        while (iterator.hasNext()) {
            IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

            stringbuilder.append(ichatbasecomponent.getText());
        }

        return stringbuilder.toString();
    }

    public static Iterator<IChatBaseComponent> a(Iterable<IChatBaseComponent> iterable) {
        Iterator iterator = Iterators.concat(Iterators.transform(iterable.iterator(), new Function() {
            public Iterator<IChatBaseComponent> a(IChatBaseComponent ichatbasecomponent) {
                return ichatbasecomponent.iterator();
            }

            @Override
			public Object apply(Object object) {
                return this.a((IChatBaseComponent) object);
            }
        }));

        iterator = Iterators.transform(iterator, new Function() {
            public IChatBaseComponent a(IChatBaseComponent ichatbasecomponent) {
                IChatBaseComponent ichatbasecomponent1 = ichatbasecomponent.f();

                ichatbasecomponent1.setChatModifier(ichatbasecomponent1.getChatModifier().n());
                return ichatbasecomponent1;
            }

            @Override
			public Object apply(Object object) {
                return this.a((IChatBaseComponent) object);
            }
        });
        return iterator;
    }

    @Override
	public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChatBaseComponent)) {
            return false;
        } else {
            ChatBaseComponent chatbasecomponent = (ChatBaseComponent) object;

            return this.a.equals(chatbasecomponent.a) && this.getChatModifier().equals(chatbasecomponent.getChatModifier());
        }
    }

    @Override
	public int hashCode() {
        return 31 * this.getChatModifier().hashCode() + this.a.hashCode(); // CraftBukkit - fix null pointer
    }

    @Override
	public String toString() {
        return "BaseComponent{style=" + this.b + ", siblings=" + this.a + '}';
    }
}
