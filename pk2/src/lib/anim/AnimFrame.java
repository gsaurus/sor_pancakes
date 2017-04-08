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
    public long mapAddress;
    public long artAddress;
    public int type;
    
    public AnimFrame(){
        // nothing to do
    }    
    
    private static String hx(int value){
        return Integer.toHexString(value);
    }
    
    public static int getTypeDependentSize(int type){
        if (type == 0) return 12;
        else if (type == 1) return 6;
        else if (type == 2) return 10;
        else return 0;
    }
    
    
    protected static AnimFrame readUnknownType(RandomAccessFile rom, long address) throws IOException{
        AnimFrame frame = new AnimFrame();
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();   
        int check = rom.read();
        if (check == 0x95){
            // type 0 (uncompressed)
            frame.type = 0;
            int a0,a1,a2;
            a0 = rom.readByte();
            rom.skipBytes(1); a1 = rom.readByte();
            rom.skipBytes(1); a2 = rom.readByte();
            
            frame.artAddress = (a0 & 0xFF) + ((a1 << 8)&0xFF00) + ((a2 << 16)&0xFF0000);
            frame.artAddress *= 2;
        }else{
            // type 1 (compressed)
            frame.type = 1;
            frame.artAddress = providedArtAddress;
            rom.seek(address+6);
        }
        
        return frame;
    }
    
    protected static AnimFrame readTypeZero(RandomAccessFile rom, long address) throws IOException{
        AnimFrame frame = new AnimFrame();     
        frame.type = 0;
        rom.seek(address);        
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();
        
        int a0,a1,a2;
        rom.skipBytes(1); a0 = rom.readByte();
        rom.skipBytes(1); a1 = rom.readByte();
        rom.skipBytes(1); a2 = rom.readByte();
                
        frame.artAddress = (a0 & 0xFF) + ((a1 << 8)&0xFF00) + ((a2 << 16)&0xFF0000);
        frame.artAddress *= 2;
        
        return frame;
    }
    
    protected static AnimFrame readTypeOne(RandomAccessFile rom, long address) throws IOException{
        AnimFrame frame = new AnimFrame();
        frame.type = 1;
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();   
        frame.artAddress = providedArtAddress;
        return frame;
    }
    
    protected static AnimFrame readTypeTwo(RandomAccessFile rom, long address) throws IOException{
        AnimFrame frame = new AnimFrame();
        frame.type = 2;
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();
        frame.artAddress = rom.readInt();        
        return frame;
    }
    
    
    public static AnimFrame read(RandomAccessFile rom, long address, int type)  throws IOException{
        if (type == 2) return readTypeTwo(rom, address);
//        if (type == 1) return readTypeOne(rom, address);
        else if (type == 0)return readTypeZero(rom, address);
        // type 1 may be mixed up (Mr. X)
        return readUnknownType(rom, address);
        
   }
    
    public void write(RandomAccessFile rom, long address)  throws IOException{        
        write(rom, address, type);
   }
    
    
    public void write(RandomAccessFile rom, long address, int type)  throws IOException{
        
        rom.seek(address);
        rom.writeShort(delay);
        rom.writeInt((int)mapAddress);
        
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
        // there is no else, type 1 does not have art address
   }
    
}
