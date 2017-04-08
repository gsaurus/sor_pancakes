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
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Gil
 */
public class Guide {
    public static String GUIDES_DIR = "guides/";
    
    private long animsListAddress;
    private long hitsListAddress;
    private long weaponsListAddress;
    private long portraitsListAddress;
    private ArrayList<Integer> skips;
    private ArrayList<String> charNames;
    private ArrayList<Long> paletteAddresses;
    private ArrayList<Integer> animTypes;
    private ArrayList<Integer> animsCount;
    private ArrayList<ArrayList<String>> animNames;
    private TreeMap<Integer, Long> compressedArtAddresses;
    
    private long hexToLong(String str){
        return Long.parseLong(str, 16);
    }
    
    private int stringToInt(String str){
        return Integer.parseInt(str);
    }
    
    public void setAnimType(int charId, int newType){
        animTypes.set(charId, newType);
    }
    
    public int getRealCharId(int id){
        int res = id;
        for(int nextSkip : skips){
            if (res >= nextSkip){                
                res++;
            }else break;
        }        
        return res;
    }
    
    public int getFakeCharId(int id){
        int res = id;
        for(int nextSkip : skips){
            if (id >= nextSkip)
                res--;
            else break;
        }
        return res;
    }
    
    private void initLists(int numChars) {
        skips = new ArrayList<Integer>();
        charNames = new ArrayList<String>(numChars);
        animNames = new ArrayList<ArrayList<String>>(numChars);
        animTypes = new ArrayList<Integer>(numChars);
        animsCount = new ArrayList<Integer>(numChars);
        paletteAddresses = new ArrayList<Long>(numChars);
        compressedArtAddresses = new TreeMap<Integer,Long>();
    }
    
    public Guide(String guideFileName) throws FileNotFoundException, Exception{
        File file = new File(guideFileName);
        String fileDir = file.getParent();
        Scanner sc = new Scanner(file);
        animsListAddress = hexToLong(sc.nextLine());    // address of animations list (hex value)
        hitsListAddress  = hexToLong(sc.nextLine());    // address of hits list (hex value)
        weaponsListAddress  = hexToLong(sc.nextLine()); // address of weapons list (hex value)
        portraitsListAddress = hexToLong(sc.nextLine());// address of miniportraits list (hex value)
        int numChars = stringToInt(sc.nextLine());      // number of characters
        initLists(numChars);
        for (int i = 0 ; i < numChars ; ++i){
            String name = "";
            while (name.isEmpty()) name = sc.nextLine();
            if (name.length() == 1){
                skips.add(i);
                continue;
            }
            charNames.add(name);                            // character name
            paletteAddresses.add(hexToLong(sc.nextLine())); // palette address
            final int type = stringToInt(sc.nextLine());
            animTypes.add(type);      // type of animation scripts
            if (type == 1){
                final String line = sc.nextLine();
                compressedArtAddresses.put(i, hexToLong(line));
            }
            int count = stringToInt(sc.nextLine());         // number of animations
            animsCount.add(count);
            ArrayList<String> names = new ArrayList<String>(count);
            animNames.add(names);
            String fileName = sc.nextLine();                // file containing animations names
            try{
                Scanner animsScanner = new Scanner(new File(fileDir + "/" + fileName));
                for (int j = 0; j < count && animsScanner.hasNextLine() ; ++j){
                    names.add( j+1 + " - " + animsScanner.nextLine() );
                }
            } catch(Exception e){
                // Ignore
            }
        }
    }

    public String getAnimName(int charId, int animId) {
        final ArrayList<String> charAnimNames = animNames.get(charId);
        if ( charAnimNames.size() <= animId ) return "Animation " + Integer.toString(animId+1);        
        return charAnimNames.get(animId);
    }
    
    public long getPaletteAddress(int charId){
        return paletteAddresses.get(charId);
    }

    public String getCharName(int charId) {
        return charNames.get(charId);
    }

    public int getType(int charId) {
        return animTypes.get(charId);
    }

    public int getAnimsCount(int charId) {
        return animsCount.get(charId);
    }

    public long getAnimsListAddress() {
        return animsListAddress;
    }

    public long getHitsListAddress() {
        return hitsListAddress;
    }
    
    public long getWeaponsListAddress(){
        return weaponsListAddress;
    }
    
    public long getPortraitsListAddress(){
        return portraitsListAddress;
    }
    
    public int getNumChars(){
        return charNames.size();
    }
    
    public long getCompressedArtAddress(int charId){
        Long res = compressedArtAddresses.get(charId);
        if (res == null) return 0;
        return res;
    }
    
    
}
