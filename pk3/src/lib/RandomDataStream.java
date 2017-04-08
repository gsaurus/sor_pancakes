/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gil
 */
public class RandomDataStream{
    
    public enum Pos{
        begin, cur, end
    }
    private List<Byte> data;
    private int writePos;
    private int readPos;
    
    public List<Byte> asList(){
        return data;
    }
    
    public RandomDataStream(){
        data = new ArrayList<Byte>();
    }
    public RandomDataStream(int capacity){
        data = new ArrayList<Byte>(capacity);
    }
    
    public void write(byte b){
        if (writePos == data.size())
            data.add(b);
        else data.set(writePos, b);
        writePos++;
    }
    
    public void append(List<Byte> bytes){
        data = data.subList(0, writePos);
        data.addAll(bytes);
        writePos = data.size();
    }
    
    public byte read(){
        return data.get(readPos++);
    }
    
    public int getReadPos(){
        return readPos;
    }
    public int getWritePos(){
        return writePos;
    }
    public void seekRead(int pos, Pos rel){
        if (rel == Pos.begin)
            readPos = pos;
        else if (rel == Pos.cur)
            readPos+=pos;
        else readPos = data.size()-pos;
    }
    public void seekWrite(int pos, Pos rel){
        if (rel == Pos.begin)
            writePos = pos;
        else if (rel == Pos.cur)
            writePos+=pos;
        else writePos = data.size()-pos;
    }
    
    public void seek(int pos){
        seekRead(pos,Pos.begin);
        seekWrite(pos,Pos.begin);
    }
}
