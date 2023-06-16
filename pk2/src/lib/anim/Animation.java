/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.awt.Color;
import java.awt.Point;
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
    private ArrayList<BufferedImage> bufferedFrames;
    private ArrayList<Point> bufferedPivots;
    
    public ArrayList<Integer> bufferedFrameIndexes;
    
    private boolean wasArtModified;
    private ArrayList<Boolean> spritesModified;

    public boolean isCompressed() {
        return this.type == 1;
    }

    public Animation(int numFrames, int type) {
        this.type = type;
        this.frames = new ArrayList(numFrames);
        this.spritesModified = new ArrayList<Boolean>(numFrames);
        bufferedFrameIndexes = new ArrayList<Integer>(numFrames);
        this.numFrames = numFrames;
    }

    public AnimFrame getFrame(int index) {
        return this.frames.get(index);
    }

    public int getNumFrames() {
        return this.numFrames;
    }

    public void setNumFrames(int num) {
        resize(num);
    }

    public boolean wasArtModified() {
        return this.wasArtModified;
    }

    public BufferedImage getImage(int index) {
        if (this.bufferedFrames != null && index < this.bufferedFrames.size()) {
            return this.bufferedFrames.get(index);
        }
        return null;
    }
    
    public Point getPivot(int index)
    {
        if (this.bufferedPivots != null && index < this.bufferedPivots.size()) {
            return this.bufferedPivots.get(index);
        }
        return null;
    }

    public void setImage(int index, BufferedImage img) {
        if (index < this.frames.size()) {
            if (this.bufferedFrames == null) {
                this.bufferedFrames = new ArrayList<BufferedImage>();
            }
            while (bufferedFrames.size() <= index)
                bufferedFrames.add(null);
            this.bufferedFrames.set(index, img);
            this.wasArtModified = true;
        }
    }
    
    public void resetPivot(int index)
    {
        setPivot(index, new Point());
    }
    
    public void setPivot(int index, Point point)
    {
        if (index < this.frames.size()) {
            if (this.bufferedPivots == null) {
                this.bufferedPivots = new ArrayList<Point>();
            }
            if (index < bufferedPivots.size())
                bufferedPivots.set(index, new Point(point));
            else
            {
            while (bufferedPivots.size() <= index)
                bufferedPivots.add(new Point(point));
            }
        }
    }
    
    public void addPivot(int index, Point deltaPos) {
        if (index < this.frames.size()) {
            if (this.bufferedPivots == null) {
                this.bufferedPivots = new ArrayList<Point>();
            }
            while (bufferedPivots.size() <= index)
                bufferedPivots.add(new Point());
            Point pivot = bufferedPivots.get(index);
            pivot.x += deltaPos.x;
            pivot.y += deltaPos.y;
            bufferedPivots.set(index, pivot);
        }
    }

    public BufferedImage getShadow(int index) {
        return null;
    }

    public boolean isBuffered() {
        return this.bufferedFrames != null && !this.bufferedFrames.isEmpty();
    }

    public void clearBufferedData() {
        this.bufferedFrames = null;
    }

    public void bufferImage(int frameId, Rom rom, Palette palette, Color shadowColor) throws IOException {
        /*
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
        */
    }

    public void buffer(Rom rom, Palette palette, Color shadowColor) throws IOException {
        /*
        this.bufferedFrames = new TreeMap();
        this.bufferedShadows = new ArrayList(this.numFrames);
        for (int i = 0; i < this.numFrames; ++i) {
            AnimFrame frame = this.getFrame(i);
            Sprite sprite = frame.type == 1 ? rom.readCompressedSprite(frame.mapAddress, frame.artAddress) : rom.readSprite(frame.mapAddress, frame.artAddress);
            this.bufferedFrames.put(frame.mapAddress, sprite.asImage(palette));
            this.bufferedShadows.add(sprite.asShadow(shadowColor));
        }
        */
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
        while (spritesModified.size() <= frame)
            spritesModified.add(true);
        this.spritesModified.set(frame, b);
        if (b) {
            this.wasArtModified = true;
        }
    }

    public boolean wasSpriteModified(int frame) {
        while (spritesModified.size() <= frame)
            spritesModified.add(false);
        return this.spritesModified.get(frame);
    }

    public void spritesNotModified() {
        this.wasArtModified = false;
        this.spritesModified = new ArrayList<Boolean>(this.frames.size());
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
            this.frames.add(new AnimFrame(f1));
            spritesModified.add(true);
        }
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

