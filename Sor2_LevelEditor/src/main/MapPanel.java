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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JViewport;
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
    
    final int MAP_PADDING = 512;
    final Dimension MAP_SIZE = new Dimension(Short.MAX_VALUE, 480 + 2 * MAP_PADDING);
    
    final int DISTANCE_DRAG_MARGIN = 16;

    public interface ObjectSelectedListener{
        void onObjectSelected(BaseObject object, int index);
    }
    
    public interface ObjectMovedListener{
        void onObjectMoved(BaseObject object);
    }
    
    class DisplayedObject{
        public BaseObject object;
        public BufferedImage image;
        
        public DisplayedObject(BaseObject obj){
            this.object = obj;
        }
        
        public int getVisualPosX(){
            return MAP_PADDING + (short)object.posX;            
        }
        
        public int getVisualPosY(){
            int posY = MAP_PADDING + (short)object.posY;
            if (object instanceof ItemObject){
                return posY + (short)((ItemObject)object).verticalSpeed;
            }
            return posY;
        }
        
        public void setVisualPosX(int x){
            object.posX = (short)(x - MAP_PADDING);
        }
        
        public void setVisualPosY(int y){
            object.posY = (short)(y - MAP_PADDING);
            if (object instanceof ItemObject){
                object.posY -= (short)((ItemObject)object).verticalSpeed;
            }            
        }
        
    }
    
    
    public boolean showBackground = true;    
    public boolean showEnemies1 = true;
    public boolean showEnemies2 = true;
    public boolean showItems = true;
    public ObjectSelectedListener selectionListener;
    public ObjectMovedListener movedListener;
    
    private final List<DisplayedObject> displayedObjects = new ArrayList<>();
    private BufferedImage background;
    private DisplayedObject selectedObject;
    private Point dragStartingDistance;
    private boolean draggingDistanceLine;
    
    
    /**
     * Creates new form MapPanel
     */
    public MapPanel() {
        initComponents();        
        setupPanel();
        setupMouseListeners();
    }
    
    void setupPanel(){
        this.setLayout(null);
        setPreferredSize(MAP_SIZE);
        setMinimumSize(MAP_SIZE);
    }
    
    void notifySelectionListener(){
        if (selectionListener != null){
            if (selectedObject != null){
                selectionListener.onObjectSelected(selectedObject.object, displayedObjects.indexOf(selectedObject));
            }else{
                selectionListener.onObjectSelected(null, -1);
            }
        }
    }
    
    void notifyMovedListener(){
        if (movedListener != null){
            if (selectedObject != null){
                movedListener.onObjectMoved(selectedObject.object);
            }else{
                movedListener.onObjectMoved(null);
            }
        }
    }
    
    private void selectNext(int delta){
        if (displayedObjects.isEmpty()) return;
        if (selectedObject == null){
            selectedObject = displayedObjects.get(delta > 0 ? 0: displayedObjects.size() - 1);
        }else{
            int index = displayedObjects.indexOf(selectedObject);
            index += delta;
            if (index >= displayedObjects.size()){
                index = 0;
            }else if (index < 0) index = displayedObjects.size() - 1;
            selectedObject = displayedObjects.get(index);
        }
        if (selectedObject != null){
            JViewport viewport = (JViewport)this.getParent();
            viewport.setViewPosition(new Point(selectedObject.getVisualPosX() - viewport.getWidth()/2, selectedObject.getVisualPosY() - viewport.getHeight()/2));
        }
        notifySelectionListener();
    }
    
    public void selectNext(){
        selectNext(1);
    }
    
    public void selectPrevious(){
        selectNext(-1);
    }
    
    public void refreshGraphics(){
        this.revalidate();
        this.repaint();
    }
    
    void selectNearestObject(Point point){
        DisplayedObject closest = null;
        double shortestDistance = Double.MAX_VALUE;
        for (DisplayedObject obj: displayedObjects){
            int x = obj.getVisualPosX();
            int y = obj.getVisualPosY();
            Rectangle rect = new Rectangle((int) (x - obj.image.getWidth() * 0.5f), y - obj.image.getHeight(), obj.image.getWidth(), obj.image.getHeight());
            if (rect.contains(point)){
                double distance = point.distance(x, y - obj.image.getHeight() * 0.5);
                if (distance < shortestDistance){
                    closest = obj;
                    // Give priority to selected object
                    if (selectedObject == obj){
                        shortestDistance = 0;
                    }else{
                        shortestDistance = distance;
                    }
                }
            }
        }
        if (closest != null){
            selectedObject = closest;
            dragStartingDistance = new Point(point.x - selectedObject.getVisualPosX(), point.y - selectedObject.getVisualPosY());
        }else{
            selectedObject = null;
        }
        refreshGraphics();
        notifySelectionListener();
    }
    
    
    void updateFinalSelectionPosition(Point finalPoint){
        if (selectedObject != null){
            selectedObject.setVisualPosX(finalPoint.x);
            selectedObject.setVisualPosY(finalPoint.y);
            refreshGraphics();
            notifyMovedListener();
        }
    }
    
    void updateSelectionPosition(Point point){
        if (selectedObject != null){
            Point finalPoint = new Point(point.x - dragStartingDistance.x, point.y - dragStartingDistance.y);
            updateFinalSelectionPosition(finalPoint);            
        }
    }
    
    
    boolean selectDistanceLine(Point point){
        if (selectedObject == null || !(selectedObject.object instanceof CharacterObject)) return false;
        CharacterObject character = (CharacterObject) selectedObject.object;
        if (character.triggerType != 1) return false;
        int distanceLine = (MAP_PADDING + character.triggerArgument);
        draggingDistanceLine = Math.abs(point.x - distanceLine) < DISTANCE_DRAG_MARGIN;
        if (draggingDistanceLine){
            dragStartingDistance = new Point(point.x - distanceLine, 0);
        }
        return draggingDistanceLine;
    }
    
    boolean tryUpdateSelectionDistance(Point point){
        if (! draggingDistanceLine || selectedObject == null || !(selectedObject.object instanceof CharacterObject)) return false;
        CharacterObject character = (CharacterObject) selectedObject.object;
        if (character.triggerType != 1) return false;
        character.triggerArgument = (point.x - dragStartingDistance.x) - MAP_PADDING;
        refreshGraphics();
        notifyMovedListener();
        return true;
    }
    
    void setupMouseListeners(){
        addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                // Nothing
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!selectDistanceLine(e.getPoint())){
                    draggingDistanceLine = false;
                    selectNearestObject(e.getPoint());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Nothing
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                // Nothing
            }
            @Override
            public void mouseExited(MouseEvent e) {
                // Nothing
            }
            
        });
        
        this.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e) {
                // Drag selection
                if (!tryUpdateSelectionDistance(e.getPoint())){
                    updateSelectionPosition(e.getPoint());
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Nothing
            }
            
        });
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
        DisplayedObject previouslySelectedObject = selectedObject;
        // Reset view
        if (displayedObjects.isEmpty()){
            JViewport viewport = (JViewport)this.getParent();
            viewport.setViewPosition(new Point(MAP_PADDING + 160 - viewport.getWidth()/2, MAP_PADDING + 112 - viewport.getHeight()/2));
        }
        clear(false);
        int gameSceneNumber = sceneNumber * 2;
        if (showEnemies1){
            for (CharacterObject obj: loadcues.enemiesPart1){
                if ((gameSceneNumber < 0 || obj.sceneId == gameSceneNumber) && (obj.minimumDifficulty <= minimumDifficulty)){
                    createDisplayedObject(obj, guide, obj.useAlternativePalette);
                }
            }
        }
        if (showEnemies2){
            for (CharacterObject obj: loadcues.enemiesPart2){
                if ((gameSceneNumber < 0 || obj.sceneId == gameSceneNumber) && (obj.minimumDifficulty <= minimumDifficulty)){
                    createDisplayedObject(obj, guide, obj.useAlternativePalette);
                }
            }
        }
        if (showItems){
            for (ItemObject obj: loadcues.goodies){
                if (gameSceneNumber < 0 || obj.sceneId == gameSceneNumber){
                    createDisplayedObject(obj, guide, false);
                }
            }
        }
        
        if (previouslySelectedObject != null){
            for (DisplayedObject obj: displayedObjects){
                if (obj.object == previouslySelectedObject.object){
                    selectedObject = obj;
                    break;
                }
            }            
        }
        if (selectedObject == null){
            selectNext();
        }        
        loadMapBackground(levelNumber, sceneNumber);        
        this.revalidate();
        this.repaint();
    }
    
    private void clear(boolean repaint){
        selectedObject = null;
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
    
    
    
    
    void drawObject(Graphics g, DisplayedObject obj, boolean selection){        
        int width = obj.image.getWidth();
        int height = obj.image.getHeight();
        int x = obj.getVisualPosX();
        int y = obj.getVisualPosY();
        int imageX = x - (int)(width * 0.5f);
        int imageY = y - height;        
        g.drawImage(obj.image, imageX, imageY, null);
        if (selection){
            g.setColor(Color.white);
        }else{
            g.setColor(Color.black);
        }
        g.drawRect(imageX, imageY, width, height);
        
        if (obj.object instanceof ItemObject){            
            int verticalHeight = (short)((ItemObject)obj.object).verticalSpeed;
            int realY = y - verticalHeight;
            Color previousColor = g.getColor();
            g.setColor(Color.orange);
            g.drawLine(x, y, x, realY);
            g.setColor(previousColor);
            y = realY;
        }
        
        g.drawLine(x-5, y, x+5, y);
        g.drawLine(x, y-5, x, y+5);
    }
    
    @Override
     protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Background
        g.setColor(Color.black);
        g.drawLine(MAP_PADDING, MAP_PADDING, MAP_PADDING, MAP_SIZE.height);
        g.drawLine(MAP_PADDING, MAP_PADDING, MAP_SIZE.width, MAP_PADDING);
        if (showBackground && background != null){
            g.drawImage(background, MAP_PADDING, MAP_PADDING, this);
        }
        
        // Objects
        for (DisplayedObject obj: displayedObjects){
            if (obj != selectedObject){
                drawObject(g, obj, false);
            }
            
        }
        
        // Selection
        if (selectedObject != null){
            drawObject(g, selectedObject, true);
            
            if (selectedObject.object instanceof CharacterObject){
                CharacterObject characterObject = (CharacterObject)selectedObject.object;
                if (characterObject.triggerType == 1){
                    g.setColor(Color.yellow);
                    g.drawLine(MAP_PADDING + characterObject.triggerArgument, 0, MAP_PADDING + characterObject.triggerArgument, MAP_SIZE.height);
                }
            }
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(0, 153, 153));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
