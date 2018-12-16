/*
 * Decompiled with CFR 0_132.
 */
package lib;

import java.util.ArrayList;

public class RandomDataStream {
    private ArrayList<Byte> data;
    private int writePos;
    private int readPos;

    public ArrayList<Byte> asList() {
        return this.data;
    }

    public byte[] getData() {
        byte[] res = new byte[this.data.size()];
        for (int i = 0; i < this.data.size(); ++i) {
            res[i] = this.data.get(i);
        }
        return res;
    }

    public RandomDataStream() {
        this.data = new ArrayList();
    }

    public RandomDataStream(int capacity) {
        this.data = new ArrayList(capacity);
    }

    public void write(byte b) {
        if (this.writePos == this.data.size()) {
            this.data.add(b);
        } else {
            this.data.set(this.writePos, b);
        }
        ++this.writePos;
    }

    public byte read() {
        return this.data.get(this.readPos++);
    }

    public int getReadPos() {
        return this.readPos;
    }

    public int getWritePos() {
        return this.writePos;
    }

    public void seekRead(int pos, Pos rel) {
        this.readPos = rel == Pos.begin ? pos : (rel == Pos.cur ? (this.readPos += pos) : this.data.size() - pos);
    }

    public void seekWrite(int pos, Pos rel) {
        this.writePos = rel == Pos.begin ? pos : (rel == Pos.cur ? (this.writePos += pos) : this.data.size() - pos);
    }

    public void seek(int pos) {
        this.seekRead(pos, Pos.begin);
        this.seekWrite(pos, Pos.begin);
    }

    public static enum Pos {
        begin,
        cur,
        end;
        

        private Pos() {
        }
    }

}

