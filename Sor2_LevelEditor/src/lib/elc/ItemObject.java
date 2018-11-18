/*
 * Copyright 2018 gil.costa.
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
package lib.elc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import lib.Rom;

/**
 *
 * @author gil.costa
 */
public class ItemObject {
    
    /**
        dc.b			; Character ID
        dc.b 0			; 1 = Level (scene)
        dc.b   1		; 2 = Spawn Type
        dc.b 0			; 3 = Palette/Enemy AI
        dc.b $1C		; 4 = Animation	Status bit 7 is	used for enemy that end	the stage ie bosses
        dc.b 0			; 5 = Starting Obj Mode
        dc.w $A0		; 6-7 =	Xpos Distance/Timer/Delay
        dc.w $190		; 8-9 =	XPos
        dc.w 0			; A-B =	YPos
        dc.b 2			; C = Contained item
        dc.b $E			; D = Hit Width
        dc.b $48		; E = Hit Height
        dc.b 6			; F = Vicinty
        dc.w $10		; Score
    */
    
    public long address;
    
    public int itemId;      // type of object
    public int sceneId;          // in what scene this character shows up
    public int triggerType;      // trigger by position, timer, etc
    public int minimumDifficulty;       // Minimum minimumDifficulty for it to show up
    public boolean useAlternativePalette; // if character use secondary palette
    public int enemyAgressiveness;       // ????
    public int initialState;     // Animation status
    public int introductionType; // If character falls from sky, or out of a sewer etc
    public int triggerArgument;  // Depending on the trigger, an argument can be used (e.g. timer, distance, etc)
    // Position x,y
    public int posX;
    public int posY;
    public int containedItemId;
    // Collision box x, y, z
    public int collisionWidth;
    public int collisionHeight;
    public int collisionDept;
    public int deathScore;      // Score added when enemie dies
    
    
    
    public ItemObject(RandomAccessFile rom, long address) throws IOException{
        this.address = address;
        rom.seek(address);
        itemId = rom.read();
        sceneId = rom.read();
        triggerType = rom.read();
        minimumDifficulty = triggerType >> 4 & 0x7;
        triggerType &= 0x7;      
        int palleteByte = rom.read();
        enemyAgressiveness = palleteByte & 0x7;
        useAlternativePalette = (palleteByte >> 4 & 0x3) == 1;
      
        initialState = rom.read();
        introductionType = rom.read();
        triggerArgument = rom.readUnsignedShort();
        
        posX = rom.readUnsignedShort();
        posY = rom.readUnsignedShort();
        
        containedItemId = rom.read();
        collisionWidth = rom.read();
        collisionHeight = rom.read();
        collisionDept = rom.read();
        deathScore = rom.readUnsignedShort();      
    }
    
    
    public void write(RandomAccessFile rom, long address) throws IOException{
        rom.seek(address);
        rom.writeByte(itemId);
        rom.writeByte(sceneId);
        int triggerAndDifficulty = triggerType;
        triggerAndDifficulty += (minimumDifficulty << 4) & 0xF8;
        rom.writeByte(triggerAndDifficulty);
 
        int palleteByte = enemyAgressiveness;
        if (useAlternativePalette){
            palleteByte |= 0xC;
        }
        rom.writeByte(palleteByte);
      
        rom.writeByte(initialState);
        rom.writeByte(introductionType);
        rom.writeShort(triggerArgument);
        
        rom.writeShort(posX);
        rom.writeShort(posY);
        
        rom.writeByte(containedItemId);
        rom.writeByte(collisionWidth);
        rom.writeByte(collisionHeight);
        rom.writeByte(collisionDept);
        rom.writeShort(deathScore);
    }
    
    public void write(RandomAccessFile rom) throws IOException{
        this.write(rom, address);
    }
    
    
    
    
    public static void main(String[] args){
        try {
            Rom rom = new Rom(new File("sor2.bin"));
            ItemObject obj = new ItemObject(rom.getRomFile(), 0x1F0DB4);
            obj.write(rom.getRomFile());
            rom.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
