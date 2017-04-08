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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Gil
 */
public class Guide {
    public static String GUIDES_DIR = "Guides/";
    private List<String> names;
    private List<Long> addresses;
    private List<BufferedImage> images;
    private int type;
    
    public static long hexToLong(String value){
        if (value.isEmpty()) return -1;
        try{
            return Long.parseLong(value, 16);
        }catch(Exception e){
            return -1;
        }
    }
    
    public static String longToHex(long value){
        try{
            return Long.toHexString(value);
        }catch(Exception e){
            return "";
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    Guide(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
        names = new ArrayList<String>();
        addresses = new ArrayList<Long>();
        images = new ArrayList<BufferedImage>();
        type = sc.nextInt();
        while(sc.hasNextLine()){
            try{
                // read the stuff
                String name;
                do{ name = sc.nextLine();
                }while (name.isEmpty() && sc.hasNextLine());
                String address;
                do{
                    address = sc.nextLine();
                    int i = 0;
                    while (!address.isEmpty() && (address.charAt(i) == ' ' || address.charAt(i) == '\t')){
                        ++i;
                    }
                    address = address.substring(i);
                }while (address.isEmpty() && sc.hasNextLine());
                String imageName;
                do{
                    imageName = sc.nextLine();
                    int i = 0;
                    while (!imageName.isEmpty() && (imageName.charAt(i) == ' ' || imageName.charAt(i) == '\t')){
                        ++i;
                    }
                    imageName = imageName.substring(i);
                }while (imageName.isEmpty() && sc.hasNextLine());

                // load the image
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File(GUIDES_DIR + imageName));
                } catch (IOException e) {
                    //e.printStackTrace();
                }

                // store everything
                names.add(name);
                addresses.add(hexToLong(address));
                images.add(img);
            }catch(NoSuchElementException e){
                e.printStackTrace();
            }
        }
    }

    public long getAddress(int id) {
        if (id >= size() || id < 0) return 0;
        return addresses.get(id);
    }
    public String getAddressAsString(int id) {
        if (id >= size()) return "";
        return longToHex(addresses.get(id));
    }
    public BufferedImage getImage(int id) {
        if (id >= size()) return null;
        return images.get(id);
    }
    public String getName(int id) {
        if (id >= size()) return "";
        return names.get(id);
    }
    
    public int size(){
        return names.size();
    }
    
    public int getType(){
        return type;
    }
    
}
