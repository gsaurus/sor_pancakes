/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.Color;
import lib.map.Palette;
import java.awt.image.BufferedImage;

/**
 *
 * @author Gil
 */
public interface Renderable {

    BufferedImage asImage(Palette Pal);
    
    BufferedImage asShadow(Color color);
    
}
