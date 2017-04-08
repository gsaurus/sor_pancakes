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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gil Costa
 */
public class PJGuide extends Guide{
    
    private String animsListLabel;
    private String boxesListLabel;
    private String hitsListLabel;
    private String weaponsListLabel;
    private ArrayList<String> paletteLabels;
    private ArrayList<ArrayList<String>> compressedArtLabels;
    
    private String romName;
    
    public PJGuide(){
        super();
    }
    
    public void setRomFile(String romFileName){
        // Reset all addresses
        animsListAddress = boxesListAddress = hitsListAddress = weaponsListAddress = null;
        for (int i = 0 ; i < paletteAddresses.size() ; ++i){
            paletteAddresses.set(i, null);
        }
        for (int i = 0 ; i < compressedArtAddresses.size() ; ++i){
            compressedArtAddresses.set(i, null);
        }
        romName = romFileName;
    }
    
    
    private Long findAddressForLabel(String label){
        RandomAccessFile rom = null;
        try { 
            rom = new RandomAccessFile(romName,"r");
            if (label == null){
                return null;
            }
            rom.seek(0);
            int pointer = 0;
            byte[] bytes = new byte[4096];
            int matchIndex = 0;
            while(pointer < rom.length()){            
                int readBytes = rom.read(bytes);
                ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                DataInputStream dis = new DataInputStream(is);
                byte b;
                for (int i = 0 ; i < readBytes ; i++){
                    b = dis.readByte();
                    if (b == (byte)label.charAt(matchIndex)){
                        if (++matchIndex == label.length()){
                            long resultingAddress = pointer + i + 1;
                            if (resultingAddress % 2 == 1) ++resultingAddress;
                            System.out.println(Long.toHexString(resultingAddress) + ": " + label);
                            return resultingAddress;
                        }
                    }else {
                        //System.out.print("\n" + Long.toHexString(pointer + i) + " ");
                        matchIndex = 0;
                    }
                }
                pointer += readBytes;
            }
            rom.close();
        } catch (IOException ex) {
            Logger.getLogger(PJGuide.class.getName()).log(Level.SEVERE, null, ex);
            if (rom != null) try {
                rom.close();
            } catch (IOException ex1) {
                Logger.getLogger(PJGuide.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        System.out.println(label + " not found!");
        return null;
    }
    
    
    @Override
    protected void initLists(int numChars) {   
        super.initLists(numChars);
        paletteLabels = new ArrayList<String>(numChars);
        compressedArtLabels = new ArrayList<ArrayList<String>>();
    }
    
    @Override
    public void setup(String guideFileName) throws FileNotFoundException, Exception{
        File file = new File(guideFileName);
        Scanner sc = new Scanner(file);
        animsListLabel = sc.nextLine();        // address of animations list (hex value)
        boxesListLabel  = sc.nextLine();       // address of bounding boxes list (hex value)
        hitsListLabel  = sc.nextLine();        // address of hits list (hex value)
        weaponsListLabel  = sc.nextLine();     // address of weapons list (hex value)
        animsListAddress = boxesListAddress = hitsListAddress = weaponsListAddress = null;
        int numChars = stringToInt(sc.nextLine());          // number of characters
        initLists(numChars);
        for (int i = 0 ; i < numChars ; ++i){
            String name = "";
            while (name.isEmpty()) name = sc.nextLine();
            if (name.length() == 1){
                charNames.add(null);
                animsCount.add(null);
                animNames.add(null);
                paletteLabels.add(null);
                compressedArtLabels.add(null);
                paletteAddresses.add(null);
                compressedArtAddresses.add(null);
                continue;
            }
//            charNames.add(name);                            // character name
            charNames.add(Integer.toHexString(i*2) + " - " + name);
            paletteLabels.add(sc.nextLine()); // palette address
            paletteAddresses.add(null);
            StringTokenizer tk = new StringTokenizer(sc.nextLine());
            String address = tk.nextToken();
            compressedArtAddresses.add(null);
            if (address.equals("0")){
                compressedArtLabels.add(null);
            }else{
                ArrayList<String> lst = new ArrayList<String>();
                compressedArtLabels.add(lst);
                lst.add(address);
                while (tk.hasMoreElements()){
                    lst.add(tk.nextToken());
                }
            }            
            int count = stringToInt(sc.nextLine());
            animsCount.add(count);
            ArrayList<String> names = new ArrayList<String>(count);
            animNames.add(names);
            String fileName = sc.nextLine();                // file containing animations names
            try{
                Scanner animsScanner = new Scanner(new File(Guide.GUIDES_DIR + fileName));
                for (int j = 0; j < count && animsScanner.hasNextLine() ; ++j){
                    names.add( Integer.toHexString(j*2) + " - " + animsScanner.nextLine() );
//                    names.add( j+1 + " - " + animsScanner.nextLine() );
                }
            } catch(FileNotFoundException e){
                // Ignore
            }
        }
    }
    
    
    @Override
    public long getPaletteAddress(int charId){
        if( paletteAddresses.get(charId) == null){
            paletteAddresses.set(charId, findAddressForLabel(paletteLabels.get(charId)));
        }
        return paletteAddresses.get(charId);
    }

    @Override
    public long getAnimsListAddress() {
        if (animsListAddress == null){
            animsListAddress = findAddressForLabel(animsListLabel);
        }
        return animsListAddress;
    }
    @Override
    public long getBoxesListAddress(){
        if (boxesListAddress == null){
            boxesListAddress = findAddressForLabel(boxesListLabel);
        }
        return boxesListAddress;
    }
    @Override
    public long getHitsListAddress(){
        if (hitsListAddress == null){
            hitsListAddress = findAddressForLabel(hitsListLabel);
        }
        return hitsListAddress;
    }
    @Override
    public long getWeaponsListAddress(){
        if (weaponsListAddress == null){
            weaponsListAddress = findAddressForLabel(weaponsListLabel);
        }
        return weaponsListAddress;
    }
    

    @Override
    List<Long> getCompressedArtAddresses(int charId) {
        if (compressedArtAddresses.get(charId) == null){
            ArrayList<String> labels = compressedArtLabels.get(charId);
            if (labels != null){
                ArrayList<Long> addresses = new ArrayList<Long>();
                for(int i = 0 ; i < labels.size() ; ++i){
                    addresses.add( findAddressForLabel(labels.get(i)) );
                }
                compressedArtAddresses.set(charId, addresses);
            }
        }
        return compressedArtAddresses.get(charId);
    }
    
}
