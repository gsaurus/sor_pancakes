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
    private final ObjectDefinition objectDefinition;
    private final AllEnemyNames enemyNames;
    private boolean isReloading;
    public NumberUtils.Formatter formatter;
    public ImportantChangeListener listener;
    
    
    public CharacterPanel(CharacterObject object, NumberUtils.Formatter formatter, AllEnemyNames enemyNames, Guide guide) {
        initComponents();
        this.object = object;
        this.formatter = formatter;
        this.enemyNames = enemyNames;
        int objIndex = object.objectId / 2;
        if (objIndex >= 0 && objIndex < guide.objectsDefinition.size()){
            objectDefinition = guide.objectsDefinition.get(objIndex);
        }else{
            objectDefinition = null;
        }
        nameComboBox.setModel(new DefaultComboBoxModel(enemyNames.allNames.toArray()));
        String[] objectNames = new String[guide.objectsDefinition.size()];
        for (int i = 0 ; i < guide.objectsDefinition.size(); ++i){
            ObjectDefinition definition = guide.objectsDefinition.get(i);
            if (definition != null){
                objectNames[i] = definition.name;
            }else{
                objectNames[i] = "";
            }
        }
        if (objectDefinition != null){
            spawnModeComboBox.setModel(new DefaultComboBoxModel(objectDefinition.spawnModes.toArray()));
        }else{
            spawnModeComboBox.setModel(new DefaultComboBoxModel(new String[]{}));
        }
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
            comboBox.setSelectedItem(value);
        }
    }
    
    void setComboValue(JComboBox comboBox, int index, long value){
        setComboValue(comboBox, index, formatter.toString(value));
    }
    void setComboValue(JComboBox comboBox, int index, int value){
        setComboValue(comboBox, index, formatter.toString(value));
    }
    void setComboValue(JComboBox comboBox, int index, short value){
        setComboValue(comboBox, index, formatter.toString(value));
    }
    void setComboValue(JComboBox comboBox, int index, byte value){
        setComboValue(comboBox, index, formatter.toString(value));
    }
    
    
    public void reloadPosition(){
        boolean wasReloading = isReloading;
        isReloading = true;
        posXTextField.setText(formatter.toString((short)object.posX));
        posYTextField.setText(formatter.toString((short)object.posY));
        isReloading = wasReloading;
    }
    
    public void reload(){
        isReloading = true;
        
        setComboValue(difficultyComboBox, object.minimumDifficulty, (byte)object.minimumDifficulty);
        int nameIndex = (int)(object.nameAddress - enemyNames.address) / AllEnemyNames.NAME_SIZE;
        setComboValue(nameComboBox, nameIndex, object.nameAddress);
        setComboValue(objectIdComboBox, object.objectId / 2, (byte)object.objectId);
        setComboValue(spawnModeComboBox, object.introductionType, (byte)object.introductionType);
        setComboValue(triggerComboBox, object.triggerType, (byte)object.triggerType);
        
        aggressivenessTextField.setText(formatter.toString((byte)object.enemyAgressiveness));
        alternativePaletteCheckBox.setSelected(object.useAlternativePalette);
        bboxDeptTextField.setText(formatter.toString((byte)object.collisionDept));
        bboxHeightTextField.setText(formatter.toString((byte)object.collisionHeight));
        bboxWidhTextField.setText(formatter.toString((byte)object.collisionWidth));
        healthTextField.setText(formatter.toString((byte)object.health));
        initialStateTextField.setText(formatter.toString((byte)object.initialState));
        sceneIdTextField.setText(formatter.toString((byte)object.sceneId));
        scoreTextField.setText(formatter.toString((short)object.deathScore));        
        triggerArgumentTextField.setText(formatter.toString((short)object.triggerArgument));
        vramTextField.setText(formatter.toString((short)object.vram));
        reloadPosition();
        
        isReloading = false;
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

        jLabel1.setText("Character ID:");

        jLabel2.setText("Scene ID:");

        jLabel3.setText("Position:");

        jLabel4.setText("Difficulty:");

        alternativePaletteCheckBox.setText("Alternative Palette");
        alternativePaletteCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alternativePaletteCheckBoxActionPerformed(evt);
            }
        });

        jLabel5.setText("Trigger:");

        jLabel6.setText("Trigg Arg:");

        jLabel7.setText("Aggressiveness:");

        jLabel8.setText("Spawn Mode:");

        jLabel9.setText("Initial State:");

        jLabel10.setText("Health:");

        jLabel11.setText("Bounding Box:");

        jLabel12.setText("x");

        jLabel13.setText("x");

        jLabel14.setText("Score:");

        jLabel15.setText("Name:");

        jLabel16.setText("VRAM:");

        objectIdComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        objectIdComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objectIdComboBoxActionPerformed(evt);
            }
        });

        sceneIdTextField.setText("jTextField1");
        sceneIdTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sceneIdTextFieldActionPerformed(evt);
            }
        });

        jLabel17.setText("x");

        posXTextField.setText("jTextField1");
        posXTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                posXTextFieldActionPerformed(evt);
            }
        });

        posYTextField.setText("jTextField1");
        posYTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                posYTextFieldActionPerformed(evt);
            }
        });

        difficultyComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Very Easy", "Easy", "Normal", "Hard", "Very Hard", "Mania" }));
        difficultyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difficultyComboBoxActionPerformed(evt);
            }
        });

        aggressivenessTextField.setText("jTextField1");
        aggressivenessTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aggressivenessTextFieldActionPerformed(evt);
            }
        });

        triggerComboBox.setEditable(true);
        triggerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        triggerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerComboBoxActionPerformed(evt);
            }
        });

        triggerArgumentTextField.setText("jTextField1");
        triggerArgumentTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerArgumentTextFieldActionPerformed(evt);
            }
        });

        spawnModeComboBox.setEditable(true);
        spawnModeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        spawnModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spawnModeComboBoxActionPerformed(evt);
            }
        });

        initialStateTextField.setText("jTextField1");
        initialStateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialStateTextFieldActionPerformed(evt);
            }
        });

        healthTextField.setText("jTextField1");
        healthTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                healthTextFieldActionPerformed(evt);
            }
        });

        scoreTextField.setText("jTextField1");
        scoreTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreTextFieldActionPerformed(evt);
            }
        });

        bboxWidhTextField.setText("jTextField1");
        bboxWidhTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bboxWidhTextFieldActionPerformed(evt);
            }
        });

        bboxHeightTextField.setText("jTextField1");
        bboxHeightTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bboxHeightTextFieldActionPerformed(evt);
            }
        });

        bboxDeptTextField.setText("jTextField1");
        bboxDeptTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bboxDeptTextFieldActionPerformed(evt);
            }
        });

        nameComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        nameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameComboBoxActionPerformed(evt);
            }
        });

        vramTextField.setText("jTextField1");
        vramTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vramTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(posXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(posYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(difficultyComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(18, 18, 18)
                            .addComponent(triggerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(aggressivenessTextField))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(initialStateTextField))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(healthTextField))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(triggerArgumentTextField))
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
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(objectIdComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sceneIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(spawnModeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(alternativePaletteCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(initialStateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(vramTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void objectIdComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objectIdComboBoxActionPerformed
        int index  = objectIdComboBox.getSelectedIndex();
        if (index != -1){
            object.objectId = index * 2;
        }else{
            object.objectId = (int)formatter.toNumber((String)objectIdComboBox.getSelectedItem());
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
    }//GEN-LAST:event_triggerComboBoxActionPerformed

    private void triggerArgumentTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerArgumentTextFieldActionPerformed
        object.triggerArgument = (int)formatter.toNumber(triggerArgumentTextField.getText());
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
        object.deathScore = (int)formatter.toNumber(vramTextField.getText());
    }//GEN-LAST:event_vramTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aggressivenessTextField;
    private javax.swing.JCheckBox alternativePaletteCheckBox;
    private javax.swing.JTextField bboxDeptTextField;
    private javax.swing.JTextField bboxHeightTextField;
    private javax.swing.JTextField bboxWidhTextField;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox<String> nameComboBox;
    private javax.swing.JComboBox<String> objectIdComboBox;
    private javax.swing.JTextField posXTextField;
    private javax.swing.JTextField posYTextField;
    private javax.swing.JTextField sceneIdTextField;
    private javax.swing.JTextField scoreTextField;
    private javax.swing.JComboBox<String> spawnModeComboBox;
    private javax.swing.JTextField triggerArgumentTextField;
    private javax.swing.JComboBox<String> triggerComboBox;
    private javax.swing.JTextField vramTextField;
    // End of variables declaration//GEN-END:variables
}
