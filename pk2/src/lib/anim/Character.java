/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.anim;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import lib.Renderable;
import lib.Rom;
import lib.map.Palette;
import lib.map.Sprite;






class HitFramesSet{
    public ArrayList<HitFrame> frames;
    
    HitFramesSet(int size){
        frames = new ArrayList<HitFrame>(size);
    }
    
    static HitFramesSet read(RandomAccessFile rom, long address, int count, TreeMap<Long, HitFrame> processed) throws IOException{
        HitFramesSet res = new HitFramesSet(count);
        for (int i = count-1 ; i >= 0 ; i-- ){
            long localAddress = address+i*4;
            HitFrame frame = processed.get(localAddress);
            if (frame == null){
                frame = HitFrame.read(rom, localAddress);
                processed.put(localAddress, frame);
            }
            res.frames.add( frame );
        }
        return res;
    }
    
    void write(RandomAccessFile rom, long address, int count) throws IOException{
        for (int i = 0 ; i < count && i < frames.size() ; i++ ){
            frames.get(i).write(rom, address + (count-1-i)*4);
        }
    }
    
    int countFrames(HashSet<HitFrame> processed){
        int res = 0;
        for (HitFrame f:frames){
            if (!processed.contains(f)){
                processed.add(f);
                res++;
            }
        }
        return res;
    }
    
    boolean process(HashSet<HitFrame> processed){
        for (HitFrame f:frames){
            if (processed.contains(f)){
                return false;
            }else{
                processed.add(f);
            }
        }
        return true;
    }
            
}


class WeaponFramesSet{
    public ArrayList<WeaponFrame> frames;
    
    WeaponFramesSet(int size){
        frames = new ArrayList<WeaponFrame>(size);
//        for (int i = 0 ; i < size ; ++i){
//            frames.add(new WeaponFrame());
//        }
    }
    
    static WeaponFramesSet read(RandomAccessFile rom, long address, int count, TreeMap<Long, WeaponFrame> processed) throws IOException{
        WeaponFramesSet res = new WeaponFramesSet(count);
        for (int i = count-1 ; i >= 0 ; i-- ){
            long localAddress = address+i*4;
            WeaponFrame frame = processed.get(localAddress);
            if (frame == null){
                frame = WeaponFrame.read(rom, localAddress);
                processed.put(localAddress, frame);
            }
            res.frames.add( frame );
        }
        return res;
    }
    
    void write(RandomAccessFile rom, long address, int count) throws IOException{
        for (int i = 0 ; i < count ; i++ ){
            frames.get(i).write(rom, address + (count-1-i)*4);
        }
    }
    
    int countFrames(HashSet<WeaponFrame> processed){
        int res = 0;
        for (WeaponFrame f:frames){
            if (!processed.contains(f)){
                processed.add(f);
                res++;
            }
        }
        return res;
    }
    
    boolean process(HashSet<WeaponFrame> processed){
        for (WeaponFrame f:frames){
            if (processed.contains(f)){
                return false;
            }else{
                processed.add(f);
            }
        }
        return true;
    }
            
}

/**
 *
 * @author Gil
 */
public class Character{
    public static final int FIRST_HIT_ANIM = 24;
    private ArrayList<Animation> animations;
    private ArrayList<HitFramesSet> animHits;
    private ArrayList<WeaponFramesSet> animWeapons;
    private HitFramesSet hits;
    private boolean modified;
    private boolean spritesModified;
    
    public void setModified(boolean modified){
        this.modified = modified;
    }
    
    public void setSpritesModified(boolean modified){
        this.spritesModified = modified;
    }
    
    public boolean wasModified(){
        return modified;
    }
    
    public Character(int numAnimations){
        animations = new ArrayList<Animation>(numAnimations);
        if (numAnimations > FIRST_HIT_ANIM){
            animHits = new ArrayList<HitFramesSet>(numAnimations - FIRST_HIT_ANIM);
        } else animHits = new ArrayList<HitFramesSet>(0);
        animWeapons = new ArrayList<WeaponFramesSet>(numAnimations);
    }
    
    public Animation getAnimation(int index){
        return animations.get(index);
    }
    
    public AnimFrame getAnimFrame(int animId, int frameId) {
        return animations.get(animId).getFrame(frameId);
    }
    
    public HitFrame getHitFrame(int animId, int frameId){
        if (animId < FIRST_HIT_ANIM || animId-FIRST_HIT_ANIM >= animWeapons.size()) return null;
        hits = animHits.get(animId-FIRST_HIT_ANIM);
        if (hits == null) return null;
        final ArrayList<HitFrame> frames = hits.frames;
        if (frames == null || frames.size() <= frameId) return null;
        return frames.get(frameId);
    }
    
    public WeaponFrame getWeaponFrame(int animId, int frameId){
        if (animId >= animWeapons.size()) return null;
        final WeaponFramesSet weapon = animWeapons.get(animId);
        if (weapon == null) return null;
        final ArrayList<WeaponFrame> frames = weapon.frames;
        if (frames == null || frameId >= frames.size()) return null;
        return frames.get(frameId);
    }
    
    public int getNumAnimations(){
        return animations.size();
    }    
    
    public static Character read(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, int count)  throws IOException{
        return read(rom, animListAddress, hitsListAddress, weaponsListAddress, id, count, -1);
    }
    
    public static Character read(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, int count, int animsType)  throws IOException{
        Character c = new Character(count);
        
        // read animations
        rom.seek( animListAddress + id*4);
        long address = rom.readInt();
        short offset;
        // treemap to avoid reading the same animation many times
        TreeMap<Long,Animation> animAddresses = new TreeMap<Long,Animation>();
        for (int i = 0 ; i < count ; ++i){
            long localAddress = address + i*2;
            
            rom.seek(localAddress);
            offset = rom.readShort();
            localAddress += offset;
            Animation anim = animAddresses.get(localAddress);
            if (anim == null){
                anim = Animation.read(rom, localAddress, animsType);
                animAddresses.put(localAddress, anim);
            }
            c.animations.add(anim);
        }
        
        if (id > 24) return c;
        // read hits
        rom.seek( hitsListAddress + id*2);
        offset = rom.readShort();
        address = hitsListAddress + id*2 + offset;
        // treemap to avoid reading the same hits many times
        TreeMap<Long,HitFramesSet> hitsAddresses = new TreeMap<Long,HitFramesSet>();
        TreeMap<Long, HitFrame> processedHits = new TreeMap<Long, HitFrame>();
        for (int i = 0 ; i < count-FIRST_HIT_ANIM ; ++i){
            long localAddress = address + i*2;
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset != 0){
                localAddress += offset;
                HitFramesSet set = hitsAddresses.get(localAddress);
                if (set == null){
                    int framesCount = c.animations.get(i+FIRST_HIT_ANIM).getNumFrames();
                    set = HitFramesSet.read(rom, localAddress, framesCount, processedHits);
                    hitsAddresses.put(localAddress, set);
                }
                c.animHits.add( set );
            }else{
                HitFramesSet set = new HitFramesSet(0);
                c.animHits.add(set);
            }
        }
        
        // read weapons
        rom.seek( weaponsListAddress + id*2);
        offset = rom.readShort();
        address = weaponsListAddress + id*2 + offset;
        // treemap to avoid reading the same weaponset many times
        TreeMap<Long,WeaponFramesSet> wpAddresses = new TreeMap<Long,WeaponFramesSet>();
        TreeMap<Long, WeaponFrame> processedWeapons = new TreeMap<Long, WeaponFrame>();
        for (int i = 0 ; i < count ; ++i){            
            long localAddress = address + i*2;
            rom.seek(localAddress);
            offset = rom.readShort();            
            if (offset != 0){
                localAddress += offset;
                WeaponFramesSet set = wpAddresses.get(localAddress);
                if (set == null){                   
                    int framesCount = c.animations.get(i).getNumFrames();
                    set = WeaponFramesSet.read(rom, localAddress, framesCount, processedWeapons);
                    wpAddresses.put(localAddress,set);
                }
                c.animWeapons.add(set);
            }else{                
                WeaponFramesSet set = new WeaponFramesSet(0);
                c.animWeapons.add(set);
            }
        }
        
        return c;
    }
    
    
    public int getAnimsSize(int animsType){
        int numAnims = animations.size();
        int totalFrames = 0;
        for (Animation a:animations){
            totalFrames += a.getNumFrames();
        }
        // 4 bytes for every anim header (2 for pointer, 2 for num frames)
        // frames size depending on type
        return numAnims*4 + totalFrames*AnimFrame.getTypeDependentSize(animsType);
    }

    public void write(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id)  throws IOException{
        long address;
        int offset;
        
        rom.seek( animListAddress + id*4);
        address = rom.readInt();
        writeAnimations(rom, address);
        
        rom.seek( hitsListAddress + id*2);
        offset = rom.readShort();
        address = hitsListAddress + id*2 + offset;
        writeHits(rom, address);
        
        rom.seek( weaponsListAddress + id*2);
        offset = rom.readShort();
        address = weaponsListAddress + id*2 + offset;
        writeWeapons(rom, address);
    }
    
    public void writeAnimations(RandomAccessFile rom, long address)  throws IOException{
        // write animations
        short offset;
        int count = animations.size();
        HashSet<Animation> processed = new HashSet<Animation>();
        for (int i = count-1 ; i >=0 ; --i){
            Animation anim = animations.get(i);
            if (!processed.contains(anim)){
                processed.add(anim);
                long localAddress = address + i*2;
                rom.seek(localAddress);
                offset = rom.readShort();
                anim.write(rom, localAddress+offset);
            }
        } 
    }
    
    
    public void writeHits(RandomAccessFile rom, long address)  throws IOException{
        // write hits
        int offset;
        int count = animations.size();
        for (int i = count-FIRST_HIT_ANIM-1 ; i >= 0 ; --i){
            long localAddress = address + i*2;
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset != 0){
                // fix offset if animSize was reduced
                Animation anim = animations.get(i+FIRST_HIT_ANIM);
                animHits.get(i).write(rom,localAddress + offset, anim.getNumFrames());
            }
        }
    }
    
    
    public void writeWeapons(RandomAccessFile rom, long address)  throws IOException{
        // write weapons
        int offset;
        int count = animations.size();
        for (int i = count-1 ; i >=0 ; --i){
            long localAddress = address + i*2;
            rom.seek(localAddress);
            offset = rom.readShort();
            if (offset != 0){
                // fix offset if animSize was reduced
                Animation anim = animations.get(i);
                animWeapons.get(i).write(rom,localAddress + offset, anim.getNumFrames());
            }
        }
    }
    
    
    public void writeNewAnimations(RandomAccessFile rom, long animListAddress, long newAnimsAddress, int id, int type)  throws IOException{
        // write animations
        rom.seek( animListAddress + id*4);
        rom.writeInt((int)newAnimsAddress);
        int count = animations.size();
        long writeAddress = newAnimsAddress + count*2;
        for (int i = count-1 ; i >=0 ; --i){
            long pointerAddress = newAnimsAddress + i*2;
            rom.seek(pointerAddress);
            rom.writeShort( (short)(writeAddress-pointerAddress) );
            int size = animations.get(i).write(rom, writeAddress, type);
            writeAddress += size;
        }
    }
    
    public void writeNewScripts(RandomAccessFile rom, long animListAddress, long hitsListAddress, long weaponsListAddress, int id, int type)  throws IOException{
        // write animations
        rom.seek( animListAddress + id*4);
        long pointerAddress = rom.readInt();
        long writeAddress = pointerAddress;
        int offset;
        int count = animations.size();
        HashMap<Animation, Long> processed = new HashMap<Animation, Long>();
        for (int i = 0 ; i < count ; ++i){
            Animation anim = animations.get(i);
            if (!processed.containsKey(anim)){
                writeAddress -= anim.getSizeInBytes(type);                
                processed.put(anim, writeAddress);                
                rom.seek(pointerAddress);
                offset = (int)(writeAddress-pointerAddress);
                rom.writeShort(offset);
                anim.write(rom, writeAddress, type);
                
            }else{
                rom.seek(pointerAddress);
                offset = (int)(processed.get(anim)-pointerAddress);
                rom.writeShort(offset);
            }
            pointerAddress += 2;
        }
        
        // write hits
        int max = getHitsSize();
        int frameCount = 0;
        rom.seek( hitsListAddress + id*2);
        offset = rom.readShort();
        pointerAddress = hitsListAddress + id*2 + offset;
        writeAddress = pointerAddress;
        HashMap<HitFramesSet, Long> processedHits = new HashMap<HitFramesSet, Long>();
        HashSet<HitFrame> processedSingleHits = new HashSet<HitFrame>();
        for (int i = 0 ; i < count-FIRST_HIT_ANIM ; ++i){
            Animation anim = animations.get(FIRST_HIT_ANIM+i);
            HitFramesSet hits = animHits.get(i);
            if (!processedHits.containsKey(hits) && hits.process(processedSingleHits)){
                rom.seek(pointerAddress);
                if (hits.frames.isEmpty()){
                    rom.writeShort(0); // no hits
                    processedHits.put(hits, new Long(0));
                }else{
                    writeAddress -= anim.getNumFrames()*4;
                    processedHits.put(hits, writeAddress);
                    offset = (int)(writeAddress-pointerAddress);
                    rom.writeShort(offset);
                    hits.write(rom, writeAddress, anim.getNumFrames());
                    frameCount += anim.getNumFrames();
                }
            }else{
                rom.seek(pointerAddress);
                long address;
                if (processedHits.containsKey(hits))
                    address = processedHits.get(hits);
                else address = processed.values().iterator().next();
                if (address != 0) address = address - pointerAddress;
                rom.writeShort((int)address);
            }
            pointerAddress += 2;
        }
        
        System.out.println("Wrote: " + frameCount + " of max " + max);
        
        // write weapons
        rom.seek( weaponsListAddress + id*2);
        offset = rom.readShort();
        pointerAddress = weaponsListAddress + id*2 + offset;
        writeAddress = pointerAddress+count*2;
        HashMap<WeaponFramesSet, Long> processedWeapons = new HashMap<WeaponFramesSet, Long>();
        HashSet<WeaponFrame> processedSingleWeapons = new HashSet<WeaponFrame>();
        for (int i = 0 ; i < count ; ++i){
            Animation anim = animations.get(i);
            WeaponFramesSet wps = animWeapons.get(i);
            if (!processedWeapons.containsKey(wps) && wps.process(processedSingleWeapons)){
                if (wps.frames.isEmpty()){
                    rom.seek(pointerAddress);
                    rom.writeShort(0);
                    processedWeapons.put(wps, new Long(0));                    
                }else{    
                    processedWeapons.put(wps, writeAddress);                    
                    rom.seek(pointerAddress);
                    offset = (int)(writeAddress-pointerAddress);
                    rom.writeShort(offset);
                    wps.write(rom, writeAddress, anim.getNumFrames());
                    writeAddress += anim.getNumFrames()*4;                    
                }
            }else{
                rom.seek(pointerAddress);
                long address;
                if (processedWeapons.containsKey(wps)){                    
                    address = processedWeapons.get(wps);
                }else{
                    address = 0;
                }
                if (address == 0) rom.writeShort(0);
                else rom.writeShort((int)(address-pointerAddress));
            }
            pointerAddress += 2;
        }
        
    }
    

    public boolean wasSpritesModified() {
        return spritesModified;
    }

    public Animation doubleAnim(int i) {
        Animation oldAnim = animations.get(i);
        Animation anim = new Animation(1,0);
        anim.addFrame(oldAnim.getFrame(0));
        animations.set(i, anim);
        animWeapons.set(i, new WeaponFramesSet(0));
        if (i >= FIRST_HIT_ANIM && animHits.get(i-FIRST_HIT_ANIM).frames.size() > 0){
            HitFramesSet set = new HitFramesSet(1);
            set.frames.add(animHits.get(i-FIRST_HIT_ANIM).frames.get(0));
            animHits.set(i-FIRST_HIT_ANIM, set);
        }
        return anim;
    }
    
    public int getHitsSize(){
        int res = 0;
        HashSet<HitFramesSet> processed = new HashSet<HitFramesSet>();
        HashSet<HitFrame> processedHits = new HashSet<HitFrame>();
        for(HitFramesSet set:animHits){
            if (!processed.contains(set)){
                res+=set.countFrames(processedHits);
                processed.add(set);
            }
        }
        return res;
    }
    
    public int getWeaponsSize(){
        int res = 0;
        HashSet<WeaponFramesSet> processed = new HashSet<WeaponFramesSet>();
        HashSet<WeaponFrame> processedWeapons = new HashSet<WeaponFrame>();
        for(WeaponFramesSet set:animWeapons){
            if (!processed.contains(set)){
                res+=set.countFrames(processedWeapons);
                processed.add(set);
            }
        }
        return res;
    }
    
//    public void setAnim(int id1, int id2, boolean ownCol, boolean ownWeap){
//        animations.set(id1, animations.get(id2));
//        if (ownWeap){
//            setWp(id1, true, animations.get(id1).getNumFrames());
//        }else{
//            animWeapons.set(id1, animWeapons.get(id2));
//        }
//        if (id1 >= FIRST_HIT_ANIM && id2 >= FIRST_HIT_ANIM){
//            if (ownCol){
//                setCol(id1, true, animations.get(id1).getNumFrames());
//            }else{
//                animHits.set(id1-FIRST_HIT_ANIM, animHits.get(id2-FIRST_HIT_ANIM));
//            }
//        }
//    }
    
    
//    public void setAnim(int id1, int id2){
//        System.out.print("set " + id2 + " to " + id1 + ", before: " + getHitsSize() + " ");
//        animations.set(id1, animations.get(id2));
//        boolean hasWeapon = !animWeapons.get(id2).frames.isEmpty();
//        setWp(id1, hasWeapon, animations.get(id2).getNumFrames());        
//        if (id1 >= FIRST_HIT_ANIM && id2 >= FIRST_HIT_ANIM){  
//            HitFramesSet set = animHits.get(id1-FIRST_HIT_ANIM);
//            HitFramesSet otherSet = animHits.get(id2-FIRST_HIT_ANIM);
//            int size = otherSet.frames.size();
//            System.out.println("old size: " + set.frames.size() + ", new size: " + size);
//            while (set.frames.size() < size)
//                set.frames.add(new HitFrame());
//        }
//        System.out.println("after: " + getHitsSize());
//    }
    
    public void setAnim(int id1, int id2, boolean setWp, boolean setHit){
//        System.out.println("before: " + getHitsSize());
        animations.set(id1, animations.get(id2));
        if (setWp)
            animWeapons.set(id1, animWeapons.get(id2));
        else{
            setWp(id1, !animWeapons.get(id1).frames.isEmpty(), animations.get(id1).getNumFrames());
        }
        if (id1 >= FIRST_HIT_ANIM && id2 >= FIRST_HIT_ANIM){            
            int d1 = id1-FIRST_HIT_ANIM;
            int d2 = id2-FIRST_HIT_ANIM;
            if (setHit){
                animHits.set(d1, animHits.get(d2)); 
            }else{
                setHit(d1,!animHits.get(d1).frames.isEmpty(), animations.get(id1).getNumFrames());
            }
        }
//        System.out.println("after: " + getHitsSize());
    }
    
    private void setWp(int id, boolean hasWp, int size){
        if (!hasWp){
            animWeapons.set(id, new WeaponFramesSet(0));
        }else{
            WeaponFramesSet set = animWeapons.get(id);
            while (set.frames.size() < size)
                set.frames.add(new WeaponFrame());
        }
    }
    
    private void setHit(int id, boolean hasHit, int size){
        if (!hasHit){
            animHits.set(id, new HitFramesSet(0));
        }else{
            HitFramesSet set = animHits.get(id);
            while (set.frames.size() < size)
                set.frames.add(new HitFrame());
        }
    }
    

    public void resizeAnim(int i, int size, boolean hasWp, boolean hasHit) {
        setWp(i,hasWp,size);
        animations.get(i).resize(size);
        if (i >=FIRST_HIT_ANIM){
            setHit(i-FIRST_HIT_ANIM,hasHit,size);
        }
    }

    
}
