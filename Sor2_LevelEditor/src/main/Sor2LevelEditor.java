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

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import lib.ExceptionUtils;
import lib.NumberUtils;
import lib.Rom;
import lib.elc.AllLevelsLoadcues;

/**
 *
 * @author gil.costa
 */
public class Sor2LevelEditor extends javax.swing.JFrame {
    
    private static final String ORIGINAL_GUIDE_NAME = "guides/default.txt";
    private static final String SW_GUIDE_NAME = "guides/syndicate_wars.txt";
    
    private JFileChooser romChooser;
    
    private NumberUtils.Formatter formatter;
    
    private Guide guide;
    private Rom rom;
    private AllLevelsLoadcues levels;
    
    
/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
//////                                                 //////
//////              OPEN  -  SAVE  -  CLOSE            //////
//////                                                 //////
/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
    
    /**
     * Creates new form Sor2LevelEditor
     */
    public Sor2LevelEditor() {
        initComponents();
        formatter = NumberUtils.decimalFormatter;
        setEnabledRecursively(false);
        setupFileChoosers();
    }
    
    
    private Component[] getComponents(Component container) {
        ArrayList<Component> list = null;
        try {
            list = new ArrayList<>(Arrays.asList(
                  ((Container) container).getComponents()));
            for (int index = 0; index < list.size(); index++) {
                for (Component currentComponent : getComponents(list.get(index))) {
                    list.add(currentComponent);
                }
            }
        } catch (ClassCastException e) {
            list = new ArrayList<>();
        }

        return list.toArray(new Component[list.size()]);
    }

    private void setEnabledRecursively(boolean enabled){
        for(Component component : getComponents(this)) {
            if (!(component instanceof JMenuItem || component instanceof JMenu)){
                component.setEnabled(enabled);
            }
        }
    }
    
    
    
    private void setupFileChoosers(){
        romChooser = new JFileChooser(new File("."));
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
    }
    
    private void close(){
        if (rom != null){
            try {
                rom.close();
            } catch (IOException ex) {
                ExceptionUtils.showError(this, "Closing rom failed", ex);
            }
            rom = null;
        }
        guide = null;
        levels = null;
        setEnabledRecursively(false);
    }
    
    
    private boolean save(){
        // save
         try {
            levels.write(rom.getRomFile());
            return true;
        } catch (IOException ex) {
            ExceptionUtils.showError(this, "Unable to save rom", ex);
            return false;
        }
    }
    
    
    private boolean askToSave(){
        if (levels == null) return true;
        int option = JOptionPane.showConfirmDialog(
                this,
                "Save before closing the current project?",
                "Save changes?",
                JOptionPane.YES_NO_CANCEL_OPTION
        );
        if (option == JOptionPane.YES_OPTION){
            if (save()){
                close();
                return true;
            }
        }else if (option == JOptionPane.NO_OPTION){
            close();
            return true;
        }
        return false;
    }
    
    
    boolean openRom(){
        // open rom
        int returnVal = romChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = romChooser.getSelectedFile();
            String romName = file.getAbsolutePath();
            try {
                rom = new Rom(new File(romName));
                return true;
            } catch (FileNotFoundException ex) {
                ExceptionUtils.showError(this, "Unable to load rom", ex);
            }            
        }
        return false;
    }
    
    boolean openGuide(String guideName){
        try {
            guide = new Guide(guideName, rom);
            return true;
        } catch (Exception ex) {
            ExceptionUtils.showError(this, "Unable to load guide " + guideName, ex);
        }
        return false;
    }
    
    
    boolean loadLevels(){
        try {
            levels = new AllLevelsLoadcues(rom.getRomFile(), guide.levelsLoadcuesAddress);
            return true;
        } catch (IOException ex) {
            ExceptionUtils.showError(this, "Unable to load levels", ex);
            return false;
        }
    }
    
    void openProject(String guideName){
        // close rom first
        if (askToSave()){        
            if (openRom() && openGuide(guideName) &&loadLevels()){
                setEnabledRecursively(true);
                reloadMap();
            }else{
                // Failed, close everything
                close();
            }
        }
    }
    
    
    
/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
//////                                                 //////
//////                   EDITOR UI                     //////
//////                                                 //////
/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////
    
    
    void reloadAllFields(){
        
    }
    
    void reloadMap(){
        int currentLevel = 0;
        int currentScene = 0;
        mapPanel.reload(levels.levels.get(currentLevel), currentScene, guide);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        numberFormatComboBox = new javax.swing.JComboBox<>();
        mapPanel = new main.MapPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        openOriginalMenuItem = new javax.swing.JMenuItem();
        openSWMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        saveMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Level:");

        jLabel2.setText("Scene:");

        jComboBox1.setEditable(true);
        jComboBox1.setMaximumRowCount(9);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "versus" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setEditable(true);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "all" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel3.setText("View as:");

        numberFormatComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "decimal", "hexadecimal", "binary" }));
        numberFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberFormatComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numberFormatComboBox, 0, 131, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(numberFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(341, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 648, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 456, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        openOriginalMenuItem.setText("Open Original");
        openOriginalMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openOriginalMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(openOriginalMenuItem);

        openSWMenuItem.setText("Open SW");
        openSWMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSWMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(openSWMenuItem);
        jMenu1.add(jSeparator3);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.META_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenuItem);
        jMenu1.add(jSeparator2);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.META_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(651, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(201, 201, 201)
                    .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (askToSave()){
            close();
            System.exit(0);
        }
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void openOriginalMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openOriginalMenuItemActionPerformed
        openProject(ORIGINAL_GUIDE_NAME);
    }//GEN-LAST:event_openOriginalMenuItemActionPerformed

    private void openSWMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSWMenuItemActionPerformed
        openProject(SW_GUIDE_NAME);
    }//GEN-LAST:event_openSWMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        save();
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (askToSave()){
            close();
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void numberFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberFormatComboBoxActionPerformed
        NumberUtils.Formatter oldFormatter = formatter;
        switch (numberFormatComboBox.getSelectedIndex()){
            case 1:
                formatter = NumberUtils.hexadecimalFormatter;
                break;
            case 2:
                formatter = NumberUtils.binaryFormatter;
                break;
            default:
                formatter = NumberUtils.decimalFormatter;
        }
        if (formatter != oldFormatter){
            reloadAllFields();
        }
    }//GEN-LAST:event_numberFormatComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sor2LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sor2LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sor2LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sor2LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sor2LevelEditor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private main.MapPanel mapPanel;
    private javax.swing.JComboBox<String> numberFormatComboBox;
    private javax.swing.JMenuItem openOriginalMenuItem;
    private javax.swing.JMenuItem openSWMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    // End of variables declaration//GEN-END:variables
}
