/* 
 * Copyright 2017 Gil Costa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
