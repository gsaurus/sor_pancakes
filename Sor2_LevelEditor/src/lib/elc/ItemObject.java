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
public class ItemObject extends BaseObject{
    
    public static final int SIZE = 0x12;
    
    /*
        $0   = Obj ID
        $1   = Scene to be used in
        $2   = Status Bitfield
        $3   = Hit Width
        $4   = Hit Height
        $5   = Hit Depth
        $6   = Animation
        $7   = Obj Mode
        $8-9 = vertical velocity
        $A-B = Xpos
        $C-D = Ypos
        $E-F = SpriteStatus
        $10  = Contained item ID
    */
    
    public long address;
    
    public int sceneId;
    public int status;
    public int collisionWidth;
    public int collisionHeight;
    public int collisionDept;
    public int animation;
    public int mode;
    public int verticalSpeed;
    public int spriteStatus;    
    public int containedItemId;
    
    
    public ItemObject(RandomAccessFile rom, long address) throws IOException{
        this.address = address;
        rom.seek(address);
        objectId = rom.read();
        sceneId = rom.read();
        status = rom.read();
        collisionWidth = rom.read();
        collisionHeight = rom.read();
        collisionDept = rom.read();
        animation = rom.read();
        mode = rom.read();
        verticalSpeed = rom.readUnsignedShort();
        posX = rom.readUnsignedShort();
        posY = rom.readUnsignedShort();
        spriteStatus = rom.readUnsignedShort();
        containedItemId = rom.read();    
    }
    
    
    public void write(RandomAccessFile rom, long address) throws IOException{
        rom.seek(address);
        rom.writeByte(objectId);
        rom.writeByte(sceneId);
        rom.writeByte(status);
        rom.writeByte(collisionWidth);
        rom.writeByte(collisionHeight);
        rom.writeByte(collisionDept);
        rom.writeByte(animation);
        rom.writeByte(mode);
        rom.writeShort(verticalSpeed);
        rom.writeShort(posX);
        rom.writeShort(posY);
        rom.writeShort(spriteStatus);
        rom.writeByte(containedItemId);
    }
    
    public void write(RandomAccessFile rom) throws IOException{
        this.write(rom, address);
    }
    
    
    
    
    public static void main(String[] args){
        try {
            Rom rom = new Rom(new File("sor2.bin"));
            ItemObject obj = new ItemObject(rom.getRomFile(), 0x1F00CE);
            obj.write(rom.getRomFile());
            rom.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
