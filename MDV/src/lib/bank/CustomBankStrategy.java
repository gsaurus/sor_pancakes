/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.bank;

import lib.VoiceInfo;
import java.io.IOException;
import java.io.RandomAccessFile;
import lib.Bank;

/**
 *
 * @author Gil
 */
public class CustomBankStrategy implements BankStrategy{
    public final int headerSize;    
    public final boolean littleEndian;
    public final boolean asGlobalPointer;
    public final long baseAddress;
    public final int baseMinimumOffset;
    public final int maxSize;
    
    public boolean hasFirstBit;
    
    public final int pitch;
    public final int[] order;

    public CustomBankStrategy(int headerSize, boolean littleEndian, boolean asGlobalPointer, long baseAddress, int baseMinimumOffset, int pitch, int[] order, int maxSize) {
        this.headerSize = headerSize;
        this.littleEndian = littleEndian;
        this.asGlobalPointer = asGlobalPointer;
        this.baseAddress = baseAddress;
        this.baseMinimumOffset = baseMinimumOffset;
        this.pitch = pitch;
        this.order = order;
        if (maxSize == -1) this.maxSize = Bank.MAX_BANK_SIZE;
        else this.maxSize = maxSize;
        hasFirstBit = false;
    }
    
    
    @Override
    public int getHeaderSize() {
        return headerSize;
    }
    
    
    private long readWord(RandomAccessFile rom) throws IOException{
        if (littleEndian){
            byte b2 = rom.readByte(); // offset byte 2
            byte b1 = rom.readByte(); // offset byte 1
            return ((b1 << 8)&0xFF00) | (b2&0xFF);
        }else{
            return rom.readShort();
        }
    }
    
    private long readAddress(RandomAccessFile rom, long bankAddress) throws IOException {
        long address = readWord(rom);
        if (asGlobalPointer) return address;
        hasFirstBit = (address & 0x8000) != 0;
        address &= 0x7FFF;
        if (baseAddress > 0) return address + baseAddress;
        return  address + bankAddress;
    }
    
    private int readSize(RandomAccessFile rom) throws IOException {
        return (int)readWord(rom);
    }
    
    private int readPitch(RandomAccessFile rom) throws IOException {
        return rom.read();
    }
    
    
    @Override
    public VoiceInfo read(RandomAccessFile rom, long bankAddress, int id) throws IOException {
        
        VoiceInfo info = new VoiceInfo();
        rom.seek(bankAddress + id*getHeaderSize());
        
        for (int i = 0 ; i < order.length ; ++i){
            switch(order[i]){
                case 0: info.address = readAddress(rom, bankAddress); break;
                case 1: info.size    = readSize(rom); break;
                case 2: {
                    if (pitch < 0)
                        info.pitch = readPitch(rom);
                    else info.pitch = pitch;
                }break;
                case 3:{
                    int skipSize = headerSize-4;
                    if (pitch < 0) skipSize-=1;
                    rom.skipBytes(skipSize);
                }
            }                    
        }                
        
        return info;
    }
    
    
    private void writeWord(RandomAccessFile rom, int word) throws IOException{
        if (littleEndian){
            rom.writeShort( (((word&0xFF)<<8) | ((word&0xFF00)>>8)));
        }else{
            rom.writeShort(word);
        }
    }
    
    private long writeAddress(RandomAccessFile rom, long address, long bankAddress, int headerSize) throws IOException {
        long newAddress = address;
        // add extra bit
        if (!asGlobalPointer && hasFirstBit) newAddress |= 0x8000;
        if (baseAddress > 0){
            if (asGlobalPointer)        newAddress += baseAddress;
            if (baseMinimumOffset > 0)  newAddress += baseMinimumOffset;
        }else{
            if (asGlobalPointer) newAddress += bankAddress;
            if (baseMinimumOffset > 0) newAddress += baseMinimumOffset;
            else newAddress += headerSize;
        }
        writeWord(rom, (int)newAddress);
        
        if (asGlobalPointer) return newAddress;
        newAddress = address;
        if (baseAddress > 0){
            newAddress += baseAddress;
            if (baseMinimumOffset > 0) newAddress += baseMinimumOffset;
            return newAddress;
        }
        if (baseMinimumOffset > 0)
            return newAddress + bankAddress + baseMinimumOffset;
        else return newAddress + bankAddress + headerSize;
    }
    
    private void writeSize(RandomAccessFile rom, int size) throws IOException {
        writeWord(rom, size);
    }
    
    private void writePitch(RandomAccessFile rom, int pitch) throws IOException {
        rom.write(pitch);
    }
    
    
    
    
    @Override
    public long write(RandomAccessFile rom, int id, VoiceInfo info, long bankAddress, int headerSize) throws IOException {
        
        rom.seek(bankAddress + id*getHeaderSize());
        long finalAddress = 0;
        
        for (int i = 0 ; i < order.length ; ++i){
            switch(order[i]){
                case 0: finalAddress = writeAddress(rom, info.address, bankAddress, headerSize); break;
                case 1: writeSize(rom, info.size); break;
                case 2: if (pitch < 0) writePitch(rom, info.pitch); break;
                case 3:{
                    int skipSize = headerSize-4;
                    if (pitch < 0) skipSize-=1;
                    rom.skipBytes(skipSize);
                }
            }                    
        }  
        return finalAddress;
    }

    @Override
    public int getFirstVoiceOffset(int numVoices) {
        if (baseMinimumOffset > 0)
            return baseMinimumOffset;
        return getHeaderSize()*numVoices;
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }
    
    
}
