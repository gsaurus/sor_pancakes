/* 
 * Copyright 2017 Gil Costa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lib.map;

import java.io.*;
import lib.RandomDataStream;



/**
 *
 * @author Gil
 */
public class Compressor {
    private static final String TEMP_FILE_NAME = "temp.bin";
    
    public static RandomDataStream run(String romName, long artAddress) throws Exception{
        String[] env = new String[4];
        env[0] = "data\\nemcmp.exe";
        env[1] = romName;
        env[2] = TEMP_FILE_NAME;
        env[3] = Long.toHexString(artAddress);
        Process p = Runtime.getRuntime().exec(env);
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            throw new Exception("Art decompression failed");
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
