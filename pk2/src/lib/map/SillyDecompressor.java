/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.map;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.RandomDataStream;



/**
 *
 * @author Gil
 */
public class SillyDecompressor {
    private static final String TEMP_FILE_NAME = "temp.bin";
    
    public static RandomDataStream run(String romName, long artAddress) throws IOException{
        String[] env = new String[4];
        env[0] = "sor2decompressor.exe";
        env[1] = romName;
        env[2] = Long.toString(artAddress);
        env[3] = TEMP_FILE_NAME;
        Process p = Runtime.getRuntime().exec(env);
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            throw new IOException("Decompression timeout");
        }
        File tempFile = new File(TEMP_FILE_NAME);
        FileInputStream fis = new FileInputStream(tempFile);
        RandomDataStream out = new RandomDataStream();
        while (fis.available() > 0){
            out.write((byte)fis.read());
        }
        fis.close();
        tempFile.delete();        
        return out;
    }
        
}
