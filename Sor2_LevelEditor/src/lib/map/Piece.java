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

import java.awt.Color;
import lib.map.Palette;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.util.TreeMap;
import lib.RandomDataStream;
import lib.Renderable;

/**
 *
 * @author Gil
 */
public class Piece implements Renderable{
    private Tile[] tiles;
    private TreeMap<Integer, Integer> originalOrder;
    private int width;
    
    public Piece(int width, int height){
        tiles = new Tile[width*height];
        originalOrder = new TreeMap<Integer, Integer>();
        this.width = width;
    }
    
    public void setTile(int x, int y, Tile tile){
        int index = y*width+x;
        tiles[index] = tile;
        originalOrder.put(index, originalOrder.size());
    }
    
    public Tile getTile(int x, int y){
        return tiles[y*width+x];
    } 
    
    private int getTileOrder(int x, int y){
        return originalOrder.get(y*width+x);
    }
    
    @Override
    public BufferedImage asImage(Palette Pal){
        int height = tiles.length/width;
        BufferedImage res = new BufferedImage(width*8,height*8,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = res.createGraphics();
        for (int x = 0 ; x < width ; ++x){
            for (int y = 0 ; y < height ; ++y){
                g2d.drawImage( getTile(x,y).asImage(Pal), x*8, y*8, null);
            }
        }
        return res;
    }
    
    @Override
    public BufferedImage asShadow(Color color) {
        int height = tiles.length/width;
        BufferedImage res = new BufferedImage(width*8,height*8,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = res.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width*8,height*8);
        return res;
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight(){
        return tiles.length/width;
    }

    public static Piece read(RandomAccessFile rom, long address, int width, int height) throws IOException{
       int numTiles = width*height;
       Piece piece = new Piece(width,height);
       for (int i = 0 ; i < numTiles ; ++i){
           Tile tile = Tile.read(rom, address + i * 32);
           piece.setTile(i/height, i%height, tile);
       }
       return piece;
   }
    
    public static Piece read(RandomDataStream art, int offset, int width, int height) throws IOException{
       int numTiles = width*height;
       Piece piece = new Piece(width,height);
       for (int i = 0 ; i < numTiles ; ++i){
           Tile tile = Tile.read(art, offset + i * 32);
           piece.setTile(i/height, i%height, tile);
       }
       return piece;
   }

    public void writeArt(RandomAccessFile rom, long address, BufferedImage image, Palette palette) throws IOException {
       int height = getHeight();       
       for (int x = 0 ; x < width ; ++x){
            for (int y = 0 ; y < height ; ++y){
                BufferedImage tileImage = image.getSubimage(x*8, y*8, 8, 8);
                Tile tile = getTile(x,y);
                int id = getTileOrder(x,y);
                tile.writeArt(rom, address + id * 32, tileImage, palette);
            }
        }      
    }

    public void write(RandomAccessFile rom, long address) throws IOException {
        for (int i = 0 ; i < tiles.length ; ++i){
           Tile tile = tiles[i]; //Tile.read(rom, address + i * 32);
           tile.write(rom, address + i * 32);
        }
    }
    
}
