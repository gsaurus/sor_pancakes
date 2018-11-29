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
import lib.elc.ItemObject;
import lib.elc.ObjectDefinition;

/**
 *
 * @author gil.costa
 */
public final class GoodiePanel extends javax.swing.JPanel {
    
    private static final String[] CONTAINED_ITEMS = {
        "Knife", "Pipe", "Unused", "Sword", "Kunai", "Cash", "Gold", "1UP", "Apple", "Chicken", "Explosion"
    };
    
    public interface ImportantChangeListener{
        void onObjectChanged(ItemObject object);
    }
    

//private static final String INVALID_OPTION = "Unknown";
    private final ItemObject object;
    private boolean isReloading;
    public NumberUtils.Formatter formatter;
    public ImportantChangeListener listener;
    
    
    public GoodiePanel(ItemObject object, NumberUtils.Formatter formatter, Guide guide) {
        initComponents();
        this.object = object;
        this.formatter = formatter;
        String[] objectNames = new String[guide.objectsDefinition.size()];
        for (int i = 0 ; i < guide.objectsDefinition.size(); ++i){
            ObjectDefinition definition = guide.objectsDefinition.get(i);
            if (definition != null){
                objectNames[i] = definition.name;
            }else{
                objectNames[i] = "";
            }
        }
        objectIdComboBox.setModel(new DefaultComboBoxModel(objectNames));
        insideComboBox.setModel(new DefaultComboBoxModel(CONTAINED_ITEMS));
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
    
    void setComboValue(JComboBox comboBox, int index, int value){
        setComboValue(comboBox, index, formatter.toUnsignedString(value));
    }
    void setSignedComboValue(JComboBox comboBox, int index, int value){
        setComboValue(comboBox, index, formatter.toString((byte)value));
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
        setComboValue(objectIdComboBox, object.objectId / 2, object.objectId);
        setSignedComboValue(insideComboBox, object.containedItemId, object.containedItemId);
        
        inside2PlayersCheckBox.setSelected(object.insideForTwoPlayersOnly);
        bboxDeptTextField.setText(formatter.toString((byte)object.collisionDept));
        bboxHeightTextField.setText(formatter.toString((byte)object.collisionHeight));
        bboxWidhTextField.setText(formatter.toString((byte)object.collisionWidth));
        animationTextField.setText(formatter.toUnsignedString(object.animation));
        initialStateTextField.setText(formatter.toUnsignedString(object.status));
        sceneIdTextField.setText(formatter.toUnsignedString(object.sceneId));
        heightTextField.setText(formatter.toString((short)object.verticalSpeed));
        spriteStatusTextField.setText(formatter.toUnsignedString(object.spriteStatus));
        reloadPosition();
        
        isReloading = false;
    }
    
    public void save(){
        isReloading = true;
        objectIdComboBoxActionPerformed(null);
        sceneIdTextFieldActionPerformed(null);
        posXTextFieldActionPerformed(null);
        posYTextFieldActionPerformed(null);
        initialStateTextFieldActionPerformed(null);
        animationTextFieldActionPerformed(null);
        heightTextFieldActionPerformed(null);
        bboxWidhTextFieldActionPerformed(null);
        bboxHeightTextFieldActionPerformed(null);
        bboxDeptTextFieldActionPerformed(null);
        spriteStatusTextFieldActionPerformed(null);
        insideComboBoxActionPerformed(null);
        inside2PlayersCheckBoxActionPerformed(null);
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
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        objectIdComboBox = new javax.swing.JComboBox<>();
        sceneIdTextField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        posXTextField = new javax.swing.JTextField();
        posYTextField = new javax.swing.JTextField();
        initialStateTextField = new javax.swing.JTextField();
        animationTextField = new javax.swing.JTextField();
        heightTextField = new javax.swing.JTextField();
        bboxWidhTextField = new javax.swing.JTextField();
        bboxHeightTextField = new javax.swing.JTextField();
        bboxDeptTextField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        spriteStatusTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        inside2PlayersCheckBox = new javax.swing.JCheckBox();
        insideComboBox = new javax.swing.JComboBox<>();

        jLabel1.setText("Item ID:");

        jLabel2.setText("Scene ID:");

        jLabel3.setText("Position:");

        jLabel9.setText("Initial State:");

        jLabel10.setText("Animation:");

        jLabel11.setText("Bounding Box:");

        jLabel12.setText("x");

        jLabel13.setText("x");

        jLabel14.setText("Height:");

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

        initialStateTextField.setText("jTextField1");
        initialStateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialStateTextFieldActionPerformed(evt);
            }
        });

        animationTextField.setText("jTextField1");
        animationTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                animationTextFieldActionPerformed(evt);
            }
        });

        heightTextField.setText("jTextField1");
        heightTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightTextFieldActionPerformed(evt);
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

        jLabel18.setText("Sprite Status:");

        spriteStatusTextField.setText("jTextField1");
        spriteStatusTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spriteStatusTextFieldActionPerformed(evt);
            }
        });

        jLabel15.setText("Inside:");

        inside2PlayersCheckBox.setText("Inside for 2 Players only");
        inside2PlayersCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inside2PlayersCheckBoxActionPerformed(evt);
            }
        });

        insideComboBox.setEditable(true);
        insideComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insideComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(heightTextField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(initialStateTextField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(animationTextField))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spriteStatusTextField))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(insideComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(objectIdComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sceneIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                                .addComponent(jLabel11))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(inside2PlayersCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
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
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(insideComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inside2PlayersCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(initialStateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(animationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(spriteStatusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(bboxWidhTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bboxHeightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bboxDeptTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
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

    private void initialStateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initialStateTextFieldActionPerformed
        object.status = (int)formatter.toNumber(initialStateTextField.getText());
    }//GEN-LAST:event_initialStateTextFieldActionPerformed

    private void animationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_animationTextFieldActionPerformed
        object.animation = (int)formatter.toNumber(animationTextField.getText());
    }//GEN-LAST:event_animationTextFieldActionPerformed

    private void heightTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightTextFieldActionPerformed
        object.verticalSpeed = (int)formatter.toNumber(heightTextField.getText());
    }//GEN-LAST:event_heightTextFieldActionPerformed

    private void bboxWidhTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bboxWidhTextFieldActionPerformed
        object.collisionWidth = (int)formatter.toNumber(bboxWidhTextField.getText());
    }//GEN-LAST:event_bboxWidhTextFieldActionPerformed

    private void bboxHeightTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bboxHeightTextFieldActionPerformed
        object.collisionHeight = (int)formatter.toNumber(bboxHeightTextField.getText());
    }//GEN-LAST:event_bboxHeightTextFieldActionPerformed

    private void bboxDeptTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bboxDeptTextFieldActionPerformed
        object.collisionDept = (int)formatter.toNumber(bboxDeptTextField.getText());
    }//GEN-LAST:event_bboxDeptTextFieldActionPerformed

    private void spriteStatusTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spriteStatusTextFieldActionPerformed
        object.spriteStatus = (int)formatter.toNumber(spriteStatusTextField.getText());
    }//GEN-LAST:event_spriteStatusTextFieldActionPerformed

    private void insideComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insideComboBoxActionPerformed
        int index  = insideComboBox.getSelectedIndex();
        if (index != -1){
            object.containedItemId = index;
        }else{
            object.containedItemId = (int)formatter.toNumber((String)insideComboBox.getSelectedItem());
        }
    }//GEN-LAST:event_insideComboBoxActionPerformed

    private void inside2PlayersCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inside2PlayersCheckBoxActionPerformed
        object.insideForTwoPlayersOnly = inside2PlayersCheckBox.isSelected();
    }//GEN-LAST:event_inside2PlayersCheckBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField animationTextField;
    private javax.swing.JTextField bboxDeptTextField;
    private javax.swing.JTextField bboxHeightTextField;
    private javax.swing.JTextField bboxWidhTextField;
    private javax.swing.JTextField heightTextField;
    private javax.swing.JTextField initialStateTextField;
    private javax.swing.JCheckBox inside2PlayersCheckBox;
    private javax.swing.JComboBox<String> insideComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox<String> objectIdComboBox;
    private javax.swing.JTextField posXTextField;
    private javax.swing.JTextField posYTextField;
    private javax.swing.JTextField sceneIdTextField;
    private javax.swing.JTextField spriteStatusTextField;
    // End of variables declaration//GEN-END:variables
}
