/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import lib.map.Palette;

/**
 *
 * @author Gil
 */
public class PaletteImagePanel extends javax.swing.JLabel {

    float getScale() {
        return scale;
    }
    
    class Point{
        public int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point() {
            x = y = 0;
        }
        
    }

    private BufferedImage image;
    private float scale = 2.f;
    private ArrayList<ArrayList<Point>> mapping;

    
    private static int trunkLeft(BufferedImage img){
        for (int x = 0 ; x < img.getWidth() ; ++x){
            for (int y = 0 ; y < img.getHeight() ; ++y){
                int rgbVal = img.getRGB(x, y);
                if (((rgbVal>>24) & 0xff) != 0){
                    return x;
                }
            }
        }
        return img.getWidth()-1;
    }
    private static int trunkRight(BufferedImage img){
        for (int x = img.getWidth()-1 ; x >= 0 ; --x){
            for (int y = 0 ; y < img.getHeight() ; ++y){
                int rgbVal = img.getRGB(x, y);
                if (((rgbVal>>24) & 0xff) != 0){
                    return x+1;
                }
            }
        }
        return 0;
    }
    private static int trunkTop(BufferedImage img){
        for (int y = 0 ; y < img.getHeight() ; ++y){
            for (int x = 0 ; x < img.getWidth() ; ++x){
                int rgbVal = img.getRGB(x, y);
                if (((rgbVal>>24) & 0xff) != 0){
                    return y;
                }
            }
        }
        return 0;
    }
    private static int trunkBottom(BufferedImage img){
        for (int y = img.getHeight()-1 ; y >= 0 ; --y){
            for (int x = 0 ; x < img.getWidth() ; ++x){
                int rgbVal = img.getRGB(x, y);
                if (((rgbVal>>24) & 0xff) != 0){
                    return y+1;
                }
            }
        }
        return 0;
    }
    
    private static BufferedImage deepCopy(BufferedImage bi) {
        int left = trunkLeft(bi);
        int right = trunkRight(bi);
        int top = trunkTop(bi);
        int bottom = trunkBottom(bi);
        
        BufferedImage res = new BufferedImage(right-left, bottom-top, BufferedImage.TYPE_INT_ARGB);
        for (int x = left ; x < right ; ++x){
            for (int y = top ; y < bottom ; ++y){
                int color = bi.getRGB(x, y);
                if (((color>>24) & 0xff) != 0){
                    res.setRGB(x-left, y-top, color);
                }
            }
        }
        return res;
    }

    
    private void generateMap(){
        if (image == null || image.getWidth() < 16 || image.getHeight() == 0){
            mapping = null;
            return;
        }
        mapping = new ArrayList<ArrayList<Point>>(16);
        int[] originalColors = new int[16];
        for (int x = 0 ; x < 16 ; ++x){
            originalColors[x] = Palette.correctRGB(image.getRGB(x, 0));
            mapping.add(new ArrayList<Point>());
        }
        
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0 ; y < height ; ++y){
            for (int x = 0 ; x < width ; ++x){
                int color = image.getRGB(x, y);
                if (((color>>24) & 0xff) != 0){
                    color = Palette.correctRGB(image.getRGB(x, y));
                    for (int i = 0 ; i < 16 ; ++i){
                        if (color == originalColors[i]){
                            mapping.get(i).add(new Point(x,y));
                            break;
                        }
                    }
                }
            }
        }
        
    }
    
    
    public void setImage(BufferedImage image, Palette pal) {
        if (image == null)
            this.image = null;
        else{
            this.image = deepCopy(image);
            int[] old = new int[16];     
            for (int x = 0 ; x < 16 ; ++x){
                old[x] = image.getRGB(x, 0);
                this.image.setRGB(x, 0, pal.getColor(x));
            }   
            generateMap();            
            for (int x = 0 ; x < 16 ; ++x){
                this.image.setRGB(x, 0, old[x]);
            }
            setScale(scale);
        }
        invalidate();
        repaint();
    }
    
    public void scale(float ammount){
        if (ammount+scale > 0)
            setScale(scale + ammount);
    }
    
    public void setScale(float scale){
        if (scale >0 && image != null){
            this.scale = scale;
            this.setBounds(0, 0, (int)(image.getWidth()*scale), (int)(image.getHeight()*scale));
            repaint();
        }
    }
    
    public void replaceColor(int id, Color color){
        ArrayList<Point> map = mapping.get(id);
        int newColor = color.getRGB();
        for(Point p: map){
            image.setRGB(p.x, p.y, newColor);
        }
        repaint();
    }
    
    public BufferedImage getImage(){
        return image;
    }
    
    /**
     * Creates new form ImagePanel
     */
    public PaletteImagePanel() {
        initComponents();
    }

    @Override
    public void paint(Graphics g) {
        if (image != null){
            g.drawImage(image, 0, 0, (int)(image.getWidth()*scale), (int)(image.getHeight()*scale), null);
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        if (image != null)
            return new Dimension((int)(image.getWidth()*scale), (int)(image.getHeight()*scale));
        else return super.getPreferredSize();
    }

    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
