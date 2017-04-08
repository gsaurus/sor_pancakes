/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
