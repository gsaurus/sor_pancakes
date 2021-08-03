/*
 * Decompiled with CFR 0_132.
 */
package lib;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.map.Palette;

public interface Renderable {
    public BufferedImage asImage(Palette var1);

    public BufferedImage asShadow(Color var1);
}

