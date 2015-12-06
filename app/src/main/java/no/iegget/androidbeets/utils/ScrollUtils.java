package no.iegget.androidbeets.utils;

/**
 * Created by iver on 06/12/15.
 */
public final class ScrollUtils {

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
