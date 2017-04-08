/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.Color;
import java.io.*;

/**
 *
 * @author Gil
 */
public class Rom {
    public static int ROM_START_ADDRESS = 512;
    private RandomAccessFile rom;      
    private boolean modified;
    
    private void fixChecksum() throws IOException{
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
    
    public long find(long startAddress) throws IOException{        
        long ptr = startAddress;
        while(ptr < rom.length()){                        
            rom.seek(ptr);
            int a = rom.read();
            a = ((a>>4)&0xF);
            if (a <= 1){
                int i;
                for (i = 0 ; i < 4 ; ++i){
                    rom.skipBytes(1);
                    int b = rom.read();
                    b = ((b>>4)&0xF);
                    if (b != a+1){                    
                        break;
                    }else a = b;
                }
                if( i == 4) return ptr;
            }
            ptr++;
        }
        return -1;
    }
    
    public void close() throws IOException {
        if (modified) fixChecksum();
        modified = false;
        rom.close();
    }
    
    protected Rom(String fileName) throws FileNotFoundException{
        rom = new RandomAccessFile(fileName,"rw");
    }
    
    protected Palette readPalette(long address, int type) throws IOException{
        return Palette.read(rom, address, type);
    }
    
    protected void writePalette(Palette pal, long address) throws IOException{
        pal.write(rom, address);
        modified = true;
    }        
    
    
    
}
