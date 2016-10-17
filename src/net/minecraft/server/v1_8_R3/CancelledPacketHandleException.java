package net.minecraft.server.v1_8_R3;

public final class CancelledPacketHandleException extends RuntimeException {

    public static final CancelledPacketHandleException INSTANCE = new CancelledPacketHandleException();

    private CancelledPacketHandleException() {
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
	public synchronized Throwable fillInStackTrace() {
        this.setStackTrace(new StackTraceElement[0]);
        return this;
    }
}
