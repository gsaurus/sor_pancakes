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
public class CharacterObject extends BaseObject {
    
    public static final int SIZE = 0x18;
    
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
        dc.b 2			; C = Health
        dc.b $E			; D = Hit Width
        dc.b $48		; E = Hit Height
        dc.b 6			; F = Vicinty
        dc.w $10		; Score
        dc.l Txt_Galsia		; "GALSIA  "
        dc.w $252		; VRAM address
    */
    
    public long address;
    
    public int sceneId;          // in what scene this character shows up
    public int triggerType;      // trigger by position, timer, etc
    public int minimumDifficulty;       // Minimum minimumDifficulty for it to show up
    public boolean useAlternativePalette; // if character use secondary palette
    public int enemyAgressiveness;       // ????
    public int initialState;     // Animation status
    public boolean useBossSlot;
    public int introductionType; // If character falls from sky, or out of a sewer etc
    // Flags used in spawn type
    public boolean bikerWeaponFlag;
    public int ninjaHookHeight;
    public int triggerArgument;  // Depending on the trigger, an argument can be used (e.g. timer, distance, etc)    
    public int health;
    // Collision box x, y, z
    public int collisionWidth;
    public int collisionHeight;
    public int collisionDept;
    public int deathScore;      // Score added when enemie dies
    public long nameAddress;     // Address of enemy name
    public int vram;            // We don't use this one
    
    
    
    public CharacterObject(RandomAccessFile rom, long address) throws IOException{
        this.address = address;
        rom.seek(address);
        objectId = rom.read();
        sceneId = rom.read();
        triggerType = rom.read();
        minimumDifficulty = triggerType >> 4 & 0x7;
        triggerType &= 0x7;      
        int palleteByte = rom.read();
        enemyAgressiveness = palleteByte & 0xEF;
        useAlternativePalette = ((palleteByte >> 4) & 0x1) == 1;
      
        initialState = rom.read();
        useBossSlot = ((initialState >> 7) & 0x1) == 1;
        initialState &= 0x7F;
        introductionType = rom.read();
        bikerWeaponFlag = (introductionType >> 7 & 0x1) == 1;
        ninjaHookHeight = (introductionType >> 4 & 0x7);
        introductionType &= 0x0F;
        triggerArgument = rom.readUnsignedShort();
        
        posX = rom.readUnsignedShort();
        posY = rom.readUnsignedShort();
        
        health = rom.read();
        collisionWidth = rom.read();
        collisionHeight = rom.read();
        collisionDept = rom.read();
        deathScore = rom.readUnsignedShort();
      
        nameAddress = rom.readInt();
        vram = rom.readUnsignedShort(); // Discarding vram for now..
    }
    
    
    public void write(RandomAccessFile rom, long address, int deltaObjectId) throws IOException{
        rom.seek(address);
        rom.writeByte(objectId + deltaObjectId);
        rom.writeByte(sceneId);
        int triggerAndDifficulty = triggerType;
        triggerAndDifficulty += (minimumDifficulty << 4) & 0xF8;
        rom.writeByte(triggerAndDifficulty);
 
        int palleteByte = enemyAgressiveness;
        if (useAlternativePalette){
            palleteByte |= 0x10;
        }
        rom.writeByte(palleteByte);
      
        int initialStateByte = initialState;
        if (useBossSlot){
            initialStateByte |= 0x80;
        } 
        rom.writeByte(initialStateByte);
        int introductionByte = introductionType & 0x0F;
        if (bikerWeaponFlag) introductionByte |= 0x80;
        introductionByte |= (ninjaHookHeight & 0x7) << 4;        
        rom.writeByte(introductionByte);
        rom.writeShort(triggerArgument);
        
        rom.writeShort(posX);
        rom.writeShort(posY);
        
        rom.writeByte(health);
        rom.writeByte(collisionWidth);
        rom.writeByte(collisionHeight);
        rom.writeByte(collisionDept);
        rom.writeShort(deathScore);
      
        rom.writeInt((int)nameAddress);
        rom.writeShort(vram);
    }
    
    public void write(RandomAccessFile rom) throws IOException{
        this.write(rom, address, 0);
    }
    
    
    
    
    public static void main(String[] args){
        try {
            Rom rom = new Rom(new File("sor2.bin"));
            CharacterObject obj = new CharacterObject(rom.getRomFile(), 0x1F0DB4);
            obj.write(rom.getRomFile());
            rom.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
