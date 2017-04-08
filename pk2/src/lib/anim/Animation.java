/* 
 * Copyright 2017 Gil Costa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lib.anim;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.TreeMap;
import lib.Renderable;
import lib.Rom;
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
    
    private TreeMap<Long,BufferedImage> bufferedFrames;
    private ArrayList<BufferedImage> bufferedShadows;
    private boolean wasArtModified;
    private boolean[] spritesModified;
    
    public boolean isCompressed(){
        return type == 1;
    }
    
    public Animation(int numFrames, int type){
        this.type = type;
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
    
//    public Sprite getSprite(int frameId, Rom rom) throws IOException{
//        AnimFrame frame = getFrame(frameId);
//        Sprite sprite;
//        if (type == 1) sprite = rom.readCompressedSprite(frame.mapAddress, frame.artAddress);
//        else sprite = rom.readSprite(frame.mapAddress, frame.artAddress);
//        return sprite;
//    }
    
    public void bufferImage(int frameId, Rom rom, Palette palette, Color shadowColor) throws IOException{        
            if (bufferedFrames == null){
                bufferedFrames = new TreeMap<Long,BufferedImage>();
                bufferedShadows = new ArrayList<BufferedImage>(numFrames);
            }
            AnimFrame frame = getFrame(frameId);
            Sprite sprite;
            if (frame.type == 1) sprite = rom.readCompressedSprite(frame.mapAddress, frame.artAddress);
            else sprite = rom.readSprite(frame.mapAddress, frame.artAddress);
//            while (bufferedFrames.size() <= frameId) bufferedFrames.put(frame.mapAddress, null);
            while (bufferedShadows.size() <= frameId) bufferedShadows.add(null);
            bufferedFrames.put(frame.mapAddress, sprite.asImage(palette) );
            bufferedShadows.set(frameId, sprite.asShadow(shadowColor) );
    }
    
    public void buffer(Rom rom, Palette palette, Color shadowColor) throws IOException{
        bufferedFrames = new TreeMap<Long,BufferedImage>();
        bufferedShadows = new ArrayList<BufferedImage>(numFrames);
        Sprite sprite;
        AnimFrame frame;
        for (int i = 0 ; i < numFrames ; ++i){
            frame = getFrame(i);
            if (frame.type == 1) sprite = rom.readCompressedSprite(frame.mapAddress, frame.artAddress);
            else sprite = rom.readSprite(frame.mapAddress, frame.artAddress);
            bufferedFrames.put(frame.mapAddress,sprite.asImage(palette) );          
            bufferedShadows.add( sprite.asShadow(shadowColor) );
        }
    }        
    
    public static Animation read(RandomAccessFile rom, long address, int type)  throws IOException{
        rom.seek(address);
        int size = rom.readUnsignedShort();
        Animation anim = new Animation(size, type);
        int accum = 0;
        for (int i = 0 ; i < size ; ++i){
            AnimFrame frame = AnimFrame.read(rom, address + 2 + accum, type);
            accum += AnimFrame.getTypeDependentSize(frame.type);
            anim.frames.add(frame);
        }
        return anim;
    }

    public void write(RandomAccessFile rom, long address)  throws IOException{
        rom.seek(address);
        rom.writeShort(numFrames);
        int accum = 0;
        for (int i = 0 ; i < numFrames ; ++i){
            AnimFrame frame = getFrame(i);
            frame.write(rom, address + 2 + accum);
            accum += AnimFrame.getTypeDependentSize(frame.type);
        }
    }
    
    public int write(RandomAccessFile rom, long address, int type)  throws IOException{
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

    public void addFrame(AnimFrame frame) {
        frames.add(frame);
    }
    
    public int getSizeInBytes(int type){
        int frameSize = AnimFrame.getTypeDependentSize(type);
        return numFrames*frameSize + 2;
    }

    
}
