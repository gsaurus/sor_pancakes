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

/**
 *
 * @author Gil
 */
public class SpritePiece{
    public int x, y;
    public int width, height;
    public boolean xFliped, yFliped;
    public boolean priorityFlag;
    public int paletteLine;
    public int spriteIndex;
    
    public SpritePiece(){
        // nothing by default
    }
    
}
