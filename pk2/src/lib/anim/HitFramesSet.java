/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import lib.anim.HitFrame;

class HitFramesSet {
    public ArrayList<HitFrame> frames;

    HitFramesSet(int size) {
        this.frames = new ArrayList(size);
    }

    static HitFramesSet read(RandomAccessFile rom, long address, int count, TreeMap<Long, HitFrame> processed) throws IOException {
        HitFramesSet res = new HitFramesSet(count);
        for (int i = count - 1; i >= 0; --i) {
            long localAddress = address + (long)(i * 4);
            HitFrame frame = processed.get(localAddress);
            if (frame == null) {
                frame = HitFrame.read(rom, localAddress);
                processed.put(localAddress, frame);
            }
            res.frames.add(frame);
        }
        return res;
    }

    void write(RandomAccessFile rom, long address, int count) throws IOException {
        for (int i = 0; i < count && i < this.frames.size(); ++i) {
            this.frames.get(i).write(rom, address + (long)((count - 1 - i) * 4));
        }
    }

    int countFrames(HashSet<HitFrame> processed) {
        int res = 0;
        for (HitFrame f : this.frames) {
            if (processed.contains(f)) continue;
            processed.add(f);
            ++res;
        }
        return res;
    }

    boolean process(HashSet<HitFrame> processed) {
        for (HitFrame f : this.frames) {
            if (processed.contains(f)) {
                return false;
            }
            processed.add(f);
        }
        return true;
    }
}

