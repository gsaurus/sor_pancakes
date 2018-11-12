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
package lib.map;

import lib.map.Palette;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import lib.RandomDataStream;
import lib.Renderable;

/**
 *
 * @author Gil
 */
public class Tile implements Renderable{
    private byte[] mx;
    private static boolean showBackground = false;
    
    public static boolean isShowingBackground(){
        return showBackground;
    }
    
    public static void setShowBackground(boolean show){
        showBackground = show;
    }
    
    public Tile(){
        mx = new byte[64];
    }
    
    public byte getPixel(int x, int y){
        return mx[y*8+x];
    }
    
    public void setPixel(int x, int y, byte val){
        mx[y*8+x] = val;
    }
    
    @Override
    public BufferedImage asImage(Palette pal){
        BufferedImage res = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
        final int background = new Color(0,0,0,0).getRGB();
        for (int y = 0 ; y < 8 ; ++y){
            for (int x = 0 ; x < 8 ; ++x){
                final byte index = getPixel(x,y);
                if (!showBackground && index == 0)
                    res.setRGB(x, y, background);
                else res.setRGB(x, y, pal.getColor(index));
            }
        }
        return res;
    }
    
    @Override
    public BufferedImage asShadow(Color color) {
        BufferedImage res = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = res.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, 8, 8);
        return res;
    }

    public static Tile read(RandomAccessFile rom, long address)  throws IOException{
        Tile tile = new Tile();
        rom.seek(address);
        byte b;
        int x, y;
        for (int i = 0 ; i <64 ; i+=2){
            b = rom.readByte();
            x = i%8;
            y = i/8;
            tile.setPixel(x,y,(byte)((b >>> 4) & 0xF));
            tile.setPixel(x+1,y,(byte)(b & 0xF));
        }
        return tile;
   }   
    
    
    public static Tile read(RandomDataStream art, int address)  throws IOException{
        Tile tile = new Tile();
        art.seek(address);
        byte b;
        int x, y;
        for (int i = 0 ; i <64 ; i+=2){
            b = art.read();
            x = i%8;
            y = i/8;
            tile.setPixel(x,y,(byte)((b >>> 4) & 0xF));
            tile.setPixel(x+1,y,(byte)(b & 0xF));
        }
        return tile;
   }
    
    
    private int getPixelValue(int x, int y, BufferedImage img, Palette pal){
        int value = img.getRGB(x, y);
        for (int i = 15 ; i >= 0 ; --i){
            if (value == pal.getColor(i)) return i;
        }
        return 0;
    }

    public void writeArt(RandomAccessFile rom, long address, BufferedImage image, Palette palette) throws IOException {
        rom.seek(address);
        byte b;
        int x, y;
        for (int i = 0 ; i < 64 ; i += 2){
            int p1 = getPixelValue(i%8,i/8, image, palette);
            int j = i+1;
            int p2 = getPixelValue(j%8,j/8, image, palette);
            rom.writeByte( ((p1 << 4)&0xF0) | (p2&0xF) );
        }
    }

    public void write(RandomAccessFile rom, long address) throws IOException {
        rom.seek(address);
        byte b;
        int x, y;
        for (int i = 0 ; i <64 ; i+=2){
            x = i%8;
            y = i/8;
            rom.write(getPixel(x,y));
            rom.write(getPixel(x+1,y));
        }
    }

}
