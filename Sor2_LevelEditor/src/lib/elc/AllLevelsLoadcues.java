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
public class AllLevelsLoadcues {
    public static final int NUM_LEVELS = 9;
    public long address;
    public final List<LevelLoadcues> levels = new ArrayList<LevelLoadcues>(NUM_LEVELS);
    
    public AllLevelsLoadcues(RandomAccessFile rom, long address) throws IOException{
        this.address = address;
        for (int i = 0; i < NUM_LEVELS ; ++i){
            rom.seek(address);
            long levelElcAddress = address + rom.readUnsignedShort();            
            // Load first scene ELC only
            rom.seek(levelElcAddress);
            long firstSceneElcAddress = levelElcAddress + rom.readUnsignedShort();
            levels.add(new LevelLoadcues(rom, firstSceneElcAddress));
            address += 2L;
        }
    }
    
    public void write(RandomAccessFile rom, long address, int deltaObjectId) throws IOException{
        for (LevelLoadcues level: levels){
            rom.seek(address);
            long levelElcAddress = address + rom.readUnsignedShort();            
            // store in first scene ELC only
            rom.seek(levelElcAddress);
            long firstSceneElcAddress = levelElcAddress + rom.readUnsignedShort();
            level.write(rom, firstSceneElcAddress, deltaObjectId);
            address += 2L;
        }
    }
    
    public void write(RandomAccessFile rom) throws IOException{
        write(rom, address, 0);
    }
    
    
    public static void main(String[] args){
        try {
//            Rom rom = new Rom(new File("sor2.bin"));
//            AllLevelsLoadcues obj = new AllLevelsLoadcues(rom.getRomFile(), 0x1EF49C);
            Rom rom = new Rom(new File("sor2built 2.bin"));
            AllLevelsLoadcues obj = new AllLevelsLoadcues(rom.getRomFile(), 0x15411e);
            obj.write(rom.getRomFile());
            rom.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
