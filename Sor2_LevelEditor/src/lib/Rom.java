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

import java.io.*;
import java.util.HashMap;

/**
 *
 * @author Gil
 */
public class Rom {
    public static final int ROM_START_ADDRESS = 512;
    private RandomAccessFile rom;
    private HashMap<String, Long> knownLabels = new HashMap<>();
     
    public void fixChecksum() throws IOException{
        short checksum = 0;
        rom.seek(ROM_START_ADDRESS);
        byte[] bytes = new byte[4096];
        int pointer = ROM_START_ADDRESS;
        while(pointer < rom.length()){            
            int readBytes = rom.read(bytes);
            pointer += readBytes;
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(is);
            for (int i = 0 ; i < readBytes-1 ; i+=2){
                checksum += dis.readShort();
            }
        }
        rom.seek(398);
        rom.writeShort(checksum);
        rom.seek(420);
        rom.writeInt((int)rom.length()-1);
    }
    
    
    public byte[] getAllData() throws IOException {
        byte[] data = new byte[(int)rom.length()];
        rom.read(data);
        return data;
    }
    
    
    public long findLabel(byte[] data, String label) throws IOException {
        Long value = knownLabels.get(label);
        if (value != null) return value;
        String matching = label;
        int cardIndex = label.indexOf('#');
        if (cardIndex > 0)
            matching = label.substring(0, cardIndex);
        byte[] pattern = matching.getBytes();
        long index = KMPMatch.indexOf(data, pattern);
        if (index != -1L) {
            index += label.length();
            if (index % 2L != 0L) {
                index += 1L;
            }
        }
        knownLabels.put(label, index);
        return index;
    }
    
    
    public RandomAccessFile getRomFile(){
        return rom;
    }
    
    public void close() throws IOException {
        fixChecksum();
        rom.close();
    }
    
    public Rom(File file) throws FileNotFoundException{
        rom = new RandomAccessFile(file,"rw");
    }    
    
    public String readName(int address, int size) throws IOException {
        rom.seek(address);
        char[] res = new char[size];
        for (int i = 0 ; i < size ; ++i){
            int b = rom.readUnsignedByte();
            switch (b){
                case 0x20: res[i] = ' '; break;
                case 0x21: res[i] = '!'; break;
                case 0x22: res[i] = '"'; break;
                case 0x27: res[i] = '\''; break;
                case 0x28: res[i] = '('; break;
                case 0x29: res[i] = ')'; break;
                case 0x2C: res[i] = ','; break;
                case 0x2E: res[i] = '.'; break;    
                case 0x2F: res[i] = '/'; break;
                case 0x3A: res[i] = '*'; break;
                case 0x3F: res[i] = '?'; break;
                case 0x40: res[i] = '©'; break;
                default:
                    if (b >= 0x30 && b <= 0x39)
                        res[i] = (char)('0'+ (b - 0x30));
                    else res[i] = (char)('A'+ (b - 0x41));
            }
            
        }
        return new String(res);
    }

    public void writeName(int speedAddress, String newName, int size) throws IOException {
        rom.seek(speedAddress);
        for (int i = 0 ; i < size ; ++i){
            char c = ' ';
            if (i < newName.length()){
                c = newName.charAt(i);
            }
            byte b = 0;
            switch (c){
                case ' ': b = 0x20; break;
                case '!': b = 0x21; break;
                case '"': b = 0x22; break;
                case '\'': b = 0x27; break;
                case '(': b = 0x28; break;
                case ')': b = 0x29; break;
                case ',': b = 0x2C; break;
                case '.': b = 0x2E; break;
                case '/': b = 0x2F; break;
                case '*': b = 0x3A; break;
                case '?': b = 0x3F; break;
                case '©': b = 0x40; break;
                default:
                    if (c >= '0' && c <= '9')
                        b = (byte)(0x30 + (c-'0'));
                    else b = (byte)(0x41 + (c-'A'));
            }
            rom.writeByte( b );
        }
    }
    
}
