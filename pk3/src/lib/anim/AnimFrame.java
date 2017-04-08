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

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Gil
 */
public class AnimFrame{
    
    public static long providedArtAddress;
    
    public int delay;
    public int attackId;
    public int damage;
    public boolean knockDown;
    public boolean reverse;
    public boolean flipedX;
    public int collisionId;
    public long mapAddress;
    public long artAddress;
    public int type;
    
    public AnimFrame(){
        // nothing to do
    }    
    
    
    public static int getTypeDependentSize(int type){
        if (type == 0) return 14;
        else if (type == 1) return 8;
        else return 0;
    }
    
    
    public static AnimFrame read(RandomAccessFile rom, long address)  throws Exception{
//        if (type == 1) return readTypeOne(rom, address);
//        return readTypeZero(rom, address);
        AnimFrame frame = new AnimFrame();     
        rom.seek(address);
        
        // delay & map        
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();        
        // odd addresses = flipped x
        if (frame.mapAddress%2 != 0){
            frame.mapAddress--;  
            frame.flipedX = true;
        }
        
        // collision
        frame.attackId = rom.readByte();
        frame.damage = rom.readByte();
        frame.knockDown = (frame.damage >> 7 & 0x1)==1;
        frame.reverse = (frame.damage >> 6 & 0x1)==1;
        frame.damage = frame.damage & 0x3F;
        
        // possibly art address
        byte firstByte = rom.readByte();
        if ((firstByte&0xFF) == 0x95){
            frame.type = 0; // uncompressed
            int a0,a1,a2;
                              a0 = rom.readByte();
            rom.skipBytes(1); a1 = rom.readByte();
            rom.skipBytes(1); a2 = rom.readByte();                
            if ((a2&0xFF) > 0x40){ // somewhere close to rom limit
//                System.out.println("fixing weird address: " + Integer.toHexString((a2&0xFF)));
                a2 = 0x0A; // this is a weird fix for what is in here: 12C186
            }
            frame.artAddress = (a0 & 0xFF) + ((a1 << 8)&0xFF00) + ((a2 << 16)&0xFF0000);
            frame.artAddress *= 2;
        }else{
            frame.type = 1; // compressed
        }
        
        // read collision Id from the spritemap script
        rom.seek(frame.mapAddress+2);
        frame.collisionId = rom.readByte();
        
        return frame;
   }
    
    public void reloadCollisionId(RandomAccessFile rom) throws IOException{
        // read collision Id from the spritemap script
        rom.seek(mapAddress+2);
        collisionId = rom.readByte();
    }
    
    
    
    public void write(RandomAccessFile rom, long address)  throws Exception{        
        write(rom, address, type);
   }
    
    
    public void write(RandomAccessFile rom, long address, int type)  throws Exception{
        
        rom.seek(address);
        rom.writeShort(delay);
        long toWrite = mapAddress;
        if (flipedX) toWrite++;
        rom.writeInt((int)toWrite);
        
        // collision
        rom.writeByte(attackId);
        int finalDamage = (damage&0x3F) | (knockDown?0x80:0) | (reverse?0x40:0);
        rom.writeByte(finalDamage);
        
        if (type == 0){
        
            long tempArtAddress = artAddress/2;

            int a0,a1,a2;
            a0 = (int)(tempArtAddress & 0xFF);
            a1 = (int)((tempArtAddress >> 8) & 0xFF);
            a2 = (int)((tempArtAddress >> 16) & 0xFF);       

            rom.writeByte(0x95); rom.writeByte(a0);
            rom.writeByte(0x96); rom.writeByte(a1);
            rom.writeByte(0x97); rom.writeByte(a2);
        }
        else if (type == 2){
            rom.writeInt((int)artAddress);
        }
        // there is no else, type 1 (compressed) does not have art address
        
        // write collision Id on the spritemap script
        rom.seek(mapAddress+2);
        rom.writeByte(collisionId);
   }
    
    
    

    public boolean isAttackEnabled() {
        return attackId > 0 && damage > 0;
    }
    
}
