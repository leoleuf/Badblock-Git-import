package fr.badblock.bungeecord.plugins.others.haproxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.logging.Level;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.limits.IPLimit;
import net.md_5.bungee.limits.IPLimit.IPStatus;
import net.md_5.bungee.netty.PipelineUtils;

public class HAProxy extends Plugin {

	@Override
	public void onEnable()
	{
		try {
			Field remoteAddressField = AbstractChannel.class.getDeclaredField("remoteAddress");
			remoteAddressField.setAccessible(true);

			Field serverChild = PipelineUtils.class.getField("SERVER_CHILD");
			serverChild.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(serverChild, serverChild.getModifiers() & ~Modifier.FINAL);

			ChannelInitializer<Channel> bungeeChannelInitializer = PipelineUtils.SERVER_CHILD;

			Method initChannelMethod = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
			initChannelMethod.setAccessible(true);

			serverChild.set(null, new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel channel) throws Exception {
					initChannelMethod.invoke(bungeeChannelInitializer, channel);
					channel.pipeline().addAfter(PipelineUtils.TIMEOUT_HANDLER, "haproxy-decoder", new HAProxyMessageDecoder());
					channel.pipeline().addAfter("haproxy-decoder", "haproxy-handler", new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							if (msg instanceof HAProxyMessage) {
								HAProxyMessage message = (HAProxyMessage) msg;
								remoteAddressField.set(channel, new InetSocketAddress(message.sourceAddress(), message.sourcePort()));
								if (!message.sourceAddress().startsWith("213.32.119.") &&
										!message.sourceAddress().equals("151.80.47.219") &&
										!message.sourceAddress().equals("94.23.153.196") &&
										!message.sourceAddress().equals("149.202.86.54") &&
										!message.sourceAddress().equals("163.172.54.235")
										)
								{	
									if (block(ctx, message.sourceAddress(), message.sourcePort()))
									{
										return;
									}
								}
							} else {
								super.channelRead(ctx, msg);
							}
						}
					});
				}
			});
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, e.getMessage(), e);
			getProxy().stop();
		}

	}

	private boolean block(ChannelHandlerContext ctx, String ip, int port)
	{
		InetSocketAddress localSocket = (InetSocketAddress) ctx.channel().localAddress();
		String localIp = localSocket.getAddress().getHostAddress();
		int localPort = localSocket.getPort();
		IPStatus ipStatus = IPLimit.incrementAndGetStatus(ip);
		System.out.println("IPStatus: " + ipStatus);
		if (ipStatus.equals(IPStatus.HIT))
		{
			IPLimit.block(ip);
			File folder = new File("/home/iplogs/");
			if (!folder.exists())
			{
				folder.mkdirs();
			}
			File file = new File(folder, ip + ".dat");
			String build = "";
			String date = BungeeCord.getInstance().simpleDateFormat.format(new Date());
			if (!file.exists())
			{
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				build = "File created date: " + date + System.lineSeparator()
				+ "Action: Blocked" + System.lineSeparator() + System.lineSeparator();
			}
			try {
				build += date + " CEST " + ip + ":" + port + " > " + localIp + ":" + localPort + " TCP SYN ATTACK:TCP_SYN" + System.lineSeparator();
				Files.write(file.toPath(), build.getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {
				e.printStackTrace();
			}
			ctx.close();
			return true;
		}
		return false;
	}

}
