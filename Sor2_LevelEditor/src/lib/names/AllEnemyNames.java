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
package lib.names;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lib.Rom;

/**
 *
 * @author gil.costa
 */
public class AllEnemyNames {
    public static final int NAME_SIZE = 8;
    public long address;
    public List<String> allNames;
    
    public AllEnemyNames(Rom rom, long address, int count) throws IOException{
        this.address = address;
        allNames = new ArrayList<>(count);
        for (int i = 0 ; i < count ; ++i){
            allNames.add(rom.readName((int) (address + i * NAME_SIZE), NAME_SIZE));
        }
    }
    
    public void write(Rom rom, long address) throws IOException{
        for (int i = 0 ; i < allNames.size() ; ++i){
            rom.writeName((int) (address + i * NAME_SIZE), allNames.get(i), NAME_SIZE);
        }
    }
    
    
    public void write(Rom rom) throws IOException{
        write(rom, address);
    }
    
}
