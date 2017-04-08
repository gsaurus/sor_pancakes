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
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Gil
 */
public class Guide {
    public static String GUIDES_DIR = "data/";
    
    protected Long animsListAddress;
    protected Long boxesListAddress;
    protected Long hitsListAddress;
    protected Long weaponsListAddress;
    protected ArrayList<String> charNames;
    protected ArrayList<Long> paletteAddresses;
    protected ArrayList<ArrayList<Long>> compressedArtAddresses;    
    protected ArrayList<Integer> animsCount;
    protected ArrayList<ArrayList<String>> animNames;
    
    protected long hexToLong(String str){
        return Long.parseLong(str, 16);
    }
    
    protected int stringToInt(String str){
        return Integer.parseInt(str);
    }            
    
    protected void initLists(int numChars) {        
        charNames = new ArrayList<String>(numChars);        
        animsCount = new ArrayList<Integer>(numChars);
        paletteAddresses = new ArrayList<Long>(numChars);
        animNames = new ArrayList<ArrayList<String>>(numChars);
        compressedArtAddresses = new ArrayList<ArrayList<Long>>();
    }
    
    public Guide(){
        // Nothing to do
    }
    
    public void setup(String guideFileName) throws FileNotFoundException, Exception{
        File file = new File(guideFileName);
        Scanner sc = new Scanner(file);
        animsListAddress = hexToLong(sc.nextLine());        // address of animations list (hex value)
        boxesListAddress  = hexToLong(sc.nextLine());       // address of bounding boxes list (hex value)
        hitsListAddress  = hexToLong(sc.nextLine());        // address of hits list (hex value)
        weaponsListAddress  = hexToLong(sc.nextLine());     // address of weapons list (hex value)
        int numChars = stringToInt(sc.nextLine());          // number of characters
        initLists(numChars);
        for (int i = 0 ; i < numChars ; ++i){
            String name = "";
            while (name.isEmpty()) name = sc.nextLine();
            if (name.length() == 1){
                charNames.add(null);
                paletteAddresses.add(null);
                compressedArtAddresses.add(null);
                animsCount.add(null);
                animNames.add(null);
                continue;
            }
//            charNames.add(name);                            // character name
            charNames.add(Integer.toHexString(i*2) + " - " + name);
            paletteAddresses.add(hexToLong(sc.nextLine())); // palette address
            StringTokenizer tk = new StringTokenizer(sc.nextLine());
            long address = hexToLong(tk.nextToken());
            if (address == 0) compressedArtAddresses.add(null);
            else{
                ArrayList<Long> lst = new ArrayList<Long>();
                compressedArtAddresses.add(lst);
                lst.add(address);
                while (tk.hasMoreElements()){
                    lst.add(hexToLong(tk.nextToken()));
                }
            }            
            int count = stringToInt(sc.nextLine());
            animsCount.add(count);
            ArrayList<String> names = new ArrayList<String>(count);
            animNames.add(names);
            String fileName = sc.nextLine();                // file containing animations names
            try{
                Scanner animsScanner = new Scanner(new File(GUIDES_DIR + fileName));
                for (int j = 0; j < count && animsScanner.hasNextLine() ; ++j){
                    names.add( Integer.toHexString(j*2) + " - " + animsScanner.nextLine() );
//                    names.add( j+1 + " - " + animsScanner.nextLine() );
                }
            } catch(Exception e){
                // Ignore
            }
        }
    }
    
    public long getPaletteAddress(int charId){
        return paletteAddresses.get(charId);
    }

    public String getCharName(int charId) {
        return charNames.get(charId);
    }

    public int getAnimsCount(int charId) {
        return animsCount.get(charId);
    }

    public long getAnimsListAddress() {
        return animsListAddress;
    }
    public long getBoxesListAddress(){
        return boxesListAddress;
    }
    public long getHitsListAddress(){
        return hitsListAddress;
    }
    public long getWeaponsListAddress(){
        return weaponsListAddress;
    }

    public int getNumChars(){
        return charNames.size();
    }
    

    List<Long> getCompressedArtAddresses(int charId) {
        return compressedArtAddresses.get(charId);
    }
    
    public String getAnimName(int charId, int animId) {
        final ArrayList<String> charAnimNames = animNames.get(charId);
        if ( charAnimNames == null || charAnimNames.size() <= animId ) return Integer.toString(animId+1) + " - <unknown>";
        return charAnimNames.get(animId);
    }
    
    
}
