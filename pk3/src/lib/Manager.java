/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.anim.AnimFrame;
import lib.anim.Animation;
import lib.map.Palette;
import lib.map.Sprite;

/**
 *
 * @author Gil
 */
public class Manager {   
    public static Color shadowColor = new Color(0,0,0);  
    private static long[] name1 = {0x421C, 0x3306};  // 5 players, 8 chars = 8x2 = 16 bytes
    private static long[] name2 = {0x636E, 0x5498};  // 7 players, 5 chars = 5x2 = 10 bytes
    private static long[] name3 = {0x63B8, 0x54E2};  // 7 players, 5 chars = 5x2 = 10 bytes
    private static long[] name4 = {0x7DDE, 0x6F9E};  // 5 players, 5 chars = 5x2 = 10 bytes
    private static long[] name5 = {0x7E12, 0x6FD2};  // 5 players, 5 chars = 5x2 = 10 bytes
    private static long[] name6 = {0x49154, 0x487a4}; // 5 players, 8 chars = 8x2 = 16 bytes
    
//    private static long[] runSpeed = {0x946a, 0}; // equal to 914C somehow
//    private static long[] rollSpeed = {0x945c, 0};
//    private static long[] jumpHeight = {0x9780, 0};
//    private static long[] jumpAttackSounds = {0x9924, 0};
//    private static long[] frontGrabSounds = {0xA3B8, 0};    // except shiva & ash
//    private static long[] backGrabSounds = {0xA708, 0};     // except shiva & ash
        
    private static long[] icon = {0x23CB2C, 0x23CDE2};
    private static long[] portraitPal = {0x245E6C, 0x2460B8};
    private static long[] portraitCutIndex = {0x23E976, 0x23EBC2};    
    private Palette palette;
    private long animsListAddress; 
    private long boxesListAddress;
    private long hitsListAddress;
    private long weaponsListAddress;
    
    
    private lib.anim.Character currCharacter;
    private int currCharacterId;
    private long currPaletteAddress;
    
    private String romFileName;
    
    public Manager(
            String romFileName,
            long animsListAddress,
            long boxesListAddres,
            long hitsListAddress,
            long weaponsListAddress
    ) throws FileNotFoundException, Exception{
        //bufferedChars = new ArrayList<lib.anim.Character>();
        this.romFileName = romFileName;
        this.animsListAddress = animsListAddress;
        this.boxesListAddress = boxesListAddres;
        this.hitsListAddress = hitsListAddress;
        this.weaponsListAddress = weaponsListAddress;
        currCharacterId = -1;
    }
    
    
    public void readPalette(long paletteAddress) throws Exception{        
        Rom rom = new Rom(new File(romFileName));
        currPaletteAddress = paletteAddress;
        palette = rom.readPalette(paletteAddress);
        rom.close();
    }
    
    private void blazeFixer(Long blazeAirAddress) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        rom.fixBlazeArt(currCharacter,blazeAirAddress);
        rom.close();
    }
    
    
    private void mrXFixer(List<Long> compressedArtAddresses) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        List<Long> lst = new ArrayList<Long>();
        lst.add(compressedArtAddresses.get(0));
        rom.uncompressArt(currCharacter, lst);
        currCharacter.swapArtToMrX();
        lst.clear();
        lst.addAll(compressedArtAddresses);
        lst.remove(0);
        rom.uncompressArt(currCharacter, lst);
        currCharacter.fixMrXArtStream();
        rom.close();
    }
    
    
     public lib.anim.Character setCharacter(int charId, int animsCount, long paletteAddress, List<Long> compressedArtAddresses) throws Exception{         
        if (charId == currCharacterId) return currCharacter;
        Rom rom = new Rom(new File(romFileName));
        lib.anim.Character newChar;
        int animsType = (compressedArtAddresses == null || compressedArtAddresses.isEmpty()) ? 0 : 1;
        newChar = rom.readCharacter(animsListAddress, boxesListAddress, hitsListAddress, weaponsListAddress,charId, animsCount);
        currCharacterId = charId;
        currCharacter = newChar;
        if (currPaletteAddress != paletteAddress){
            palette = rom.readPalette(paletteAddress);
            currPaletteAddress = paletteAddress;
        }
       
        // supper ad-hoc solver for Mr.X
        if (charId == 25 || charId == 28){
            mrXFixer(compressedArtAddresses);
//        } // supper ad-hoc solver for Blaze
//        else if (charId == 4){
//            blazeFixer(compressedArtAddresses.get(0));
        }else{
            if (animsType == 1)
                rom.uncompressArt(currCharacter, compressedArtAddresses);
        }
        rom.close();
        return currCharacter;
    }
    
    public lib.anim.Character getCharacter(){
        return currCharacter;
    }
    
    public int getCurrentCharacterId(){
        return currCharacterId;
    }
   
    public void setPalette(long address) throws Exception{
        if (currPaletteAddress != address){
            Rom rom = new Rom(new File(romFileName));
            palette = rom.readPalette(address);
            currPaletteAddress = address;
            rom.close();
        }
    }
    
    public void savePalette() throws Exception{
        Rom rom = new Rom(new File(romFileName));
        rom.writePalette(palette, currPaletteAddress);
        rom.close();
    }
    
    public Palette getPalette(){
        return palette;
    }   
    
    public BufferedImage getImage(int animId, int frameId){
        return currCharacter.getAnimation(animId).getImage(frameId);
    }
    
    public BufferedImage getShadow(int animId, int frameId){
        return currCharacter.getAnimation(animId).getShadow(frameId);
    }    

    public void bufferAnimation(int animId) throws Exception{                
        final Animation anim = currCharacter.getAnimation(animId);
        if (anim.isBuffered()) return;
        Rom rom = new Rom(new File(romFileName));
        anim.buffer(rom, palette, shadowColor);
        rom.close();
    }
    
    
    public void bufferAnimFrame(int animId, int frameId) throws Exception{        
        final Animation anim = currCharacter.getAnimation(animId);
        Rom rom = new Rom(new File(romFileName));
        anim.bufferImage(frameId, rom, palette, shadowColor);
        rom.close();
    }

    
    public Sprite readSprite(int animationId, int frameId) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        AnimFrame frame = currCharacter.getAnimFrame(animationId, frameId);
        Sprite res = rom.readSprite(frame.mapAddress, frame.artAddress);
        rom.close();
        return res;
    }
    
    
    public void save() throws Exception{
        Rom rom = new Rom(new File(romFileName));
        if (currCharacter.wasModified()){
            rom.writeCharacter(currCharacter, animsListAddress, boxesListAddress, hitsListAddress, weaponsListAddress, currCharacterId);
            if (currCharacter.wasSpritesModified()){
                rom.writeCharacterSprites(currCharacter, palette);
            }
        }
        rom.close();
        currCharacter.setModified(false);
        currCharacter.setSpritesModified(false);
    }
    
    public void writeSprite(Sprite sprite, long mapAddress, long artAddress) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        rom.writeSprite(sprite, mapAddress, artAddress);
        rom.close();
    }
    
    public void writeSpriteOnly(Sprite sprite, long mapAddress) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        rom.writeSpriteOnly(sprite, mapAddress);
        rom.close();
    }
    
    public void writeNewAnimations(long newAddress) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        rom.writeNewCharAnims(currCharacter, animsListAddress, newAddress, currCharacterId, 0);
        rom.close();
    }
    
    public void reloadCollisionId(int animationId, int frameId) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        rom.reloadCollisionId(currCharacter.getAnimFrame(animationId,frameId));
        rom.close();
    }
    
    
    public long romSize() throws Exception{
        Rom rom = new Rom(new File(romFileName));
        long res = rom.length();
        rom.close();
        return res;
    }
   

    public long getPaletteAddress() {
        return currPaletteAddress;
    }
    
    public void writeNewScripts(long newAddress) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        rom.writeNewScripts(currCharacter, animsListAddress, weaponsListAddress, currCharacterId, 0, newAddress);
        rom.close();
    }
    
    
    public void writeIcon(BufferedImage img, int romType) throws Exception {
        Rom rom = new Rom(new File(romFileName));
        int charId = currCharacterId;
        if (charId == 0) charId = 5;        // Ash
        else if (charId == 1) charId = 14;  // Shiva
        else if (charId == 2) charId = 3;   // Zan
        else if (charId == 3) charId = 0;   // Axel
        else if (charId == 4) charId = 1;   // Blaze
        else if (charId == 5) charId = 2;   // Skate
        else if (charId == 6) charId = 4;   // Roo
       
        rom.writeIcon(icon[romType]+(charId*128), img, palette);
        rom.close();
    }
    
    public void writePortrait(BufferedImage img, int romType) throws Exception {
        int charId = currCharacterId;
//        if (charId == 0) charId = 5;        // Ash
//        else if (charId == 1) charId = 14;   // Shiva
        if (charId == 2) charId = 3;        // Zan
        else if (charId == 3) charId = 0;   // Axel
        else if (charId == 4) charId = 1;   // Blaze
        else if (charId == 5) charId = 2;   // Skate
        else if (charId == 6) charId = 4;   // Roo
       
        // Read palette
        Rom rom = new Rom(new File(romFileName));
        Palette pal = rom.readPalette(portraitPal[romType]);
        
        // "clear" cutscenes
        long portraitAddress = 0;        
        if (charId == 0){
            portraitAddress = portraitCutIndex[romType]+4;
            for (int i = 0 ; i < 5 ; ++i) // Axel
                rom.writePortraitCuts((portraitCutIndex[romType]+i*16)+8);
        }else if (charId == 1){
            portraitAddress = portraitCutIndex[romType]+80+4;
            for (int i = 0 ; i < 5 ; ++i) // Blaze
                rom.writePortraitCuts(80+(portraitCutIndex[romType]+i*16)+8);
        }else if (charId == 2){
            portraitAddress = portraitCutIndex[romType]+160+4;
            for (int i = 0 ; i < 7 ; ++i) // Skate
                rom.writePortraitCuts(160+(portraitCutIndex[romType]+i*16)+8);
        }else if (charId == 3){
            portraitAddress = portraitCutIndex[romType]+272+4;
            for (int i = 0 ; i < 5 ; ++i) // Zan
                rom.writePortraitCuts(272+(portraitCutIndex[romType]+i*16)+8);
        }
        else{
            // roo
            portraitAddress = portraitCutIndex[romType]+352+4;
        }
        // write art
        rom.writePortrait(portraitAddress, img, pal);
        rom.close();
    }
    
    public Palette getPortraitPalette(int romType) throws Exception{
        Rom rom = new Rom(new File(romFileName));
        Palette pal = rom.readPalette(portraitPal[romType]);
        rom.close();
        return pal;
    }

    
    public void writeName(String newName, int romType) throws Exception {
        int charId = currCharacterId;
        if (charId == 2) charId = 5;        // Zan
        else if (charId == 3) charId = 2;   // Axel
        else if (charId == 4) charId = 3;   // Blaze
        else if (charId == 5) charId = 4;   // Skate
        Rom rom = new Rom(new File(romFileName));
        if (charId > 1){
            int localId = charId-2;
            rom.writeName(name1[romType]+localId*16, newName);
            rom.writeName(name4[romType]+localId*10, newName);
            rom.writeName(name5[romType]+localId*10, newName);
            rom.writeSelectScreenName(name6[romType]+localId*10, newName);
        }
        rom.writeName(name2[romType]+charId*10, newName);
        rom.writeName(name3[romType]+charId*10, newName);       
        rom.close();
    }
    

    
}
