/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashSet;
import java.util.List;
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
    private String romName;
     
    private void fixChecksum() throws Exception{
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
    
    public void close() throws Exception {
        fixChecksum();
        rom.close();
    }
    
    public Rom(File file) throws FileNotFoundException{
        rom = new RandomAccessFile(file,"rw");
        romName = file.getAbsolutePath();
    }
    public Palette readPalette(long address) throws Exception {
        return Palette.read(rom,address);
    }
    public Tile readTile(long address) throws Exception {
        return Tile.read(rom,address);
    }
    public Piece readPiece(long address, int width, int height) throws Exception {
        return Piece.read(rom,address, width, height);
    }
    public Sprite readSprite(long spriteAddress, long artAddress) throws Exception {
        return Sprite.read(rom,spriteAddress, artAddress);
    }  
    public Sprite readSpriteOnly(long spriteAddress) throws Exception {
        return Sprite.readSpriteOnly(rom,spriteAddress);
    }  
    public AnimFrame readAnimFrame(long address) throws Exception {
        return AnimFrame.read(rom,address);
    }
    public lib.anim.Character readCharacter(
            long animsListAddress,
            long boxesListAddres,
            long hitsListAddress,
            long weaponsListAddress,
            int id, int count
    ) throws Exception {
        return lib.anim.Character.read(rom, animsListAddress, boxesListAddres, hitsListAddress, weaponsListAddress, id, count);
    }   
    
    public long length() throws Exception{
        return rom.length();
    }

    public String getRomName() {
        return romName;
    }

    public Sprite readSprite(long mapAddress, RandomDataStream art) throws Exception {
        return Sprite.read(rom, mapAddress, art);
    }

    void uncompressArt(Character ch, List<Long> compressedArtAddresses) throws Exception {
        ch.uncompressArt(romName, compressedArtAddresses);
    }

    void fixBlazeArt(Character currCharacter, Long blazeAir) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
    public void writeTileArt(Tile tile, long address, BufferedImage image, Palette palette) throws Exception {
        tile.writeArt(rom, address, image, palette);
    }
    public void writePieceArt(Piece piece, long address, BufferedImage image, Palette palette) throws Exception {
        piece.writeArt(rom, address, image, palette);
    }
    public void writeSpriteArt(Sprite sprite, long artAddress, BufferedImage image, Palette palette) throws Exception {
        sprite.writeArt(rom, artAddress, image, palette);
    }
    
    public void writeTile(Tile tile, long address) throws Exception {
        tile.write(rom,address);
    }
    public void writePiece(Piece piece, long address) throws Exception {
        piece.write(rom,address);
    }
    public void writeSprite(Sprite sprite, long mapAddress, long artAddress) throws Exception {
        sprite.write(rom, mapAddress, artAddress);
    }
    public void writeSpriteOnly(Sprite sprite, long mapAddress) throws Exception {
        sprite.writeSpriteOnly(rom, mapAddress);
    }
    
    
    public void writeAnimFrame(AnimFrame frame, long address) throws Exception {
        frame.write(rom,address);
    }
    public void writeAnimation(Animation anim, long address) throws Exception {
        anim.write(rom, address);
    }
    public void writeCharacter(
            lib.anim.Character ch,
            long animsListAddress,
            long boxesListAddres,
            long hitsListAddress,
            long weaponsListAddress,
            int id
    ) throws Exception {
        ch.write(rom, animsListAddress, boxesListAddres, hitsListAddress, weaponsListAddress, id);        
    } 
    
    public void writeCharacterSprites(lib.anim.Character ch, Palette palette) throws Exception{
        int numAnims = ch.getNumAnimations();
        HashSet<Animation> processed = new HashSet<Animation>();
        for (int i = 0 ; i < numAnims ; ++i){
            Animation anim = ch.getAnimation(i);
            if (!processed.contains(anim)){
                processed.add(anim);
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
//        System.out.println("All sprites done");
    }
    
    void writeNewCharAnims(Character ch, long animsListAddress, long newAddress, int id, int type) throws Exception {
        ch.writeNewAnimations(rom, animsListAddress, newAddress, id, type);
    }
    
    
    void reloadCollisionId(AnimFrame frame) throws IOException{
        frame.reloadCollisionId(rom);
    }

    void writeIcon(long address,BufferedImage img, Palette palette) throws Exception{        
        rom.seek(address);
        Piece piece = new Piece(2,2);
        piece.setTile(0, 0, new Tile()); piece.setTile(1, 0, new Tile());
        piece.setTile(0, 1, new Tile()); piece.setTile(1, 1, new Tile());
        piece.writeArt(rom, address, img, palette);
    }
    
    
    void writePortrait(long address,BufferedImage img, Palette palette) throws Exception{        
        rom.seek(address);
        long portraitAddress = rom.readInt();
        Piece piece = new Piece(6,6);           
        for (int y = 0 ; y < 6 ; ++y){
            for (int x = 0 ; x < 6 ; ++x){
                piece.setTile(x, y, new Tile());
            }
        }                          
        piece.writeArt(rom, portraitAddress, img, palette);
    }
    
    void writePortraitCuts(long address) throws IOException{
        rom.seek(address);
        rom.writeInt(0);
        rom.writeInt(0);
    }

    void writePalette(Palette palette, long currPaletteAddress) throws Exception{
        palette.write(rom, currPaletteAddress);
    }
    
    void writeNewScripts(Character ch, long animsListAddress, long weaponsListAddress, int id, int type, long newAddress) throws Exception {
        ch.writeNewScripts(rom, animsListAddress, weaponsListAddress, id, type, newAddress);
    }
    
    
    
    
    int char2Hex(char c){
        if (c >= '0' && c <= '9') return 0xC0 + (c-'0');
        if (c >= 'A' && c <= 'Z') return 0xCE + (c-'A');
        if (c >= 'a' && c <= 'z') return 0xCE + (c-'a');
        if (c == ' ') return 0xB5;
        if (c == '-') return 0xBD;
        if (c == '.') return 0xBE;
        if (c == '*') return 0xCA;
        if (c == '@') return 0xCD;
        //if (c == '+')
        return 0x86;
    }

    void writeName(long address, String name) throws IOException{
        writeName(address,name,0x86);
    }
    void writeSelectScreenName(long address, String name) throws IOException{
        writeName(address,name,0x00);
    }
    
    void writeName(long address, String name, int separator) throws IOException{
        rom.seek(address);
        for (char c:name.toCharArray()){
            rom.writeByte(separator);
            rom.writeByte(char2Hex(c));
        }
    }
    
}
