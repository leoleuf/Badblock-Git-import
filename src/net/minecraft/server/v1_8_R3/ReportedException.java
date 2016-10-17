package net.minecraft.server.v1_8_R3;

public class ReportedException extends RuntimeException {

    private final CrashReport a;

    public ReportedException(CrashReport crashreport) {
        this.a = crashreport;
    }

    public CrashReport a() {
        return this.a;
    }

    @Override
	public Throwable getCause() {
        return this.a.b();
    }

    @Override
	public String getMessage() {
        return this.a.a();
    }
}
