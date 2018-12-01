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
package lib.elc;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import javax.imageio.ImageIO;







public class ObjectDefinition{    
    
    public final int characterId;
    public final String name;
    public final List<String> spawnModes; // formatted strings. E.g. "distance: %d pixels"
    
    public BufferedImage spriteOne;
    public BufferedImage spriteTwo;    
    
    
    public ObjectDefinition(RandomAccessFile rom, String name, int characterId, List<String> spawnModes) throws IOException{
        this.characterId = characterId;
        this.name = name;
        this.spawnModes = spawnModes;
        
        String[] tokens = name.split(" ");
        if (tokens.length > 1){
            try{
                Integer.parseInt(tokens[tokens.length - 1]);
                // If reaches here, means string ends with a number
                tokens = Arrays.copyOf(tokens, tokens.length - 1);
                name = String.join(" ", tokens);
            }catch(NumberFormatException e){
                // Nothing to do
            }
        }
        
        // read icon
        try{
            spriteOne = ImageIO.read(new File("images/objects/" + name + ".png"));
        }catch(IOException ex){
            // No image found for this object, use default
            spriteOne = ImageIO.read(new File("images/error.png"));
        }
        RescaleOp filter = new RescaleOp(
            new float[]{0.5f, 0.5f, 0.5f, 1f},
            new float[]{0, 0, 0, 0},
            null
        );
        spriteTwo = filter.filter(spriteOne, null);
    }

    
}
