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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gil
 */
public class Clipboard {
    private List<Color> myColors;
    
    public Clipboard(){
        myColors = new LinkedList<Color>();
    }
    
    public void set(Color color){
        myColors.clear();
        myColors.add(color);
    }
    
    public void set(List<Color> colors){
        myColors.clear();
        myColors.addAll(colors);
    }
    
    public List<Color> get(){
        return myColors;
    }
    
}
