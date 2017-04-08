/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.anim;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.Exception;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import lib.RandomDataStream;
import lib.Rom;
import lib.map.Decompressor;
import lib.map.Palette;
import lib.map.Sprite;

/**
 *
 * @author Gil
 */
public class Animation{
    private ArrayList<AnimFrame> frames;
    private int numFrames;
    private int type;
    private RandomDataStream art;
    private long uncompressedAddress;
    
    private TreeMap<Long,BufferedImage> bufferedFrames;
    private ArrayList<BufferedImage> bufferedShadows;
    private boolean wasArtModified;
    private boolean[] spritesModified;
    
    public void setArtStream(RandomDataStream art){
        this.art = art;
    }
    
    public boolean isCompressed(){
        return type == 1;
    }
    
    public Animation(int numFrames){
        frames = new ArrayList<AnimFrame>(numFrames);
        spritesModified = new boolean[numFrames];
        this.numFrames = numFrames;
    }
    
    public AnimFrame getFrame(int index){
        return frames.get(index);
    }
    
    public int getNumFrames() {
        return numFrames;
    }
    
    public int getMaxNumFrames(){
        return frames.size();
    }
    
    
    public void setNumFrames(int num){
        if (numFrames < 0 || num > frames.size()) throw new ArrayIndexOutOfBoundsException();
        numFrames = num;
    }
    
    public boolean wasArtModified(){
        return wasArtModified;
    }
    
    
    public BufferedImage getImage(int index){        
        if (bufferedFrames != null && index < frames.size()){
            long address = frames.get(index).mapAddress;
            return bufferedFrames.get(address);
        }else
            return null;
    }
    
    public void setImage(int index, BufferedImage img){
        if (index < frames.size()){
            long address = frames.get(index).mapAddress;
            if (bufferedFrames == null){
                bufferedFrames = new TreeMap<Long,BufferedImage>();
                bufferedShadows = new ArrayList<BufferedImage>(numFrames);
            }
            bufferedFrames.put(address, img);
            spritesModified[index] = true;
            wasArtModified = true;
        }
    }
    
    public BufferedImage getShadow(int index){
        if (bufferedShadows != null && index <  bufferedShadows.size())
            return bufferedShadows.get(index);
        else return null;
    }
    
    public boolean isBuffered(){
        return bufferedFrames != null && !bufferedFrames.isEmpty();
    }
    
    public void clearBufferedData(){
        bufferedFrames = null;
        bufferedShadows = null;
    }
    
    
    public void bufferImage(int frameId, Rom rom, Palette palette, Color shadowColor) throws Exception{        
            if (bufferedFrames == null){
                bufferedFrames = new TreeMap<Long,BufferedImage>();
                bufferedShadows = new ArrayList<BufferedImage>(numFrames);
            }
            AnimFrame frame = getFrame(frameId);
            Sprite sprite;
            if (frame.type == 1) sprite = rom.readSprite(frame.mapAddress, art);
            else sprite = rom.readSprite(frame.mapAddress, frame.artAddress);
            while (bufferedShadows.size() <= frameId) bufferedShadows.add(null);
            bufferedFrames.put(frame.mapAddress, sprite.asImage(palette) );
            bufferedShadows.set(frameId, sprite.asShadow(shadowColor) );
    }
    
    public void buffer(Rom rom, Palette palette, Color shadowColor) throws Exception{
        bufferedFrames = new TreeMap<Long,BufferedImage>();
        bufferedShadows = new ArrayList<BufferedImage>(numFrames);
        Sprite sprite;
        AnimFrame frame;
        if (type == 1 && art == null && uncompressedAddress <= 0) throw new Exception("Art is not decompressed");
            //art = Decompressor.run(rom.getRomName(), getFrame(0).artAddress);
        for (int i = 0 ; i < numFrames ; ++i){
            frame = getFrame(i);
            if (frame.type == 1){
                if (uncompressedAddress <= 0)
                    sprite = rom.readSprite(frame.mapAddress, art);
                else sprite = rom.readSprite(frame.mapAddress, uncompressedAddress);
            }else sprite = rom.readSprite(frame.mapAddress, frame.artAddress);
            bufferedFrames.put(frame.mapAddress,sprite.asImage(palette) );
            bufferedShadows.add( sprite.asShadow(shadowColor) );
        }
    }        
    
    public static Animation read(RandomAccessFile rom, long address, TreeMap<Integer, Integer>boxes, TreeMap<Integer, Integer> attacks)  throws Exception{
        rom.seek(address);
        int size = rom.readUnsignedShort(); // num frames
        Animation anim = new Animation(size);
        long currAddress = address+2;
        for (int i = 0 ; i < size ; ++i){
            AnimFrame frame = AnimFrame.read(rom, currAddress);
            Integer val = boxes.get(frame.collisionId);
            if (val == null) boxes.put(frame.collisionId, 1);
            else boxes.put(frame.collisionId, val+1);            
            val = attacks.get(frame.attackId);
            if (val == null) attacks.put(frame.attackId, 1);
            else attacks.put(frame.attackId, val+1);
            anim.frames.add(frame);
            if (frame.type == 1){
                anim.type = 1; // frame by frame art type
            }
            currAddress += AnimFrame.getTypeDependentSize(frame.type);
        }
        return anim;
    }
    
    
    public void write(RandomAccessFile rom, long address)  throws Exception{
        rom.seek(address);
        rom.writeShort(numFrames);
        int accum = 0;
        for (int i = 0 ; i < numFrames ; ++i){
            AnimFrame frame = getFrame(i);
            frame.write(rom, address + 2 + accum);
            accum += AnimFrame.getTypeDependentSize(frame.type);
        }
    }
    
    public int write(RandomAccessFile rom, long address, int type)  throws Exception{
        rom.seek(address);
        rom.writeShort(numFrames);
        int frameSize = AnimFrame.getTypeDependentSize(type);
        for (int i = 0 ; i < numFrames ; ++i){
            AnimFrame frame = getFrame(i);
            frame.write(rom, address + 2 + i*frameSize, type);
        }
        return 2 + numFrames*frameSize;
    }

    public void setSpritesModified(int frame, boolean b) {
        spritesModified[frame] = b;
        if (b) wasArtModified = true;
    }
    
    public boolean wasSpriteModified(int frame){
        return spritesModified[frame];
    }
    
    public void spritesNotModified(){
        wasArtModified = false;
        spritesModified = new boolean[frames.size()];
    }
    
    public void addFrame(AnimFrame frame) {
        frames.add(frame);
    }

    public void setAnimType(int newType) {
        type = newType;
        for(AnimFrame frame:frames){
            frame.type = newType;
        }
    }

    public void resize(Integer size) {
        numFrames = size;        
        AnimFrame f1 = frames.get(0);
        int dif = size-frames.size();
        for (int i = 0 ; i < dif ; ++i){
            frames.add(f1);
        }
        clearBufferedData();
    }
    

    int getMaxHitId() {
        int res = 0;
        for (AnimFrame f: frames){
            if (f.attackId > res) res = f.attackId;
        }
        return res;
    }

    int getMaxBoxId() {
        int res = 0;
        for (AnimFrame f: frames){
            if (f.collisionId > res) res = f.collisionId;
        }
        return res;
    }
    
    public int getSizeInBytes(int type){
        int frameSize = AnimFrame.getTypeDependentSize(type);
        return numFrames*frameSize + 2;
    }

    void setUncompressedArtAddress(long uncompressedAddress) {
        this.uncompressedAddress = uncompressedAddress;
    }

    
}
