/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import lib.Rom;
import org.json.JSONArray;
import org.json.JSONObject;

public class Character {
    public static final int FIRST_HIT_ANIM = 24;
    private ArrayList<Animation> animations;
    private ArrayList<HitFramesSet> animHits;
    private ArrayList<WeaponFramesSet> animWeapons;
    private boolean modified;
    private boolean spritesModified;
    static boolean DEBUG_RESIZERS = false;
    static boolean DEBUG_RESIZERS_FIX = false;

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void setSpritesModified(boolean modified) {
        this.spritesModified = modified;
    }

    public boolean wasModified() {
        return this.modified;
    }

    public Character(int numAnimations) {
        this.animations = new ArrayList(numAnimations);
        this.animHits = numAnimations > FIRST_HIT_ANIM ? new ArrayList(numAnimations - FIRST_HIT_ANIM) : new ArrayList(0);
        this.animWeapons = new ArrayList(numAnimations);
    }

    public Animation getAnimation(int index) {
        return this.animations.get(index);
    }

    public AnimFrame getAnimFrame(int animId, int frameId) {
        return this.animations.get(animId).getFrame(frameId);
    }

    public HitFrame getHitFrame(int animId, int frameId) {
        if (animId < FIRST_HIT_ANIM || animId - FIRST_HIT_ANIM >= this.animHits.size()) {
            return null;
        }
        HitFramesSet hits = this.animHits.get(animId - FIRST_HIT_ANIM);
        if (hits == null) {
            return null;
        }
        ArrayList<HitFrame> frames = hits.frames;
        if (frames == null || frames.size() <= frameId) {
            return null;
        }
        return frames.get(frameId);
    }

    public WeaponFrame getWeaponFrame(int animId, int frameId) {
        if (animId >= this.animWeapons.size()) {
            return null;
        }
        WeaponFramesSet weapon = this.animWeapons.get(animId);
        if (weapon == null) {
            return null;
        }
        ArrayList<WeaponFrame> frames = weapon.frames;
        if (frames == null || frameId >= frames.size()) {
            return null;
        }
        return frames.get(frameId);
    }

    public int getNumAnimations() {
        return this.animations.size();
    }

    public static Character read(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, int count, boolean globalCol, boolean globalWeap, int numPlayableChars) throws IOException {
        return Character.read(rom, animListAddress, hitsListAddress, weaponsListAddress, id, count, -1, globalCol, globalWeap, numPlayableChars);
    }

    private static void reorderFiles() throws IOException {
        for (int i = 1; i < 23; ++i) {
            String s;
            Scanner sc = new Scanner(new File("" + i + ".txt"));
            ArrayList<Integer> anims = new ArrayList<Integer>();
            ArrayList<Integer> hits = new ArrayList<Integer>();
            ArrayList<Integer> weapons = new ArrayList<Integer>();
            while (sc.hasNextLine() && !(s = sc.nextLine()).isEmpty()) {
                anims.add(Integer.parseInt(s));
            }
            while (sc.hasNextLine() && !(s = sc.nextLine()).isEmpty()) {
                if (s.equalsIgnoreCase("#")) {
                    s = "0";
                }
                hits.add(Integer.parseInt(s));
            }
            while (sc.hasNextLine() && !(s = sc.nextLine()).isEmpty()) {
                if (s.equalsIgnoreCase("#")) {
                    s = "0";
                }
                weapons.add(Integer.parseInt(s));
            }
            sc.close();
            PrintWriter dos = new PrintWriter("" + i + " - .txt");
            for (int j = 0; j < anims.size(); ++j) {
                int first = (Integer)anims.get(j);
                int second = (Integer)weapons.get(j);
                if (first < 0) {
                    second = second < 0 ? 1 : 0;
                }
                dos.print("" + first + "\t" + second);
                if (j >= FIRST_HIT_ANIM) {
                    int third = (Integer)hits.get(j - FIRST_HIT_ANIM);
                    if (first < 0) {
                        third = third < 0 ? 1 : 0;
                    }
                    dos.print("\t" + third);
                }
                if (j >= anims.size() - 1) continue;
                dos.println();
            }
            dos.close();
        }
    }

    public static Character read(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int charId, int count, int animsType, boolean globalCol, boolean globalWeap, int numPlayableChars) throws IOException {
        short offset;
        Character c = new Character(count);
        if (DEBUG_RESIZERS_FIX) {
            Character.reorderFiles();
        }
        PrintWriter dos = null;
        TreeMap<Long, Integer> animIds = null;
        if (DEBUG_RESIZERS) {
            dos = new PrintWriter("" + (charId + 1) + ".txt");
        }
        if (DEBUG_RESIZERS) {
            animIds = new TreeMap<Long, Integer>();
        }
        rom.seek(animListAddress + (long)(charId * 4));
        long address = rom.readInt();
        TreeMap<Long, Animation> animAddresses = new TreeMap<Long, Animation>();
        for (int i = 0; i < count; ++i) {
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            Animation anim = (Animation)animAddresses.get(localAddress += (long)offset);
            if (anim == null) {
                anim = Animation.read(rom, localAddress, animsType);
                animAddresses.put(localAddress, anim);
                if (DEBUG_RESIZERS) {
                    animIds.put(localAddress, i);
                }
                if (DEBUG_RESIZERS) {
                    dos.println(anim.getNumFrames());
                }
            } else if (DEBUG_RESIZERS) {
                int cloneId = -1 - (Integer)animIds.get(localAddress);
                dos.println(cloneId);
            }
            c.animations.add(anim);
        }
        if (DEBUG_RESIZERS) {
            dos.println();
        }
        if (DEBUG_RESIZERS) {
            animIds = new TreeMap();
        }
        if (charId > 25 && charId > numPlayableChars) {
            if (DEBUG_RESIZERS) {
                dos.close();
            }
            return c;
        }
        if (globalCol) {
            rom.seek(hitsListAddress + (long)(charId * 4));
            address = rom.readInt();
        } else {
            rom.seek(hitsListAddress + (long)(charId * 2));
            offset = rom.readShort();
            address = hitsListAddress + (long)(charId * 2) + (long)offset;
        }
        TreeMap<Long, HitFramesSet> hitsAddresses = new TreeMap<Long, HitFramesSet>();
        TreeMap<Long, HitFrame> processedHits = new TreeMap<Long, HitFrame>();
        for (int i = 0; i < count - FIRST_HIT_ANIM; ++i) {
            HitFramesSet set;
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset != 0) {
                set = (HitFramesSet)hitsAddresses.get(localAddress += (long)offset);
                if (set == null) {
                    int framesCount = c.animations.get(i + FIRST_HIT_ANIM).getNumFrames();
                    int before = processedHits.size();
                    set = HitFramesSet.read(rom, localAddress, framesCount, processedHits);
                    hitsAddresses.put(localAddress, set);
                    if (DEBUG_RESIZERS) {
                        animIds.put(localAddress, i);
                        if (processedHits.size() - before == framesCount) {
                            dos.println(1);
                        } else {
                            dos.println("#");
                        }
                    }
                } else if (DEBUG_RESIZERS) {
                    int oldId = -1 - (Integer)animIds.get(localAddress);
                    dos.println(oldId);
                }
                c.animHits.add(set);
            } else {
                set = new HitFramesSet(0);
                c.animHits.add(set);
                if (DEBUG_RESIZERS) {
                    dos.println(0);
                }
            }
            // Have no idea what the following line meant to be, just know that it's broken
            //if (charId == 24 && numPlayableChars < 24 || charId == numPlayableChars - 1) break;
        }
        if (DEBUG_RESIZERS) {
            dos.println();
        }
        if (DEBUG_RESIZERS) {
            animIds = new TreeMap();
        }
        if (globalWeap) {
            rom.seek(weaponsListAddress + (long)(charId * 4));
            address = rom.readInt();
        } else {
            rom.seek(weaponsListAddress + (long)(charId * 2));
            offset = rom.readShort();
            address = weaponsListAddress + (long)(charId * 2) + (long)offset;
        }
        TreeMap<Long, WeaponFramesSet> wpAddresses = new TreeMap<Long, WeaponFramesSet>();
        TreeMap<Long, WeaponFrame> processedWeapons = new TreeMap<Long, WeaponFrame>();
        for (int i = 0; i < count; ++i) {
            WeaponFramesSet set;
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset != 0) {
                set = (WeaponFramesSet)wpAddresses.get(localAddress += (long)offset);
                if (set == null) {
                    int framesCount = c.animations.get(i).getNumFrames();
                    int before = processedWeapons.size();
                    set = WeaponFramesSet.read(rom, localAddress, framesCount, processedWeapons);
                    wpAddresses.put(localAddress, set);
                    if (DEBUG_RESIZERS) {
                        animIds.put(localAddress, i);
                        if (processedWeapons.size() - before == framesCount) {
                            dos.println(1);
                        } else {
                            dos.println("#");
                        }
                    }
                } else if (DEBUG_RESIZERS) {
                    int oldId = - ((Integer)animIds.get(localAddress)).intValue();
                    dos.println(oldId);
                }
                c.animWeapons.add(set);
                continue;
            }
            set = new WeaponFramesSet(0);
            c.animWeapons.add(set);
            if (!DEBUG_RESIZERS) continue;
            dos.println(0);
        }
        if (DEBUG_RESIZERS) {
            dos.close();
        }
        return c;
    }

    public int getAnimsSize(int animsType) {
        int numAnims = this.animations.size();
        int totalSize = 0;
        for (Animation a : this.animations) {
            int totalFrames = a.getNumFrames();
            totalSize += totalFrames * a.getSizeInBytes();
        }
        return numAnims * 4 + totalSize;
    }

    public void write(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, boolean globalCol, boolean globalWeap, int numPlayableChars) throws IOException {
        Animation anim;
        short offset;
        int i;
        rom.seek(animListAddress + (long)(id * 4));
        long address = rom.readInt();
        int count = this.animations.size();
        HashSet<Animation> processed = new HashSet<Animation>();
        for (i = count - 1; i >= 0; --i) {
            Animation anim2 = this.animations.get(i);
            if (processed.contains(anim2)) continue;
            processed.add(anim2);
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            anim2.write(rom, localAddress + (long)offset);
        }
        if (globalCol) {
            rom.seek(hitsListAddress + (long)(id * 4));
            address = rom.readInt();
        } else {
            rom.seek(hitsListAddress + (long)(id * 2));
            offset = rom.readShort();
            address = hitsListAddress + (long)(id * 2) + (long)offset;
        }
        for (i = count - 24 - 1; i >= 0; --i) {
            // Have no idea what the following line meant to be, just know that it's broken
//            if (id == 24 && numPlayableChars < 24 || id == numPlayableChars - 1) {
//                i = 0;
//            }
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset == 0) continue;
            anim = this.animations.get(i + FIRST_HIT_ANIM);
            this.animHits.get(i).write(rom, localAddress + (long)offset, anim.getNumFrames());
        }
        // Have no idea what the following line meant to be, just know that it's broken
//        if (id == 24 && numPlayableChars < 24 || id == numPlayableChars - 1) {
//            return;
//        }
        if (globalWeap) {
            rom.seek(weaponsListAddress + (long)(id * 4));
            address = rom.readInt();
        } else {
            rom.seek(weaponsListAddress + (long)(id * 2));
            offset = rom.readShort();
            address = weaponsListAddress + (long)(id * 2) + (long)offset;
        }
        for (i = count - 1; i >= 0; --i) {
            long localAddress = address + (long)(i * 2);
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset == 0) continue;
            anim = this.animations.get(i);
            this.animWeapons.get(i).write(rom, localAddress + (long)offset, anim.getNumFrames());
        }
    }

    public void writeNewAnimations(RandomAccessFile rom, long animListAddress, long newAnimsAddress, int id, int type) throws IOException {
        rom.seek(animListAddress + (long)(id * 4));
        rom.writeInt((int)newAnimsAddress);
        int count = this.animations.size();
        long writeAddress = newAnimsAddress + (long)(count * 2);
        for (int i = count - 1; i >= 0; --i) {
            long pointerAddress = newAnimsAddress + (long)(i * 2);
            rom.seek(pointerAddress);
            rom.writeShort((short)(writeAddress - pointerAddress));
            int size = this.animations.get(i).write(rom, writeAddress, type);
            writeAddress += (long)size;
        }
    }

    public void writeNewScripts(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, int type, boolean globalCol, boolean globalWeap, long newAddress, boolean newHits, boolean newWeapons) throws IOException {
        long address;
        Animation anim;
        Animation anim2;
        int i;
        int offset;
        HitFramesSet hits;
        long pointerAddress;
        long writeAddress;
        int i2;
        rom.seek(animListAddress + (long)(id * 4));
        int count = this.animations.size();
        HashMap<Animation, Long> processed = new HashMap<Animation, Long>();
        if (newAddress > 0L) {
            pointerAddress = newAddress;
            rom.writeInt((int)newAddress);
            writeAddress = pointerAddress + (long)(count * 2);
            for (i2 = 0; i2 < count; ++i2) {
                anim2 = this.animations.get(i2);
                if (!processed.containsKey(anim2)) {
                    processed.put(anim2, writeAddress);
                    rom.seek(pointerAddress);
                    offset = (int)(writeAddress - pointerAddress);
                    rom.writeShort(offset);
                    anim2.write(rom, writeAddress);
                    writeAddress += (long)anim2.getSizeInBytes(type);
                } else {
                    rom.seek(pointerAddress);
                    offset = (int)((Long)processed.get(anim2) - pointerAddress);
                    rom.writeShort(offset);
                }
                pointerAddress += 2L;
            }
        } else {
            writeAddress = pointerAddress = (long)rom.readInt();
            for (i2 = 0; i2 < count; ++i2) {
                anim2 = this.animations.get(i2);
                if (!processed.containsKey(anim2)) {
                    processed.put(anim2, writeAddress -= (long)anim2.getSizeInBytes(type));
                    rom.seek(pointerAddress);
                    offset = (int)(writeAddress - pointerAddress);
                    rom.writeShort(offset);
                    anim2.write(rom, writeAddress, type);
                } else {
                    rom.seek(pointerAddress);
                    offset = (int)((Long)processed.get(anim2) - pointerAddress);
                    rom.writeShort(offset);
                }
                pointerAddress += 2L;
            }
        }
        HashMap<HitFramesSet, Long> processedHits = new HashMap<HitFramesSet, Long>();
        HashSet<HitFrame> processedSingleHits = new HashSet<HitFrame>();
        int max = this.getHitsSize();
        int frameCount = 0;
        if (globalCol) {
            rom.seek(hitsListAddress + (long)(id * 4));
            if (newHits) {
                rom.writeInt((int)writeAddress);
                pointerAddress = writeAddress;
            } else {
                pointerAddress = rom.readInt();
            }
        } else {
            rom.seek(hitsListAddress + (long)(id * 2));
            offset = rom.readShort();
            pointerAddress = hitsListAddress + (long)(id * 2) + (long)offset;
        }
        if (newHits) {
            writeAddress = pointerAddress + (long)((count - FIRST_HIT_ANIM) * 2);
            for (i = 0; i < count - FIRST_HIT_ANIM; ++i) {
                anim = this.animations.get(FIRST_HIT_ANIM + i);
                hits = this.animHits.get(i);
                if (!processedHits.containsKey(hits) && hits.process(processedSingleHits)) {
                    rom.seek(pointerAddress);
                    if (hits.frames.isEmpty()) {
                        rom.writeShort(0);
                        processedHits.put(hits, new Long(0L));
                    } else {
                        processedHits.put(hits, writeAddress);
                        offset = (int)(writeAddress - pointerAddress);
                        rom.writeShort(offset);
                        hits.write(rom, writeAddress, anim.getNumFrames());
                        writeAddress += (long)(anim.getNumFrames() * 4);
                        frameCount += anim.getNumFrames();
                    }
                } else {
                    rom.seek(pointerAddress);
                    address = processedHits.containsKey(hits) ? ((Long)processedHits.get(hits)).longValue() : ((Long)processed.values().iterator().next()).longValue();
                    if (address != 0L) {
                        address -= pointerAddress;
                    }
                    rom.writeShort((int)address);
                }
                pointerAddress += 2L;
            }
        } else {
            writeAddress = pointerAddress;
            for (i = 0; i < count - FIRST_HIT_ANIM; ++i) {
                anim = this.animations.get(FIRST_HIT_ANIM + i);
                hits = this.animHits.get(i);
                if (!processedHits.containsKey(hits) && hits.process(processedSingleHits)) {
                    rom.seek(pointerAddress);
                    if (hits.frames.isEmpty()) {
                        rom.writeShort(0);
                        processedHits.put(hits, new Long(0L));
                    } else {
                        processedHits.put(hits, writeAddress -= (long)(anim.getNumFrames() * 4));
                        offset = (int)(writeAddress - pointerAddress);
                        rom.writeShort(offset);
                        hits.write(rom, writeAddress, anim.getNumFrames());
                        frameCount += anim.getNumFrames();
                    }
                } else {
                    rom.seek(pointerAddress);
                    address = processedHits.containsKey(hits) ? ((Long)processedHits.get(hits)).longValue() : ((Long)processed.values().iterator().next()).longValue();
                    if (address != 0L) {
                        address -= pointerAddress;
                    }
                    rom.writeShort((int)address);
                }
                pointerAddress += 2L;
            }
        }
        System.out.println("Wrote: " + frameCount + " of max " + max);
        if (globalWeap) {
            rom.seek(weaponsListAddress + (long)(id * 4));
            if (newWeapons) {
                rom.writeInt((int)writeAddress);
                pointerAddress = writeAddress;
            } else {
                pointerAddress = rom.readInt();
            }
        } else {
            rom.seek(weaponsListAddress + (long)(id * 2));
            offset = rom.readShort();
            pointerAddress = weaponsListAddress + (long)(id * 2) + (long)offset;
        }
        writeAddress = pointerAddress + (long)(count * 2);
        HashMap<WeaponFramesSet, Long> processedWeapons = new HashMap<WeaponFramesSet, Long>();
        HashSet<WeaponFrame> processedSingleWeapons = new HashSet<WeaponFrame>();
        for (int i3 = 0; i3 < count; ++i3) {
            Animation anim3 = this.animations.get(i3);
            WeaponFramesSet wps = this.animWeapons.get(i3);
            if (!processedWeapons.containsKey(wps) && wps.process(processedSingleWeapons)) {
                if (wps.frames.isEmpty()) {
                    rom.seek(pointerAddress);
                    rom.writeShort(0);
                    processedWeapons.put(wps, new Long(0L));
                } else {
                    processedWeapons.put(wps, writeAddress);
                    rom.seek(pointerAddress);
                    offset = (int)(writeAddress - pointerAddress);
                    rom.writeShort(offset);
                    wps.write(rom, writeAddress, anim3.getNumFrames());
                    writeAddress += (long)(anim3.getNumFrames() * 4);
                }
            } else {
                rom.seek(pointerAddress);
                long address2 = processedWeapons.containsKey(wps) ? (Long)processedWeapons.get(wps) : 0L;
                if (address2 == 0L) {
                    rom.writeShort(0);
                } else {
                    rom.writeShort((int)(address2 - pointerAddress));
                }
            }
            pointerAddress += 2L;
        }
    }

    public boolean wasSpritesModified() {
        return this.spritesModified;
    }

    public Animation doubleAnim(int i) {
        Animation oldAnim = this.animations.get(i);
        Animation anim = new Animation(1, 0);
        anim.addFrame(oldAnim.getFrame(0));
        this.animations.set(i, anim);
        this.animWeapons.set(i, new WeaponFramesSet(0));
        if (i >= FIRST_HIT_ANIM && this.animHits.get((int)(i - FIRST_HIT_ANIM)).frames.size() > 0) {
            HitFramesSet set = new HitFramesSet(1);
            set.frames.add(this.animHits.get((int)(i - FIRST_HIT_ANIM)).frames.get(0));
            this.animHits.set(i - FIRST_HIT_ANIM, set);
        }
        return anim;
    }

    public int getHitsSize() {
        int res = 0;
        HashSet<HitFramesSet> processed = new HashSet<HitFramesSet>();
        HashSet<HitFrame> processedHits = new HashSet<HitFrame>();
        for (HitFramesSet set : this.animHits) {
            if (processed.contains(set)) continue;
            res += set.countFrames(processedHits);
            processed.add(set);
        }
        return res;
    }

    public int getWeaponsSize() {
        int res = 0;
        HashSet<WeaponFramesSet> processed = new HashSet<WeaponFramesSet>();
        HashSet<WeaponFrame> processedWeapons = new HashSet<WeaponFrame>();
        for (WeaponFramesSet set : this.animWeapons) {
            if (processed.contains(set)) continue;
            res += set.countFrames(processedWeapons);
            processed.add(set);
        }
        return res;
    }

    public void setAnim(int id1, int id2, boolean setWp, boolean setHit) {
        this.animations.set(id1, this.animations.get(id2));
        if (setWp) {
            this.animWeapons.set(id1, this.animWeapons.get(id2));
        } else {
            this.setWp(id1, !this.animWeapons.get((int)id1).frames.isEmpty(), this.animations.get(id1).getNumFrames());
        }
        if (id1 >= FIRST_HIT_ANIM && id2 >= FIRST_HIT_ANIM) {
            int d1 = id1 - FIRST_HIT_ANIM;
            int d2 = id2 - FIRST_HIT_ANIM;
            if (setHit) {
                this.animHits.set(d1, this.animHits.get(d2));
            } else {
                this.setHit(d1, !this.animHits.get((int)d1).frames.isEmpty(), this.animations.get(id1).getNumFrames());
            }
        }
    }

    private void setWp(int id, boolean hasWp, int size) {
        if (!hasWp) {
            this.animWeapons.set(id, new WeaponFramesSet(0));
        } else {
            WeaponFramesSet set = this.animWeapons.get(id);
            while (set.frames.size() < size) {
                set.frames.add(new WeaponFrame());
            }
        }
    }

    private void setHit(int id, boolean hasHit, int size) {
        if (!hasHit) {
            this.animHits.set(id, new HitFramesSet(0));
        } else {
            HitFramesSet set = this.animHits.get(id);
            while (set.frames.size() < size) {
                set.frames.add(new HitFrame());
            }
        }
    }

    public void resizeAnim(int i, int size, boolean hasWp, boolean hasHit) {
        this.setWp(i, hasWp, size);
        this.animations.get(i).resize(size);
        if (i >= FIRST_HIT_ANIM) {
            this.setHit(i - FIRST_HIT_ANIM, hasHit, size);
        }
    }
    
    
    private static String WEAPON_FRAME_KEY = "weapon";
    
    public JSONObject toJson(int characterId, String romName){
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonAnims = new JSONArray();
        JSONArray jsonAnimationsLogic = new JSONArray();
        TreeMap<Long, Integer> maps = new TreeMap<Long, Integer>();
        HashMap<Animation, Integer> processed = new HashMap<Animation, Integer>();
        int artFrame = 0;
        for (int animId = 0; animId < animations.size(); ++animId){
            JSONArray jsonFrames = new JSONArray();
            JSONArray jsonHits = new JSONArray();
            Animation animation = animations.get(animId);
            Integer framePointer = processed.get(animation);
            if (framePointer != null) {
                // Animation pointer
                jsonAnims.put(framePointer);
                jsonAnimationsLogic.put(framePointer);
                continue;
            }
            processed.put(animation, animId);
            int numFrames = animation.getNumFrames();
            int screenFramesCount = 0;
            for (int frameId = 0; frameId < numFrames; ++frameId) {
                AnimFrame animFrame = animation.getFrame(frameId);
                HitFrame hitFrame = getHitFrame(animId, frameId);
                WeaponFrame weapFrame = getWeaponFrame(animId, frameId);                
                framePointer = maps.get(animFrame.mapAddress);
                if (framePointer == null) {
                    // Frame pointer
                    framePointer = artFrame++;
                    maps.put(animFrame.mapAddress, framePointer);
                }
                JSONObject jsonFrame = animFrame.toJson(framePointer);
                if (hitFrame != null) {
                    JSONObject obj = hitFrame.toJson();                    
                    if (obj != null) {
                        obj.put("frame", screenFramesCount);
                        obj.put("duration", animFrame.delay);
                        jsonHits.put(obj);
                    }                    
                }
                if (weapFrame != null) {
                    JSONObject obj = weapFrame.toJson();
                    if (obj != null) jsonFrame.put(WEAPON_FRAME_KEY, obj);
                }
                jsonFrames.put(jsonFrame);
                screenFramesCount += animFrame.delay;
                
            }
            JSONObject framesObject = new JSONObject();
            framesObject.put("frames", jsonFrames);
            jsonAnims.put(framesObject);
            JSONObject animationLogicJson = new JSONObject();
            animationLogicJson.put("duration", screenFramesCount);
            if (jsonHits.length() > 0) {
                animationLogicJson.put("hitboxes", jsonHits);
            }
            addThrowLogic(animationLogicJson, characterId, animId, romName);
            jsonAnimationsLogic.put(animationLogicJson);
        }
        clonePointers(jsonAnims);
        clonePointers(jsonAnimationsLogic);
        jsonObj.put("animations", jsonAnims);
        jsonObj.put("animationsLogic", jsonAnimationsLogic);
        return jsonObj;
    }
    
    private boolean isFrontThrow(int characterId, int animId)
    {
        // #D460
        // Max, Axel, Blaze, Skate, Barbon, Signal, Ninja, Abadede (there are others? D4F8 onwards)
        return characterId <= 2 && animId == 24
            || characterId == 3 && animId == 41 // Skate vault throw
            || characterId == 7 && animId == 28 // Barbon
            || characterId == 9 && animId == 25 // Signal
            || characterId == 11 && animId == 24 // Ninja
            || characterId == 18 && animId == 24 // Abadede
        ;
    }
    
    private boolean isBackThrow(int characterId, int animId)
    {
        // #D558
        // Max, Axel, Blaze, Zamza, Abadede, Shiva
        return characterId <= 2 && animId == 25
            || characterId == 16 && animId == 24 // Zamza
            || characterId == 18 && animId == 34 // Abadede
            || characterId == 21 && animId == 32 // Shiva
        ;
    }
    
    private void addThrowLogic(JSONObject animationLogicJson, int characterId, int animId, String romName)
    {
        int throwPositionsAddress = -1;
        int throwReleaseAddress = -1;
        // Blaze front slam:
        if (characterId == 2 && animId == 39) {
            throwPositionsAddress = 0xD1D6;
            throwReleaseAddress = 0xD558 + characterId * 8;
        }
        // Max air throw
        else if (characterId == 0 && animId == 40) {
            throwPositionsAddress = 0xD1DA;
            throwReleaseAddress = 0xD550;
        }
        // front throws
        else if (isFrontThrow(characterId, animId)){
            throwPositionsAddress = 0xD1DE + characterId * 4;
            throwReleaseAddress = 0xD460 + characterId * 8;
        }
        // back throws
        else if (isBackThrow(characterId, animId)){
            throwPositionsAddress = 0xD3E8 + characterId * 4;
            throwReleaseAddress = 0xD558 + characterId * 8;
        }
        if (throwPositionsAddress > 0) {
            Rom rom = null;
            try {
                rom = new Rom(new File(romName));
                // Position frames data
                rom.rom.seek(throwPositionsAddress);
                int framesDataAddress = throwPositionsAddress + rom.rom.readShort();
                int releaseFrame = rom.rom.readShort();
                TODO: read frame offsets, figure tick from frame and invert order 
                // Release data
                rom.rom.seek(throwReleaseAddress);
                int velY = rom.rom.readInt();
                int velX = rom.rom.readShort();
                int damage = rom.rom.readShort();
                
                TODO: create json array with frames, create json with data, add to animationLogicJson
                JSONObject
            }
            catch (IOException ex) {
                ex.printStackTrace();
            } finally
            {
                if (rom != null)
                {
                    try {
                        rom.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    
    
    
    private void clonePointers(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length() ; ++i)
        {
            Object obj = jsonArray.get(i);
            if (obj instanceof Integer) {
                do {
                    obj = jsonArray.get((Integer)obj);
                } while (obj instanceof Integer);
                jsonArray.put(i, obj);
            }
        }
    }
    
}

