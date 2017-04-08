/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.anim;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.io.RandomAccessFile;
import java.util.*;
import lib.RandomDataStream;
import lib.Rom;
import lib.map.Decompressor;
import lib.map.Palette;
import lib.map.Sprite;



class BoxFramesSet{
    public ArrayList<BoxFrame> frames;
    
    public BoxFrame get(int id){
        return frames.get(id);
    }
    
    BoxFramesSet(int size){
        frames = new ArrayList<BoxFrame>(size);
    }
    
    static BoxFramesSet read(RandomAccessFile rom, long address, int count) throws Exception{
        BoxFramesSet res = new BoxFramesSet(count);
        for (int i = 0 ; i <= count ; i++ ){
            res.frames.add( BoxFrame.read(rom, address + i*8) );
        }
        return res;
    }
    
    void write(RandomAccessFile rom, long address) throws Exception{
        for (int i = 0 ; i < frames.size() ; i++ ){
            frames.get(i).write(rom, address + i*8);
        }
    }
            
}


class WeaponFramesSet{
    public ArrayList<WeaponFrame> frames;
    
    WeaponFramesSet(int size){
        frames = new ArrayList<WeaponFrame>(size);
    }
    
    static WeaponFramesSet read(RandomAccessFile rom, long address, int count, TreeMap<Long, WeaponFrame> processed) throws Exception{
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
    
    void write(RandomAccessFile rom, long address, int count) throws Exception{
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
    
//    public static final int MAX_WP_ANIMS = 75;
    public static final int MAX_WP_ANIMS = 84;
    
    private ArrayList<Animation> animations;
    private BoxFramesSet animBoxes;
    private BoxFramesSet animHits; 
    private ArrayList<WeaponFramesSet> animWeapons;
    private RandomDataStream art;
    private RandomDataStream mrXSpecialArt;
    
    private int maxAttackId;
    private int maxCollisionId;
    private TreeMap<Integer, Integer> boxesUsed;
    private TreeMap<Integer, Integer> attacksUsed;
    
    private boolean modified;
    private boolean spritesModified;
    
    private static int numWeaponChars = 20;
           
    
    
    
    public Character(int numAnimations, boolean hasWeapons){
        animations = new ArrayList<Animation>(numAnimations);        
        if (hasWeapons)
            animWeapons = new ArrayList<WeaponFramesSet>(Math.min(numAnimations,MAX_WP_ANIMS));
    }
    
    public void setModified(boolean modified){
        this.modified = modified;
    }
    
    public void setSpritesModified(boolean modified){
        this.spritesModified = modified;
        if (!modified){
            for(Animation anim:animations){
                anim.spritesNotModified();
            }
        }
    }
    
    public void clearBufferedData(){
        for (Animation a:animations){
            a.clearBufferedData();
        }
    }
    
    public boolean wasModified(){
        return modified;
    }
    
    public boolean wasSpritesModified() {
        return spritesModified;
    }
    
    public int getMaxCollisionId(){
        return maxCollisionId;
    }
    
    public int getMaxAttackId(){
        return maxAttackId;
    }
    
    public Animation getAnimation(int index){
        return animations.get(index);
    }
    
    public AnimFrame getAnimFrame(int animId, int frameId) {
        return animations.get(animId).getFrame(frameId);
    }
    
    public BoxFrame getBoxFrame(int animId, int frameId){  
        if (animBoxes == null) return null;
        // get the id of the hit box
        int boxId = animations.get(animId).getFrame(frameId).collisionId;
        // get the corresponding box
        if (boxId < animBoxes.frames.size())
            return animBoxes.get(boxId);
        return null;
    }
    
    public BoxFrame getHitFrame(int animId, int frameId){  
        if (animHits == null) return null;
        // get the id of the hit box
        int hitId = animations.get(animId).getFrame(frameId).attackId;
        // get the corresponding box
        if (hitId < animHits.frames.size())
            return animHits.get(hitId);
        return null;
    }
    
    public WeaponFrame getWeaponFrame(int animId, int frameId){
        if (animWeapons == null || animId >= MAX_WP_ANIMS) return null; // no weapons for this char
        final ArrayList<WeaponFrame> frames = animWeapons.get(animId).frames;
        if (frameId >= frames.size()) return null;
        return frames.get(frameId);
    }
    
    public int getNumAnimations(){
        return animations.size();
    } 
    
    public boolean isCollisionBoxDoubled(int id){
        Integer val = boxesUsed.get(id);
        if (val != null) return val > 1;
        return false;
    }
    
    public boolean isAttackBoxDoubled(int id){
        Integer val = attacksUsed.get(id);
        if (val != null) return val > 1;
        return false;
    }
    
    public void changeCollisionBox(int oldId, int newId){
        Integer val = boxesUsed.get(oldId);
        if (val != null){
            if (val == 1) boxesUsed.remove(oldId);
            else boxesUsed.put(oldId, val-1);
        }
        val = boxesUsed.get(newId);
        if (val == null) boxesUsed.put(newId, 1);
        else boxesUsed.put(newId, val+1);
    }
    
    public void changeAttackBox(int oldId, int newId){
        Integer val = attacksUsed.get(oldId);
        if (val != null){
            if (val == 1) attacksUsed.remove(oldId);
            else attacksUsed.put(oldId, val-1);
        }
        val = attacksUsed.get(newId);
        if (val == null) attacksUsed.put(newId, 1);
        else attacksUsed.put(newId, val+1);
    }
    
 
    
    public static Character read(
            RandomAccessFile rom,
            long animsListAddress,
            long boxesListAddres,
            long hitsListAddress,
            long weaponsListAddress,
            int id, int count
    ) throws Exception
    {       
        boolean hasWeapons = id < numWeaponChars;
        Character c = new Character(count, hasWeapons);
        c.boxesUsed = new TreeMap<Integer, Integer>();
        c.attacksUsed = new TreeMap<Integer, Integer>();
        
        // read animations
        rom.seek( animsListAddress + id*4);
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
            if (anim != null){
                c.animations.add(anim);
            }else{
                anim = Animation.read(rom, localAddress, c.boxesUsed, c.attacksUsed);
                animAddresses.put(localAddress, anim);
                c.animations.add(anim);
                // check max boxes indexes
                int maxFromAni = anim.getMaxBoxId();
                if (maxFromAni > c.maxCollisionId) c.maxCollisionId = maxFromAni;
                maxFromAni = anim.getMaxHitId();
                if (maxFromAni > c.maxAttackId) c.maxAttackId = maxFromAni;
            }
        }
        
        // read collision boxes
        rom.seek( boxesListAddres + id*4);
        address = rom.readInt();        
        c.animBoxes = BoxFramesSet.read(rom, address, c.maxCollisionId);
        
        // read hits
        rom.seek( hitsListAddress + id*4);        
        address = rom.readInt();        
        c.animHits = BoxFramesSet.read(rom, address, c.maxAttackId);
        
        // read weapons
        if (hasWeapons){
            rom.seek( weaponsListAddress + id*2);
            offset = rom.readShort();
            address = weaponsListAddress + id*2 + offset;
            // treemap to avoid reading the same weaponset many times
            TreeMap<Long,WeaponFramesSet> wpAddresses = new TreeMap<Long,WeaponFramesSet>();
            TreeMap<Long, WeaponFrame> processedWeapons = new TreeMap<Long, WeaponFrame>();
            if (count > MAX_WP_ANIMS) count = MAX_WP_ANIMS;
            for (int i = 0 ; i < count ; ++i){            
                long localAddress = address + i*2;
                rom.seek(localAddress);
                offset = rom.readShort();  
//                System.out.println(offset);
                if (offset != 0){
                    localAddress += offset;
//                    System.out.println(Long.toHexString(localAddress));
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
        }
//        System.out.println("###################");
        
        return c;
    }
    
    
    public void write(
            RandomAccessFile rom,
            long animsListAddress,
            long boxesListAddres,
            long hitsListAddress,
            long weaponsListAddress,
            int id
    ) throws Exception
    {       
        boolean hasWeapons = id < numWeaponChars;
                
        // write animations
        rom.seek( animsListAddress + id*4);
        long address = rom.readInt();
        short offset;
        HashSet<Animation> processed = new HashSet<Animation>();
        for (int i = 0 ; i < animations.size() ; ++i){
            Animation anim = animations.get(i);
            if (!processed.contains(anim)){
                processed.add(anim);
                long localAddress = address + i*2;
                rom.seek(localAddress);
                offset = rom.readShort();

                anim.write(rom,localAddress+offset);   
            }
        } 
        
        
        // write collision boxes
        rom.seek( boxesListAddres + id*4);
        address = rom.readInt();        
        animBoxes.write(rom, address);
        
        // write hits
        rom.seek( hitsListAddress + id*4);        
        address = rom.readInt();        
        animHits.write(rom, address);
        
        // write weapons
        if (hasWeapons){
            rom.seek( weaponsListAddress + id*2);
            offset = rom.readShort();
            address = weaponsListAddress + id*2 + offset;
            int numWpAnims = Math.min(animations.size(), MAX_WP_ANIMS);
            for (int i = 0 ; i < numWpAnims ; ++i){
                long localAddress = address + i*2;
                rom.seek(localAddress);
                offset = rom.readShort();
                if (offset != 0){
                    Animation anim = animations.get(i);                    
                    animWeapons.get(i).write(rom,localAddress + offset, anim.getNumFrames());
                }
            }
        }
    }
    
    
    
    
    public void writeNewAnimations(RandomAccessFile rom, long animListAddress, long newAnimsAddress, int id, int type)  throws Exception{        
        // write animations
        rom.seek( animListAddress + id*4);
        rom.writeInt((int)newAnimsAddress);
        int count = animations.size();
        long writeAddress = newAnimsAddress + count*2;
        for (int i = 0 ; i < count ; ++i){
            long pointerAddress = newAnimsAddress + i*2;
            rom.seek(pointerAddress);
            rom.writeShort( (short)(writeAddress-pointerAddress) );
            int size = animations.get(i).write(rom, writeAddress, type);
            writeAddress += size;
        }
    }
    
    
    public void writeNewScripts(RandomAccessFile rom, long animListAddress, long weaponsListAddress, int id, int type, long newAddress)  throws Exception{
        boolean hasWeapons = id < numWeaponChars;
        // write animations
        rom.seek( animListAddress + id*4);        
        long pointerAddress;
        if (newAddress > 0){
            pointerAddress = newAddress;
            rom.writeInt((int)newAddress);
        }else{
            pointerAddress = rom.readInt();
        }
        int offset;
        int count = animations.size();
        long writeAddress = pointerAddress + count*2;        
        HashMap<Animation, Long> processed = new HashMap<Animation, Long>();
        for (int i = 0 ; i < count ; ++i){
            Animation anim = animations.get(i);
            if (!processed.containsKey(anim)){                
                processed.put(anim, writeAddress);                
                rom.seek(pointerAddress);
                offset = (int)(writeAddress-pointerAddress);
                rom.writeShort(offset);
                anim.write(rom, writeAddress);
                writeAddress += anim.getSizeInBytes(type);
            }else{
                rom.seek(pointerAddress);
                offset = (int)(processed.get(anim)-pointerAddress);
                rom.writeShort(offset);
            }
            pointerAddress += 2;
        }                                
        
        // write weapons
        if (hasWeapons){
            int numWpAnims = Math.min(count, MAX_WP_ANIMS);
            rom.seek( weaponsListAddress + id*2);
            offset = rom.readShort();
            pointerAddress = weaponsListAddress + id*2 + offset;
            writeAddress = pointerAddress+numWpAnims*2;
            HashMap<WeaponFramesSet, Long> processedWeapons = new HashMap<WeaponFramesSet, Long>();
            HashSet<WeaponFrame> processedSingleWeapons = new HashSet<WeaponFrame>();            
            for (int i = 0 ; i < numWpAnims ; ++i){
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
        
    }
    
//    public void clearUncompressedArt(){
//        art = null;
//    }

    public void uncompressArt(String romName, List<Long> compressedArtAddresses) throws Exception {
        if (art == null)
            art = new RandomDataStream();
        long uncompressedAddress = -1;
        for (long l:compressedArtAddresses){
            if (l > 0)
                art.append(Decompressor.run(romName, l).asList());
            else uncompressedAddress = -l;
        }
        for (Animation anim: animations){
            if (uncompressedAddress < 0)
                anim.setArtStream(art);
            else anim.setUncompressedArtAddress(uncompressedAddress);
        }
    }

    public void swapArtToMrX() {
        mrXSpecialArt = new RandomDataStream();
        mrXSpecialArt.append(art.asList());
//        System.out.println(Long.toHexString(art.getWritePos()));
        // art size: 1480
        // between 45d & 467
        art.seek(0x460);
    }
    
    public void fixMrXArtStream(){
        animations.get(3).setArtStream(mrXSpecialArt);
        animations.get(26).setArtStream(mrXSpecialArt);
        animations.get(27).setArtStream(mrXSpecialArt);
        animations.get(28).setArtStream(mrXSpecialArt);
        animations.get(29).setArtStream(mrXSpecialArt);
        animations.get(30).setArtStream(mrXSpecialArt);
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
    
    public Animation doubleAnim(int i) {
        Animation oldAnim = animations.get(i);
        Animation anim = new Animation(1);
        anim.addFrame(oldAnim.getFrame(0));
        animations.set(i, anim);
        if (i < animWeapons.size())
            animWeapons.set(i, new WeaponFramesSet(0));        
        return anim;
    }

    
    public void setAnim(int id1, int id2, boolean setWp){
        animations.set(id1, animations.get(id2));
        if (id1 < animWeapons.size() && id2 < animWeapons.size()){
            if (setWp)
                animWeapons.set(id1, animWeapons.get(id2));
            else{
                setWp(id1, !animWeapons.get(id1).frames.isEmpty(), animations.get(id1).getNumFrames());
            }
        }
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
    

    public void resizeAnim(int i, int size, boolean hasWp) {
        if (i < animWeapons.size())
            setWp(i,hasWp,size);
        animations.get(i).resize(size);        
    }
    
}
