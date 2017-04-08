/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
