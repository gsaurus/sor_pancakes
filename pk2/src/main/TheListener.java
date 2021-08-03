/*
 * Decompiled with CFR 0_132.
 */
package main;

import main.Mode;

public interface TheListener {
    public void hitPositionChanged(int var1, int var2);

    public void artChanged();

    public void modeChanged(Mode var1);

    public void spriteDragged(int var1, int var2);

    public void weaponPositionChanged(int var1, int var2);
}

