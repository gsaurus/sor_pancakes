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
package main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import lib.anim.BoxFrame;




class Weapon{
    private BufferedImage[] imgs;
    private int cX, cY;
    
    private int scale(int val, float scale){
        return (int)(val*scale);
    }
    
    Weapon(String weaponName, int cX, int cY){
        imgs = new BufferedImage[8];
        this.cX = cX;
        this.cY = cY;
        for (int i = 0 ; i < 8 ; ++i){
            try {
                imgs[i] = ImageIO.read(new File("images/" + weaponName + i + ".png"));
            } catch (Exception ex) {
                // ignore
            }
        }
    }
    
    public void draw(Graphics2D g2d, int x, int y, int angle, float scale){        
        angle = angle%8;
        x+=128;
        y+=128;
        if (imgs[angle] != null){
            BufferedImage img = imgs[angle];
            g2d.drawImage(img, scale(x-cX, scale), scale(y-cY, scale), scale(img.getWidth(), scale), scale(img.getHeight(), scale), null);
        }
    }

    void drawOver(Graphics2D g2d, int x, int y, int angle, float scale) {
        angle = angle%8;
        x+=128;
        y+=128;
        if (imgs[angle] != null){
            BufferedImage img = imgs[angle];
            BufferedImage imgOver = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int i = 0 ; i < img.getWidth(); ++i){
                for (int j = 0 ; j < img.getHeight(); ++j){
                    if (((img.getRGB(i, j)>>24)&0xFF) > 0 ){
//                        imgOver.setRGB(i, j, Color.white.getRGB());
                        Color bc = new Color(img.getRGB(i, j));
                        bc = bc.brighter().brighter();
                        imgOver.setRGB(i, j, bc.getRGB());
                    }
                }
            }
            g2d.drawImage(imgOver, scale(x-cX, scale), scale(y-cY, scale), scale(img.getWidth(), scale), scale(img.getHeight(), scale), null);            
        }
    }

    void drawCircleOver(Graphics2D g2d, int x, int y, float scale) {
        x+=128;
        y+=128;
        int scaledX = scale(x,scale);
        int scaledY = scale(y,scale);
        int size = ImagePanel.OVER_WEAPON_SIZE;
        Composite oldComposite = g2d.getComposite();
        g2d.setColor(Color.red);        
        g2d.drawOval(scaledX-size, scaledY-size, size*2, size*2);
        g2d.setColor(Color.black);
        g2d.drawOval(scaledX-size-1, scaledY-size-1, (size+1)*2, (size+1)*2);
        g2d.drawOval(scaledX-size+1, scaledY-size+1, (size-1)*2, (size-1)*2);
        g2d.setColor(Color.white);        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));       
        g2d.fillOval(scale(x,scale)-ImagePanel.OVER_WEAPON_SIZE, scale(y,scale)-ImagePanel.OVER_WEAPON_SIZE, ImagePanel.OVER_WEAPON_SIZE*2, ImagePanel.OVER_WEAPON_SIZE*2);
        g2d.setComposite(oldComposite);
    }
}



class Box{
    private Color color;
    private Color bkColor;
    private int x, y, w, h;
    private BufferedImage dotImg, overDotImg;
    
    private boolean overTopLeft;
    private boolean overTopRight;
    private boolean overBottomLeft;
    private boolean overBottomRight;
    private boolean overRect;
    
    public Box(BoxFrame frame, Color color, Color bkColor, BufferedImage dotImg, BufferedImage overDotImg, boolean facedRight){
        x = frame.x;
        y = frame.y;
        w = frame.w;
        h = frame.h;
        this.color = color;
        this.bkColor = bkColor;
        this.dotImg = ImagePanel.flipImage(dotImg);
        this.overDotImg = ImagePanel.flipImage(overDotImg);
        processImgs();
        if (facedRight) flip();
    }
    
    private void processImgs(){
        for(int x = 0 ; x < dotImg.getWidth() ; ++x){
            for(int y = 0 ; y < dotImg.getHeight() ; ++y){
                int rgb = dotImg.getRGB(x, y);
                int alpha = (rgb>>24) & 0xff;
                //if (((rgb>>8) & 0xFFFFFF) == 0xFFFFFF){
                if (alpha != 0 && (rgb & 0xFFFFFF) != 0x000000){
                    dotImg.setRGB(x, y, color.getRGB());
                }
            }
        }
        for(int x = 0 ; x < overDotImg.getWidth() ; ++x){
            for(int y = 0 ; y < overDotImg.getHeight() ; ++y){
                int rgb = overDotImg.getRGB(x, y);
                if (((rgb>>8) & 0xFFFFFF) == 0xFFFFFF){
                    overDotImg.setRGB(x, y, color.getRGB());
                }
            }
        }
    }
    
    private boolean checkOver(int x, int y, int rad, int mx, int my){
        return mx > x-rad && mx < x + rad && my > y-rad && my < y+rad;
    }
    
    public boolean checkOver(int mouseX, int mouseY){
        int rad = (int)(dotImg.getWidth()*0.7);
        int scaledX = ImagePanel.scale(128+x);
        int scaledY = ImagePanel.scale(128+y);
        int scaledW = ImagePanel.scale(w);
        int scaledH = ImagePanel.scale(h);
        boolean check, res = false;
        check = checkOver(scaledX, scaledY, rad, mouseX, mouseY);
        res |= check != overTopLeft; overTopLeft = check;
        check = checkOver(scaledX+scaledW, scaledY, rad, mouseX, mouseY);
        res |= check != overTopRight; overTopRight = check;
        check = checkOver(scaledX, scaledY+scaledH, rad, mouseX, mouseY);
        res |= check != overBottomLeft; overBottomLeft = check;
        check = checkOver(scaledX+scaledW, scaledY+scaledH, rad, mouseX, mouseY);
        res |= check != overBottomRight; overBottomRight = check;
        check = (mouseX > scaledX && mouseX < scaledX+scaledW) && (mouseY > scaledY && mouseY < scaledY+scaledH);
        res |= check != overRect; overRect = check;
        return res;
    }
    
    public void flip(){
        x = -(x+w);
    }
    
//    private int getRealX(boolean facedRight){
//        if (facedRight) return -(x+w);
//        return x;
//    }
    
    private void drawCorners(Graphics2D g2d, int sx, int sy, int sw, int sh){
        int c = dotImg.getWidth()/2;
        sx-=c; sy-=c;
        if (overTopLeft) g2d.drawImage(overDotImg, sx, sy, null);
        else g2d.drawImage(dotImg, sx, sy, null);
        if (overTopRight) g2d.drawImage(overDotImg, sx+sw, sy, null);
        else g2d.drawImage(dotImg, sx+sw, sy, null);
        if (overBottomLeft) g2d.drawImage(overDotImg, sx, sy+sh, null);
        else g2d.drawImage(dotImg, sx, sy+sh, null);
        if (overBottomRight) g2d.drawImage(overDotImg, sx+sw, sy+sh, null);
        else g2d.drawImage(dotImg, sx+sw, sy+sh, null);
    }
    
    public void draw(Graphics2D g2d){
        int scaledCenter = ImagePanel.scale(128);
        int x, y, width, height;
        x = ImagePanel.scale(this.x);
        y = ImagePanel.scale(this.y);                        
        width = ImagePanel.scale(w);
        height = ImagePanel.scale(h);
        x += scaledCenter;
        y += scaledCenter;
        if (isMouseOver()){
            g2d.setColor(bkColor);
            g2d.fillRect(x, y, width, height);
        }
        g2d.setColor(color);
        g2d.drawRect(x, y, width, height);
        g2d.setColor(Color.black);
        g2d.drawRect(x-1, y-1, width+2, height+2);
        drawCorners(g2d,x,y,width,height);
    }
    
    public boolean isMouseOver(){
        return overRect || overTopLeft || overTopRight || overBottomLeft || overBottomRight;
    }
    
    void updateListenerByDrag(TheListener listener, int dx, int dy, boolean isAttack){
        if (overRect && !overTopLeft && !overTopRight && !overBottomLeft && !overBottomRight){
            int newX = x+dx; int newY = y+dy;            
            if (isAttack)
                listener.attackChanged(newX,newY,w,h);
            else listener.collisionChanged(newX,newY,w,h);
        }
    }


    void updateListener(TheListener listener, int mx, int my, boolean isAttack) {
        if (mx < -127 || my < -127 || mx > 127 || my > 127) return;
        int x, y, w, h;
        x = this.x; y = this.y; w = this.w; h = this.h;                
        if (mx > -128 && mx < 128){
            if (overTopLeft || overBottomLeft){
                if (mx < x+w && w+(x-mx) < 128){ w += x-mx; x = mx; }
            }else if (overTopRight || overBottomRight){
                if (mx > x) w = mx-x;
            }
        }
        if (my > -128 && my < 128){
            if (overTopLeft || overTopRight){
                if (my < y+h && h+(y-my) < 128){ h += y-my; y = my; }
            }else if (overBottomLeft || overBottomRight){
                if (my > y) h = my-y;
            }
        }
        if (x == this.x && y == this.y && w == this.w && h == this.h) return;
        if (isAttack)
            listener.attackChanged(x,y,w,h);
        else listener.collisionChanged(x,y,w,h);
    }

    void set(BoxFrame frame, Color color, Color bkColor, boolean facedRight) {
        x = frame.x;
        y = frame.y;
        w = frame.w;
        h = frame.h;
        this.color = color;
        this.bkColor = bkColor;
        processImgs();
        if (facedRight) flip();
    }
    
    
}

/**
 *
 * @author Gil
 */
public class ImagePanel extends javax.swing.JLabel {
    
    protected static final int OVER_WEAPON_SIZE = 16;
    
    private BufferedImage image;
    private BufferedImage ghostImage;
    private BufferedImage ghostShadow;
    private BufferedImage shadow;
    private BufferedImage hitImage;
    private BufferedImage overHitImage;
    private BufferedImage replaceImage;
    private BufferedImage previewImage;
    private int imgX, imgY;
    private static float scale = 1.f;    
    private boolean facedRight;
    
    private Box attackBox;
    private Box collisionBox;
    
    private int weaponX, weaponY;
    private int weaponAngle;
    private boolean weaponShowBehind;
    private boolean hasWeapon;
    private Weapon weapon;
    
    private boolean showHit;
    private boolean showBox;
    private boolean showWeapon;
    private boolean showCenter;
    private boolean showGhost;
    private boolean showImage;
    private boolean showShadow;
    
    private TheListener listener;
            
    private boolean mouseOverWeapon;
    private int originalMouseX, originalMouseY;
    private int oldMouseX, oldMouseY;
    private boolean isLeftButtonPressed;
    private boolean isRightButtonPressed;
    
    private Color paintColor;
    private boolean imgModified;
    
    private Mode mode;
    private Mode lastMode;
    private Cursor pencilCursor;
    private Cursor brushCursor;
    private Cursor bucketCursor;
    private Cursor dragCursor;
    
    private int brushSize;
    private int dragSpriteX, dragSpriteY;
    private int dragShiftMode;
    
    
    private Cursor createCursor(String imageName, int x, int y){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage(imageName);
        Point cursorHotSpot = new Point(x,y);
        return toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
    }
    
    private void preInitComponents(){
        showBox = false;
        showHit = true;
        showWeapon = true;
        showCenter = true;
        showImage = true;
        showShadow = false;
        facedRight = true;
        showGhost = false;
        paintColor = null;
        brushSize = 3;
        imgX = imgY = Integer.MIN_VALUE;
        pencilCursor = createCursor("images/pencil.png", 0,14);
        brushCursor = createCursor("images/brush.png", 0,13);
        bucketCursor = createCursor("images/bucket.png", 0,13);
        dragCursor = createCursor("images/drag.png", 0,0);
        setMode(Mode.none);
    }
    
    private void postInitComponents(){
        try {
            BufferedImage hit = ImageIO.read(new File("images/hit.png"));
            hitImage = hit;
            hit = ImageIO.read(new File("images/overHit.png"));
            overHitImage = hit;
            setWeaponPreview(1);
        } catch (Exception ex) {
            // ignore
        }
    }
    
    private Color getHitColor(boolean isKnockDown){
        if (isKnockDown)
            return new Color(255,0,0,255);
        else return new Color(255,255,0,255);
    }
    
    private Color getHitBackgroundColor(boolean isKnockDown){
        if (isKnockDown)
            return new Color(255, 0, 0,128);
        else return new Color(255, 255, 0, 128);
    }
    
    public void setBrushSize(int size){
        brushSize = size-1;
    }
    
    
    public float getScale() {
        return scale;
    }

    public void showGhost(boolean show){
        showGhost = show;
        repaint();
    }
    public void showHit(boolean show){
        showHit = show;
        repaint();
    }
    public void showBox(boolean show){
        showBox = show;
        repaint();
    }
    public void showWeapon(boolean show){
        showWeapon = show;
        repaint();
    }
    public void showImage(boolean show){
        showImage = show;
        repaint();
    }
    public void showShadow(boolean show){
        showShadow = show;
        repaint();
    }
    public void removeHit(){
        attackBox = null;
        repaint();
    }
    public void removeCollision(){
        collisionBox = null;
        repaint();
    }
    public void removeWeapon(){
        hasWeapon = false;
        repaint();
    }
    public void setHit(BoxFrame attack, boolean knockDown){
        if (attackBox == null)
            attackBox = new Box(attack, getHitColor(knockDown), getHitBackgroundColor(knockDown), hitImage, overHitImage, facedRight);
        else attackBox.set(attack, getHitColor(knockDown), getHitBackgroundColor(knockDown), facedRight);
        repaint();
    }
    public void setCollision(BoxFrame collision){
        if (collisionBox == null)
            collisionBox = new Box(collision, new Color(255,255,255, 255), new Color(255,255,255, 128), hitImage, overHitImage, facedRight);
        else collisionBox.set(collision, new Color(255,255,255, 255), new Color(255,255,255, 128), facedRight);
        repaint();
    }
    
    public void setWeapon(int x, int y, int angle, boolean behind){
        weaponX = x;
        weaponY = y;
        weaponAngle = angle;
        weaponShowBehind = behind;        
        hasWeapon = true;
        if (facedRight){
            weaponX*=-1;
            weaponAngle = 8-weaponAngle;
        }
        repaint();
    }
    
    public void setWeaponPreview(int id){
        switch(id){
            case 0: 
                weapon = new Weapon("knife", 16, 16);
                break;
            case 1: 
                weapon = new Weapon("pipe", 48, 43);
                break;
        }
        repaint();
    }
    
    public void updateGhost(){
        ghostImage = image;
        ghostShadow = shadow;
    }
    
    public void setImage(BufferedImage image, BufferedImage shadow) {
        replaceImage = null;
        previewImage = null;
        imgModified = false;
        dragSpriteX = dragSpriteY = 0;
        if (image == null){
            ghostImage = null;
            ghostShadow = null;
        }
        this.image = image;
        this.shadow = shadow;
//        if (facedRight){
//            this.image = flipImage(this.image);
//            this.shadow = flipImage(this.shadow);
//        }
        setScale(scale);
        if (mode == Mode.dragImage)
            changeMode(lastMode);
        repaint();
    }
    
    public void setFacedRight(boolean facedRight){
        if (this.facedRight == facedRight) return;
        this.facedRight = facedRight;                
        weaponX*=-1;
        weaponAngle=8-weaponAngle;
        if (attackBox != null) attackBox.flip();
        if (collisionBox != null) collisionBox.flip();
        repaint();
    }
    
    public boolean isFacedRight(){
        return facedRight;
    }
    
    public void setReplaceImage(BufferedImage img, int cx, int cy){
        imgX = cx;
        imgY = cy;
        setReplaceImage(img);
    }
    
    public void setMode(Mode mode){
        lastMode = this.mode;
        this.mode = mode;
        updateCursor();
        repaint();
    }
    
    private void updateCursor(){
        if ((showHit && attackBox != null && attackBox.isMouseOver()) || (showBox && collisionBox != null && collisionBox.isMouseOver()) || mouseOverWeapon){
            setCursor(Cursor.getDefaultCursor());
        }else{
            switch(mode){
                case pencil:
                    setCursor(pencilCursor);
                    break;
                case brush:
                    setCursor(brushCursor);
                    break;
                case bucket:
                    setCursor(bucketCursor);
                    break;
                case none:
                    setCursor(Cursor.getDefaultCursor());
                    break;
                case dragSprite:
                case dragImage:           
                    setCursor(dragCursor);
                    break;
            }
        }
    }
    
    private void changeMode(Mode newMode){
        if (mode != newMode){
            lastMode = mode;
            mode = newMode;
            listener.modeChanged(mode);
            updateCursor();
        }
    }
    
    public void setReplaceImage(BufferedImage img){
        previewImage = null;
        if (img == null){
            replaceImage = null;
            repaint();
            return;
        }
        imgModified = true;
        this.replaceImage = img;
        if (imgX == Integer.MIN_VALUE){
            imgX = 128-(int)(img.getWidth()*0.6);
            imgY = 128-(int)(img.getHeight()*1.0);
        }
        changeMode(Mode.dragImage);
        repaint();
    }
    
    public boolean wasImageModified(){
        return imgModified;
    }
    
    public BufferedImage getImage(){
        if (replaceImage != null){
            if (previewImage == null)
                previewImage = genPreviewImage();
            if (facedRight)
                previewImage = flipImage(previewImage);
            return previewImage;
        }
        return image;
    }
    
    public void zoom(float ammount){
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
    
    public void setColor(Color color){
        paintColor = color;
    }
    
    /**
     * Creates new form ImagePanel
     */
    public ImagePanel(TheListener listener) {
        this.listener = listener;
        preInitComponents();
        initComponents();
        postInitComponents();
    }
    
    public static int scale(int val){
        return (int)(val*scale);
    }

    
    private BufferedImage genPreviewImage() {
        if (shadow == null) return new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
        int minX = Math.max(imgX,0);
        int minY = Math.max(imgY,0);
        int width = replaceImage.getWidth();
        int height = replaceImage.getHeight();
        BufferedImage theShadow = shadow;
        if (facedRight){
            theShadow = flipImage(theShadow);
        }
        int maxX = Math.min(imgX+width,theShadow.getWidth());
        int maxY = Math.min(imgY+height,theShadow.getHeight());
        BufferedImage res = new BufferedImage(theShadow.getWidth(), theShadow.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = minX ; x < maxX ; x++){
            for (int y = minY ; y < maxY ; y++){
                int val = theShadow.getRGB(x, y);
                if (val != 0){
                    val = replaceImage.getRGB(x-imgX, y-imgY);
                    res.setRGB(x, y, val);
                }
            }
        }
        return res;
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        final int scaledCenter = scale(128);
        final int scaledBounds = scale(256);
        if (showCenter){
            g2d.setColor(Color.black);
            g2d.drawLine(scaledCenter, 0, scaledCenter, scaledBounds);            
            g2d.drawLine(0, scaledCenter, scaledBounds, scaledCenter);
        }      
        if (showGhost && ghostImage != null && ghostShadow != null){
            int scaledW = scale(ghostShadow.getWidth());
            int scaledH = scale(ghostShadow.getHeight());
            Composite original = g2d.getComposite();
            if (showShadow){
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                if (facedRight)
                    g2d.drawImage(ghostShadow, 0, 0, scaledW, scaledH, ghostShadow.getWidth(), 0, 0, ghostShadow.getHeight(), null);
                else g2d.drawImage(ghostShadow, 0, 0, scaledW, scaledH, null); 
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            scaledW = scale(ghostImage.getWidth());
            scaledH = scale(ghostImage.getHeight());
            if (facedRight)
                 g2d.drawImage(ghostImage, 0, 0, scaledW, scaledH, ghostImage.getWidth(), 0, 0, ghostImage.getHeight(), null);
            else g2d.drawImage(ghostImage, 0, 0, scaledW, scaledH, null); 
            g2d.setComposite(original);
        }
        if (shadow != null && showShadow){
            final int scaledX = scale(dragSpriteX);
            final int scaledY = scale(dragSpriteY);
            final int scaledW = scale(shadow.getWidth());
            final int scaledH = scale(shadow.getHeight());
            Composite original = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            if (facedRight)
                 g2d.drawImage(shadow, scaledX, scaledY, scaledX+scaledW, scaledY+scaledH, shadow.getWidth(), 0, 0, shadow.getHeight(), null);
            else g2d.drawImage(shadow, scaledX, scaledY, scaledW, scaledH, null);
            g2d.setComposite(original);
        }
        if (weaponShowBehind){
            if (showWeapon && hasWeapon && weapon != null){
                if (mouseOverWeapon){
                    weapon.drawOver(g2d, weaponX, weaponY, weaponAngle, scale);
                }else{
                    weapon.draw(g2d, weaponX, weaponY, weaponAngle, scale);                
                }
            }
        }
        if (image != null && replaceImage == null && showImage){
            final int scaledX = scale(dragSpriteX);
            final int scaledY = scale(dragSpriteY);
            final int scaledW = scale(image.getWidth());
            final int scaledH = scale(image.getHeight());
            if (facedRight)
                g2d.drawImage(image, scaledX, scaledY, scaledX+scaledW, scaledY+scaledH, image.getWidth(), 0, 0, image.getHeight(), null);
            else g2d.drawImage(image, scaledX, scaledY, scaledW, scaledH, null);
        }
        if (replaceImage != null){
            final int scaledX = scale(dragSpriteX);
            final int scaledY = scale(dragSpriteY);
            final int scaledW = scale(image.getWidth());
            final int scaledH = scale(image.getHeight());
            if (previewImage == null)
                previewImage = genPreviewImage();
            Composite original = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            if (facedRight)
                g2d.drawImage(image, scaledX, scaledY, scaledX+scaledW, scaledY+scaledH, image.getWidth(), 0, 0, image.getHeight(), null);
            else g2d.drawImage(image, scaledX, scaledY, scaledW, scaledH, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.drawImage(replaceImage, scale(imgX), scale(imgY), scale(replaceImage.getWidth()), scale(replaceImage.getHeight()), null);
            g2d.setComposite(original);            
            g2d.drawImage(previewImage, 0, 0, scaledW, scaledH, null);
        }
        if (showBox && collisionBox != null){
            collisionBox.draw(g2d);
        }
        if (showHit && attackBox != null){          
            attackBox.draw(g2d);
        }
        if (showWeapon && hasWeapon && weapon != null){
            if (!weaponShowBehind){
                if (mouseOverWeapon){
                    weapon.drawOver(g2d, weaponX, weaponY, weaponAngle, scale);
                }else{
                    weapon.draw(g2d, weaponX, weaponY, weaponAngle, scale);                
                }
            }
            if (mouseOverWeapon)
                weapon.drawCircleOver(g2d, weaponX, weaponY, scale);
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

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        int mouseX = evt.getX();
        int mouseY = evt.getY();
        if (showWeapon && hasWeapon && !(showHit && attackBox!=null && attackBox.isMouseOver()) && !(showBox && collisionBox!=null && collisionBox.isMouseOver())){
            int scaledWeaponX = scale(128+weaponX);
            int scaledWeaponY = scale(128+weaponY);
            int left = scaledWeaponX - OVER_WEAPON_SIZE;
            int right = scaledWeaponX + OVER_WEAPON_SIZE;
            int top = scaledWeaponY - OVER_WEAPON_SIZE;
            int bottom = scaledWeaponY + OVER_WEAPON_SIZE;
            boolean oldOver = mouseOverWeapon;
            mouseOverWeapon = mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
            if (oldOver != mouseOverWeapon){
                updateCursor();
                repaint();
            }
        }
        if (!mouseOverWeapon){
            if (!(showBox && collisionBox != null && collisionBox.isMouseOver()) && showHit && attackBox != null && attackBox.checkOver(mouseX, mouseY)){
                updateCursor();
                repaint();               
            }
            if (!(showHit && attackBox != null && attackBox.isMouseOver()) && showBox && collisionBox != null && collisionBox.checkOver(mouseX, mouseY)){
                updateCursor();
                repaint();
            }
        }
    }//GEN-LAST:event_formMouseMoved

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        originalMouseX = oldMouseX = evt.getX();
        originalMouseY = oldMouseY = evt.getY();
        isLeftButtonPressed = evt.getButton() == MouseEvent.BUTTON1;
        isRightButtonPressed = evt.getButton() == MouseEvent.BUTTON3;
        
        int x = (int)(evt.getX()/scale);
        int y = (int)(evt.getY()/scale);
        if (facedRight) x = 255-x;
        if (!(x < 0 || y < 0 || x > 255 || y > 255)){

            if (shadow != null && shadow.getRGB(x, y) != 0){
                // it's a possible area
                imgModified = true;
                Graphics2D g2d = image.createGraphics();

                if (paintColor == null){
                    AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
                    g2d.setComposite(composite);
                    g2d.setColor(new Color(0, 0, 0, 0));
                }
                else{
                    g2d.setColor(paintColor);
                }
                if (mode == Mode.pencil){
                    g2d.drawLine(x,y, x, y);
                } else if (mode == Mode.brush){
                    drawThickLine(g2d,x,y,x,y,brushSize);
                }else if (mode == Mode.bucket){
                    if (paintColor != null)
                        floodFill(image,paintColor,new Point(x,y));
                    else floodFill(image,new Color(0, 0, 0, 0),new Point(x,y));
                }
                g2d.dispose();
                listener.artChanged();
                repaint();
            }
        }                    
        
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (isLeftButtonPressed){
            int x = (int)(evt.getX()/scale);
            int y = (int)(evt.getY()/scale);
            int oldX = (int)(oldMouseX/scale);
            int oldY = (int)(oldMouseY/scale);
            int originalX = (int)(originalMouseX/scale);
            int originalY = (int)(originalMouseY/scale);
            if (showWeapon && hasWeapon && listener != null && mouseOverWeapon){
                int center = scale(128);
                listener.weaponPositionChanged((int)((evt.getX()-center)/scale), -(int)((evt.getY()-center)/scale));
            }else if (showHit && attackBox != null && listener != null && attackBox.isMouseOver()){
                int center = scale(128);
                int mx = (int)((evt.getX()-center)/scale);
                int my = (int)((evt.getY()-center)/scale); 
                attackBox.updateListener(listener, mx, my, true);
                attackBox.updateListenerByDrag(listener,x-oldX, y-oldY, true);                
            } else if (showBox && collisionBox != null && listener != null && collisionBox.isMouseOver()){
                int center = scale(128);
                int mx = (int)((evt.getX()-center)/scale);
                int my = (int)((evt.getY()-center)/scale); 
                collisionBox.updateListener(listener, mx, my, false);
                collisionBox.updateListenerByDrag(listener,x-oldX, y-oldY, false);
            }
            else {                                
                if (replaceImage != null && mode == Mode.dragImage){
                    imgX += x-oldX;
                    imgY += y-oldY;
                    previewImage = null;
                    repaint();
                }else if (mode == Mode.dragSprite){
                    int dx = x - oldX;
                    int dy = y - oldY;
                    if (evt.isShiftDown()){
//                        dragSpriteX = dragSpriteX/8;
//                        dragSpriteX *= 8;
//                        dragSpriteY = dragSpriteY/8;
//                        dragSpriteY *= 8;
                        if (dragShiftMode == 0){
                            if (Math.abs(dx) > Math.abs(dy))
                                dragShiftMode = 1;
                            else dragShiftMode = 2;
                        }
                        if (dragShiftMode == 1)
                            dragSpriteX = x-originalX;
                        else dragSpriteY = y-originalY;
                    }else{
                        dragShiftMode = 0;
                        dragSpriteX = x-originalX;
                        dragSpriteY = y-originalY;
                    }
                    repaint();
                }else{                    
                    if (facedRight){
                        x = 255-x;
                        oldX = 255-oldX;
                    }                        
                    if (!(x < 0 || y < 0 || x > 255 || y > 255 || oldX < 0 || oldY < 0 || oldX > 255 || oldY > 255)){
                        
                        if (shadow != null && shadow.getRGB(x, y) != 0 && shadow.getRGB(oldX, oldY) != 0){
                            // it's a possible area
                            //image.setRGB(x, y, paintColor.getRGB());
                            imgModified = true;
                            Graphics2D g2d = image.createGraphics();

                            if (paintColor == null){
                                AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
                                g2d.setComposite(composite);
                                g2d.setColor(new Color(0, 0, 0, 0));
                            }
                            else{
                                g2d.setColor(paintColor);
                            }
                            if (mode == Mode.pencil){
                                g2d.drawLine(oldX, oldY, x, y);
                            } else if (mode == Mode.brush){
                                drawThickLine(g2d,oldX,oldY,x,y,brushSize);
                            }
                            g2d.dispose();
                            listener.artChanged();
                            repaint();
                        }
                    } 
                }                
            }
        }
        else if (isRightButtonPressed){
            JViewport vp = (JViewport)getParent();
            Point p = vp.getViewPosition();
            p.x += originalMouseX - evt.getX();
            p.y += originalMouseY - evt.getY();
            int size = scale(255);
            if (p.x + vp.getWidth() > size) p.x = size - vp.getWidth();
            if (p.y + vp.getHeight() > size) p.y = size - vp.getHeight();
            if (p.x < 0) p.x = 0;        
            if (p.y < 0) p.y = 0;                        
            vp.setViewPosition(p);
        }
        oldMouseX = evt.getX();
        oldMouseY = evt.getY();
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if (evt.getButton() == MouseEvent.BUTTON1) isLeftButtonPressed = false;
        if (evt.getButton() == MouseEvent.BUTTON3) isRightButtonPressed = false;
        if (mode == Mode.dragSprite && (dragSpriteX != 0 || dragSpriteY != 0)){
            listener.spriteDragged(dragSpriteX, dragSpriteY);
        }
        dragShiftMode = 0;
    }//GEN-LAST:event_formMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    
    public void drawThickLine(
        Graphics g, int x1, int y1, int x2, int y2, int thickness
    ) {
        // The thick line is in fact a filled polygon
        int dX = x2 - x1;
        int dY = y2 - y1;
        // line length
        double lineLength = Math.sqrt(dX * dX + dY * dY);

        double scale = (double)(thickness) / (2 * lineLength);

        // The x,y increments from an endpoint needed to create a rectangle...
        double ddx = -scale * (double)dY;
        double ddy = scale * (double)dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        int dx = (int)ddx;
        int dy = (int)ddy;

        // Now we can compute the corner points...
        int xPoints[] = new int[4];
        int yPoints[] = new int[4];

        xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

        g.fillPolygon(xPoints, yPoints, 4);
        
        // draw spheres at the edges:
        int circleTickness = thickness+2;
        g.fillOval(x1-circleTickness/2, y1-circleTickness/2, circleTickness, circleTickness);
        g.fillOval(x2-circleTickness/2, y2-circleTickness/2, circleTickness, circleTickness);
    }



 public static void floodFill(BufferedImage img, Color fillColor, Point loc) {  
   if (loc.x < 0 || loc.x >= img.getWidth() || loc.y < 0 || loc.y >= img.getHeight()) throw new IllegalArgumentException();  
     
   WritableRaster raster = img.getRaster();  
   int[] fill =  
       new int[] {fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(),  
           fillColor.getAlpha()};  
   int[] old = raster.getPixel(loc.x, loc.y, new int[4]);  
     
   // Checks trivial case where loc is of the fill color  
   if (isEqualRgba(fill, old)) return;  
     
   floodLoop(raster, loc.x, loc.y, fill, old);  
 }  
   
 // Recursively fills surrounding pixels of the old color  
 private static void floodLoop(WritableRaster raster, int x, int y, int[] fill, int[] old) {  
   Rectangle bounds = raster.getBounds();  
   int[] aux = {255, 255, 255, 255};  
     
   // finds the left side, filling along the way  
   int fillL = x;  
   do {  
     raster.setPixel(fillL, y, fill);  
     fillL--;  
   } while (fillL >= 0 && isEqualRgba(raster.getPixel(fillL, y, aux), old));  
   fillL++;  
     
   // find the right right side, filling along the way  
   int fillR = x;  
   do {  
     raster.setPixel(fillR, y, fill);  
     fillR++;  
   } while (fillR < bounds.width - 1 && isEqualRgba(raster.getPixel(fillR, y, aux), old));  
   fillR--;  
     
   // checks if applicable up or down  
   for (int i = fillL; i <= fillR; i++) {  
     if (y > 0 && isEqualRgba(raster.getPixel(i, y - 1, aux), old)) floodLoop(raster, i, y - 1,  
         fill, old);  
     if (y < bounds.height - 1 && isEqualRgba(raster.getPixel(i, y + 1, aux), old)) floodLoop(  
         raster, i, y + 1, fill, old);  
   }  
 }  
   
 // Returns true if RGBA arrays are equivalent, false otherwise  
 // Could use Arrays.equals(int[], int[]), but this is probably a little faster...  
 private static boolean isEqualRgba(int[] pix1, int[] pix2) {  
   return pix1[0] == pix2[0] && pix1[1] == pix2[1] && pix1[2] == pix2[2] && pix1[3] == pix2[3];  
 }

    public static BufferedImage flipImage(BufferedImage image) {
        if (image == null) return null;
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        final int width = image.getWidth();
        final int height = image.getHeight();
        for (int x = 0 ; x < width ; ++x){
            for (int y = 0 ; y < height ; ++y){
                res.setRGB(width-1-x, y, image.getRGB(x, y));
            }
        }
        return res;
    }
 
 
}