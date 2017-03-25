package fr.badblock.bungeecord.plugins.others.logs.filters;

import fr.badblock.bungeecord.netty.ChannelWrapper;
import io.netty.channel.ChannelHandlerContext;

public class CustomChannelWrapper extends ChannelWrapper {

	public CustomChannelWrapper(ChannelHandlerContext ctx) {
		super(ctx);
	}

	@Override
	public boolean isClosed() {
		return false;
	}
	
	@Override
	public void write(Object packet) {
		
	}

}
