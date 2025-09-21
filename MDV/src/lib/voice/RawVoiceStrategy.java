/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.voice;

import java.io.IOException;

/**
 * @author Gil Costa
 */
public class RawVoiceStrategy implements VoiceStrategy{
        
    @Override
    public final byte[] decompress(byte[] data, byte[] table) throws IOException{        
        return data;
    }            
    
    @Override
    public final byte[] compress(byte[] data, byte[] table) throws IOException{
        return data;
    }
    
}
