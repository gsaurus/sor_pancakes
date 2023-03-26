/*
 * Decompiled with CFR 0_132.
 */
package lib;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import lib.anim.AnimFrame;
import lib.anim.Animation;
import lib.anim.Character;
import lib.map.Palette;
import lib.map.Sprite;
import main.Guide;

public class Manager {
    public static int NAME_ADDRESS1 = 29618;
    public static int NAME_ADDRESS2 = 26186;
    public static int NAME_ADDRESS3 = 26218;
    public static int[] NAME_ADDRESSES4 = new int[]{29894, 29901, 29909, 29917};
    public static int[] NAME_ADDRESSES5 = new int[]{29928, 29935, 29943, 29951};
    public static Color shadowColor = new Color(0, 0, 0);
    private String romFileName;
    private Palette palette;
    private long animsListAddress;
    private long hitsListAddress;
    private long weaponsListAddress;
    private long iconsListAddress;
    private long namesListAddress;
    private long statsListAddress;
    private long speedListAddress;
    private int playableChars;
    private int namesSize;
    private int namesOffset;
    private TreeMap<Integer, Long> providedArtAddresses;
    private ArrayList<Long> paletteAddresses;
    private boolean globalCollisions;
    private boolean globalWeapons;
    public Character currCharacter;
    public int currCharacterId;
    private long currPaletteAddress;

    public Manager(String romFileName, Guide guide) throws IOException {
        int numChars = guide.getNumChars();
        numChars = guide.getRealCharId(numChars);
        this.providedArtAddresses = new TreeMap();
        this.paletteAddresses = new ArrayList(numChars);
        this.romFileName = romFileName;
        Rom rom = new Rom(new File(romFileName));
        try {
            int i;
            this.animsListAddress = guide.getAnimsListAddress();
            this.hitsListAddress = guide.getHitsListAddress();
            this.weaponsListAddress = guide.getWeaponsListAddress();
            this.iconsListAddress = guide.getPortraitsListAddress();
            this.namesListAddress = guide.getNamesListAddress();
            this.statsListAddress = guide.getStatsListAddress();
            this.speedListAddress = guide.getSpeedListAddress();
            this.playableChars = guide.getPlayableChars();
            this.namesSize = guide.getNumNameLetters();
            this.globalCollisions = guide.isGlobalCollisions();
            this.globalWeapons = guide.isGlobalWeapons();
            byte[] data = rom.getAllData();
            if (this.animsListAddress == 0L) {
                this.animsListAddress = rom.findLabel(data, guide.getAnimsListLabel());
            }
            if (this.hitsListAddress == 0L) {
                this.hitsListAddress = rom.findLabel(data, guide.getHitsListLabel());
            }
            if (this.weaponsListAddress == 0L) {
                this.weaponsListAddress = rom.findLabel(data, guide.getWeaponsListLabel());
            }
            if (this.iconsListAddress == 0L) {
                this.iconsListAddress = rom.findLabel(data, guide.getPortraitsListLabel());
            }
            if (this.namesListAddress == 0L) {
                this.namesListAddress = rom.findLabel(data, guide.getNamesListLabel());
            }
            if (this.statsListAddress == 0L) {
                this.statsListAddress = rom.findLabel(data, guide.getStatsListLabel());
            }
            if (this.speedListAddress == 0L) {
                this.speedListAddress = rom.findLabel(data, guide.getSpeedListLabel());
            }
            if (this.namesListAddress < -1L) {
                this.namesOffset = (int)(- this.namesListAddress);
            }
            for (i = 0; i < numChars; ++i) {
                String s = guide.getCompressedArtLabel(i);
                if (!s.isEmpty()) {
                    this.providedArtAddresses.put(i, rom.findLabel(data, s));
                    continue;
                }
                s = guide.getSubArtLabel(i);
                if (!s.isEmpty()) {
                    this.providedArtAddresses.put(i, rom.findLabel(data, s));
                    continue;
                }
                long id = guide.getCompressedArtAddress(i);
                if (id != 0L) {
                    this.providedArtAddresses.put(i, id);
                    continue;
                }
                id = guide.getSubArtAddress(i);
                if (id == 0L) continue;
                this.providedArtAddresses.put(i, id);
            }
            for (i = 0; i < numChars; ++i) {
                int fake = guide.getFakeCharId(i);
                String s = guide.getPaletteLabel(fake);
                if (!s.isEmpty()) {
                    this.paletteAddresses.add(rom.findLabel(data, s));
                    continue;
                }
                this.paletteAddresses.add(guide.getPaletteAddress(fake));
            }
            data = null;
            System.gc();
            this.currCharacterId = -1;
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        System.out.println("Manager set up");
    }

    public boolean hasGlobalCollision() {
        return this.globalCollisions;
    }

    public boolean hasGlobalWeapons() {
        return this.globalWeapons;
    }

    public void setPaletteAddress(long paletteAddress) throws IOException {
        this.currPaletteAddress = paletteAddress;
        Rom rom = new Rom(new File(this.romFileName));
        try {
            this.palette = rom.readPalette(paletteAddress);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public void setCharacter(int charId, int animsCount) throws IOException {
        this.setCharacter(charId, animsCount, -1);
    }

    public void setCharacter(int charId, int animsCount, int animsType) throws IOException {
        long paletteAddress = this.paletteAddresses.get(charId);
        Long subArt = this.providedArtAddresses.get(charId);
        AnimFrame.providedArtAddress = subArt != null ? subArt : 0L;
        Rom rom = new Rom(new File(this.romFileName));
        try {
            Character newChar = rom.readCharacter(this.animsListAddress, this.hitsListAddress, this.weaponsListAddress, charId, animsCount, animsType, this.globalCollisions, this.globalWeapons, this.playableChars);
            this.currCharacterId = charId;
            this.currCharacter = newChar;
            if (this.currPaletteAddress != paletteAddress) {
                if (rom == null) {
                    rom = new Rom(new File(this.romFileName));
                }
                this.palette = rom.readPalette(paletteAddress);
            }
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public Character getCharacter() {
        return this.currCharacter;
    }

    public int getCurrentCharacterId() {
        return this.currCharacterId;
    }
    
    public Palette readDefaultPalette() throws FileNotFoundException, IOException {
        return readPalette(0);
    }
    
    public Palette readPalette(int charId) throws FileNotFoundException, IOException {
        Rom rom = new Rom(new File(this.romFileName));
        long paletteAddress = this.paletteAddresses.get(0);
        Palette palette = null;
        try {            
            palette = rom.readPalette(paletteAddress);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        return palette;
    }

    public Palette getPalette() {
        return this.palette;
    }

    public BufferedImage getImage(int animId, int frameId) {
        return this.currCharacter.getAnimation(animId).getImage(frameId);
    }
    
    public Point getPivot(int animId, int frameId) {
        return this.currCharacter.getAnimation(animId).getPivot(frameId);
    }

    public BufferedImage getShadow(int animId, int frameId) {
        return this.currCharacter.getAnimation(animId).getShadow(frameId);
    }

    public void bufferAnimation(int animId) throws IOException {
        // removed
    }

    public void bufferAnimFrame(int animId, int frameId) throws IOException {
        Animation anim = this.currCharacter.getAnimation(animId);
        Rom rom = new Rom(new File(this.romFileName));
        try {
            anim.bufferImage(frameId, rom, this.palette, shadowColor);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public void save() throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            if (this.currCharacter.wasModified()) {
                rom.writeCharacter(this.currCharacter, this.animsListAddress, this.hitsListAddress, this.weaponsListAddress, this.currCharacterId, this.globalCollisions, this.globalWeapons, this.playableChars);
                if (this.currCharacter.wasSpritesModified()) {
                    rom.writeCharacterSprites(this.currCharacter, this.palette);
                }
            }
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        this.currCharacter.setModified(false);
        this.currCharacter.setSpritesModified(false);
    }

    public void replaceCharacterFromManager(Manager otherManager) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        long romSize = rom.length();
        rom.close();
        boolean newHits = this.currCharacter.getHitsSize() < otherManager.currCharacter.getHitsSize();
        boolean newWeapons = this.currCharacter.getWeaponsSize() < otherManager.currCharacter.getWeaponsSize();
        boolean newAnimationScripts = newHits || newWeapons;
        int newType = this.currCharacter.getAnimation((int)0).getFrame((int)0).type;
        if (!newAnimationScripts) {
            for (int i = 0; i < this.currCharacter.getNumAnimations(); ++i) {
                if (this.currCharacter.getAnimation(i).getMaxNumFrames() >= otherManager.currCharacter.getAnimation(i).getNumFrames()) continue;
                newAnimationScripts = true;
                break;
            }
        }
        this.currCharacter = otherManager.currCharacter;
        if (newAnimationScripts) {
            this.writeNewScripts(romSize, newHits, newWeapons);
        }
        rom = new Rom(new File(otherManager.romFileName));
        try {
            this.importSprites(this.currCharacter, rom);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        this.currCharacter.setModified(true);
        this.save();
        String name = otherManager.readName();
        if (name.length() > this.namesSize) {
            name = name.substring(0, this.namesSize);
        }
        this.writeName(name);
        int speed = otherManager.readSpeed();
        this.writeSpeed(speed);
        BufferedImage icon = otherManager.readPortrait();
        this.writePortrait(icon);
        int val = otherManager.readPowerStats();
        this.writePowerStats(val);
        val = otherManager.readTechniqueStats();
        this.writeTechniqueStats(val);
        val = otherManager.readSpeedStats();
        this.writeSpeedStats(val);
        val = otherManager.readJumpStats();
        this.writeJumpStats(val);
        val = otherManager.readStaminaStats();
        this.writeStaminaStats(val);
    }

    private void importSprites(Character otherCharacter, Rom otherRom) throws IOException {
        Rom ourRom = new Rom(new File(this.romFileName));
        try {
            HashSet<AnimFrame> processed = new HashSet<AnimFrame>();
            for (int i = 0; i < otherCharacter.getNumAnimations(); ++i) {
                Animation anim = otherCharacter.getAnimation(i);
                for (int j = 0; j < anim.getNumFrames(); ++j) {
                    AnimFrame frame = anim.getFrame(j);
                    if (processed.contains(frame)) continue;
                    processed.add(frame);
                    Sprite newSprite = otherRom.readSprite(frame.mapAddress, frame.artAddress);
                    BufferedImage img = newSprite.asImage(this.palette);
                    // Free space from ours
//                    Sprite sprite = rom.readSprite(frame.mapAddress, frame.artAddress);
//                    FreeAddressesManager.freeChunk(frame.mapAddress, sprite.getMappingsSizeInBytes());
//                    FreeAddressesManager.freeChunk(frame.artAddress, sprite.getArtSizeInBytes());
                    long mapAddress = FreeAddressesManager.useBestSuitedAddress(newSprite.getMappingsSizeInBytes(), romSize());
                    ourRom.writeSpriteOnly(newSprite, mapAddress);
                    long artAddress = FreeAddressesManager.useBestSuitedAddress(newSprite.getArtSizeInBytes(), romSize());
                    ourRom.writeSpriteArt(newSprite, artAddress, img, this.palette);
                    frame.mapAddress = mapAddress;
                    frame.artAddress = artAddress;                    
                }
            }
        }
        catch (IOException e) {
            ourRom.close();
            throw e;
        }
        ourRom.close();
    }

    public Sprite readSprite(int animationId, int frameId) throws IOException {
        Sprite res;
        Rom rom = new Rom(new File(this.romFileName));
        try {
            AnimFrame frame = this.currCharacter.getAnimFrame(animationId, frameId);
            res = rom.readSprite(frame.mapAddress, frame.artAddress);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        return res;
    }

    public void writeSprite(Sprite sprite, long mapAddress, long artAddress) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            rom.writeSprite(sprite, mapAddress, artAddress);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public void writeSpriteOnly(Sprite sprite, long mapAddress) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            rom.writeSpriteOnly(sprite, mapAddress);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }
    public void writeSpriteArtOnly(Sprite sprite, long artAddress) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            rom.writeSpriteArtOnly(sprite, artAddress);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public void writeNewAnimations(long newAddress) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            rom.writeNewCharAnims(this.currCharacter, this.animsListAddress, newAddress, this.currCharacterId, 0);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public void writeNewScripts(long newAddress, boolean newHits, boolean newWeapons) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            rom.writeNewScripts(this.currCharacter, this.animsListAddress, this.hitsListAddress, this.weaponsListAddress, this.currCharacterId, 0, this.globalCollisions, this.globalWeapons, newAddress, newHits, newWeapons);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public long romSize() throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        long res = rom.length();
        rom.close();
        return res;
    }

    public int readSpeed() throws IOException {
        int res;
        Rom rom = new Rom(new File(this.romFileName));
        try {
            res = rom.readSpeed(this.speedListAddress + (long)(this.currCharacterId * 2));
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        return res;
    }

    public void decompressArt(String outName, long address) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            RandomDataStream data = rom.decompressArt(address);
            byte[] byteArray = data.getData();
            FileOutputStream fos = new FileOutputStream(outName);
            fos.write(byteArray);
            fos.close();
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public void writeSpeed(int newSpeed) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            rom.writeSpeed(this.speedListAddress + (long)(this.currCharacterId * 2), newSpeed);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public String readName() throws IOException {
        String res;
        Rom rom = new Rom(new File(this.romFileName));
        try {
            res = this.namesListAddress > 0L ? rom.readNameWithTable(this.namesListAddress + (long)(this.currCharacterId * 4), this.namesSize) : rom.readName(NAME_ADDRESS1 + this.namesOffset + this.currCharacterId * 8, this.namesSize);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        return res;
    }

    public void writeName(String newName) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            String fullyName = newName;
            while (fullyName.length() < this.namesSize) {
                fullyName = fullyName + " ";
            }
            if (this.namesListAddress > 0L) {
                rom.writeNameWithTable(this.namesListAddress + (long)(this.currCharacterId * 4), fullyName, this.namesSize);
            } else {
                rom.writeName(NAME_ADDRESS1 + this.namesOffset + this.currCharacterId * 8, fullyName, this.namesSize);
                rom.writeName(NAME_ADDRESSES4[this.currCharacterId] + this.namesOffset, fullyName, this.namesSize);
                rom.writeName(NAME_ADDRESSES5[this.currCharacterId] + this.namesOffset, fullyName, this.namesSize);
                fullyName = newName;
                while (fullyName.length() < this.namesSize) {
                    fullyName = " " + fullyName;
                }
                rom.writeName(NAME_ADDRESS2 + this.namesOffset + this.currCharacterId * 8, fullyName, this.namesSize);
                rom.writeName(NAME_ADDRESS3 + this.namesOffset + this.currCharacterId * 8, fullyName, this.namesSize);
            }
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public void writePortrait(BufferedImage img) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        long paletteAddress = this.paletteAddresses.get(0);
        try {            
            Palette palette = rom.readPalette(paletteAddress);
            rom.writePortrait(this.iconsListAddress, this.currCharacterId, img, palette);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public BufferedImage readPortrait() throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        BufferedImage ret = null;
        long paletteAddress = this.paletteAddresses.get(0);
        try {            
            Palette palette = rom.readPalette(paletteAddress);
            ret = rom.readPortrait(this.iconsListAddress, this.currCharacterId, palette);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        return ret;
    }

    public int getNameMaxLen() {
        return this.namesSize;
    }

    private int readStats(int id) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        int ret = 0;
        try {
            ret = rom.readStats(this.statsListAddress, this.currCharacterId, id);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
        return ret;
    }

    private void writeStats(int id, int stats) throws IOException {
        Rom rom = new Rom(new File(this.romFileName));
        try {
            rom.writeStats(this.statsListAddress, this.currCharacterId, id, stats);
        }
        catch (IOException e) {
            rom.close();
            throw e;
        }
        rom.close();
    }

    public int readPowerStats() throws IOException {
        return this.readStats(0);
    }

    public int readTechniqueStats() throws IOException {
        return this.readStats(1);
    }

    public int readSpeedStats() throws IOException {
        return this.readStats(2);
    }

    public int readJumpStats() throws IOException {
        return this.readStats(3);
    }

    public int readStaminaStats() throws IOException {
        return this.readStats(4);
    }

    public void writePowerStats(int value) throws IOException {
        this.writeStats(0, value);
    }

    public void writeTechniqueStats(int value) throws IOException {
        this.writeStats(1, value);
    }

    public void writeSpeedStats(int value) throws IOException {
        this.writeStats(2, value);
    }

    public void writeJumpStats(int value) throws IOException {
        this.writeStats(3, value);
    }

    public void writeStaminaStats(int value) throws IOException {
        this.writeStats(4, value);
    }
}

