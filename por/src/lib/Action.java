/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
