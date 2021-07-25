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
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lib.Rom;
import lib.elc.ObjectDefinition;

/**
 *
 * @author Gil
 */
public class Guide {
    public static String GUIDES_DIR = "guides/";
    
    private static String INVALID_CHARACTER = "x";
    
    public long levelsLoadcuesAddress;
    public long enemyNamesAddress;
    public int totalNumberOfEnemyNames;
    public int numberOfMainCharacters;
    public int numberOfLevels;
    public List<ObjectDefinition> objectsDefinition = new ArrayList<>(50);
    
    
    private String readLine(Scanner scanner){
        String text;
        text = scanner.nextLine();
        int indexOfComments = text.indexOf("//");
        if (indexOfComments >= 0){
            text = text.substring(0, indexOfComments);
        }
        return text.trim();
    }
    
    private String findNextLine(Scanner scanner){
        String text = "";
        while (text.isEmpty() && scanner.hasNextLine()){
            text = readLine(scanner);
        }
        return text;
    }
    
    private int readInt(Scanner scanner){
        String text = readLine(scanner);
        return Integer.parseInt(text);
    }
    
    private long readHexValue(Scanner scanner){
        String text = readLine(scanner);
        return Long.parseLong(text, 16);
    }
    
    private long readAddress(Rom rom, Scanner scanner, byte[] allData) throws IOException{
        String text = readLine(scanner);
        if (text.startsWith("0x")){
            return Long.parseLong(text.substring(2), 16);
        }else{
            return rom.findLabel(allData, text);
        }
    }
    
    
    public Guide(String guideFileName, Rom rom) throws FileNotFoundException, Exception{
        File file = new File(guideFileName);
        Scanner scanner = new Scanner(file);
        byte[] allData = rom.getAllData();
        
        levelsLoadcuesAddress = readAddress(rom, scanner, allData);
        enemyNamesAddress = readAddress(rom, scanner, allData);
        
        totalNumberOfEnemyNames = readInt(scanner);
        numberOfMainCharacters = readInt(scanner);
        numberOfLevels = readInt(scanner);
                
        // Read character spawning mode guides
        String characterName;
        while(scanner.hasNextLine()){
            characterName = findNextLine(scanner);
            if (characterName.equals(INVALID_CHARACTER)){
                objectsDefinition.add(null);
                continue;
            }
            if (characterName.isEmpty()) break;
            // Read character spawning modes
            List<String> spawningModes = new ArrayList<>(5);
            String spawningMode;
            while(scanner.hasNextLine()){
                spawningMode = readLine(scanner);
                if (spawningMode.isEmpty()) break;
                spawningModes.add(spawningMode);
            }
            System.out.println("Reading " + characterName + " definition");
            
            objectsDefinition.add(
                    new ObjectDefinition(
                            rom.getRomFile(),
                            characterName,
                            objectsDefinition.size() * 2,
                            spawningModes
                    )
            );
        }
        
    }
    
    
    
    public static void main(String[] args){
        try {
//            Rom rom = new Rom(new File("sor2.bin"));
//            new Guide(GUIDES_DIR + "default.txt", rom);
            Rom rom = new Rom(new File("sor2built 2.bin"));
            new Guide(GUIDES_DIR + "syndicate_wars.txt", rom);
            rom.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
}
