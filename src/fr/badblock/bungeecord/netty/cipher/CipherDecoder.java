package fr.badblock.bungeecord.netty.cipher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.util.List;

import fr.badblock.bungeecord.jni.cipher.BungeeCipher;

@RequiredArgsConstructor
public class CipherDecoder extends MessageToMessageDecoder<ByteBuf>
{

    private final BungeeCipher cipher;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
    {
        out.add( cipher.cipher( ctx, msg ) );
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        cipher.free();
    }
}
