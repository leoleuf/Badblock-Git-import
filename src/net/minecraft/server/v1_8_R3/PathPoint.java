package net.minecraft.server.v1_8_R3;

public class PathPoint {

    public final int a;
    public final int b;
    public final int c;
    private final int j;
    int d = -1;
    float e;
    float f;
    float g;
    PathPoint h;
    public boolean i;

    public PathPoint(int i, int j, int k) {
        this.a = i;
        this.b = j;
        this.c = k;
        this.j = a(i, j, k);
    }

    public static int a(int i, int j, int k) {
        return j & 255 | (i & 32767) << 8 | (k & 32767) << 24 | (i < 0 ? Integer.MIN_VALUE : 0) | (k < 0 ? '\u8000' : 0);
    }

    public float a(PathPoint pathpoint) {
        float f = pathpoint.a - this.a;
        float f1 = pathpoint.b - this.b;
        float f2 = pathpoint.c - this.c;

        return MathHelper.c(f * f + f1 * f1 + f2 * f2);
    }

    public float b(PathPoint pathpoint) {
        float f = pathpoint.a - this.a;
        float f1 = pathpoint.b - this.b;
        float f2 = pathpoint.c - this.c;

        return f * f + f1 * f1 + f2 * f2;
    }

    @Override
	public boolean equals(Object object) {
        if (!(object instanceof PathPoint)) {
            return false;
        } else {
            PathPoint pathpoint = (PathPoint) object;

            return this.j == pathpoint.j && this.a == pathpoint.a && this.b == pathpoint.b && this.c == pathpoint.c;
        }
    }

    @Override
	public int hashCode() {
        return this.j;
    }

    public boolean a() {
        return this.d >= 0;
    }

    @Override
	public String toString() {
        return this.a + ", " + this.b + ", " + this.c;
    }
}
