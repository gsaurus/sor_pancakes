/*
 * Decompiled with CFR 0_132.
 */
package lib.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lib.RandomDataStream;

public class SillyDecompressor {
    private static final String TEMP_FILE_NAME = "temp.bin";

    public static RandomDataStream run(String romName, long artAddress) throws IOException {
        String[] env = new String[]{"sor2decompressor.exe", romName, Long.toString(artAddress), TEMP_FILE_NAME};
        Process p = Runtime.getRuntime().exec(env);
        try {
            p.waitFor();
        }
        catch (InterruptedException ex) {
            throw new IOException("Decompression timeout");
        }
        File tempFile = new File(TEMP_FILE_NAME);
        FileInputStream fis = new FileInputStream(tempFile);
        RandomDataStream out = new RandomDataStream();
        while (fis.available() > 0) {
            out.write((byte)fis.read());
        }
        fis.close();
        tempFile.delete();
        return out;
    }
}

