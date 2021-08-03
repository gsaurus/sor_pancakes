/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.io.IOException;
import java.io.RandomAccessFile;

public class HitFrame {
    public int x;
    public int y;
    public int sound;
    public int damage;
    public boolean knockDown;
    private boolean enabled;

    public void setEnabled(boolean enabledFlag) {
        this.enabled = enabledFlag;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public static HitFrame read(RandomAccessFile rom, long address) throws IOException {
        HitFrame frame = new HitFrame();
        rom.seek(address);
        frame.x = rom.readByte();
        frame.y = rom.readByte();
        frame.damage = rom.readUnsignedByte();
        frame.knockDown = (frame.damage >> 7 & 1) == 1;
        frame.damage &= 127;
        frame.sound = rom.readUnsignedByte();
        frame.enabled = frame.damage > 0;
        return frame;
    }

    public void write(RandomAccessFile rom, long address) throws IOException {
        rom.seek(address);
        HitFrame frame = this.enabled ? this : new HitFrame();
        rom.writeByte(frame.x);
        rom.writeByte(frame.y);
        int damageByte = frame.damage;
        if (frame.knockDown) {
            damageByte += 128;
        }
        rom.writeByte(damageByte);
        rom.writeByte(frame.sound);
    }
}

