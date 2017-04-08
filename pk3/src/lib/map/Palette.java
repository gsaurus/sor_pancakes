/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.lang.Exception;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import lib.Rom;

/**
 *
 * @author Gil
 */
public class Palette{

    public static Color correct(Color color) {
        return new Color(correct(color.getRed()),correct(color.getGreen()),correct(color.getBlue()) );
    }

    public static int correctRGB(int rgb) {
        Color res = correct(new Color(rgb));
        return res.getRGB();
    }
    private int[] colors;
    private boolean type2;
    private boolean bit0;
    private boolean bit4;
    private boolean bit8;
    
    public static int correct(int value){
        value = (value/17)*17;
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
    
    private static int convertToGens(int rgb){
        Color c = new Color(rgb);
        int b = correct(c.getBlue())/17;
        int g = correct(c.getGreen())/17;
        int r = correct(c.getRed())/17;
        int value = r | (g<<4) | (b<<8);
//        System.out.println(Integer.toHexString(value));
        return value;
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

    
     public static Palette read(RandomAccessFile rom, long address) throws Exception {
        Palette pal = new Palette();
        rom.seek(address);
        short[] values = new short[16];
        int limit = 16;
        for (int i = 0 ; i < limit ; ++i){
            short sh = rom.readShort();
            int id = ((sh>>12)&0xF);
            if (id>0){
                values[id] = sh;
                limit = 15;
                if (!pal.type2){
                    pal.type2 = true;
                    pal.bit0 = (sh&0x1) == 1;
                    pal.bit4 = ((sh>>4)&0x1) == 1;
                    pal.bit8 = ((sh>>8)&0x1) == 1;
                }
            }
            else values[i] = sh;              
        }
        for (int i = 0 ; i < 16 ; ++i){
            short gensColor = values[i];
            pal.setColor(i, convertToRgb(gensColor));
        }
        return pal;
    }
     
     
      public void write(RandomAccessFile rom, long address) throws Exception {
        rom.seek(address);
        int i = 0;
        if (type2) i = 1;
        for ( ; i < 16 ; ++i){
            int value = convertToGens(colors[i]);
            if (type2){
                value |= (i<<12);  
                if (bit0) value |= 1;
                else value &= ~1;
                if (bit4) value |= (1<<4);
                else value &= ~(1<<4);
                if (bit8) value |= (1<<8);
                else value &= ~(1<<8);
                System.out.print(Integer.toHexString(value) + " ");
            }
            rom.writeShort(value);            
        }
    }              
   
    
}
