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
import java.io.IOException;
import java.io.RandomAccessFile;
import lib.anim.AnimFrame;
import lib.map.Palette;
import lib.map.Sprite;







public class ItemDefinition{    
    
    public final int itemId;
    public final String name;    
    private final Sprite sprite;
    
    
    public BufferedImage loadImage(Palette palette) throws IOException{        
        return sprite.asImage(palette);
    }
    
    
    public ItemDefinition(RandomAccessFile rom, String name, int itemId, int animListAddress) throws IOException{
        this.itemId = itemId;
        this.name = name;
        
        // read a sprite from animations address
        // Used to obtain a visual representation of the item, depending on the palette used.
        rom.seek(animListAddress + itemId*4);
        int localAddress = rom.readInt();        
        rom.seek(localAddress);
        localAddress += rom.readShort();
        
        rom.seek(localAddress + 2); // discard size (short)
        AnimFrame frame = AnimFrame.read(rom, localAddress + 2, 0);
        sprite = Sprite.read(rom, frame.mapAddress, frame.artAddress);
    }

    
}
