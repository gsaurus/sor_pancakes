/*
 * Copyright 2018 gil.costa.
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
package lib.elc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import lib.Rom;

/**
 *
 * @author gil.costa
 */
public final class LevelLoadcues {
    public long address;
    public final List<CharacterObject> enemiesPart1 = new ArrayList<CharacterObject>(70);
    public final List<CharacterObject> enemiesPart2 = new ArrayList<CharacterObject>(20);
    public final List<CharacterObject> goodies = new ArrayList<CharacterObject>(20);
    
    public LevelLoadcues(RandomAccessFile rom, long address) throws IOException{
        this.address = address;
        rom.seek(address + 2); // discard first word
        long enemiesAddress1 = rom.readInt();
        long enemiesAddress2 = rom.readInt();
        long goodiesAddress = rom.readInt();
        readObjectsList(rom, enemiesPart1, enemiesAddress1);
        readObjectsList(rom, enemiesPart2, enemiesAddress2);
        readObjectsList(rom, goodies, goodiesAddress);
    }
    
    void readObjectsList(RandomAccessFile rom, List<CharacterObject> list, long address) throws IOException{
        rom.seek(address);
        while (rom.readUnsignedShort() != 0xFFFF){
            list.add(new CharacterObject(rom, address));
            address += 0x18;
        }
    }
    
    void writeObjectsList(RandomAccessFile rom, List<CharacterObject> list, long address) throws IOException{
        rom.seek(address);
        for (CharacterObject obj : list){
            obj.write(rom, address);
            address += 0x18;
        }
    }
    
    public void write(RandomAccessFile rom) throws IOException{
        rom.seek(address + 2); // discard first word
        long enemiesAddress1 = rom.readInt();
        long enemiesAddress2 = rom.readInt();
        long goodiesAddress = rom.readInt();
        writeObjectsList(rom, enemiesPart1, enemiesAddress1);
        writeObjectsList(rom, enemiesPart2, enemiesAddress2);
        writeObjectsList(rom, goodies, goodiesAddress);
    }
    
    public void write(RandomAccessFile rom, long mainAddress, long enemiesAddress1, long enemiesAddress2, long goodiesAddress) throws IOException{
        rom.seek(mainAddress + 2); // discard first word
        rom.writeInt((int)enemiesAddress1);
        rom.writeInt((int)enemiesAddress2);
        rom.writeInt((int)goodiesAddress);
        writeObjectsList(rom, enemiesPart1, enemiesAddress1);
        writeObjectsList(rom, enemiesPart2, enemiesAddress2);
        writeObjectsList(rom, goodies, goodiesAddress);
    }
    

    public static void main(String[] args){
        try {
            Rom rom = new Rom(new File("sor2.bin"));
            LevelLoadcues obj = new LevelLoadcues(rom.getRomFile(), 0x1EF53C);
            obj.write(rom.getRomFile());
            rom.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
    
}
