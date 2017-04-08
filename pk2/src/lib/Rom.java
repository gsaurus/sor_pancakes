/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.image.BufferedImage;
import java.io.*;
import lib.anim.AnimFrame;
import lib.anim.Animation;
import lib.anim.Character;
import lib.map.*;

/**
 *
 * @author Gil
 */
public class Rom {
    public static final int ROM_START_ADDRESS = 512;
    private RandomAccessFile rom;
     
    private void fixChecksum() throws IOException{
        short checksum = 0;
        rom.seek(ROM_START_ADDRESS);
        byte[] bytes = new byte[4096];
        int pointer = ROM_START_ADDRESS;
        while(pointer < rom.length()){            
            int readBytes = rom.read(bytes);
            pointer += readBytes;
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(is);
            for (int i = 0 ; i < readBytes-1 ; i+=2){
                checksum += dis.readShort();
            }
        }
        rom.seek(398);
        rom.writeShort(checksum);
        rom.seek(420);
        rom.writeInt((int)rom.length()-1);
    }
    
    public void close() throws IOException {
        fixChecksum();
        rom.close();
    }
    
    public Rom(File file) throws FileNotFoundException{
        rom = new RandomAccessFile(file,"rw");
    }
    public Palette readPalette(long address) throws IOException {
        return Palette.read(rom,address);
    }
    public Tile readTile(long address) throws IOException {
        return Tile.read(rom,address);
    }
    public Piece readPiece(long address, int width, int height) throws IOException {
        return Piece.read(rom,address, width, height);
    }
    public Sprite readSprite(long spriteAddress, long artAddress) throws IOException {
        return Sprite.read(rom,spriteAddress, artAddress);
    }
    public Sprite readCompressedSprite(long spriteAddress, long artAddress) throws IOException {
        RandomDataStream art = Decompressor.run(rom, artAddress);
//        RandomDataStream art = SillyDecompressor.run(romName, artAddress);
        return Sprite.read(rom,spriteAddress, art);
    }    
    public AnimFrame readAnimFrame(long address, int animsType) throws IOException {
        return AnimFrame.read(rom,address, animsType);
    }
    public Animation readAnimation(long address, int animsType) throws IOException {
        return Animation.read(rom, address, animsType);
    }
    public lib.anim.Character readCharacter(long animsAddress, long hitsAddress, long weaponsAddress, int id, int count, int animsType) throws IOException {
        return lib.anim.Character.read(rom, animsAddress, hitsAddress, weaponsAddress, id, count, animsType);
    }
    
    
    public void writeTileArt(Tile tile, long address, BufferedImage image, Palette palette) throws IOException {
        tile.writeArt(rom, address, image, palette);
    }
    public void writePieceArt(Piece piece, long address, BufferedImage image, Palette palette) throws IOException {
        piece.writeArt(rom, address, image, palette);
    }
    public void writeSpriteArt(Sprite sprite, long artAddress, BufferedImage image, Palette palette) throws IOException {
        sprite.writeArt(rom, artAddress, image, palette);
    }
    
    public void writeTile(Tile tile, long address) throws IOException {
        tile.write(rom,address);
    }
    public void writePiece(Piece piece, long address) throws IOException {
        piece.write(rom,address);
    }
    public void writeSprite(Sprite sprite, long mapAddress, long artAddress) throws IOException {
        sprite.write(rom, mapAddress, artAddress);
    }
    public void writeSpriteOnly(Sprite sprite, long mapAddress) throws IOException {
        sprite.writeSpriteOnly(rom, mapAddress);
    }
    
    
    public void writeAnimFrame(AnimFrame frame, long address) throws IOException {
        frame.write(rom,address);
    }
    public void writeAnimation(Animation anim, long address) throws IOException {
        anim.write(rom, address);
    }
    public void writeCharacter(lib.anim.Character ch, long animsAddress, long hitsAddress, long weaponsAddress, int id) throws IOException {
        ch.write(rom, animsAddress, hitsAddress, weaponsAddress, id);        
    } 
    
    public void writeCharacterSprites(lib.anim.Character ch, Palette palette) throws IOException{
        int numAnims = ch.getNumAnimations();
        for (int i = numAnims-1 ; i >=0 ; --i){
            Animation anim = ch.getAnimation(i);
            if (anim.wasArtModified() && !anim.isCompressed()){
                int framesCount = anim.getNumFrames();
                for (int j = 0 ; j < framesCount ; ++j){
                    if (anim.wasSpriteModified(j)){
                        AnimFrame frame = anim.getFrame(j);
                        long mapAddress = frame.mapAddress;
                        long artAddress = frame.artAddress;
                        // read original sprite
                        Sprite originalSprite = readSprite(mapAddress, artAddress);
                        // write new sprite art
                        writeSpriteArt(originalSprite, artAddress, anim.getImage(j),palette);
                    }
                }
                anim.spritesNotModified();
            }
        }
    }
    
    public long length() throws IOException{
        return rom.length();
    }

    int readSpeed(int speedAddress) throws IOException {
        rom.seek(speedAddress);
        return (rom.readUnsignedShort()>>4)&0x0FFF;
    }

    void writeSpeed(int speedAddress, int newSpeed) throws IOException {
        rom.seek(speedAddress);
        rom.writeShort((newSpeed<<4)&0xFFF0);
    }
    
    String readName(int speedAddress) throws IOException {
        rom.seek(speedAddress);
        char[] res = new char[5];
        for (int i = 0 ; i < 5 ; ++i){
            int b = rom.readUnsignedByte();
            switch (b){
                case 0x20: res[i] = ' '; break;
                case 0x21: res[i] = '!'; break;
                case 0x22: res[i] = '"'; break;
                case 0x27: res[i] = '\''; break;
                case 0x28: res[i] = '('; break;
                case 0x29: res[i] = ')'; break;
                case 0x2C: res[i] = ','; break;
                case 0x2E: res[i] = '.'; break;    
                case 0x2F: res[i] = '/'; break;
                case 0x3A: res[i] = '*'; break;
                case 0x3F: res[i] = '?'; break;
                case 0x40: res[i] = '©'; break;
                default:
                    if (b >= 0x30 && b <= 0x39)
                        res[i] = (char)('0'+ (b - 0x30));
                    else res[i] = (char)('A'+ (b - 0x41));
            }
            
        }
        return new String(res);
    }

    void writeName(int speedAddress, String newName) throws IOException {
        rom.seek(speedAddress);
        for (int i = 0 ; i < 5 && i < newName.length() ; ++i){
            char c = newName.charAt(i);
            byte b = 0;
            switch (c){
                case ' ': b = 0x20; break;
                case '!': b = 0x21; break;
                case '"': b = 0x22; break;
                case '\'': b = 0x27; break;
                case '(': b = 0x28; break;
                case ')': b = 0x29; break;
                case ',': b = 0x2C; break;
                case '.': b = 0x2E; break;
                case '/': b = 0x2F; break;
                case '*': b = 0x3A; break;
                case '?': b = 0x3F; break;
                case '©': b = 0x40; break;
                default:
                    if (c >= '0' && c <= '9')
                        b = (byte)(0x30 + (c-'0'));
                    else b = (byte)(0x41 + (c-'A'));
            }
            rom.writeByte( b );
        }
    }

    void writePortrait(long address, int charId, BufferedImage img, Palette palette) throws IOException {
        rom.seek(address+charId*4);
        long localAddress = rom.readInt();
        Piece piece = new Piece(2,2);
        piece.setTile(0, 0, new Tile()); piece.setTile(1, 0, new Tile());
        piece.setTile(0, 1, new Tile()); piece.setTile(1, 1, new Tile());
        piece.writeArt(rom, localAddress, img, palette);
    }
    
    void writeNewCharAnims(Character ch, long animsListAddress, long newAddress, int id, int type) throws IOException {
        ch.writeNewAnimations(rom, animsListAddress, newAddress, id, type);
    }

    void writeNewScripts(Character ch, long animsListAddress, long hitsListAddress, long weaponsListAddress, int id, int type) throws IOException {
        ch.writeNewScripts(rom, animsListAddress, hitsListAddress, weaponsListAddress, id, type);
    }
    
}
