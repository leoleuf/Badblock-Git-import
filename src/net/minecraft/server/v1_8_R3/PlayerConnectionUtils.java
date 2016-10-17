package net.minecraft.server.v1_8_R3;

public class PlayerConnectionUtils {

    public static <T extends PacketListener> void ensureMainThread(final Packet<T> packet, final T t0, IAsyncTaskHandler iasynctaskhandler) throws CancelledPacketHandleException {
        if (!iasynctaskhandler.isMainThread()) {
            iasynctaskhandler.postToMainThread(new Runnable() {
                @Override
				public void run() {
                    packet.a(t0);
                }
            });
            throw CancelledPacketHandleException.INSTANCE;
        }
    }
}
