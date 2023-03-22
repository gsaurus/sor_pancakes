/*
 * Decompiled with CFR 0_132.
 */
package lib.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import lib.RandomDataStream;
import lib.Renderable;

public class Tile
implements Renderable {
    private byte[] mx = new byte[64];
    private static boolean showBackground = false;

    public static boolean isShowingBackground() {
        return showBackground;
    }

    public static void setShowBackground(boolean show) {
        showBackground = show;
    }

    public byte getPixel(int x, int y) {
        return this.mx[y * 8 + x];
    }

    public void setPixel(int x, int y, byte val) {
        this.mx[y * 8 + x] = val;
    }

    @Override
    public BufferedImage asImage(Palette pal) {
        BufferedImage res = new BufferedImage(8, 8, 2);
        int background = new Color(0, 0, 0, 0).getRGB();
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 8; ++x) {
                byte index = this.getPixel(x, y);
                if (!showBackground && index == 0) {
                    res.setRGB(x, y, background);
                    continue;
                }
                res.setRGB(x, y, pal.getColor(index));
            }
        }
        return res;
    }

    @Override
    public BufferedImage asShadow(Color color) {
        BufferedImage res = new BufferedImage(8, 8, 2);
        Graphics2D g2d = res.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, 8, 8);
        return res;
    }

    public static Tile read(RandomAccessFile rom, long address) throws IOException {
        Tile tile = new Tile();
        rom.seek(address);
        for (int i = 0; i < 64; i += 2) {
            byte b = rom.readByte();
            int x = i % 8;
            int y = i / 8;
            tile.setPixel(x, y, (byte)(b >>> 4 & 15));
            tile.setPixel(x + 1, y, (byte)(b & 15));
        }
        return tile;
    }

    public static Tile read(RandomDataStream art, int address) throws IOException {
        Tile tile = new Tile();
        art.seek(address);
        for (int i = 0; i < 64; i += 2) {
            byte b = art.read();
            int x = i % 8;
            int y = i / 8;
            tile.setPixel(x, y, (byte)(b >>> 4 & 15));
            tile.setPixel(x + 1, y, (byte)(b & 15));
        }
        return tile;
    }

    private int getPixelValue(int x, int y, BufferedImage img, Palette pal) {
        int value = img.getRGB(x, y);
        for (int i = 15; i >= 0; --i) {
            if (value != pal.getColor(i)) continue;
            return i;
        }
        return 0;
    }

    public void writeArt(RandomAccessFile rom, long address, BufferedImage image, Palette palette) throws IOException {
        /*
        rom.seek(address);
        for (int i = 0; i < 64; i += 2) {
            int p1 = this.getPixelValue(i % 8, i / 8, image, palette);
            int j = i + 1;
            int p2 = this.getPixelValue(j % 8, j / 8, image, palette);
            rom.writeByte(p1 << 4 & 240 | p2 & 15);
        }
        */
    }
    
    
    public static long getSizeInBytes(){
        return 32L;
    }
    

    public void write(RandomAccessFile rom, long address) throws IOException {
        /*
        rom.seek(address);
        for (int i = 0; i < 64; i += 2) {
            byte p1 = this.getPixel(i % 8, i / 8);
            int j = i + 1;
            byte p2 = this.getPixel(j % 8, i / 8);
            rom.writeByte(p1 << 4 & 240 | p2 & 15);
        }
        */
    }
}

