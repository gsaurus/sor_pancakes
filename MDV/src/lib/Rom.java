/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.io.*;
import lib.bank.BankStrategy;
import lib.voice.VoiceStrategy;

/**
 *
 * @author Gil Costa
 */
public class Rom {
    private static final int ROM_NAME_SIZE = 48;
    public static final int ROM_START_ADDRESS = 512;
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
    
    
    public void close() throws IOException {
        if (modified) fixChecksum();
        modified = false;
        rom.close();
        rom = null;
    }
    
    public Rom(String fileName) throws FileNotFoundException{
        rom = new RandomAccessFile(fileName,"rw");
    }
    
    public Bank readBank(long address, int numSounds, byte[] table, BankStrategy bankStrategy, VoiceStrategy voiceStrategy) throws IOException{
        return Bank.read(rom, address, numSounds, table, bankStrategy, voiceStrategy);
    }
    
    public void writeBank(Bank bank, long address, BankStrategy bankStrategy) throws IOException{
        bank.write(rom, address, bankStrategy);
        modified = true;
    }
    
    
    String readName(int address) throws IOException {
        rom.seek(address);
        char[] res = new char[ROM_NAME_SIZE];
        for (int i = 0 ; i < ROM_NAME_SIZE ; ++i){
            int b = rom.readUnsignedByte();
            switch (b){
                case 0x20: res[i] = ' '; break;
                case 0x21: res[i] = '!'; break;
                case 0x22: res[i] = '"'; break;
                case 0x26: res[i] = '&'; break;
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
                    else if (b >= 0x41 && b <= 0x60)
                        res[i] = (char)('a'+ (b - 0x41));  // uppercase
                    else res[i] = (char)('a'+ (b - 0x61)); // lowercase
            }
            
        }
        return new String(res);
    }        

    public String readRomName1() throws IOException {
        return readName(0x120);        
    }
    
    public String readRomName2() throws IOException {
        return readName(0x150);        
    }
    
    
}
