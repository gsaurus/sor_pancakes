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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import lib.NumberUtils;
import lib.elc.CharacterObject;
import lib.elc.ObjectDefinition;
import lib.names.AllEnemyNames;

/**
 *
 * @author gil.costa
 */
public final class CharacterPanel extends javax.swing.JPanel {
    
    public interface ImportantChangeListener{
        void onObjectChanged(CharacterObject object);
    }
    
    String[] spawnModes = new String[]{
        "Instantaneous",
        "Distance",
        "Clock",
        "Delayed"
    };
    

//private static final String INVALID_OPTION = "Unknown";
    private final CharacterObject object;
    private ObjectDefinition objectDefinition;
    private final AllEnemyNames enemyNames;
    private final Guide guide;
    private boolean isReloading;
    public NumberUtils.Formatter formatter;
    public ImportantChangeListener listener;
    
    
    void reloadObjectDefinition(){
        int objIndex = object.objectId / 2;
        if (objIndex >= 0 && objIndex < guide.objectsDefinition.size()){
            objectDefinition = guide.objectsDefinition.get(objIndex);
        }else{
            objectDefinition = null;
        }
        if (objectDefinition != null){
            spawnModeComboBox.setModel(new DefaultComboBoxModel(objectDefinition.spawnModes.toArray()));
        }else{
            spawnModeComboBox.setModel(new DefaultComboBoxModel(new String[]{}));
        }
    }
    
    
    public CharacterPanel(CharacterObject object, NumberUtils.Formatter formatter, AllEnemyNames enemyNames, Guide guide) {
        initComponents();
        this.guide = guide;
        this.object = object;
        this.formatter = formatter;
        this.enemyNames = enemyNames;
        
        nameComboBox.setModel(new DefaultComboBoxModel(enemyNames.allNames.toArray()));
        
        String[] objectNames = new String[guide.objectsDefinition.size()];
        for (int i = 0 ; i < guide.objectsDefinition.size(); ++i){
            ObjectDefinition definition = guide.objectsDefinition.get(i);
            if (definition != null){
                objectNames[i] = definition.name;
            }else{
                objectNames[i] = Integer.toHexString(i * 2);
            }
        }
        reloadObjectDefinition();
        
        objectIdComboBox.setModel(new DefaultComboBoxModel(objectNames));
        triggerComboBox.setModel(new DefaultComboBoxModel(spawnModes));
        reload();
    }
    
    
    void notifyListener(){
        if (!isReloading && listener != null){
            listener.onObjectChanged(object);            
        }
    }
    
    
    void setComboValue(JComboBox comboBox, int index, String value){
        try{
            comboBox.setSelectedIndex(index);
        }catch(IllegalArgumentException ex){
            if (comboBox.getSelectedIndex() != index){
                comboBox.setSelectedItem(value);
            }
        }
    }

    void setComboValue(JComboBox comboBox, int index, int value){
        setComboValue(comboBox, index, formatter.toUnsignedString(value));
    }
    
    void setComboValue(JComboBox comboBox, int index, long value){
        setComboValue(comboBox, index, formatter.toUnsignedString(value));
    }
    
    
    public void reloadPosition(){
        boolean wasReloading = isReloading;
        isReloading = true;
        posXTextField.setText(formatter.toString((short)object.posX));
        posYTextField.setText(formatter.toString((short)object.posY));
        setTriggerArgumentValueToTextField();
        isReloading = wasReloading;
    }
    
    public void reload(){
        isReloading = true;
        
        setComboValue(difficultyComboBox, object.minimumDifficulty, object.minimumDifficulty);
        int nameIndex = (int)(object.nameAddress - enemyNames.address) / AllEnemyNames.NAME_SIZE;
        setComboValue(nameComboBox, nameIndex, object.nameAddress);
        setComboValue(objectIdComboBox, object.objectId / 2, object.objectId);
        setComboValue(spawnModeComboBox, object.introductionType, object.introductionType);
        setComboValue(triggerComboBox, object.triggerType, object.triggerType);
        
        aggressivenessTextField.setText(formatter.toUnsignedString(object.enemyAgressiveness));
        alternativePaletteCheckBox.setSelected(object.useAlternativePalette);
        bikerWeaponCheckBox.setSelected(object.bikerWeaponFlag);
        ninjaHeightTextField.setText(formatter.toUnsignedString(object.ninjaHookHeight));        
        bboxDeptTextField.setText(formatter.toString((byte)object.collisionDept));
        bboxHeightTextField.setText(formatter.toString((byte)object.collisionHeight));
        bboxWidhTextField.setText(formatter.toString((byte)object.collisionWidth));
        healthTextField.setText(formatter.toUnsignedString(object.health));
        initialStateTextField.setText(formatter.toUnsignedString(object.initialState));
        useBossSlotCheckBox.setSelected(object.useBossSlot);
        sceneIdTextField.setText(formatter.toUnsignedString(object.sceneId));
        scoreTextField.setText(formatter.toUnsignedString(object.deathScore));                
        vramTextField.setText(formatter.toUnsignedString(object.vram));
        reloadPosition();
        
        isReloading = false;
    }
    
    
    public void save(){
        isReloading = true;
        objectIdComboBoxActionPerformed(null);
        sceneIdTextFieldActionPerformed(null);
        posXTextFieldActionPerformed(null);
        posYTextFieldActionPerformed(null);
        alternativePaletteCheckBoxActionPerformed(null);
        difficultyComboBoxActionPerformed(null);
        aggressivenessTextFieldActionPerformed(null);
        triggerComboBoxActionPerformed(null);
        triggerArgumentTextFieldActionPerformed(null);
        spawnModeComboBoxActionPerformed(null);
        initialStateTextFieldActionPerformed(null);
        healthTextFieldActionPerformed(null);
        scoreTextFieldActionPerformed(null);
        bboxWidhTextFieldActionPerformed(null);
        bboxHeightTextFieldActionPerformed(null);
        bboxDeptTextFieldActionPerformed(null);
        nameComboBoxActionPerformed(null);
        vramTextFieldActionPerformed(null);
        bikerWeaponCheckBoxActionPerformed(null);
        ninjaHeightTextFieldActionPerformed(null);
        isReloading = false;
    }    
    
    void setTriggerArgumentValueToTextField(){
        if (object.triggerType == 2){
            int arg1 = (object.triggerArgument >> 8) & 0xFF;
            int arg2 = object.triggerArgument & 0xFF;
            triggerArgumentTextField.setText(formatter.toUnsignedString(arg1) + " " + formatter.toUnsignedString(arg2));
        }else{
            triggerArgumentTextField.setText(formatter.toUnsignedString(object.triggerArgument));
        }
    }
    
    void getTriggerArgumentValueFromTextField(){
        String[] values = triggerArgumentTextField.getText().split(" ");
        if (values.length > 1){
            int arg1 = (int) formatter.toNumber(values[0]);
            int arg2 = (int) formatter.toNumber(values[1]);
            object.triggerArgument = ((arg1 << 8) & 0xFF00) + (arg2 & 0xFF);
        }else if (values.length == 1){
            object.triggerArgument = (int)formatter.toNumber(values[0]);
        }
    }
    
    void refreshTriggerArgumentValue(){
        getTriggerArgumentValueFromTextField();
        setTriggerArgumentValueToTextField();
    }
    

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        alternativePaletteCheckBox = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        objectIdComboBox = new javax.swing.JComboBox<>();
        sceneIdTextField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        posXTextField = new javax.swing.JTextField();
        posYTextField = new javax.swing.JTextField();
        difficultyComboBox = new javax.swing.JComboBox<>();
        aggressivenessTextField = new javax.swing.JTextField();
        triggerComboBox = new javax.swing.JComboBox<>();
        triggerArgumentTextField = new javax.swing.JTextField();
        spawnModeComboBox = new javax.swing.JComboBox<>();
        initialStateTextField = new javax.swing.JTextField();
        healthTextField = new javax.swing.JTextField();
        scoreTextField = new javax.swing.JTextField();
        bboxWidhTextField = new javax.swing.JTextField();
        bboxHeightTextField = new javax.swing.JTextField();
        bboxDeptTextField = new javax.swing.JTextField();
        nameComboBox = new javax.swing.JComboBox<>();
        vramTextField = new javax.swing.JTextField();
        bikerWeaponCheckBox = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        useBossSlotCheckBox = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        ninjaHeightTextField = new javax.swing.JTextField();

        jLabel1.setText("Character ID:");

        jLabel2.setText("Scene ID:");

        jLabel3.setText("Position:");

        jLabel4.setText("Difficulty:");

        alternativePaletteCheckBox.setText("Alternative Palette");
        alternativePaletteCheckBox.setToolTipText("<html>Use the alternative colors<br>Use <b><i>Palettes of Rage</b></i> to edit the alt. palette of each scene<br>Alt. Palette can also be set by using a sub-boss<br>or boss with it's own palette</html>");
        alternativePaletteCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alternativePaletteCheckBoxActionPerformed(evt);
            }
        });

        jLabel5.setText("Trigger:");

        jLabel6.setText("Trigg Arg:");

        jLabel7.setText("Aggressiveness:");

        jLabel8.setText("Spawn Mode:");

        jLabel9.setText("Settings:");

        jLabel10.setText("Health:");

        jLabel11.setText("Bounding Box:");

        jLabel12.setText("x");

        jLabel13.setText("x");

        jLabel14.setText("Score:");

        jLabel15.setText("Name:");

        jLabel16.setText("VRAM:");

        objectIdComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        objectIdComboBox.setToolTipText("Type of enemy to spawn");
        objectIdComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objectIdComboBoxActionPerformed(evt);
            }
        });

        sceneIdTextField.setText("jTextField1");
        sceneIdTextField.setToolTipText("<html>Which scene this enemy spawns at<br>Note: only move enemies between adjacent scenes<br>I.e. you can move last enemies of the scene to the next scene,<br>or first enemies to previous scene</html>");
        sceneIdTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sceneIdTextFieldActionPerformed(evt);
            }
        });

        jLabel17.setText("x");

        posXTextField.setText("jTextField1");
        posXTextField.setToolTipText("<html>Position X<br>If triggered by distance or clock, position is relative to screen bounds</html>");
        posXTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                posXTextFieldActionPerformed(evt);
            }
        });

        posYTextField.setText("jTextField1");
        posYTextField.setToolTipText("<html>Position Y</html>");
        posYTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                posYTextFieldActionPerformed(evt);
            }
        });

        difficultyComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Very Easy", "Easy", "Normal", "Hard", "Very Hard", "Mania" }));
        difficultyComboBox.setToolTipText("Minimum difficulty in which this enemy can spawn");
        difficultyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difficultyComboBoxActionPerformed(evt);
            }
        });

        aggressivenessTextField.setText("jTextField1");
        aggressivenessTextField.setToolTipText("<html>Level of AI agressiveness<br>Maximum is 0xF, 15 in decimal<br>On Mania, all enemies are at their maximum</html>");
        aggressivenessTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aggressivenessTextFieldActionPerformed(evt);
            }
        });

        triggerComboBox.setEditable(true);
        triggerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        triggerComboBox.setToolTipText("<html>What triggers the spawning of this enemy<br><b>Instantaneous:</b> spawn imediately<br><b>Distance:</b> Spawn if player crosses a certain line on map,<br>Drag the yellow line on the map, or edit Trigg Arg value<br><b>Clock:</b> Spawn if HUD clock drops below the first hex value of the argument,<br> or if number of enemies on screen drop bellow the second value of the argument<br><b>Delayed:</b> The enemy is not used, but the next enemy<br>will be delayed by the value in Trigg Arg</html>");
        triggerComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                triggerComboBoxItemStateChanged(evt);
            }
        });
        triggerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerComboBoxActionPerformed(evt);
            }
        });

        triggerArgumentTextField.setText("jTextField1");
        triggerArgumentTextField.setToolTipText("<html>Argument used in the spawn trigger<br>Depends on the type of trigger<br><b>Instantaneous:</b> argument is not used<br><b>Distance:</b> position on map that player needs to cross to spawn the enemy<br><b>Clock:</b> Divides the argument in two numbers, separated by spaces<br> - First value represents clock value;<br> - Second value represents number of enemies on screen<br>Enemy spawns if clock is under the first value,<br>or num enemies on screen is below the second value<br><b>Delayed:</b> Time to delay the spawning of the next enemy</html>");
        triggerArgumentTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerArgumentTextFieldActionPerformed(evt);
            }
        });

        spawnModeComboBox.setEditable(true);
        spawnModeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        spawnModeComboBox.setToolTipText("<html>How the enemy shows up<br>Different per type of enemy</html>");
        spawnModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spawnModeComboBoxActionPerformed(evt);
            }
        });

        initialStateTextField.setText("jTextField1");
        initialStateTextField.setToolTipText("Tecnical settings of the enemy");
        initialStateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialStateTextFieldActionPerformed(evt);
            }
        });

        healthTextField.setText("jTextField1");
        healthTextField.setToolTipText("<html>Enemy health/energy bar<br>It is multiplied by difficulty level<html>");
        healthTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                healthTextFieldActionPerformed(evt);
            }
        });

        scoreTextField.setText("jTextField1");
        scoreTextField.setToolTipText("<html>Score won by beating this enemy<br>Sometimes used as \"bonus\" enemies</html>");
        scoreTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreTextFieldActionPerformed(evt);
            }
        });

        bboxWidhTextField.setText("jTextField1");
        bboxWidhTextField.setToolTipText("Width");
        bboxWidhTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bboxWidhTextFieldActionPerformed(evt);
            }
        });

        bboxHeightTextField.setText("jTextField1");
        bboxHeightTextField.setToolTipText("Height");
        bboxHeightTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bboxHeightTextFieldActionPerformed(evt);
            }
        });

        bboxDeptTextField.setText("jTextField1");
        bboxDeptTextField.setToolTipText("Depth");
        bboxDeptTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bboxDeptTextFieldActionPerformed(evt);
            }
        });

        nameComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        nameComboBox.setToolTipText("Enemy name on HUD");
        nameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameComboBoxActionPerformed(evt);
            }
        });

        vramTextField.setText("jTextField1");
        vramTextField.setToolTipText("<html>Address in VRAM where this type of enemy is loaded<br>Unused in Syndicate Wars</html>");
        vramTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vramTextFieldActionPerformed(evt);
            }
        });

        bikerWeaponCheckBox.setToolTipText("When set, biker carries a pipe weapon");
        bikerWeaponCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bikerWeaponCheckBoxActionPerformed(evt);
            }
        });

        jLabel19.setText("Biker Pipe:");

        useBossSlotCheckBox.setToolTipText("<html>Must be triggered for bosses<br>They use a special spawning slot</html>");
        useBossSlotCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useBossSlotCheckBoxActionPerformed(evt);
            }
        });

        jLabel20.setText("Boss:");

        jLabel21.setText("Ninja H:");

        ninjaHeightTextField.setText("jTextField1");
        ninjaHeightTextField.setToolTipText("When a ninja is hooked, this determines how high he is");
        ninjaHeightTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ninjaHeightTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(alternativePaletteCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(difficultyComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(triggerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aggressivenessTextField))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(triggerArgumentTextField))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(objectIdComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spawnModeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(healthTextField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scoreTextField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nameComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vramTextField))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(initialStateTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(useBossSlotCheckBox))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(posXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(posYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sceneIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(bboxWidhTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bboxHeightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bboxDeptTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel11))
                                .addGap(0, 11, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bikerWeaponCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ninjaHeightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addGap(1, 1, 1)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(objectIdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sceneIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel17)
                    .addComponent(posXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(posYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(alternativePaletteCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(difficultyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(aggressivenessTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(triggerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(triggerArgumentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(spawnModeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bikerWeaponCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(initialStateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(useBossSlotCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(healthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(scoreTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(bboxWidhTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bboxHeightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bboxDeptTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(nameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(vramTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ninjaHeightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void objectIdComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objectIdComboBoxActionPerformed
        int index  = objectIdComboBox.getSelectedIndex();
        if (index != -1){
            object.objectId = index * 2;
        }else{
            object.objectId = (int)formatter.toNumber((String)objectIdComboBox.getSelectedItem());
        }
        if (!isReloading){
            reloadObjectDefinition();
        }
        notifyListener();
    }//GEN-LAST:event_objectIdComboBoxActionPerformed

    private void sceneIdTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sceneIdTextFieldActionPerformed
        object.sceneId = (int)formatter.toNumber(sceneIdTextField.getText());
        notifyListener();
    }//GEN-LAST:event_sceneIdTextFieldActionPerformed

    private void posXTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_posXTextFieldActionPerformed
        object.posX = (int)formatter.toNumber(posXTextField.getText());
        notifyListener();
    }//GEN-LAST:event_posXTextFieldActionPerformed

    private void posYTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_posYTextFieldActionPerformed
        object.posY = (int)formatter.toNumber(posYTextField.getText());
        notifyListener();
    }//GEN-LAST:event_posYTextFieldActionPerformed

    private void alternativePaletteCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alternativePaletteCheckBoxActionPerformed
        object.useAlternativePalette = alternativePaletteCheckBox.isSelected();
        notifyListener();
    }//GEN-LAST:event_alternativePaletteCheckBoxActionPerformed

    private void difficultyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difficultyComboBoxActionPerformed
        int index  = difficultyComboBox.getSelectedIndex();
        if (index != -1){
            object.minimumDifficulty = index;
        }else{
            object.minimumDifficulty = (int)formatter.toNumber((String)difficultyComboBox.getSelectedItem());
        }
        notifyListener();
    }//GEN-LAST:event_difficultyComboBoxActionPerformed

    private void aggressivenessTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aggressivenessTextFieldActionPerformed
        object.enemyAgressiveness = (int)formatter.toNumber(aggressivenessTextField.getText());
    }//GEN-LAST:event_aggressivenessTextFieldActionPerformed

    private void triggerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerComboBoxActionPerformed
        int index  = triggerComboBox.getSelectedIndex();
        if (index != -1){
            object.triggerType = index;
        }else{
            object.triggerType = (int)formatter.toNumber((String)triggerComboBox.getSelectedItem());
        }
        // Update trigger argument display
        refreshTriggerArgumentValue();
    }//GEN-LAST:event_triggerComboBoxActionPerformed

    private void triggerArgumentTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerArgumentTextFieldActionPerformed
        getTriggerArgumentValueFromTextField();
    }//GEN-LAST:event_triggerArgumentTextFieldActionPerformed

    private void spawnModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spawnModeComboBoxActionPerformed
        int index  = spawnModeComboBox.getSelectedIndex();
        if (index != -1){
            object.introductionType = index;
        }else{
            object.introductionType = (int)formatter.toNumber((String)spawnModeComboBox.getSelectedItem());
        }
    }//GEN-LAST:event_spawnModeComboBoxActionPerformed

    private void initialStateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initialStateTextFieldActionPerformed
        object.initialState = (int)formatter.toNumber(initialStateTextField.getText());
    }//GEN-LAST:event_initialStateTextFieldActionPerformed

    private void healthTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_healthTextFieldActionPerformed
        object.health = (int)formatter.toNumber(healthTextField.getText());
    }//GEN-LAST:event_healthTextFieldActionPerformed

    private void scoreTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreTextFieldActionPerformed
        object.deathScore = (int)formatter.toNumber(scoreTextField.getText());
    }//GEN-LAST:event_scoreTextFieldActionPerformed

    private void bboxWidhTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bboxWidhTextFieldActionPerformed
        object.collisionWidth = (int)formatter.toNumber(bboxWidhTextField.getText());
    }//GEN-LAST:event_bboxWidhTextFieldActionPerformed

    private void bboxHeightTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bboxHeightTextFieldActionPerformed
        object.collisionHeight = (int)formatter.toNumber(bboxHeightTextField.getText());
    }//GEN-LAST:event_bboxHeightTextFieldActionPerformed

    private void bboxDeptTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bboxDeptTextFieldActionPerformed
        object.collisionDept = (int)formatter.toNumber(bboxDeptTextField.getText());
    }//GEN-LAST:event_bboxDeptTextFieldActionPerformed

    private void nameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameComboBoxActionPerformed
        int index  = nameComboBox.getSelectedIndex();
        if (index != -1){
            object.nameAddress = enemyNames.address + index * AllEnemyNames.NAME_SIZE;
        }else{
            object.nameAddress = formatter.toNumber((String)nameComboBox.getSelectedItem());
        }
    }//GEN-LAST:event_nameComboBoxActionPerformed

    private void vramTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vramTextFieldActionPerformed
        object.vram = (int)formatter.toNumber(vramTextField.getText());
    }//GEN-LAST:event_vramTextFieldActionPerformed

    private void bikerWeaponCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bikerWeaponCheckBoxActionPerformed
        object.bikerWeaponFlag = bikerWeaponCheckBox.isSelected();
    }//GEN-LAST:event_bikerWeaponCheckBoxActionPerformed

    private void triggerComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_triggerComboBoxItemStateChanged
        refreshTriggerArgumentValue();
    }//GEN-LAST:event_triggerComboBoxItemStateChanged

    private void useBossSlotCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useBossSlotCheckBoxActionPerformed
        object.useBossSlot = useBossSlotCheckBox.isSelected();
    }//GEN-LAST:event_useBossSlotCheckBoxActionPerformed

    private void ninjaHeightTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ninjaHeightTextFieldActionPerformed
        object.ninjaHookHeight = (int)formatter.toUnsignedNumber(ninjaHeightTextField.getText());
    }//GEN-LAST:event_ninjaHeightTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aggressivenessTextField;
    private javax.swing.JCheckBox alternativePaletteCheckBox;
    private javax.swing.JTextField bboxDeptTextField;
    private javax.swing.JTextField bboxHeightTextField;
    private javax.swing.JTextField bboxWidhTextField;
    private javax.swing.JCheckBox bikerWeaponCheckBox;
    private javax.swing.JComboBox<String> difficultyComboBox;
    private javax.swing.JTextField healthTextField;
    private javax.swing.JTextField initialStateTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox<String> nameComboBox;
    private javax.swing.JTextField ninjaHeightTextField;
    private javax.swing.JComboBox<String> objectIdComboBox;
    private javax.swing.JTextField posXTextField;
    private javax.swing.JTextField posYTextField;
    private javax.swing.JTextField sceneIdTextField;
    private javax.swing.JTextField scoreTextField;
    private javax.swing.JComboBox<String> spawnModeComboBox;
    private javax.swing.JTextField triggerArgumentTextField;
    private javax.swing.JComboBox<String> triggerComboBox;
    private javax.swing.JCheckBox useBossSlotCheckBox;
    private javax.swing.JTextField vramTextField;
    // End of variables declaration//GEN-END:variables
}
