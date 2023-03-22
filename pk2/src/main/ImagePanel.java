/*
 * Decompiled with CFR 0_132.
 */
package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JViewport;

public class ImagePanel
extends JLabel {
    protected static final int OVER_WEAPON_SIZE = 16;
    private static final int X_TRANSFORMER = -2;
    private static final int Y_TRANSFORMER = -1;
    private BufferedImage image;
    private BufferedImage ghostImage;
    private BufferedImage ghostShadow;
    private BufferedImage shadow;
    private BufferedImage hitImage;
    private BufferedImage overHitImage;
    private BufferedImage replaceImage;
    private BufferedImage previewImage;
    private int imgX;
    private int imgY;
    private float scale = 2.0f;
    private int hitX;
    private int hitY;
    private boolean hasHit;
    private boolean isKnockdown;
    private boolean facedRight;
    private int weaponX;
    private int weaponY;
    private int weaponAngle;
    private boolean weaponShowBehind;
    private boolean hasWeapon;
    private Weapon weapon;
    private boolean showHit;
    private boolean showWeapon;
    private boolean showCenter;
    private boolean showGhost;
    private boolean showImage;
    private boolean showShadow;
    private TheListener listener;
    private boolean mouseOverHit;
    private boolean mouseOverWeapon;
    private int originalMouseX;
    private int originalMouseY;
    private int oldMouseX;
    private int oldMouseY;
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
    private Point pivot;
    private int dragSpriteX;
    private int dragSpriteY;
    private int dragShiftMode;

    private Cursor createCursor(String imageName, int x, int y) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage(imageName);
        Point cursorHotSpot = new Point(x, y);
        return toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
    }

    private void preInitComponents() {
        this.showHit = true;
        this.showWeapon = true;
        this.showCenter = true;
        this.showImage = true;
        this.showShadow = true;
        this.facedRight = true;
        this.showGhost = false;
        this.paintColor = null;
        this.brushSize = 3;
        this.imgY = Integer.MIN_VALUE;
        this.imgX = Integer.MIN_VALUE;
        this.pencilCursor = this.createCursor("images/pencil.png", 0, 14);
        this.brushCursor = this.createCursor("images/brush.png", 0, 13);
        this.bucketCursor = this.createCursor("images/bucket.png", 0, 13);
        this.dragCursor = this.createCursor("images/drag.png", 0, 0);
        this.setMode(Mode.none);
    }

    private void postInitComponents() {
        try {
            BufferedImage hit;
            this.hitImage = hit = ImageIO.read(new File("images/hit.png"));
            this.overHitImage = hit = ImageIO.read(new File("images/overHit.png"));
            this.setWeaponPreview(0);
        }
        catch (IOException ex) {
            // empty catch block
        }
    }

    private Color getHitColor() {
        if (this.isKnockdown) {
            return new Color(255, 0, 0, 128);
        }
        return new Color(255, 200, 0, 128);
    }

    private Color getHitBackgroundColor() {
        if (this.isKnockdown) {
            return new Color(255, 0, 0, 96);
        }
        return new Color(255, 255, 0, 96);
    }

    public void setBrushSize(int size) {
        this.brushSize = size - 1;
    }

    public float getScale() {
        return this.scale;
    }

    public void showGhost(boolean show) {
        this.showGhost = show;
        this.repaint();
    }

    public void showHit(boolean show) {
        this.showHit = show;
        this.repaint();
    }

    public void showWeapon(boolean show) {
        this.showWeapon = show;
        this.repaint();
    }

    public void showImage(boolean show) {
        this.showImage = show;
        this.repaint();
    }

    public void showShadow(boolean show) {
        this.showShadow = show;
        this.repaint();
    }

    public void removeHit() {
        this.hasHit = false;
        this.repaint();
    }

    public void removeWeapon() {
        this.hasWeapon = false;
        this.repaint();
    }

    public void setHit(int x, int y, boolean knockdown) {
        this.hitX = x * -2;
        this.hitY = y * -1;
        if (this.facedRight) {
            this.hitX *= -1;
        }
        this.isKnockdown = knockdown;
        this.hasHit = true;
        this.repaint();
    }

    public void setWeapon(int x, int y, int angle, boolean behind) {
        this.weaponX = x;
        this.weaponY = y;
        this.weaponAngle = angle;
        this.weaponShowBehind = behind;
        this.hasWeapon = true;
        if (this.facedRight) {
            this.weaponX *= -1;
            this.weaponAngle = 8 - this.weaponAngle;
        }
        this.repaint();
    }

    public void setWeaponPreview(int id) {
        switch (id) {
            case 0: {
                this.weapon = new Weapon("knife", 16, 16);
                break;
            }
            case 1: {
                this.weapon = new Weapon("pipe", 48, 43);
            }
        }
        this.repaint();
    }

    public void updateGhost() {
        this.ghostImage = this.image;
        this.ghostShadow = this.shadow;
    }

    public void setImage(BufferedImage image, Point pivot) {
        this.replaceImage = null;
        this.previewImage = null;
        this.imgModified = false;
        this.dragSpriteX = 0;
        this.dragSpriteY = 0;
        this.pivot = new Point(128, 128);
        if (pivot != null)
        {
            this.pivot.x -= pivot.x;
            this.pivot.y += pivot.y;
        }
        if (image == null) {
            this.ghostImage = null;
            this.ghostShadow = null;
        }
        this.image = image;
        this.shadow = shadow;
        this.setScale(this.scale);
        if (this.mode == Mode.dragImage) {
            this.changeMode(this.lastMode);
        }
        this.repaint();
    }

    public void setFacedRight(boolean facedRight) {
        this.facedRight = facedRight;
        this.hitX *= -1;
        this.weaponX *= -1;
        this.weaponAngle = 8 - this.weaponAngle;
        this.repaint();
    }

    public boolean isFacedRight() {
        return this.facedRight;
    }

    public void setReplaceImage(BufferedImage img, int cx, int cy) {
        this.imgX = cx;
        this.imgY = cy;
        this.setReplaceImage(img);
    }

    public void setMode(Mode mode) {
        this.lastMode = this.mode;
        this.mode = mode;
        this.updateCursor();
        this.repaint();
    }

    private void updateCursor() {
        if (this.mouseOverHit || this.mouseOverWeapon) {
            this.setCursor(Cursor.getDefaultCursor());
        } else {
            switch (this.mode) {
                case pencil: {
                    this.setCursor(this.pencilCursor);
                    break;
                }
                case brush: {
                    this.setCursor(this.brushCursor);
                    break;
                }
                case bucket: {
                    this.setCursor(this.bucketCursor);
                    break;
                }
                case none: {
                    this.setCursor(Cursor.getDefaultCursor());
                    break;
                }
                case dragSprite:
                case dragImage: {
                    this.setCursor(this.dragCursor);
                }
            }
        }
    }

    private void changeMode(Mode newMode) {
        if (this.mode != newMode) {
            this.lastMode = this.mode;
            this.mode = newMode;
            this.listener.modeChanged(this.mode);
            this.updateCursor();
        }
    }

    public void setReplaceImage(BufferedImage img) {
        this.previewImage = null;
        if (img == null) {
            this.replaceImage = null;
            this.repaint();
            return;
        }
        this.imgModified = true;
        this.replaceImage = img;
        float scale = Gui.instance.GetScale();
        if (this.imgX == Integer.MIN_VALUE) {
            this.imgX = 128 - (int)((double)img.getWidth() * scale * 0.6);
            this.imgY = 128 - (int)((double)img.getHeight() * scale * 1.0);
        }
        this.setImage(img, null);
        this.changeMode(Mode.dragSprite);
        this.repaint();
    }

    public boolean wasImageModified() {
        return this.imgModified;
    }

    public BufferedImage getImage() {
        if (this.replaceImage != null) {
            if (this.previewImage == null) {
                this.previewImage = this.genPreviewImage();
            }
            if (this.facedRight) {
                this.previewImage = ImagePanel.flipImage(this.previewImage);
            }
            return this.previewImage;
        }
        return this.image;
    }

    public void zoom(float ammount) {
        if (ammount + this.scale > 0.0f) {
            this.setScale(this.scale + ammount);
        }
    }

    public void setScale(float scale) {
        if (scale > 0.0f && this.image != null) {
            this.scale = scale;
            this.setBounds(0, 0, (int)((float)this.image.getWidth() * scale), (int)((float)this.image.getHeight() * scale));
            this.repaint();
        }
    }

    public void setColor(Color color) {
        this.paintColor = color;
    }

    public ImagePanel(TheListener listener) {
        this.listener = listener;
        this.preInitComponents();
        this.initComponents();
        this.postInitComponents();
    }
    
    private int scale(float val) {
        return scale(val, false);
    }

    private int scale(float val, boolean useGuiScale) {
        return (int)(val * this.scale * (useGuiScale ? Gui.instance.GetScale() : 1));
    }

    private BufferedImage genPreviewImage() {
        return replaceImage;
        /*
        int minX = Math.max(this.imgX, 0);
        int minY = Math.max(this.imgY, 0);
        int width = this.replaceImage.getWidth();
        int height = this.replaceImage.getHeight();
        BufferedImage theShadow = this.shadow;
        if (this.facedRight) {
            theShadow = ImagePanel.flipImage(theShadow);
        }
        int maxX = Math.min(this.imgX + width, theShadow.getWidth());
        int maxY = Math.min(this.imgY + height, theShadow.getHeight());
        BufferedImage res = new BufferedImage(theShadow.getWidth(), theShadow.getHeight(), 2);
        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                int val = theShadow.getRGB(x, y);
                if (val == 0) continue;
                val = this.replaceImage.getRGB(x - this.imgX, y - this.imgY);
                res.setRGB(x, y, val);
            }
        }
        return res;
        */
    }

    @Override
    public void paint(Graphics g) {
        float guiScale = Gui.instance.GetScale();
        int scaledY;
        int scaledX;
        Composite original;
        int scaledH;
        Graphics2D g2d = (Graphics2D)g;
        int scaledCenter = this.scale(128, false);
        int scaledBounds = this.scale(256, false);
        if (this.showCenter) {
            g2d.setColor(Color.black);
            g2d.drawLine(scaledCenter, 0, scaledCenter, scaledBounds);
            g2d.drawLine(0, scaledCenter, scaledBounds, scaledCenter);
        }
        if (this.showGhost && this.ghostImage != null && this.ghostShadow != null) {
            int scaledW = this.scale(this.ghostShadow.getWidth(), true);
            int scaledH2 = this.scale(this.ghostShadow.getHeight(), true);
            Composite original2 = g2d.getComposite();
            if (this.showShadow) {
                g2d.setComposite(AlphaComposite.getInstance(3, 0.1f));
                if (this.facedRight) {
                    g2d.drawImage(this.ghostShadow, 0, 0, scaledW, scaledH2, this.ghostShadow.getWidth(), 0, 0, this.ghostShadow.getHeight(), null);
                } else {
                    g2d.drawImage(this.ghostShadow, 0, 0, scaledW, scaledH2, null);
                }
            }
            g2d.setComposite(AlphaComposite.getInstance(3, 0.4f));
            scaledW = this.scale(this.ghostImage.getWidth(), true);
            scaledH2 = this.scale(this.ghostImage.getHeight(), true);
            if (this.facedRight) {
                g2d.drawImage(this.ghostImage, 0, 0, scaledW, scaledH2, this.ghostImage.getWidth(), 0, 0, this.ghostImage.getHeight(), null);
            } else {
                g2d.drawImage(this.ghostImage, 0, 0, scaledW, scaledH2, null);
            }
            g2d.setComposite(original2);
        }
        if (this.weaponShowBehind && this.showWeapon && this.hasWeapon && this.weapon != null) {
            if (this.mouseOverWeapon) {
                this.weapon.drawOver(g2d, this.weaponX, this.weaponY, this.weaponAngle, this.scale);
            } else {
                this.weapon.draw(g2d, this.weaponX, this.weaponY, this.weaponAngle, this.scale);
            }
        }
        if (this.image != null && this.replaceImage == null && this.showImage) {
            scaledX = this.scale(pivot.x + this.dragSpriteX, false);
            scaledY = this.scale(pivot.y + this.dragSpriteY, false);
            int scaledW = this.scale(this.image.getWidth(), true);
            scaledH = this.scale(this.image.getHeight(), true);
            if (this.facedRight) {
                g2d.drawImage(this.image, scaledX, scaledY - scaledH, scaledX + scaledW, scaledY, this.image.getWidth(), 0, 0, this.image.getHeight(), null);
            } else {
                g2d.drawImage(this.image, scaledX, scaledY - scaledH, scaledW, scaledH, null);
            }
        }
        if (this.showWeapon && this.hasWeapon && this.weapon != null) {
            if (!this.weaponShowBehind) {
                if (this.mouseOverWeapon) {
                    this.weapon.drawOver(g2d, this.weaponX, this.weaponY, this.weaponAngle, this.scale);
                } else {
                    this.weapon.draw(g2d, this.weaponX, this.weaponY, this.weaponAngle, this.scale);
                }
            }
            if (this.mouseOverWeapon) {
                this.weapon.drawCircleOver(g2d, this.weaponX, this.weaponY, this.scale);
            }
        }
        if (this.showHit && this.hasHit) {
            int centerX;
            int scaledHitX = this.scale(this.hitX, false);
            int scaledHitY = this.scale(this.hitY, false);
            int width = Math.abs(scaledHitX);
            int height = 5;
            int x = scaledHitX > 0 ? scaledCenter : scaledCenter + scaledHitX;
            int y = scaledCenter + scaledHitY - 2;
            g2d.setColor(this.getHitBackgroundColor());
            g2d.fillRect(x, y, width, height);
            g2d.setColor(this.getHitColor());
            g2d.drawRect(x, y, width, height);
            if (this.mouseOverHit) {
                if (this.overHitImage != null) {
                    centerX = this.overHitImage.getWidth() / 2;
                    int centerY = this.overHitImage.getWidth() / 2;
                    g2d.drawImage(this.overHitImage, scaledCenter + scaledHitX - centerX, scaledCenter + scaledHitY - centerY, null);
                }
            } else if (this.hitImage != null) {
                centerX = this.hitImage.getWidth() / 2;
                int centerY = this.hitImage.getWidth() / 2;
                g2d.drawImage(this.hitImage, scaledCenter + scaledHitX - centerX, scaledCenter + scaledHitY - centerY, null);
            }
            g2d.setColor(Color.black);
            g2d.drawLine(scaledCenter + scaledHitX - 3, scaledCenter + scaledHitY - 1, scaledCenter + scaledHitX + 3, scaledCenter + scaledHitY - 1);
            g2d.drawLine(scaledCenter + scaledHitX - 3, scaledCenter + scaledHitY + 1, scaledCenter + scaledHitX + 3, scaledCenter + scaledHitY + 1);
            g2d.drawLine(scaledCenter + scaledHitX - 1, scaledCenter + scaledHitY - 3, scaledCenter + scaledHitX - 1, scaledCenter + scaledHitY + 3);
            g2d.drawLine(scaledCenter + scaledHitX + 1, scaledCenter + scaledHitY - 3, scaledCenter + scaledHitX + 1, scaledCenter + scaledHitY + 3);
            Color crossColor = this.getHitColor();
            crossColor = new Color(crossColor.getRed(), crossColor.getGreen(), crossColor.getBlue(), 255);
            g2d.setColor(crossColor);
            g2d.drawLine(scaledCenter + scaledHitX - 3, scaledCenter + scaledHitY, scaledCenter + scaledHitX + 3, scaledCenter + scaledHitY);
            g2d.drawLine(scaledCenter + scaledHitX, scaledCenter + scaledHitY - 3, scaledCenter + scaledHitX, scaledCenter + scaledHitY + 3);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.image != null) {
            return new Dimension((int)((float)this.image.getWidth() * this.scale), (int)((float)this.image.getHeight() * this.scale));
        }
        return super.getPreferredSize();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                ImagePanel.this.formMousePressed(evt);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                ImagePanel.this.formMouseReleased(evt);
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter(){

            @Override
            public void mouseDragged(MouseEvent evt) {
                ImagePanel.this.formMouseDragged(evt);
            }

            @Override
            public void mouseMoved(MouseEvent evt) {
                ImagePanel.this.formMouseMoved(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        int mouseX = evt.getX();
        int mouseY = evt.getY();
        if (this.showHit && this.hasHit && !this.mouseOverWeapon) {
            int centerX = (int)((double)this.hitImage.getWidth() * 0.7);
            int centerY = (int)((double)this.hitImage.getWidth() * 0.7);
            int scaledHitX = this.scale(128 + this.hitX);
            int scaledHitY = this.scale(128 + this.hitY);
            int left = scaledHitX - centerX;
            int right = scaledHitX + centerX;
            int top = scaledHitY - centerY;
            int bottom = scaledHitY + centerY;
            boolean oldOver = this.mouseOverHit;
            boolean bl = this.mouseOverHit = mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
            if (oldOver != this.mouseOverHit) {
                this.updateCursor();
                this.repaint();
            }
        }
        if (this.showWeapon && this.hasWeapon && !this.mouseOverHit) {
            int scaledWeaponX = this.scale(128 + this.weaponX);
            int scaledWeaponY = this.scale(128 + this.weaponY);
            int left = scaledWeaponX - 16;
            int right = scaledWeaponX + 16;
            int top = scaledWeaponY - 16;
            int bottom = scaledWeaponY + 16;
            boolean oldOver = this.mouseOverWeapon;
            boolean bl = this.mouseOverWeapon = mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
            if (oldOver != this.mouseOverWeapon) {
                this.updateCursor();
                this.repaint();
            }
        }
    }//GEN-LAST:event_formMouseMoved

    private void formMousePressed(MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        this.originalMouseX = this.oldMouseX = evt.getX();
        this.originalMouseY = this.oldMouseY = evt.getY();
        this.isLeftButtonPressed = evt.getButton() == 1;
        this.isRightButtonPressed = evt.getButton() == 3;
        int x = (int)((float)evt.getX() / this.scale);
        int y = (int)((float)evt.getY() / this.scale);
        if (this.facedRight) {
            x = 255 - x;
        }
        if (x >= 0 && y >= 0 && x <= 255 && y <= 255 && this.shadow != null && this.shadow.getRGB(x, y) != 0) {
            this.imgModified = true;
            Graphics2D g2d = this.image.createGraphics();
            if (this.paintColor == null) {
                AlphaComposite composite = AlphaComposite.getInstance(1, 0.0f);
                g2d.setComposite(composite);
                g2d.setColor(new Color(0, 0, 0, 0));
            } else {
                g2d.setColor(this.paintColor);
            }
            if (this.mode == Mode.pencil) {
                g2d.drawLine(x, y, x, y);
            } else if (this.mode == Mode.brush) {
                this.drawThickLine(g2d, x, y, x, y, this.brushSize);
            } else if (this.mode == Mode.bucket) {
                if (this.paintColor != null) {
                    ImagePanel.floodFill(this.image, this.paintColor, new Point(x, y));
                } else {
                    ImagePanel.floodFill(this.image, new Color(0, 0, 0, 0), new Point(x, y));
                }
            }
            g2d.dispose();
            this.listener.artChanged();
            this.repaint();
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (this.isLeftButtonPressed) {
            if (this.showHit && this.hasHit && this.listener != null && this.mouseOverHit) {
                int center = this.scale(128);
                this.listener.hitPositionChanged((int)((float)((evt.getX() - center) / -2) / this.scale), (int)((float)((evt.getY() - center) / -1) / this.scale));
            } else if (this.showWeapon && this.hasWeapon && this.listener != null && this.mouseOverWeapon) {
                int center = this.scale(128);
                this.listener.weaponPositionChanged((int)((float)(evt.getX() - center) / this.scale), - (int)((float)(evt.getY() - center) / this.scale));
            } else {
                int x = (int)((float)evt.getX() / this.scale);
                int y = (int)((float)evt.getY() / this.scale);
                int oldX = (int)((float)this.oldMouseX / this.scale);
                int oldY = (int)((float)this.oldMouseY / this.scale);
                int originalX = (int)((float)this.originalMouseX / this.scale);
                int originalY = (int)((float)this.originalMouseY / this.scale);
                if (this.replaceImage != null && this.mode == Mode.dragImage) {
                    this.imgX += x - oldX;
                    this.imgY += y - oldY;
                    this.previewImage = null;
                    this.repaint();
                } else if (this.mode == Mode.dragSprite) {
                    int dx = x - oldX;
                    int dy = y - oldY;
                    if (evt.isShiftDown()) {
                        if (this.dragShiftMode == 0) {
                            this.dragShiftMode = Math.abs(dx) > Math.abs(dy) ? 1 : 2;
                        }
                        if (this.dragShiftMode == 1) {
                            this.dragSpriteX = x - originalX;
                        } else {
                            this.dragSpriteY = y - originalY;
                        }
                    } else {
                        this.dragShiftMode = 0;
                        this.dragSpriteX = x - originalX;
                        this.dragSpriteY = y - originalY;
                    }
                    this.repaint();
                } else {
                    if (this.facedRight) {
                        x = 255 - x;
                        oldX = 255 - oldX;
                    }
                    if (x >= 0 && y >= 0 && x <= 255 && y <= 255 && oldX >= 0 && oldY >= 0 && oldX <= 255 && oldY <= 255 && shadow != null && this.shadow.getRGB(x, y) != 0 && this.shadow.getRGB(oldX, oldY) != 0) {
                        this.imgModified = true;
                        Graphics2D g2d = this.image.createGraphics();
                        if (this.paintColor == null) {
                            AlphaComposite composite = AlphaComposite.getInstance(1, 0.0f);
                            g2d.setComposite(composite);
                            g2d.setColor(new Color(0, 0, 0, 0));
                        } else {
                            g2d.setColor(this.paintColor);
                        }
                        if (this.mode == Mode.pencil) {
                            g2d.drawLine(oldX, oldY, x, y);
                        } else if (this.mode == Mode.brush) {
                            this.drawThickLine(g2d, oldX, oldY, x, y, this.brushSize);
                        }
                        g2d.dispose();
                        this.listener.artChanged();
                        this.repaint();
                    }
                }
            }
        } else if (this.isRightButtonPressed) {
            JViewport vp = (JViewport)this.getParent();
            Point p = vp.getViewPosition();
            p.x += this.originalMouseX - evt.getX();
            p.y += this.originalMouseY - evt.getY();
            int size = this.scale(255);
            if (p.x + vp.getWidth() > size) {
                p.x = size - vp.getWidth();
            }
            if (p.y + vp.getHeight() > size) {
                p.y = size - vp.getHeight();
            }
            if (p.x < 0) {
                p.x = 0;
            }
            if (p.y < 0) {
                p.y = 0;
            }
            vp.setViewPosition(p);
        }
        this.oldMouseX = evt.getX();
        this.oldMouseY = evt.getY();
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if (evt.getButton() == 1) {
            this.isLeftButtonPressed = false;
        }
        if (evt.getButton() == 3) {
            this.isRightButtonPressed = false;
        }
        if (this.mode == Mode.dragSprite && (this.dragSpriteX != 0 || this.dragSpriteY != 0)) {
            this.listener.spriteDragged(this.dragSpriteX, this.dragSpriteY);
        }
        this.dragShiftMode = 0;
    }//GEN-LAST:event_formMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void drawThickLine(Graphics g, int x1, int y1, int x2, int y2, int thickness) {
        int dX = x2 - x1;
        int dY = y2 - y1;
        double lineLength = Math.sqrt(dX * dX + dY * dY);
        double scale = (double)thickness / (2.0 * lineLength);
        double ddx = (- scale) * (double)dY;
        double ddy = scale * (double)dX;
        int dx = (int)(ddx += ddx > 0.0 ? 0.5 : -0.5);
        int dy = (int)(ddy += ddy > 0.0 ? 0.5 : -0.5);
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        xPoints[0] = x1 + dx;
        yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx;
        yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx;
        yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx;
        yPoints[3] = y2 + dy;
        g.fillPolygon(xPoints, yPoints, 4);
        int circleTickness = thickness + 2;
        g.fillOval(x1 - circleTickness / 2, y1 - circleTickness / 2, circleTickness, circleTickness);
        g.fillOval(x2 - circleTickness / 2, y2 - circleTickness / 2, circleTickness, circleTickness);
    }

    public static void floodFill(BufferedImage img, Color fillColor, Point loc) {
        int[] old;
        if (loc.x < 0 || loc.x >= img.getWidth() || loc.y < 0 || loc.y >= img.getHeight()) {
            throw new IllegalArgumentException();
        }
        WritableRaster raster = img.getRaster();
        int[] fill = new int[]{fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), fillColor.getAlpha()};
        if (ImagePanel.isEqualRgba(fill, old = raster.getPixel(loc.x, loc.y, new int[4]))) {
            return;
        }
        ImagePanel.floodLoop(raster, loc.x, loc.y, fill, old);
    }

    private static void floodLoop(WritableRaster raster, int x, int y, int[] fill, int[] old) {
        Rectangle bounds = raster.getBounds();
        int[] aux = new int[]{255, 255, 255, 255};
        int fillL = x;
        do {
            raster.setPixel(fillL, y, fill);
        } while (--fillL >= 0 && ImagePanel.isEqualRgba(raster.getPixel(fillL, y, aux), old));
        ++fillL;
        int fillR = x;
        do {
            raster.setPixel(fillR, y, fill);
        } while (++fillR < bounds.width - 1 && ImagePanel.isEqualRgba(raster.getPixel(fillR, y, aux), old));
        for (int i = fillL; i <= --fillR; ++i) {
            if (y > 0 && ImagePanel.isEqualRgba(raster.getPixel(i, y - 1, aux), old)) {
                ImagePanel.floodLoop(raster, i, y - 1, fill, old);
            }
            if (y >= bounds.height - 1 || !ImagePanel.isEqualRgba(raster.getPixel(i, y + 1, aux), old)) continue;
            ImagePanel.floodLoop(raster, i, y + 1, fill, old);
        }
    }

    private static boolean isEqualRgba(int[] pix1, int[] pix2) {
        return pix1[0] == pix2[0] && pix1[1] == pix2[1] && pix1[2] == pix2[2] && pix1[3] == pix2[3];
    }

    public static BufferedImage flipImage(BufferedImage image) {
        if (image == null) {
            return null;
        }
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), 2);
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                res.setRGB(width - 1 - x, y, image.getRGB(x, y));
            }
        }
        return res;
    }

}
