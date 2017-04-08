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

import java.lang.Exception;
import java.io.RandomAccessFile;

/**
 *
 * @author Gil
 */
public class WeaponFrame{
    public int x, y;
    public int angle;
    public boolean showBehind;
    public int unusedByte;
    private boolean isEnabled;
    
    public WeaponFrame(){
        // nothing to do
    }
    
    public boolean isEnabled(){
        return isEnabled;
    }
    
    public void setEnabled(boolean enabled){
        isEnabled = enabled;
    }
    
    
    public static WeaponFrame read(RandomAccessFile rom, long address)  throws Exception{
        WeaponFrame frame = new WeaponFrame();
        rom.seek(address);
        frame.x = rom.readByte();
        frame.y = rom.readByte();
        frame.angle = rom.readByte();
        frame.unusedByte = rom.readByte();
        frame.isEnabled = frame.angle >= 0;
        frame.showBehind = frame.angle > 7;
        frame.angle%=8;
        if (!frame.isEnabled) frame.angle = 0;
        return frame;
   }
    
    
    public void write(RandomAccessFile rom, long address)  throws Exception{       
        rom.seek(address); 
        WeaponFrame frame;
        if (isEnabled) frame = this;
        else{
            frame = new WeaponFrame();
            frame.angle = -1;
        }
        rom.writeByte(frame.x);
        rom.writeByte(frame.y);
        if (frame.showBehind)
            rom.writeByte(frame.angle+8);
        else rom.writeByte(frame.angle);
        rom.writeByte(frame.unusedByte);
   }
    
}
