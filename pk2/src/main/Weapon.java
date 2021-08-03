/*
 * Decompiled with CFR 0_132.
 */
package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Weapon {
    private BufferedImage[] imgs = new BufferedImage[8];
    private int cX;
    private int cY;

    private int scale(int val, float scale) {
        return (int)((float)val * scale);
    }

    Weapon(String weaponName, int cX, int cY) {
        this.cX = cX;
        this.cY = cY;
        for (int i = 0; i < 8; ++i) {
            try {
                this.imgs[i] = ImageIO.read(new File("images/" + weaponName + i + ".png"));
                continue;
            }
            catch (IOException ex) {
                // empty catch block
            }
        }
    }

    public void draw(Graphics2D g2d, int x, int y, int angle, float scale) {
        x += 128;
        y += 128;
        if (this.imgs[angle %= 8] != null) {
            BufferedImage img = this.imgs[angle];
            g2d.drawImage(img, this.scale(x - this.cX, scale), this.scale(y - this.cY, scale), this.scale(img.getWidth(), scale), this.scale(img.getHeight(), scale), null);
        }
    }

    void drawOver(Graphics2D g2d, int x, int y, int angle, float scale) {
        x += 128;
        y += 128;
        if (this.imgs[angle %= 8] != null) {
            BufferedImage img = this.imgs[angle];
            BufferedImage imgOver = new BufferedImage(img.getWidth(), img.getHeight(), 2);
            for (int i = 0; i < img.getWidth(); ++i) {
                for (int j = 0; j < img.getHeight(); ++j) {
                    if ((img.getRGB(i, j) >> 24 & 255) <= 0) continue;
                    Color bc = new Color(img.getRGB(i, j));
                    bc = bc.brighter().brighter();
                    imgOver.setRGB(i, j, bc.getRGB());
                }
            }
            g2d.drawImage(imgOver, this.scale(x - this.cX, scale), this.scale(y - this.cY, scale), this.scale(img.getWidth(), scale), this.scale(img.getHeight(), scale), null);
        }
    }

    void drawCircleOver(Graphics2D g2d, int x, int y, float scale) {
        int scaledX = this.scale(x += 128, scale);
        int scaledY = this.scale(y += 128, scale);
        int size = 16;
        Composite oldComposite = g2d.getComposite();
        g2d.setColor(Color.red);
        g2d.drawOval(scaledX - size, scaledY - size, size * 2, size * 2);
        g2d.setColor(Color.black);
        g2d.drawOval(scaledX - size - 1, scaledY - size - 1, (size + 1) * 2, (size + 1) * 2);
        g2d.drawOval(scaledX - size + 1, scaledY - size + 1, (size - 1) * 2, (size - 1) * 2);
        g2d.setColor(Color.white);
        g2d.setComposite(AlphaComposite.getInstance(3, 0.1f));
        g2d.fillOval(this.scale(x, scale) - 16, this.scale(y, scale) - 16, 32, 32);
        g2d.setComposite(oldComposite);
    }
}

