/*
 * Decompiled with CFR 0_132.
 */
package lib.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import lib.RandomDataStream;
import lib.Renderable;
import lib.map.Palette;
import lib.map.Piece;
import lib.map.SpritePiece;

public class Sprite
implements Renderable {
    private static final int ACCEPTABLE_PIECES = 128;
    private ArrayList<SpritePiece> pieces;
    private ArrayList<Piece> tiledPieces;
    private int numTiles;

    public Sprite(int numPieces) {
        this.pieces = new ArrayList(numPieces);
        this.tiledPieces = new ArrayList(numPieces);
    }

    public Sprite() {
        this.pieces = new ArrayList();
        this.tiledPieces = new ArrayList();
    }

    public void setNumTiles(int numTiles) {
        this.numTiles = numTiles;
    }

    public void addPiece(SpritePiece piece, Piece tiledPiece) {
        this.pieces.add(piece);
        this.tiledPieces.add(tiledPiece);
    }

    private static BufferedImage flip(BufferedImage image, boolean horizontaly, boolean verticaly) {
        int h = 1;
        int v = 1;
        int th = 0;
        int tv = 0;
        if (horizontaly) {
            h = -1;
            th = - image.getWidth();
        }
        if (verticaly) {
            v = -1;
            tv = - image.getHeight();
        }
        AffineTransform tx = AffineTransform.getScaleInstance(h, v);
        tx.translate(th, tv);
        AffineTransformOp op = new AffineTransformOp(tx, 1);
        return op.filter(image, null);
    }

    @Override
    public BufferedImage asImage(Palette Pal) {
        BufferedImage res = new BufferedImage(256, 256, 2);
        Graphics2D g2d = res.createGraphics();
        for (int i = this.pieces.size() - 1; i >= 0; --i) {
            SpritePiece piece = this.pieces.get(i);
            Piece tiledPiece = this.tiledPieces.get(i);
            BufferedImage pieceImg = tiledPiece.asImage(Pal);
            if (piece.xFliped || piece.yFliped) {
                pieceImg = Sprite.flip(pieceImg, piece.xFliped, piece.yFliped);
            }
            g2d.drawImage(pieceImg, 128 + piece.xL, 128 + piece.y, null);
        }
        return res;
    }

    @Override
    public BufferedImage asShadow(Color color) {
        BufferedImage res = new BufferedImage(256, 256, 2);
        Graphics2D g2d = res.createGraphics();
        for (int i = this.pieces.size() - 1; i >= 0; --i) {
            SpritePiece piece = this.pieces.get(i);
            Piece tiledPiece = this.tiledPieces.get(i);
            BufferedImage pieceImg = tiledPiece.asShadow(color);
            if (piece.xFliped || piece.yFliped) {
                pieceImg = Sprite.flip(pieceImg, piece.xFliped, piece.yFliped);
            }
            g2d.drawImage(pieceImg, 128 + piece.xL, 128 + piece.y, null);
        }
        return res;
    }

    public static Sprite read(RandomAccessFile rom, long mapAddress, RandomDataStream art) throws IOException {
        rom.seek(mapAddress);
        int numPieces = rom.readUnsignedShort() + 1;
        if (numPieces > 128) {
            throw new IOException("Bad map address");
        }
        Sprite sprite = new Sprite(numPieces);
        rom.skipBytes(1);
        byte a0 = rom.readByte();
        rom.skipBytes(1);
        byte a1 = rom.readByte();
        sprite.numTiles = ((a1 & 255) + (a0 << 8 & 65280)) / 16;
        for (int i = 0; i < numPieces; ++i) {
            rom.seek(mapAddress + 6L + (long)(i * 6));
            SpritePiece p = new SpritePiece();
            p.xR = rom.readByte();
            p.xL = rom.readByte();
            p.y = rom.readByte();
            byte wh = rom.readByte();
            p.width = wh >>> 2 & 3;
            p.height = wh & 3;
            short tail = rom.readShort();
            p.priorityFlag = (tail >>> 15 & 1) == 1;
            p.paletteLine = tail >>> 13 & 3;
            p.yFliped = (tail >>> 12 & 1) == 1;
            p.xFliped = (tail >>> 11 & 1) == 1;
            p.spriteIndex = tail & 2047;
            Piece piece = Piece.read(art, p.spriteIndex * 32, p.width + 1, p.height + 1);
            sprite.addPiece(p, piece);
        }
        return sprite;
    }

    public static Sprite read(RandomAccessFile rom, long mapAddress, long artAddress) throws IOException {
        rom.seek(mapAddress);
        int numPieces = rom.readUnsignedShort() + 1;
        if (numPieces > 128) {
            throw new IOException("Bad map address");
        }
        Sprite sprite = new Sprite(numPieces);
        rom.skipBytes(1);
        byte a0 = rom.readByte();
        rom.skipBytes(1);
        byte a1 = rom.readByte();
        sprite.numTiles = ((a1 & 255) + (a0 << 8 & 65280)) / 16;
        for (int i = 0; i < numPieces; ++i) {
            rom.seek(mapAddress + 6L + (long)(i * 6));
            SpritePiece p = new SpritePiece();
            p.xR = rom.readByte();
            p.xL = rom.readByte();
            p.y = rom.readByte();
            byte wh = rom.readByte();
            p.width = wh >>> 2 & 3;
            p.height = wh & 3;
            short tail = rom.readShort();
            p.priorityFlag = (tail >>> 15 & 1) == 1;
            p.paletteLine = tail >>> 13 & 3;
            p.yFliped = (tail >>> 12 & 1) == 1;
            p.xFliped = (tail >>> 11 & 1) == 1;
            p.spriteIndex = tail & 2047;
            Piece piece = Piece.read(rom, artAddress + (long)(p.spriteIndex * 32), p.width + 1, p.height + 1);
            sprite.addPiece(p, piece);
        }
        return sprite;
    }

    public void write(RandomAccessFile rom, long mapAddress, long artAddress) throws IOException {
        rom.seek(mapAddress);
        rom.writeShort(this.pieces.size() - 1);
        long tempSize = this.numTiles * 16;
        int a0 = (int)(tempSize & 255L);
        int a1 = (int)(tempSize >> 8 & 255L);
        rom.write(148);
        rom.write(a1);
        rom.write(147);
        rom.write(a0);
        for (int i = 0; i < this.pieces.size(); ++i) {
            rom.seek(mapAddress + 6L + (long)(i * 6));
            SpritePiece p = this.pieces.get(i);
            rom.write(p.xR);
            rom.write(p.xL);
            rom.write(p.y);
            int wh = p.width << 2 & 12 | p.height & 3;
            rom.write(wh);
            int pf = p.priorityFlag ? 1 : 0;
            int xf = p.xFliped ? 1 : 0;
            int yf = p.yFliped ? 1 : 0;
            int tail = pf << 15 | (p.paletteLine & 3) << 13 | yf << 12 | xf << 11 | p.spriteIndex & 2047;
            rom.writeShort(tail);
            Piece piece = this.tiledPieces.get(i);
            piece.write(rom, artAddress + (long)(p.spriteIndex * 32));
        }
    }

    public void writeSpriteOnly(RandomAccessFile rom, long mapAddress) throws IOException {
        rom.seek(mapAddress);
        rom.writeShort(this.pieces.size() - 1);
        long tempSize = this.numTiles * 16;
        int a0 = (int)(tempSize & 255L);
        int a1 = (int)(tempSize >> 8 & 255L);
        rom.write(148);
        rom.write(a1);
        rom.write(147);
        rom.write(a0);
        for (int i = 0; i < this.pieces.size(); ++i) {
            rom.seek(mapAddress + 6L + (long)(i * 6));
            SpritePiece p = this.pieces.get(i);
            rom.write(p.xR);
            rom.write(p.xL);
            rom.write(p.y);
            int wh = p.width << 2 & 12 | p.height & 3;
            rom.write(wh);
            int pf = p.priorityFlag ? 1 : 0;
            int xf = p.xFliped ? 1 : 0;
            int yf = p.yFliped ? 1 : 0;
            int tail = pf << 15 | (p.paletteLine & 3) << 13 | yf << 12 | xf << 11 | p.spriteIndex & 2047;
            rom.writeShort(tail);
        }
    }

    public void writeArt(RandomAccessFile rom, long artAddress, BufferedImage image, Palette palette) throws IOException {
        int numPieces = this.pieces.size();
        for (int i = 0; i < numPieces; ++i) {
            SpritePiece p = this.pieces.get(i);
            int x = 128 + p.xL;
            int y = 128 + p.y;
            Piece tp = this.tiledPieces.get(i);
            int width = tp.getWidth() * 8;
            int height = tp.getHeight() * 8;
            BufferedImage tileImage = image.getSubimage(x, y, width, height);
            if (p.xFliped || p.yFliped) {
                tileImage = Sprite.flip(tileImage, p.xFliped, p.yFliped);
            }
            tp.writeArt(rom, artAddress + (long)(p.spriteIndex * 32), tileImage, palette);
        }
    }

    public long getNumPieces() {
        return this.pieces.size();
    }

    public void applyOffset(int deltaX, int deltaY) {
        for (SpritePiece piece : this.pieces) {
            piece.xL += deltaX;
            piece.xR -= deltaX;
            piece.y += deltaY;
        }
    }

    public Rectangle getBounds() {
        Rectangle res = new Rectangle();
        res.x = Integer.MAX_VALUE;
        res.y = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;
        for (SpritePiece piece : this.pieces) {
            int x = piece.xL + 128;
            int y = piece.y + 128;
            int r = x + (piece.width + 1) * 8;
            int b = y + (piece.width + 1) * 8;
            if (x < res.x) {
                res.x = x;
            }
            if (y < res.y) {
                res.y = y;
            }
            if (r > right) {
                right = r;
            }
            if (b <= bottom) continue;
            bottom = b;
        }
        res.width = right - res.x;
        res.height = bottom - res.y;
        return res;
    }
}

