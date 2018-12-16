/*
 * Decompiled with CFR 0_132.
 */
package lib;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import lib.Rom;
import lib.anim.AnimFrame;
import lib.anim.Animation;
import lib.anim.Character;
import lib.map.Palette;
import lib.map.Sprite;

public class TilesTest
extends JFrame {
    Palette palette;
    Rom rom;
    private JLabel label;

    private void loadRom() {
        try {
            this.rom = new Rom(new File("sor2.bin"));
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(TilesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadPalette() {
        try {
            this.palette = this.rom.readPalette(42464L);
        }
        catch (IOException ex) {
            Logger.getLogger(TilesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TilesTest() {
        this.initComponents();
        this.label.setText("");
        this.loadRom();
        this.loadPalette();
        try {
            Character character = this.rom.readCharacter(134828L, 18752L, 69668L, 1, 6, 0, false, false, 4);
            AnimFrame animFrame = character.getAnimation(3).getFrame(2);
            System.out.println(Long.toHexString(animFrame.artAddress / 2L) + "*2 = " + Long.toHexString(animFrame.artAddress));
            Sprite sprite = this.rom.readSprite(animFrame.mapAddress, animFrame.artAddress);
            this.label.setIcon(new ImageIcon(sprite.asImage(this.palette)));
        }
        catch (Exception ex) {
            Logger.getLogger(TilesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initComponents() {
        this.label = new JLabel();
        this.setDefaultCloseOperation(3);
        this.label.setHorizontalAlignment(0);
        this.label.setText("jLabel1");
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.label, -1, 400, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.label, -1, 300, 32767));
        this.pack();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (!"Nimbus".equals(info.getName())) continue;
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(TilesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(TilesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(TilesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TilesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                new TilesTest().setVisible(true);
            }
        });
    }

}

