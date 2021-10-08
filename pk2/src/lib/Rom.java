/*
 * Decompiled with CFR 0_132.
 */
package lib;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import lib.KMPMatch;
import lib.RandomDataStream;
import lib.anim.AnimFrame;
import lib.anim.Animation;
import lib.anim.Character;
import lib.map.Decompressor;
import lib.map.Palette;
import lib.map.Piece;
import lib.map.Sprite;
import lib.map.Tile;

public class Rom {
    public static final int ROM_START_ADDRESS = 512;
    public RandomAccessFile rom;
    private HashMap<String, Long> knownLabels;

    private void fixChecksum() throws IOException {
        int checksum = 0;
        this.rom.seek(512L);
        byte[] bytes = new byte[4096];
        int pointer = 512;
        while ((long)pointer < this.rom.length()) {
            int readBytes = this.rom.read(bytes);
            pointer += readBytes;
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(is);
            for (int i = 0; i < readBytes - 1; i += 2) {
                checksum = (short)(checksum + dis.readShort());
            }
        }
        this.rom.seek(398L);
        this.rom.writeShort(checksum);
        this.rom.seek(420L);
        this.rom.writeInt((int)this.rom.length() - 1);
    }

    public void close() throws IOException {
        this.fixChecksum();
        this.rom.close();
    }

    public Rom(File file) throws FileNotFoundException {
        this.rom = new RandomAccessFile(file, "rw");
        this.knownLabels = new HashMap();
    }

    public Palette readPalette(long address) throws IOException {
        return Palette.read(this.rom, address);
    }

    public Tile readTile(long address) throws IOException {
        return Tile.read(this.rom, address);
    }

    public Piece readPiece(long address, int width, int height) throws IOException {
        return Piece.read(this.rom, address, width, height);
    }

    public Sprite readSprite(long spriteAddress, long artAddress) throws IOException {
        return Sprite.read(this.rom, spriteAddress, artAddress);
    }

    public Sprite readCompressedSprite(long spriteAddress, long artAddress) throws IOException {
        RandomDataStream art = Decompressor.run(this.rom, artAddress);
        return Sprite.read(this.rom, spriteAddress, art);
    }

    public RandomDataStream decompressArt(long artAddress) throws IOException {
        return Decompressor.run(this.rom, artAddress);
    }

    public AnimFrame readAnimFrame(long address, int animsType) throws IOException {
        return AnimFrame.read(this.rom, address, animsType);
    }

    public Animation readAnimation(long address, int animsType) throws IOException {
        return Animation.read(this.rom, address, animsType);
    }

    public Character readCharacter(long animsAddress, long hitsAddress, long weaponsAddress, int id, int count, int animsType, boolean globalCol, boolean globalWeap, int numPlayableChars) throws IOException {
        return Character.read(this.rom, animsAddress, hitsAddress, weaponsAddress, id, count, animsType, globalCol, globalWeap, numPlayableChars);
    }

    public void writeTileArt(Tile tile, long address, BufferedImage image, Palette palette) throws IOException {
        tile.writeArt(this.rom, address, image, palette);
    }

    public void writePieceArt(Piece piece, long address, BufferedImage image, Palette palette) throws IOException {
        piece.writeArt(this.rom, address, image, palette);
    }

    public void writeSpriteArt(Sprite sprite, long artAddress, BufferedImage image, Palette palette) throws IOException {
        sprite.writeArt(this.rom, artAddress, image, palette);
    }

    public void writeTile(Tile tile, long address) throws IOException {
        tile.write(this.rom, address);
    }

    public void writePiece(Piece piece, long address) throws IOException {
        piece.write(this.rom, address);
    }

    public void writeSprite(Sprite sprite, long mapAddress, long artAddress) throws IOException {
        sprite.write(this.rom, mapAddress, artAddress);
    }

    public void writeSpriteOnly(Sprite sprite, long mapAddress) throws IOException {
        sprite.writeSpriteOnly(this.rom, mapAddress);
    }
    
    public void writeSpriteArtOnly(Sprite sprite, long artAddress) throws IOException {
        sprite.writeArtOnly(this.rom, artAddress);
    }

    public void writeAnimFrame(AnimFrame frame, long address) throws IOException {
        frame.write(this.rom, address);
    }

    public void writeAnimation(Animation anim, long address) throws IOException {
        anim.write(this.rom, address);
    }

    public void writeCharacter(Character ch, long animsAddress, long hitsAddress, long weaponsAddress, int id, boolean globalCol, boolean globalWeap, int numPlayableChars) throws IOException {
        ch.write(this.rom, animsAddress, hitsAddress, weaponsAddress, id, globalCol, globalWeap, numPlayableChars);
    }

    public void writeCharacterSprites(Character ch, Palette palette) throws IOException {
        int numAnims = ch.getNumAnimations();
        for (int i = numAnims - 1; i >= 0; --i) {
            Animation anim = ch.getAnimation(i);
            if (!anim.wasArtModified() || anim.isCompressed()) continue;
            int framesCount = anim.getNumFrames();
            for (int j = 0; j < framesCount; ++j) {
                if (!anim.wasSpriteModified(j)) continue;
                AnimFrame frame = anim.getFrame(j);
                long mapAddress = frame.mapAddress;
                long artAddress = frame.artAddress;
                Sprite originalSprite = this.readSprite(mapAddress, artAddress);
                this.writeSpriteArt(originalSprite, artAddress, anim.getImage(j), palette);
            }
            anim.spritesNotModified();
        }
    }

    public long length() throws IOException {
        return this.rom.length();
    }

    int readSpeed(long speedAddress) throws IOException {
        this.rom.seek(speedAddress);
        return this.rom.readUnsignedShort();
    }

    void writeSpeed(long speedAddress, int newSpeed) throws IOException {
        this.rom.seek(speedAddress);
        this.rom.writeShort(newSpeed);
    }

    String readNameWithTable(long speedAddress, int len) throws IOException {
        this.rom.seek(speedAddress);
        long address = this.rom.readInt();
        return this.readName(address, len);
    }

    String readName(long speedAddress, int len) throws IOException {
        this.rom.seek(speedAddress);
        char[] res = new char[len];
        block14 : for (int i = 0; i < len; ++i) {
            int b = this.rom.readUnsignedByte();
            switch (b) {
                case 32: {
                    res[i] = 32;
                    continue block14;
                }
                case 33: {
                    res[i] = 33;
                    continue block14;
                }
                case 34: {
                    res[i] = 34;
                    continue block14;
                }
                case 39: {
                    res[i] = 39;
                    continue block14;
                }
                case 40: {
                    res[i] = 40;
                    continue block14;
                }
                case 41: {
                    res[i] = 41;
                    continue block14;
                }
                case 44: {
                    res[i] = 44;
                    continue block14;
                }
                case 46: {
                    res[i] = 46;
                    continue block14;
                }
                case 47: {
                    res[i] = 47;
                    continue block14;
                }
                case 58: {
                    res[i] = 42;
                    continue block14;
                }
                case 63: {
                    res[i] = 63;
                    continue block14;
                }
                case 64: {
                    res[i] = 169;
                    continue block14;
                }
                default: {
                    res[i] = b >= 48 && b <= 57 ? (char)(48 + (b - 48)) : (char)(65 + (b - 65));
                }
            }
        }
        return new String(res);
    }

    void writeNameWithTable(long speedAddress, String newName, int len) throws IOException {
        this.rom.seek(speedAddress);
        long address = this.rom.readInt();
        this.writeName(address, newName, len);
    }

    void writeName(long speedAddress, String newName, int len) throws IOException {
        this.rom.seek(speedAddress);
        for (int i = 0; i < len && i < newName.length(); ++i) {
            char c = newName.charAt(i);
            int b = 0;
            switch (c) {
                case ' ': {
                    b = 32;
                    break;
                }
                case '!': {
                    b = 33;
                    break;
                }
                case '\"': {
                    b = 34;
                    break;
                }
                case '\'': {
                    b = 39;
                    break;
                }
                case '(': {
                    b = 40;
                    break;
                }
                case ')': {
                    b = 41;
                    break;
                }
                case ',': {
                    b = 44;
                    break;
                }
                case '.': {
                    b = 46;
                    break;
                }
                case '/': {
                    b = 47;
                    break;
                }
                case '*': {
                    b = 58;
                    break;
                }
                case '?': {
                    b = 63;
                    break;
                }
                case '\u00a9': {
                    b = 64;
                    break;
                }
                default: {
                    b = c >= '0' && c <= '9' ? (int)((byte)(48 + (c - 48))) : (int)((byte)(65 + (c - 65)));
                }
            }
            this.rom.writeByte(b);
        }
    }

    void writePortrait(long address, int charId, BufferedImage img, Palette palette) throws IOException {
        this.rom.seek(address + (long)(charId * 4));
        long localAddress = this.rom.readInt();
        Piece piece = new Piece(2, 2);
        piece.setTile(0, 0, new Tile());
        piece.setTile(1, 0, new Tile());
        piece.setTile(0, 1, new Tile());
        piece.setTile(1, 1, new Tile());
        piece.writeArt(this.rom, localAddress, img, palette);
    }

    void writeNewCharAnims(Character ch, long animsListAddress, long newAddress, int id, int type) throws IOException {
        ch.writeNewAnimations(this.rom, animsListAddress, newAddress, id, type);
    }

    void writeNewScripts(Character ch, long animsListAddress, long hitsListAddress, long weaponsListAddress, int id, int type, boolean globalCol, boolean globalWeap, long newAddress, boolean newHits, boolean newWeapons) throws IOException {
        ch.writeNewScripts(this.rom, animsListAddress, hitsListAddress, weaponsListAddress, id, type, globalCol, globalWeap, newAddress, newHits, newWeapons);
    }

    byte[] getAllData() throws IOException {
        byte[] data = new byte[(int)this.rom.length()];
        this.rom.read(data);
        return data;
    }

    long findLabel(byte[] data, String label) throws IOException {
        byte[] pattern;
        long index;
        Long value = this.knownLabels.get(label);
        if (value != null) {
            return value;
        }
        String matching = label;
        int cardIndex = label.indexOf(35);
        if (cardIndex > 0) {
            matching = label.substring(0, cardIndex);
        }
        if ((index = (long)KMPMatch.indexOf(data, pattern = matching.getBytes())) != -1L && (index += (long)label.length()) % 2L != 0L) {
            ++index;
        }
        this.knownLabels.put(label, index);
        return index;
    }

    BufferedImage readPortrait(long address, int charId, Palette palette) throws IOException {
        this.rom.seek(address + (long)(charId * 4));
        long localAddress = this.rom.readInt();
        Piece piece = Piece.read(this.rom, localAddress, 2, 2);
        return piece.asInversedImage(palette);
    }

    int readStats(long address, int charId, int id) throws IOException {
        this.rom.seek(address + (long)(charId * 8) + (long)id);
        return this.rom.read();
    }

    void writeStats(long address, int charId, int id, int stats) throws IOException {
        this.rom.seek(address + (long)(charId * 8) + (long)id);
        this.rom.write(stats);
    }
}

