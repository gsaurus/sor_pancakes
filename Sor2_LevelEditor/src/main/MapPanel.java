/*
 * Copyright 2018 gil.costa.
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import lib.ExceptionUtils;
import lib.elc.BaseObject;
import lib.elc.CharacterObject;
import lib.elc.ItemObject;
import lib.elc.LevelLoadcues;
import lib.elc.ObjectDefinition;

/**
 *
 * @author gil.costa
 */
public final class MapPanel extends javax.swing.JPanel {
    
    final int MAP_PADDING = 128;
    final Dimension MAP_SIZE = new Dimension(Short.MAX_VALUE, 736);

    class DisplayedObject{
        public BaseObject object;
        public BufferedImage image;
        
        public DisplayedObject(BaseObject obj){
            this.object = obj;
        }
        
        public int getDisplayX(){
            return MAP_PADDING + (short)object.posX;
        }
        
        public int getDisplayY(){
            return MAP_PADDING + (short)object.posY;
        }
        
        public void setDisplayX(int x){
            object.posX = (short)(x - MAP_PADDING);
        }
        
        public void setDisplayY(int y){
            object.posY = (short)(y - MAP_PADDING);
        }
    }
    
    final List<DisplayedObject> displayedObjects = new ArrayList<>();
    private BufferedImage background;
    
    
    /**
     * Creates new form MapPanel
     */
    public MapPanel() {
        initComponents();
        this.setLayout(null);
        setPreferredSize(MAP_SIZE);
        setMinimumSize(MAP_SIZE);
    }
    
    
    void createDisplayedObject(BaseObject obj, Guide guide, boolean useAlternativePalette){
        DisplayedObject object = new DisplayedObject(obj);
        ObjectDefinition definition = guide.objectsDefinition.get(obj.getObjectIndex());
        if (definition != null){
            if (useAlternativePalette){
                object.image = definition.spriteTwo;
            }else{
                object.image = definition.spriteOne;
            }
        }
        else{
            try {
                object.image = ImageIO.read(new File("images/error.png"));
            } catch (IOException ex) {
                ExceptionUtils.showError(this, "Unable to load default error image", ex);
            }
        }    
        displayedObjects.add(object);
    }
    
    
    void loadMapBackground(int levelNumber, int sceneNumber){
        background = null;
        levelNumber += 1;
        sceneNumber += 1;        
        // Try current scene, or any of the previous scenes (may be reusing same background)
        while (sceneNumber > 0){
            String imageName = "images/scenes/level_" + levelNumber + "_" + sceneNumber + ".png";
            File backgroundFile = new File(imageName);
            if (backgroundFile.exists()){
                try{
                    background = ImageIO.read(backgroundFile);
                    break;
                }catch(IOException ex){
                    ExceptionUtils.showError(this, "Unable to load background image" + imageName, ex);
                }
            }
            --sceneNumber;
        }
    }
    
    public void reload(LevelLoadcues loadcues, int levelNumber, int sceneNumber, int minimumDifficulty, Guide guide){
        clear(false);
        int gameSceneNumber = sceneNumber * 2;
        for (CharacterObject obj: loadcues.enemiesPart1){
            if ((gameSceneNumber < 0 || obj.sceneId == gameSceneNumber) && (obj.minimumDifficulty <= minimumDifficulty)){
                createDisplayedObject(obj, guide, obj.useAlternativePalette);
            }
        }
        for (CharacterObject obj: loadcues.enemiesPart2){
            if ((gameSceneNumber < 0 || obj.sceneId == gameSceneNumber) && (obj.minimumDifficulty <= minimumDifficulty)){
                createDisplayedObject(obj, guide, obj.useAlternativePalette);
            }
        }
        for (ItemObject obj: loadcues.goodies){
            if (gameSceneNumber < 0 || obj.sceneId == gameSceneNumber){
                createDisplayedObject(obj, guide, false);
            }
        }
        loadMapBackground(levelNumber, sceneNumber);
        this.revalidate();
        this.repaint();
    }
    
    private void clear(boolean repaint){
        this.removeAll();
        displayedObjects.clear();
        if (repaint){
            this.revalidate();
            this.repaint();
        }
    }
    
    public void clear(){
        clear(true);
    }
    
    
    
    
    
    
    @Override
     protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (background != null){
            g.drawImage(background, MAP_PADDING, MAP_PADDING, this);
        }
        
        for (DisplayedObject obj: displayedObjects){
            g.drawImage(
                    obj.image,
                    obj.getDisplayX() + (int)(obj.image.getWidth() * 0.5f),
                    obj.getDisplayY() - obj.image.getHeight(),
                    null
            );
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
