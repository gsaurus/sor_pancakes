/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.map;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import lib.Rom;

/**
 *
 * @author Gil
 */
public class Palette{
    private int[] colors;
    
    public static int correct(int value){
        switch (value){
            case 0: return 0;
            case 17: return 0; // !!!
            case 34: return 32;
            case 51: return 32;
            case 68: return 74;
            case 85: return 74;
            case 102: return 106;
            case 119: return 106;
            case 136: return 148;
            case 153: return 148;
            case 170: return 189;
            case 187: return 189;
            case 204: return 222;
            case 221: return 222;
            case 238: return 255;
            case 255: return 255;
        }
        return 0;
    }
    
    private static int hexToVal(int val) {
        return val*17;
    }    
    private static int convertToRgb(short gensColor){
        int r, g, b;
        r = gensColor & 0xF;
        g = (gensColor >>> 4) & 0xF;
        b = (gensColor >>> 8) & 0xF;
        r = correct(hexToVal(r));
        g = correct(hexToVal(g));
        b = correct(hexToVal(b));
        return new java.awt.Color(r,g,b).getRGB();
    }
    
    public Palette(){
         colors = new int[16];
    }
    
    public void setColor(int index, int color){
        colors[index] = color;
    }
    
     public int getColor(int index){
        return colors[index];
    }

    public static Palette read(RandomAccessFile rom, long address) throws IOException {
        Palette pal = new Palette();
        rom.seek(address);
        for (int i = 0 ; i < 16 ; ++i){
            short gensColor = rom.readShort();
            pal.setColor(i, convertToRgb(gensColor));
        }
        return pal;
    }
    
    
    public void write(RandomAccessFile rom, long address) throws Exception {
        rom.seek(address);
        for (int i = 0 ; i < 16 ; ++i){
            rom.writeShort(colors[i]);            
        }
    }
    
    
    public static boolean isPalette(RandomAccessFile rom, long address) {
        if (address < Rom.ROM_START_ADDRESS) return false;
        try {
            rom.seek(address);
            for (int i = 0 ; i < 16 ; ++i){
                if (((rom.readShort() >>> 12)&0xF) > 0 )
                    return false;
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
   
    
}
