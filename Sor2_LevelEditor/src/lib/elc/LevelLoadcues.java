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
    public final List<CharacterObject> enemiesPart1 = new ArrayList<>(70);
    public final List<CharacterObject> enemiesPart2 = new ArrayList<>(20);
    public final List<ItemObject> goodies = new ArrayList<>(20);
    // DEBUG!!!!
    private final int extraOffset = 0;//0x4100;
    
    public LevelLoadcues(RandomAccessFile rom, long address, boolean isLastLevel) throws IOException{
        this.address = address;
        rom.seek(address + 2); // discard first word
        long enemiesAddress1 = rom.readInt();
        long enemiesAddress2 = rom.readInt();
        long goodiesAddress = rom.readInt();
        readCharactersList(rom, enemiesPart1, enemiesAddress1, isLastLevel);
        readCharactersList(rom, enemiesPart2, enemiesAddress2, isLastLevel);
        readItemsList(rom, goodies, goodiesAddress);
    }
    
    
    public int getTotalNumberOfScenes(){
        int maxScene = 0;
        for (CharacterObject obj: enemiesPart1){
            if (obj.sceneId > maxScene){
                maxScene = obj.sceneId;
            }
        }
        return (maxScene / 2) + 1;
    }
    
    void readCharactersList(RandomAccessFile rom, List<CharacterObject> list, long address, boolean isLastLevel) throws IOException{
        address += extraOffset;
        rom.seek(address);
        while(true)
        {
            while (rom.readUnsignedShort() != 0xFFFF){
                list.add(new CharacterObject(rom, address));
                address += CharacterObject.SIZE;
                rom.seek(address);
            }
            if (isLastLevel)
            {
                address += 2;
                isLastLevel = false;
            }
            else
            {
                break;
            }
        }
    }
    
    void readItemsList(RandomAccessFile rom, List<ItemObject> list, long address) throws IOException{
        rom.seek(address);
        while (rom.readUnsignedShort() != 0xFFFF){
            list.add(new ItemObject(rom, address));
            address += ItemObject.SIZE;
            rom.seek(address);
        }
    }
    
    void writeCharactersList(RandomAccessFile rom, List<CharacterObject> list, long address, int deltaObjectId) throws IOException{
        address += extraOffset;
        rom.seek(address);
        for (CharacterObject obj : list){
            obj.write(rom, address, deltaObjectId);
            address += CharacterObject.SIZE;
            rom.seek(address);
        }
    }
    
    void writeItemsList(RandomAccessFile rom, List<ItemObject> list, long address, int deltaObjectId) throws IOException{
        rom.seek(address);
        for (ItemObject obj : list){
            obj.write(rom, address, deltaObjectId);
            address += ItemObject.SIZE;
            rom.seek(address);
        }
    }
    
    public void write(RandomAccessFile rom, long mainAddress, long enemiesAddress1, long enemiesAddress2, long goodiesAddress, int deltaObjectId) throws IOException{
        rom.seek(mainAddress + 2); // discard first word
        rom.writeInt((int)enemiesAddress1);
        rom.writeInt((int)enemiesAddress2);
        rom.writeInt((int)goodiesAddress);
        writeCharactersList(rom, enemiesPart1, enemiesAddress1, deltaObjectId);
        writeCharactersList(rom, enemiesPart2, enemiesAddress2, deltaObjectId);
        writeItemsList(rom, goodies, goodiesAddress, deltaObjectId);
    }
    public void write(RandomAccessFile rom, long mainAddress, int deltaObjectId) throws IOException{
        rom.seek(mainAddress + 2); // discard first word
        long enemiesAddress1 = rom.readInt();
        long enemiesAddress2 = rom.readInt();
        long goodiesAddress = rom.readInt();
        writeCharactersList(rom, enemiesPart1, enemiesAddress1, deltaObjectId);
        writeCharactersList(rom, enemiesPart2, enemiesAddress2, deltaObjectId);
        writeItemsList(rom, goodies, goodiesAddress, deltaObjectId);
    }
    
    public void write(RandomAccessFile rom) throws IOException{
        write(rom, address, 0);
    }

    public static void main(String[] args){
        try {
            Rom rom = new Rom(new File("sor2.bin"));
            LevelLoadcues obj = new LevelLoadcues(rom.getRomFile(), 0x1EF53C, false);
            obj.write(rom.getRomFile());
            rom.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void randomizeEnemiesListOne(long enemyNamesAddress) {
        randomizeEnemiesList(enemyNamesAddress, enemiesPart1, 0, enemiesPart1.size() - 1, true);
    }

    public int randomizeEnemiesList(long enemyNamesAddress, List<CharacterObject> enemies, int accum, int total, boolean updateScene) {
        CharacterObject.prepareForRandomization();
        for (int i = 0; i < enemies.size(); ++i) {
            enemies.get(i).randomize(accum++, total, enemyNamesAddress, updateScene);
        }
        return accum;
    }
    
    public int randomizeEnemies(long enemyNamesAddress, int accum, int total) {
        accum = randomizeEnemiesList(enemyNamesAddress, enemiesPart1, accum, total, false);
        return randomizeEnemiesList(enemyNamesAddress, enemiesPart2, accum, total, false);
    }
    
    public int getTotalEnemies() {
        return enemiesPart1.size() + enemiesPart2.size();
    }
    
}
