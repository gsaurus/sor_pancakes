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
import java.util.ArrayList;
import java.util.TreeMap;
import lib.Renderable;
import lib.Rom;
import lib.anim.AnimFrame;
import lib.anim.Animation;
import lib.map.Palette;
import lib.map.Sprite;

/**
 *
 * @author Gil
 */
public class Manager {
    public static int SPEED_ADDRESS = 0xC0EA;
    public static int NAME_ADDRESS1 = 0x73B2;
    public static int NAME_ADDRESS2 = 0x664a;
    public static int NAME_ADDRESS3 = 0x666a;
    public static int[] NAME_ADDRESSES4 = {0x74c6, 0x74cd, 0x74d5, 0x74dd};
    public static int[] NAME_ADDRESSES5 = {0x74e8, 0x74ef, 0x74f7, 0x74ff};
    public static Color shadowColor = new Color(0,0,0);
    private String romFileName;
    private Palette palette;
    private long animsListAddress;
    private long hitsListAddress;
    private long weaponsListAddress;
    private long portraitListAddress;
    
    //private ArrayList<lib.anim.Character> bufferedChars;
    
    private lib.anim.Character currCharacter;
    private int currCharacterId;
    private long currPaletteAddress;
    
    public Manager(String romFileName, long animsListAddress, long hitsListAddress, long weaponsListAddress, long portraitsAddress) throws FileNotFoundException, IOException{
        this.romFileName = romFileName;
        //bufferedChars = new ArrayList<lib.anim.Character>();
        Rom rom = new Rom(new File(romFileName));
        rom.close();
        this.animsListAddress = animsListAddress;
        this.hitsListAddress = hitsListAddress;
        this.weaponsListAddress = weaponsListAddress;
        this.portraitListAddress = portraitsAddress;
        currCharacterId = -1;
    }
    
    
    public void setPaletteAddress(long paletteAddress) throws IOException{        
        currPaletteAddress = paletteAddress;
        Rom rom = new Rom(new File(romFileName));
        palette = rom.readPalette(paletteAddress);
        rom.close();
    }
    
//    private void bufferCurrentCharacter(){
//        if (bufferedChars.size() <= currCharacterId){
//            bufferedChars.ensureCapacity(currCharacterId);
//            for (int i = bufferedChars.size() ; i <= currCharacterId ; ++i){
//                bufferedChars.add(null);
//            }                
//        }
//        bufferedChars.set(currCharacterId, currCharacter);
//    }
//    
//    private lib.anim.Character getBufferedCharacter(int charId){
//        if (bufferedChars.size() <= charId) return null;
//        return bufferedChars.get(charId);
//    }
    
    public void setCharacter(int charId, int animsCount, long paletteAddress) throws IOException{
        setCharacter(charId, animsCount, paletteAddress, -1);
    }
    
    public void setCharacter(int charId, int animsCount, long paletteAddress, int animsType) throws IOException{
        
        lib.anim.Character newChar;
//        newChar = getBufferedCharacter(charId);
        Rom rom = null;
//        if (newChar == null){
            rom = new Rom(new File(romFileName));
            newChar = rom.readCharacter(animsListAddress, hitsListAddress, weaponsListAddress, charId, animsCount, animsType);
//        }
//        if (currCharacter!= null) bufferCurrentCharacter();
        currCharacterId = charId;
        currCharacter = newChar;
        if (currPaletteAddress != paletteAddress){
            if (rom == null) rom = new Rom(new File(romFileName));
            palette = rom.readPalette(paletteAddress);
        }
        if (rom != null){
            rom.close();
        }
        
    }
    
    public lib.anim.Character getCharacter(){
        return currCharacter;
    }
    
    public int getCurrentCharacterId(){
        return currCharacterId;
    }
    
    public Palette getPalette(){
        return palette;
    }
    
//    public BufferedImage render(Renderable renderable){
//        return renderable.asImage(palette);
//    }
    
//    public BufferedImage readImage(AnimFrame frame) throws IOException{
//        Rom rom = new Rom(new File(romFileName));
//        Sprite sprite = rom.readSprite(frame.mapAddress, frame.artAddress);
//        rom.close();
//        return sprite.asImage(palette);
//    }
    
    public BufferedImage getImage(int animId, int frameId){
        return currCharacter.getAnimation(animId).getImage(frameId);
    }
    
    public BufferedImage getShadow(int animId, int frameId){
        return currCharacter.getAnimation(animId).getShadow(frameId);
    }

    public void bufferAnimation(int animId) throws IOException{        
        final Animation anim = currCharacter.getAnimation(animId);
        if (anim.isBuffered()) return;
        Rom rom = new Rom(new File(romFileName));
        anim.buffer(rom, palette, shadowColor);
        rom.close();
    }
    
    public void bufferAnimFrame(int animId, int frameId) throws IOException{        
        final Animation anim = currCharacter.getAnimation(animId);
        Rom rom = new Rom(new File(romFileName));
        anim.bufferImage(frameId, rom, palette, shadowColor);
        rom.close();
    }

    public void save() throws IOException{
        Rom rom = new Rom(new File(romFileName));
        if (currCharacter.wasModified()){
            rom.writeCharacter(currCharacter, animsListAddress, hitsListAddress, weaponsListAddress, currCharacterId);
            if (currCharacter.wasSpritesModified())
                rom.writeCharacterSprites(currCharacter, palette);
        }
        rom.close();
        currCharacter.setModified(false);
        currCharacter.setSpritesModified(false);
    }
    
    
    public void dumpInFiles() throws IOException{
        Rom rom = new Rom(new File(currCharacterId + "_anims"));
        if (currCharacter.wasModified()){
            rom.writeCharacter(currCharacter, animsListAddress, hitsListAddress, weaponsListAddress, currCharacterId);
            if (currCharacter.wasSpritesModified())
                rom.writeCharacterSprites(currCharacter, palette);
        }
        rom.close();
        currCharacter.setModified(false);
        currCharacter.setSpritesModified(false);
    }
    
    public Sprite readSprite(int animationId, int frameId) throws IOException{
        Rom rom = new Rom(new File(romFileName));
        AnimFrame frame = currCharacter.getAnimFrame(animationId, frameId);
        Sprite res = rom.readSprite(frame.mapAddress, frame.artAddress);
        rom.close();
        return res;
    }
    
    public void writeSprite(Sprite sprite, long mapAddress, long artAddress) throws IOException{
        Rom rom = new Rom(new File(romFileName));
        rom.writeSprite(sprite, mapAddress, artAddress);
        rom.close();
    }
    
    public void writeSpriteOnly(Sprite sprite, long mapAddress) throws IOException{
        Rom rom = new Rom(new File(romFileName));
        rom.writeSpriteOnly(sprite, mapAddress);
        rom.close();
    }
    
    public void writeNewAnimations(long newAddress) throws IOException{
        Rom rom = new Rom(new File(romFileName));
        rom.writeNewCharAnims(currCharacter, animsListAddress, newAddress, currCharacterId, 0);
        rom.close();
    }
    
    public void writeNewScripts() throws IOException{
        Rom rom = new Rom(new File(romFileName));
        rom.writeNewScripts(currCharacter, animsListAddress, hitsListAddress, weaponsListAddress, currCharacterId, 0);
        rom.close();
    }
    
    public long romSize() throws IOException{
        Rom rom = new Rom(new File(romFileName));
        long res = rom.length();
        rom.close();
        return res;
    }

    public int readSpeed() throws IOException {
        Rom rom = new Rom(new File(romFileName));
        int res = rom.readSpeed(SPEED_ADDRESS+currCharacterId*2);
        rom.close();
        return res;
    }

    public void writeSpeed(int newSpeed) throws IOException {
        Rom rom = new Rom(new File(romFileName));
        rom.writeSpeed(SPEED_ADDRESS+currCharacterId*2, newSpeed);
        rom.close();
    }   
    
    
    public String readName() throws IOException {
        Rom rom = new Rom(new File(romFileName));
        String res = rom.readName(NAME_ADDRESS1+currCharacterId*8);
        rom.close();
        return res;
    }

    public void writeName(String newName) throws IOException {
        Rom rom = new Rom(new File(romFileName));
        String fullyName = newName;
        while (fullyName.length() < 5) fullyName = fullyName + " ";
        rom.writeName(NAME_ADDRESS1+currCharacterId*8, fullyName);
        rom.writeName(NAME_ADDRESSES4[currCharacterId], fullyName);
        rom.writeName(NAME_ADDRESSES5[currCharacterId], fullyName);
        // highscores
        fullyName = newName;
        while (fullyName.length() < 5) fullyName = " " + fullyName;
        rom.writeName(NAME_ADDRESS2+currCharacterId*8, fullyName);
        rom.writeName(NAME_ADDRESS3+currCharacterId*8, fullyName);
        rom.close();
    }

    public void writePortrait(BufferedImage img) throws IOException {
        Rom rom = new Rom(new File(romFileName));        
        rom.writePortrait(portraitListAddress, currCharacterId, img, palette);
        rom.close();
    }

    
}
