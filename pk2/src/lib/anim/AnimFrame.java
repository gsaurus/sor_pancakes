/*
 * Decompiled with CFR 0_132.
 */
package lib.anim;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.json.JSONObject;

public class AnimFrame {
    public static long providedArtAddress;
    public int delay;
    public long mapAddress;
    public long artAddress;
    public int type;

    public AnimFrame() {
    }

    AnimFrame(AnimFrame f1) {
        this.delay = f1.type;
        this.mapAddress = f1.type;
        this.artAddress = f1.type;
        this.type = f1.type;
    }

    private static String hx(int value) {
        return Integer.toHexString(value);
    }

    public static int getTypeDependentSize(int type) {
        if (type == 0) {
            return 12;
        }
        if (type == 1) {
            return 6;
        }
        if (type == 2) {
            return 10;
        }
        if (type == 3) {
            return 6;
        }
        return 0;
    }

    protected static AnimFrame readTypesTreeAndZero(RandomAccessFile rom, long address) throws IOException {
        AnimFrame frame = new AnimFrame();
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();
        int check = rom.read();
        if (check == 149) {
            frame.type = 0;
            byte a0 = rom.readByte();
            rom.skipBytes(1);
            byte a1 = rom.readByte();
            rom.skipBytes(1);
            byte a2 = rom.readByte();
            frame.artAddress = (a0 & 255) + (a1 << 8 & 65280) + (a2 << 16 & 16711680);
            frame.artAddress *= 2L;
        } else {
            frame.type = 3;
            frame.artAddress = providedArtAddress;
        }
        return frame;
    }

    protected static AnimFrame readTypesOneAndZero(RandomAccessFile rom, long address) throws IOException {
        AnimFrame frame = new AnimFrame();
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();
        int check = rom.read();
        if (check == 149) {
            frame.type = 0;
            byte a0 = rom.readByte();
            rom.skipBytes(1);
            byte a1 = rom.readByte();
            rom.skipBytes(1);
            byte a2 = rom.readByte();
            frame.artAddress = (a0 & 255) + (a1 << 8 & 65280) + (a2 << 16 & 16711680);
            frame.artAddress *= 2L;
        } else {
            frame.type = 1;
            frame.artAddress = providedArtAddress;
            rom.seek(address + 6L);
        }
        return frame;
    }

    protected static AnimFrame readTypeZero(RandomAccessFile rom, long address) throws IOException {
        AnimFrame frame = new AnimFrame();
        frame.type = 0;
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();
        rom.skipBytes(1);
        byte a0 = rom.readByte();
        rom.skipBytes(1);
        byte a1 = rom.readByte();
        rom.skipBytes(1);
        byte a2 = rom.readByte();
        frame.artAddress = (a0 & 255) + (a1 << 8 & 65280) + (a2 << 16 & 16711680);
        frame.artAddress *= 2L;
        return frame;
    }

    protected static AnimFrame readTypeTree(RandomAccessFile rom, long address) throws IOException {
        AnimFrame frame = new AnimFrame();
        frame.type = 3;
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();
        frame.artAddress = providedArtAddress;
        return frame;
    }

    protected static AnimFrame readTypeOne(RandomAccessFile rom, long address) throws IOException {
        AnimFrame frame = new AnimFrame();
        frame.type = 1;
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();
        frame.artAddress = providedArtAddress;
        return frame;
    }

    protected static AnimFrame readTypeTwo(RandomAccessFile rom, long address) throws IOException {
        AnimFrame frame = new AnimFrame();
        frame.type = 2;
        rom.seek(address);
        frame.delay = rom.readUnsignedShort();
        frame.mapAddress = rom.readInt();
        frame.artAddress = rom.readInt();
        return frame;
    }

    public static AnimFrame read(RandomAccessFile rom, long address, int type) throws IOException {
        if (type == 3) {
            return AnimFrame.readTypesTreeAndZero(rom, address);
        }
        if (type == 2) {
            return AnimFrame.readTypeTwo(rom, address);
        }
        if (type == 1) {
            return AnimFrame.readTypesOneAndZero(rom, address);
        }
        if (type == 0) {
            return AnimFrame.readTypeZero(rom, address);
        }
        return null;
    }

    public void write(RandomAccessFile rom, long address) throws IOException {
        this.write(rom, address, this.type);
    }

    public void write(RandomAccessFile rom, long address, int type) throws IOException {
        rom.seek(address);
        rom.writeShort(this.delay);
        rom.writeInt((int)this.mapAddress);
        if (type == 0) {
            long tempArtAddress = this.artAddress / 2L;
            int a0 = (int)(tempArtAddress & 255L);
            int a1 = (int)(tempArtAddress >> 8 & 255L);
            int a2 = (int)(tempArtAddress >> 16 & 255L);
            rom.writeByte(149);
            rom.writeByte(a0);
            rom.writeByte(150);
            rom.writeByte(a1);
            rom.writeByte(151);
            rom.writeByte(a2);
        } else if (type == 2) {
            rom.writeInt((int)this.artAddress);
        }
    }
    
    public JSONObject toJson(int framePointer){
        JSONObject res = new JSONObject();
        res.put("imageId", framePointer);
        res.put("duration", delay);
        return res;
    }
}

