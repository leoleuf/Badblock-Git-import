package net.minecraft.server.v1_8_R3;

public class GenLayerTopSoil extends GenLayer {

    public GenLayerTopSoil(long i, GenLayer genlayer) {
        super(i);
        this.a = genlayer;
    }

    @Override
	public int[] a(int i, int j, int k, int l) {
        int i1 = i - 1;
        int j1 = j - 1;
        int k1 = k + 2;
        int l1 = l + 2;
        int[] aint = this.a.a(i1, j1, k1, l1);
        int[] aint1 = IntCache.a(k * l);

        for (int i2 = 0; i2 < l; ++i2) {
            for (int j2 = 0; j2 < k; ++j2) {
                int k2 = aint[j2 + 1 + (i2 + 1) * k1];

                this.a((long) (j2 + i), (long) (i2 + j));
                if (k2 == 0) {
                    aint1[j2 + i2 * k] = 0;
                } else {
                    int l2 = this.a(6);
                    byte b0;

                    if (l2 == 0) {
                        b0 = 4;
                    } else if (l2 <= 1) {
                        b0 = 3;
                    } else {
                        b0 = 1;
                    }

                    aint1[j2 + i2 * k] = b0;
                }
            }
        }

        return aint1;
    }
}
