/*
 * Decompiled with CFR 0_132.
 */
package lib.map;

import java.awt.Color;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Palette {
    private int[] colors = new int[16];

    public static int correct(int value) {
        switch (value) {
            case 0: {
                return 0;
            }
            case 17: {
                return 0;
            }
            case 34: {
                return 32;
            }
            case 51: {
                return 32;
            }
            case 68: {
                return 74;
            }
            case 85: {
                return 74;
            }
            case 102: {
                return 106;
            }
            case 119: {
                return 106;
            }
            case 136: {
                return 148;
            }
            case 153: {
                return 148;
            }
            case 170: {
                return 189;
            }
            case 187: {
                return 189;
            }
            case 204: {
                return 222;
            }
            case 221: {
                return 222;
            }
            case 238: {
                return 255;
            }
            case 255: {
                return 255;
            }
        }
        return 0;
    }

    private static int hexToVal(int val) {
        return val * 17;
    }

    private static int convertToRgb(short gensColor) {
        int r = gensColor & 15;
        int g = gensColor >>> 4 & 15;
        int b = gensColor >>> 8 & 15;
        r = Palette.correct(Palette.hexToVal(r));
        g = Palette.correct(Palette.hexToVal(g));
        b = Palette.correct(Palette.hexToVal(b));
        return new Color(r, g, b).getRGB();
    }

    public void setColor(int index, int color) {
        this.colors[index] = color;
    }

    public int getColor(int index) {
        return this.colors[index];
    }

    public static Palette read(RandomAccessFile rom, long address) throws IOException {
        Palette pal = new Palette();
        rom.seek(address);
        for (int i = 0; i < 16; ++i) {
            short gensColor = rom.readShort();
            pal.setColor(i, Palette.convertToRgb(gensColor));
        }
        return pal;
    }

    public void write(RandomAccessFile rom, long address) throws Exception {
        rom.seek(address);
        for (int i = 0; i < 16; ++i) {
            rom.writeShort(this.colors[i]);
        }
    }

    public static boolean isPalette(RandomAccessFile rom, long address) {
        if (address < 512L) {
            return false;
        }
        try {
            rom.seek(address);
            for (int i = 0; i < 16; ++i) {
                if ((rom.readShort() >>> 12 & 15) <= 0) continue;
                return false;
            }
        }
        catch (IOException ex) {
            return false;
        }
        return true;
    }
}

