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
package lib;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.IOException;
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
    private boolean[] assigned;
    private boolean type2;
    private boolean[] bit0;
    private boolean[] bit4;
    private boolean[] bit8;
    private int[] originalOrder;
    private int type;
    
    public static int correct(int value){
        value = Math.round(value/17.f)*17;
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
    public static int convertToRgb(short gensColor){
        int r, g, b;        
        r = gensColor & 0xF;
        g = (gensColor >>> 4) & 0xF;
        b = (gensColor >>> 8) & 0xF;
        r = correct(hexToVal(r));
        g = correct(hexToVal(g));
        b = correct(hexToVal(b));
        return new java.awt.Color(r,g,b).getRGB();
    }
    
    public static int convertToGens(int rgb){
        Color c = new Color(rgb);
        int b = Math.round(correct(c.getBlue())/17.f);
        int g = Math.round(correct(c.getGreen())/17.f);
        int r = Math.round(correct(c.getRed())/17.f);
        int value = r | (g<<4) | (b<<8);
        return value;
    }
    
    public Palette(int type){
         this.type = type;
         colors = new int[16];
         assigned = new boolean[16];
         originalOrder = new int[16];
         bit0 = new boolean[16];
         bit4 = new boolean[16];
         bit8 = new boolean[16];
    }
    
    public void setColor(int index, int color){
        if (assigned[index])
            colors[index] = color;
    }
    
     public int getColor(int index){
        return colors[index];
    }

    
     public static Palette read(RandomAccessFile rom, long address, int type) throws IOException {
        Palette pal = new Palette(type);
        rom.seek(address);
        short[] values = new short[16];
        int limit = 16;
        int highest = 0;
        for (int i = 0 ; i < limit ; ++i){
            short sh = rom.readShort();
            int id = ((sh>>12)&0xF);
            if (id == 0 && limit == 15){
                break;
            }
            if (id>0){
                if (id > highest) highest = id;
                // check assigned, and also the particular case of sor1 twins pal 2
                if (pal.assigned[id] || (address == 0x2f546 && id == 8)){
                    break;
                }
                pal.originalOrder[i] = id;
                values[id] = sh;                
                limit = 15;
                pal.type2 = true; 
                if (pal.type == 1){
                    pal.bit0[id] = (sh&0x1) == 1;
                    pal.bit4[id] = ((sh>>4)&0x1) == 1;
                    pal.bit8[id] = ((sh>>8)&0x1) == 1;
                }
                pal.assigned[id] = true;
            }
            else{
                values[i] = sh;
                if (pal.type == 1){
                    pal.bit0[i] = (sh&0x1) == 1;
                    pal.bit4[i] = ((sh>>4)&0x1) == 1;
                    pal.bit8[i] = ((sh>>8)&0x1) == 1;
                }
                pal.assigned[i] = true;
            }  
        }
        for (int i = 0 ; i < 16 ; ++i){
            short gensColor = values[i];
            pal.setColor(i, convertToRgb(gensColor));
        }
        return pal;
    }
     
     
      public void write(RandomAccessFile rom, long address) throws IOException {
        rom.seek(address);
        int i = 0;
        if (type2) i = 1;
//        System.out.print("Write: ");
        for ( ; i < 16 ; ++i){                
            int id = i;
            if (type2){
                id = originalOrder[i-1];
            }
            if (assigned[id]){
                int value = convertToGens(colors[id]);
                if (type2){
                    value |= (id<<12);                  
                }  
                if (type == 1){
                    if (bit0[id]) value |= 1;
                    else value &= ~1;
                    if (bit4[id]) value |= (1<<4);
                    else value &= ~(1<<4);
                    if (bit8[id]) value |= (1<<8);
                    else value &= ~(1<<8);
                }
    //            System.out.print(Integer.toHexString(value) + " ");
                rom.writeShort(value);
            }
        }
//        System.out.println();
    }              
   
    
}
