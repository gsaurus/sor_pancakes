/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.voice;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Gil Costa
 */
public interface VoiceStrategy {    
    public byte[] compress(byte[] data, byte[] table) throws IOException;
    public byte[] decompress(byte[] data, byte[] table) throws IOException;    
}
