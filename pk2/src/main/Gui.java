/*
 * Decompiled with CFR 0_132.
 */
package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
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
    private static final String VERSION = " v1.6b";
    private static final String TITLE = "Pancake 2 v1.6b";
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
    private JTextField angleField;
    private JComboBox animationCombo;
    private JPanel animationPanel;
    private JTextField artField;
    private JButton backBut;
    private JCheckBox behindCheck;
    private JRadioButtonMenuItem brushMenu;
    private JRadioButton brushRadio;
    private JRadioButtonMenuItem bucketMenu;
    private JRadioButton bucketRadio;
    private JComboBox characterCombo;
    private JPanel characterPanel;
    private JPanel characterPanel1;
    private JMenuItem closeMenu;
    private JPanel colorPanel1;
    private JPanel colorPanel10;
    private JPanel colorPanel11;
    private JPanel colorPanel12;
    private JPanel colorPanel13;
    private JPanel colorPanel14;
    private JPanel colorPanel15;
    private JPanel colorPanel16;
    private JPanel colorPanel2;
    private JPanel colorPanel3;
    private JPanel colorPanel4;
    private JPanel colorPanel5;
    private JPanel colorPanel6;
    private JPanel colorPanel7;
    private JPanel colorPanel8;
    private JPanel colorPanel9;
    private JPanel colorsPanel1;
    private JLabel compressedLabel;
    private JMenuItem copyMenu;
    private JTextField damageField;
    private JTextField delayField;
    private JRadioButtonMenuItem dragImageMenu;
    private JRadioButton dragImageRadio;
    private JRadioButtonMenuItem dragSpriteMenu;
    private JRadioButton dragSpriteRadio;
    private JMenu exportMenu;
    private JPanel framePanel;
    private JButton frontBut;
    private JTextField genAddressField;
    private JTextField genPaletteField;
    private JPanel generatePanel;
    private JButton hardReplaceButton;
    private JRadioButtonMenuItem hexIdsMenu;
    private JCheckBox hitCheck;
    private JPanel hitPanel;
    private JMenu inportMenu;
    private JButton jButton2;
    private JButton jButton3;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JMenu jMenu1;
    private JMenu jMenu2;
    private JMenu jMenu3;
    private JMenu jMenu4;
    private JMenu jMenu5;
    private JMenu jMenu6;
    private JMenuBar jMenuBar1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem10;
    private JMenuItem jMenuItem11;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem3;
    private JMenuItem jMenuItem4;
    private JMenuItem jMenuItem5;
    private JMenuItem jMenuItem6;
    private JMenuItem jMenuItem7;
    private JMenuItem jMenuItem9;
    private JPopupMenu.Separator jSeparator1;
    private JPopupMenu.Separator jSeparator2;
    private JPopupMenu.Separator jSeparator3;
    private JPopupMenu.Separator jSeparator4;
    private JPopupMenu.Separator jSeparator5;
    private JPopupMenu.Separator jSeparator6;
    private JCheckBox koCheck;
    private JPanel mainPanel;
    private JTextField mapField;
    private JMenuItem nameMenu;
    private JButton nextBut;
    private JRadioButtonMenuItem noneMenu;
    private JRadioButton noneRadio;
    private JMenuItem openRomMenu;
    private JPanel overridePanel;
    private JMenuItem pasteMenu;
    private JMenuItem pasteMenu1;
    private JRadioButtonMenuItem pencilMenu;
    private JRadioButton pencilRadio;
    private JToggleButton playToggle;
    private JPanel playerPanel;
    private JPanel previewPanel;
    private JButton previousBut;
    private JMenuItem resizeAnimsMenu;
    private JScrollPane scrollPanel;
    private JCheckBox showCenterCheck;
    private JCheckBox showFacedRightCheck;
    private JCheckBox showHitsCheck;
    private JCheckBox showTileCheck;
    private JCheckBox showWeaponCheck;
    private JTextField sizeField;
    private JRadioButtonMenuItem sizeRadioMenu1;
    private JRadioButtonMenuItem sizeRadioMenu2;
    private JRadioButtonMenuItem sizeRadioMenu3;
    private JButton softReplaceButton;
    private JTextField soundField;
    private JMenuItem spriteSheetMenu;
    private JMenuItem spriteSheetMenu1;
    private JPanel toolsPanel;
    private JTextField wXField;
    private JTextField wYField;
    private JCheckBox weaponCheck;
    private JComboBox weaponCombo;
    private JPanel weaponPanel;
    private JTextField xField;
    private JTextField yField;

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
            this.setTitle("Pancake 2 v1.6b - " + new File(this.romName).getName() + "*");
        } else {
            this.setTitle("Pancake 2 v1.6b - " + new File(this.romName).getName());
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
        this.updateGenAddress();
        this.nameMenu.setEnabled(charId < this.guide.getPlayableChars());
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
            this.resizeAnimsMenu.setEnabled(false);
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
        this.pasteMenu1.setEnabled(this.manager != null);
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
        this.timer = new Timer(1, this);
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

    private void initComponents() {
        this.mainPanel = new JPanel();
        this.characterPanel = new JPanel();
        this.jLabel1 = new JLabel();
        this.characterCombo = new JComboBox();
        this.animationPanel = new JPanel();
        this.jLabel5 = new JLabel();
        this.animationCombo = new JComboBox();
        this.jLabel6 = new JLabel();
        this.sizeField = new JTextField();
        this.framePanel = new JPanel();
        this.jLabel7 = new JLabel();
        this.delayField = new JTextField();
        this.hitPanel = new JPanel();
        this.jLabel8 = new JLabel();
        this.xField = new JTextField();
        this.jLabel9 = new JLabel();
        this.yField = new JTextField();
        this.jLabel10 = new JLabel();
        this.soundField = new JTextField();
        this.jLabel11 = new JLabel();
        this.damageField = new JTextField();
        this.koCheck = new JCheckBox();
        this.hitCheck = new JCheckBox();
        this.jLabel12 = new JLabel();
        this.mapField = new JTextField();
        this.jLabel3 = new JLabel();
        this.artField = new JTextField();
        this.jButton2 = new JButton();
        this.jButton3 = new JButton();
        this.weaponPanel = new JPanel();
        this.jLabel15 = new JLabel();
        this.wXField = new JTextField();
        this.jLabel16 = new JLabel();
        this.wYField = new JTextField();
        this.jLabel17 = new JLabel();
        this.angleField = new JTextField();
        this.behindCheck = new JCheckBox();
        this.weaponCheck = new JCheckBox();
        this.compressedLabel = new JLabel();
        this.previewPanel = new JPanel();
        this.scrollPanel = new JScrollPane(this.imagePanel);
        this.playerPanel = new JPanel();
        this.backBut = new JButton();
        this.previousBut = new JButton();
        this.nextBut = new JButton();
        this.frontBut = new JButton();
        this.playToggle = new JToggleButton();
        this.colorsPanel1 = new JPanel();
        this.colorPanel2 = new JPanel();
        this.colorPanel3 = new JPanel();
        this.colorPanel4 = new JPanel();
        this.colorPanel5 = new JPanel();
        this.colorPanel6 = new JPanel();
        this.colorPanel7 = new JPanel();
        this.colorPanel8 = new JPanel();
        this.colorPanel9 = new JPanel();
        this.colorPanel10 = new JPanel();
        this.colorPanel11 = new JPanel();
        this.colorPanel12 = new JPanel();
        this.colorPanel13 = new JPanel();
        this.colorPanel14 = new JPanel();
        this.colorPanel15 = new JPanel();
        this.colorPanel16 = new JPanel();
        this.colorPanel1 = new JPanel();
        this.characterPanel1 = new JPanel();
        this.showHitsCheck = new JCheckBox();
        this.showWeaponCheck = new JCheckBox();
        this.weaponCombo = new JComboBox();
        this.showFacedRightCheck = new JCheckBox();
        this.showTileCheck = new JCheckBox();
        this.showCenterCheck = new JCheckBox();
        this.toolsPanel = new JPanel();
        this.pencilRadio = new JRadioButton();
        this.brushRadio = new JRadioButton();
        this.bucketRadio = new JRadioButton();
        this.dragSpriteRadio = new JRadioButton();
        this.dragImageRadio = new JRadioButton();
        this.noneRadio = new JRadioButton();
        this.overridePanel = new JPanel();
        this.softReplaceButton = new JButton();
        this.jLabel2 = new JLabel();
        this.generatePanel = new JPanel();
        this.hardReplaceButton = new JButton();
        this.jLabel13 = new JLabel();
        this.genAddressField = new JTextField();
        this.jLabel14 = new JLabel();
        this.genPaletteField = new JTextField();
        this.jMenuBar1 = new JMenuBar();
        this.jMenu1 = new JMenu();
        this.openRomMenu = new JMenuItem();
        this.jMenuItem4 = new JMenuItem();
        this.closeMenu = new JMenuItem();
        this.jSeparator2 = new JPopupMenu.Separator();
        this.inportMenu = new JMenu();
        this.jMenuItem1 = new JMenuItem();
        this.jMenuItem2 = new JMenuItem();
        this.jSeparator6 = new JPopupMenu.Separator();
        this.jMenuItem3 = new JMenuItem();
        this.exportMenu = new JMenu();
        this.spriteSheetMenu1 = new JMenuItem();
        this.spriteSheetMenu = new JMenuItem();
        this.jSeparator1 = new JPopupMenu.Separator();
        this.jMenuItem5 = new JMenuItem();
        this.jMenu2 = new JMenu();
        this.jMenu6 = new JMenu();
        this.pencilMenu = new JRadioButtonMenuItem();
        this.brushMenu = new JRadioButtonMenuItem();
        this.bucketMenu = new JRadioButtonMenuItem();
        this.dragSpriteMenu = new JRadioButtonMenuItem();
        this.dragImageMenu = new JRadioButtonMenuItem();
        this.noneMenu = new JRadioButtonMenuItem();
        this.jMenu5 = new JMenu();
        this.sizeRadioMenu1 = new JRadioButtonMenuItem();
        this.sizeRadioMenu2 = new JRadioButtonMenuItem();
        this.sizeRadioMenu3 = new JRadioButtonMenuItem();
        this.jMenu4 = new JMenu();
        this.jMenuItem11 = new JMenuItem();
        this.jMenuItem7 = new JMenuItem();
        this.jMenuItem9 = new JMenuItem();
        this.jMenuItem10 = new JMenuItem();
        this.hexIdsMenu = new JRadioButtonMenuItem();
        this.jSeparator3 = new JPopupMenu.Separator();
        this.resizeAnimsMenu = new JMenuItem();
        this.nameMenu = new JMenuItem();
        this.jSeparator4 = new JPopupMenu.Separator();
        this.copyMenu = new JMenuItem();
        this.pasteMenu = new JMenuItem();
        this.jSeparator5 = new JPopupMenu.Separator();
        this.pasteMenu1 = new JMenuItem();
        this.jMenu3 = new JMenu();
        this.jMenuItem6 = new JMenuItem();
        this.setDefaultCloseOperation(0);
        this.setTitle(TITLE);
        this.setLocationByPlatform(true);
        this.addMouseWheelListener(new MouseWheelListener(){

            @Override
            public void mouseWheelMoved(MouseWheelEvent evt) {
                Gui.this.formMouseWheelMoved(evt);
            }
        });
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent evt) {
                Gui.this.formWindowClosing(evt);
            }
        });
        this.characterPanel.setBackground(new Color(229, 235, 235));
        this.characterPanel.setBorder(BorderFactory.createTitledBorder(""));
        this.jLabel1.setFont(new Font("Tahoma", 1, 14));
        this.jLabel1.setHorizontalAlignment(4);
        this.jLabel1.setText("Character:");
        this.characterCombo.setFont(new Font("Tahoma", 0, 14));
        this.characterCombo.setMaximumRowCount(16);
        this.characterCombo.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.characterComboActionPerformed(evt);
            }
        });
        this.characterCombo.addKeyListener(new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent evt) {
                Gui.this.characterComboKeyPressed(evt);
            }
        });
        this.animationPanel.setBackground(new Color(241, 241, 171));
        this.animationPanel.setBorder(BorderFactory.createTitledBorder(""));
        this.jLabel5.setFont(new Font("Tahoma", 1, 12));
        this.jLabel5.setHorizontalAlignment(4);
        this.jLabel5.setText("Animation:");
        this.animationCombo.setFont(new Font("Tahoma", 0, 14));
        this.animationCombo.setMaximumRowCount(16);
        this.animationCombo.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.animationComboActionPerformed(evt);
            }
        });
        this.animationCombo.addKeyListener(new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent evt) {
                Gui.this.animationComboKeyPressed(evt);
            }
        });
        this.jLabel6.setFont(new Font("Tahoma", 0, 14));
        this.jLabel6.setHorizontalAlignment(4);
        this.jLabel6.setText("Size:");
        this.sizeField.setEditable(false);
        this.sizeField.setFont(new Font("Courier New", 0, 14));
        this.sizeField.setHorizontalAlignment(4);
        this.sizeField.setText("0");
        this.framePanel.setBackground(new Color(240, 226, 157));
        this.framePanel.setBorder(BorderFactory.createTitledBorder(null, "Frame #", 0, 0, new Font("Tahoma", 0, 14), new Color(0, 0, 0)));
        this.jLabel7.setFont(new Font("Tahoma", 0, 14));
        this.jLabel7.setHorizontalAlignment(4);
        this.jLabel7.setText("Delay:");
        this.delayField.setFont(new Font("Courier New", 0, 14));
        this.delayField.setHorizontalAlignment(4);
        this.delayField.setText("0");
        this.hitPanel.setBackground(new Color(236, 209, 127));
        this.hitPanel.setBorder(BorderFactory.createTitledBorder(null, "Hit", 0, 0, new Font("Tahoma", 0, 11), new Color(0, 0, 0)));
        this.jLabel8.setFont(new Font("Tahoma", 0, 14));
        this.jLabel8.setHorizontalAlignment(4);
        this.jLabel8.setText("x:");
        this.xField.setFont(new Font("Courier New", 0, 14));
        this.xField.setHorizontalAlignment(4);
        this.xField.setText("0");
        this.jLabel9.setFont(new Font("Tahoma", 0, 14));
        this.jLabel9.setHorizontalAlignment(4);
        this.jLabel9.setText("y:");
        this.yField.setFont(new Font("Courier New", 0, 14));
        this.yField.setHorizontalAlignment(4);
        this.yField.setText("0");
        this.jLabel10.setFont(new Font("Tahoma", 0, 14));
        this.jLabel10.setHorizontalAlignment(4);
        this.jLabel10.setText("sound:");
        this.soundField.setFont(new Font("Courier New", 0, 14));
        this.soundField.setHorizontalAlignment(4);
        this.soundField.setText("0");
        this.jLabel11.setFont(new Font("Tahoma", 0, 14));
        this.jLabel11.setHorizontalAlignment(4);
        this.jLabel11.setText("damage:");
        this.damageField.setFont(new Font("Courier New", 0, 14));
        this.damageField.setHorizontalAlignment(4);
        this.damageField.setText("0");
        this.koCheck.setBackground(new Color(236, 209, 127));
        this.koCheck.setFont(new Font("Tahoma", 0, 14));
        this.koCheck.setText("Knock Down");
        this.koCheck.setContentAreaFilled(false);
        this.koCheck.setFocusable(false);
        this.koCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.koCheckActionPerformed(evt);
            }
        });
        GroupLayout hitPanelLayout = new GroupLayout(this.hitPanel);
        this.hitPanel.setLayout(hitPanelLayout);
        hitPanelLayout.setHorizontalGroup(hitPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(hitPanelLayout.createSequentialGroup().addGroup(hitPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(hitPanelLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.jLabel8, -2, 22, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.xField, -2, 33, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel9, -2, 22, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.yField, -2, 33, -2)).addGroup(GroupLayout.Alignment.TRAILING, hitPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.jLabel11, -2, 63, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.damageField, -2, 33, -2))).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(hitPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.koCheck).addGroup(hitPanelLayout.createSequentialGroup().addComponent(this.jLabel10, -2, 57, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.soundField, -2, 33, -2))).addContainerGap(-1, 32767)));
        hitPanelLayout.setVerticalGroup(hitPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(hitPanelLayout.createSequentialGroup().addGroup(hitPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel8).addComponent(this.xField, -2, -1, -2).addComponent(this.jLabel9).addComponent(this.yField, -2, -1, -2).addComponent(this.jLabel10).addComponent(this.soundField, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(hitPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel11).addComponent(this.damageField, -2, -1, -2).addComponent(this.koCheck)).addGap(0, 9, 32767)));
        this.hitCheck.setBackground(new Color(240, 226, 157));
        this.hitCheck.setFont(new Font("Tahoma", 0, 14));
        this.hitCheck.setText("Hit frame");
        this.hitCheck.setContentAreaFilled(false);
        this.hitCheck.setFocusable(false);
        this.hitCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.hitCheckActionPerformed(evt);
            }
        });
        this.jLabel12.setFont(new Font("Tahoma", 0, 14));
        this.jLabel12.setHorizontalAlignment(4);
        this.jLabel12.setText("SpriteMap:");
        this.mapField.setFont(new Font("Courier New", 0, 14));
        this.mapField.setHorizontalAlignment(4);
        this.mapField.setText("200");
        this.jLabel3.setFont(new Font("Tahoma", 0, 14));
        this.jLabel3.setHorizontalAlignment(4);
        this.jLabel3.setText("Art Address:");
        this.artField.setFont(new Font("Courier New", 0, 14));
        this.artField.setHorizontalAlignment(4);
        this.artField.setText("200");
        this.jButton2.setText("<");
        this.jButton2.setFocusable(false);
        this.jButton2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jButton2ActionPerformed(evt);
            }
        });
        this.jButton3.setText(">");
        this.jButton3.setFocusable(false);
        this.jButton3.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jButton3ActionPerformed(evt);
            }
        });
        this.weaponPanel.setBackground(new Color(217, 227, 154));
        this.weaponPanel.setBorder(BorderFactory.createTitledBorder(null, "Weapon Point", 0, 0, new Font("Tahoma", 0, 11), new Color(0, 0, 0)));
        this.jLabel15.setFont(new Font("Tahoma", 0, 14));
        this.jLabel15.setHorizontalAlignment(4);
        this.jLabel15.setText("x:");
        this.wXField.setFont(new Font("Courier New", 0, 14));
        this.wXField.setHorizontalAlignment(4);
        this.wXField.setText("0");
        this.jLabel16.setFont(new Font("Tahoma", 0, 14));
        this.jLabel16.setHorizontalAlignment(4);
        this.jLabel16.setText("y:");
        this.wYField.setFont(new Font("Courier New", 0, 14));
        this.wYField.setHorizontalAlignment(4);
        this.wYField.setText("0");
        this.jLabel17.setFont(new Font("Tahoma", 0, 14));
        this.jLabel17.setHorizontalAlignment(4);
        this.jLabel17.setText("rotation");
        this.angleField.setFont(new Font("Courier New", 0, 14));
        this.angleField.setHorizontalAlignment(4);
        this.angleField.setText("0");
        this.behindCheck.setFont(new Font("Tahoma", 0, 14));
        this.behindCheck.setText("Show behind");
        this.behindCheck.setContentAreaFilled(false);
        this.behindCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.behindCheckActionPerformed(evt);
            }
        });
        GroupLayout weaponPanelLayout = new GroupLayout(this.weaponPanel);
        this.weaponPanel.setLayout(weaponPanelLayout);
        weaponPanelLayout.setHorizontalGroup(weaponPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(weaponPanelLayout.createSequentialGroup().addComponent(this.jLabel15, -2, 22, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.wXField, -2, 33, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel16, -2, 22, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.wYField, -2, 33, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel17, -2, 51, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.angleField, -2, 19, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.behindCheck)));
        weaponPanelLayout.setVerticalGroup(weaponPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(weaponPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel15).addComponent(this.wXField, -2, -1, -2).addComponent(this.jLabel16).addComponent(this.wYField, -2, -1, -2).addComponent(this.jLabel17).addComponent(this.angleField, -2, -1, -2).addComponent(this.behindCheck)));
        this.weaponCheck.setBackground(new Color(240, 226, 157));
        this.weaponCheck.setFont(new Font("Tahoma", 0, 14));
        this.weaponCheck.setText("Weapon Point");
        this.weaponCheck.setContentAreaFilled(false);
        this.weaponCheck.setFocusable(false);
        this.weaponCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.weaponCheckActionPerformed(evt);
            }
        });
        GroupLayout framePanelLayout = new GroupLayout(this.framePanel);
        this.framePanel.setLayout(framePanelLayout);
        framePanelLayout.setHorizontalGroup(framePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(framePanelLayout.createSequentialGroup().addComponent(this.jLabel12, -2, 79, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.mapField, -2, 62, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jLabel3, -2, 79, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.artField, -2, 62, -2).addContainerGap()).addGroup(framePanelLayout.createSequentialGroup().addGroup(framePanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(framePanelLayout.createSequentialGroup().addComponent(this.jLabel7).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.delayField, -2, 33, -2)).addComponent(this.hitCheck)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.jButton2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton3)).addGroup(framePanelLayout.createSequentialGroup().addComponent(this.weaponCheck).addGap(0, 0, 32767)).addComponent(this.hitPanel, -1, -1, 32767).addComponent(this.weaponPanel, -1, -1, 32767));
        framePanelLayout.setVerticalGroup(framePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(framePanelLayout.createSequentialGroup().addGroup(framePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(framePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButton2).addComponent(this.jButton3)).addGroup(framePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel7).addComponent(this.delayField, -2, -1, -2))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.hitCheck).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.hitPanel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 8, 32767).addComponent(this.weaponCheck).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.weaponPanel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(framePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel12).addComponent(this.mapField, -2, -1, -2).addComponent(this.jLabel3).addComponent(this.artField, -2, -1, -2))));
        GroupLayout animationPanelLayout = new GroupLayout(this.animationPanel);
        this.animationPanel.setLayout(animationPanelLayout);
        animationPanelLayout.setHorizontalGroup(animationPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(animationPanelLayout.createSequentialGroup().addComponent(this.jLabel5).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.animationCombo, -2, 202, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel6).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.sizeField, -2, 33, -2).addGap(6, 6, 6)).addComponent(this.framePanel, -1, -1, 32767));
        animationPanelLayout.setVerticalGroup(animationPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(animationPanelLayout.createSequentialGroup().addGroup(animationPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel5).addComponent(this.animationCombo, -2, -1, -2).addComponent(this.sizeField, -2, -1, -2).addComponent(this.jLabel6)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.framePanel, -1, -1, 32767)));
        this.compressedLabel.setFont(new Font("Tahoma", 1, 12));
        this.compressedLabel.setForeground(new Color(199, 18, 18));
        this.compressedLabel.setText("Compressed!");
        GroupLayout characterPanelLayout = new GroupLayout(this.characterPanel);
        this.characterPanel.setLayout(characterPanelLayout);
        characterPanelLayout.setHorizontalGroup(characterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(characterPanelLayout.createSequentialGroup().addGroup(characterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(characterPanelLayout.createSequentialGroup().addComponent(this.jLabel1, -2, 73, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.characterCombo, -2, 205, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.compressedLabel)).addComponent(this.animationPanel, -2, -1, -2)).addContainerGap(-1, 32767)));
        characterPanelLayout.setVerticalGroup(characterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(characterPanelLayout.createSequentialGroup().addGroup(characterPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.characterCombo, -2, -1, -2).addComponent(this.compressedLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.animationPanel, -1, -1, 32767)));
        this.scrollPanel.setBackground(new Color(0, 0, 0));
        this.scrollPanel.setFocusable(false);
        this.backBut.setBackground(new Color(241, 241, 171));
        this.backBut.setText("|<<");
        this.backBut.setFocusable(false);
        this.backBut.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.backButActionPerformed(evt);
            }
        });
        this.previousBut.setBackground(new Color(240, 226, 157));
        this.previousBut.setText("<<");
        this.previousBut.setFocusable(false);
        this.previousBut.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.previousButActionPerformed(evt);
            }
        });
        this.nextBut.setBackground(new Color(240, 226, 157));
        this.nextBut.setText(">>");
        this.nextBut.setFocusable(false);
        this.nextBut.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.nextButActionPerformed(evt);
            }
        });
        this.frontBut.setBackground(new Color(241, 241, 171));
        this.frontBut.setText(">>|");
        this.frontBut.setFocusable(false);
        this.frontBut.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.frontButActionPerformed(evt);
            }
        });
        this.playToggle.setBackground(new Color(236, 209, 127));
        this.playToggle.setText(">");
        this.playToggle.setFocusable(false);
        this.playToggle.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.playToggleActionPerformed(evt);
            }
        });
        GroupLayout playerPanelLayout = new GroupLayout(this.playerPanel);
        this.playerPanel.setLayout(playerPanelLayout);
        playerPanelLayout.setHorizontalGroup(playerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, playerPanelLayout.createSequentialGroup().addComponent(this.backBut, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.previousBut, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.playToggle, -2, 105, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.nextBut, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.frontBut, -1, -1, 32767)));
        playerPanelLayout.setVerticalGroup(playerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(playerPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.backBut).addComponent(this.previousBut).addComponent(this.nextBut).addComponent(this.frontBut).addComponent(this.playToggle)));
        this.colorsPanel1.setBorder(BorderFactory.createTitledBorder(null, "Palette", 0, 0, new Font("Tahoma", 0, 11), new Color(0, 0, 0)));
        this.colorsPanel1.setPreferredSize(new Dimension(256, 64));
        this.colorPanel2.setBackground(new Color(255, 255, 255));
        this.colorPanel2.setCursor(new Cursor(0));
        this.colorPanel2.setPreferredSize(new Dimension(32, 32));
        this.colorPanel2.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel2MousePressed(evt);
            }
        });
        GroupLayout colorPanel2Layout = new GroupLayout(this.colorPanel2);
        this.colorPanel2.setLayout(colorPanel2Layout);
        colorPanel2Layout.setHorizontalGroup(colorPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel2Layout.setVerticalGroup(colorPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel3.setBackground(new Color(255, 255, 255));
        this.colorPanel3.setCursor(new Cursor(0));
        this.colorPanel3.setPreferredSize(new Dimension(32, 32));
        this.colorPanel3.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel3MousePressed(evt);
            }
        });
        GroupLayout colorPanel3Layout = new GroupLayout(this.colorPanel3);
        this.colorPanel3.setLayout(colorPanel3Layout);
        colorPanel3Layout.setHorizontalGroup(colorPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel3Layout.setVerticalGroup(colorPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel4.setBackground(new Color(255, 255, 255));
        this.colorPanel4.setCursor(new Cursor(0));
        this.colorPanel4.setPreferredSize(new Dimension(32, 32));
        this.colorPanel4.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel4MousePressed(evt);
            }
        });
        GroupLayout colorPanel4Layout = new GroupLayout(this.colorPanel4);
        this.colorPanel4.setLayout(colorPanel4Layout);
        colorPanel4Layout.setHorizontalGroup(colorPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel4Layout.setVerticalGroup(colorPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel5.setBackground(new Color(255, 255, 255));
        this.colorPanel5.setCursor(new Cursor(0));
        this.colorPanel5.setPreferredSize(new Dimension(32, 32));
        this.colorPanel5.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel5MousePressed(evt);
            }
        });
        GroupLayout colorPanel5Layout = new GroupLayout(this.colorPanel5);
        this.colorPanel5.setLayout(colorPanel5Layout);
        colorPanel5Layout.setHorizontalGroup(colorPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel5Layout.setVerticalGroup(colorPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel6.setBackground(new Color(255, 255, 255));
        this.colorPanel6.setCursor(new Cursor(0));
        this.colorPanel6.setPreferredSize(new Dimension(32, 32));
        this.colorPanel6.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel6MousePressed(evt);
            }
        });
        GroupLayout colorPanel6Layout = new GroupLayout(this.colorPanel6);
        this.colorPanel6.setLayout(colorPanel6Layout);
        colorPanel6Layout.setHorizontalGroup(colorPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel6Layout.setVerticalGroup(colorPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel7.setBackground(new Color(255, 255, 255));
        this.colorPanel7.setCursor(new Cursor(0));
        this.colorPanel7.setPreferredSize(new Dimension(32, 32));
        this.colorPanel7.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel7MousePressed(evt);
            }
        });
        GroupLayout colorPanel7Layout = new GroupLayout(this.colorPanel7);
        this.colorPanel7.setLayout(colorPanel7Layout);
        colorPanel7Layout.setHorizontalGroup(colorPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel7Layout.setVerticalGroup(colorPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel8.setBackground(new Color(255, 255, 255));
        this.colorPanel8.setCursor(new Cursor(0));
        this.colorPanel8.setPreferredSize(new Dimension(32, 32));
        this.colorPanel8.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel8MousePressed(evt);
            }
        });
        GroupLayout colorPanel8Layout = new GroupLayout(this.colorPanel8);
        this.colorPanel8.setLayout(colorPanel8Layout);
        colorPanel8Layout.setHorizontalGroup(colorPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel8Layout.setVerticalGroup(colorPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel9.setBackground(new Color(255, 255, 255));
        this.colorPanel9.setCursor(new Cursor(0));
        this.colorPanel9.setPreferredSize(new Dimension(32, 32));
        this.colorPanel9.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel9MousePressed(evt);
            }
        });
        GroupLayout colorPanel9Layout = new GroupLayout(this.colorPanel9);
        this.colorPanel9.setLayout(colorPanel9Layout);
        colorPanel9Layout.setHorizontalGroup(colorPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel9Layout.setVerticalGroup(colorPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel10.setBackground(new Color(255, 255, 255));
        this.colorPanel10.setCursor(new Cursor(0));
        this.colorPanel10.setPreferredSize(new Dimension(32, 32));
        this.colorPanel10.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel10MousePressed(evt);
            }
        });
        GroupLayout colorPanel10Layout = new GroupLayout(this.colorPanel10);
        this.colorPanel10.setLayout(colorPanel10Layout);
        colorPanel10Layout.setHorizontalGroup(colorPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel10Layout.setVerticalGroup(colorPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel11.setBackground(new Color(255, 255, 255));
        this.colorPanel11.setCursor(new Cursor(0));
        this.colorPanel11.setPreferredSize(new Dimension(32, 32));
        this.colorPanel11.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel11MousePressed(evt);
            }
        });
        GroupLayout colorPanel11Layout = new GroupLayout(this.colorPanel11);
        this.colorPanel11.setLayout(colorPanel11Layout);
        colorPanel11Layout.setHorizontalGroup(colorPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel11Layout.setVerticalGroup(colorPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel12.setBackground(new Color(255, 255, 255));
        this.colorPanel12.setCursor(new Cursor(0));
        this.colorPanel12.setPreferredSize(new Dimension(32, 32));
        this.colorPanel12.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel12MousePressed(evt);
            }
        });
        GroupLayout colorPanel12Layout = new GroupLayout(this.colorPanel12);
        this.colorPanel12.setLayout(colorPanel12Layout);
        colorPanel12Layout.setHorizontalGroup(colorPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel12Layout.setVerticalGroup(colorPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel13.setBackground(new Color(255, 255, 255));
        this.colorPanel13.setCursor(new Cursor(0));
        this.colorPanel13.setPreferredSize(new Dimension(32, 32));
        this.colorPanel13.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel13MousePressed(evt);
            }
        });
        GroupLayout colorPanel13Layout = new GroupLayout(this.colorPanel13);
        this.colorPanel13.setLayout(colorPanel13Layout);
        colorPanel13Layout.setHorizontalGroup(colorPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel13Layout.setVerticalGroup(colorPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel14.setBackground(new Color(255, 255, 255));
        this.colorPanel14.setCursor(new Cursor(0));
        this.colorPanel14.setPreferredSize(new Dimension(32, 32));
        this.colorPanel14.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel14MousePressed(evt);
            }
        });
        GroupLayout colorPanel14Layout = new GroupLayout(this.colorPanel14);
        this.colorPanel14.setLayout(colorPanel14Layout);
        colorPanel14Layout.setHorizontalGroup(colorPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel14Layout.setVerticalGroup(colorPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel15.setBackground(new Color(255, 255, 255));
        this.colorPanel15.setCursor(new Cursor(0));
        this.colorPanel15.setPreferredSize(new Dimension(32, 32));
        this.colorPanel15.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel15MousePressed(evt);
            }
        });
        GroupLayout colorPanel15Layout = new GroupLayout(this.colorPanel15);
        this.colorPanel15.setLayout(colorPanel15Layout);
        colorPanel15Layout.setHorizontalGroup(colorPanel15Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel15Layout.setVerticalGroup(colorPanel15Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel16.setBackground(new Color(255, 255, 255));
        this.colorPanel16.setCursor(new Cursor(0));
        this.colorPanel16.setPreferredSize(new Dimension(32, 32));
        this.colorPanel16.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel16MousePressed(evt);
            }
        });
        GroupLayout colorPanel16Layout = new GroupLayout(this.colorPanel16);
        this.colorPanel16.setLayout(colorPanel16Layout);
        colorPanel16Layout.setHorizontalGroup(colorPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel16Layout.setVerticalGroup(colorPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        this.colorPanel1.setBackground(new Color(255, 255, 255));
        this.colorPanel1.setCursor(new Cursor(0));
        this.colorPanel1.setPreferredSize(new Dimension(32, 32));
        this.colorPanel1.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                Gui.this.colorPanel1MousePressed(evt);
            }
        });
        GroupLayout colorPanel1Layout = new GroupLayout(this.colorPanel1);
        this.colorPanel1.setLayout(colorPanel1Layout);
        colorPanel1Layout.setHorizontalGroup(colorPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, 32767));
        colorPanel1Layout.setVerticalGroup(colorPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 32, 32767));
        GroupLayout colorsPanel1Layout = new GroupLayout(this.colorsPanel1);
        this.colorsPanel1.setLayout(colorsPanel1Layout);
        colorsPanel1Layout.setHorizontalGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(colorsPanel1Layout.createSequentialGroup().addGap(0, 0, 0).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel1, -1, 33, 32767).addComponent(this.colorPanel9, -1, 33, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel2, -1, 33, 32767).addComponent(this.colorPanel10, -1, 33, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel3, -1, 33, 32767).addComponent(this.colorPanel11, -1, 33, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel4, -1, 35, 32767).addComponent(this.colorPanel12, -1, 35, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel5, -1, 37, 32767).addComponent(this.colorPanel13, -1, 37, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel6, -1, 35, 32767).addComponent(this.colorPanel14, -1, 35, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel7, -1, 35, 32767).addComponent(this.colorPanel15, -1, 35, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel8, -1, 34, 32767).addComponent(this.colorPanel16, -1, 34, 32767))));
        colorsPanel1Layout.setVerticalGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(colorsPanel1Layout.createSequentialGroup().addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel2, -2, -1, -2).addComponent(this.colorPanel3, -2, -1, -2).addComponent(this.colorPanel4, -2, -1, -2).addComponent(this.colorPanel5, -2, -1, -2).addComponent(this.colorPanel6, -2, -1, -2).addComponent(this.colorPanel7, -2, -1, -2).addComponent(this.colorPanel8, -2, -1, -2).addComponent(this.colorPanel1, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(colorsPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorPanel10, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.colorPanel11, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.colorPanel12, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.colorPanel13, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.colorPanel14, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.colorPanel15, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.colorPanel16, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.colorPanel9, GroupLayout.Alignment.TRAILING, -2, -1, -2))));
        GroupLayout previewPanelLayout = new GroupLayout(this.previewPanel);
        this.previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(previewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.playerPanel, GroupLayout.Alignment.TRAILING, -1, -1, 32767).addComponent(this.scrollPanel, GroupLayout.Alignment.TRAILING).addComponent(this.colorsPanel1, -1, 333, 32767));
        previewPanelLayout.setVerticalGroup(previewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(previewPanelLayout.createSequentialGroup().addComponent(this.scrollPanel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.playerPanel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.colorsPanel1, -2, 100, -2)));
        this.characterPanel1.setBackground(new Color(228, 236, 191));
        this.characterPanel1.setBorder(BorderFactory.createTitledBorder(null, "View", 0, 0, new Font("Tahoma", 0, 14), new Color(0, 0, 0)));
        this.showHitsCheck.setBackground(new Color(228, 236, 191));
        this.showHitsCheck.setSelected(true);
        this.showHitsCheck.setText("Hit Point");
        this.showHitsCheck.setContentAreaFilled(false);
        this.showHitsCheck.setFocusable(false);
        this.showHitsCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.showHitsCheckActionPerformed(evt);
            }
        });
        this.showWeaponCheck.setBackground(new Color(228, 236, 191));
        this.showWeaponCheck.setSelected(true);
        this.showWeaponCheck.setText("Weapon:");
        this.showWeaponCheck.setContentAreaFilled(false);
        this.showWeaponCheck.setFocusable(false);
        this.showWeaponCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.showWeaponCheckActionPerformed(evt);
            }
        });
        this.weaponCombo.setModel(new DefaultComboBoxModel<String>(new String[]{"Knife", "Pipe"}));
        this.weaponCombo.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.weaponComboActionPerformed(evt);
            }
        });
        this.showFacedRightCheck.setBackground(new Color(228, 236, 191));
        this.showFacedRightCheck.setSelected(true);
        this.showFacedRightCheck.setText("Faced Right");
        this.showFacedRightCheck.setContentAreaFilled(false);
        this.showFacedRightCheck.setFocusable(false);
        this.showFacedRightCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.showFacedRightCheckActionPerformed(evt);
            }
        });
        this.showTileCheck.setBackground(new Color(228, 236, 191));
        this.showTileCheck.setSelected(true);
        this.showTileCheck.setText("Tile Space");
        this.showTileCheck.setContentAreaFilled(false);
        this.showTileCheck.setFocusable(false);
        this.showTileCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.showTileCheckActionPerformed(evt);
            }
        });
        this.showCenterCheck.setBackground(new Color(228, 236, 191));
        this.showCenterCheck.setText("Ghost");
        this.showCenterCheck.setContentAreaFilled(false);
        this.showCenterCheck.setFocusable(false);
        this.showCenterCheck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.showCenterCheckActionPerformed(evt);
            }
        });
        GroupLayout characterPanel1Layout = new GroupLayout(this.characterPanel1);
        this.characterPanel1.setLayout(characterPanel1Layout);
        characterPanel1Layout.setHorizontalGroup(characterPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(characterPanel1Layout.createSequentialGroup().addGroup(characterPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.showWeaponCheck).addComponent(this.showCenterCheck).addComponent(this.showHitsCheck)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(characterPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.weaponCombo, -2, 83, -2).addComponent(this.showFacedRightCheck).addComponent(this.showTileCheck)).addContainerGap(-1, 32767)));
        characterPanel1Layout.setVerticalGroup(characterPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(characterPanel1Layout.createSequentialGroup().addGap(1, 1, 1).addGroup(characterPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.showFacedRightCheck).addComponent(this.showCenterCheck)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(characterPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.showTileCheck).addComponent(this.showHitsCheck)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 3, 32767).addGroup(characterPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.weaponCombo, -2, -1, -2).addComponent(this.showWeaponCheck))));
        this.toolsPanel.setBackground(new Color(230, 230, 235));
        this.toolsPanel.setBorder(BorderFactory.createTitledBorder(null, "Tools", 0, 0, new Font("Tahoma", 0, 14), new Color(0, 0, 0)));
        this.pencilRadio.setBackground(new Color(230, 226, 235));
        this.pencilRadio.setText("Pencil");
        this.pencilRadio.setContentAreaFilled(false);
        this.pencilRadio.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.pencilRadioActionPerformed(evt);
            }
        });
        this.brushRadio.setBackground(new Color(230, 226, 235));
        this.brushRadio.setText("Brush");
        this.brushRadio.setContentAreaFilled(false);
        this.brushRadio.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.brushRadioActionPerformed(evt);
            }
        });
        this.bucketRadio.setBackground(new Color(230, 226, 235));
        this.bucketRadio.setText("Paint Bucket");
        this.bucketRadio.setContentAreaFilled(false);
        this.bucketRadio.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.bucketRadioActionPerformed(evt);
            }
        });
        this.dragSpriteRadio.setBackground(new Color(230, 226, 235));
        this.dragSpriteRadio.setText("Drag Sprite");
        this.dragSpriteRadio.setContentAreaFilled(false);
        this.dragSpriteRadio.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.dragSpriteRadioActionPerformed(evt);
            }
        });
        this.dragImageRadio.setBackground(new Color(230, 226, 235));
        this.dragImageRadio.setText("Drag Image");
        this.dragImageRadio.setContentAreaFilled(false);
        this.dragImageRadio.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.dragImageRadioActionPerformed(evt);
            }
        });
        this.noneRadio.setBackground(new Color(230, 226, 235));
        this.noneRadio.setSelected(true);
        this.noneRadio.setText("None");
        this.noneRadio.setContentAreaFilled(false);
        this.noneRadio.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.noneRadioActionPerformed(evt);
            }
        });
        GroupLayout toolsPanelLayout = new GroupLayout(this.toolsPanel);
        this.toolsPanel.setLayout(toolsPanelLayout);
        toolsPanelLayout.setHorizontalGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(toolsPanelLayout.createSequentialGroup().addContainerGap().addGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pencilRadio).addComponent(this.brushRadio).addComponent(this.bucketRadio)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(toolsPanelLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(this.dragSpriteRadio)).addGroup(GroupLayout.Alignment.TRAILING, toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.dragImageRadio, -1, -1, 32767).addComponent(this.noneRadio, -2, 81, -2)))));
        toolsPanelLayout.setVerticalGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(toolsPanelLayout.createSequentialGroup().addGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.pencilRadio).addComponent(this.dragSpriteRadio)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.brushRadio).addComponent(this.dragImageRadio)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.bucketRadio).addComponent(this.noneRadio))));
        this.overridePanel.setBackground(new Color(240, 233, 221));
        this.overridePanel.setBorder(BorderFactory.createTitledBorder(null, "Override Art", 0, 0, new Font("Tahoma", 0, 14), new Color(0, 0, 0)));
        this.softReplaceButton.setText("Replace from Image");
        this.softReplaceButton.setFocusable(false);
        this.softReplaceButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.softReplaceButtonActionPerformed(evt);
            }
        });
        this.jLabel2.setText("Replace art using same tiles");
        GroupLayout overridePanelLayout = new GroupLayout(this.overridePanel);
        this.overridePanel.setLayout(overridePanelLayout);
        overridePanelLayout.setHorizontalGroup(overridePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(overridePanelLayout.createSequentialGroup().addContainerGap(-1, 32767).addGroup(overridePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, overridePanelLayout.createSequentialGroup().addComponent(this.softReplaceButton).addGap(18, 18, 18)).addGroup(GroupLayout.Alignment.TRAILING, overridePanelLayout.createSequentialGroup().addComponent(this.jLabel2).addContainerGap()))));
        overridePanelLayout.setVerticalGroup(overridePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, overridePanelLayout.createSequentialGroup().addGap(19, 19, 19).addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 27, 32767).addComponent(this.softReplaceButton)));
        this.generatePanel.setBackground(new Color(240, 221, 221));
        this.generatePanel.setBorder(BorderFactory.createTitledBorder(null, "Generate Sprite", 0, 0, new Font("Tahoma", 0, 14), new Color(0, 0, 0)));
        this.hardReplaceButton.setText("Generate From Image");
        this.hardReplaceButton.setFocusable(false);
        this.hardReplaceButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.hardReplaceButtonActionPerformed(evt);
            }
        });
        this.jLabel13.setFont(new Font("Tahoma", 0, 14));
        this.jLabel13.setHorizontalAlignment(4);
        this.jLabel13.setText("Address:");
        this.genAddressField.setFont(new Font("Courier New", 0, 14));
        this.genAddressField.setHorizontalAlignment(4);
        this.genAddressField.setText("200");
        this.jLabel14.setFont(new Font("Tahoma", 0, 14));
        this.jLabel14.setHorizontalAlignment(4);
        this.jLabel14.setText("Palette Line:");
        this.genPaletteField.setFont(new Font("Courier New", 0, 14));
        this.genPaletteField.setHorizontalAlignment(4);
        this.genPaletteField.setText("0");
        GroupLayout generatePanelLayout = new GroupLayout(this.generatePanel);
        this.generatePanel.setLayout(generatePanelLayout);
        generatePanelLayout.setHorizontalGroup(generatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(generatePanelLayout.createSequentialGroup().addGroup(generatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(generatePanelLayout.createSequentialGroup().addGap(18, 18, 18).addGroup(generatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(generatePanelLayout.createSequentialGroup().addComponent(this.jLabel13, -2, 61, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.genAddressField)).addGroup(generatePanelLayout.createSequentialGroup().addComponent(this.jLabel14, -2, 79, -2).addGap(18, 18, 18).addComponent(this.genPaletteField, -2, 24, -2)))).addGroup(generatePanelLayout.createSequentialGroup().addContainerGap().addComponent(this.hardReplaceButton))).addGap(0, 25, 32767)));
        generatePanelLayout.setVerticalGroup(generatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, generatePanelLayout.createSequentialGroup().addGroup(generatePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel13).addComponent(this.genAddressField, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(generatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel14, GroupLayout.Alignment.TRAILING).addComponent(this.genPaletteField, GroupLayout.Alignment.TRAILING, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.hardReplaceButton)));
        GroupLayout mainPanelLayout = new GroupLayout(this.mainPanel);
        this.mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.characterPanel1, -1, 0, 32767).addComponent(this.overridePanel, -1, -1, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.toolsPanel, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.generatePanel, GroupLayout.Alignment.TRAILING, -2, -1, -2))).addComponent(this.characterPanel, -2, 376, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.previewPanel, -1, -1, 32767)));
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addComponent(this.characterPanel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.characterPanel1, -2, -1, -2).addComponent(this.toolsPanel, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.overridePanel, -2, -1, -2).addComponent(this.generatePanel, -2, -1, -2))).addComponent(this.previewPanel, -1, -1, 32767));
        this.jMenu1.setText("File");
        this.openRomMenu.setAccelerator(KeyStroke.getKeyStroke(82, 2));
        this.openRomMenu.setText("Open Rom");
        this.openRomMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.openRomMenuActionPerformed(evt);
            }
        });
        this.jMenu1.add(this.openRomMenu);
        this.jMenuItem4.setText("Open with Specific Guide");
        this.jMenuItem4.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem4ActionPerformed(evt);
            }
        });
        this.jMenu1.add(this.jMenuItem4);
        this.closeMenu.setText("Close");
        this.closeMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.closeMenuActionPerformed(evt);
            }
        });
        this.jMenu1.add(this.closeMenu);
        this.jMenu1.add(this.jSeparator2);
        this.inportMenu.setText("Inport Character...");
        this.jMenuItem1.setText("Replace from Spritesheet");
        this.jMenuItem1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem1ActionPerformed(evt);
            }
        });
        this.inportMenu.add(this.jMenuItem1);
        this.jMenuItem2.setText("Generate from Spritesheet");
        this.jMenuItem2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem2ActionPerformed(evt);
            }
        });
        this.inportMenu.add(this.jMenuItem2);
        this.inportMenu.add(this.jSeparator6);
        this.jMenuItem3.setText("Import from other ROM");
        this.jMenuItem3.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem3ActionPerformed(evt);
            }
        });
        this.inportMenu.add(this.jMenuItem3);
        this.jMenu1.add(this.inportMenu);
        this.exportMenu.setText("Export Character...");
        this.spriteSheetMenu1.setText("Individual Frames");
        this.spriteSheetMenu1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.spriteSheetMenu1ActionPerformed(evt);
            }
        });
        this.exportMenu.add(this.spriteSheetMenu1);
        this.spriteSheetMenu.setText("Spritesheet");
        this.spriteSheetMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.spriteSheetMenuActionPerformed(evt);
            }
        });
        this.exportMenu.add(this.spriteSheetMenu);
        this.jMenu1.add(this.exportMenu);
        this.jMenu1.add(this.jSeparator1);
        this.jMenuItem5.setAccelerator(KeyStroke.getKeyStroke(115, 8));
        this.jMenuItem5.setText("Exit");
        this.jMenuItem5.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem5ActionPerformed(evt);
            }
        });
        this.jMenu1.add(this.jMenuItem5);
        this.jMenuBar1.add(this.jMenu1);
        this.jMenu2.setText("Edit");
        this.jMenu6.setText("Tool");
        this.pencilMenu.setAccelerator(KeyStroke.getKeyStroke(49, 8));
        this.pencilMenu.setText("Pencil");
        this.pencilMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.pencilMenuActionPerformed(evt);
            }
        });
        this.jMenu6.add(this.pencilMenu);
        this.brushMenu.setAccelerator(KeyStroke.getKeyStroke(50, 8));
        this.brushMenu.setText("Brush");
        this.brushMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.brushMenuActionPerformed(evt);
            }
        });
        this.jMenu6.add(this.brushMenu);
        this.bucketMenu.setAccelerator(KeyStroke.getKeyStroke(51, 8));
        this.bucketMenu.setText("Paint Bucket");
        this.bucketMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.bucketMenuActionPerformed(evt);
            }
        });
        this.jMenu6.add(this.bucketMenu);
        this.dragSpriteMenu.setAccelerator(KeyStroke.getKeyStroke(52, 8));
        this.dragSpriteMenu.setText("Drag Sprite");
        this.dragSpriteMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.dragSpriteMenuActionPerformed(evt);
            }
        });
        this.jMenu6.add(this.dragSpriteMenu);
        this.dragImageMenu.setAccelerator(KeyStroke.getKeyStroke(53, 8));
        this.dragImageMenu.setText("Drag Image");
        this.dragImageMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.dragImageMenuActionPerformed(evt);
            }
        });
        this.jMenu6.add(this.dragImageMenu);
        this.noneMenu.setAccelerator(KeyStroke.getKeyStroke(54, 8));
        this.noneMenu.setSelected(true);
        this.noneMenu.setText("None");
        this.noneMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.noneMenuActionPerformed(evt);
            }
        });
        this.jMenu6.add(this.noneMenu);
        this.jMenu2.add(this.jMenu6);
        this.jMenu5.setText("Brush Size");
        this.sizeRadioMenu1.setAccelerator(KeyStroke.getKeyStroke(49, 1));
        this.sizeRadioMenu1.setSelected(true);
        this.sizeRadioMenu1.setText("3 pixels");
        this.sizeRadioMenu1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.sizeRadioMenu1ActionPerformed(evt);
            }
        });
        this.jMenu5.add(this.sizeRadioMenu1);
        this.sizeRadioMenu2.setAccelerator(KeyStroke.getKeyStroke(50, 1));
        this.sizeRadioMenu2.setText("5 pixels");
        this.sizeRadioMenu2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.sizeRadioMenu2ActionPerformed(evt);
            }
        });
        this.jMenu5.add(this.sizeRadioMenu2);
        this.sizeRadioMenu3.setAccelerator(KeyStroke.getKeyStroke(51, 1));
        this.sizeRadioMenu3.setText("10 pixels");
        this.sizeRadioMenu3.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.sizeRadioMenu3ActionPerformed(evt);
            }
        });
        this.jMenu5.add(this.sizeRadioMenu3);
        this.jMenu2.add(this.jMenu5);
        this.jMenu4.setText("Scale");
        this.jMenuItem11.setAccelerator(KeyStroke.getKeyStroke(49, 2));
        this.jMenuItem11.setText("Reset Scale");
        this.jMenuItem11.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem11ActionPerformed(evt);
            }
        });
        this.jMenu4.add(this.jMenuItem11);
        this.jMenuItem7.setAccelerator(KeyStroke.getKeyStroke(50, 2));
        this.jMenuItem7.setText("2x Scale");
        this.jMenuItem7.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem7ActionPerformed(evt);
            }
        });
        this.jMenu4.add(this.jMenuItem7);
        this.jMenuItem9.setAccelerator(KeyStroke.getKeyStroke(51, 2));
        this.jMenuItem9.setText("6x Scale");
        this.jMenuItem9.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem9ActionPerformed(evt);
            }
        });
        this.jMenu4.add(this.jMenuItem9);
        this.jMenuItem10.setAccelerator(KeyStroke.getKeyStroke(52, 2));
        this.jMenuItem10.setText("12x Scale");
        this.jMenuItem10.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem10ActionPerformed(evt);
            }
        });
        this.jMenu4.add(this.jMenuItem10);
        this.jMenu2.add(this.jMenu4);
        this.hexIdsMenu.setText("See IDs as hex");
        this.hexIdsMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.hexIdsMenuActionPerformed(evt);
            }
        });
        this.jMenu2.add(this.hexIdsMenu);
        this.jMenu2.add(this.jSeparator3);
        this.resizeAnimsMenu.setText("Resize Animations");
        this.resizeAnimsMenu.setEnabled(false);
        this.resizeAnimsMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.resizeAnimsMenuActionPerformed(evt);
            }
        });
        this.jMenu2.add(this.resizeAnimsMenu);
        this.nameMenu.setText("Properties");
        this.nameMenu.setEnabled(false);
        this.nameMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.nameMenuActionPerformed(evt);
            }
        });
        this.jMenu2.add(this.nameMenu);
        this.jMenu2.add(this.jSeparator4);
        this.copyMenu.setAccelerator(KeyStroke.getKeyStroke(116, 0));
        this.copyMenu.setText("Copy Frame");
        this.copyMenu.setEnabled(false);
        this.copyMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.copyMenuActionPerformed(evt);
            }
        });
        this.jMenu2.add(this.copyMenu);
        this.pasteMenu.setAccelerator(KeyStroke.getKeyStroke(117, 0));
        this.pasteMenu.setText("Paste Frame");
        this.pasteMenu.setEnabled(false);
        this.pasteMenu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.pasteMenuActionPerformed(evt);
            }
        });
        this.jMenu2.add(this.pasteMenu);
        this.jMenu2.add(this.jSeparator5);
        this.pasteMenu1.setText("Decompress Art");
        this.pasteMenu1.setEnabled(false);
        this.pasteMenu1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.pasteMenu1ActionPerformed(evt);
            }
        });
        this.jMenu2.add(this.pasteMenu1);
        this.jMenuBar1.add(this.jMenu2);
        this.jMenu3.setText("Help");
        this.jMenuItem6.setText("About");
        this.jMenuItem6.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Gui.this.jMenuItem6ActionPerformed(evt);
            }
        });
        this.jMenu3.add(this.jMenuItem6);
        this.jMenuBar1.add(this.jMenu3);
        this.setJMenuBar(this.jMenuBar1);
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.mainPanel, -1, -1, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.mainPanel, -1, -1, 32767));
        this.pack();
    }

    private void colorPanel2MousePressed(MouseEvent evt) {
        this.colorPanelPressed(1);
    }

    private void colorPanel3MousePressed(MouseEvent evt) {
        this.colorPanelPressed(2);
    }

    private void colorPanel4MousePressed(MouseEvent evt) {
        this.colorPanelPressed(3);
    }

    private void colorPanel5MousePressed(MouseEvent evt) {
        this.colorPanelPressed(4);
    }

    private void colorPanel6MousePressed(MouseEvent evt) {
        this.colorPanelPressed(5);
    }

    private void colorPanel7MousePressed(MouseEvent evt) {
        this.colorPanelPressed(6);
    }

    private void colorPanel8MousePressed(MouseEvent evt) {
        this.colorPanelPressed(7);
    }

    private void colorPanel9MousePressed(MouseEvent evt) {
        this.colorPanelPressed(8);
    }

    private void colorPanel10MousePressed(MouseEvent evt) {
        this.colorPanelPressed(9);
    }

    private void colorPanel11MousePressed(MouseEvent evt) {
        this.colorPanelPressed(10);
    }

    private void colorPanel12MousePressed(MouseEvent evt) {
        this.colorPanelPressed(11);
    }

    private void colorPanel13MousePressed(MouseEvent evt) {
        this.colorPanelPressed(12);
    }

    private void colorPanel14MousePressed(MouseEvent evt) {
        this.colorPanelPressed(13);
    }

    private void colorPanel15MousePressed(MouseEvent evt) {
        this.colorPanelPressed(14);
    }

    private void colorPanel16MousePressed(MouseEvent evt) {
        this.colorPanelPressed(15);
    }

    private void colorPanel1MousePressed(MouseEvent evt) {
        this.colorPanelPressed(0);
    }

    private void formMouseWheelMoved(MouseWheelEvent evt) {
        this.scaleImagePanel(-0.2f * (float)evt.getWheelRotation());
    }

    private void jMenuItem11ActionPerformed(ActionEvent evt) {
        this.setImagePanelScale(1.0f);
    }

    private void jMenuItem7ActionPerformed(ActionEvent evt) {
        this.setImagePanelScale(2.0f);
    }

    private void jMenuItem9ActionPerformed(ActionEvent evt) {
        this.setImagePanelScale(6.0f);
    }

    private void jMenuItem10ActionPerformed(ActionEvent evt) {
        this.setImagePanelScale(12.0f);
    }

    private void openRomMenuActionPerformed(ActionEvent evt) {
        Guide oldGuide = this.guide;
        if (this.openDefaultGuide() || this.openCustomGuide()) {
            if (!this.openRom()) {
                this.guide = oldGuide;
            }
        } else {
            this.guide = oldGuide;
        }
    }

    private void closeMenuActionPerformed(ActionEvent evt) {
        this.closeRom();
    }

    private void jMenuItem4ActionPerformed(ActionEvent evt) {
        Guide oldGuide = this.guide;
        if (this.openCustomGuide()) {
            if (!this.openRom()) {
                this.guide = oldGuide;
            }
        } else {
            this.guide = oldGuide;
        }
    }

    private void jMenuItem5ActionPerformed(ActionEvent evt) {
        if (this.askSaveRom()) {
            this.closeRom();
            System.exit(0);
        }
    }

    private void jButton3ActionPerformed(ActionEvent evt) {
        this.setNextFrame();
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        this.setPreviousFrame();
    }

    private void nextButActionPerformed(ActionEvent evt) {
        this.setNextFrame();
    }

    private void previousButActionPerformed(ActionEvent evt) {
        this.setPreviousFrame();
    }

    private void animationComboActionPerformed(ActionEvent evt) {
        int newAnim = this.animationCombo.getSelectedIndex();
        if (newAnim != this.currAnimation && newAnim >= 0) {
            this.changeAnimation(newAnim);
        }
    }

    private void frontButActionPerformed(ActionEvent evt) {
        this.setNextAnimation();
    }

    private void backButActionPerformed(ActionEvent evt) {
        this.setPreviousAnimation();
    }

    private void animationComboKeyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case 39: {
                this.setNextFrame();
                break;
            }
            case 37: {
                this.setPreviousFrame();
            }
        }
    }

    private void characterComboActionPerformed(ActionEvent evt) {
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
    }

    private void playToggleActionPerformed(ActionEvent evt) {
        if (this.playToggle.isSelected()) {
            this.timer.start();
            this.playToggle.setText("[]");
        } else {
            this.timer.stop();
            this.playToggle.setText(">");
        }
    }

    private void showHitsCheckActionPerformed(ActionEvent evt) {
        this.imagePanel.showHit(this.showHitsCheck.isSelected());
    }

    private void showCenterCheckActionPerformed(ActionEvent evt) {
        this.imagePanel.showGhost(this.showCenterCheck.isSelected());
    }

    private void showTileCheckActionPerformed(ActionEvent evt) {
        this.imagePanel.showShadow(this.showTileCheck.isSelected());
    }

    private void hitCheckActionPerformed(ActionEvent evt) {
        Character ch = this.manager.getCharacter();
        HitFrame hitFrame = ch.getHitFrame(this.currAnimation, this.currFrame);
        boolean selected = this.hitCheck.isSelected();
        if (hitFrame.isEnabled() != selected) {
            hitFrame.setEnabled(selected);
            ch.setModified(true);
            this.refresh();
        }
    }

    private void koCheckActionPerformed(ActionEvent evt) {
        Character ch = this.manager.getCharacter();
        HitFrame hitFrame = ch.getHitFrame(this.currAnimation, this.currFrame);
        boolean selected = this.koCheck.isSelected();
        if (hitFrame.knockDown != selected) {
            hitFrame.knockDown = selected;
            ch.setModified(true);
            this.refresh();
        }
    }

    private void formWindowClosing(WindowEvent evt) {
        if (this.askSaveRom()) {
            this.closeRom();
            System.exit(0);
        }
    }

    private void softReplaceButtonActionPerformed(ActionEvent evt) {
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
    }

    private void hardReplaceButtonActionPerformed(ActionEvent evt) {
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
                this.replaceSprite(replaceImg, this.currAnimation, this.currFrame, cx, cy);
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
    }

    private void pencilRadioActionPerformed(ActionEvent evt) {
        this.setRadiosOff();
        this.pencilRadio.setSelected(true);
        this.imagePanel.setMode(Mode.pencil);
    }

    private void brushRadioActionPerformed(ActionEvent evt) {
        this.setRadiosOff();
        this.brushRadio.setSelected(true);
        this.imagePanel.setMode(Mode.brush);
    }

    private void bucketRadioActionPerformed(ActionEvent evt) {
        this.setRadiosOff();
        this.bucketRadio.setSelected(true);
        this.imagePanel.setMode(Mode.bucket);
    }

    private void dragSpriteRadioActionPerformed(ActionEvent evt) {
        this.setRadiosOff();
        this.dragSpriteRadio.setSelected(true);
        this.imagePanel.setMode(Mode.dragSprite);
    }

    private void dragImageRadioActionPerformed(ActionEvent evt) {
        this.setRadiosOff();
        this.dragImageRadio.setSelected(true);
        this.imagePanel.setMode(Mode.dragImage);
    }

    private void noneRadioActionPerformed(ActionEvent evt) {
        this.setRadiosOff();
        this.noneRadio.setSelected(true);
        this.imagePanel.setMode(Mode.none);
    }

    private void sizeRadioMenu1ActionPerformed(ActionEvent evt) {
        this.setSizeRadioMenusOff();
        this.sizeRadioMenu1.setSelected(true);
        this.imagePanel.setBrushSize(3);
    }

    private void sizeRadioMenu2ActionPerformed(ActionEvent evt) {
        this.setSizeRadioMenusOff();
        this.sizeRadioMenu2.setSelected(true);
        this.imagePanel.setBrushSize(5);
    }

    private void sizeRadioMenu3ActionPerformed(ActionEvent evt) {
        this.setSizeRadioMenusOff();
        this.sizeRadioMenu3.setSelected(true);
        this.imagePanel.setBrushSize(10);
    }

    private void bucketMenuActionPerformed(ActionEvent evt) {
        this.bucketRadioActionPerformed(null);
    }

    private void pencilMenuActionPerformed(ActionEvent evt) {
        this.pencilRadioActionPerformed(null);
    }

    private void brushMenuActionPerformed(ActionEvent evt) {
        this.brushRadioActionPerformed(null);
    }

    private void dragSpriteMenuActionPerformed(ActionEvent evt) {
        this.dragSpriteRadioActionPerformed(null);
    }

    private void dragImageMenuActionPerformed(ActionEvent evt) {
        this.dragImageRadioActionPerformed(null);
    }

    private void noneMenuActionPerformed(ActionEvent evt) {
        this.noneRadioActionPerformed(null);
    }

    private void showWeaponCheckActionPerformed(ActionEvent evt) {
        this.weaponCombo.setEnabled(this.showWeaponCheck.isSelected());
        this.imagePanel.showWeapon(this.showWeaponCheck.isSelected());
    }

    private void weaponCheckActionPerformed(ActionEvent evt) {
        Character ch = this.manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        boolean selected = this.weaponCheck.isSelected();
        if (frame.isEnabled() != selected) {
            frame.setEnabled(selected);
            ch.setModified(true);
            this.refresh();
        }
    }

    private void weaponComboActionPerformed(ActionEvent evt) {
        this.imagePanel.setWeaponPreview(this.weaponCombo.getSelectedIndex());
    }

    private void behindCheckActionPerformed(ActionEvent evt) {
        Character ch = this.manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(this.currAnimation, this.currFrame);
        boolean selected = this.behindCheck.isSelected();
        if (frame.showBehind != selected) {
            frame.showBehind = selected;
            ch.setModified(true);
            this.refresh();
        }
    }

    private void jMenuItem6ActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Pancake 2 v1.6b\n\u00a9 Gil Costa 2012\n\nAcknowledgment on derived work\nwould be appreciated but is not required\n\nPk2 is free software. The author can not be held responsible\nfor any illicit use of this program.\n", "About", 1);
    }

    private void spriteSheetMenuActionPerformed(ActionEvent evt) {
        this.exportSpriteSheet();
    }

    private void showFacedRightCheckActionPerformed(ActionEvent evt) {
        this.imagePanel.setFacedRight(this.showFacedRightCheck.isSelected());
    }

    private void jMenuItem1ActionPerformed(ActionEvent evt) {
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
    }

    private void jMenuItem2ActionPerformed(ActionEvent evt) {
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
    }

    private void nameMenuActionPerformed(ActionEvent evt) {
        new PropertiesDialog(this, this.manager).setVisible(true);
    }

    public BufferedImage openImageForPortrait() {
        int returnVal = this.imageChooser.showOpenDialog(this);
        if (returnVal == 0) {
            BufferedImage img;
            File file = this.imageChooser.getSelectedFile();
            try {
                img = ImageIO.read(file);
                img = this.processReplaceImg(img, false);
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

    private void copyMenuActionPerformed(ActionEvent evt) {
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
    }

    private void pasteMenuActionPerformed(ActionEvent evt) {
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
    }

    private void spriteSheetMenu1ActionPerformed(ActionEvent evt) {
        this.exportIndividualFrames();
    }

    private void resizeAnimsMenuActionPerformed(ActionEvent evt) {
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
    }

    private void characterComboKeyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case 81: {
                this.setPreviousAnimation();
                break;
            }
            case 65: {
                this.setNextAnimation();
            }
        }
    }

    private void pasteMenu1ActionPerformed(ActionEvent evt) {
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
    }

    private void jMenuItem3ActionPerformed(ActionEvent evt) {
        this.portRom();
        this.hardRefresh();
        JOptionPane.showMessageDialog(this, "Character sucessfully imported", "Done!", 1);
    }

    private void hexIdsMenuActionPerformed(ActionEvent evt) {
        this.setupCharacterCombo();
        this.setupAnimationCombo();
    }

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

    private void updateGenAddress() {
        try {
            long mapAddress = this.manager.romSize();
            this.setFieldAsHex(this.genAddressField, mapAddress);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
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

    private long replaceSprite(BufferedImage img, int animId, int frameId, int cx, int cy) {
        int width = img.getWidth();
        int height = img.getHeight();
        AnimFrame frame = this.manager.getCharacter().getAnimFrame(animId, frameId);
        if (frame.type == 3) {
            return frame.mapAddress;
        }
        long mapAddress = this.getHexFromField(this.genAddressField);
        int paletteLine = this.getIntFromField(this.genPaletteField);
        if (paletteLine == Integer.MIN_VALUE || paletteLine < 0 || paletteLine > 3) {
            this.showError("Invalid palette line");
            return Long.MIN_VALUE;
        }
        if (mapAddress == Long.MIN_VALUE || mapAddress < 0L) {
            this.showError("Invalid address");
            this.updateGenAddress();
            return Long.MIN_VALUE;
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
        long artAddress = mapAddress + 6L + sprite.getNumPieces() * 6L;
        frame.mapAddress = mapAddress;
        frame.artAddress = artAddress;
        try {
            this.manager.writeSprite(sprite, mapAddress, artAddress);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to create the new sprite");
            return Long.MIN_VALUE;
        }
        this.updateGenAddress();
        BufferedImage extended = this.expandImage(img, cx, cy);
        Animation anim = this.manager.getCharacter().getAnimation(animId);
        anim.setImage(frameId, extended);
        try {
            this.manager.save();
        }
        catch (IOException e) {
            e.printStackTrace();
            this.showError("Unable to render the new sprite");
            return Long.MIN_VALUE;
        }
        return mapAddress;
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
        String romName = null;
        Manager manager = null;
        returnVal = this.romChooser.showOpenDialog(this);
        if (returnVal == 0) {
            File file = this.romChooser.getSelectedFile();
            romName = file.getAbsolutePath();
            try {
                manager = new Manager(romName, guide);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                this.showError("File '" + romName + "' not found");
            }
            catch (IOException ex) {
                ex.printStackTrace();
                this.showError("File '" + romName + "' is not a valid Streets of Rage 2 ROM");
            }
        }
        if (manager == null) {
            return;
        }
        int charId = this.manager.getCurrentCharacterId();
        int fakeId = guide.getFakeCharId(charId);
        try {
            manager.setCharacter(charId, guide.getAnimsCount(fakeId), guide.getType(fakeId));
        }
        catch (IOException ex) {
            ex.printStackTrace();
            this.showError("Unable to read character #" + charId);
        }
        try {
            this.manager.replaceCharacterFromManager(manager);
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
                HashSet<Animation> processed = new HashSet<Animation>();
                for (int i = 0; i < numAnims; ++i) {
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)) {
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0; j < animSize; ++j) {
                            long newAddress;
                            anim.setSpritesModified(j, true);
                            long mapAddress = anim.getFrame((int)j).mapAddress;
                            if (maps.containsKey(mapAddress)) {
                                AddressesPair p = (AddressesPair)maps.get(mapAddress);
                                anim.getFrame((int)j).mapAddress = p.a;
                                anim.getFrame((int)j).artAddress = p.b;
                                anim.setImage(j, p.img);
                                continue;
                            }
                            int x = index % columns * frameWidth;
                            int y = index / columns * frameHeight;
                            BufferedImage img = sheet.getSubimage(x, y, frameWidth, frameHeight);
                            if (Gui.this.imagePanel.isFacedRight()) {
                                img = ImagePanel.flipImage(img);
                            }
                            if ((newAddress = Gui.this.replaceSprite(img, i, j, cx, cy)) == Long.MIN_VALUE) {
                                this.finish();
                                Gui.this.showError("Unable to replace sprites");
                                return;
                            }
                            maps.put(mapAddress, new AddressesPair(anim.getFrame((int)j).mapAddress, anim.getFrame((int)j).artAddress, anim.getImage(j)));
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
                    newAnimsAddress = Gui.this.getHexFromField(Gui.this.genAddressField);
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
                Gui.this.updateGenAddress();
                Gui.this.hardRefresh();
                this.finish();
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

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
                long newAnimsAddress = Gui.this.getHexFromField(Gui.this.genAddressField);
                int animsSyzeInBytes = ch.getAnimsSize(type);
                Gui.this.setFieldAsHex(Gui.this.genAddressField, newAnimsAddress + (long)animsSyzeInBytes);
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
                        long newAddress = Gui.this.replaceSprite(img, i, j, 128, 128);
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
                Gui.this.updateGenAddress();
                Gui.this.guide.setAnimType(Gui.this.guide.getFakeCharId(Gui.this.manager.getCurrentCharacterId()), type);
                Gui.this.hardRefresh();
                this.finish();
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

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

