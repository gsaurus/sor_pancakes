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

import lib.map.*;
import lib.map.Palette;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import lib.Renderable;

/**
 *
 * @author Gil
 */
public class HitFrame{
    public int x, y;
    public int sound;
    public int damage;        // 0111 1111 = 0x7F
    public boolean knockDown; // 1000 0000 = 0x80
    private boolean enabled;
    
    public HitFrame(){
        // nothing to do
    }
    
    public void setEnabled(boolean enabledFlag){
        enabled = enabledFlag;
    } 
    public boolean isEnabled(){
        return enabled;
    }
    
    
    public static HitFrame read(RandomAccessFile rom, long address)  throws IOException{
        HitFrame frame = new HitFrame();
        rom.seek(address);
        frame.x = rom.readByte();
        frame.y = rom.readByte();
        frame.damage = rom.readUnsignedByte();
        frame.knockDown = (frame.damage >> 7 & 0x1)==1;
        frame.damage = frame.damage & 0x7F;
        frame.sound = rom.readUnsignedByte();
        frame.enabled = (frame.damage > 0);
        return frame;
   }
    
    
    public void write(RandomAccessFile rom, long address)  throws IOException{       
        rom.seek(address);
        HitFrame frame;
        if (enabled) frame = this;
        else frame = new HitFrame(); // empty hitframe
        
        rom.writeByte(frame.x);
        rom.writeByte(frame.y);
        int damageByte = frame.damage;
        if (frame.knockDown){
           damageByte += 0x80;
        }
        rom.writeByte(damageByte);
        rom.writeByte(frame.sound);
   }
    
}
