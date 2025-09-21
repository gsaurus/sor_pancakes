/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.bank;

import lib.VoiceInfo;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Gil
 */
public interface BankStrategy {
    public VoiceInfo read(RandomAccessFile rom, long bankAddress, int id) throws IOException;        
    public long write(RandomAccessFile rom, int id, VoiceInfo info, long bankAddress, int headerSize) throws IOException;
    public int getHeaderSize();
    public int getFirstVoiceOffset(int numVoices);
    public int getMaxSize();
}
