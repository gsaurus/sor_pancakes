/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.map;

import java.awt.Color;
import lib.map.Piece;
import lib.map.Palette;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.util.ArrayList;
import lib.RandomDataStream;
import lib.Renderable;




//class SpriteFinalPiece{    
//    public int x, y;
//    public boolean xFliped, yFliped;
//    public Piece tiledPiece;
//    public int spriteIndex;
//
//    SpriteFinalPiece(int x, int y, Piece piece, boolean xFliped, boolean yFliped, int spriteIndex) {
//        this.x = x; this.y = y;
//        this.spriteIndex = spriteIndex;
//        tiledPiece = piece;
//        this.xFliped = xFliped; this.yFliped = yFliped;
//    }
//}


/**
 *
 * @author Gil
 */
public class Sprite implements Renderable{
    private static final int ACCEPTABLE_PIECES = 128;


    private ArrayList<SpritePiece> pieces;
    private ArrayList<Piece> tiledPieces;
    private int numTiles;
    
    public Sprite(int numPieces){
        pieces = new ArrayList<SpritePiece>(numPieces);
        tiledPieces = new ArrayList<Piece>(numPieces);
    }
    
    public Sprite(){
        pieces = new ArrayList<SpritePiece>();
        tiledPieces = new ArrayList<Piece>();
    }
    
    public void setNumTiles(int numTiles){
        this.numTiles = numTiles;
    }
    
    
    public void addPiece(SpritePiece piece, Piece tiledPiece){
        //final SpriteFinalPiece finalPiece = new SpriteFinalPiece(x, y, piece, isFlipedX, isFlipedY, spriteIndex);
        pieces.add(piece);
        tiledPieces.add(tiledPiece);
    }
    
    // Flip the image
    private static BufferedImage flip(BufferedImage image, boolean horizontaly, boolean verticaly){        
        int h = 1, v = 1;
        int th = 0, tv = 0;
        if (horizontaly){
            h = -1;
            th = -image.getWidth();
        }
        if (verticaly){
            v = -1;
            tv = -image.getHeight();
        }
        AffineTransform tx = AffineTransform.getScaleInstance(h, v);
        tx.translate(th, tv);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    

    @Override
    public BufferedImage asImage(Palette Pal) {
        BufferedImage res = new BufferedImage(256,256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = res.createGraphics();
        for (int i = pieces.size()-1 ; i >= 0 ; --i){
            SpritePiece piece = pieces.get(i);
            Piece tiledPiece = tiledPieces.get(i);
            BufferedImage pieceImg = tiledPiece.asImage(Pal);
            if (piece.xFliped || piece.yFliped){
                pieceImg = flip(pieceImg,piece.xFliped, piece.yFliped);
            }
            g2d.drawImage(pieceImg, 128+piece.xL, 128+piece.y, null);
        }
        return res;
    }
    
    @Override
    public BufferedImage asShadow(Color color) {
        BufferedImage res = new BufferedImage(256,256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = res.createGraphics();
        for (int i = pieces.size()-1 ; i >= 0 ; --i){
            SpritePiece piece = pieces.get(i);
            Piece tiledPiece = tiledPieces.get(i);
            BufferedImage pieceImg = tiledPiece.asShadow(color);
            if (piece.xFliped || piece.yFliped){
                pieceImg = flip(pieceImg,piece.xFliped, piece.yFliped);
            }
            g2d.drawImage(pieceImg, 128+piece.xL, 128+piece.y, null);
        }
        return res;
    }
    
    public static Sprite read(RandomAccessFile rom, long mapAddress, RandomDataStream art) throws IOException{        
       rom.seek(mapAddress);
       // read number of pieces
       int numPieces = rom.readUnsignedShort() + 1;
       if (numPieces > ACCEPTABLE_PIECES) throw new IOException("Bad map address");
       Sprite sprite = new Sprite(numPieces);
       
       // read num tiles
        int a0,a1;
        rom.skipBytes(1); a0 = rom.readByte();
        rom.skipBytes(1); a1 = rom.readByte();                
        sprite.numTiles = ((a1 & 0xFF) + ((a0 << 8)&0xFF00))/16;
       
       // read each piece
       for (int i = 0 ; i < numPieces ; ++i){
           rom.seek(mapAddress+6+i*6);
           SpritePiece p = new SpritePiece();
           // read position
           p.xR = rom.readByte();
           p.xL = rom.readByte();
           p.y  = rom.readByte();
           // read Width/Height
           byte wh = rom.readByte();
           p.width = (wh >>> 2) & 0x3;
           p.height = wh & 0x3;
           // read flags and index
           short tail = rom.readShort();
           p.priorityFlag = ((tail >>> 15) & 0x1) == 1;
           p.paletteLine = ((tail >>> 13) & 0x3);
           p.yFliped = ((tail >>> 12) & 0x1) == 1;
           p.xFliped = ((tail >>> 11) & 0x1) == 1;
           p.spriteIndex = (tail & 0x7FF);  
           
           // Load piece and ddd piece
           Piece piece = Piece.read(art, p.spriteIndex*32, p.width+1, p.height+1);
           sprite.addPiece(p, piece);
       }
       
       return sprite;
   }    
    
    public static Sprite read(RandomAccessFile rom, long mapAddress, long artAddress) throws IOException{
        rom.seek(mapAddress);
        
        // read number of pieces
        int numPieces = rom.readUnsignedShort() + 1;
        if (numPieces > ACCEPTABLE_PIECES) throw new IOException("Bad map address");
        Sprite sprite = new Sprite(numPieces);
        
        // read num tiles
        int a0,a1;
        rom.skipBytes(1); a0 = rom.readByte();
        rom.skipBytes(1); a1 = rom.readByte();                
        sprite.numTiles = ((a1 & 0xFF) + ((a0 << 8)&0xFF00))/16;
        
        // read each piece
        for (int i = 0 ; i < numPieces ; ++i){
            rom.seek(mapAddress+6+i*6);
            SpritePiece p = new SpritePiece();
            // read position
            p.xR = rom.readByte();
            p.xL = rom.readByte();
            p.y  = rom.readByte();
            // read Width/Height
            byte wh = rom.readByte();
            p.width = (wh >>> 2) & 0x3;
            p.height = wh & 0x3;
            // read flags and index
            short tail = rom.readShort();
            p.priorityFlag = ((tail >>> 15) & 0x1) == 1;
            p.paletteLine = ((tail >>> 13) & 0x3);
            p.yFliped = ((tail >>> 12) & 0x1) == 1;
            p.xFliped = ((tail >>> 11) & 0x1) == 1;
            p.spriteIndex = (tail & 0x7FF);    
           
            // Load piece and ddd piece
            Piece piece = Piece.read(rom, artAddress + p.spriteIndex*32, p.width+1, p.height+1);
            sprite.addPiece(p, piece);
       }
       
       return sprite;
   }
    
    
   public void write(RandomAccessFile rom, long mapAddress, long artAddress) throws IOException{
       rom.seek(mapAddress);
       
       // write number of pieces
       rom.writeShort(pieces.size()-1);
       
       // write num tiles
       long tempSize = numTiles*16;
       int a0,a1;
       a0 = (int)(tempSize & 0xFF);
       a1 = (int)((tempSize >> 8) & 0xFF);
       rom.write(0x94); rom.write(a1);
       rom.write(0x93); rom.write(a0);       
       
       // write each piece
       for (int i = 0 ; i < pieces.size() ; ++i){
           rom.seek(mapAddress+6+i*6);
           SpritePiece p = pieces.get(i);
           // position
           rom.write(p.xR);
           rom.write(p.xL);
           rom.write(p.y);
           // Width/Height
           int wh = ((p.width << 2) & 0xC) | (p.height & 0x3);
           rom.write(wh);
           // flags and index
           int pf = p.priorityFlag?1:0;
           int xf = p.xFliped?1:0;
           int yf = p.yFliped?1:0;
           int tail = ( pf << 15)
                    | ((p.paletteLine&0x3) << 13 )
                    | ( yf << 12)
                    | ( xf << 11)
                    | p.spriteIndex&0x7FF
           ;
           rom.writeShort(tail);
           
           // Write piece
           Piece piece = tiledPieces.get(i);
           piece.write(rom, artAddress + p.spriteIndex*32);
       }
   }
   
   public void writeSpriteOnly(RandomAccessFile rom, long mapAddress) throws IOException{
       rom.seek(mapAddress);
       
       // write number of pieces
       rom.writeShort(pieces.size()-1);
       
       // write num tiles
       long tempSize = numTiles*16;
       int a0,a1;
       a0 = (int)(tempSize & 0xFF);
       a1 = (int)((tempSize >> 8) & 0xFF);
       rom.write(0x94); rom.write(a1);
       rom.write(0x93); rom.write(a0);       
       
       // write each piece
       for (int i = 0 ; i < pieces.size() ; ++i){
           rom.seek(mapAddress+6+i*6);
           SpritePiece p = pieces.get(i);
           // position
           rom.write(p.xR);
           rom.write(p.xL);
           rom.write(p.y);
           // Width/Height
           int wh = ((p.width << 2) & 0xC) | (p.height & 0x3);
           rom.write(wh);
           // flags and index
           int pf = p.priorityFlag?1:0;
           int xf = p.xFliped?1:0;
           int yf = p.yFliped?1:0;
           int tail = ( pf << 15)
                    | ((p.paletteLine&0x3) << 13 )
                    | ( yf << 12)
                    | ( xf << 11)
                    | p.spriteIndex&0x7FF
           ;
           rom.writeShort(tail);                      
       }
   }
    

   public void writeArt(RandomAccessFile rom, long artAddress, BufferedImage image, Palette palette) throws IOException {
       // rewrite each piece art
       int numPieces = pieces.size();
       for (int i = 0 ; i < numPieces ; ++i){
           SpritePiece p = pieces.get(i);
           int x = 128 + p.xL;
           int y = 128 + p.y;
           Piece tp = tiledPieces.get(i);
           int width = tp.getWidth()*8;
           int height = tp.getHeight()*8;
           BufferedImage tileImage = image.getSubimage(x, y, width, height);
           if (p.xFliped || p.yFliped){
                tileImage = flip(tileImage,p.xFliped, p.yFliped);
            }
           tp.writeArt(rom, artAddress + p.spriteIndex*32, tileImage, palette);
       }
    }

    public long getNumPieces() {
        return pieces.size();
    }

    public void applyOffset(int deltaX, int deltaY) {
        for (SpritePiece piece:pieces){
            piece.xL+=deltaX;
            piece.xR-=deltaX;
            piece.y+=deltaY;
        }
    }
    
    public Rectangle getBounds(){
        Rectangle res = new Rectangle();
        res.x = Integer.MAX_VALUE;
        res.y = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;
        for (SpritePiece piece:pieces){
            int x = piece.xL+128;
            int y = piece.y+128;
            int r = (x+(piece.width+1)*8);
            int b =  y+(piece.width+1)*8;
            if (x < res.x) res.x = x;
            if (y < res.y) res.y = y;
            if (r > right) right = r;
            if (b > bottom) bottom = b;            
        }
        res.width = right-res.x;
        res.height = bottom-res.y;
        return res;
    }
    
    
}
