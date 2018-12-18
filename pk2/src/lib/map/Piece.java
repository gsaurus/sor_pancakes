/*
 * Decompiled with CFR 0_132.
 */
package lib.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;
import lib.RandomDataStream;
import lib.Renderable;

public class Piece
implements Renderable {
    private Tile[] tiles;
    private TreeMap<Integer, Integer> originalOrder;
    private int width;

    public Piece(int width, int height) {
        this.tiles = new Tile[width * height];
        this.originalOrder = new TreeMap();
        this.width = width;
    }

    public void setTile(int x, int y, Tile tile) {
        int index = y * this.width + x;
        this.tiles[index] = tile;
        this.originalOrder.put(index, this.originalOrder.size());
    }

    public Tile getTile(int x, int y) {
        return this.tiles[y * this.width + x];
    }

    private int getTileOrder(int x, int y) {
        return this.originalOrder.get(y * this.width + x);
    }

    @Override
    public BufferedImage asImage(Palette Pal) {
        int height = this.tiles.length / this.width;
        BufferedImage res = new BufferedImage(this.width * 8, height * 8, 2);
        Graphics2D g2d = res.createGraphics();
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < height; ++y) {
                g2d.drawImage(this.getTile(x, y).asImage(Pal), x * 8, y * 8, null);
            }
        }
        return res;
    }

    public BufferedImage asInversedImage(Palette Pal) {
        int height = this.tiles.length / this.width;
        BufferedImage res = new BufferedImage(this.width * 8, height * 8, 2);
        Graphics2D g2d = res.createGraphics();
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < height; ++y) {
                g2d.drawImage(this.getTile(x, y).asImage(Pal), y * 8, x * 8, null);
            }
        }
        return res;
    }

    @Override
    public BufferedImage asShadow(Color color) {
        int height = this.tiles.length / this.width;
        BufferedImage res = new BufferedImage(this.width * 8, height * 8, 2);
        Graphics2D g2d = res.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, this.width * 8, height * 8);
        return res;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.tiles.length / this.width;
    }

    public static Piece read(RandomAccessFile rom, long address, int width, int height) throws IOException {
        int numTiles = width * height;
        Piece piece = new Piece(width, height);
        for (int i = 0; i < numTiles; ++i) {
            Tile tile = Tile.read(rom, address + (long)(i * 32));
            piece.setTile(i / height, i % height, tile);
        }
        return piece;
    }

    public static Piece read(RandomDataStream art, int offset, int width, int height) throws IOException {
        int numTiles = width * height;
        Piece piece = new Piece(width, height);
        for (int i = 0; i < numTiles; ++i) {
            Tile tile = Tile.read(art, offset + i * 32);
            piece.setTile(i / height, i % height, tile);
        }
        return piece;
    }

    public void writeArt(RandomAccessFile rom, long address, BufferedImage image, Palette palette) throws IOException {
        int height = this.getHeight();
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < height; ++y) {
                BufferedImage tileImage = image.getSubimage(x * 8, y * 8, 8, 8);
                Tile tile = this.getTile(x, y);
                int id = this.getTileOrder(x, y);
                tile.writeArt(rom, address + (long)(id * 32), tileImage, palette);
            }
        }
    }
    
    public long getSizeInBytes(){
        return this.width * this.getHeight() * Tile.getSizeInBytes();
    }
    

    public void write(RandomAccessFile rom, long address) throws IOException {
        for (int i = 0; i < this.tiles.length; ++i) {
            Tile tile = this.tiles[i];
            tile.write(rom, address + (long)(i * 32));
        }
    }
}

