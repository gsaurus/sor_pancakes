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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import lib.Manager;
import lib.anim.AnimFrame;
import lib.anim.Animation;
import lib.anim.HitFrame;
import lib.anim.WeaponFrame;
import lib.map.*;




//class FieldSetter implements Runnable{
//    private JTextField field;
//    private String value;
//
//    public FieldSetter(JTextField field, String value) {
//        this.field = field;
//        this.value = value;
//    }
//    
//    @Override
//    public void run() {
//        if (!field.getText().equals(value))
//            field.setText(value);
//    }
//    
//}


/**
 *
 * @author Gil
 */
public class Gui extends javax.swing.JFrame implements ActionListener, TheListener{
    private static final String VERSION = " v1.5";    
    private static final String TITLE = "Pancake 2" + VERSION;
    
    private static final int INVALID_INT = Integer.MIN_VALUE;
    private static final long INVALID_LONG = Long.MIN_VALUE;
    
    private String romName;
    private String currentDirectory;
    private JFileChooser romChooser;
    private JFileChooser guideChooser;
    private JFileChooser imageChooser;
    private JFileChooser imageSaver;
    private JFileChooser resizerChooser;
    
    private ImagePanel imagePanel;
    private Manager manager;
    private Guide guide;
    
    private JPanel[] colorPanels;
    private Border selectionBorder;
    
    private javax.swing.Timer timer;
    private int frameDelayCount;
    
    private int currAnimation;
    private int currFrame;
    
    private HashSet<JTextField> inUse;
    private int selectedColor;
    
    private long copiedMap;
    private long copiedArt;
    private boolean copiedHasHit, copiedKnockDown, copiedHasWeapon, copiedWpShowBehind;
    private int copiedHitX, copiedHitY, copiedHitSound, copiedHitDamage;
    private int copiedWpX, copiedWpY, copiedWpRotation;
//    private int copiedDelay;
    
    private int lastcX, lastcY;
    private boolean wasFrameReplaced;
        
    
    
    private void setupAnimationCombo(){
        int charId = guide.getFakeCharId(manager.getCurrentCharacterId());
        int count = guide.getAnimsCount(charId);
        animationCombo.removeAllItems();
        for (int i = 0 ; i < count ; ++i){
            animationCombo.addItem(guide.getAnimName(charId, i));
        }
    }
    
    private void setupCharacterCombo(){
        int count = guide.getNumChars();
        characterCombo.removeAllItems();
        for (int i = 0 ; i < count ; ++i){
            characterCombo.addItem(i + 1 + " - " +guide.getCharName(i));
        }
    }
    
    private void updateTitle(){
        lib.anim.Character ch = manager.getCharacter();
        if (ch.wasModified()) setTitle(TITLE + " - "  + new File(romName).getName() + "*");
        else setTitle(TITLE + " - "  + new File(romName).getName());
    }
    
    
    private void verifyModifications(){
        if (manager == null) return;
        // check image modifications
        if (imagePanel.wasImageModified()){
            BufferedImage img = imagePanel.getImage();
            Animation anim = manager.getCharacter().getAnimation(currAnimation);
            anim.setImage(currFrame, img);
            manager.getCharacter().setModified(true);
            manager.getCharacter().setSpritesModified(true);
            anim.setSpritesModified(currFrame, true);
            dragImageRadio.setEnabled(false);
        }
    }
    
    private void changeFrame(int frameId){
        verifyModifications();
        if (manager != null && manager.getCharacter() != null){
            if (manager.getCharacter().wasModified()){
                saveRom();
            }
            setFrame(frameId);
        }
    }
    
    private void changeAnimation(int animId){
        verifyModifications();
        setAnimation(animId);
    }
    
    private void changeCharacter(int charId) throws IOException{
        verifyModifications();
        setCharacter(charId);
    }
    
    private void setFrame(int frameId){
        if (currFrame != frameId){
            wasFrameReplaced = false;
            imagePanel.updateGhost();
        }
        currFrame = frameId;
        lib.anim.Character ch = manager.getCharacter();
        AnimFrame animFrame = ch.getAnimFrame(currAnimation, currFrame);       
        HitFrame hitFrame = ch.getHitFrame(currAnimation, currFrame);
        WeaponFrame weaponFrame = ch.getWeaponFrame(currAnimation, currFrame);
        
        // update frame text
        final TitledBorder border = (TitledBorder)framePanel.getBorder();
        border.setTitle("Frame " + Integer.toString(currFrame+1));
        framePanel.repaint();

        // update animFrame fields:
        setField(delayField, animFrame.delay);
        setFieldAsHex(mapField, animFrame.mapAddress);
        setFieldAsHex(artField, animFrame.artAddress);

        // update hitFrame fields:
        if (hitFrame != null){
            setField(xField,hitFrame.x);
            setField(yField,hitFrame.y);
            setField(damageField,hitFrame.damage);
            setField(soundField,hitFrame.sound);
            setCheck(koCheck, hitFrame.knockDown);
            setCheck(hitCheck, hitFrame.isEnabled());
            if (hitFrame.isEnabled()){
                imagePanel.setHit(hitFrame.x, hitFrame.y, hitFrame.knockDown);
            } else imagePanel.removeHit();
        }else{
            setCheck(hitCheck, false);
            imagePanel.removeHit();
        }
        updateHitFrameEnabling();
        
        // update weaponFrame fields:
        if (weaponFrame != null){
            setField(wXField,weaponFrame.x);
            setField(wYField,-weaponFrame.y);
            setField(angleField,weaponFrame.angle);  
            setCheck(behindCheck, weaponFrame.showBehind);
            setCheck(weaponCheck, weaponFrame.isEnabled());
            if (weaponFrame.isEnabled()){
                imagePanel.setWeapon(weaponFrame.x, weaponFrame.y, weaponFrame.angle, weaponFrame.showBehind);
            }else imagePanel.removeWeapon();
        }else{
            imagePanel.removeWeapon();
        }
        updateWeaponFrameEnabling();
        
        // update imagePanel
        frameDelayCount = 0;
        setImage(manager.getImage(currAnimation, currFrame), manager.getShadow(currAnimation, currFrame));
        // update tittle
        updateTitle();     
        copyMenu.setEnabled(true);
        pasteMenu.setEnabled(copiedMap != 0);
    }
    
    private void setAnimation(int animId){
        if (currAnimation != animId) imagePanel.updateGhost();
        currAnimation = animId;
        try {
            manager.bufferAnimation(currAnimation);
        } catch (IOException ex) {
            showError("Unable to read animation graphics");
            ex.printStackTrace();
            return;
        }
        setComboSelection(animationCombo, currAnimation);
        lib.anim.Character ch = manager.getCharacter();
        int animSize = ch.getAnimation(currAnimation).getNumFrames();
        setField(sizeField,animSize);
        
        boolean isCompressed = ch.getAnimation(currAnimation).isCompressed();
        artField.setEnabled(!isCompressed);
        setEnable(toolsPanel,!isCompressed);
        setEnable(overridePanel,!isCompressed);
        setEnable(generatePanel,!isCompressed);
        inportMenu.setEnabled(!isCompressed);
        resizeAnimsMenu.setEnabled(!isCompressed);
        if (isCompressed) imagePanel.setMode(Mode.none);
//        uncompressMenu.setEnabled(isCompressed);
        
        // update current frame
        setFrame(0);
    }
    
    private void setCharacter(int charId) throws IOException{
        // save current art stuff:
        changeFrame(currFrame);  
        if (manager == null) return;
        lib.anim.Character ch = manager.getCharacter();
        if (ch != null && ch.wasModified()){
            //if (!askSaveRom()) return;
            saveRom();
        }
        int backupCurrAnim = currAnimation;
        int fakeId = guide.getFakeCharId(charId);
        final int type = guide.getType(fakeId);
        if (type == 1){
            AnimFrame.providedArtAddress = guide.getCompressedArtAddress(charId);
        }
        manager.setCharacter(charId, guide.getAnimsCount(fakeId), guide.getPaletteAddress(fakeId), type);
        compressedLabel.setVisible(type == 1);
        setComboSelection(characterCombo, fakeId);
        setupAnimationCombo();
        int numAnimations = manager.getCharacter().getNumAnimations();
        
        // update palette
        updatePalettePanels();
        
        // update current animation
        if (backupCurrAnim == -1) setAnimation(0);
        else if (backupCurrAnim >= numAnimations) setAnimation(numAnimations-1);
        else setAnimation(backupCurrAnim);
        
        updateGenAddress();
        speedMenu.setEnabled(charId < 4);
        nameMenu.setEnabled(charId < 4);
        portraitMenu.setEnabled(true);
    }
    
    
    private void guideChanged(){
        setupCharacterCombo();
    }
    
    private boolean openCustomGuide(){
        int returnVal = guideChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = guideChooser.getSelectedFile();
            try {
                guide = new Guide(file.getAbsolutePath());
                guideChanged();
                return true;
            } catch (FileNotFoundException ex) {
                showError("Guide file \'" + file.getName() + "\' not found");
            } catch (Exception ex) {
                showError("Unable to open guide \'" + file.getName() + "\'");
            }
        }
        return false;
    }  
    private boolean openDefaultGuide(){
        try{
            guide = new Guide(Guide.GUIDES_DIR + "default.txt");
            guideChanged();
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    private boolean setupManager(String romName) {
        Manager oldManager = manager;
        try {
            manager = new Manager(romName, guide.getAnimsListAddress(), guide.getHitsListAddress(), guide.getWeaponsListAddress(), guide.getPortraitsListAddress());
            changeCharacter(0);
        } catch (FileNotFoundException ex) {
            showError("File \'" + romName + "\' not found");
            manager = oldManager;
            return false;
        } catch (IOException ex) {
            showError("File \'" + romName + "\' is not a valid Streets of Rage 2 ROM");
            manager = oldManager;
            return false;
        }
        Manager newManager = manager;
        manager = oldManager;
        closeRom();
        manager = newManager;
        return true;
    }
    
    private void openRom(){
        // close rom first
        if (!askSaveRom()) return;
        
        // open rom
        int returnVal = romChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = romChooser.getSelectedFile();
            romName = file.getAbsolutePath();
            if (setupManager(romName)){
                setTitle(TITLE + " - "  + file.getName());
                updateEnablings();
            }
        }
        try {
            setCharacter(0);
        } catch (IOException ex) {
            showError("Unable to read first character");
        }
    }
    
    
    private void closeRom(){
        if (askSaveRom()){
            manager = null;
            copyMenu.setEnabled(false);
            pasteMenu.setEnabled(false);
            speedMenu.setEnabled(false);
            nameMenu.setEnabled(false);
            portraitMenu.setEnabled(false);
            resizeAnimsMenu.setEnabled(false);
//            uncompressMenu.setEnabled(false);
            imagePanel.setImage(null, null);
            imagePanel.setReplaceImage(null);
            imagePanel.removeHit();
            imagePanel.removeWeapon();
            updateEnablings();
        }
    }
    
    private boolean saveRom(){
        // save
         try {
            manager.save();
            updateTitle();
            return true;
        } catch (IOException ex) {
            showError("Unable to save rom");
            return false;
        }
    }
    
    private boolean askSaveRom(){
        if (guide == null || manager == null) return true;
        lib.anim.Character ch = manager.getCharacter();
        if (!ch.wasModified()) return true;
        int option = JOptionPane.showConfirmDialog(this,
            guide.getCharName(guide.getFakeCharId(manager.getCurrentCharacterId())) + " was modified.\n"
            + "Save changes?",
            "Character modified",
            JOptionPane.YES_NO_CANCEL_OPTION
        );
        if (option == JOptionPane.YES_OPTION){
            return saveRom();
        }else if (option != JOptionPane.NO_OPTION){
            // cancel or close, return false
            return false;
        }
        return true;
    }
    
    
    private void setRadiosOff(){
        pencilRadio.setSelected(false);
        brushRadio.setSelected(false);
        bucketRadio.setSelected(false);
        dragSpriteRadio.setSelected(false);
        dragImageRadio.setSelected(false);
        noneRadio.setSelected(false);
        pencilMenu.setSelected(false);
        brushMenu.setSelected(false);
        bucketMenu.setSelected(false);
        dragSpriteMenu.setSelected(false);
        dragImageMenu.setSelected(false);
        noneMenu.setSelected(false);
    }
    
    private static void setEnable(JComponent component, boolean enabled){
        if (component.isEnabled() == enabled) return;
        component.setEnabled(enabled);
        Component[] com = component.getComponents();  
        for (int a = 0; a < com.length; a++) {  
            if (com[a] instanceof JComponent)
                setEnable((JComponent)com[a], enabled);
            else com[a].setEnabled(enabled);
        } 
    }
    
    private void updateHitFrameEnabling(){
        lib.anim.Character ch = manager.getCharacter();
        HitFrame hitFrame = ch.getHitFrame(currAnimation, currFrame);
        boolean enabled = hitFrame != null && hitFrame.isEnabled();
        setEnable(hitPanel, enabled);
        hitCheck.setSelected(enabled);
        hitCheck.setEnabled(hitFrame != null);
        
    }
    
    private void updateWeaponFrameEnabling(){
        lib.anim.Character ch = manager.getCharacter();
        WeaponFrame weaponFrame = ch.getWeaponFrame(currAnimation, currFrame);
        boolean enabled = weaponFrame != null && weaponFrame.isEnabled();
        setEnable(weaponPanel, enabled);
        weaponCheck.setSelected(enabled);
        weaponCheck.setEnabled(weaponFrame != null);
    }
    
    private void updateEnablings(){
        setEnable(mainPanel, manager != null && guide != null);
        openRomMenu.setEnabled(guide != null);
        closeMenu.setEnabled(manager != null);
        inportMenu.setEnabled(manager != null);        
        exportMenu.setEnabled(manager != null);
        resizeAnimsMenu.setEnabled(manager != null);
        copyMenu.setEnabled(manager != null);
        pasteMenu.setEnabled(manager != null && copiedMap != 0);
        if (manager != null){
            updateHitFrameEnabling();
            updateWeaponFrameEnabling();
        }
    }
    
    private void sizeChanged(){
        if (inUse.contains(sizeField)) return; // in use, ignore input
        inUse.add(sizeField);
        lib.anim.Character ch = manager.getCharacter();
        Animation anim = ch.getAnimation(currAnimation);
        int maxSize = anim.getMaxNumFrames();
        int newSize = getIntFromField(sizeField, 1, maxSize);
        if (newSize == INVALID_INT){
            sizeField.setBackground(Color.red);
        } else{
            sizeField.setBackground(Color.white);
            if (newSize != anim.getNumFrames()){
                anim.setNumFrames(newSize);
                ch.setModified(true);
                changeAnimation(currAnimation);
            }
        }
        inUse.remove(sizeField);
    }
    private void delayChanged(){
        if (inUse.contains(delayField)) return; // in use, ignore input
        inUse.add(delayField);
        lib.anim.Character ch = manager.getCharacter();
        Animation anim = ch.getAnimation(currAnimation);
        AnimFrame frame = anim.getFrame(currFrame);
        int newDelay = getIntFromField(delayField, 1, 255);
        if (newDelay == INVALID_INT){
            delayField.setBackground(Color.red);
        } else{
            delayField.setBackground(Color.white);
            if (newDelay != frame.delay){
                frame.delay = newDelay;
                ch.setModified(true);
                refresh();
            }
        }
        inUse.remove(delayField);
    }
    private void hitXChanged(){
        if (inUse.contains(xField)) return; // in use, ignore input
        inUse.add(xField);
        lib.anim.Character ch = manager.getCharacter();
        HitFrame frame = ch.getHitFrame(currAnimation, currFrame);
        int newX = getIntFromField(xField, -127, 127);
        if (newX == INVALID_INT){
            xField.setBackground(Color.red);
        } else{
            xField.setBackground(Color.white);
            if (newX != frame.x){
                frame.x = newX;
                ch.setModified(true);
                refresh();
            }
        }
        inUse.remove(xField);
    }
    private void hitYChanged(){
        if (inUse.contains(yField)) return; // in use, ignore input
        inUse.add(yField);
        lib.anim.Character ch = manager.getCharacter();
        HitFrame frame = ch.getHitFrame(currAnimation, currFrame);
        int newY = getIntFromField(yField, -127, 127);
        if (newY == INVALID_INT){
            yField.setBackground(Color.red);
        } else{
            yField.setBackground(Color.white);
            if (newY != frame.y){
                frame.y = newY;
                ch.setModified(true);
                refresh();
            }
        }
        inUse.remove(yField);
    }
    private void hitSoundChanged(){
        if (inUse.contains(soundField)) return; // in use, ignore input
        inUse.add(soundField);
        lib.anim.Character ch = manager.getCharacter();
        HitFrame frame = ch.getHitFrame(currAnimation, currFrame);
        int newSound = getIntFromField(soundField, 0, 255);
        if (newSound == INVALID_INT){
            soundField.setBackground(Color.red);
        } else{
            soundField.setBackground(Color.white);
            if (newSound != frame.sound){
                frame.sound = newSound;
                ch.setModified(true);
                refresh();
            }
        }
        inUse.remove(soundField);
    }
    private void hitDamageChanged(){
        if (inUse.contains(damageField)) return; // in use, ignore input
        inUse.add(damageField);
        lib.anim.Character ch = manager.getCharacter();
        HitFrame frame = ch.getHitFrame(currAnimation, currFrame);
        int newDamage = getIntFromField(damageField, -127, 127);
        if (newDamage == INVALID_INT){
            damageField.setBackground(Color.red);
        } else{
            damageField.setBackground(Color.white);
            if (newDamage != frame.damage){
                frame.damage = newDamage;
                ch.setModified(true);
                refresh();
            }
        }
        inUse.remove(damageField);
    }
    private void weaponXChanged(){
        if (inUse.contains(wXField)) return; // in use, ignore input
        inUse.add(wXField);
        lib.anim.Character ch = manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(currAnimation, currFrame);
        int newX = getIntFromField(wXField, -127, 127);
        if (newX == INVALID_INT){
            wXField.setBackground(Color.red);
        } else{
            wXField.setBackground(Color.white);
            if (newX != frame.x){
                frame.x = newX;
                ch.setModified(true);
                refresh();
            }
        }
        inUse.remove(wXField);
    }
    private void weaponYChanged(){
        if (inUse.contains(wYField)) return; // in use, ignore input
        inUse.add(wYField);
        lib.anim.Character ch = manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(currAnimation, currFrame);
        int newY = getIntFromField(wYField, -127, 127);
        if (newY == INVALID_INT){
            wYField.setBackground(Color.red);
        } else{
            newY = -newY;
            wYField.setBackground(Color.white);
            if (newY != frame.y){
                frame.y = newY;
                ch.setModified(true);
                refresh();
            }
        }
        inUse.remove(wYField);
    }
    private void weaponAngleChanged(){
        if (inUse.contains(angleField)) return; // in use, ignore input
        inUse.add(angleField);
        lib.anim.Character ch = manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(currAnimation, currFrame);
        int newAngle = getIntFromField(angleField, -127, 127);
        if (newAngle == INVALID_INT || newAngle < 0 || newAngle > 7){
            if (frame.isEnabled())
                angleField.setBackground(Color.red);
        } else{
            angleField.setBackground(Color.white);
            if (newAngle != frame.angle){
                frame.angle = newAngle;
                ch.setModified(true);
                refresh();
            }
        }
        inUse.remove(angleField);
    }
    private void mapAddressChanged(){
        if (inUse.contains(mapField)) return; // in use, ignore input
        inUse.add(mapField);
        lib.anim.Character ch = manager.getCharacter();
        Animation anim = ch.getAnimation(currAnimation);
        AnimFrame frame = anim.getFrame(currFrame);
        long newMap = getHexFromField(mapField);
        if (newMap == INVALID_LONG){
            mapField.setBackground(Color.red);
        } else{
            mapField.setBackground(Color.white);
            if (newMap != frame.mapAddress){
                long oldMap = frame.mapAddress;
                frame.mapAddress = newMap;
                try {
                    manager.bufferAnimFrame(currAnimation, currFrame);
                    ch.setModified(true);
                    refresh();
                } catch (IOException ex) {
                    frame.mapAddress = oldMap;
                    mapField.setBackground(Color.red);
                }                
            }
        }
        inUse.remove(mapField);
    }
    private void artAddressChanged(){
        if (inUse.contains(artField)) return; // in use, ignore input
        inUse.add(artField);
        lib.anim.Character ch = manager.getCharacter();
        Animation anim = ch.getAnimation(currAnimation);
        AnimFrame frame = anim.getFrame(currFrame);
        long newArt = getHexFromField(artField);
        if (newArt == INVALID_LONG){
            artField.setBackground(Color.red);
        } else{
            artField.setBackground(Color.white);
            if (newArt != frame.artAddress){
                long oldArt = frame.artAddress;
                frame.artAddress = newArt;
                try {
                    manager.bufferAnimFrame(currAnimation, currFrame);
                    ch.setModified(true);
                    refresh();
                } catch (IOException ex) {
                    frame.artAddress = oldArt;
                    artField.setBackground(Color.red);
                }                
            }
        }
        inUse.remove(artField);
    }
    
    private void setupFields(){
        sizeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                sizeChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                sizeChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                sizeChanged();
            }
        });        
        delayField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                delayChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                delayChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                delayChanged();
            }
        });
        xField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                hitXChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                hitXChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitXChanged();
            }
        });
        yField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                hitYChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                hitYChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitYChanged();
            }
        });
        soundField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                hitSoundChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                hitSoundChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitSoundChanged();
            }
        });
        damageField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                hitDamageChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                hitDamageChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitDamageChanged();
            }
        });
        wXField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                weaponXChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                weaponXChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                weaponXChanged();
            }
        });
        wYField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                weaponYChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                weaponYChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                weaponYChanged();
            }
        });
        angleField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                weaponAngleChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                weaponAngleChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                weaponAngleChanged();
            }
        });
        mapField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                mapAddressChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                mapAddressChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                mapAddressChanged();
            }
        });
        artField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                artAddressChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                artAddressChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                artAddressChanged();
            }
        });
        
    }    
    private void setupFileChoosers(){
        romChooser.addChoosableFileFilter(new FileFilter(){
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return filename.endsWith(".bin");
            }
            @Override
            public String getDescription() {
                return "*.bin";
            }            
        });        
        guideChooser.addChoosableFileFilter(new FileFilter(){
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return filename.endsWith(".txt");
            }
            @Override
            public String getDescription() {
                return "*.txt";
            }            
        });
        resizerChooser.addChoosableFileFilter(new FileFilter(){
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return filename.endsWith(".txt");
            }
            @Override
            public String getDescription() {
                return "*.txt";
            }            
        });
        imageChooser.addChoosableFileFilter(new FileFilter(){
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return filename.endsWith(".png") || filename.endsWith(".gif") || filename.endsWith(".jpg") || filename.endsWith(".bmp");
            }
            @Override
            public String getDescription() {
                return "Image (*.png, *.gif, *.jpg, *.bmp)";
            }            
        });  
        imageSaver.addChoosableFileFilter(new FileFilter(){
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return filename.endsWith(".png");
            }
            @Override
            public String getDescription() {
                return "*.png";
            }            
        }); 
    }      
    private void initColorPanels(){
        colorPanels = new JPanel[16];
        colorPanels[0] = colorPanel1;
        colorPanels[1] = colorPanel2;
        colorPanels[2] = colorPanel3;
        colorPanels[3] = colorPanel4;
        colorPanels[4] = colorPanel5;
        colorPanels[5] = colorPanel6;
        colorPanels[6] = colorPanel7;
        colorPanels[7] = colorPanel8;
        colorPanels[8] = colorPanel9;
        colorPanels[9] = colorPanel10;
        colorPanels[10] = colorPanel11;
        colorPanels[11] = colorPanel12;
        colorPanels[12] = colorPanel13;
        colorPanels[13] = colorPanel14;
        colorPanels[14] = colorPanel15;
        colorPanels[15] = colorPanel16;
        selectionBorder = BorderFactory.createCompoundBorder(
                new LineBorder(java.awt.Color.white, 2),
                new LineBorder(java.awt.Color.black)
        );
        selectionBorder = BorderFactory.createCompoundBorder(
                new LineBorder(java.awt.Color.black), selectionBorder
        );
        colorPanel1.setBorder(selectionBorder);
        
    }
    private void preInitComponents(){
        lastcX = lastcY = -1;
        inUse = new HashSet<JTextField>();
        imagePanel = new ImagePanel(this);
        currentDirectory = new File(".").getAbsolutePath();
        romChooser = new JFileChooser(currentDirectory);
        romChooser.setDialogTitle("Open the \'Streets of Rage 2\' ROM to edit");
        guideChooser = new JFileChooser(currentDirectory + "/" + Guide.GUIDES_DIR);
        guideChooser.setDialogTitle("Open characters guide");
        imageChooser = new JFileChooser(currentDirectory);
        imageChooser.setDialogTitle("Open image");
        imageSaver = new JFileChooser(currentDirectory);
        imageSaver.setDialogTitle("Save image");
        resizerChooser = new JFileChooser(currentDirectory);
        resizerChooser.setDialogTitle("Open resizing script");
        
        try {            
            Image icon = ImageIO.read(new File("images/icon.png"));
            this.setIconImage(icon);
        } catch (IOException ex) { /* Ignore */ }
        
        timer = new Timer(1,this);
    }
    
    private void postInitComponents(){
        compressedLabel.setVisible(false);
        setupFields();                
        setupFileChoosers();
        scrollPanel.setWheelScrollingEnabled(false);
        scrollPanel.setAutoscrolls(true);

        initColorPanels();
        if (!openDefaultGuide()){
            openCustomGuide();
        }
        updateEnablings();
    }
    
    /**
     * Creates new form Gui
     */
    public Gui() {
        preInitComponents();
        initComponents();
        setVisible(true);
        postInitComponents();
    }
    
     private void setImagePanelScale(float newScale){
         // Save the previous coordinates
        float oldScale = imagePanel.getScale();
        Rectangle oldView = scrollPanel.getViewport().getViewRect();
        // resize the panel for the new zoom
        imagePanel.setScale(newScale);
        // calculate the new view position
        Point newViewPos = new Point();
        newViewPos.x = (int)(Math.max(0, (oldView.x + oldView.width / 2) * newScale / oldScale - oldView.width / 2));
        newViewPos.y = (int)(Math.max(0, (oldView.y + oldView.height / 2) * newScale / oldScale - oldView.height / 2));
        scrollPanel.getViewport().setViewPosition(newViewPos);
     }
    
     private void scaleImagePanel(float zoom){
        setImagePanelScale(imagePanel.getScale() + zoom);
    }
     
     private void showError(String text){
        String title = "Wops!";
        JOptionPane.showMessageDialog(this,
            text, title,
            JOptionPane.ERROR_MESSAGE
        );
    }
     
     private int getIntFromField(JTextField field, int lowerLim, int upperLim){
         String text = field.getText();
         if (text.isEmpty()) return INVALID_INT;
         try{
            int res = Integer.parseInt(text);
            if (res < lowerLim || res > upperLim) return INVALID_INT;
            return res;
         }catch(Exception e){
            return INVALID_INT;
        }
     }
     private int getPositiveIntFromField(JTextField field){
         return getIntFromField(field, 0, Integer.MAX_VALUE);
     }
     private int getIntFromField(JTextField field){
         String text = field.getText();
         if (text.isEmpty()) return INVALID_INT;
         try{
            return Integer.parseInt(text);
         }catch(Exception e){
            return INVALID_INT;
        }
     }
     private long getHexFromField(JTextField field){
         String text = field.getText();
         if (text.isEmpty()) return INVALID_LONG;
         try{
            return Long.parseLong(text, 16);
         }catch(Exception e){
            return INVALID_LONG;
        }
     }
     private void setField(JTextField field, String value){
         if (inUse.contains(field)) return; // in use, ignore
         if (!field.getText().equals(value)){
             inUse.add(field);
             field.setBackground(Color.white);
             field.setText(value);  
             inUse.remove(field);
         }
     }
     private void setField(JTextField field, int value){
         setField(field, Integer.toString(value));
     }
     private void setField(JTextField field, long value){
         setField(field, Long.toString(value));
     }
     private void setFieldAsHex(JTextField field, long value){
         setField(field, Long.toString(value, 16));
     }
     private void setCheck(JCheckBox check, boolean value){
         if (check.isSelected() != value)
            check.setSelected(value);
     }
     private void setComboSelection(JComboBox combo, int newIndex) {
        if (combo.getSelectedIndex() != newIndex){
            combo.setSelectedIndex(newIndex);
        }
    }
     
     private void setImage(BufferedImage image, BufferedImage shadow) {
         // don't even ask what this is... Revalidate etc didn't work
        JScrollBar horizontalScrollBar = scrollPanel.getHorizontalScrollBar();
        JScrollBar verticalScrollBar = scrollPanel.getVerticalScrollBar();
        int x = horizontalScrollBar.getValue();
        int y = verticalScrollBar.getValue();
        imagePanel.setImage(image, shadow);
        horizontalScrollBar.setValue(x+1);
        horizontalScrollBar.setValue(x);
        verticalScrollBar.setValue(y+1);
        verticalScrollBar.setValue(y);    
        dragImageRadio.setEnabled(false);
    }
     
     
    private void setNextAnimation() {
        int nextAnimation = currAnimation+1;
        if (nextAnimation >= manager.getCharacter().getNumAnimations()){
            nextAnimation = 0;
        }
        changeAnimation(nextAnimation);
    }
    
    private void setPreviousAnimation() {
        int previousAnimation = currAnimation-1;
        if (previousAnimation < 0){
            previousAnimation = manager.getCharacter().getNumAnimations()-1;
        }
        changeAnimation(previousAnimation);
    }
    
    private void setNextFrame() {
        int nextFrame = currFrame+1;
        if (nextFrame >= manager.getCharacter().getAnimation(currAnimation).getNumFrames()){
            nextFrame = 0;
        }
        changeFrame(nextFrame);
    }

    private void setPreviousFrame() {
        int previousFrame = currFrame+-1;
        if (previousFrame < 0){
            previousFrame = manager.getCharacter().getAnimation(currAnimation).getNumFrames()-1;
        }
        changeFrame(previousFrame);
    }
    
    private void updatePalettePanels(){
        Palette palette = manager.getPalette();
        if (palette == null){
            palette = new Palette();
        }
        for (int i = 0 ; i < 16 ; ++i){
            colorPanels[i].setBackground(new Color(palette.getColor(i)));
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
        jLabel13 = new javax.swing.JLabel();
        genAddressField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        genPaletteField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        openRomMenu = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        closeMenu = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        inportMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
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
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        resizeAnimsMenu = new javax.swing.JMenuItem();
        nameMenu = new javax.swing.JMenuItem();
        speedMenu = new javax.swing.JMenuItem();
        portraitMenu = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        copyMenu = new javax.swing.JMenuItem();
        pasteMenu = new javax.swing.JMenuItem();
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
        framePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Frame #", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Delay:");

        delayField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        delayField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        delayField.setText("0");

        hitPanel.setBackground(new java.awt.Color(236, 209, 127));
        hitPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hit", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N

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

        mapField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        mapField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mapField.setText("200");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Art Address:");

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
        weaponPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Weapon Point", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N

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
                .addContainerGap())
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
                .addComponent(animationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGroup(characterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(characterPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(characterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(compressedLabel))
                    .addComponent(animationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(backBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previousBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playToggle, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frontBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        colorsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Palette", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N
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
                    .addComponent(colorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(colorPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(colorPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(colorPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(colorPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(colorPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(colorPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(colorPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(colorPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(scrollPanel, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(colorsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
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
        characterPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "View", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weaponCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showFacedRightCheck)
                    .addComponent(showTileCheck))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, Short.MAX_VALUE)
                .addGroup(characterPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weaponCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showWeaponCheck)))
        );

        toolsPanel.setBackground(new java.awt.Color(230, 230, 235));
        toolsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tools", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

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
                    .addComponent(pencilRadio)
                    .addComponent(brushRadio)
                    .addComponent(bucketRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(toolsPanelLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(dragSpriteRadio))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dragImageRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(noneRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
        overridePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Override Art", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

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
        generatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Generate Sprite", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        hardReplaceButton.setText("Generate From Image");
        hardReplaceButton.setFocusable(false);
        hardReplaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hardReplaceButtonActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Address:");

        genAddressField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        genAddressField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        genAddressField.setText("200");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Palette Line:");

        genPaletteField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        genPaletteField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        genPaletteField.setText("0");

        javax.swing.GroupLayout generatePanelLayout = new javax.swing.GroupLayout(generatePanel);
        generatePanel.setLayout(generatePanelLayout);
        generatePanelLayout.setHorizontalGroup(
            generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generatePanelLayout.createSequentialGroup()
                .addGroup(generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generatePanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(generatePanelLayout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(genAddressField))
                            .addGroup(generatePanelLayout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(genPaletteField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(generatePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(hardReplaceButton)))
                .addGap(0, 25, Short.MAX_VALUE))
        );
        generatePanelLayout.setVerticalGroup(
            generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generatePanelLayout.createSequentialGroup()
                .addGroup(generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(genAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(generatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(genPaletteField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(characterPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(overridePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(toolsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(generatePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(characterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jMenu2.add(jSeparator3);

        resizeAnimsMenu.setText("Resize Animations");
        resizeAnimsMenu.setEnabled(false);
        resizeAnimsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resizeAnimsMenuActionPerformed(evt);
            }
        });
        jMenu2.add(resizeAnimsMenu);

        nameMenu.setText("Name");
        nameMenu.setEnabled(false);
        nameMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameMenuActionPerformed(evt);
            }
        });
        jMenu2.add(nameMenu);

        speedMenu.setText("Speed");
        speedMenu.setEnabled(false);
        speedMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedMenuActionPerformed(evt);
            }
        });
        jMenu2.add(speedMenu);

        portraitMenu.setText("Mini-Portrait");
        portraitMenu.setEnabled(false);
        portraitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portraitMenuActionPerformed(evt);
            }
        });
        jMenu2.add(portraitMenu);
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

    private void colorPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel2MousePressed
        colorPanelPressed(1);
    }//GEN-LAST:event_colorPanel2MousePressed

    private void colorPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel3MousePressed
        colorPanelPressed(2);
    }//GEN-LAST:event_colorPanel3MousePressed

    private void colorPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel4MousePressed
        colorPanelPressed(3);
    }//GEN-LAST:event_colorPanel4MousePressed

    private void colorPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel5MousePressed
        colorPanelPressed(4);
    }//GEN-LAST:event_colorPanel5MousePressed

    private void colorPanel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel6MousePressed
        colorPanelPressed(5);
    }//GEN-LAST:event_colorPanel6MousePressed

    private void colorPanel7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel7MousePressed
        colorPanelPressed(6);
    }//GEN-LAST:event_colorPanel7MousePressed

    private void colorPanel8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel8MousePressed
        colorPanelPressed(7);
    }//GEN-LAST:event_colorPanel8MousePressed

    private void colorPanel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel9MousePressed
        colorPanelPressed(8);
    }//GEN-LAST:event_colorPanel9MousePressed

    private void colorPanel10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel10MousePressed
        colorPanelPressed(9);
    }//GEN-LAST:event_colorPanel10MousePressed

    private void colorPanel11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel11MousePressed
        colorPanelPressed(10);
    }//GEN-LAST:event_colorPanel11MousePressed

    private void colorPanel12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel12MousePressed
        colorPanelPressed(11);
    }//GEN-LAST:event_colorPanel12MousePressed

    private void colorPanel13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel13MousePressed
        colorPanelPressed(12);
    }//GEN-LAST:event_colorPanel13MousePressed

    private void colorPanel14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel14MousePressed
        colorPanelPressed(13);
    }//GEN-LAST:event_colorPanel14MousePressed

    private void colorPanel15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel15MousePressed
        colorPanelPressed(14);
    }//GEN-LAST:event_colorPanel15MousePressed

    private void colorPanel16MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel16MousePressed
        colorPanelPressed(15);
    }//GEN-LAST:event_colorPanel16MousePressed

    private void colorPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel1MousePressed
        colorPanelPressed(0);
    }//GEN-LAST:event_colorPanel1MousePressed

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        scaleImagePanel(-0.2f*evt.getWheelRotation());
    }//GEN-LAST:event_formMouseWheelMoved

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        setImagePanelScale(1.f);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        setImagePanelScale(2.f);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        setImagePanelScale(6.f);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        setImagePanelScale(12.f);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void openRomMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openRomMenuActionPerformed
        openRom();
    }//GEN-LAST:event_openRomMenuActionPerformed

    private void closeMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuActionPerformed
        closeRom();
    }//GEN-LAST:event_closeMenuActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (openCustomGuide()){
            openRom();
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (askSaveRom()){
            closeRom();
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        setNextFrame();        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        setPreviousFrame();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void nextButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButActionPerformed
        setNextFrame();
    }//GEN-LAST:event_nextButActionPerformed

    private void previousButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButActionPerformed
        setPreviousFrame();
    }//GEN-LAST:event_previousButActionPerformed

    private void animationComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_animationComboActionPerformed
        int newAnim = animationCombo.getSelectedIndex();
        if (newAnim != currAnimation && newAnim >= 0){
            changeAnimation(newAnim);
        }
    }//GEN-LAST:event_animationComboActionPerformed

    private void frontButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frontButActionPerformed
        setNextAnimation();
    }//GEN-LAST:event_frontButActionPerformed

    private void backButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButActionPerformed
        setPreviousAnimation();
    }//GEN-LAST:event_backButActionPerformed

    private void animationComboKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_animationComboKeyPressed
        switch(evt.getKeyCode()){
            case java.awt.event.KeyEvent.VK_RIGHT:
                setNextFrame();
                break;
            case java.awt.event.KeyEvent.VK_LEFT:
                setPreviousFrame();
                break;
        }
    }//GEN-LAST:event_animationComboKeyPressed

    private void characterComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_characterComboActionPerformed
        if (manager == null || guide == null) return;
        int newChar = guide.getRealCharId(characterCombo.getSelectedIndex());
        if (newChar != manager.getCurrentCharacterId() && newChar >= 0){
            try {
                changeCharacter(newChar);
            } catch (IOException ex) {
                showError("Unable to read '" + characterCombo.getSelectedItem() + "' character");
            }
        }
    }//GEN-LAST:event_characterComboActionPerformed

    private void playToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playToggleActionPerformed
        if (playToggle.isSelected()){
            timer.start();
            playToggle.setText("[]");
        }else{
            timer.stop();
            playToggle.setText(">");            
        }
//        lastUsed.requestFocus();
    }//GEN-LAST:event_playToggleActionPerformed

    private void showHitsCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHitsCheckActionPerformed
        imagePanel.showHit(showHitsCheck.isSelected());
    }//GEN-LAST:event_showHitsCheckActionPerformed

    private void showCenterCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showCenterCheckActionPerformed
        imagePanel.showGhost(showCenterCheck.isSelected());
    }//GEN-LAST:event_showCenterCheckActionPerformed

    private void showTileCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTileCheckActionPerformed
        imagePanel.showShadow(showTileCheck.isSelected());
    }//GEN-LAST:event_showTileCheckActionPerformed

    private void hitCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hitCheckActionPerformed
        lib.anim.Character ch = manager.getCharacter();
        HitFrame hitFrame = ch.getHitFrame(currAnimation, currFrame);
        boolean selected = hitCheck.isSelected();
        if (hitFrame.isEnabled() != selected){
            hitFrame.setEnabled(selected);
            ch.setModified(true);
            refresh();
        }
    }//GEN-LAST:event_hitCheckActionPerformed

    private void koCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_koCheckActionPerformed
        lib.anim.Character ch = manager.getCharacter();
        HitFrame hitFrame = ch.getHitFrame(currAnimation, currFrame);
        boolean selected = koCheck.isSelected();
        if (hitFrame.knockDown  != selected){
            hitFrame.knockDown = selected;
            ch.setModified(true);
            refresh();
        }
    }//GEN-LAST:event_koCheckActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (askSaveRom()){
            closeRom();
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void softReplaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_softReplaceButtonActionPerformed
        int returnVal = imageChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = imageChooser.getSelectedFile();
            BufferedImage replaceImg;
            try {
                replaceImg = ImageIO.read(file);                
                replaceImg = processReplaceImg(replaceImg);
                dragImageRadio.setEnabled(true);
                imagePanel.setReplaceImage(replaceImg);
            } catch (IOException ex) {
                showError("Unable to read image file: " + file.getName());
                imagePanel.setReplaceImage(null);
                dragImageRadio.setEnabled(false);
            }
            manager.getCharacter().getAnimation(currAnimation).setSpritesModified(currFrame, true);
            manager.getCharacter().setSpritesModified(true);
            manager.getCharacter().setModified(true);
        }
    }//GEN-LAST:event_softReplaceButtonActionPerformed

    private void hardReplaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hardReplaceButtonActionPerformed
        int returnVal = imageChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = imageChooser.getSelectedFile();
            BufferedImage replaceImg;
            try {
                manager.getCharacter().setModified(true);
                manager.getCharacter().setSpritesModified(true);
                manager.getCharacter().getAnimation(currAnimation).setSpritesModified(currFrame, true);
                replaceImg = ImageIO.read(file);
                if (imagePanel.isFacedRight()) replaceImg = ImagePanel.flipImage(replaceImg);
                replaceImg = processReplaceImg(replaceImg);
                
                if (lastcX == -1){
                    lastcX = (int)(replaceImg.getWidth()*0.5);
                    lastcY = (int)(replaceImg.getHeight()*0.95);
                }
                int cx = lastcX;
                int cy = lastcY;
//                int left = trunkLeft(replaceImg);
//                int right = trunkRight(replaceImg);
//                int top = trunkTop(replaceImg);
//                int bottom = trunkBottom(replaceImg);       
//                replaceImg = replaceImg.getSubimage(left, top, right - left, bottom - top);
//                cx-=left; cy-=top;
                replaceSprite(replaceImg, currAnimation, currFrame,cx,cy);
                wasFrameReplaced = true;
                
                try {
                    manager.bufferAnimFrame(currAnimation, currFrame);
                } catch (IOException ex) {
                    showError("Unable to save the generated sprite");
                }
                dragImageRadio.setEnabled(false);
        
                hardRefresh();
            } catch (IOException ex) {
                showError("Unable to read image file: " + file.getName());
            }
        }
    }//GEN-LAST:event_hardReplaceButtonActionPerformed

    private void pencilRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pencilRadioActionPerformed
        setRadiosOff();
        pencilRadio.setSelected(true);
        imagePanel.setMode(Mode.pencil);
    }//GEN-LAST:event_pencilRadioActionPerformed

    private void brushRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brushRadioActionPerformed
        setRadiosOff();
        brushRadio.setSelected(true);
        imagePanel.setMode(Mode.brush);
    }//GEN-LAST:event_brushRadioActionPerformed

    private void bucketRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bucketRadioActionPerformed
        setRadiosOff();
        bucketRadio.setSelected(true);
        imagePanel.setMode(Mode.bucket);
    }//GEN-LAST:event_bucketRadioActionPerformed

    private void dragSpriteRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dragSpriteRadioActionPerformed
        setRadiosOff();
        dragSpriteRadio.setSelected(true);
        imagePanel.setMode(Mode.dragSprite);
    }//GEN-LAST:event_dragSpriteRadioActionPerformed

    private void dragImageRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dragImageRadioActionPerformed
        setRadiosOff();
        dragImageRadio.setSelected(true);
        imagePanel.setMode(Mode.dragImage);
    }//GEN-LAST:event_dragImageRadioActionPerformed

    private void noneRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noneRadioActionPerformed
        setRadiosOff();
        noneRadio.setSelected(true);
        imagePanel.setMode(Mode.none);
    }//GEN-LAST:event_noneRadioActionPerformed

    private void sizeRadioMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sizeRadioMenu1ActionPerformed
        setSizeRadioMenusOff();
        sizeRadioMenu1.setSelected(true);
        imagePanel.setBrushSize(3);
    }//GEN-LAST:event_sizeRadioMenu1ActionPerformed

    private void sizeRadioMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sizeRadioMenu2ActionPerformed
        setSizeRadioMenusOff();
        sizeRadioMenu2.setSelected(true);
        imagePanel.setBrushSize(5);
    }//GEN-LAST:event_sizeRadioMenu2ActionPerformed

    private void sizeRadioMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sizeRadioMenu3ActionPerformed
        setSizeRadioMenusOff();
        sizeRadioMenu3.setSelected(true);
        imagePanel.setBrushSize(10);
    }//GEN-LAST:event_sizeRadioMenu3ActionPerformed

    private void bucketMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bucketMenuActionPerformed
        bucketRadioActionPerformed(null);
    }//GEN-LAST:event_bucketMenuActionPerformed

    private void pencilMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pencilMenuActionPerformed
        pencilRadioActionPerformed(null);
    }//GEN-LAST:event_pencilMenuActionPerformed

    private void brushMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brushMenuActionPerformed
        brushRadioActionPerformed(null);
    }//GEN-LAST:event_brushMenuActionPerformed

    private void dragSpriteMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dragSpriteMenuActionPerformed
        dragSpriteRadioActionPerformed(null);
    }//GEN-LAST:event_dragSpriteMenuActionPerformed

    private void dragImageMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dragImageMenuActionPerformed
        dragImageRadioActionPerformed(null);
    }//GEN-LAST:event_dragImageMenuActionPerformed

    private void noneMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noneMenuActionPerformed
        noneRadioActionPerformed(null);
    }//GEN-LAST:event_noneMenuActionPerformed

    private void showWeaponCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showWeaponCheckActionPerformed
        weaponCombo.setEnabled(showWeaponCheck.isSelected());
        imagePanel.showWeapon(showWeaponCheck.isSelected());
    }//GEN-LAST:event_showWeaponCheckActionPerformed

    private void weaponCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponCheckActionPerformed
        lib.anim.Character ch = manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(currAnimation, currFrame);
        boolean selected = weaponCheck.isSelected();
        if (frame.isEnabled() != selected){
            frame.setEnabled(selected);
            ch.setModified(true);
            refresh();
        }
    }//GEN-LAST:event_weaponCheckActionPerformed

    private void weaponComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponComboActionPerformed
        imagePanel.setWeaponPreview(weaponCombo.getSelectedIndex());
    }//GEN-LAST:event_weaponComboActionPerformed

    private void behindCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_behindCheckActionPerformed
        lib.anim.Character ch = manager.getCharacter();
        WeaponFrame frame = ch.getWeaponFrame(currAnimation, currFrame);
        boolean selected = behindCheck.isSelected();
        if (frame.showBehind != selected){
            frame.showBehind = selected;
            ch.setModified(true);
            refresh();
        }
    }//GEN-LAST:event_behindCheckActionPerformed

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

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        JOptionPane.showMessageDialog(this,
            TITLE + "\n" +
            "© Gil Costa 2012\n\n" +
            "Acknowledgment on derived work\n"+
            "would be appreciated but is not required\n\n"+
            "Pk2 is free software. The author can not be held responsible\nfor any illicit use of this program.\n",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void spriteSheetMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spriteSheetMenuActionPerformed
        exportSpriteSheet();
    }//GEN-LAST:event_spriteSheetMenuActionPerformed

    private void showFacedRightCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFacedRightCheckActionPerformed
        imagePanel.setFacedRight(showFacedRightCheck.isSelected());
    }//GEN-LAST:event_showFacedRightCheckActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        int charId = manager.getCurrentCharacterId();        
        String charName = guide.getCharName(guide.getFakeCharId(charId));
        int returnVal = imageChooser.showOpenDialog(this); 
        BufferedImage replaceImg;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = imageChooser.getSelectedFile();            
            try {
                replaceImg = ImageIO.read(file);                
                replaceImg = processReplaceImg(replaceImg);
            }catch(IOException e){
                showError("Unable to read image " + file.getName());
                return;
            }
        }else return;
            
        JTextField columnsField = new JTextField();
        JTextField rowsField = new JTextField();
        JTextField cxField = new JTextField();
        JTextField cyField = new JTextField();
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Number of columns:"),
            columnsField,
            new JLabel("Number of rows:"),
            rowsField,
            new JLabel("Sprite center X:"),
            cxField,
            new JLabel("Sprite center Y:"),
            cyField
        };
        int res = JOptionPane.showConfirmDialog(null, inputs, charName + "art replacer", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        int columns = getIntFromField(columnsField, 1, 9999);
        int rows = getIntFromField(rowsField, 1, 9999);
        int cx = getIntFromField(cxField, 0, 256);
        int cy = getIntFromField(cyField, 0, 256);
        if (columns == INVALID_INT || rows == INVALID_INT || cx == INVALID_INT || cy == INVALID_INT){
            showError("Invalid columns/rows/center");
            return;
        }
        importSpriteReplacer(replaceImg, columns, rows, cx, cy);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int charId = manager.getCurrentCharacterId();        
        String charName = guide.getCharName(guide.getFakeCharId(charId));
        int returnVal = imageChooser.showOpenDialog(this); 
        BufferedImage replaceImg;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = imageChooser.getSelectedFile();            
            try {
                replaceImg = ImageIO.read(file);                
                replaceImg = processReplaceImg(replaceImg);
            }catch(IOException e){
                showError("Unable to read image " + file.getName());
                return;
            }
        }else return;
            
        JTextField columnsField = new JTextField();
        JTextField rowsField = new JTextField();
        JTextField cxField = new JTextField();
        JTextField cyField = new JTextField();
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Number of columns:"),
            columnsField,
            new JLabel("Number of rows:"),
            rowsField,
            new JLabel("Sprite center X:"),
            cxField,
            new JLabel("Sprite center Y:"),
            cyField
        };
        int res = JOptionPane.showConfirmDialog(null, inputs, charName + "art replacer", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        int columns = getIntFromField(columnsField, 1, 9999);
        int rows = getIntFromField(rowsField, 1, 9999);
        int cx = getIntFromField(cxField, 0, 256);
        int cy = getIntFromField(cyField, 0, 256);
        if (columns == INVALID_INT || rows == INVALID_INT || cx == INVALID_INT || cy == INVALID_INT){
            showError("Invalid columns/rows/center");
            return;
        }
        importSpriteGenerator(replaceImg, columns, rows, cx, cy);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void nameMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameMenuActionPerformed
        String currName = "";
        try{
            currName = manager.readName();
        }catch(IOException e){
            showError("Unable to read character name");
            return;
        }
        while (!currName.isEmpty() && currName.endsWith(" ")) currName = currName.substring(0,currName.length()-1);
        JTextField speedField = new JTextField();
        speedField.setText(currName);
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Type a new name for " + currName + " (max 5 characters)"),
            new JLabel("Allowed chars: A-Z space ! \" ' ( ) , . / * ? ©"),
            speedField
        };
        int res = JOptionPane.showConfirmDialog(null, inputs, "Speed modifier", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION){
            String newName = speedField.getText();
            newName = newName.toUpperCase();
            if (newName.length() > 5){
                showError("Name is too big");
                return;
            }
            for (char c:newName.toCharArray()){
                switch (c){
                    case ' ': continue;
                    case '!': continue;
                    case '"': continue;
                    case '\'': continue;
                    case '(': continue;
                    case ')': continue;
                    case ',': continue;
                    case '.': continue;
                    case '/': continue;
                    case '*': continue;
                    case '?': continue;
                    case '©': continue;
                    default:
                        if (c >= '0' && c <= '9') continue;
                        else if (c >= 'A' && c <= 'Z') continue;
                        showError("Name contains invalid characters");
                        return;
                }
            }
            try{
                manager.writeName(newName);
            }catch(IOException e){
                showError("Unable to write character name");
                return;
            }
        }
    }//GEN-LAST:event_nameMenuActionPerformed

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

    private void copyMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuActionPerformed
        copiedMap = getHexFromField(mapField);
        copiedArt = getHexFromField(artField);
        copiedHasHit = hitCheck.isSelected();
        copiedKnockDown = koCheck.isSelected();
        copiedHasWeapon = weaponCheck.isSelected();
        copiedWpShowBehind = behindCheck.isSelected();
        copiedHitX = getIntFromField(xField);
        copiedHitY = getIntFromField(yField);
        copiedHitSound = getIntFromField(soundField);
        copiedHitDamage = getIntFromField(damageField);     
        copiedWpX = getIntFromField(wXField);
        copiedWpY = getIntFromField(wYField);
        copiedWpRotation = getIntFromField(angleField);
//        copiedDelay = getIntFromField(delayField);
        pasteMenu.setEnabled(true);
    }//GEN-LAST:event_copyMenuActionPerformed

    private void pasteMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenuActionPerformed
        setFieldAsHex(mapField, copiedMap);  
        mapAddressChanged();
        setFieldAsHex(artField, copiedArt);
        artAddressChanged();
                
        if (hitCheck.isEnabled()){
            hitCheck.setSelected(copiedHasHit);
            hitCheckActionPerformed(null);
            koCheck.setSelected(copiedKnockDown);
            koCheckActionPerformed(null);
            setField(xField, copiedHitX);
            hitXChanged();
            setField(yField, copiedHitY);
            hitYChanged();
            setField(soundField, copiedHitSound);
            hitSoundChanged();
            setField(damageField, copiedHitDamage);
            hitDamageChanged();
        }
        if (weaponCheck.isEnabled()){
            weaponCheck.setSelected(copiedHasWeapon);
            weaponCheckActionPerformed(null);
            behindCheck.setSelected(copiedWpShowBehind);
            behindCheckActionPerformed(null);
            setField(wXField, copiedWpX);
            weaponXChanged();
            setField(wYField, copiedWpY);
            weaponYChanged();
            setField(angleField, copiedWpRotation);
            weaponAngleChanged();
        }
//        setField(delayField,copiedDelay); 
//        delayChanged();
    }//GEN-LAST:event_pasteMenuActionPerformed

    private void spriteSheetMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spriteSheetMenu1ActionPerformed
        exportIndividualFrames();
    }//GEN-LAST:event_spriteSheetMenu1ActionPerformed

    private void resizeAnimsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resizeAnimsMenuActionPerformed
        int charId = manager.getCurrentCharacterId();        
        lib.anim.Character c = manager.getCharacter();
        HashSet<Animation> processed = new HashSet<Animation>();
        int totalFrames = 0;
        for (int i = 0 ; i < c.getNumAnimations() ; ++i){
            Animation anim = c.getAnimation(i);
            if (!processed.contains(anim)){
                totalFrames+=anim.getMaxNumFrames();
                processed.add(anim);
            }
        }
        
        int returnVal = resizerChooser.showOpenDialog(this);        
        if (returnVal != JFileChooser.APPROVE_OPTION) return;
        File file = resizerChooser.getSelectedFile();
        Scanner sc;
        try{sc = new Scanner(file);
        }catch(Exception e){
            showError("Unable to open file " + file.getName());
            return;
        }
        ArrayList<Integer> sizes = new ArrayList<Integer>(c.getNumAnimations());
        ArrayList<Integer> wps = new ArrayList<Integer>(c.getNumAnimations());
        ArrayList<Boolean> hit = new ArrayList<Boolean>(c.getNumAnimations()-lib.anim.Character.FIRST_HIT_ANIM);
        int totalProvided = 0;
        int totalHits = 0;
        int totalWps = 0;
        while (sc.hasNext()){
            int s;
            try{
                s = sc.nextInt();       // read size
                sizes.add(s);
                int hasWp;
                hasWp = sc.nextInt();   // read has weapon
                wps.add(hasWp);  
                boolean hasHit = false;
                if (sizes.size()-1 >= lib.anim.Character.FIRST_HIT_ANIM){                   
                    if (hasWp < 2){
                        hasHit = sc.nextInt() != 0; // read has hit
                    }
                    hit.add(hasHit);                    
                } 
                if (s >0){              // size > 0, count
                    totalProvided += s; 
                    if (hasWp == 1) totalWps += s;
                    if (hasHit) totalHits += s;
                }else if (s < 0){       // size < 0, maybe count weapons & hits but... whatever
//                    if (hasWp == 0 && c.getWeaponFrame(sizes.size()-1, 0) != null) totalWps += -s;
//                    if (!hasHit && c.getHitFrame(sizes.size()-1, 0) != null) totalHits += -s;
                }
            }catch(Exception e){
                showError("Invalid size values");
                e.printStackTrace();
                return;
            }
        }
        int numAnims = c.getNumAnimations();
        int numHits = c.getHitsSize();
        int numWeapons = c.getWeaponsSize();
        System.out.println(totalProvided + "-" + totalFrames + ", " + totalHits + "-" + numHits + ", " + totalWps + "-" + numWeapons);
        if (sizes.isEmpty()){ showError("Empty input"); return; }
        if (sizes.size() != numAnims){ showError("Number of animations mismatch, \nGot " + sizes.size() + ", expected " + numAnims); return; }
        if (totalProvided > totalFrames){ showError("Total number of frames exceeds the available space\nGot " + totalProvided + ", max allowed " + totalFrames); return; }
        if (totalHits > numHits){ showError("Total number of frames with collision exceeds the available space\nGot " + totalHits + ", max allowed " + numHits); return; }
        if (totalWps > numWeapons){ showError("Total number of frames with weapons exceeds the available space\nGot " + totalWps + ", max allowed " + numWeapons); return; }
        resizeAnimations(sizes, wps, hit);
    }//GEN-LAST:event_resizeAnimsMenuActionPerformed

    private void setSizeRadioMenusOff(){
        sizeRadioMenu1.setSelected(false);
        sizeRadioMenu2.setSelected(false);
        sizeRadioMenu3.setSelected(false);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){ /* Ignore */ }

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui();
            }
//                try{
//                    new Gui();
//                }catch(Throwable e){
//                    try {
//                        FileOutputStream dos = new FileOutputStream("log.txt");
//                        e.printStackTrace(new PrintStream(dos)); 
//                        dos.close();
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
        });
    }
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
    private javax.swing.JTextField genAddressField;
    private javax.swing.JTextField genPaletteField;
    private javax.swing.JPanel generatePanel;
    private javax.swing.JButton hardReplaceButton;
    private javax.swing.JCheckBox hitCheck;
    private javax.swing.JPanel hitPanel;
    private javax.swing.JMenu inportMenu;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
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
    private javax.swing.JRadioButtonMenuItem pencilMenu;
    private javax.swing.JRadioButton pencilRadio;
    private javax.swing.JToggleButton playToggle;
    private javax.swing.JPanel playerPanel;
    private javax.swing.JMenuItem portraitMenu;
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
    private javax.swing.JMenuItem speedMenu;
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

    @Override
    public void actionPerformed(ActionEvent e) {        
        if (guide == null || manager == null || currAnimation < 0) return;
        
        lib.anim.Character ch = manager.getCharacter();
        AnimFrame animFrame = ch.getAnimFrame(currAnimation, currFrame);
        
        if (frameDelayCount++ >= animFrame.delay){
            frameDelayCount = 0;
            setNextFrame();
        }
    }

    @Override
    public void hitPositionChanged(int newX, int newY) {
        if (imagePanel.isFacedRight()) newX*=-1;
        if (newX < -127) newX = -127;
        if (newX >  127) newX =  127;
        if (newY < -127) newY = -127;
        if (newY >  127) newY =  127;
        setField(xField, newX);
        hitXChanged();        
        setField(yField, newY);
        hitYChanged();
    }
    
    private void colorPanelPressed(int id){
        JPanel panel = colorPanels[id];
        if (id == 0)
            imagePanel.setColor(null);
        else imagePanel.setColor(panel.getBackground());
        colorPanels[selectedColor].setBorder(null);
        selectedColor = id;
        colorPanels[selectedColor].setBorder(selectionBorder);
        
    }

    @Override
    public void artChanged() {
        manager.getCharacter().setModified(true);
        updateTitle();
    }
    
    
    private int findTransparency(BufferedImage img){
        TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
        for (int x = 0 ; x < img.getWidth() ; ++x){
            int val = img.getRGB(x, 0);
            if (((val>>24) & 0xff) == 0) return val;
            Integer count = map.get(val);
            if (count == null) count = 0;
            map.put(val, count+1);
        }
        for (int x = 0 ; x < img.getWidth() ; ++x){
            int val = img.getRGB(x, img.getHeight()-1);
            if (((val>>24) & 0xff) == 0) return val;
            Integer count = map.get(val);
            if (count == null) count = 0;
            map.put(val, count+1);
        }
        for (int y = 0 ; y < img.getHeight() ; ++y){
            int val = img.getRGB(0, y);
            if (((val>>24) & 0xff) == 0) return val;
            Integer count = map.get(val);
            if (count == null) count = 0;
            map.put(val, count+1);
        }
        for (int y = 0 ; y < img.getHeight() ; ++y){
            int val = img.getRGB(img.getWidth()-1, y);
            if (((val>>24) & 0xff) == 0) return val;
            Integer count = map.get(val);
            if (count == null) count = 0;
            map.put(val, count+1);
        }
        int maxCount = 0;
        int moda = 0;
        for (Entry<Integer,Integer> entry:map.entrySet()){
            if (entry.getValue() > maxCount){
                moda = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        return moda;
    }
    
    private BufferedImage processReplaceImg(BufferedImage replaceImg){
        return processReplaceImg(replaceImg, true);
    }

    private BufferedImage processReplaceImg(BufferedImage replaceImg, boolean transp) {
        Palette palette = manager.getPalette();
        BufferedImage res = new BufferedImage(replaceImg.getWidth(), replaceImg.getHeight(),BufferedImage.TYPE_INT_ARGB);
        int transparency = 0;
        if (transp) transparency = findTransparency(replaceImg);
        // TODO: check palette at start
        for (int x = 0 ; x < replaceImg.getWidth() ; ++x){
            for (int y = 0 ; y < replaceImg.getHeight() ; ++y){
                int val = replaceImg.getRGB(x, y);
                Color c = new Color(val);
                if (val != transparency && ((val>>24) & 0xff) != 0){
                    float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                    c = nearestColor(hsb,palette);
                    res.setRGB(x, y, c.getRGB());
                }
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
        for (int i = 15 ; i > 0 ; --i){
            Color p = new Color(palette.getColor(i));
            float[] phsb = Color.RGBtoHSB(p.getRed(), p.getGreen(), p.getBlue(), null);
            float dist = (float)Math.sqrt(
                    Math.pow(hsb[0]-phsb[0],2)+
                    Math.pow(hsb[1]-phsb[1],2)+
                    Math.pow(hsb[2]-phsb[2],2)
            );
            if (dist < bestDist){                
                index = i;
                bestDist = dist;
            }
        }
        return new Color(palette.getColor(index));
    }
    
    
    private boolean isTile(BufferedImage img, int xi, int yi){
        xi*=8;
        yi*=8;
        for (int i = 0 ; i < 8 ; ++i){
            int x = xi+i;
            if (x >= img.getWidth()) continue;
            for (int j = 0 ; j < 8 ; ++j){
                int y = yi+j;
                if (y >= img.getHeight()) break;
                int val = img.getRGB(x, y);
                if (((val>>24) & 0xff) != 0)
                    return true;
            }
        }
        return false;
    }
    
    private void updateGenAddress(){
        try {
            long mapAddress = manager.romSize();
            setFieldAsHex(genAddressField, mapAddress);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private Piece optimizedPiece1(boolean[][] mx, boolean[][] done, int xi, int yi){
        if (done[xi][yi]) return null;
        int sum = 0;
        int lx = 0;
        int ly = 0;
        int maxX = Math.min(4, 32-xi);
        int maxY = Math.min(4, 32-yi);
        for (int i = 0 ; i < maxX ; ++i){
            int x = xi+i;
            if (!mx[x][0] || done[x][0]) break;
            for (int j = 0 ; j < maxY ; ++j){
                int y = yi+j;
                if (!mx[x][y] || done[x][y]){
                    maxY = j;
                    break;
                }
                int locSum = i*j;
                if (locSum > sum){
                    sum = locSum;
                    lx = i;
                    ly = j;
                }
            }
        }
        Piece piece = new Piece(lx+1, ly+1);
        for (int i = 0 ; i < lx+1 ; ++i){
            for (int j = 0 ; j < lx+1 ; ++j){
                done[xi+i][yi+j] = true;
                piece.setTile(i, j, new Tile());
            }
        }
        return piece;
    }
    
    private boolean check(boolean[][] mx, int x, int y, int width, int height){
        if (x + width > 32) return false;
        if (y + height > 32) return false;
        for (int i = 0; i < width ; ++i){
            for (int j = 0; j < height ; ++j){
                if (!mx[x+i][y+j]) return false;
            }
        }
        return true;
    }
    
    // worst code ever (shedule issues)
    private boolean optimizePieces(boolean[][] mx, Piece[][] res){
        int bestX = 0, bestY = 0, bestWidth = 0, bestHeight = 0;
        int size = 0;
        for (int x = 0 ; x < 32 ; ++x){
            for (int y = 0 ; y < 32 ; ++y){
                if (check(mx,x,y,1,1)){
                    if (check(mx,x,y,4,4)){
                        bestX = x; bestY = y; bestWidth = 4; bestHeight = 4;
                        size = bestWidth*bestHeight; 
                        break;
                    }
                    if (size >= 12) break;
                    if (check(mx,x,y,4,3)){
                        bestX = x; bestY = y; bestWidth = 4; bestHeight = 3;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (check(mx,x,y,3,4)){
                        bestX = x; bestY = y; bestWidth = 3; bestHeight = 4;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (size >= 9) break;
                    if (check(mx,x,y,3,3)){
                        bestX = x; bestY = y; bestWidth = 3; bestHeight = 3;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (size >= 8) break;
                    if (check(mx,x,y,4,2)){
                        bestX = x; bestY = y; bestWidth = 4; bestHeight = 2;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (check(mx,x,y,2,4)){
                        bestX = x; bestY = y; bestWidth = 2; bestHeight = 4;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (size >= 6) break;
                    if (check(mx,x,y,3,2)){
                        bestX = x; bestY = y; bestWidth = 3; bestHeight = 2;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (check(mx,x,y,2,3)){
                        bestX = x; bestY = y; bestWidth = 2; bestHeight = 3;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (size >= 4) break;
                    if (check(mx,x,y,4,1)){
                        bestX = x; bestY = y; bestWidth = 4; bestHeight = 1;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (check(mx,x,y,1,4)){
                        bestX = x; bestY = y; bestWidth = 1; bestHeight = 4;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (check(mx,x,y,2,2)){
                        bestX = x; bestY = y; bestWidth = 2; bestHeight = 2;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (size >= 3) break;
                    if (check(mx,x,y,3,1)){
                        bestX = x; bestY = y; bestWidth = 3; bestHeight = 1;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (check(mx,x,y,1,3)){
                        bestX = x; bestY = y; bestWidth = 1; bestHeight = 3;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (size >= 2) break;
                    if (check(mx,x,y,2,1)){
                        bestX = x; bestY = y; bestWidth = 2; bestHeight = 1;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (check(mx,x,y,1,2)){
                        bestX = x; bestY = y; bestWidth = 1; bestHeight = 2;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                    if (size >= 1) break;
                    else{
                        bestX = x; bestY = y; bestWidth = 1; bestHeight = 1;
                        size = bestWidth*bestHeight;
                        continue;
                    }
                }
            }
            if (size == 16) break;
        }
//        System.out.println("( " + bestX + ", " + bestY + "), " + bestWidth + "x" + bestHeight);
        if (size == 0) return false;
        Piece piece = new Piece(bestWidth, bestHeight);
        for (int i = 0 ; i < bestWidth ; ++i){
            for (int j = 0 ; j < bestHeight ; ++j){
                piece.setTile(i, j, new Tile());     
                mx[bestX+i][bestY+j] = false;
            }
        }
        res[bestX][bestY] = piece;
        return true;
    }
    
//    private Piece[][] genPieces(boolean[][] mx){
//        Piece[][] res = new Piece[32][32];
//        boolean[][] done = new boolean[32][32];
//        for (int x = 0 ; x < 32 ; ++x){
//            for (int y = 0 ; y < 32 ; ++y){
//                if (mx[x][y]){
//                    //Piece piece = optimizedPiece(mx, done, x, y);
//                    Piece piece = new Piece(1,1);
//                    piece.setTile(0, 0, new Tile());
//                    res[x][y] = piece;
//                }
//            }
//        }
//        return res;
//    }
    
    private Piece[][] genPieces(boolean[][] mx){
        Piece[][] res = new Piece[32][32];
        while(optimizePieces(mx, res));
        return res;
    }
    
    private int trunkLeft(BufferedImage img){
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
    private int trunkRight(BufferedImage img){
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
    private int trunkTop(BufferedImage img){
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
    private int trunkBottom(BufferedImage img){
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
    

    private long replaceSprite(BufferedImage img, int animId, int frameId, int cx, int cy) {
        
        int width  = img.getWidth();
        int height = img.getHeight();        
        
        AnimFrame frame = manager.getCharacter().getAnimFrame(animId, frameId);
        long mapAddress = getHexFromField(genAddressField);
        int  paletteLine = getIntFromField(genPaletteField);
        if (paletteLine == INVALID_INT || paletteLine < 0 || paletteLine > 3){
            showError("Invalid palette line");
            return INVALID_LONG;
        }
        if (mapAddress == INVALID_LONG || mapAddress < 0){
            showError("Invalid address");
            updateGenAddress();
            return INVALID_LONG;
        }
        Sprite sprite = new Sprite();
        
        // find tiles
        int numTiles = 0;
        if (width%8 == 0) width/=8;
        else width = width/8 + 1;
        if (height%8 == 0) height/=8;
        else height = height/8 + 1;
        boolean[][] mx = new boolean[32][32];
        for (int x = 0 ; x < width ; x++){
            for (int y = 0 ; y < height ; y++){
                if (isTile(img,x,y)){
                    mx[x][y] = true;
                    numTiles++;
                }                 
            }
        }
        
        // create sprite tiles
        sprite.setNumTiles(numTiles);
        Piece[][] pieces = genPieces(mx);
        int nextIndex = 0;
        for (int x = 0 ; x < 32 ; ++x){
            for (int y = 0 ; y < 32 ; ++y){
                Piece p = pieces[x][y];
                if (pieces[x][y] != null){
                    SpritePiece sp = new SpritePiece();
                    sp.width = p.getWidth()-1;
                    sp.height = p.getHeight()-1;
                    sp.paletteLine = paletteLine;
                    sp.priorityFlag = false;
                    sp.xFliped = sp.yFliped = false;
                    sp.spriteIndex = nextIndex;
                    sp.y = y*8-cy;
                    sp.xL = x*8-cx;
                    sp.xR = -sp.xL - p.getWidth()*8;
//                    System.err.println(sp.xL + " <-> " + sp.xR + ", width: " + p.getWidth()*8);
                    nextIndex += p.getWidth() * p.getHeight();
                    sprite.addPiece(sp, p);
                }
            }
        }
//        System.out.println("pieces: " + sprite.getNumPieces() + ", num tiles: " + numTiles);
        
        // save sprite
        long artAddress = mapAddress + 6 + sprite.getNumPieces()*6;
        frame.mapAddress = mapAddress;
        frame.artAddress = artAddress;
        try {
            manager.writeSprite(sprite, mapAddress, artAddress);
        } catch (IOException ex) {
            showError("Unable to create the new sprite");
            return INVALID_LONG;
        }
        updateGenAddress();
        
        
        BufferedImage extended = expandImage(img, cx, cy);
//        if (img.getWidth() != 256 && img.getHeight() != 256 && cx != 128 && cy != 128)
//            extended = expandImage(img, cx, cy);
//        else extended = img;
        Animation anim = manager.getCharacter().getAnimation(animId);
        anim.setImage(frameId, extended);
        try {
            manager.save();
        }catch(IOException e){
            showError("Unable to render the new sprite");
            return INVALID_LONG;
        }
                
        return mapAddress;
    }

    
    
    @Override
    public void modeChanged(Mode mode) {
        setRadiosOff();
        switch(mode){
            case pencil:
                pencilRadio.setSelected(true);
                break;
            case brush:
                brushRadio.setSelected(true);
                break;
            case bucket:
                bucketRadio.setSelected(true);
                break;
            case dragSprite:
                dragSpriteRadio.setSelected(true);
                break;
            case dragImage:
                dragImageRadio.setSelected(true);
                break;
            case none:
                noneRadio.setSelected(true);
                break;
        }
    }

    @Override
    public void spriteDragged(int deltaX, int deltaY) {
        if (imagePanel.isFacedRight()) deltaX*=-1;
        if (wasFrameReplaced){
           lastcX -= deltaX;
           lastcY -= deltaY;
        }
        try {
            Sprite sprite = manager.readSprite(currAnimation, currFrame);
            AnimFrame frame = manager.getCharacter().getAnimFrame(currAnimation, currFrame);
            sprite.applyOffset(deltaX, deltaY);
            HitFrame hitFrame = manager.getCharacter().getHitFrame(currAnimation, currFrame);
            WeaponFrame weaponFrame = manager.getCharacter().getWeaponFrame(currAnimation, currFrame);
            if (hitFrame != null){
                hitFrame.x-=deltaX/2;
                hitFrame.y-=deltaY; 
            }
            if (weaponFrame != null){
                weaponFrame.x+=deltaX;
                weaponFrame.y+=deltaY; 
            }
            // save sprite
            manager.writeSpriteOnly(sprite, frame.mapAddress);
            // save hitframe & weaponframe
            manager.save();
            manager.bufferAnimFrame(currAnimation, currFrame);
        } catch (IOException ex) {
            showError("Unable to apply offset");
        }
        setFrame(currFrame);
    }

    private void refresh(){
        verifyModifications();
        setFrame(currFrame);       
    }
    
    private void hardRefresh(){
        int frame = currFrame;
        try {
            setCharacter(manager.getCurrentCharacterId());
        } catch (IOException ex) {
            showError("Error loading character");
        }
        setFrame(frame);        
    }

    @Override
    public void weaponPositionChanged(int newX, int newY) {
        if (imagePanel.isFacedRight()) newX*=-1;
        if (newX < -127) newX = -127;
        if (newX >  127) newX =  127;
        if (newY < -127) newY = -127;
        if (newY >  127) newY =  127;
        setField(wXField, newX);
        weaponXChanged();        
        setField(wYField, newY);
        weaponYChanged();
    }
   
    
    private BufferedImage expandImage(BufferedImage img, int cx, int cy){
        BufferedImage extended = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
        for (int i = 0 ; i < img.getWidth() ; ++i){
            for (int j = 0 ; j < img.getHeight() ; ++j){
                int x = i+128-cx;
                int y = j+128-cy;
                int rgbVal = img.getRGB(i, j);
                if (((rgbVal>>24) & 0xff) != 0){
                    if (x > 0 && x < 256 && y > 0 && y < 256)
                        extended.setRGB(x, y, rgbVal);
                }
            }
        }
        return extended;
    }    
    
    private void importSpriteReplacer(final BufferedImage sheet, final int columns, final int rows, final int cx, final int cy){
        final lib.anim.Character ch = manager.getCharacter();
        final int numAnims = ch.getNumAnimations();
        final ProgressMonitor progressMonitor = new ProgressMonitor(
            this,
            "Importing spritesheet",
            "", 0, numAnims
        );
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);

        final int frameWidth= sheet.getWidth()/columns;
        final int frameHeight= sheet.getHeight()/rows;
        final int maxId = columns*rows;
        new Thread(new Runnable(){
            
            private void finish(){
                ch.setModified(true);
                ch.setSpritesModified(true);
                try {
                    manager.save();
                    setCharacter(manager.getCurrentCharacterId());
                } catch (IOException ex) {
                    showError("Unable to save the sprites");                    
                }   
                progressMonitor.setProgress(999999);
                setEnabled(true);
                requestFocus();
            }

            @Override
            public void run() {
                setEnabled(false);
                int index = 0;
                TreeSet<Long> maps = new TreeSet<Long>();
                HashSet<Animation> processed = new HashSet<Animation>();
                for (int i = 0 ; i < numAnims ; ++i){
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)){
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0 ; j < animSize ; ++j){
                            long mapAddress = ch.getAnimFrame(i, j).mapAddress;
                            if (maps.contains(mapAddress)) continue;                        
                            maps.add(mapAddress);
                            int x = (index%columns)*frameWidth;
                            int y = (index/columns)*frameHeight;
                            BufferedImage img = sheet.getSubimage(x, y, frameWidth, frameHeight);
                            if (imagePanel.isFacedRight()) img = ImagePanel.flipImage(img);
                            img = expandImage(img, cx, cy);
                            anim.setImage(j, img);
                            anim.setSpritesModified(j, true);
                            index++;
                            if (index == maxId){
                                finish();
                                Toolkit.getDefaultToolkit().beep();
                                return;
                            }
                        }
                    }
                    progressMonitor.setNote("processing animations: " + (int)((i*1.0/numAnims)*100) + "%");
                    progressMonitor.setProgress(i);
                    if (progressMonitor.isCanceled()){
                        finish();
                        return;
                    }
                }
                finish();
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }
    
    
    
    class AddressesPair{
        public AddressesPair(long a, long b, BufferedImage img){
            this.a = a; this.b = b; this.img = img;
        }
        public BufferedImage img;
        public long a;
        public long b;
    }
    
    
    private void importSpriteGenerator(final BufferedImage sheet, final int columns, final int rows, final int cx, final int cy){
        final lib.anim.Character ch = manager.getCharacter();
        final int numAnims = ch.getNumAnimations();
        final ProgressMonitor progressMonitor = new ProgressMonitor(
            this,
            "Importing spritesheet",
            "", 0, numAnims
        );
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);

        final int frameWidth= sheet.getWidth()/columns;
        final int frameHeight= sheet.getHeight()/rows;
        final int maxId = columns*rows;
        new Thread(new Runnable(){
            
            private void finish(){
                ch.setModified(true);
                ch.setSpritesModified(true);
                hardRefresh();
                progressMonitor.setProgress(999999);
                setEnabled(true);
                requestFocus();
            }

            @Override
            public void run() {
                setEnabled(false);
                int index = 0;
                TreeMap<Long,AddressesPair> maps = new TreeMap<Long, AddressesPair>();
                HashSet<Animation> processed = new HashSet<Animation>();
                for (int i = 0 ; i < numAnims ; ++i){
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)){
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0 ; j < animSize ; ++j){
                            anim.setSpritesModified(j, true);
                            long mapAddress = anim.getFrame(j).mapAddress;
                            if (maps.containsKey(mapAddress)){
                                AddressesPair p = maps.get(mapAddress);
                                anim.getFrame(j).mapAddress = p.a;
                                anim.getFrame(j).artAddress = p.b;
                                anim.setImage(j, p.img);                            
                                continue;
                            }

                            int x = (index%columns)*frameWidth;
                            int y = (index/columns)*frameHeight;
                            BufferedImage img = sheet.getSubimage(x, y, frameWidth, frameHeight);
                            if (imagePanel.isFacedRight()) img = ImagePanel.flipImage(img);

                            long newAddress = replaceSprite(img, i, j,cx,cy);
                            if (newAddress == INVALID_LONG){
                                finish();
                                showError("Unable to replace sprites");                            
                                return;
                            }

                            maps.put(mapAddress, new AddressesPair(anim.getFrame(j).mapAddress,anim.getFrame(j).artAddress, anim.getImage(j)));
                            index++;
                            if (index == maxId){
                                finish();                            
                                Toolkit.getDefaultToolkit().beep();
                                return;
                            }
                        }
                    }
                    progressMonitor.setNote("processing animations: " + (int)((i*1.0/numAnims)*100) + "%");
                    progressMonitor.setProgress(i);
                    if (progressMonitor.isCanceled()){
                        finish();
                        return;
                    }
                }
                finish();                
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }
    
    
    private void exportSpriteSheet() {
        exportSpriteSheet(false);
    }
    
     private void exportSpriteSheet(final boolean single) {
        final String charName = guide.getCharName(guide.getFakeCharId(manager.getCurrentCharacterId()));
        File tmpFile = null;
        if (!single){
            imageSaver.setSelectedFile(new File(charName+".png"));
            int returnOption = imageSaver.showSaveDialog(this);
            if (returnOption != JFileChooser.APPROVE_OPTION) return;
            tmpFile = imageSaver.getSelectedFile();
            if (tmpFile == null) return;
        }
        final File outputFile = tmpFile;
        
        final lib.anim.Character ch = manager.getCharacter();
        final int numAnims = ch.getNumAnimations();
        final ProgressMonitor progressMonitor = new ProgressMonitor(
            this,
            single?"Exporting sprites":"Exporting spritesheet",
            "", 0, numAnims*2
        );
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);

        new Thread(new Runnable(){

            @Override
            public void run() {
                setEnabled(false);
                int left = Integer.MAX_VALUE;
                int right = Integer.MIN_VALUE;
                int top = Integer.MAX_VALUE;
                int bottom = Integer.MIN_VALUE;        
                TreeSet<Long> maps = new TreeSet<Long>();
                HashSet<Animation> processed = new HashSet<Animation>();
                for (int i = 0 ; i < ch.getNumAnimations() ; ++i){
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)){
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0 ; j < animSize ; ++j){
                            maps.add(manager.getCharacter().getAnimFrame(i, j).mapAddress);
                            Sprite sp;
                            try { sp = manager.readSprite(i, j);
                            } catch (IOException ex) {
                                showError("Unable to access sprites");
                                progressMonitor.setProgress(999999);
                                setEnabled(true);
                                requestFocus();
                                return;
                            }
                            Rectangle rect = sp.getBounds();
                            if (rect.x < left) left = rect.x;
                            if (rect.y < top) top = rect.y;
                            int r = rect.x+rect.width;
                            int b = rect.y+rect.height;
                            if (r > right) right = r;
                            if (b > bottom) bottom = b;
                        }
                    }
                    if (progressMonitor.isCanceled()){
                        setEnabled(true);
                        requestFocus();
                        return;
                    }
                    progressMonitor.setNote("Computing space: " + (int)((i*1.f/numAnims)*100) + "%");
                    progressMonitor.setProgress(i);
                }
                // add spacing
//                left--; right++; top--; bottom++;

                // create final image
                int numMaps = maps.size()+1;
                int width = right-left;
                int height = bottom-top;
                int columns = (int)(Math.sqrt(numMaps));
                int rows = numMaps/columns;
                if (numMaps%columns != 0) rows++;
                BufferedImage res = null;
                Graphics2D g2d = null;
                if (!single){
                    res = new BufferedImage(columns*width, rows*height,BufferedImage.TYPE_INT_ARGB);
                    g2d = res.createGraphics();
                }

                // draw frames
                maps.clear();
                processed.clear();
                int index = 0;
                for (int i = 0 ; i < ch.getNumAnimations() ; ++i){
                    Animation anim = ch.getAnimation(i);
                    if (!processed.contains(anim)){
                        processed.add(anim);
                        int animSize = anim.getNumFrames();
                        for (int j = 0 ; j < animSize ; ++j){
                            long address = manager.getCharacter().getAnimFrame(i, j).mapAddress;
                            try {
                                manager.bufferAnimation(i);
                            } catch (IOException ex) {                            
                                showError("Unable to access sprites");
                                progressMonitor.setProgress(999999);
                                setEnabled(true);
                                requestFocus();
                                return;
                            }
                            if (!maps.contains(address)){
                                maps.add(address);
                                BufferedImage img = manager.getImage(i, j).getSubimage(left, top, width, height);

                                if (single){
                                    try {
                                        File outputFile = new File(currentDirectory + "/" + charName + " " + i + "." + j + ".png");
                                        if (imagePanel.isFacedRight()){
                                            img = ImagePanel.flipImage(img);
                                        }
                                        ImageIO.write(img, "png", outputFile);
                                    } catch (IOException ex) {
                                        showError("Unable to save image");
                                        progressMonitor.setProgress(999999);
                                        setEnabled(true);
                                        requestFocus();
                                        return;
                                    }                                
                                }else{
                                    int x = (index%columns)*width;
                                    int y = (index/columns)*height;
                                    if (imagePanel.isFacedRight()){
                                        int w = img.getWidth();
                                        int h = img.getHeight();
                                        g2d.drawImage(img, x, y, x+w,y+h, w,0,0,h, null);
                                    }else g2d.drawImage(img, x, y, null);
                                }
                                index++;
                            }
                        }
                    }
                    progressMonitor.setNote("Rendering sprites: " + (int)((i*1.f/numAnims)*100) + "%");
                    progressMonitor.setProgress(numAnims + i);
                    if (progressMonitor.isCanceled()){
                        setEnabled(true);
                        requestFocus();
                        return;
                    }
                }
                if (!single){
                    int x = (index%columns)*width;
                    int y = (index/columns)*height;
                    String cs = ((columns/10==0)?" ":"") + ((columns/100==0)?" ":"");                
                    String watermark0a = "Columns:  " + cs + columns;
                    cs = ((rows/10==0)?" ":"") + ((rows/100==0)?" ":"");
                    String watermark0b = "Rows:     " + cs + rows;
                    cs = (((128-left)/10==0)?" ":"") + (((128-left)/100==0)?" ":"");
                    String watermark0c = "Center X: " + cs + (128-left);
                    cs = (((128-top)/10==0)?" ":"") + (((128-top)/100==0)?" ":"");
                    String watermark0d = "Center Y: " + cs + (128-top);
                    String watermark1 = "Generated by";
                    String watermark2 = TITLE;
    //                String watermark3 = "© gsaurus 2012";
                    g2d.setColor(Color.red);
                    g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
                    g2d.drawChars(watermark0a.toCharArray(), 0, watermark0a.length(), x+4, y+00);
                    g2d.drawChars(watermark0b.toCharArray(), 0, watermark0b.length(), x+4, y+10);
                    g2d.drawChars(watermark0c.toCharArray(), 0, watermark0c.length(), x+4, y+20);
                    g2d.drawChars(watermark0d.toCharArray(), 0, watermark0d.length(), x+4, y+30);
                    g2d.drawChars(watermark1.toCharArray(), 0, watermark1.length(), x+4, y+45);
                    g2d.drawChars(watermark2.toCharArray(), 0, watermark2.length(), x+4, y+55);
    //                g2d.drawChars(watermark3.toCharArray(), 0, watermark3.length(), x+12, y+60);
                    Palette pal = manager.getPalette();
                    int rc = 12;
                    for (int i = 0 ; i < 16 ; ++i){
                        int c = pal.getColor(i);
                        g2d.setColor(new Color(c));
                        g2d.fillRect(x+4+(i%8)*rc, y+60+(i/8)*rc, rc, rc);
                    }
                    g2d.dispose();

                    try {
                        ImageIO.write(res, "png", outputFile);
                    } catch (IOException ex) {                       
                        showError("Unable to save spritesheet");
                        progressMonitor.setProgress(999999);
                        setEnabled(true);
                        requestFocus();
                        return;
                    }
                }
                progressMonitor.setProgress(999999);
                setEnabled(true);
                requestFocus();
                Toolkit.getDefaultToolkit().beep();
            }            
        }).start();                        
    }
     
     
    private void exportIndividualFrames() {
        exportSpriteSheet(true);
    }
     
     
     
     //private void resizeAnimations(final ArrayList<Integer> sizes, final ArrayList<Boolean> wps, final TreeMap<Integer, Boolean> ownCol, final TreeMap<Integer, Boolean> ownWeap) {
     private void resizeAnimations(final ArrayList<Integer> sizes, final ArrayList<Integer> wps, final ArrayList<Boolean> hits) {
        final lib.anim.Character ch = manager.getCharacter();
        final int numAnims = sizes.size();
        final ProgressMonitor progressMonitor = new ProgressMonitor(
            this,
            "Generating resized animations",
            "", 0, numAnims+1
        );
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);
        
        new Thread(new Runnable(){
            
            private void finish(){
                ch.setModified(true);
                hardRefresh();
                progressMonitor.setProgress(999999);
                setEnabled(true);
                requestFocus();
            }

            @Override
            public void run() {
                setEnabled(false);                
                HashSet<Animation> processedAnims = new HashSet<Animation>();                
                // Resize animations                
                for (int i = 0 ; i < numAnims ; ++i){
                    int size = sizes.get(i);
                    int wp = wps.get(i);
                    boolean hit = false;
                    if (i >= lib.anim.Character.FIRST_HIT_ANIM){
                        hit = hits.get(i-lib.anim.Character.FIRST_HIT_ANIM);
                    }
                    if (wp != 2){
                        if (size < 0){
                            //ch.setAnim(i,-size-1, ownCol.get(i), ownWeap.get(i));                        
//                            System.out.println("set anim " + i + ", before: " + ch.getHitsSize());
                            ch.setAnim(i,-size-1, wp==1, hit);                        
//                            System.out.println("after: " + ch.getHitsSize());
                            processedAnims.add(ch.getAnimation(i));
                        }else if (size > 0){
                            Animation anim = ch.getAnimation(i);
                            if (processedAnims.contains(anim)){
                                ch.doubleAnim(i);
                            }
                            ch.resizeAnim(i,size, wp==1, hit); 
                        } else processedAnims.add(ch.getAnimation(i));
                    }else processedAnims.add(ch.getAnimation(i));
                    processedAnims.add(ch.getAnimation(i));
                    progressMonitor.setNote("Resizing animations: " + (int)((i*1.0/numAnims)*100) + "%");
                    progressMonitor.setProgress(i);
                    if (progressMonitor.isCanceled()){
                        finish();
                        return;
                    }
                }
                
                progressMonitor.setNote("Generating new scripts");
                progressMonitor.setProgress(numAnims);
                
                // Write the new animations script
                try{
                    manager.writeNewScripts();
                } catch(IOException e){
                    finish();
                    showError("Failed to convert the animations script");
                    return;
                }                             
                hardRefresh();                
                finish();                
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

    
    
     
     
     private void uncompressChar(final int type) {                     
        final lib.anim.Character ch = manager.getCharacter();
        final int numAnims = ch.getNumAnimations();
        final ProgressMonitor progressMonitor = new ProgressMonitor(
            this,
            "Uncompressing Character",
            "", 0, numAnims+1
        );
        progressMonitor.setMillisToDecideToPopup(50);
        progressMonitor.setMillisToPopup(100);
        
        new Thread(new Runnable(){
            
            private void finish(){
                ch.setModified(true);
                ch.setSpritesModified(true);
                hardRefresh();
                progressMonitor.setProgress(999999);
                setEnabled(true);
                requestFocus();
            }

            @Override
            public void run() {
                setEnabled(false);
                int index = 0;
                long newAnimsAddress = getHexFromField(genAddressField);                
                int animsSyzeInBytes = ch.getAnimsSize(type);
                setFieldAsHex(genAddressField,newAnimsAddress+animsSyzeInBytes);
                
                // Replace art with the uncompressed version
                TreeMap<Long,AddressesPair> maps = new TreeMap<Long, AddressesPair>();
                for (int i = 0 ; i < numAnims ; ++i){
                    Animation anim = ch.getAnimation(i);
                    try{
                        manager.bufferAnimation(i);
                    } catch(IOException e){
                        finish();
                        showError("Failed to buffer animations");                            
                        return;
                    }
                    int animSize = anim.getNumFrames();
                    for (int j = 0 ; j < animSize ; ++j){
                        anim.setSpritesModified(j, true);
                        ch.setModified(true);
                        ch.setSpritesModified(true);
                        long mapAddress = anim.getFrame(j).mapAddress;
                        if (maps.containsKey(mapAddress)){
                            AddressesPair p = maps.get(mapAddress);
                            anim.getFrame(j).mapAddress = p.a;
                            anim.getFrame(j).artAddress = p.b;
                            anim.setImage(j, p.img);
                            anim.setAnimType(type);
                            continue;
                        }
                        
                        BufferedImage img = anim.getImage(j);
                        anim.setAnimType(type);
                        long newAddress = replaceSprite(img, i, j,128,128);
                        if (newAddress == INVALID_LONG){
                            finish();
                            showError("Unable to replace the compressed sprites");
                            return;
                        }
                        
                        maps.put(mapAddress, new AddressesPair(anim.getFrame(j).mapAddress,anim.getFrame(j).artAddress, anim.getImage(j)));
                        index++;                        
                    }
                    progressMonitor.setNote("Converting sprites: " + (int)((i*1.0/numAnims)*100) + "%");
                    progressMonitor.setProgress(i);
                    if (progressMonitor.isCanceled()){
                        finish();
                        return;
                    }
                }
                
                progressMonitor.setNote("Converting animations script");
                progressMonitor.setProgress(numAnims);
                
                // Write the new animations script
                try{
                    manager.writeNewAnimations(newAnimsAddress);
                } catch(IOException e){
                    finish();
                    showError("Failed to convert the animations script");
                    return;
                }
                updateGenAddress();
                guide.setAnimType(guide.getFakeCharId(manager.getCurrentCharacterId()), type);
                
                hardRefresh();
                
                finish();                
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

    
    
    
}
