/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.bank;

import java.io.IOException;
import java.io.RandomAccessFile;
import lib.Bank;
import lib.VoiceInfo;

/**
 *
 * @author Gil
 */
public class FiveBytesBankStrategy implements BankStrategy{
    private static final int HEADER_SIZE = 5;
    
    
    @Override
    public int getHeaderSize() { return HEADER_SIZE; }
    
    
    @Override
    public VoiceInfo read(RandomAccessFile rom, long bankAddress, int id) throws IOException {
        
        VoiceInfo info = new VoiceInfo();
        rom.seek(bankAddress + id*getHeaderSize());
        
        // get audio address
        byte b2 = rom.readByte(); // offset byte 2
        byte b1 = rom.readByte(); // offset byte 1
        info.address = bankAddress + ((b1 << 8)&0xFF00) | (b2&0xFF);

        // get audio compressed size
        b2 = rom.readByte(); // size byte 2
        b1 = rom.readByte(); // size byte 1
        info.size = ((b1 << 8)&0xFF00) | (b2&0xFF);

        // get pitch
        info.pitch = rom.read();
        
        return info;
    }
    
    @Override
    public long write(RandomAccessFile rom, int id, VoiceInfo info, long bankAddress, int headerSize) throws IOException {
        
        rom.seek(bankAddress + id*getHeaderSize());
        
        // write address offset
        long offset = info.address + headerSize;
        rom.writeShort( (int)(((offset&0xFF)<<8) | ((offset&0xFF00)>>8)));
        
        // write compressed size
        rom.writeShort( (int)(((info.size&0xFF)<<8) | ((info.size&0xFF00)>>8)));
        
        // write pitch
        rom.write(info.pitch);
        
        return info.address + bankAddress + headerSize;
        
    }
    
    @Override
    public int getFirstVoiceOffset(int numVoices) {        
        return getHeaderSize()*numVoices;
    }

    @Override
    public int getMaxSize() {
        return Bank.MAX_BANK_SIZE;
    }
    
    
}
