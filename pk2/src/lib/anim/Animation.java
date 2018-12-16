/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.TreeMap;
import lib.Rom;
import lib.anim.AnimFrame;
import lib.map.Palette;
import lib.map.Sprite;

public class Animation {
    private ArrayList<AnimFrame> frames;
    private int numFrames;
    private int type;
    private TreeMap<Long, BufferedImage> bufferedFrames;
    private ArrayList<BufferedImage> bufferedShadows;
    private boolean wasArtModified;
    private boolean[] spritesModified;

    public boolean isCompressed() {
        return this.type == 1;
    }

    public Animation(int numFrames, int type) {
        this.type = type;
        this.frames = new ArrayList(numFrames);
        this.spritesModified = new boolean[numFrames];
        this.numFrames = numFrames;
    }

    public AnimFrame getFrame(int index) {
        return this.frames.get(index);
    }

    public int getNumFrames() {
        return this.numFrames;
    }

    public int getMaxNumFrames() {
        return this.frames.size();
    }

    public void setNumFrames(int num) {
        if (this.numFrames < 0 || num > this.frames.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.numFrames = num;
    }

    public boolean wasArtModified() {
        return this.wasArtModified;
    }

    public BufferedImage getImage(int index) {
        if (this.bufferedFrames != null && index < this.frames.size()) {
            long address = this.frames.get((int)index).mapAddress;
            return this.bufferedFrames.get(address);
        }
        return null;
    }

    public void setImage(int index, BufferedImage img) {
        if (index < this.frames.size()) {
            long address = this.frames.get((int)index).mapAddress;
            if (this.bufferedFrames == null) {
                this.bufferedFrames = new TreeMap();
                this.bufferedShadows = new ArrayList(this.numFrames);
            }
            this.bufferedFrames.put(address, img);
            this.wasArtModified = true;
        }
    }

    public BufferedImage getShadow(int index) {
        if (this.bufferedShadows != null && index < this.bufferedShadows.size()) {
            return this.bufferedShadows.get(index);
        }
        return null;
    }

    public boolean isBuffered() {
        return this.bufferedFrames != null && !this.bufferedFrames.isEmpty();
    }

    public void clearBufferedData() {
        this.bufferedFrames = null;
        this.bufferedShadows = null;
    }

    public void bufferImage(int frameId, Rom rom, Palette palette, Color shadowColor) throws IOException {
        if (this.bufferedFrames == null) {
            this.bufferedFrames = new TreeMap();
            this.bufferedShadows = new ArrayList(this.numFrames);
        }
        AnimFrame frame = this.getFrame(frameId);
        Sprite sprite = frame.type == 1 ? rom.readCompressedSprite(frame.mapAddress, frame.artAddress) : rom.readSprite(frame.mapAddress, frame.artAddress);
        while (this.bufferedShadows.size() <= frameId) {
            this.bufferedShadows.add(null);
        }
        this.bufferedFrames.put(frame.mapAddress, sprite.asImage(palette));
        this.bufferedShadows.set(frameId, sprite.asShadow(shadowColor));
    }

    public void buffer(Rom rom, Palette palette, Color shadowColor) throws IOException {
        this.bufferedFrames = new TreeMap();
        this.bufferedShadows = new ArrayList(this.numFrames);
        for (int i = 0; i < this.numFrames; ++i) {
            AnimFrame frame = this.getFrame(i);
            Sprite sprite = frame.type == 1 ? rom.readCompressedSprite(frame.mapAddress, frame.artAddress) : rom.readSprite(frame.mapAddress, frame.artAddress);
            this.bufferedFrames.put(frame.mapAddress, sprite.asImage(palette));
            this.bufferedShadows.add(sprite.asShadow(shadowColor));
        }
    }

    public static Animation read(RandomAccessFile rom, long address, int type) throws IOException {
        rom.seek(address);
        int size = rom.readUnsignedShort();
        Animation anim = new Animation(size, type);
        int accum = 0;
        for (int i = 0; i < size; ++i) {
            AnimFrame frame = AnimFrame.read(rom, address + 2L + (long)accum, type);
            accum += AnimFrame.getTypeDependentSize(frame.type);
            anim.frames.add(frame);
        }
        return anim;
    }

    public void write(RandomAccessFile rom, long address) throws IOException {
        rom.seek(address);
        rom.writeShort(this.numFrames);
        int accum = 0;
        for (int i = 0; i < this.numFrames; ++i) {
            AnimFrame frame = this.getFrame(i);
            frame.write(rom, address + 2L + (long)accum);
            accum += AnimFrame.getTypeDependentSize(frame.type);
        }
    }

    public int write(RandomAccessFile rom, long address, int type) throws IOException {
        if (type == this.type || type == 3 && this.type == 0 || type == 0 && this.type == 3) {
            this.write(rom, address);
            return 2 + this.getSizeInBytes();
        }
        rom.seek(address);
        rom.writeShort(this.numFrames);
        int frameSize = AnimFrame.getTypeDependentSize(type);
        for (int i = 0; i < this.numFrames; ++i) {
            AnimFrame frame = this.getFrame(i);
            frame.write(rom, address + 2L + (long)(i * frameSize), type);
        }
        return 2 + this.numFrames * frameSize;
    }

    public void setSpritesModified(int frame, boolean b) {
        this.spritesModified[frame] = b;
        if (b) {
            this.wasArtModified = true;
        }
    }

    public boolean wasSpriteModified(int frame) {
        return this.spritesModified[frame];
    }

    public void spritesNotModified() {
        this.wasArtModified = false;
        this.spritesModified = new boolean[this.frames.size()];
    }

    public void setAnimType(int newType) {
        this.type = newType;
        for (AnimFrame frame : this.frames) {
            if (frame.type == 3) continue;
            frame.type = newType;
        }
    }

    public void resize(Integer size) {
        this.numFrames = size;
        int ondSize = this.frames.size();
        int dif = size - ondSize;
        for (int i = 0; i < dif; ++i) {
            AnimFrame f1 = this.frames.get(i % ondSize);
            this.frames.add(f1);
        }
        this.clearBufferedData();
    }

    public void addFrame(AnimFrame frame) {
        this.frames.add(frame);
    }

    public int getSizeInBytes(int type) {
        int frameSize = AnimFrame.getTypeDependentSize(type);
        return this.numFrames * frameSize + 2;
    }

    public int getSizeInBytes() {
        int res = 0;
        for (AnimFrame f : this.frames) {
            res += AnimFrame.getTypeDependentSize(f.type);
        }
        return res;
    }
}

