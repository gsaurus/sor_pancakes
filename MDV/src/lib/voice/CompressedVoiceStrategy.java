/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.voice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.TreeMap;


final class CompressionState{
    public static final int INITIAL_OUT = 0x80;
    public int out;
    public int dac;
    
    public CompressionState(){
        super();
        out = INITIAL_OUT;
        dac = 0;
    }
}

/**
 * @author Gil Costa
 */
public class CompressedVoiceStrategy implements VoiceStrategy{

    
    private void decompressByte(int index, byte[] table, CompressionState state, ByteArrayOutputStream dos){
        if (index == 0){
            // decompress a loop
            for (byte i = table[0] ; i > 0 ; --i){
                state.out += state.dac;
                dos.write(state.out);
            }
        }else{
            // decompress a byte
            state.dac = table[index];
            state.out += state.dac;
            dos.write(state.out);
        }
    }
    
    @Override
    public final byte[] decompress(byte[] data, byte[] table) throws IOException{        
        // decompression state
        CompressionState state = new CompressionState();
        // output stream for the decompressed bytes
        ByteArrayOutputStream dos = new ByteArrayOutputStream();
        
        // decompression loop
        for (int i = 0 ; i < data.length ; ++i){
            // get two nibbles from each byte
            int b = data[i];
            int s1 = (b >> 4)&0xF;
            int s2 = b&0xF;
            // and decompress them into the output stream
            decompressByte(s1, table, state, dos);
            decompressByte(s2, table, state, dos);            
        }
        // return the data from the stream
        return dos.toByteArray();
    }
    
    
    
    // ------------------------------------------------------------------------
    
    
    // Aproximate the source byte to the closest DAC from the table
    protected static byte aproximateByte(byte src, byte[] table) {
        int distance = 0xFFF; byte trueValue = 0;
        for (int j = 1 ; j < table.length && distance != 0; ++j){
            int d = Math.abs(src-table[j]);
            if (d < distance){
                distance = d;
                trueValue = table[j];
            }
        }
        return trueValue;
    }
     
    
    // Generate a handy reverse table for easily indexing IDs from DAC values
    protected static TreeMap<Byte, Byte> createReverseTable(byte[] table){
        TreeMap<Byte, Byte> res = new TreeMap<Byte, Byte>();
        for (int i = 1 ; i < table.length ; ++i){
            res.put(table[i], (byte)i);
        }
        return res;
    }
     

    // Algorithm to generate the dacs array
    private static byte[] genDacArray(byte[] data, byte[] table){
        byte[] dac = new byte[data.length];
        int out = CompressionState.INITIAL_OUT;
        for (int i = 0 ; i < data.length ; ++i){
            dac[i] = (byte)(data[i] - out);
            dac[i] = aproximateByte(dac[i], table);            
            out += dac[i];
        }
        return dac;
    }
    
    
    // Algorithm to compress the dacs array into the final data
    private static byte[] compressDacArray(byte[] dac, byte[] table){
        if (dac.length == 0) return new byte[0];
        ByteArrayOutputStream dos = new ByteArrayOutputStream();
        
        // we'll use the reverse table as lookup table to fill the output
        TreeMap<Byte, Byte> reverseTable = createReverseTable(table);        
        byte prev = dac[0];     // previous dac
        byte bitsOut = reverseTable.get(prev);
        int loop = table[0];    // number of dacs looping for ID zero
        byte bits = 0;          // output bits, (4 bits on the right and 4 bits on the left)
        boolean left = true;   // if we're writing the left bits (and not the right bits)
        
        int i = 1;
        while (i < dac.length){
            
            // write the last processed nibble            
            if (left){
                bits = (byte)(bitsOut << 4);
            }else{
                bits |= bitsOut;
                dos.write(bits);
            }
            left = !left;
            
            // check if next nibbles are looping
            byte next = dac[i];
            byte nextInLoop = next;
            int repetition = 0;            
            while (repetition < loop && i+repetition < dac.length && nextInLoop == prev){
                ++repetition;
                if (i+repetition == dac.length) break; // TODO: this is ad hoc
                nextInLoop = dac[i+repetition];
            }
            if (repetition == loop && repetition > 0){
                i += repetition;    // skip loop
                bitsOut = 0;        // write zero, which means loop
            }else{
                // move to next nibble
                prev = next;
                bitsOut = reverseTable.get(prev);
                ++i;
            }
        }
        
        // write the last processed bits
        if (left){
            bits = (byte)(bitsOut << 4);            
            dos.write(bits);
        }else{
            bits |= bitsOut;
            dos.write(bits);
        }
        
        // return the resulting byte array
        return dos.toByteArray();
    }
    
    
    @Override
    public final byte[] compress(byte[] data, byte[] table) throws IOException{
        // generate the dac's array
        byte[] out = genDacArray(data, table);
        // compress the resulting dac's array
        return compressDacArray(out, table);
    }
    
}
