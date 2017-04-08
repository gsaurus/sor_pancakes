/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Gil
 */
public interface TheListener {
    public void attackChanged(int x, int y, int w, int h);
    public void collisionChanged(int x, int y, int w, int h);
    public void artChanged();  
    public void modeChanged(Mode mode);
    public void spriteDragged(int deltaX, int deltaY);

    public void weaponPositionChanged(int i, int i0);
}
