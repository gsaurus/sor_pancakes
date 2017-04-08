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
public class BoxFrame{
    public int x, y;
    public int w, h;
    
    public BoxFrame(){
        // nothing to do
    }   
    
    
    public static BoxFrame read(RandomAccessFile rom, long address)  throws Exception{
        BoxFrame frame = new BoxFrame();
        rom.seek(address);
        frame.x = rom.readByte();
        frame.w = rom.readByte();
        frame.y = rom.readByte();
        frame.h = rom.readByte();        
        return frame;
   }
    
    
    public void write(RandomAccessFile rom, long address)  throws Exception{       
        rom.seek(address);
        // left
        rom.writeByte(x);
        rom.writeByte(w);
        rom.writeByte(y);
        rom.writeByte(h);
        // right
        rom.writeByte(-(x+w)); // flip
        rom.writeByte(w);
        rom.writeByte(y);
        rom.writeByte(h);
   }
    
}
