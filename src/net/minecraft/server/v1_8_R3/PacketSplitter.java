package net.minecraft.server.v1_8_R3;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

public class PacketSplitter extends ByteToMessageDecoder {

    public PacketSplitter() {}

    @Override
	protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        bytebuf.markReaderIndex();
        byte[] abyte = new byte[3];

        for (int i = 0; i < abyte.length; ++i) {
            if (!bytebuf.isReadable()) {
                bytebuf.resetReaderIndex();
                return;
            }

            abyte[i] = bytebuf.readByte();
            if (abyte[i] >= 0) {
                PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.wrappedBuffer(abyte));

                try {
                    int j = packetdataserializer.e();

                    if (bytebuf.readableBytes() >= j) {
                        list.add(bytebuf.readBytes(j));
                        return;
                    }

                    bytebuf.resetReaderIndex();
                } finally {
                    packetdataserializer.release();
                }

                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
