/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import lib.anim.WeaponFrame;

class WeaponFramesSet {
    public ArrayList<WeaponFrame> frames;

    WeaponFramesSet(int size) {
        this.frames = new ArrayList(size);
    }

    static WeaponFramesSet read(RandomAccessFile rom, long address, int count, TreeMap<Long, WeaponFrame> processed) throws IOException {
        WeaponFramesSet res = new WeaponFramesSet(count);
        for (int i = count - 1; i >= 0; --i) {
            long localAddress = address + (long)(i * 4);
            WeaponFrame frame = processed.get(localAddress);
            if (frame == null) {
                frame = WeaponFrame.read(rom, localAddress);
                processed.put(localAddress, frame);
            }
            res.frames.add(frame);
        }
        return res;
    }

    void write(RandomAccessFile rom, long address, int count) throws IOException {
        for (int i = 0; i < count; ++i) {
            this.frames.get(i).write(rom, address + (long)((count - 1 - i) * 4));
        }
    }

    int countFrames(HashSet<WeaponFrame> processed) {
        int res = 0;
        for (WeaponFrame f : this.frames) {
            if (processed.contains(f)) continue;
            processed.add(f);
            ++res;
        }
        return res;
    }

    boolean process(HashSet<WeaponFrame> processed) {
        for (WeaponFrame f : this.frames) {
            if (processed.contains(f)) {
                return false;
            }
            processed.add(f);
        }
        return true;
    }
}

