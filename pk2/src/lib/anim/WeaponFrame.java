/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.anim;

import java.io.IOException;
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
    
    
    public static WeaponFrame read(RandomAccessFile rom, long address)  throws IOException{
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
    
    
    public void write(RandomAccessFile rom, long address)  throws IOException{       
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
