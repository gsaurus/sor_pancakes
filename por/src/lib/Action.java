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
package lib;

import java.awt.Color;

/**
 *
 * @author Gil
 */
public class Action {
    private int offset;
    private Color newColor;
    private Color oldColor;

    protected Action(int offset, Color oldColor, Color newColor) {
        this.offset = offset;
        this.newColor = newColor;
        this.oldColor = oldColor;
    }
    
    public int getOffset() {
        return offset;
    }

    protected Color getNewColor() {
        return newColor;
    }
    protected Color getOldColor() {
        return oldColor;
    }
    
    protected void setNewColor(Color newColor){
        this.newColor = newColor;
    }
    
    
}
