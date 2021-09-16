/*
 * Decompiled with CFR 0_132.
 */
package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import lib.FreeAddressesManager;
import lib.Manager;
import lib.anim.AnimFrame;
import lib.anim.Animation;
import lib.anim.Character;
import lib.anim.HitFrame;
import lib.anim.WeaponFrame;
import lib.map.Palette;
import lib.map.Piece;
import lib.map.Sprite;
import lib.map.SpritePiece;
import lib.map.Tile;

public class Gui
extends JFrame
implements ActionListener,
TheListener {
    private static final String VERSION = "1.7.2";
    private static final String YEAR = "2021";
    private static final String TITLE = "Pancake 2 " + VERSION;
    private static final int INVALID_INT = Integer.MIN_VALUE;
    private static final long INVALID_LONG = Long.MIN_VALUE;
    private String romName;
    private String currentDirectory;
    private JFileChooser romChooser;
    private JFileChooser guideChooser;
    public JFileChooser imageChooser;
    private JFileChooser imageSaver;
    private JFileChooser resizerChooser;
    private ImagePanel imagePanel;
    private Manager manager;
    private Guide guide;
    private JPanel[] colorPanels;
    private Border selectionBorder;
    private Timer timer;
    private int frameDelayCount;
    private int currAnimation;
    private int currFrame;
    private HashSet<JTextField> inUse;
    private int selectedColor;
    private long copiedMap;
    private long copiedArt;
    private boolean copiedHasHit;
    private boolean copiedKnockDown;
    private boolean copiedHasWeapon;
    private boolean copiedWpShowBehind;
    private int copiedHitX;
    private int copiedHitY;
    private int copiedHitSound;
    private int copiedHitDamage;
    private int copiedWpX;
    private int copiedWpY;
    private int copiedWpRotation;
    private int lastcX;
    private int lastcY;
    private boolean wasFrameReplaced;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField angleField;
    private javax.swing.JComboBox animationCombo;
    private javax.swing.JPanel animationPanel;
    private javax.swing.JTextField artField;
    private javax.swing.JButton backBut;
    private javax.swing.JCheckBox behindCheck;
    private javax.swing.JRadioButtonMenuItem brushMenu;
    private javax.swing.JRadioButton brushRadio;
    private javax.swing.JRadioButtonMenuItem bucketMenu;
    private javax.swing.JRadioButton bucketRadio;
    private javax.swing.JComboBox characterCombo;
    private javax.swing.JPanel characterPanel;
    private javax.swing.JPanel characterPanel1;
    private javax.swing.JMenuItem closeMenu;
    private javax.swing.JPanel colorPanel1;
    private javax.swing.JPanel colorPanel10;
    private javax.swing.JPanel colorPanel11;
    private javax.swing.JPanel colorPanel12;
    private javax.swing.JPanel colorPanel13;
    private javax.swing.JPanel colorPanel14;
    private javax.swing.JPanel colorPanel15;
    private javax.swing.JPanel colorPanel16;
    private javax.swing.JPanel colorPanel2;
    private javax.swing.JPanel colorPanel3;
    private javax.swing.JPanel colorPanel4;
    private javax.swing.JPanel colorPanel5;
    private javax.swing.JPanel colorPanel6;
    private javax.swing.JPanel colorPanel7;
    private javax.swing.JPanel colorPanel8;
    private javax.swing.JPanel colorPanel9;
    private javax.swing.JPanel colorsPanel1;
    private javax.swing.JLabel compressedLabel;
    private javax.swing.JMenuItem copyMenu;
    private javax.swing.JTextField damageField;
    private javax.swing.JTextField delayField;
    private javax.swing.JRadioButtonMenuItem dragImageMenu;
    private javax.swing.JRadioButton dragImageRadio;
    private javax.swing.JRadioButtonMenuItem dragSpriteMenu;
    private javax.swing.JRadioButton dragSpriteRadio;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JPanel framePanel;
    private javax.swing.JButton frontBut;
    private javax.swing.JTextField genPaletteField;
    private javax.swing.JPanel generatePanel;
    private javax.swing.JButton hardReplaceButton;
    private javax.swing.JCheckBoxMenuItem hexIdsMenu;
    private javax.swing.JCheckBox hitCheck;
    private javax.swing.JPanel hitPanel;
    private javax.swing.JMenu inportMenu;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JCheckBox koCheck;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField mapField;
    private javax.swing.JMenuItem nameMenu;
    private javax.swing.JButton nextBut;
    private javax.swing.JRadioButtonMenuItem noneMenu;
    private javax.swing.JRadioButton noneRadio;
    private javax.swing.JMenuItem openRomMenu;
    private javax.swing.JPanel overridePanel;
    private javax.swing.JMenuItem pasteMenu;
    private javax.swing.JMenuItem pasteMenu1;
    private javax.swing.JMenuItem pasteMenu2;
    private javax.swing.JRadioButtonMenuItem pencilMenu;
    private javax.swing.JRadioButton pencilRadio;
    private javax.swing.JToggleButton playToggle;
    private javax.swing.JPanel playerPanel;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JButton previousBut;
    private javax.swing.JMenuItem resizeAnimsMenu;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JCheckBox showCenterCheck;
    private javax.swing.JCheckBox showFacedRightCheck;
    private javax.swing.JCheckBox showHitsCheck;
    private javax.swing.JCheckBox showTileCheck;
    private javax.swing.JCheckBox showWeaponCheck;
    private javax.swing.JTextField sizeField;
    private javax.swing.JRadioButtonMenuItem sizeRadioMenu1;
    private javax.swing.JRadioButtonMenuItem sizeRadioMenu2;
    private javax.swing.JRadioButtonMenuItem sizeRadioMenu3;
    private javax.swing.JButton softReplaceButton;
    private javax.swing.JTextField soundField;
    private javax.swing.JMenuItem spriteSheetMenu;
    private javax.swing.JMenuItem spriteSheetMenu1;
    private javax.swing.JPanel toolsPanel;
    private javax.swing.JTextField wXField;
    private javax.swing.JTextField wYField;
    private javax.swing.JCheckBox weaponCheck;
    private javax.swing.JComboBox weaponCombo;
    private javax.swing.JPanel weaponPanel;
    private javax.swing.JTextField xField;
    private javax.swing.JTextField yField;
    // End of variables declaration//GEN-END:variables

    private void setupAnimationCombo() {
        int charId = this.guide.getFakeCharId(this.manager.getCurrentCharacterId());
        int count = this.guide.getAnimsCount(charId);
        this.animationCombo.removeAllItems();
        if (this.areIdsHex()) {
            for (int i = 0; i < count; ++i) {
                this.animationCombo.addItem(Integer.toHexString(i * 2) + " - " + this.guide.getAnimName(charId, i));
            }
        } else {
            for (int i = 0; i < count; ++i) {
                this.animationCombo.addItem("" + (i + 1) + " - " + this.guide.getAnimName(charId, i));
            }
        }
    }

    private boolean areIdsHex() {
        return this.hexIdsMenu.isSelected();
    }

    private void setupCharacterCombo() {
        int count = this.guide.getNumChars();
        this.characterCombo.removeAllItems();
        if (this.areIdsHex()) {
            for (int i = 0; i < count; ++i) {
                this.characterCombo.addItem(Integer.toHexString(i * 2) + " - " + this.guide.getCharName(i));
            }
        } else {
            for (int i = 0; i < count; ++i) {
                this.characterCombo.addItem("" + (i + 1) + " - " + this.guide.getCharName(i));
            }
        }
    }

    private void updateTitle() {
        Character ch = this.manager.getCharacter();
        if (ch.wasModified()) {
            this.setTitle("Pancake 2 " + VERSION + " - " + new File(this.romName).getName() + "*");
        } else {
            this.setTitle("Pancake 2 " + VERSION + " - " + new File(this.romName).getName());
        }
    }

    private void verifyModifications() {
        if (this.manager == null) {
            return;
        }
        if (this.imagePanel.wasImageModified()) {
            BufferedImage img = this.imagePanel.getImage();
            Animation anim = this.manager.getCharacter().getAnimation(this.currAnimation);
            anim.setImage(this.currFrame, img);
            this.manager.getCharacter().setModified(true);
            this.manager.getCharacter().setSpritesModified(true);
            anim.setSpritesModified(this.currFrame, true);
            this.dragImageRadio.setEnabled(false);
        }
    }

    private void changeFrame(int frameId) {
        this.verifyModifications();
        if (this.manager != null && this.manager.getCharacter() != null) {
            if (this.manager.getCharacter().wasModified()) {
                this.saveRom();
            }
            this.setFrame(frameId);
        }
    }

    private void changeAnimation(int animId) {
        this.verifyModifications();
        this.setAnimation(animId);
    }

    private void changeCharacter(int charId) throws IOException {
        this.verifyModifications();
        this.setCharacter(charId);
    }

    private void setFrame(int frameId) {
        if (this.currFrame != frameId) {
            this.wasFrameReplaced = false;
            this.imagePanel.updateGhost();
        }
        this.currFrame = frameId;
        Character ch = this.manager.getCharacter();
        AnimFrame animFrame = ch.getAnimFrame(this.currAnimation, this.currFrame);
        HitFrame hitFrame = ch.getHitFrame(this.currAnimation, this.currFrame);
        WeaponFrame weaponFrame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        TitledBorder border = (TitledBorder)this.framePanel.getBorder();
        border.setTitle("Frame " + Integer.toString(this.currFrame + 1));
        this.framePanel.repaint();
        this.setField(this.delayField, animFrame.delay);
        this.setFieldAsHex(this.mapField, animFrame.mapAddress);
        this.setFieldAsHex(this.artField, animFrame.artAddress);
        if (hitFrame != null) {
            this.setField(this.xField, hitFrame.x);
            this.setField(this.yField, hitFrame.y);
            this.setField(this.damageField, hitFrame.damage);
            this.setField(this.soundField, hitFrame.sound);
            this.setCheck(this.koCheck, hitFrame.knockDown);
            this.setCheck(this.hitCheck, hitFrame.isEnabled());
            if (hitFrame.isEnabled()) {
                this.imagePanel.setHit(hitFrame.x, hitFrame.y, hitFrame.knockDown);
            } else {
                this.imagePanel.removeHit();
            }
        } else {
            this.setCheck(this.hitCheck, false);
            this.imagePanel.removeHit();
        }
        this.updateHitFrameEnabling();
        if (weaponFrame != null) {
            this.setField(this.wXField, weaponFrame.x);
            this.setField(this.wYField, - weaponFrame.y);
            this.setField(this.angleField, weaponFrame.angle);
            this.setCheck(this.behindCheck, weaponFrame.showBehind);
            this.setCheck(this.weaponCheck, weaponFrame.isEnabled());
            if (weaponFrame.isEnabled()) {
                this.imagePanel.setWeapon(weaponFrame.x, weaponFrame.y, weaponFrame.angle, weaponFrame.showBehind);
            } else {
                this.imagePanel.removeWeapon();
            }
        } else {
            this.imagePanel.removeWeapon();
        }
        this.updateWeaponFrameEnabling();
        this.frameDelayCount = 0;
        this.setImage(this.manager.getImage(this.currAnimation, this.currFrame), this.manager.getShadow(this.currAnimation, this.currFrame));
        this.updateTitle();
        this.copyMenu.setEnabled(true);
        this.pasteMenu.setEnabled(this.copiedMap != 0L);
    }

    private void setAnimation(int animId) {
        if (this.currAnimation != animId) {
            this.imagePanel.updateGhost();
        }
        this.currAnimation = animId;
        try {
            this.manager.bufferAnimation(this.currAnimation);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to read animation graphics");
            return;
        }
        this.setComboSelection(this.animationCombo, this.currAnimation);
        Character ch = this.manager.getCharacter();
        int animSize = ch.getAnimation(this.currAnimation).getNumFrames();
        this.setField(this.sizeField, animSize);
        boolean isCompressed = ch.getAnimation(this.currAnimation).isCompressed();
        this.artField.setEnabled(!isCompressed);
        Gui.setEnable(this.toolsPanel, !isCompressed);
        Gui.setEnable(this.overridePanel, !isCompressed);
        Gui.setEnable(this.generatePanel, !isCompressed);
        this.inportMenu.setEnabled(!isCompressed);
        this.resizeAnimsMenu.setEnabled(!isCompressed);
        if (isCompressed) {
            this.imagePanel.setMode(Mode.none);
        }
        this.setFrame(0);
    }

    private void setCharacter(int charId) throws IOException {
        this.changeFrame(this.currFrame);
        if (this.manager == null) {
            return;
        }
        Character ch = this.manager.getCharacter();
        if (ch != null && ch.wasModified()) {
            this.saveRom();
        }
        int backupCurrAnim = this.currAnimation;
        int fakeId = this.guide.getFakeCharId(charId);
        int type = this.guide.getType(fakeId);
        if (type == 1) {
            AnimFrame.providedArtAddress = this.guide.getCompressedArtAddress(charId);
        }
        this.manager.setCharacter(charId, this.guide.getAnimsCount(fakeId), type);
        this.compressedLabel.setVisible(type == 1);
        this.setComboSelection(this.characterCombo, fakeId);
        this.setupAnimationCombo();
        int numAnimations = this.manager.getCharacter().getNumAnimations();
        this.updatePalettePanels();
        if (backupCurrAnim == -1) {
            this.setAnimation(0);
        } else if (backupCurrAnim >= numAnimations) {
            this.setAnimation(numAnimations - 1);
        } else {
            this.setAnimation(backupCurrAnim);
        }        
        this.nameMenu.setEnabled(true);
    }
    
    public boolean isPlayableChar() {
        return this.manager.getCurrentCharacterId() < this.guide.getPlayableChars();
    }

    private void guideChanged() {
        this.setupCharacterCombo();
    }

    private boolean openCustomGuide() {
        int returnVal = this.guideChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.guideChooser.getSelectedFile();
            try {
                this.guide = new Guide(file.getAbsolutePath());
                this.guideChanged();
                return true;
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                this.showError("Guide file '" + file.getName() + "' not found");
            }
            catch (Exception ex) {
                ex.printStackTrace();
                this.showError("Unable to open guide '" + file.getName() + "'");
            }
        }
        return false;
    }

    private boolean openDefaultGuide() {
        try {
            this.guide = new Guide(Guide.GUIDES_DIR + "default.txt");
            this.guideChanged();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean setupManager(String romName) {
        Manager oldManager = this.manager;
        try {
            this.manager = new Manager(romName, this.guide);
            this.changeCharacter(0);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            this.showError("File '" + romName + "' not found");
            this.manager = oldManager;
            return false;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("File '" + romName + "' is not a valid Streets of Rage 2 ROM");
            this.manager = oldManager;
            return false;
        }
        Manager newManager = this.manager;
        this.manager = oldManager;
        this.closeRom();
        this.manager = newManager;
        return true;
    }

    private boolean openRom() {
        if (!this.askSaveRom()) {
            return false;
        }
        int returnVal = this.romChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.romChooser.getSelectedFile();
            this.romName = file.getAbsolutePath();
            if (this.setupManager(this.romName)) {
                this.setTitle("Pancake 2 v1.6b - " + file.getName());
                this.updateEnablings();
            }
        }
        try {
            this.setCharacter(0);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to read first character");
            return false;
        }
        return true;
    }

    private void closeRom() {
        if (this.askSaveRom()) {
            this.manager = null;
            this.copyMenu.setEnabled(false);
            this.pasteMenu.setEnabled(false);
            this.nameMenu.setEnabled(false);
            this.pasteMenu1.setEnabled(false);
            this.pasteMenu2.setEnabled(false);
            this.resizeAnimsMenu.setEnabled(false);
            this.hexIdsMenu.setEnabled(false);
            this.imagePanel.setImage(null, null);
            this.imagePanel.setReplaceImage(null);
            this.imagePanel.removeHit();
            this.imagePanel.removeWeapon();
            this.updateEnablings();
        }
    }

    private boolean saveRom() {
        try {
            this.manager.save();
            this.updateTitle();
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to save rom");
            return false;
        }
    }

    private boolean askSaveRom() {
        if (this.guide == null || this.manager == null) {
            return true;
        }
        Character ch = this.manager.getCharacter();
        if (!ch.wasModified()) {
            return true;
        }
        int option = JOptionPane.showConfirmDialog(this, this.guide.getCharName(this.guide.getFakeCharId(this.manager.getCurrentCharacterId())) + " was modified.\n" + "Save changes?", "Character modified", 1);
        if (option == 0) {
            return this.saveRom();
        }
        if (option != 1) {
            return false;
        }
        return true;
    }

    private void setRadiosOff() {
        this.pencilRadio.setSelected(false);
        this.brushRadio.setSelected(false);
        this.bucketRadio.setSelected(false);
        this.dragSpriteRadio.setSelected(false);
        this.dragImageRadio.setSelected(false);
        this.noneRadio.setSelected(false);
        this.pencilMenu.setSelected(false);
        this.brushMenu.setSelected(false);
        this.bucketMenu.setSelected(false);
        this.dragSpriteMenu.setSelected(false);
        this.dragImageMenu.setSelected(false);
        this.noneMenu.setSelected(false);
    }

    private static void setEnable(JComponent component, boolean enabled) {
        if (component.isEnabled() == enabled) {
            return;
        }
        component.setEnabled(enabled);
        Component[] com = component.getComponents();
        for (int a = 0; a < com.length; ++a) {
            if (com[a] instanceof JComponent) {
                Gui.setEnable((JComponent)com[a], enabled);
                continue;
            }
            com[a].setEnabled(enabled);
        }
    }

    private void updateHitFrameEnabling() {
        Character ch = this.manager.getCharacter();
        HitFrame hitFrame = ch.getHitFrame(this.currAnimation, this.currFrame);
        boolean enabled = hitFrame != null && hitFrame.isEnabled();
        Gui.setEnable(this.hitPanel, enabled);
        this.hitCheck.setSelected(enabled);
        this.hitCheck.setEnabled(hitFrame != null);
    }

    private void updateWeaponFrameEnabling() {
        Character ch = this.manager.getCharacter();
        WeaponFrame weaponFrame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        boolean enabled = weaponFrame != null && weaponFrame.isEnabled();
        Gui.setEnable(this.weaponPanel, enabled);
        this.weaponCheck.setSelected(enabled);
        this.weaponCheck.setEnabled(weaponFrame != null);
    }

    private void updateEnablings() {
        Gui.setEnable(this.mainPanel, this.manager != null && this.guide != null);
        this.closeMenu.setEnabled(this.manager != null);
        this.inportMenu.setEnabled(this.manager != null);
        this.exportMenu.setEnabled(this.manager != null);
        this.resizeAnimsMenu.setEnabled(this.manager != null);
        this.copyMenu.setEnabled(this.manager != null);
        this.pasteMenu.setEnabled(this.manager != null && this.copiedMap != 0L);
        //this.pasteMenu1.setEnabled(this.manager != null); // disabled for now
        this.pasteMenu2.setEnabled(this.manager != null);
        this.hexIdsMenu.setEnabled(this.manager != null);
        if (this.manager != null) {
            this.updateHitFrameEnabling();
            this.updateWeaponFrameEnabling();
        }
    }

    private void sizeChanged() {
        if (this.inUse.contains(this.sizeField)) {
            return;
        }
        this.inUse.add(this.sizeField);
        Character ch = this.manager.getCharacter();
        Animation anim = ch.getAnimation(this.currAnimation);
        int maxSize = anim.getMaxNumFrames();
        int newSize = this.getIntFromField(this.sizeField, 1, maxSize);
        if (newSize == Integer.MIN_VALUE) {
            this.sizeField.setBackground(Color.red);
        } else {
            this.sizeField.setBackground(Color.white);
            if (newSize != anim.getNumFrames()) {
                anim.setNumFrames(newSize);
                ch.setModified(true);
                this.changeAnimation(this.currAnimation);
            }
        }
        this.inUse.remove(this.sizeField);
    }

    private void delayChanged() {
        if (this.inUse.contains(this.delayField)) {
            return;
        }
        this.inUse.add(this.delayField);
        Character ch = this.manager.getCharacter();
        Animation anim = ch.getAnimation(this.currAnimation);
        AnimFrame frame = anim.getFrame(this.currFrame);
        int newDelay = this.getIntFromField(this.delayField, 1, 255);
        if (newDelay == Integer.MIN_VALUE) {
            this.delayField.setBackground(Color.red);
        } else {
            this.delayField.setBackground(Color.white);
            if (newDelay != frame.delay) {
                frame.delay = newDelay;
                ch.setModified(true);
                this.refresh();
            }
        }
        this.inUse.remove(this.delayField);
    }

    private void hitXChanged() {
        if (this.inUse.contains(this.xField)) {
            return;
        }
        this.inUse.add(this.xField);
        Character ch = this.manager.getCharacter();
        HitFrame frame = ch.getHitFrame(this.currAnimation, this.currFrame);
        int newX = this.getIntFromField(this.xField, -127, 127);
        if (newX == Integer.MIN_VALUE) {
            this.xField.setBackground(Color.red);
        } else {
            this.xField.setBackground(Color.white);
            if (newX != frame.x) {
                frame.x = newX;
                ch.setModified(true);
                this.refresh();
            }
        }
        this.inUse.remove(this.xField);
    }

    private void hitYChanged() {
        if (this.inUse.contains(this.yField)) {
            return;
        }
        this.inUse.add(this.yField);
        Character ch = this.manager.getCharacter();
        HitFrame frame = ch.getHitFrame(this.currAnimation, this.currFrame);
        int newY = this.getIntFromField(this.yField, -127, 127);
        if (newY == Integer.MIN_VALUE) {
            this.yField.setBackground(Color.red);
        } else {
            this.yField.setBackground(Color.white);
            if (newY != frame.y) {
                frame.y = newY;
                ch.setModified(true);
                this.refresh();
            }
        }
        this.inUse.remove(this.yField);
    }

    private void hitSoundChanged() {
        if (this.inUse.contains(this.soundField)) {
            return;
        }
        this.inUse.add(this.soundField);
        Character ch = this.manager.getCharacter();
        HitFrame frame = ch.getHitFrame(this.currAnimation, this.currFrame);
        int newSound = this.getIntFromField(this.soundField, 0, 255);
        if (newSound == Integer.MIN_VALUE) {
            this.soundField.setBackground(Color.red);
        } else {
            this.soundField.setBackground(Color.white);
            if (newSound != frame.sound) {
                frame.sound = newSound;
                ch.setModified(true);
                this.refresh();
            }
        }
        this.inUse.remove(this.soundField);
    }

    private void hitDamageChanged() {
        if (this.inUse.contains(this.damageField)) {
            return;
        }
        this.inUse.add(this.damageField);
        Character ch = this.manager.getCharacter();
        HitFrame frame = ch.getHitFrame(this.currAnimation, this.currFrame);
        int newDamage = this.getIntFromField(this.damageField, -127, 127);
        if (newDamage == Integer.MIN_VALUE) {
            this.damageField.setBackground(Color.red);
        } else {
            this.damageField.setBackground(Color.white);
            if (newDamage != frame.damage) {
                frame.damage = newDamage;
                ch.setModified(true);
                this.refresh();
            }
        }
        this.inUse.remove(this.damageField);
    }

    private void weaponXChanged() {
        if (this.inUse.contains(this.wXField)) {
            return;
        }
        this.inUse.add(this.wXField);
        Character ch = this.manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        int newX = this.getIntFromField(this.wXField, -127, 127);
        if (newX == Integer.MIN_VALUE) {
            this.wXField.setBackground(Color.red);
        } else {
            this.wXField.setBackground(Color.white);
            if (newX != frame.x) {
                frame.x = newX;
                ch.setModified(true);
                this.refresh();
            }
        }
        this.inUse.remove(this.wXField);
    }

    private void weaponYChanged() {
        if (this.inUse.contains(this.wYField)) {
            return;
        }
        this.inUse.add(this.wYField);
        Character ch = this.manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        int newY = this.getIntFromField(this.wYField, -127, 127);
        if (newY == Integer.MIN_VALUE) {
            this.wYField.setBackground(Color.red);
        } else {
            newY = - newY;
            this.wYField.setBackground(Color.white);
            if (newY != frame.y) {
                frame.y = newY;
                ch.setModified(true);
                this.refresh();
            }
        }
        this.inUse.remove(this.wYField);
    }

    private void weaponAngleChanged() {
        if (this.inUse.contains(this.angleField)) {
            return;
        }
        this.inUse.add(this.angleField);
        Character ch = this.manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        int newAngle = this.getIntFromField(this.angleField, -127, 127);
        if (newAngle == Integer.MIN_VALUE || newAngle < 0 || newAngle > 7) {
            if (frame.isEnabled()) {
                this.angleField.setBackground(Color.red);
            }
        } else {
            this.angleField.setBackground(Color.white);
            if (newAngle != frame.angle) {
                frame.angle = newAngle;
                ch.setModified(true);
                this.refresh();
            }
        }
        this.inUse.remove(this.angleField);
    }

    private void mapAddressChanged() {
        if (this.inUse.contains(this.mapField)) {
            return;
        }
        this.inUse.add(this.mapField);
        Character ch = this.manager.getCharacter();
        Animation anim = ch.getAnimation(this.currAnimation);
        AnimFrame frame = anim.getFrame(this.currFrame);
        long newMap = this.getHexFromField(this.mapField);
        if (newMap == Long.MIN_VALUE) {
            this.mapField.setBackground(Color.red);
        } else {
            this.mapField.setBackground(Color.white);
            if (newMap != frame.mapAddress) {
                long oldMap = frame.mapAddress;
                frame.mapAddress = newMap;
                try {
                    this.manager.bufferAnimFrame(this.currAnimation, this.currFrame);
                    ch.setModified(true);
                    this.refresh();
                }
                catch (IOException ex) {
                    frame.mapAddress = oldMap;
                    this.mapField.setBackground(Color.red);
                }
            }
        }
        this.inUse.remove(this.mapField);
    }

    private void artAddressChanged() {
        if (this.inUse.contains(this.artField)) {
            return;
        }
        this.inUse.add(this.artField);
        Character ch = this.manager.getCharacter();
        Animation anim = ch.getAnimation(this.currAnimation);
        AnimFrame frame = anim.getFrame(this.currFrame);
        long newArt = this.getHexFromField(this.artField);
        if (newArt == Long.MIN_VALUE) {
            this.artField.setBackground(Color.red);
        } else {
            this.artField.setBackground(Color.white);
            if (newArt != frame.artAddress) {
                long oldArt = frame.artAddress;
                frame.artAddress = newArt;
                try {
                    this.manager.bufferAnimFrame(this.currAnimation, this.currFrame);
                    ch.setModified(true);
                    this.refresh();
                }
                catch (IOException ex) {
                    frame.artAddress = oldArt;
                    this.artField.setBackground(Color.red);
                }
            }
        }
        this.inUse.remove(this.artField);
    }

    private void setupFields() {
        this.sizeField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.sizeChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.sizeChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.sizeChanged();
            }
        });
        this.delayField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.delayChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.delayChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.delayChanged();
            }
        });
        this.xField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.hitXChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.hitXChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.hitXChanged();
            }
        });
        this.yField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.hitYChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.hitYChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.hitYChanged();
            }
        });
        this.soundField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.hitSoundChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.hitSoundChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.hitSoundChanged();
            }
        });
        this.damageField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.hitDamageChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.hitDamageChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.hitDamageChanged();
            }
        });
        this.wXField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.weaponXChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.weaponXChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.weaponXChanged();
            }
        });
        this.wYField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.weaponYChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.weaponYChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.weaponYChanged();
            }
        });
        this.angleField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.weaponAngleChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.weaponAngleChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.weaponAngleChanged();
            }
        });
        this.mapField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.mapAddressChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.mapAddressChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.mapAddressChanged();
            }
        });
        this.artField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                Gui.this.artAddressChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Gui.this.artAddressChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                Gui.this.artAddressChanged();
            }
        });
    }

    private void setupFileChoosers() {
        this.romChooser.addChoosableFileFilter(new FileFilter(){

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String filename = file.getName();
                return filename.endsWith(".bin");
            }

            @Override
            public String getDescription() {
                return "*.bin";
            }
        });
        this.guideChooser.addChoosableFileFilter(new FileFilter(){

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String filename = file.getName();
                return filename.endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "*.txt";
            }
        });
        this.resizerChooser.addChoosableFileFilter(new FileFilter(){

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String filename = file.getName();
                return filename.endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "*.txt";
            }
        });
        this.imageChooser.addChoosableFileFilter(new FileFilter(){

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String filename = file.getName();
                return filename.endsWith(".png") || filename.endsWith(".gif") || filename.endsWith(".jpg") || filename.endsWith(".bmp");
            }

            @Override
            public String getDescription() {
                return "Image (*.png, *.gif, *.jpg, *.bmp)";
            }
        });
        this.imageSaver.addChoosableFileFilter(new FileFilter(){

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String filename = file.getName();
                return filename.endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "*.png";
            }
        });
    }

    private void initColorPanels() {
        this.colorPanels = new JPanel[16];
        this.colorPanels[0] = this.colorPanel1;
        this.colorPanels[1] = this.colorPanel2;
        this.colorPanels[2] = this.colorPanel3;
        this.colorPanels[3] = this.colorPanel4;
        this.colorPanels[4] = this.colorPanel5;
        this.colorPanels[5] = this.colorPanel6;
        this.colorPanels[6] = this.colorPanel7;
        this.colorPanels[7] = this.colorPanel8;
        this.colorPanels[8] = this.colorPanel9;
        this.colorPanels[9] = this.colorPanel10;
        this.colorPanels[10] = this.colorPanel11;
        this.colorPanels[11] = this.colorPanel12;
        this.colorPanels[12] = this.colorPanel13;
        this.colorPanels[13] = this.colorPanel14;
        this.colorPanels[14] = this.colorPanel15;
        this.colorPanels[15] = this.colorPanel16;
        this.selectionBorder = BorderFactory.createCompoundBorder(new LineBorder(Color.white, 2), new LineBorder(Color.black));
        this.selectionBorder = BorderFactory.createCompoundBorder(new LineBorder(Color.black), this.selectionBorder);
        this.colorPanel1.setBorder(this.selectionBorder);
    }

    private void preInitComponents() {
        this.lastcY = -1;
        this.lastcX = -1;
        this.inUse = new HashSet();
        this.imagePanel = new ImagePanel(this);
        this.currentDirectory = new File(".").getAbsolutePath();
        this.romChooser = new JFileChooser(this.currentDirectory);
        this.romChooser.setDialogTitle("Open the 'Streets of Rage 2' ROM to edit");
        this.guideChooser = new JFileChooser(this.currentDirectory + "/" + Guide.GUIDES_DIR);
        this.guideChooser.setDialogTitle("Open characters guide");
        this.imageChooser = new JFileChooser(this.currentDirectory);
        this.imageChooser.setDialogTitle("Open image");
        this.imageSaver = new JFileChooser(this.currentDirectory);
        this.imageSaver.setDialogTitle("Save image");
        this.resizerChooser = new JFileChooser(this.currentDirectory);
        this.resizerChooser.setDialogTitle("Open resizing script");
        try {
            BufferedImage icon = ImageIO.read(new File("images/icon.png"));
            this.setIconImage(icon);
        }
        catch (IOException ex) {
            // empty catch block
        }
        this.timer = new Timer(16, this);
    }

    private void postInitComponents() {
        this.compressedLabel.setVisible(false);
        this.setupFields();
        this.setupFileChoosers();
        this.scrollPanel.setWheelScrollingEnabled(false);
        this.scrollPanel.setAutoscrolls(true);
        this.initColorPanels();
        this.updateEnablings();        
    }

    public Gui() {
        this.preInitComponents();
        this.initComponents();
        this.setVisible(true);
        this.postInitComponents();
    }

    private void setImagePanelScale(float newScale) {
        float oldScale = this.imagePanel.getScale();
        Rectangle oldView = this.scrollPanel.getViewport().getViewRect();
        this.imagePanel.setScale(newScale);
        Point newViewPos = new Point();
        newViewPos.x = (int)Math.max(0.0f, (float)(oldView.x + oldView.width / 2) * newScale / oldScale - (float)(oldView.width / 2));
        newViewPos.y = (int)Math.max(0.0f, (float)(oldView.y + oldView.height / 2) * newScale / oldScale - (float)(oldView.height / 2));
        this.scrollPanel.getViewport().setViewPosition(newViewPos);
    }

    private void scaleImagePanel(float zoom) {
        this.setImagePanelScale(this.imagePanel.getScale() + zoom);
    }

    public void showError(String text) {
        String title = "Wops!";
        JOptionPane.showMessageDialog(this, text, title, 0);
    }

    public int getIntFromField(JTextField field, int lowerLim, int upperLim) {
        String text = field.getText();
        if (text.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        try {
            int res = Integer.parseInt(text);
            if (res < lowerLim || res > upperLim) {
                return Integer.MIN_VALUE;
            }
            return res;
        }
        catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }

    public int getPositiveIntFromField(JTextField field) {
        return this.getIntFromField(field, 0, Integer.MAX_VALUE);
    }

    public int getIntFromField(JTextField field) {
        String text = field.getText();
        if (text.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        try {
            return Integer.parseInt(text);
        }
        catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }

    public long getHexFromField(JTextField field) {
        String text = field.getText();
        if (text.isEmpty()) {
            return Long.MIN_VALUE;
        }
        try {
            return Long.parseLong(text, 16);
        }
        catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }

    private void setField(JTextField field, String value) {
        if (this.inUse.contains(field)) {
            return;
        }
        if (!field.getText().equals(value)) {
            this.inUse.add(field);
            field.setBackground(Color.white);
            field.setText(value);
            this.inUse.remove(field);
        }
    }

    private void setField(JTextField field, int value) {
        this.setField(field, Integer.toString(value));
    }

    private void setField(JTextField field, long value) {
        this.setField(field, Long.toString(value));
    }

    private void setFieldAsHex(JTextField field, long value) {
        this.setField(field, Long.toString(value, 16));
    }

    private void setCheck(JCheckBox check, boolean value) {
        if (check.isSelected() != value) {
            check.setSelected(value);
        }
    }

    private void setComboSelection(JComboBox combo, int newIndex) {
        if (combo.getSelectedIndex() != newIndex) {
            combo.setSelectedIndex(newIndex);
        }
    }

    private void setImage(BufferedImage image, BufferedImage shadow) {
        JScrollBar horizontalScrollBar = this.scrollPanel.getHorizontalScrollBar();
        JScrollBar verticalScrollBar = this.scrollPanel.getVerticalScrollBar();
        int x = horizontalScrollBar.getValue();
        int y = verticalScrollBar.getValue();
        this.imagePanel.setImage(image, shadow);
        horizontalScrollBar.setValue(x + 1);
        horizontalScrollBar.setValue(x);
        verticalScrollBar.setValue(y + 1);
        verticalScrollBar.setValue(y);
        this.dragImageRadio.setEnabled(false);
    }

    private void setNextAnimation() {
        int nextAnimation = this.currAnimation + 1;
        if (nextAnimation >= this.manager.getCharacter().getNumAnimations()) {
            nextAnimation = 0;
        }
        this.changeAnimation(nextAnimation);
    }

    private void setPreviousAnimation() {
        int previousAnimation = this.currAnimation - 1;
        if (previousAnimation < 0) {
            previousAnimation = this.manager.getCharacter().getNumAnimations() - 1;
        }
        this.changeAnimation(previousAnimation);
    }

    private void setNextFrame() {
        int nextFrame = this.currFrame + 1;
        if (nextFrame >= this.manager.getCharacter().getAnimation(this.currAnimation).getNumFrames()) {
            nextFrame = 0;
        }
        this.changeFrame(nextFrame);
    }

    private void setPreviousFrame() {
        int previousFrame = this.currFrame + -1;
        if (previousFrame < 0) {
            previousFrame = this.manager.getCharacter().getAnimation(this.currAnimation).getNumFrames() - 1;
        }
        this.changeFrame(previousFrame);
    }

    private void updatePalettePanels() {
        Palette palette = this.manager.getPalette();
        if (palette == null) {
            palette = new Palette();
        }
        for (int i = 0; i < 16; ++i) {
            this.colorPanels[i].setBackground(new Color(palette.getColor(i)));
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        characterPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        characterCombo = new javax.swing.JComboBox();
        animationPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        animationCombo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        sizeField = new javax.swing.JTextField();
        framePanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        delayField = new javax.swing.JTextField();
        hitPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        xField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        yField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        soundField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        damageField = new javax.swing.JTextField();
        koCheck = new javax.swing.JCheckBox();
        hitCheck = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        mapField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        artField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        weaponPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        wXField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        wYField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        angleField = new javax.swing.JTextField();
        behindCheck = new javax.swing.JCheckBox();
        weaponCheck = new javax.swing.JCheckBox();
        compressedLabel = new javax.swing.JLabel();
        previewPanel = new javax.swing.JPanel();
        scrollPanel = new javax.swing.JScrollPane(imagePanel);
        playerPanel = new javax.swing.JPanel();
        backBut = new javax.swing.JButton();
        previousBut = new javax.swing.JButton();
        nextBut = new javax.swing.JButton();
        frontBut = new javax.swing.JButton();
        playToggle = new javax.swing.JToggleButton();
        colorsPanel1 = new javax.swing.JPanel();
        colorPanel2 = new javax.swing.JPanel();
        colorPanel3 = new javax.swing.JPanel();
        colorPanel4 = new javax.swing.JPanel();
        colorPanel5 = new javax.swing.JPanel();
        colorPanel6 = new javax.swing.JPanel();
        colorPanel7 = new javax.swing.JPanel();
        colorPanel8 = new javax.swing.JPanel();
        colorPanel9 = new javax.swing.JPanel();
        colorPanel10 = new javax.swing.JPanel();
        colorPanel11 = new javax.swing.JPanel();
        colorPanel12 = new javax.swing.JPanel();
        colorPanel13 = new javax.swing.JPanel();
        colorPanel14 = new javax.swing.JPanel();
        colorPanel15 = new javax.swing.JPanel();
        colorPanel16 = new javax.swing.JPanel();
        colorPanel1 = new javax.swing.JPanel();
        characterPanel1 = new javax.swing.JPanel();
        showHitsCheck = new javax.swing.JCheckBox();
        showWeaponCheck = new javax.swing.JCheckBox();
        weaponCombo = new javax.swing.JComboBox();
        showFacedRightCheck = new javax.swing.JCheckBox();
        showTileCheck = new javax.swing.JCheckBox();
        showCenterCheck = new javax.swing.JCheckBox();
        toolsPanel = new javax.swing.JPanel();
        pencilRadio = new javax.swing.JRadioButton();
        brushRadio = new javax.swing.JRadioButton();
        bucketRadio = new javax.swing.JRadioButton();
        dragSpriteRadio = new javax.swing.JRadioButton();
        dragImageRadio = new javax.swing.JRadioButton();
        noneRadio = new javax.swing.JRadioButton();
        overridePanel = new javax.swing.JPanel();
        softReplaceButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        generatePanel = new javax.swing.JPanel();
        hardReplaceButton = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        genPaletteField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        openRomMenu = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        closeMenu = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        inportMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        spriteSheetMenu1 = new javax.swing.JMenuItem();
        spriteSheetMenu = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        pencilMenu = new javax.swing.JRadioButtonMenuItem();
        brushMenu = new javax.swing.JRadioButtonMenuItem();
        bucketMenu = new javax.swing.JRadioButtonMenuItem();
        dragSpriteMenu = new javax.swing.JRadioButtonMenuItem();
        dragImageMenu = new javax.swing.JRadioButtonMenuItem();
        noneMenu = new javax.swing.JRadioButtonMenuItem();
        jMenu5 = new javax.swing.JMenu();
        sizeRadioMenu1 = new javax.swing.JRadioButtonMenuItem();
        sizeRadioMenu2 = new javax.swing.JRadioButtonMenuItem();
        sizeRadioMenu3 = new javax.swing.JRadioButtonMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        hexIdsMenu = new javax.swing.JCheckBoxMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        resizeAnimsMenu = new javax.swing.JMenuItem();
        nameMenu = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        copyMenu = new javax.swing.JMenuItem();
        pasteMenu = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        pasteMenu1 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        pasteMenu2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(TITLE);
        setLocationByPlatform(true);
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        characterPanel.setBackground(new java.awt.Color(229, 235, 235));
        characterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Character:");

        characterCombo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        characterCombo.setMaximumRowCount(16);
        characterCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                characterComboActionPerformed(evt);
            }
        });

        animationPanel.setBackground(new java.awt.Color(241, 241, 171));
        animationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Animation:");

        animationCombo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        animationCombo.setMaximumRowCount(16);
        animationCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                animationComboActionPerformed(evt);
            }
        });
        animationCombo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                animationComboKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Size:");

        sizeField.setEditable(false);
        sizeField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        sizeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        sizeField.setText("0");

        framePanel.setBackground(new java.awt.Color(240, 226, 157));
        framePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Frame #", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Delay:");

        delayField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        delayField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        delayField.setText("0");

        hitPanel.setBackground(new java.awt.Color(236, 209, 127));
        hitPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Hit"));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("x:");

        xField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        xField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        xField.setText("0");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("y:");

        yField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        yField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        yField.setText("0");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("sound:");

        soundField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        soundField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        soundField.setText("0");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("damage:");

        damageField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        damageField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        damageField.setText("0");

        koCheck.setBackground(new java.awt.Color(236, 209, 127));
        koCheck.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        koCheck.setText("Knock Down");
        koCheck.setContentAreaFilled(false);
        koCheck.setFocusable(false);
        koCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                koCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout hitPanelLayout = new javax.swing.GroupLayout(hitPanel);
        hitPanel.setLayout(hitPanelLayout);
        hitPanelLayout.setHorizontalGroup(
            hitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hitPanelLayout.createSequentialGroup()
                .addGroup(hitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hitPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hitPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(damageField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(hitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(koCheck)
                    .addGroup(hitPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(soundField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        hitPanelLayout.setVerticalGroup(
            hitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hitPanelLayout.createSequentialGroup()
                .addGroup(hitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(xField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(yField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(soundField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(hitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(damageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(koCheck))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        hitCheck.setBackground(new java.awt.Color(240, 226, 157));
        hitCheck.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        hitCheck.setText("Hit frame");
        hitCheck.setContentAreaFilled(false);
        hitCheck.setFocusable(false);
        hitCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hitCheckActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("SpriteMap:");

        mapField.setEditable(false);
        mapField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        mapField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mapField.setText("200");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Art Address:");

        artField.setEditable(false);
        artField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        artField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        artField.setText("200");

        jButton2.setText("<");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(">");
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        weaponPanel.setBackground(new java.awt.Color(217, 227, 154));
        weaponPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weapon Point"));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("x:");

        wXField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        wXField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        wXField.setText("0");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("y:");

        wYField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        wYField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        wYField.setText("0");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("rotation");

        angleField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        angleField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        angleField.setText("0");

        behindCheck.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        behindCheck.setText("Show behind");
        behindCheck.setContentAreaFilled(false);
        behindCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                behindCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout weaponPanelLayout = new javax.swing.GroupLayout(weaponPanel);
        weaponPanel.setLayout(weaponPanelLayout);
        weaponPanelLayout.setHorizontalGroup(
            weaponPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weaponPanelLayout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wXField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wYField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(angleField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(behindCheck))
        );
        weaponPanelLayout.setVerticalGroup(
            weaponPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weaponPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel15)
                .addComponent(wXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel16)
                .addComponent(wYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel17)
                .addComponent(angleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(behindCheck))
        );

        weaponCheck.setBackground(new java.awt.Color(240, 226, 157));
        weaponCheck.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        weaponCheck.setText("Weapon Point");
        weaponCheck.setContentAreaFilled(false);
        weaponCheck.setFocusable(false);
        weaponCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout framePanelLayout = new javax.swing.GroupLayout(framePanel);
        framePanel.setLayout(framePanelLayout);
        framePanelLayout.setHorizontalGroup(
            framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePanelLayout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mapField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(artField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(framePanelLayout.createSequentialGroup()
                .addGroup(framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(framePanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delayField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(hitCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3))
            .addGroup(framePanelLayout.createSequentialGroup()
                .addComponent(weaponCheck)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(hitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(weaponPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        framePanelLayout.setVerticalGroup(
            framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePanelLayout.createSequentialGroup()
                .addGroup(framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton3))
                    .addGroup(framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(delayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hitCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(weaponCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(weaponPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(mapField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(artField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout animationPanelLayout = new javax.swing.GroupLayout(animationPanel);
        animationPanel.setLayout(animationPanelLayout);
        animationPanelLayout.setHorizontalGroup(
            animationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animationPanelLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(animationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sizeField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addComponent(framePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        animationPanelLayout.setVerticalGroup(
            animationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animationPanelLayout.createSequentialGroup()
                .addGroup(animationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(animationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(framePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        compressedLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        compressedLabel.setForeground(new java.awt.Color(199, 18, 18));
        compressedLabel.setText("Compressed!");

        javax.swing.GroupLayout characterPanelLayout = new javax.swing.GroupLayout(characterPanel);
        characterPanel.setLayout(characterPanelLayout);
        characterPanelLayout.setHorizontalGroup(
            characterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(characterPanelLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(characterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compressedLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(animationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        characterPanelLayout.setVerticalGroup(
            characterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(characterPanelLayout.createSequentialGroup()
                .addGroup(characterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(characterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(compressedLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(animationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrollPanel.setBackground(new java.awt.Color(0, 0, 0));
        scrollPanel.setFocusable(false);

        backBut.setBackground(new java.awt.Color(241, 241, 171));
        backBut.setText("|<<");
        backBut.setFocusable(false);
        backBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButActionPerformed(evt);
            }
        });

        previousBut.setBackground(new java.awt.Color(240, 226, 157));
        previousBut.setText("<<");
        previousBut.setFocusable(false);
        previousBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButActionPerformed(evt);
            }
        });

        nextBut.setBackground(new java.awt.Color(240, 226, 157));
        nextBut.setText(">>");
        nextBut.setFocusable(false);
        nextBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButActionPerformed(evt);
            }
        });

        frontBut.setBackground(new java.awt.Color(241, 241, 171));
        frontBut.setText(">>|");
        frontBut.setFocusable(false);
        frontBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frontButActionPerformed(evt);
            }
        });

        playToggle.setBackground(new java.awt.Color(236, 209, 127));
        playToggle.setText(">");
        playToggle.setFocusable(false);
        playToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playToggleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout playerPanelLayout = new javax.swing.GroupLayout(playerPanel);
        playerPanel.setLayout(playerPanelLayout);
        playerPanelLayout.setHorizontalGroup(
            playerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, playerPanelLayout.createSequentialGroup()
                .addComponent(backBut, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previousBut, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playToggle, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextBut, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frontBut, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
        );
        playerPanelLayout.setVerticalGroup(
            playerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(backBut)
                .addComponent(previousBut)
                .addComponent(nextBut)
                .addComponent(frontBut)
                .addComponent(playToggle))
        );

        colorsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette"));
        colorsPanel1.setPreferredSize(new java.awt.Dimension(256, 64));

        colorPanel2.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel2.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel2MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel2Layout = new javax.swing.GroupLayout(colorPanel2);
        colorPanel2.setLayout(colorPanel2Layout);
        colorPanel2Layout.setHorizontalGroup(
            colorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel2Layout.setVerticalGroup(
            colorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel3.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel3.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel3MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel3Layout = new javax.swing.GroupLayout(colorPanel3);
        colorPanel3.setLayout(colorPanel3Layout);
        colorPanel3Layout.setHorizontalGroup(
            colorPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel3Layout.setVerticalGroup(
            colorPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel4.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel4.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel4MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel4Layout = new javax.swing.GroupLayout(colorPanel4);
        colorPanel4.setLayout(colorPanel4Layout);
        colorPanel4Layout.setHorizontalGroup(
            colorPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel4Layout.setVerticalGroup(
            colorPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel5.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel5.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel5MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel5Layout = new javax.swing.GroupLayout(colorPanel5);
        colorPanel5.setLayout(colorPanel5Layout);
        colorPanel5Layout.setHorizontalGroup(
            colorPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel5Layout.setVerticalGroup(
            colorPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel6.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel6.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel6MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel6Layout = new javax.swing.GroupLayout(colorPanel6);
        colorPanel6.setLayout(colorPanel6Layout);
        colorPanel6Layout.setHorizontalGroup(
            colorPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel6Layout.setVerticalGroup(
            colorPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel7.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel7.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel7MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel7Layout = new javax.swing.GroupLayout(colorPanel7);
        colorPanel7.setLayout(colorPanel7Layout);
        colorPanel7Layout.setHorizontalGroup(
            colorPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel7Layout.setVerticalGroup(
            colorPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel8.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel8.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel8MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel8Layout = new javax.swing.GroupLayout(colorPanel8);
        colorPanel8.setLayout(colorPanel8Layout);
        colorPanel8Layout.setHorizontalGroup(
            colorPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel8Layout.setVerticalGroup(
            colorPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel9.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel9.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel9MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel9Layout = new javax.swing.GroupLayout(colorPanel9);
        colorPanel9.setLayout(colorPanel9Layout);
        colorPanel9Layout.setHorizontalGroup(
            colorPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel9Layout.setVerticalGroup(
            colorPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel10.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel10.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel10MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel10Layout = new javax.swing.GroupLayout(colorPanel10);
        colorPanel10.setLayout(colorPanel10Layout);
        colorPanel10Layout.setHorizontalGroup(
            colorPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel10Layout.setVerticalGroup(
            colorPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel11.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel11.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel11MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel11Layout = new javax.swing.GroupLayout(colorPanel11);
        colorPanel11.setLayout(colorPanel11Layout);
        colorPanel11Layout.setHorizontalGroup(
            colorPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel11Layout.setVerticalGroup(
            colorPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel12.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel12.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel12MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel12Layout = new javax.swing.GroupLayout(colorPanel12);
        colorPanel12.setLayout(colorPanel12Layout);
        colorPanel12Layout.setHorizontalGroup(
            colorPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel12Layout.setVerticalGroup(
            colorPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel13.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel13.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel13.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel13MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel13Layout = new javax.swing.GroupLayout(colorPanel13);
        colorPanel13.setLayout(colorPanel13Layout);
        colorPanel13Layout.setHorizontalGroup(
            colorPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel13Layout.setVerticalGroup(
            colorPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel14.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel14.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel14.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel14MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel14Layout = new javax.swing.GroupLayout(colorPanel14);
        colorPanel14.setLayout(colorPanel14Layout);
        colorPanel14Layout.setHorizontalGroup(
            colorPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel14Layout.setVerticalGroup(
            colorPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel15.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel15.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel15.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel15MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel15Layout = new javax.swing.GroupLayout(colorPanel15);
        colorPanel15.setLayout(colorPanel15Layout);
        colorPanel15Layout.setHorizontalGroup(
            colorPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel15Layout.setVerticalGroup(
            colorPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel16.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel16.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel16.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel16MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel16Layout = new javax.swing.GroupLayout(colorPanel16);
        colorPanel16.setLayout(colorPanel16Layout);
        colorPanel16Layout.setHorizontalGroup(
            colorPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel16Layout.setVerticalGroup(
            colorPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel1.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel1.setPreferredSize(new java.awt.Dimension(32, 32));
        colorPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorPanel1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout colorPanel1Layout = new javax.swing.GroupLayout(colorPanel1);
        colorPanel1.setLayout(colorPanel1Layout);
        colorPanel1Layout.setHorizontalGroup(
            colorPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel1Layout.setVerticalGroup(
            colorPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorsPanel1Layout = new javax.swing.GroupLayout(colorsPanel1);
        colorsPanel1.setLayout(colorsPanel1Layout);
        colorsPanel1Layout.setHorizontalGroup(
            colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(colorsPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(colorPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(colorPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(colorPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(colorPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(colorPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(colorPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(colorPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(colorPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)))
        );
        colorsPanel1Layout.setVerticalGroup(
            colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPanel1Layout.createSequentialGroup()
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(playerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(colorsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
            .addComponent(scrollPanel, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(previewPanelLayout.createSequentialGroup()
                .addComponent(scrollPanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorsPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        characterPanel1.setBackground(new java.awt.Color(228, 236, 191));
        characterPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "View", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        showHitsCheck.setBackground(new java.awt.Color(228, 236, 191));
        showHitsCheck.setSelected(true);
        showHitsCheck.setText("Hit Point");
        showHitsCheck.setContentAreaFilled(false);
        showHitsCheck.setFocusable(false);
        showHitsCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHitsCheckActionPerformed(evt);
            }
        });

        showWeaponCheck.setBackground(new java.awt.Color(228, 236, 191));
        showWeaponCheck.setSelected(true);
        showWeaponCheck.setText("Weapon:");
        showWeaponCheck.setContentAreaFilled(false);
        showWeaponCheck.setFocusable(false);
        showWeaponCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showWeaponCheckActionPerformed(evt);
            }
        });

        weaponCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Knife", "Pipe" }));
        weaponCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponComboActionPerformed(evt);
            }
        });

        showFacedRightCheck.setBackground(new java.awt.Color(228, 236, 191));
        showFacedRightCheck.setSelected(true);
        showFacedRightCheck.setText("Faced Right");
        showFacedRightCheck.setContentAreaFilled(false);
        showFacedRightCheck.setFocusable(false);
        showFacedRightCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFacedRightCheckActionPerformed(evt);
            }
        });

        showTileCheck.setBackground(new java.awt.Color(228, 236, 191));
        showTileCheck.setSelected(true);
        showTileCheck.setText("Tile Space");
        showTileCheck.setContentAreaFilled(false);
        showTileCheck.setFocusable(false);
        showTileCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTileCheckActionPerformed(evt);
            }
        });

        showCenterCheck.setBackground(new java.awt.Color(228, 236, 191));
        showCenterCheck.setText("Ghost");
        showCenterCheck.setContentAreaFilled(false);
        showCenterCheck.setFocusable(false);
        showCenterCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showCenterCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout characterPanel1Layout = new javax.swing.GroupLayout(characterPanel1);
        characterPanel1.setLayout(characterPanel1Layout);
        characterPanel1Layout.setHorizontalGroup(
            characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(characterPanel1Layout.createSequentialGroup()
                .addGroup(characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showWeaponCheck)
                    .addComponent(showCenterCheck)
                    .addComponent(showHitsCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weaponCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showFacedRightCheck)
                    .addComponent(showTileCheck)))
        );
        characterPanel1Layout.setVerticalGroup(
            characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(characterPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showFacedRightCheck)
                    .addComponent(showCenterCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showTileCheck)
                    .addComponent(showHitsCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weaponCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showWeaponCheck)))
        );

        toolsPanel.setBackground(new java.awt.Color(230, 230, 235));
        toolsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tools", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        pencilRadio.setBackground(new java.awt.Color(230, 226, 235));
        pencilRadio.setText("Pencil");
        pencilRadio.setContentAreaFilled(false);
        pencilRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pencilRadioActionPerformed(evt);
            }
        });

        brushRadio.setBackground(new java.awt.Color(230, 226, 235));
        brushRadio.setText("Brush");
        brushRadio.setContentAreaFilled(false);
        brushRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brushRadioActionPerformed(evt);
            }
        });

        bucketRadio.setBackground(new java.awt.Color(230, 226, 235));
        bucketRadio.setText("Paint Bucket");
        bucketRadio.setContentAreaFilled(false);
        bucketRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bucketRadioActionPerformed(evt);
            }
        });

        dragSpriteRadio.setBackground(new java.awt.Color(230, 226, 235));
        dragSpriteRadio.setText("Drag Sprite");
        dragSpriteRadio.setContentAreaFilled(false);
        dragSpriteRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dragSpriteRadioActionPerformed(evt);
            }
        });

        dragImageRadio.setBackground(new java.awt.Color(230, 226, 235));
        dragImageRadio.setText("Drag Image");
        dragImageRadio.setContentAreaFilled(false);
        dragImageRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dragImageRadioActionPerformed(evt);
            }
        });

        noneRadio.setBackground(new java.awt.Color(230, 226, 235));
        noneRadio.setSelected(true);
        noneRadio.setText("None");
        noneRadio.setContentAreaFilled(false);
        noneRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noneRadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout toolsPanelLayout = new javax.swing.GroupLayout(toolsPanel);
        toolsPanel.setLayout(toolsPanelLayout);
        toolsPanelLayout.setHorizontalGroup(
            toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(toolsPanelLayout.createSequentialGroup()
                        .addComponent(bucketRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(noneRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addGroup(toolsPanelLayout.createSequentialGroup()
                        .addGroup(toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(toolsPanelLayout.createSequentialGroup()
                                .addComponent(pencilRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dragSpriteRadio))
                            .addGroup(toolsPanelLayout.createSequentialGroup()
                                .addComponent(brushRadio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dragImageRadio)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        toolsPanelLayout.setVerticalGroup(
            toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolsPanelLayout.createSequentialGroup()
                .addGroup(toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pencilRadio)
                    .addComponent(dragSpriteRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brushRadio)
                    .addComponent(dragImageRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bucketRadio)
                    .addComponent(noneRadio)))
        );

        overridePanel.setBackground(new java.awt.Color(240, 233, 221));
        overridePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Override Art", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        softReplaceButton.setText("Replace from Image");
        softReplaceButton.setFocusable(false);
        softReplaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                softReplaceButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Replace art using same tiles");

        javax.swing.GroupLayout overridePanelLayout = new javax.swing.GroupLayout(overridePanel);
        overridePanel.setLayout(overridePanelLayout);
        overridePanelLayout.setHorizontalGroup(
            overridePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overridePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(overridePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, overridePanelLayout.createSequentialGroup()
                        .addComponent(softReplaceButton)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, overridePanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap())))
        );
        overridePanelLayout.setVerticalGroup(
            overridePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, overridePanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(softReplaceButton))
        );

        generatePanel.setBackground(new java.awt.Color(240, 221, 221));
        generatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Generate Sprite", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        hardReplaceButton.setText("Generate From Image");
        hardReplaceButton.setFocusable(false);
        hardReplaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hardReplaceButtonActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Palette Line:");

        genPaletteField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        genPaletteField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        genPaletteField.setText("0");

        jLabel4.setText("Produce new tiles");

        javax.swing.GroupLayout generatePanelLayout = new javax.swing.GroupLayout(generatePanel);
        generatePanel.setLayout(generatePanelLayout);
        generatePanelLayout.setHorizontalGroup(
            generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generatePanelLayout.createSequentialGroup()
                .addGroup(generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generatePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(hardReplaceButton))
                    .addGroup(generatePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(generatePanelLayout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(genPaletteField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 2, Short.MAX_VALUE))
        );
        generatePanelLayout.setVerticalGroup(
            generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generatePanelLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(genPaletteField)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hardReplaceButton))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(characterPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(overridePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(toolsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(generatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(characterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(characterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(characterPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toolsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(overridePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        openRomMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        openRomMenu.setText("Open Rom");
        openRomMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openRomMenuActionPerformed(evt);
            }
        });
        jMenu1.add(openRomMenu);

        jMenuItem4.setText("Open with Specific Guide");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        closeMenu.setText("Close");
        closeMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuActionPerformed(evt);
            }
        });
        jMenu1.add(closeMenu);
        jMenu1.add(jSeparator2);

        inportMenu.setText("Inport Character...");

        jMenuItem1.setText("Replace from Spritesheet");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        inportMenu.add(jMenuItem1);

        jMenuItem2.setText("Generate from Spritesheet");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        inportMenu.add(jMenuItem2);
        inportMenu.add(jSeparator5);

        jMenuItem3.setText("Import from other ROM");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        inportMenu.add(jMenuItem3);

        jMenu1.add(inportMenu);

        exportMenu.setText("Export Character...");

        spriteSheetMenu1.setText("Individual Frames");
        spriteSheetMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spriteSheetMenu1ActionPerformed(evt);
            }
        });
        exportMenu.add(spriteSheetMenu1);

        spriteSheetMenu.setText("Spritesheet");
        spriteSheetMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spriteSheetMenuActionPerformed(evt);
            }
        });
        exportMenu.add(spriteSheetMenu);

        jMenu1.add(exportMenu);
        jMenu1.add(jSeparator1);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem5.setText("Exit");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenu6.setText("Tool");

        pencilMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.ALT_MASK));
        pencilMenu.setText("Pencil");
        pencilMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pencilMenuActionPerformed(evt);
            }
        });
        jMenu6.add(pencilMenu);

        brushMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.ALT_MASK));
        brushMenu.setText("Brush");
        brushMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brushMenuActionPerformed(evt);
            }
        });
        jMenu6.add(brushMenu);

        bucketMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.ALT_MASK));
        bucketMenu.setText("Paint Bucket");
        bucketMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bucketMenuActionPerformed(evt);
            }
        });
        jMenu6.add(bucketMenu);

        dragSpriteMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.ALT_MASK));
        dragSpriteMenu.setText("Drag Sprite");
        dragSpriteMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dragSpriteMenuActionPerformed(evt);
            }
        });
        jMenu6.add(dragSpriteMenu);

        dragImageMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.ALT_MASK));
        dragImageMenu.setText("Drag Image");
        dragImageMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dragImageMenuActionPerformed(evt);
            }
        });
        jMenu6.add(dragImageMenu);

        noneMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.InputEvent.ALT_MASK));
        noneMenu.setSelected(true);
        noneMenu.setText("None");
        noneMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noneMenuActionPerformed(evt);
            }
        });
        jMenu6.add(noneMenu);

        jMenu2.add(jMenu6);

        jMenu5.setText("Brush Size");

        sizeRadioMenu1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.SHIFT_MASK));
        sizeRadioMenu1.setSelected(true);
        sizeRadioMenu1.setText("3 pixels");
        sizeRadioMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sizeRadioMenu1ActionPerformed(evt);
            }
        });
        jMenu5.add(sizeRadioMenu1);

        sizeRadioMenu2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.SHIFT_MASK));
        sizeRadioMenu2.setText("5 pixels");
        sizeRadioMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sizeRadioMenu2ActionPerformed(evt);
            }
        });
        jMenu5.add(sizeRadioMenu2);

        sizeRadioMenu3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.SHIFT_MASK));
        sizeRadioMenu3.setText("10 pixels");
        sizeRadioMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sizeRadioMenu3ActionPerformed(evt);
            }
        });
        jMenu5.add(sizeRadioMenu3);

        jMenu2.add(jMenu5);

        jMenu4.setText("Scale");

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Reset Scale");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem11);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("2x Scale");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("6x Scale");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setText("12x Scale");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenu2.add(jMenu4);

        hexIdsMenu.setText("See IDs as Hex");
        hexIdsMenu.setEnabled(false);
        hexIdsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hexIdsMenuActionPerformed(evt);
            }
        });
        jMenu2.add(hexIdsMenu);
        jMenu2.add(jSeparator3);

        resizeAnimsMenu.setText("Resize Animations");
        resizeAnimsMenu.setEnabled(false);
        resizeAnimsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resizeAnimsMenuActionPerformed(evt);
            }
        });
        jMenu2.add(resizeAnimsMenu);

        nameMenu.setText("Properties");
        nameMenu.setEnabled(false);
        nameMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameMenuActionPerformed(evt);
            }
        });
        jMenu2.add(nameMenu);
        jMenu2.add(jSeparator4);

        copyMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        copyMenu.setText("Copy Frame");
        copyMenu.setEnabled(false);
        copyMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuActionPerformed(evt);
            }
        });
        jMenu2.add(copyMenu);

        pasteMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        pasteMenu.setText("Paste Frame");
        pasteMenu.setEnabled(false);
        pasteMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenuActionPerformed(evt);
            }
        });
        jMenu2.add(pasteMenu);
        jMenu2.add(jSeparator6);

        pasteMenu1.setText("Decompress Art");
        pasteMenu1.setEnabled(false);
        pasteMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenu1ActionPerformed(evt);
            }
        });
        jMenu2.add(pasteMenu1);
        jMenu2.add(jSeparator7);

        pasteMenu2.setText("Delete Character !!");
        pasteMenu2.setActionCommand("Delete Character");
        pasteMenu2.setEnabled(false);
        pasteMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenu2ActionPerformed(evt);
            }
        });
        jMenu2.add(pasteMenu2);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");

        jMenuItem6.setText("About");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void colorPanel2MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel2MousePressed
        this.colorPanelPressed(1);
    }//GEN-LAST:event_colorPanel2MousePressed

    private void colorPanel3MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel3MousePressed
        this.colorPanelPressed(2);
    }//GEN-LAST:event_colorPanel3MousePressed

    private void colorPanel4MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel4MousePressed
        this.colorPanelPressed(3);
    }//GEN-LAST:event_colorPanel4MousePressed

    private void colorPanel5MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel5MousePressed
        this.colorPanelPressed(4);
    }//GEN-LAST:event_colorPanel5MousePressed

    private void colorPanel6MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel6MousePressed
        this.colorPanelPressed(5);
    }//GEN-LAST:event_colorPanel6MousePressed

    private void colorPanel7MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel7MousePressed
        this.colorPanelPressed(6);
    }//GEN-LAST:event_colorPanel7MousePressed

    private void colorPanel8MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel8MousePressed
        this.colorPanelPressed(7);
    }//GEN-LAST:event_colorPanel8MousePressed

    private void colorPanel9MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel9MousePressed
        this.colorPanelPressed(8);
    }//GEN-LAST:event_colorPanel9MousePressed

    private void colorPanel10MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel10MousePressed
        this.colorPanelPressed(9);
    }//GEN-LAST:event_colorPanel10MousePressed

    private void colorPanel11MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel11MousePressed
        this.colorPanelPressed(10);
    }//GEN-LAST:event_colorPanel11MousePressed

    private void colorPanel12MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel12MousePressed
        this.colorPanelPressed(11);
    }//GEN-LAST:event_colorPanel12MousePressed

    private void colorPanel13MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel13MousePressed
        this.colorPanelPressed(12);
    }//GEN-LAST:event_colorPanel13MousePressed

    private void colorPanel14MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel14MousePressed
        this.colorPanelPressed(13);
    }//GEN-LAST:event_colorPanel14MousePressed

    private void colorPanel15MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel15MousePressed
        this.colorPanelPressed(14);
    }//GEN-LAST:event_colorPanel15MousePressed

    private void colorPanel16MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel16MousePressed
        this.colorPanelPressed(15);
    }//GEN-LAST:event_colorPanel16MousePressed

    private void colorPanel1MousePressed(MouseEvent evt) {//GEN-FIRST:event_colorPanel1MousePressed
        this.colorPanelPressed(0);
    }//GEN-LAST:event_colorPanel1MousePressed

    private void formMouseWheelMoved(MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        this.scaleImagePanel(-0.2f * (float)evt.getWheelRotation());
    }//GEN-LAST:event_formMouseWheelMoved

    private void jMenuItem11ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        this.setImagePanelScale(1.0f);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem7ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        this.setImagePanelScale(2.0f);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem9ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        this.setImagePanelScale(6.0f);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        this.setImagePanelScale(12.0f);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void openRomMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_openRomMenuActionPerformed
        Guide oldGuide = this.guide;
        if (this.openDefaultGuide() || this.openCustomGuide()) {
            if (!this.openRom()) {
                this.guide = oldGuide;
            }
        } else {
            this.guide = oldGuide;
        }
    }//GEN-LAST:event_openRomMenuActionPerformed

    private void closeMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_closeMenuActionPerformed
        this.closeRom();
    }//GEN-LAST:event_closeMenuActionPerformed

    private void jMenuItem4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Guide oldGuide = this.guide;
        if (this.openCustomGuide()) {
            if (!this.openRom()) {
                this.guide = oldGuide;
            }
        } else {
            this.guide = oldGuide;
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (this.askSaveRom()) {
            this.closeRom();
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.setNextFrame();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.setPreviousFrame();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void nextButActionPerformed(ActionEvent evt) {//GEN-FIRST:event_nextButActionPerformed
        this.setNextFrame();
    }//GEN-LAST:event_nextButActionPerformed

    private void previousButActionPerformed(ActionEvent evt) {//GEN-FIRST:event_previousButActionPerformed
        this.setPreviousFrame();
    }//GEN-LAST:event_previousButActionPerformed

    private void animationComboActionPerformed(ActionEvent evt) {//GEN-FIRST:event_animationComboActionPerformed
        int newAnim = this.animationCombo.getSelectedIndex();
        if (newAnim != this.currAnimation && newAnim >= 0) {
            this.changeAnimation(newAnim);
        }
    }//GEN-LAST:event_animationComboActionPerformed

    private void frontButActionPerformed(ActionEvent evt) {//GEN-FIRST:event_frontButActionPerformed
        this.setNextAnimation();
    }//GEN-LAST:event_frontButActionPerformed

    private void backButActionPerformed(ActionEvent evt) {//GEN-FIRST:event_backButActionPerformed
        this.setPreviousAnimation();
    }//GEN-LAST:event_backButActionPerformed

    private void animationComboKeyPressed(KeyEvent evt) {//GEN-FIRST:event_animationComboKeyPressed
        switch (evt.getKeyCode()) {
            case 39: {
                this.setNextFrame();
                break;
            }
            case 37: {
                this.setPreviousFrame();
            }
        }
    }//GEN-LAST:event_animationComboKeyPressed

    private void characterComboActionPerformed(ActionEvent evt) {//GEN-FIRST:event_characterComboActionPerformed
        if (this.manager == null || this.guide == null) {
            return;
        }
        int newChar = this.guide.getRealCharId(this.characterCombo.getSelectedIndex());
        if (newChar != this.manager.getCurrentCharacterId() && newChar >= 0) {
            try {
                this.changeCharacter(newChar);
            }
            catch (IOException ex) {
                ex.printStackTrace();
                this.showError("Unable to read '" + this.characterCombo.getSelectedItem() + "' character");
            }
        }
    }//GEN-LAST:event_characterComboActionPerformed

    private void playToggleActionPerformed(ActionEvent evt) {//GEN-FIRST:event_playToggleActionPerformed
        if (this.playToggle.isSelected()) {
            this.timer.start();
            this.playToggle.setText("[]");
        } else {
            this.timer.stop();
            this.playToggle.setText(">");
        }
    }//GEN-LAST:event_playToggleActionPerformed

    private void showHitsCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showHitsCheckActionPerformed
        this.imagePanel.showHit(this.showHitsCheck.isSelected());
    }//GEN-LAST:event_showHitsCheckActionPerformed

    private void showCenterCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showCenterCheckActionPerformed
        this.imagePanel.showGhost(this.showCenterCheck.isSelected());
    }//GEN-LAST:event_showCenterCheckActionPerformed

    private void showTileCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showTileCheckActionPerformed
        this.imagePanel.showShadow(this.showTileCheck.isSelected());
    }//GEN-LAST:event_showTileCheckActionPerformed

    private void hitCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_hitCheckActionPerformed
        Character ch = this.manager.getCharacter();
        HitFrame hitFrame = ch.getHitFrame(this.currAnimation, this.currFrame);
        boolean selected = this.hitCheck.isSelected();
        if (hitFrame.isEnabled() != selected) {
            hitFrame.setEnabled(selected);
            ch.setModified(true);
            this.refresh();
        }
    }//GEN-LAST:event_hitCheckActionPerformed

    private void koCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_koCheckActionPerformed
        Character ch = this.manager.getCharacter();
        HitFrame hitFrame = ch.getHitFrame(this.currAnimation, this.currFrame);
        boolean selected = this.koCheck.isSelected();
        if (hitFrame.knockDown != selected) {
            hitFrame.knockDown = selected;
            ch.setModified(true);
            this.refresh();
        }
    }//GEN-LAST:event_koCheckActionPerformed

    private void formWindowClosing(WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (this.askSaveRom()) {
            this.closeRom();
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void softReplaceButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_softReplaceButtonActionPerformed
        int returnVal = this.imageChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.imageChooser.getSelectedFile();
            try {
                BufferedImage replaceImg = ImageIO.read(file);
                replaceImg = this.processReplaceImg(replaceImg);
                this.dragImageRadio.setEnabled(true);
                this.imagePanel.setReplaceImage(replaceImg);
            }
            catch (IOException ex) {
                ex.printStackTrace();
                this.showError("Unable to read image file: " + file.getName());
                this.imagePanel.setReplaceImage(null);
                this.dragImageRadio.setEnabled(false);
            }
            this.manager.getCharacter().getAnimation(this.currAnimation).setSpritesModified(this.currFrame, true);
            this.manager.getCharacter().setSpritesModified(true);
            this.manager.getCharacter().setModified(true);
        }
    }//GEN-LAST:event_softReplaceButtonActionPerformed

    private void hardReplaceButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_hardReplaceButtonActionPerformed
        int returnVal = this.imageChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.imageChooser.getSelectedFile();
            try {
                this.manager.getCharacter().setModified(true);
                this.manager.getCharacter().setSpritesModified(true);
                this.manager.getCharacter().getAnimation(this.currAnimation).setSpritesModified(this.currFrame, true);
                BufferedImage replaceImg = ImageIO.read(file);
                if (this.imagePanel.isFacedRight()) {
                    replaceImg = ImagePanel.flipImage(replaceImg);
                }
                replaceImg = this.processReplaceImg(replaceImg);
                if (this.lastcX == -1) {
                    this.lastcX = (int)((double)replaceImg.getWidth() * 0.5);
                    this.lastcY = (int)((double)replaceImg.getHeight() * 0.95);
                }
                int cx = this.lastcX;
                int cy = this.lastcY;
                this.regenerateSprite(replaceImg, this.currAnimation, this.currFrame, cx, cy, true);
                this.wasFrameReplaced = true;
                try {
                    this.manager.bufferAnimFrame(this.currAnimation, this.currFrame);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    this.showError("Unable to save the generated sprite");
                }
                this.dragImageRadio.setEnabled(false);
                this.hardRefresh();
            }
            catch (IOException ex) {
                ex.printStackTrace();
                this.showError("Unable to read image file: " + file.getName());
            }
        }
    }//GEN-LAST:event_hardReplaceButtonActionPerformed

    private void pencilRadioActionPerformed(ActionEvent evt) {//GEN-FIRST:event_pencilRadioActionPerformed
        this.setRadiosOff();
        this.pencilRadio.setSelected(true);
        this.imagePanel.setMode(Mode.pencil);
    }//GEN-LAST:event_pencilRadioActionPerformed

    private void brushRadioActionPerformed(ActionEvent evt) {//GEN-FIRST:event_brushRadioActionPerformed
        this.setRadiosOff();
        this.brushRadio.setSelected(true);
        this.imagePanel.setMode(Mode.brush);
    }//GEN-LAST:event_brushRadioActionPerformed

    private void bucketRadioActionPerformed(ActionEvent evt) {//GEN-FIRST:event_bucketRadioActionPerformed
        this.setRadiosOff();
        this.bucketRadio.setSelected(true);
        this.imagePanel.setMode(Mode.bucket);
    }//GEN-LAST:event_bucketRadioActionPerformed

    private void dragSpriteRadioActionPerformed(ActionEvent evt) {//GEN-FIRST:event_dragSpriteRadioActionPerformed
        this.setRadiosOff();
        this.dragSpriteRadio.setSelected(true);
        this.imagePanel.setMode(Mode.dragSprite);
    }//GEN-LAST:event_dragSpriteRadioActionPerformed

    private void dragImageRadioActionPerformed(ActionEvent evt) {//GEN-FIRST:event_dragImageRadioActionPerformed
        this.setRadiosOff();
        this.dragImageRadio.setSelected(true);
        this.imagePanel.setMode(Mode.dragImage);
    }//GEN-LAST:event_dragImageRadioActionPerformed

    private void noneRadioActionPerformed(ActionEvent evt) {//GEN-FIRST:event_noneRadioActionPerformed
        this.setRadiosOff();
        this.noneRadio.setSelected(true);
        this.imagePanel.setMode(Mode.none);
    }//GEN-LAST:event_noneRadioActionPerformed

    private void sizeRadioMenu1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_sizeRadioMenu1ActionPerformed
        this.setSizeRadioMenusOff();
        this.sizeRadioMenu1.setSelected(true);
        this.imagePanel.setBrushSize(3);
    }//GEN-LAST:event_sizeRadioMenu1ActionPerformed

    private void sizeRadioMenu2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_sizeRadioMenu2ActionPerformed
        this.setSizeRadioMenusOff();
        this.sizeRadioMenu2.setSelected(true);
        this.imagePanel.setBrushSize(5);
    }//GEN-LAST:event_sizeRadioMenu2ActionPerformed

    private void sizeRadioMenu3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_sizeRadioMenu3ActionPerformed
        this.setSizeRadioMenusOff();
        this.sizeRadioMenu3.setSelected(true);
        this.imagePanel.setBrushSize(10);
    }//GEN-LAST:event_sizeRadioMenu3ActionPerformed

    private void bucketMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_bucketMenuActionPerformed
        this.bucketRadioActionPerformed(null);
    }//GEN-LAST:event_bucketMenuActionPerformed

    private void pencilMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_pencilMenuActionPerformed
        this.pencilRadioActionPerformed(null);
    }//GEN-LAST:event_pencilMenuActionPerformed

    private void brushMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_brushMenuActionPerformed
        this.brushRadioActionPerformed(null);
    }//GEN-LAST:event_brushMenuActionPerformed

    private void dragSpriteMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_dragSpriteMenuActionPerformed
        this.dragSpriteRadioActionPerformed(null);
    }//GEN-LAST:event_dragSpriteMenuActionPerformed

    private void dragImageMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_dragImageMenuActionPerformed
        this.dragImageRadioActionPerformed(null);
    }//GEN-LAST:event_dragImageMenuActionPerformed

    private void noneMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_noneMenuActionPerformed
        this.noneRadioActionPerformed(null);
    }//GEN-LAST:event_noneMenuActionPerformed

    private void showWeaponCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showWeaponCheckActionPerformed
        this.weaponCombo.setEnabled(this.showWeaponCheck.isSelected());
        this.imagePanel.showWeapon(this.showWeaponCheck.isSelected());
    }//GEN-LAST:event_showWeaponCheckActionPerformed

    private void weaponCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_weaponCheckActionPerformed
        Character ch = this.manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        boolean selected = this.weaponCheck.isSelected();
        if (frame.isEnabled() != selected) {
            frame.setEnabled(selected);
            ch.setModified(true);
            this.refresh();
        }
    }//GEN-LAST:event_weaponCheckActionPerformed

    private void weaponComboActionPerformed(ActionEvent evt) {//GEN-FIRST:event_weaponComboActionPerformed
        this.imagePanel.setWeaponPreview(this.weaponCombo.getSelectedIndex());
    }//GEN-LAST:event_weaponComboActionPerformed

    private void behindCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_behindCheckActionPerformed
        Character ch = this.manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        boolean selected = this.behindCheck.isSelected();
        if (frame.showBehind != selected) {
            frame.showBehind = selected;
            ch.setModified(true);
            this.refresh();
        }
    }//GEN-LAST:event_behindCheckActionPerformed

    private void jMenuItem6ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        JOptionPane.showMessageDialog(this, "Pancake 2 " + VERSION + "\n\u00a9 gsaurus 2012-" + YEAR + "\n\nAcknowledgment on derived work\nwould be appreciated but is not required\n\nPk2 is free software. The author can not be held responsible\nfor any illicit use of this program.\n", "About", 1);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void spriteSheetMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_spriteSheetMenuActionPerformed
        this.exportSpriteSheet();
    }//GEN-LAST:event_spriteSheetMenuActionPerformed

    private void showFacedRightCheckActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showFacedRightCheckActionPerformed
        this.imagePanel.setFacedRight(this.showFacedRightCheck.isSelected());
    }//GEN-LAST:event_showFacedRightCheckActionPerformed

    private void jMenuItem1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        BufferedImage replaceImg;
        int charId = this.manager.getCurrentCharacterId();
        String charName = this.guide.getCharName(this.guide.getFakeCharId(charId));
        int returnVal = this.imageChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.imageChooser.getSelectedFile();
            try {
                replaceImg = ImageIO.read(file);
                replaceImg = this.processReplaceImg(replaceImg);
            }
            catch (IOException e) {
                e.printStackTrace();
                this.showError("Unable to read image " + file.getName());
                return;
            }
        } else {
            return;
        }
        JTextField columnsField = new JTextField();
        JTextField rowsField = new JTextField();
        JTextField cxField = new JTextField();
        JTextField cyField = new JTextField();
        JComponent[] inputs = new JComponent[]{new JLabel("Number of columns:"), columnsField, new JLabel("Number of rows:"), rowsField, new JLabel("Sprite center X:"), cxField, new JLabel("Sprite center Y:"), cyField};
        int res = JOptionPane.showConfirmDialog(null, inputs, charName + "art replacer", 2);
        if (res != 0) {
            return;
        }
        int columns = this.getIntFromField(columnsField, 1, 9999);
        int rows = this.getIntFromField(rowsField, 1, 9999);
        int cx = this.getIntFromField(cxField, 0, 256);
        int cy = this.getIntFromField(cyField, 0, 256);
        if (columns == Integer.MIN_VALUE || rows == Integer.MIN_VALUE || cx == Integer.MIN_VALUE || cy == Integer.MIN_VALUE) {
            this.showError("Invalid columns/rows/center");
            return;
        }
        if (this.timer.isRunning()) {
            this.playToggleActionPerformed(null);
        }
        this.importSpriteReplacer(replaceImg, columns, rows, cx, cy);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        BufferedImage replaceImg;
        int charId = this.manager.getCurrentCharacterId();
        String charName = this.guide.getCharName(this.guide.getFakeCharId(charId));
        int returnVal = this.imageChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.imageChooser.getSelectedFile();
            try {
                replaceImg = ImageIO.read(file);
                replaceImg = this.processReplaceImg(replaceImg);
            }
            catch (IOException e) {
                e.printStackTrace();
                this.showError("Unable to read image " + file.getName());
                return;
            }
        } else {
            return;
        }
        JTextField columnsField = new JTextField();
        JTextField rowsField = new JTextField();
        JTextField cxField = new JTextField();
        JTextField cyField = new JTextField();
        JComponent[] inputs = new JComponent[]{new JLabel("Number of columns:"), columnsField, new JLabel("Number of rows:"), rowsField, new JLabel("Sprite center X:"), cxField, new JLabel("Sprite center Y:"), cyField};
        int res = JOptionPane.showConfirmDialog(null, inputs, charName + "art replacer", 2);
        if (res != 0) {
            return;
        }
        int columns = this.getIntFromField(columnsField, 1, 9999);
        int rows = this.getIntFromField(rowsField, 1, 9999);
        int cx = this.getIntFromField(cxField, 0, 256);
        int cy = this.getIntFromField(cyField, 0, 256);
        if (columns == Integer.MIN_VALUE || rows == Integer.MIN_VALUE || cx == Integer.MIN_VALUE || cy == Integer.MIN_VALUE) {
            this.showError("Invalid columns/rows/center");
            return;
        }
        if (this.timer.isRunning()) {
            this.playToggleActionPerformed(null);
        }
        this.importSpriteGenerator(replaceImg, columns, rows, cx, cy);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void nameMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_nameMenuActionPerformed
        new PropertiesDialog(this, this.manager).setVisible(true);
    }//GEN-LAST:event_nameMenuActionPerformed

    public BufferedImage openImageForPortrait() {
        int returnVal = this.imageChooser.showOpenDialog(this);
        if (returnVal == 0) {
            BufferedImage img;
            File file = this.imageChooser.getSelectedFile();
            try {
                img = ImageIO.read(file);
                img = this.processReplaceImg(img, false, manager.readDefaultPalette());
            }
            catch (IOException ex) {
                ex.printStackTrace();
                this.showError("Unable to read image file: " + file.getName());
                return null;
            }
            if (img.getWidth() < 16 || img.getHeight() < 16) {
                this.showError("Image too small");
                return null;
            }
            return img;
        }
        return null;
    }

    private void copyMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_copyMenuActionPerformed
        this.copiedMap = this.getHexFromField(this.mapField);
        this.copiedArt = this.getHexFromField(this.artField);
        this.copiedHasHit = this.hitCheck.isSelected();
        this.copiedKnockDown = this.koCheck.isSelected();
        this.copiedHasWeapon = this.weaponCheck.isSelected();
        this.copiedWpShowBehind = this.behindCheck.isSelected();
        this.copiedHitX = this.getIntFromField(this.xField);
        this.copiedHitY = this.getIntFromField(this.yField);
        this.copiedHitSound = this.getIntFromField(this.soundField);
        this.copiedHitDamage = this.getIntFromField(this.damageField);
        this.copiedWpX = this.getIntFromField(this.wXField);
        this.copiedWpY = this.getIntFromField(this.wYField);
        this.copiedWpRotation = this.getIntFromField(this.angleField);
        this.pasteMenu.setEnabled(true);
    }//GEN-LAST:event_copyMenuActionPerformed

    private void pasteMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_pasteMenuActionPerformed
        this.setFieldAsHex(this.mapField, this.copiedMap);
        this.mapAddressChanged();
        this.setFieldAsHex(this.artField, this.copiedArt);
        this.artAddressChanged();
        if (this.hitCheck.isEnabled()) {
            this.hitCheck.setSelected(this.copiedHasHit);
            this.hitCheckActionPerformed(null);
            this.koCheck.setSelected(this.copiedKnockDown);
            this.koCheckActionPerformed(null);
            this.setField(this.xField, this.copiedHitX);
            this.hitXChanged();
            this.setField(this.yField, this.copiedHitY);
            this.hitYChanged();
            this.setField(this.soundField, this.copiedHitSound);
            this.hitSoundChanged();
            this.setField(this.damageField, this.copiedHitDamage);
            this.hitDamageChanged();
        }
        if (this.weaponCheck.isEnabled()) {
            this.weaponCheck.setSelected(this.copiedHasWeapon);
            this.weaponCheckActionPerformed(null);
            this.behindCheck.setSelected(this.copiedWpShowBehind);
            this.behindCheckActionPerformed(null);
            this.setField(this.wXField, this.copiedWpX);
            this.weaponXChanged();
            this.setField(this.wYField, this.copiedWpY);
            this.weaponYChanged();
            this.setField(this.angleField, this.copiedWpRotation);
            this.weaponAngleChanged();
        }
    }//GEN-LAST:event_pasteMenuActionPerformed

    private void spriteSheetMenu1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_spriteSheetMenu1ActionPerformed
        this.exportIndividualFrames();
    }//GEN-LAST:event_spriteSheetMenu1ActionPerformed

    private void resizeAnimsMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_resizeAnimsMenuActionPerformed
        Scanner sc;
        int charId = this.manager.getCurrentCharacterId();
        Character c = this.manager.getCharacter();
        HashSet<Animation> processed = new HashSet<Animation>();
        int totalFrames = 0;
        for (int i = 0; i < c.getNumAnimations(); ++i) {
            Animation anim = c.getAnimation(i);
            if (processed.contains(anim)) continue;
            totalFrames += anim.getMaxNumFrames();
            processed.add(anim);
        }
        int returnVal = this.resizerChooser.showOpenDialog(this);
        if (returnVal != 0) {
            return;
        }
        File file = this.resizerChooser.getSelectedFile();
        try {
            sc = new Scanner(file);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.showError("Unable to open file " + file.getName());
            return;
        }
        ArrayList<Integer> sizes = new ArrayList<Integer>(c.getNumAnimations());
        ArrayList<Integer> wps = new ArrayList<Integer>(c.getNumAnimations());
        ArrayList<Boolean> hit = new ArrayList<Boolean>(c.getNumAnimations() - 24);
        int totalProvided = 0;
        int totalHits = 0;
        int totalWps = 0;
        while (sc.hasNext()) {
            try {
                int s = sc.nextInt();
                sizes.add(s);
                int hasWp = sc.nextInt();
                wps.add(hasWp);
                boolean hasHit = false;
                if (sizes.size() - 1 >= 24) {
                    if (hasWp < 2) {
                        hasHit = sc.nextInt() != 0;
                    }
                    hit.add(hasHit);
                }
                if (s > 0) {
                    totalProvided += s;
                    if (hasWp == 1) {
                        totalWps += s;
                    }
                    if (!hasHit) continue;
                    totalHits += s;
                    continue;
                }
                if (s >= 0) continue;
            }
            catch (Exception e) {
                this.showError("Invalid size values");
                e.printStackTrace();
                return;
            }
        }
        int numAnims = c.getNumAnimations();
        int numHits = c.getHitsSize();
        int numWeapons = c.getWeaponsSize();
        System.out.println("" + totalProvided + "-" + totalFrames + ", " + totalHits + "-" + numHits + ", " + totalWps + "-" + numWeapons);
        boolean hasGlobalColl = this.manager.hasGlobalCollision();
        boolean hasGlobalWeap = this.manager.hasGlobalWeapons();
        if (sizes.isEmpty()) {
            this.showError("Empty input");
            return;
        }
        if (sizes.size() != numAnims) {
            this.showError("Number of animations mismatch, \nGot " + sizes.size() + ", expected " + numAnims);
            return;
        }
        if (totalProvided > totalFrames && !hasGlobalColl && !hasGlobalWeap) {
            this.showError("Total number of frames exceeds the available space\nGot " + totalProvided + ", max allowed " + totalFrames);
            return;
        }
        if (totalHits > numHits && !hasGlobalColl) {
            this.showError("Total number of frames with collision exceeds the available space\nGot " + totalHits + ", max allowed " + numHits);
            return;
        }
        if (totalWps > numWeapons && !hasGlobalWeap) {
            this.showError("Total number of frames with weapons exceeds the available space\nGot " + totalWps + ", max allowed " + numWeapons);
            return;
        }
        if (this.timer.isRunning()) {
            this.playToggleActionPerformed(null);
        }
        this.resizeAnimations(sizes, wps, hit, totalProvided > totalFrames, totalHits > numHits, totalWps > numWeapons);
    }//GEN-LAST:event_resizeAnimsMenuActionPerformed

    private void characterComboKeyPressed(KeyEvent evt) {//GEN-FIRST:event_characterComboKeyPressed
        switch (evt.getKeyCode()) {
            case 81: {
                this.setPreviousAnimation();
                break;
            }
            case 65: {
                this.setNextAnimation();
            }
        }
    }//GEN-LAST:event_characterComboKeyPressed

    private void pasteMenu1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_pasteMenu1ActionPerformed
        JTextField field = new JTextField();
        field.setText("0");
        JTextField nameField = new JTextField();
        nameField.setText("decompressed.bin");
        JComponent[] inputs = new JComponent[]{new JLabel("Compressed art address:"), field, new JLabel("Output filename:"), nameField};
        int res = JOptionPane.showConfirmDialog(null, inputs, "Decompress SOR2 art", 2);
        if (res == 0) {
            long address = this.getHexFromField(field);
            if (address == Long.MIN_VALUE) {
                this.showError("Invalid address");
                return;
            }
            String fileName = nameField.getText();
            try {
                FileOutputStream fos = new FileOutputStream(fileName);
            }
            catch (Exception e) {
                this.showError("Unable to save to file \"" + fileName + "\"");
                return;
            }
            try {
                this.manager.decompressArt(fileName, address);
            }
            catch (IOException e) {
                e.printStackTrace();
                this.showError("Invalid data");
                return;
            }
            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_pasteMenu1ActionPerformed

    private void jMenuItem3ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        this.portRom();
        this.hardRefresh();
        JOptionPane.showMessageDialog(this, "Character sucessfully imported", "Done!", 1);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void hexIdsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hexIdsMenuActionPerformed
        this.setupCharacterCombo();
        this.setupAnimationCombo();
    }//GEN-LAST:event_hexIdsMenuActionPerformed

    
    private void speedMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedMenuActionPerformed
        int charId = manager.getCurrentCharacterId();
        int currSpeed = 0;
        try{
            currSpeed = manager.readSpeed();
        }catch(IOException e){
            showError("Unable to read character speed");
            return;
        }
        String charName = guide.getCharName(guide.getFakeCharId(charId));
        JTextField speedField = new JTextField();
        speedField.setText(currSpeed + "");
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Set " + charName + " walking speed"),
            speedField
        };
        int res = JOptionPane.showConfirmDialog(null, inputs, "Speed modifier", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION){
            int newSpeed = getIntFromField(speedField);
            try{
                manager.writeSpeed(newSpeed);
            }catch(IOException e){
                showError("Unable to write character speed");
                return;
            }
        }
    }//GEN-LAST:event_speedMenuActionPerformed
    
    
    private void portraitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portraitMenuActionPerformed
        int returnVal = imageChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = imageChooser.getSelectedFile();
            BufferedImage img;
            try {
                img = ImageIO.read(file);                
                img = processReplaceImg(img, false);                
            } catch (IOException ex) {
                showError("Unable to read image file: " + file.getName());
                return;
            }
            if (img.getWidth() < 16 || img.getHeight() < 16){
                showError("Image too small");
                return;
            }
            try {
                manager.writePortrait(img);
            } catch (IOException ex) {
                showError("Unable to save portrait");
            }
        }
    }//GEN-LAST:event_portraitMenuActionPerformed

    private void pasteMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenu2ActionPerformed
        String characterName = this.guide.getCharName(this.guide.getFakeCharId(this.manager.getCurrentCharacterId()));
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to DELETE the character " + characterName, "Delete " + characterName, 1);
        if (option == 0) {
            deleteCharacterArtAndMaps();
        }
    }//GEN-LAST:event_pasteMenu2ActionPerformed
    
    
    private void setSizeRadioMenusOff() {
        this.sizeRadioMenu1.setSelected(false);
        this.sizeRadioMenu2.setSelected(false);
        this.sizeRadioMenu3.setSelected(false);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            // empty catch block
        }
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                new Gui();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.guide == null || this.manager == null || this.currAnimation < 0) {
            return;
        }
        Character ch = this.manager.getCharacter();
        AnimFrame animFrame = ch.getAnimFrame(this.currAnimation, this.currFrame);
        if (this.frameDelayCount++ >= animFrame.delay) {
            this.frameDelayCount = 0;
            this.setNextFrame();
        }
    }

    @Override
    public void hitPositionChanged(int newX, int newY) {
        if (this.imagePanel.isFacedRight()) {
            newX *= -1;
        }
        if (newX < -127) {
            newX = -127;
        }
        if (newX > 127) {
            newX = 127;
        }
        if (newY < -127) {
            newY = -127;
        }
        if (newY > 127) {
            newY = 127;
        }
        this.setField(this.xField, newX);
        this.hitXChanged();
        this.setField(this.yField, newY);
        this.hitYChanged();
    }

    private void colorPanelPressed(int id) {
        JPanel panel = this.colorPanels[id];
        if (id == 0) {
            this.imagePanel.setColor(null);
        } else {
            this.imagePanel.setColor(panel.getBackground());
        }
        this.colorPanels[this.selectedColor].setBorder(null);
        this.selectedColor = id;
        this.colorPanels[this.selectedColor].setBorder(this.selectionBorder);
    }

    @Override
    public void artChanged() {
        this.manager.getCharacter().setModified(true);
        this.updateTitle();
    }

    private int findTransparency(BufferedImage img) {
        Integer count;
        int y;
        int val;
        int x;
        TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
        for (x = 0; x < img.getWidth(); ++x) {
            val = img.getRGB(x, 0);
            if ((val >> 24 & 255) == 0) {
                return val;
            }
            count = (Integer)map.get(val);
            if (count == null) {
                count = 0;
            }
            map.put(val, count + 1);
        }
        for (x = 0; x < img.getWidth(); ++x) {
            val = img.getRGB(x, img.getHeight() - 1);
            if ((val >> 24 & 255) == 0) {
                return val;
            }
            count = (Integer)map.get(val);
            if (count == null) {
                count = 0;
            }
            map.put(val, count + 1);
        }
        for (y = 0; y < img.getHeight(); ++y) {
            val = img.getRGB(0, y);
            if ((val >> 24 & 255) == 0) {
                return val;
            }
            count = (Integer)map.get(val);
            if (count == null) {
                count = 0;
            }
            map.put(val, count + 1);
        }
        for (y = 0; y < img.getHeight(); ++y) {
            val = img.getRGB(img.getWidth() - 1, y);
            if ((val >> 24 & 255) == 0) {
                return val;
            }
            count = (Integer)map.get(val);
            if (count == null) {
                count = 0;
            }
            map.put(val, count + 1);
        }
        int maxCount = 0;
        int moda = 0;
        for (Map.Entry entry : map.entrySet()) {
            if ((Integer)entry.getValue() <= maxCount) continue;
            moda = (Integer)entry.getKey();
            maxCount = (Integer)entry.getValue();
        }
        return moda;
    }

    private BufferedImage processReplaceImg(BufferedImage replaceImg) {
        return this.processReplaceImg(replaceImg, true);
    }

    private BufferedImage processReplaceImg(BufferedImage replaceImg, boolean transp) {
        Palette palette = this.manager.getPalette();
        return processReplaceImg(replaceImg, transp, palette);
    }
        
    private BufferedImage processReplaceImg(BufferedImage replaceImg, boolean transp, Palette palette) {    
        BufferedImage res = new BufferedImage(replaceImg.getWidth(), replaceImg.getHeight(), 2);
        int transparency = 0;
        if (transp) {
            transparency = this.findTransparency(replaceImg);
        }
        for (int x = 0; x < replaceImg.getWidth(); ++x) {
            for (int y = 0; y < replaceImg.getHeight(); ++y) {
                int val = replaceImg.getRGB(x, y);
                Color c = new Color(val);
                if (val == transparency || (val >> 24 & 255) == 0) continue;
                float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                c = this.nearestColor(hsb, palette);
                res.setRGB(x, y, c.getRGB());
            }
        }
        return res;
    }

    private Color nearestColor(float[] hsb, Palette palette) {
        float hx = 0.4f;
        float sx = 0.3f;
        float bx = 0.3f;
        float bestDist = Float.MAX_VALUE;
        int index = 0;
        for (int i = 15; i > 0; --i) {
            Color p = new Color(palette.getColor(i));
            float[] phsb = Color.RGBtoHSB(p.getRed(), p.getGreen(), p.getBlue(), null);
            float dist = (float)Math.sqrt(Math.pow(hsb[0] - phsb[0], 2.0) + Math.pow(hsb[1] - phsb[1], 2.0) + Math.pow(hsb[2] - phsb[2], 2.0));
            if (dist >= bestDist) continue;
            index = i;
            bestDist = dist;
        }
        return new Color(palette.getColor(index));
    }

    private boolean isTile(BufferedImage img, int xi, int yi) {
        xi *= 8;
        yi *= 8;
        for (int i = 0; i < 8; ++i) {
            int y;
            int x = xi + i;
            if (x >= img.getWidth()) continue;
            for (int j = 0; j < 8 && (y = yi + j) < img.getHeight(); ++j) {
                int val = img.getRGB(x, y);
                if ((val >> 24 & 255) == 0) continue;
                return true;
            }
        }
        return false;
    }

    private Piece optimizedPiece1(boolean[][] mx, boolean[][] done, int xi, int yi) {
        int j;
        int x;
        if (done[xi][yi]) {
            return null;
        }
        int sum = 0;
        int lx = 0;
        int ly = 0;
        int maxX = Math.min(4, 32 - xi);
        int maxY = Math.min(4, 32 - yi);
        block0 : for (int i = 0; i < maxX && mx[x = xi + i][0] && !done[x][0]; ++i) {
            for (j = 0; j < maxY; ++j) {
                int y = yi + j;
                if (!mx[x][y] || done[x][y]) {
                    maxY = j;
                    continue block0;
                }
                int locSum = i * j;
                if (locSum <= sum) continue;
                sum = locSum;
                lx = i;
                ly = j;
            }
        }
        Piece piece = new Piece(lx + 1, ly + 1);
        for (int i = 0; i < lx + 1; ++i) {
            for (j = 0; j < lx + 1; ++j) {
                done[xi + i][yi + j] = true;
                piece.setTile(i, j, new Tile());
            }
        }
        return piece;
    }

    private boolean check(boolean[][] mx, int x, int y, int width, int height) {
        if (x + width > 32) {
            return false;
        }
        if (y + height > 32) {
            return false;
        }
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (mx[x + i][y + j]) continue;
                return false;
            }
        }
        return true;
    }

    private boolean optimizePieces(boolean[][] mx, Piece[][] res) {
        int bestX = 0;
        int bestY = 0;
        int bestWidth = 0;
        int bestHeight = 0;
        int size = 0;
        for (int x = 0; x < 32; ++x) {
            for (int y = 0; y < 32; ++y) {
                if (!this.check(mx, x, y, 1, 1)) continue;
                if (this.check(mx, x, y, 4, 4)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 4;
                    bestHeight = 4;
                    size = bestWidth * bestHeight;
                    break;
                }
                if (size >= 12) break;
                if (this.check(mx, x, y, 4, 3)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 4;
                    bestHeight = 3;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (this.check(mx, x, y, 3, 4)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 3;
                    bestHeight = 4;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (size >= 9) break;
                if (this.check(mx, x, y, 3, 3)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 3;
                    bestHeight = 3;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (size >= 8) break;
                if (this.check(mx, x, y, 4, 2)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 4;
                    bestHeight = 2;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (this.check(mx, x, y, 2, 4)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 2;
                    bestHeight = 4;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (size >= 6) break;
                if (this.check(mx, x, y, 3, 2)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 3;
                    bestHeight = 2;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (this.check(mx, x, y, 2, 3)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 2;
                    bestHeight = 3;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (size >= 4) break;
                if (this.check(mx, x, y, 4, 1)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 4;
                    bestHeight = 1;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (this.check(mx, x, y, 1, 4)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 1;
                    bestHeight = 4;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (this.check(mx, x, y, 2, 2)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 2;
                    bestHeight = 2;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (size >= 3) break;
                if (this.check(mx, x, y, 3, 1)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 3;
                    bestHeight = 1;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (this.check(mx, x, y, 1, 3)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 1;
                    bestHeight = 3;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (size >= 2) break;
                if (this.check(mx, x, y, 2, 1)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 2;
                    bestHeight = 1;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (this.check(mx, x, y, 1, 2)) {
                    bestX = x;
                    bestY = y;
                    bestWidth = 1;
                    bestHeight = 2;
                    size = bestWidth * bestHeight;
                    continue;
                }
                if (size >= 1) break;
                bestX = x;
                bestY = y;
                bestWidth = 1;
                bestHeight = 1;
                size = bestWidth * bestHeight;
            }
            if (size == 16) break;
        }
        if (size == 0) {
            return false;
        }
        Piece piece = new Piece(bestWidth, bestHeight);
        for (int i = 0; i < bestWidth; ++i) {
            for (int j = 0; j < bestHeight; ++j) {
                piece.setTile(i, j, new Tile());
                mx[bestX + i][bestY + j] = false;
            }
        }
        res[bestX][bestY] = piece;
        return true;
    }

    private Piece[][] genPieces(boolean[][] mx) {
        Piece[][] res = new Piece[32][32];
        while (this.optimizePieces(mx, res)) {
        }
        return res;
    }

    private int trunkLeft(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); ++x) {
            for (int y = 0; y < img.getHeight(); ++y) {
                int rgbVal = img.getRGB(x, y);
                if ((rgbVal >> 24 & 255) == 0) continue;
                return x;
            }
        }
        return img.getWidth() - 1;
    }

    private int trunkRight(BufferedImage img) {
        for (int x = img.getWidth() - 1; x >= 0; --x) {
            for (int y = 0; y < img.getHeight(); ++y) {
                int rgbVal = img.getRGB(x, y);
                if ((rgbVal >> 24 & 255) == 0) continue;
                return x + 1;
            }
        }
        return 0;
    }

    private int trunkTop(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); ++y) {
            for (int x = 0; x < img.getWidth(); ++x) {
                int rgbVal = img.getRGB(x, y);
                if ((rgbVal >> 24 & 255) == 0) continue;
                return y;
            }
        }
        return 0;
    }

    private int trunkBottom(BufferedImage img) {
        for (int y = img.getHeight() - 1; y >= 0; --y) {
            for (int x = 0; x < img.getWidth(); ++x) {
                int rgbVal = img.getRGB(x, y);
                if ((rgbVal >> 24 & 255) == 0) continue;
                return y + 1;
            }
        }
        return 0;
    }
    
    private long getRomSize(){
        try {
            return manager.romSize();
        } catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Failed to identify rom size");
            return Long.MIN_VALUE;
        }
    }

    private boolean regenerateSprite(BufferedImage img, int animId, int frameId, int cx, int cy, boolean eraseOld) {
        int width = img.getWidth();
        int height = img.getHeight();
        AnimFrame frame = this.manager.getCharacter().getAnimFrame(animId, frameId);
        if (frame.type == 3) {
            return true;
        }        
        int paletteLine = this.getIntFromField(this.genPaletteField);
        if (paletteLine == Integer.MIN_VALUE || paletteLine < 0 || paletteLine > 3) {
            this.showError("Invalid palette line");
            return false;
        }
        
        // Free space from original sprite
        if (eraseOld){
            try {
                Sprite originalSprite = manager.readSprite(animId, frameId);
                FreeAddressesManager.freeChunk(frame.mapAddress, originalSprite.getMappingsSizeInBytes());            
                FreeAddressesManager.freeChunk(frame.artAddress, originalSprite.getArtSizeInBytes());
            } catch (IOException ex) {
                // No freed space? It's ok I guess?
                System.out.println("Failed to free some space in rom");
            }
        }
        Sprite sprite = new Sprite();
        int numTiles = 0;
        width = width % 8 == 0 ? (width /= 8) : width / 8 + 1;
        height = height % 8 == 0 ? (height /= 8) : height / 8 + 1;
        boolean[][] mx = new boolean[32][32];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (!this.isTile(img, x, y)) continue;
                mx[x][y] = true;
                ++numTiles;
            }
        }
        sprite.setNumTiles(numTiles);
        Piece[][] pieces = this.genPieces(mx);
        int nextIndex = 0;
        for (int x = 0; x < 32; ++x) {
            for (int y = 0; y < 32; ++y) {
                Piece p = pieces[x][y];
                if (pieces[x][y] == null) continue;
                SpritePiece sp = new SpritePiece();
                sp.width = p.getWidth() - 1;
                sp.height = p.getHeight() - 1;
                sp.paletteLine = paletteLine;
                sp.priorityFlag = false;
                sp.yFliped = false;
                sp.xFliped = false;
                sp.spriteIndex = nextIndex;
                sp.y = y * 8 - cy;
                sp.xL = x * 8 - cx;
                sp.xR = - sp.xL - p.getWidth() * 8;
                nextIndex += p.getWidth() * p.getHeight();
                sprite.addPiece(sp, p);
            }
        }
        // Find available space for new sprite
        long mapAddress = FreeAddressesManager.useBestSuitedAddress(sprite.getMappingsSizeInBytes(), getRomSize());
        try {
            manager.writeSpriteOnly(sprite, mapAddress);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to write sprite map");
            return false;
        }
        frame.mapAddress = mapAddress;  
        long artAddress = FreeAddressesManager.useBestSuitedAddress(sprite.getArtSizeInBytes(), getRomSize());
        try {
            manager.writeSpriteArtOnly(sprite, artAddress);
        }        
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to write sprite map");
            return false;
        }
        frame.artAddress = artAddress;
        BufferedImage extended = this.expandImage(img, cx, cy);
        Animation anim = this.manager.getCharacter().getAnimation(animId);
        anim.setImage(frameId, extended);
        try {
            this.manager.save();
        }
        catch (IOException e) {
            e.printStackTrace();
            this.showError("Unable to render the new sprite");
            return false;
        }
        
        return true;
    }

    @Override
    public void modeChanged(Mode mode) {
        this.setRadiosOff();
        switch (mode) {
            case pencil: {
                this.pencilRadio.setSelected(true);
                break;
            }
            case brush: {
                this.brushRadio.setSelected(true);
                break;
            }
            case bucket: {
                this.bucketRadio.setSelected(true);
                break;
            }
            case dragSprite: {
                this.dragSpriteRadio.setSelected(true);
                break;
            }
            case dragImage: {
                this.dragImageRadio.setSelected(true);
                break;
            }
            case none: {
                this.noneRadio.setSelected(true);
            }
        }
    }

    @Override
    public void spriteDragged(int deltaX, int deltaY) {
        if (this.imagePanel.isFacedRight()) {
            deltaX *= -1;
        }
        if (this.wasFrameReplaced) {
            this.lastcX -= deltaX;
            this.lastcY -= deltaY;
        }
        try {
            Sprite sprite = this.manager.readSprite(this.currAnimation, this.currFrame);
            AnimFrame frame = this.manager.getCharacter().getAnimFrame(this.currAnimation, this.currFrame);
            sprite.applyOffset(deltaX, deltaY);
            HitFrame hitFrame = this.manager.getCharacter().getHitFrame(this.currAnimation, this.currFrame);
            WeaponFrame weaponFrame = this.manager.getCharacter().getWeaponFrame(this.currAnimation, this.currFrame);
            if (hitFrame != null) {
                hitFrame.x -= deltaX / 2;
                hitFrame.y -= deltaY;
            }
            if (weaponFrame != null) {
                weaponFrame.x += deltaX;
                weaponFrame.y += deltaY;
            }
            this.manager.writeSpriteOnly(sprite, frame.mapAddress);
            this.manager.save();
            this.manager.bufferAnimFrame(this.currAnimation, this.currFrame);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to apply offset");
        }
        this.setFrame(this.currFrame);
    }

    private void refresh() {
        this.verifyModifications();
        this.setFrame(this.currFrame);
    }

    private void hardRefresh() {
        int frame = this.currFrame;
        try {
            this.setCharacter(this.manager.getCurrentCharacterId());
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Error loading character");
        }
        this.setFrame(frame);
    }

    @Override
    public void weaponPositionChanged(int newX, int newY) {
        if (this.imagePanel.isFacedRight()) {
            newX *= -1;
        }
        if (newX < -127) {
            newX = -127;
        }
        if (newX > 127) {
            newX = 127;
        }
        if (newY < -127) {
            newY = -127;
        }
        if (newY > 127) {
            newY = 127;
        }
        this.setField(this.wXField, newX);
        this.weaponXChanged();
        this.setField(this.wYField, newY);
        this.weaponYChanged();
    }

    private BufferedImage expandImage(BufferedImage img, int cx, int cy) {
        BufferedImage extended = new BufferedImage(256, 256, 2);
        for (int i = 0; i < img.getWidth(); ++i) {
            for (int j = 0; j < img.getHeight(); ++j) {
                int x = i + 128 - cx;
                int y = j + 128 - cy;
                int rgbVal = img.getRGB(i, j);
                if ((rgbVal >> 24 & 255) == 0 || x <= 0 || x >= 256 || y <= 0 || y >= 256) continue;
                extended.setRGB(x, y, rgbVal);
            }
        }
        return extended;
    }

    private void importSpriteReplacer(final BufferedImage sheet, final int columns, int rows, final int cx, final int cy) {
        final Character ch = this.manager.getCharacter();
        final int numAnims = ch.getNumAnimations();
        final ProgressMonitor progressMonitor = new ProgressMonitor(this, "Importing spritesheet", "", 0, numAnims);
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);
        final int frameWidth = sheet.getWidth() / columns;
        final int frameHeight = sheet.getHeight() / rows;
        final int maxId = columns * rows;
        new Thread(new Runnable(){

            private void finish() {
                ch.setModified(true);
                ch.setSpritesModified(true);
                try {
                    Gui.this.manager.save();
                    Gui.this.setCharacter(Gui.this.manager.getCurrentCharacterId());
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    Gui.this.showError("Unable to save the sprites");
                }
                progressMonitor.setProgress(999999);
                Gui.this.setEnabled(true);
                Gui.this.requestFocus();
            }

            @Override
            public void run() {
                Gui.this.setEnabled(false);
                int index = 0;
                TreeSet<Long> maps = new TreeSet<Long>();
                HashSet<Animation> processed = new HashSet<Animation>();
                for (int i = 0; i < numAnims; ++i) {
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)) {
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0; j < animSize; ++j) {
                            long mapAddress = ch.getAnimFrame((int)i, (int)j).mapAddress;
                            if (maps.contains(mapAddress)) continue;
                            maps.add(mapAddress);
                            int x = index % columns * frameWidth;
                            int y = index / columns * frameHeight;
                            BufferedImage img = sheet.getSubimage(x, y, frameWidth, frameHeight);
                            if (Gui.this.imagePanel.isFacedRight()) {
                                img = ImagePanel.flipImage(img);
                            }
                            img = Gui.this.expandImage(img, cx, cy);
                            anim.setImage(j, img);
                            anim.setSpritesModified(j, true);
                            if (++index != maxId) continue;
                            this.finish();
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }
                    }
                    progressMonitor.setNote("processing animations: " + (int)((double)i * 1.0 / (double)numAnims * 100.0) + "%");
                    progressMonitor.setProgress(i);
                    if (!progressMonitor.isCanceled()) continue;
                    this.finish();
                    return;
                }
                this.finish();
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

    private void portRom() {
        int returnVal = this.guideChooser.showOpenDialog(this);
        Guide guide = null;
        if (returnVal == 0) {
            File file = this.guideChooser.getSelectedFile();
            try {
                guide = new Guide(file.getAbsolutePath());
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                this.showError("Guide file '" + file.getName() + "' not found");
            }
            catch (Exception ex) {
                ex.printStackTrace();
                this.showError("Unable to open guide '" + file.getName() + "'");
            }
        }
        if (guide == null) {
            return;
        }
        String otherRomName = null;
        Manager otherManager = null;
        returnVal = this.romChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.romChooser.getSelectedFile();
            otherRomName = file.getAbsolutePath();
            try {
                otherManager = new Manager(otherRomName, guide);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                this.showError("File '" + otherRomName + "' not found");
            }
            catch (IOException ex) {
                ex.printStackTrace();
                this.showError("File '" + otherRomName + "' is not a valid Streets of Rage 2 ROM");
            }
        }
        if (otherManager == null) {
            return;
        }
        int charId = this.manager.getCurrentCharacterId();
        int fakeId = guide.getFakeCharId(charId);
        try {
            otherManager.setCharacter(charId, guide.getAnimsCount(fakeId), guide.getType(fakeId));
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to read character #" + charId);
        }
        this.deleteCharacterArtAndMaps();
        try {
            this.manager.replaceCharacterFromManager(otherManager);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to port character #" + charId);
        }
    }

    private void importSpriteGenerator(final BufferedImage sheet, final int columns, int rows, final int cx, final int cy) {
        final Character ch = this.manager.getCharacter();
        final int numAnims = ch.getNumAnimations();
        final ProgressMonitor progressMonitor = new ProgressMonitor(this, "Importing spritesheet", "", 0, numAnims);
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);
        final int frameWidth = sheet.getWidth() / columns;
        final int frameHeight = sheet.getHeight() / rows;
        final int maxId = columns * rows;
        new Thread(new Runnable(){

            private void finish() {
                ch.setModified(true);
                ch.setSpritesModified(true);
                Gui.this.hardRefresh();
                progressMonitor.setProgress(999999);
                Gui.this.setEnabled(true);
                Gui.this.requestFocus();
            }

            @Override
            public void run() {
                Gui.this.setEnabled(false);
                int index = 0;
                TreeMap<Long, AddressesPair> maps = new TreeMap<Long, AddressesPair>();
                TreeMap<Long, AddressesPair> arts = new TreeMap<Long, AddressesPair>();
                HashSet<Animation> processed = new HashSet<Animation>();
                for (int i = 0; i < numAnims; ++i) {
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)) {
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0; j < animSize; ++j) {
                            boolean eraseOld = true;
                            anim.setSpritesModified(j, true);
                            long mapAddress = anim.getFrame((int)j).mapAddress;
                            if (maps.containsKey(mapAddress)) {
                                AddressesPair p = (AddressesPair)maps.get(mapAddress);
                                anim.getFrame((int)j).mapAddress = p.a;
                                anim.getFrame((int)j).artAddress = p.b;
                                anim.setImage(j, p.img);
                                continue;
                            }
                            long artAddress = anim.getFrame((int)j).artAddress;
                            if (arts.containsKey(artAddress)){
                                AddressesPair p = (AddressesPair)arts.get(artAddress);
                                anim.getFrame((int)j).mapAddress = p.a;
                                anim.getFrame((int)j).artAddress = p.b;
                                anim.setImage(j, p.img);
                                // Same art with different sprite mappings, sounds really dangerous!
                                // Keep original art, and regenerate frame somewhere else
                                eraseOld = false;
                            }
                            
                            int x = index % columns * frameWidth;
                            int y = index / columns * frameHeight;
                            BufferedImage img = sheet.getSubimage(x, y, frameWidth, frameHeight);
                            if (Gui.this.imagePanel.isFacedRight()) {
                                img = ImagePanel.flipImage(img);
                            }
                            if (!regenerateSprite(img, i, j, cx, cy, eraseOld)) {
                                this.finish();
                                Gui.this.showError("Unable to replace sprites");
                                return;
                            }
                            maps.put(mapAddress, new AddressesPair(anim.getFrame((int)j).mapAddress, anim.getFrame((int)j).artAddress, anim.getImage(j)));
                            arts.put(artAddress, new AddressesPair(anim.getFrame((int)j).mapAddress, anim.getFrame((int)j).artAddress, anim.getImage(j)));
                            if (++index != maxId) continue;
                            this.finish();
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }
                    }
                    progressMonitor.setNote("processing animations: " + (int)((double)i * 1.0 / (double)numAnims * 100.0) + "%");
                    progressMonitor.setProgress(i);
                    if (!progressMonitor.isCanceled()) continue;
                    this.finish();
                    return;
                }
                this.finish();
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

    private void exportSpriteSheet() {
        this.exportSpriteSheet(false);
    }

    private void exportSpriteSheet(final boolean single) {
        final String charName = this.guide.getCharName(this.guide.getFakeCharId(this.manager.getCurrentCharacterId()));
        File tmpFile = null;
        if (!single) {
            this.imageSaver.setSelectedFile(new File(charName + ".png"));
            int returnOption = this.imageSaver.showSaveDialog(this);
            if (returnOption != 0) {
                return;
            }
            tmpFile = this.imageSaver.getSelectedFile();
            if (tmpFile == null) {
                return;
            }
        }
        final File outputFile = tmpFile;
        final Character ch = this.manager.getCharacter();
        final int numAnims = ch.getNumAnimations();
        final ProgressMonitor progressMonitor = new ProgressMonitor(this, single ? "Exporting sprites" : "Exporting spritesheet", "", 0, numAnims * 2);
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);
        new Thread(new Runnable(){

            @Override
            public void run() {
                Gui.this.setEnabled(false);
                int left = Integer.MAX_VALUE;
                int right = Integer.MIN_VALUE;
                int top = Integer.MAX_VALUE;
                int bottom = Integer.MIN_VALUE;
                TreeSet<Long> maps = new TreeSet<Long>();
                HashSet<Animation> processed = new HashSet<Animation>();
                for (int i = 0; i < ch.getNumAnimations(); ++i) {
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)) {
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0; j < animSize; ++j) {
                            Sprite sp;
                            maps.add(manager.getCharacter().getAnimFrame((int)i, (int)j).mapAddress);
                            try {
                                sp = Gui.this.manager.readSprite(i, j);
                            }
                            catch (IOException ex) {
                                ex.printStackTrace();
                                Gui.this.showError("Unable to access sprites");
                                progressMonitor.setProgress(999999);
                                Gui.this.setEnabled(true);
                                Gui.this.requestFocus();
                                return;
                            }
                            Rectangle rect = sp.getBounds();
                            if (rect.x < left) {
                                left = rect.x;
                            }
                            if (rect.y < top) {
                                top = rect.y;
                            }
                            int r = rect.x + rect.width;
                            int b = rect.y + rect.height;
                            if (r > right) {
                                right = r;
                            }
                            if (b <= bottom) continue;
                            bottom = b;
                        }
                    }
                    if (progressMonitor.isCanceled()) {
                        Gui.this.setEnabled(true);
                        Gui.this.requestFocus();
                        return;
                    }
                    progressMonitor.setNote("Computing space: " + (int)((float)i * 1.0f / (float)numAnims * 100.0f) + "%");
                    progressMonitor.setProgress(i);
                }
                int numMaps = maps.size() + 1;
                int width = right - left;
                int height = bottom - top;
                int columns = (int)Math.sqrt(numMaps);
                int rows = numMaps / columns;
                if (numMaps % columns != 0) {
                    ++rows;
                }
                BufferedImage res = null;
                Graphics2D g2d = null;
                if (!single) {
                    res = new BufferedImage(columns * width, rows * height, 2);
                    g2d = res.createGraphics();
                }
                maps.clear();
                processed.clear();
                int index = 0;
                for (int i = 0; i < ch.getNumAnimations(); ++i) {
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)) {
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0; j < animSize; ++j) {
                            long address = manager.getCharacter().getAnimFrame((int)i, (int)j).mapAddress;
                            try {
                                Gui.this.manager.bufferAnimation(i);
                            }
                            catch (IOException ex) {
                                ex.printStackTrace();
                                Gui.this.showError("Unable to access sprites");
                                progressMonitor.setProgress(999999);
                                Gui.this.setEnabled(true);
                                Gui.this.requestFocus();
                                return;
                            }
                            if (maps.contains(address)) continue;
                            maps.add(address);
                            BufferedImage img = Gui.this.manager.getImage(i, j).getSubimage(left, top, width, height);
                            if (single) {
                                try {
                                    File outputFile2 = new File(Gui.this.currentDirectory + "/" + charName + " " + i + "." + j + ".png");
                                    if (Gui.this.imagePanel.isFacedRight()) {
                                        img = ImagePanel.flipImage(img);
                                    }
                                    ImageIO.write((RenderedImage)img, "png", outputFile2);
                                }
                                catch (IOException ex) {
                                    ex.printStackTrace();
                                    Gui.this.showError("Unable to save image");
                                    progressMonitor.setProgress(999999);
                                    Gui.this.setEnabled(true);
                                    Gui.this.requestFocus();
                                    return;
                                }
                            } else {
                                int x = index % columns * width;
                                int y = index / columns * height;
                                if (Gui.this.imagePanel.isFacedRight()) {
                                    int w = img.getWidth();
                                    int h = img.getHeight();
                                    g2d.drawImage(img, x, y, x + w, y + h, w, 0, 0, h, null);
                                } else {
                                    g2d.drawImage(img, x, y, null);
                                }
                            }
                            ++index;
                        }
                    }
                    progressMonitor.setNote("Rendering sprites: " + (int)((float)i * 1.0f / (float)numAnims * 100.0f) + "%");
                    progressMonitor.setProgress(numAnims + i);
                    if (!progressMonitor.isCanceled()) continue;
                    Gui.this.setEnabled(true);
                    Gui.this.requestFocus();
                    return;
                }
                if (!single) {
                    int x = index % columns * width;
                    int y = index / columns * height;
                    String cs = (columns / 10 == 0 ? " " : "") + (columns / 100 == 0 ? " " : "");
                    String watermark0a = "Columns:  " + cs + columns;
                    cs = (rows / 10 == 0 ? " " : "") + (rows / 100 == 0 ? " " : "");
                    String watermark0b = "Rows:     " + cs + rows;
                    cs = ((128 - left) / 10 == 0 ? " " : "") + ((128 - left) / 100 == 0 ? " " : "");
                    String watermark0c = "Center X: " + cs + (128 - left);
                    cs = ((128 - top) / 10 == 0 ? " " : "") + ((128 - top) / 100 == 0 ? " " : "");
                    String watermark0d = "Center Y: " + cs + (128 - top);
                    String watermark1 = "Generated by";
                    String watermark2 = Gui.TITLE;
                    g2d.setColor(Color.red);
                    g2d.setFont(new Font("Courier New", 0, 12));
                    g2d.drawChars(watermark0a.toCharArray(), 0, watermark0a.length(), x + 4, y + 0);
                    g2d.drawChars(watermark0b.toCharArray(), 0, watermark0b.length(), x + 4, y + 10);
                    g2d.drawChars(watermark0c.toCharArray(), 0, watermark0c.length(), x + 4, y + 20);
                    g2d.drawChars(watermark0d.toCharArray(), 0, watermark0d.length(), x + 4, y + 30);
                    g2d.drawChars(watermark1.toCharArray(), 0, watermark1.length(), x + 4, y + 45);
                    g2d.drawChars(watermark2.toCharArray(), 0, watermark2.length(), x + 4, y + 55);
                    Palette pal = Gui.this.manager.getPalette();
                    int rc = 12;
                    for (int i = 0; i < 16; ++i) {
                        int c = pal.getColor(i);
                        g2d.setColor(new Color(c));
                        g2d.fillRect(x + 4 + i % 8 * rc, y + 60 + i / 8 * rc, rc, rc);
                    }
                    g2d.dispose();
                    try {
                        ImageIO.write((RenderedImage)res, "png", outputFile);
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                        Gui.this.showError("Unable to save spritesheet");
                        progressMonitor.setProgress(999999);
                        Gui.this.setEnabled(true);
                        Gui.this.requestFocus();
                        return;
                    }
                }
                progressMonitor.setProgress(999999);
                Gui.this.setEnabled(true);
                Gui.this.requestFocus();
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

    private void exportIndividualFrames() {
        this.exportSpriteSheet(true);
    }

    private void resizeAnimations(final ArrayList<Integer> sizes, final ArrayList<Integer> wps, final ArrayList<Boolean> hits, final boolean newArea, final boolean newHits, final boolean newWeapons) {
        final Character ch = this.manager.getCharacter();
        final int numAnims = sizes.size();
        final ProgressMonitor progressMonitor = new ProgressMonitor(this, "Generating resized animations", "", 0, numAnims + 1);
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);
        new Thread(new Runnable(){

            private void finish() {
                ch.setModified(true);
                Gui.this.hardRefresh();
                progressMonitor.setProgress(999999);
                Gui.this.setEnabled(true);
                Gui.this.requestFocus();
            }

            @Override
            public void run() {
                Gui.this.setEnabled(false);
                HashSet<Animation> processedAnims = new HashSet<Animation>();
                for (int i = 0; i < numAnims; ++i) {
                    int size = (Integer)sizes.get(i);
                    int wp = (Integer)wps.get(i);
                    boolean hit = false;
                    if (i >= 24) {
                        hit = (Boolean)hits.get(i - 24);
                    }
                    if (wp != 2) {
                        if (size < 0) {
                            ch.setAnim(i, - size - 1, wp == 1, hit);
                            processedAnims.add(ch.getAnimation(i));
                        } else if (size > 0) {
                            Animation anim = ch.getAnimation(i);
                            if (processedAnims.contains(anim)) {
                                ch.doubleAnim(i);
                            }
                            ch.resizeAnim(i, size, wp == 1, hit);
                        } else {
                            processedAnims.add(ch.getAnimation(i));
                        }
                    } else {
                        processedAnims.add(ch.getAnimation(i));
                    }
                    processedAnims.add(ch.getAnimation(i));
                    progressMonitor.setNote("Resizing animations: " + (int)((double)i * 1.0 / (double)numAnims * 100.0) + "%");
                    progressMonitor.setProgress(i);
                    if (!progressMonitor.isCanceled()) continue;
                    this.finish();
                    return;
                }
                progressMonitor.setNote("Generating new scripts");
                progressMonitor.setProgress(numAnims);
                long newAnimsAddress = 0L;
                if (newArea) {
                    // TODO: also re-use space here
                    newAnimsAddress = getRomSize();
                }
                try {
                    Gui.this.manager.writeNewScripts(newAnimsAddress, newHits, newWeapons);
                }
                catch (IOException e) {
                    this.finish();
                    e.printStackTrace();
                    Gui.this.showError("Failed to convert the animations script");
                    return;
                }
                Gui.this.hardRefresh();
                this.finish();
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }
    
    
    // Delete character!
    private void deleteCharacterArtAndMaps(){
        final Character ch = this.manager.getCharacter();
        final int numAnims = ch.getNumAnimations();            
        HashSet<Animation> processed = new HashSet<Animation>();
        for (int i = 0; i < numAnims; ++i) {
            Animation anim = ch.getAnimation(i);
            if (!processed.contains(anim)) {
                processed.add(anim);
                int animSize = anim.getNumFrames();
                for (int j = 0; j < animSize; ++j) {
                    AnimFrame frame = anim.getFrame(j);
                    Sprite sprite;
                    try {
                        sprite = manager.readSprite(i, j);
                    } catch (IOException ex) {
                        showError("Failled to free space for anim " + i + ", frame " + j);
                        return;
                    }
                    FreeAddressesManager.freeChunk(frame.mapAddress, sprite.getMappingsSizeInBytes());
                    FreeAddressesManager.freeChunk(frame.artAddress, sprite.getArtSizeInBytes());
                }
            }
        }
    }

    
    // Unused
    /*
    private void uncompressChar(final int type) {
        final Character ch = this.manager.getCharacter();
        final int numAnims = ch.getNumAnimations();
        final ProgressMonitor progressMonitor = new ProgressMonitor(this, "Uncompressing Character", "", 0, numAnims + 1);
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);
        new Thread(new Runnable(){

            private void finish() {
                ch.setModified(true);
                ch.setSpritesModified(true);
                Gui.this.hardRefresh();
                progressMonitor.setProgress(999999);
                Gui.this.setEnabled(true);
                Gui.this.requestFocus();
            }

            @Override
            public void run() {
                Gui.this.setEnabled(false);
                int index = 0;
                long newAnimsAddress = getRomSize();
                int animsSyzeInBytes = ch.getAnimsSize(type);
                TreeMap<Long, AddressesPair> maps = new TreeMap<Long, AddressesPair>();
                for (int i = 0; i < numAnims; ++i) {
                    Animation anim = ch.getAnimation(i);
                    try {
                        Gui.this.manager.bufferAnimation(i);
                    }
                    catch (IOException e) {
                        this.finish();
                        e.printStackTrace();
                        Gui.this.showError("Failed to buffer animations");
                        return;
                    }
                    int animSize = anim.getNumFrames();
                    for (int j = 0; j < animSize; ++j) {
                        anim.setSpritesModified(j, true);
                        ch.setModified(true);
                        ch.setSpritesModified(true);
                        long mapAddress = anim.getFrame((int)j).mapAddress;
                        if (maps.containsKey(mapAddress)) {
                            AddressesPair p = (AddressesPair)maps.get(mapAddress);
                            anim.getFrame((int)j).mapAddress = p.a;
                            anim.getFrame((int)j).artAddress = p.b;
                            anim.setImage(j, p.img);
                            anim.setAnimType(type);
                            continue;
                        }
                        BufferedImage img = anim.getImage(j);
                        anim.setAnimType(type);
                        long newAddress = Gui.this.regenerateSprite(img, i, j, 128, 128);
                        if (newAddress == Long.MIN_VALUE) {
                            this.finish();
                            Gui.this.showError("Unable to replace the compressed sprites");
                            return;
                        }
                        maps.put(mapAddress, new AddressesPair(anim.getFrame((int)j).mapAddress, anim.getFrame((int)j).artAddress, anim.getImage(j)));
                        ++index;
                    }
                    progressMonitor.setNote("Converting sprites: " + (int)((double)i * 1.0 / (double)numAnims * 100.0) + "%");
                    progressMonitor.setProgress(i);
                    if (!progressMonitor.isCanceled()) continue;
                    this.finish();
                    return;
                }
                progressMonitor.setNote("Converting animations script");
                progressMonitor.setProgress(numAnims);
                try {
                    Gui.this.manager.writeNewAnimations(newAnimsAddress);
                }
                catch (IOException e) {
                    this.finish();
                    e.printStackTrace();
                    Gui.this.showError("Failed to convert the animations script");
                    return;
                }
                Gui.this.guide.setAnimType(Gui.this.guide.getFakeCharId(Gui.this.manager.getCurrentCharacterId()), type);
                Gui.this.hardRefresh();
                this.finish();
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }
*/

    class AddressesPair {
        public BufferedImage img;
        public long a;
        public long b;

        public AddressesPair(long a, long b, BufferedImage img) {
            this.a = a;
            this.b = b;
            this.img = img;
        }
    }

}
