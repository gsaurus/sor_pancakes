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
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.filechooser.FileFilter;
import lib.ExceptionUtils;
import lib.NumberUtils;
import lib.Rom;
import lib.elc.AllLevelsLoadcues;
import lib.elc.BaseObject;
import lib.elc.CharacterObject;
import lib.elc.ItemObject;
import lib.elc.LevelLoadcues;
import lib.names.AllEnemyNames;

/**
 *
 * @author gil.costa
 */
public final class Sor2LevelEditor extends javax.swing.JFrame {
    
    private static final String VERSION = " v1.1.1";    
    private static final String TITLE = "SoR2 Level Editor" + VERSION;
    
    private static final String ORIGINAL_GUIDE_NAME = "guides/default.txt";
    private static final String SW_GUIDE_NAME = "guides/syndicate_wars_v2.txt";
    private static final String SW_SURVIVAL_GUIDE_NAME = "guides/syndicate_wars_v2 - survival.txt";
    
    private JFileChooser romChooser;
    private JFileChooser guideChooser;
    
    private NumberUtils.Formatter formatter;
    
    private Guide guide;
    private Rom rom;
    private AllLevelsLoadcues levels;
    private AllEnemyNames enemyNames;
    
    public static Sor2LevelEditor instance;
    
    
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
        setLocationRelativeTo(null);
        formatter = NumberUtils.decimalFormatter;
        setEnabledRecursively(false);
        difficultyComboBox.setSelectedItem("Mania");
        setupFileChoosers();
        setupMapListeners();
    }
    
    private boolean ReloadRandomizerSettings() {
        try {
            CharacterObject.readRandomizer();
        } catch(Exception e) {
            e.printStackTrace(System.err);
            JOptionPane.showMessageDialog(null, e, "Failed to parse randomizer.txt", ERROR_MESSAGE);
            return false;
        }
        return true;
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
        FileFilter filter = new FileFilter(){
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
        };
        romChooser.addChoosableFileFilter(filter);
        romChooser.setFileFilter(filter);
        romChooser.setDialogTitle("Open ROM");
        
        guideChooser = new JFileChooser(new File(Guide.GUIDES_DIR));
        filter = new FileFilter(){
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
        };
        guideChooser.addChoosableFileFilter(filter);
        guideChooser.setFileFilter(filter);
        guideChooser.setDialogTitle("Select Guide");
    }
    
    private void reloadObjectPanel(){
        if (objectContainerPanel.getComponentCount() > 0){
            Component component = objectContainerPanel.getComponent(0);
            if (component != null){
                if (component instanceof CharacterPanel){
                    CharacterPanel charPanel = ((CharacterPanel)component);
                    charPanel.formatter = formatter;
                    charPanel.reload();
                }else if (component instanceof GoodiePanel){
                    GoodiePanel goodiePanel = ((GoodiePanel)component);
                    goodiePanel.formatter = formatter;
                    goodiePanel.reload();
                }
            }
        }
    }
    
    private void clearObjectPanel(){
        storeUnsavedDataInJPanels();
        objectContainerPanel.removeAll();
        objectContainerPanel.revalidate();
        objectContainerPanel.repaint();
    }
    
    private void setupMapListeners(){
        
        mapPanel.selectionListener = (BaseObject object, int index) -> {
            clearObjectPanel();
            objectIndexLabel.setText("");
            if (object instanceof CharacterObject){
                CharacterPanel panel = new CharacterPanel((CharacterObject)object, formatter, enemyNames, guide);
                objectIndexLabel.setText(Integer.toString(index + 1));
                panel.listener = (CharacterObject charObj) -> {
                    reloadMap(false);
                };                
                objectContainerPanel.add(panel);
                objectContainerPanel.revalidate();
                objectContainerPanel.repaint();
            } else if (object instanceof ItemObject){
                GoodiePanel panel = new GoodiePanel((ItemObject)object, formatter, guide);
                objectIndexLabel.setText(Integer.toString(index + 1));
                panel.listener = (ItemObject charObj) -> {
                    reloadMap(false);
                };                
                objectContainerPanel.add(panel);
                objectContainerPanel.revalidate();
                objectContainerPanel.repaint();
            }
        };
        
        mapPanel.movedListener = (BaseObject object) -> {
            if (objectContainerPanel.getComponentCount() > 0){
                Component component = objectContainerPanel.getComponent(0);
                if (component != null){
                    if (component instanceof CharacterPanel){
                        ((CharacterPanel)component).reloadPosition();
                    }else if (component instanceof GoodiePanel){
                        ((GoodiePanel)component).reloadPosition();
                    }
                }
            }
        }; 
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
        enemyNames = null;
        setEnabledRecursively(false);
    }
    
    
    private void storeUnsavedDataInJPanels(){
        if (objectContainerPanel == null || objectContainerPanel.getComponentCount() == 0) return;
        Component component = objectContainerPanel.getComponent(0);
        if (component != null){
            if (component instanceof CharacterPanel){
                CharacterPanel charPanel = ((CharacterPanel)component);
                charPanel.save();
            }else if (component instanceof GoodiePanel){
                GoodiePanel goodiesPanel = ((GoodiePanel)component);
                goodiesPanel.save();
            }
        }
    }
    
    private boolean saveToRom(Rom rom, Guide guide){
        requestFocusInWindow();
        storeUnsavedDataInJPanels();
        // when saving between different guides, number of characters may vary
        int deltaObjectId = guide.numberOfMainCharacters - this.guide.numberOfMainCharacters;
        // Object ID is always even
        deltaObjectId *= 2;

        // save
         try {
            levels.write(rom.getRomFile(), guide.levelsLoadcuesAddress, deltaObjectId);
            if (enemyNames != null){
                enemyNames.write(rom, guide.enemyNamesAddress);
            }
            rom.fixChecksum();
            return true;
        } catch (IOException ex) {
            ExceptionUtils.showError(this, "Unable to save rom", ex);
        }
         return false;
    }
    
    
    private boolean save(){
        if (rom == null || guide == null){
            return false;
        }
        return saveToRom(rom, guide);
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
    
    
    Rom openRom(){
        // open rom
        Rom localRom = null;
        int returnVal = romChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = romChooser.getSelectedFile();
            String romName = file.getAbsolutePath();
            try {
                localRom = new Rom(new File(romName));
            } catch (FileNotFoundException ex) {
                ExceptionUtils.showError(this, "Unable to load rom", ex);
            }            
        }
        return localRom;
    }
    
    String askForGuideFile(){
        int returnVal = guideChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = guideChooser.getSelectedFile();
            return file.getAbsolutePath();
        }
        return null;
    }
    
    Guide openGuide(String guideName, Rom rom){
        try {
            return new Guide(guideName, rom);
        } catch (Exception ex) {
            ExceptionUtils.showError(this, "Unable to load guide " + guideName, ex);
        }
        return null;
    }
    
    
    boolean loadLevels(Rom rom){
        try {
            levels = new AllLevelsLoadcues(rom.getRomFile(), guide.levelsLoadcuesAddress, guide.numberOfLevels);
            return true;
        } catch (IOException ex) {
            ExceptionUtils.showError(this, "Unable to load levels", ex);
            return false;
        }
    }
    
    boolean loadEnemyNames(Rom rom){
        try {
            if (guide.enemyNamesAddress > 0){
                enemyNames = new AllEnemyNames(rom, guide.enemyNamesAddress, guide.totalNumberOfEnemyNames);
            }else{
                enemyNames = null;
            }
            return true;
        } catch (IOException ex) {
            ExceptionUtils.showError(this, "Unable to load enemy names", ex);
            return false;
        }
    }
    
    void openProject(String guideName){
        // close rom first
        if (askToSave()){ 
            rom = openRom();
            if (rom != null){
                guide = openGuide(guideName, rom);
                if (guide != null && loadLevels(rom) && loadEnemyNames(rom)){
                    setEnabledRecursively(true);
                    updateNumberOfLevels();
                    updateNumberOfScenes();
                    reloadMap(false);
                }else{
                    // Failed, close everything
                    close();
                }
            }else{
                close();
            }
        }
    }
    
    
    void exportProject(String guideName){
        if (rom == null || guide == null) return;
        Rom romToExport = openRom();
        if (romToExport != null){
            Guide guideToExport = openGuide(guideName, romToExport);
            saveToRom(romToExport, guideToExport);
            JOptionPane.showMessageDialog(this,
                "Export successful!", "Export successful",
                JOptionPane.INFORMATION_MESSAGE
            );
            try {
                romToExport.close();
            } catch (IOException ex) {
                ExceptionUtils.showError(this, "Closing rom failed", ex);
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
    
    
    void updateNumberOfLevels(){
        int maxLevels = guide.numberOfLevels;
        String[] levelOptions = new String[maxLevels];
        for (int i = 0 ; i < maxLevels; ++i){
            levelOptions[i] = Integer.toString(i+1);
        }
        ComboBoxModel model = new DefaultComboBoxModel(levelOptions);
        levelComboBox.setModel(model);
        levelComboBox.setSelectedIndex(0);
    }
    
    void updateNumberOfScenes(){
        int currentLevel = levelComboBox.getSelectedIndex();
        int numberOfScenes = levels.levels.get(currentLevel).getTotalNumberOfScenes();
        String[] sceneOptions = new String[numberOfScenes];
        for (int i = 0 ; i < numberOfScenes; ++i){
            sceneOptions[i] = Integer.toString(i+1);
        }
        ComboBoxModel model = new DefaultComboBoxModel(sceneOptions);
        sceneComboBox.setModel(model);
        sceneComboBox.setSelectedIndex(0);
    }
    
    void reloadMap(boolean mustClearObjectPanel){
        if (mustClearObjectPanel) clearObjectPanel();
        if (levels != null){
            int currentLevel = levelComboBox.getSelectedIndex();
            int currentScene = sceneComboBox.getSelectedIndex();        
            int minimumDifficulty = difficultyComboBox.getSelectedIndex();
            mapPanel.reload(levels.levels.get(currentLevel), currentLevel, currentScene, minimumDifficulty, guide);
        }else{
            mapPanel.clear();
        }
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
        levelComboBox = new javax.swing.JComboBox<>();
        sceneComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        numberFormatComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        difficultyComboBox = new javax.swing.JComboBox<>();
        showBackgroundCheckBox = new javax.swing.JCheckBox();
        showEnemiesPack1CheckBox = new javax.swing.JCheckBox();
        showGoodiesCheckBox = new javax.swing.JCheckBox();
        showEnemiesPack2CheckBox = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        editNamesButton = new javax.swing.JButton();
        objectIndexLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mapPanel = new main.MapPanel();
        objectContainerPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        openOriginalMenuItem = new javax.swing.JMenuItem();
        openSWMenuItem = new javax.swing.JMenuItem();
        openSWMenuItem1 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        saveMenuItem = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        randomizeSurvivalMenuItem = new javax.swing.JMenuItem();
        randomizeAllStagesMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(TITLE);
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Level:");

        jLabel2.setText("Scene:");

        levelComboBox.setMaximumRowCount(9);
        levelComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "versus" }));
        levelComboBox.setToolTipText("Display objects for a specific level");
        levelComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelComboBoxActionPerformed(evt);
            }
        });

        sceneComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));
        sceneComboBox.setToolTipText("<html>Display objects of a scene<br>in currently selected level</html>");
        sceneComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sceneComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("View as:");

        numberFormatComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Decimal", "Hexadecimal", "Binary" }));
        numberFormatComboBox.setToolTipText("<html>Display settings in different numerical bases<br>This allow you to better interpret certain values</html>");
        numberFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberFormatComboBoxActionPerformed(evt);
            }
        });

        jLabel4.setText("Difficulty:");

        difficultyComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Very Easy", "Easy", "Normal", "Hard", "Very Hard", "Mania" }));
        difficultyComboBox.setToolTipText("<html>Display all enemies that spawn at a specific difficulty</html>");
        difficultyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difficultyComboBoxActionPerformed(evt);
            }
        });

        showBackgroundCheckBox.setSelected(true);
        showBackgroundCheckBox.setText("Background");
        showBackgroundCheckBox.setToolTipText("<html>Display scene background<br>As a guideline for objets positioning</html>");
        showBackgroundCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showBackgroundCheckBoxActionPerformed(evt);
            }
        });

        showEnemiesPack1CheckBox.setSelected(true);
        showEnemiesPack1CheckBox.setText("Enemies pack 1");
        showEnemiesPack1CheckBox.setToolTipText("<html>Display enemies in pack 1 for the current scene<br>Enemies in sor2 are stored in two packs per scene</html>");
        showEnemiesPack1CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEnemiesPack1CheckBoxActionPerformed(evt);
            }
        });

        showGoodiesCheckBox.setSelected(true);
        showGoodiesCheckBox.setText("Items");
        showGoodiesCheckBox.setToolTipText("<html>Display item objects<br>Such as goodies and crates</html>");
        showGoodiesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showGoodiesCheckBoxActionPerformed(evt);
            }
        });

        showEnemiesPack2CheckBox.setSelected(true);
        showEnemiesPack2CheckBox.setText("Enemies pack 2");
        showEnemiesPack2CheckBox.setToolTipText("<html>Display enemies in pack 2 for the current scene<br>Enemies in sor2 are stored in two packs per scene</html>");
        showEnemiesPack2CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEnemiesPack2CheckBoxActionPerformed(evt);
            }
        });

        jButton1.setText("|>");
        jButton1.setToolTipText("<html>Select next enemy or object<br>And focus the view on it</html>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("<|");
        jButton2.setToolTipText("<html>Select previous enemy or object<br>And focus the view on it</html>");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        editNamesButton.setText("Edit Names");
        editNamesButton.setToolTipText("<html>Edit the list of all enemy names<br>The names that display on game HUD</html>");
        editNamesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editNamesButtonActionPerformed(evt);
            }
        });

        objectIndexLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        objectIndexLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        objectIndexLabel.setText("      ");
        objectIndexLabel.setToolTipText("<html>Spawning order of the selected enemy or object</html>");
        objectIndexLabel.setFocusable(false);
        objectIndexLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sceneComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(levelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(difficultyComboBox, 0, 130, Short.MAX_VALUE)
                    .addComponent(numberFormatComboBox, 0, 1, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showBackgroundCheckBox)
                    .addComponent(showGoodiesCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(showEnemiesPack1CheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(objectIndexLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(showEnemiesPack2CheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editNamesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)))
                .addContainerGap(485, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(levelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(difficultyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showBackgroundCheckBox)
                    .addComponent(showEnemiesPack1CheckBox)
                    .addComponent(jButton2)
                    .addComponent(objectIndexLabel)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(sceneComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(numberFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(showGoodiesCheckBox)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(showEnemiesPack2CheckBox)
                        .addComponent(editNamesButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mapPanel.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(mapPanel);

        objectContainerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        objectContainerPanel.setMinimumSize(new java.awt.Dimension(209, 0));
        objectContainerPanel.setLayout(new java.awt.BorderLayout());

        jMenu1.setText("File");

        jMenu5.setText("Open...");

        openOriginalMenuItem.setText("Original");
        openOriginalMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openOriginalMenuItemActionPerformed(evt);
            }
        });
        jMenu5.add(openOriginalMenuItem);

        openSWMenuItem.setText("Syndicate Wars 2.x");
        openSWMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSWMenuItemActionPerformed(evt);
            }
        });
        jMenu5.add(openSWMenuItem);

        openSWMenuItem1.setText("SW 2.x Survival");
        openSWMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openSWMenuItem1ActionPerformed(evt);
            }
        });
        jMenu5.add(openSWMenuItem1);
        jMenu5.add(jSeparator5);

        jMenuItem5.setText("Specific Guide");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem5);

        jMenu1.add(jMenu5);
        jMenu1.add(jSeparator3);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.META_DOWN_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenuItem);

        jMenu4.setText("Export...");

        jMenuItem6.setText("To Original");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuItem7.setText("To Syndicate Wars");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenu1.add(jMenu4);
        jMenu1.add(jSeparator2);

        randomizeSurvivalMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0));
        randomizeSurvivalMenuItem.setText("Randomize Survival");
        randomizeSurvivalMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomizeSurvivalMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(randomizeSurvivalMenuItem);

        randomizeAllStagesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        randomizeAllStagesMenuItem.setText("Randomize All Stages");
        randomizeAllStagesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomizeAllStagesMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(randomizeAllStagesMenuItem);
        jMenu1.add(jSeparator4);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.META_DOWN_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem1.setText("Enemy Names");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);
        jMenu2.add(jSeparator1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, 0));
        jMenuItem2.setText("Previous Object");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, 0));
        jMenuItem3.setText("Next Object");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");

        jMenuItem4.setText("About");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(objectContainerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(244, 244, 244)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(objectContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(64, 64, 64)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void levelComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelComboBoxActionPerformed
        updateNumberOfScenes();
        reloadMap(true);
    }//GEN-LAST:event_levelComboBoxActionPerformed

    private void sceneComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sceneComboBoxActionPerformed
        reloadMap(true);
    }//GEN-LAST:event_sceneComboBoxActionPerformed

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
            reloadObjectPanel();
        }
    }//GEN-LAST:event_numberFormatComboBoxActionPerformed

    private void difficultyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difficultyComboBoxActionPerformed
        reloadMap(true);
    }//GEN-LAST:event_difficultyComboBoxActionPerformed

    private void showBackgroundCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showBackgroundCheckBoxActionPerformed
        mapPanel.showBackground = showBackgroundCheckBox.isSelected();
        mapPanel.refreshGraphics();
    }//GEN-LAST:event_showBackgroundCheckBoxActionPerformed

    private void showEnemiesPack1CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEnemiesPack1CheckBoxActionPerformed
        mapPanel.showEnemies1 = showEnemiesPack1CheckBox.isSelected();
        reloadMap(true);
    }//GEN-LAST:event_showEnemiesPack1CheckBoxActionPerformed

    private void showGoodiesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showGoodiesCheckBoxActionPerformed
        mapPanel.showItems = showGoodiesCheckBox.isSelected();
        reloadMap(true);
    }//GEN-LAST:event_showGoodiesCheckBoxActionPerformed

    private void showEnemiesPack2CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEnemiesPack2CheckBoxActionPerformed
        mapPanel.showEnemies2 = showEnemiesPack2CheckBox.isSelected();
        reloadMap(true);
    }//GEN-LAST:event_showEnemiesPack2CheckBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mapPanel.selectNext();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        mapPanel.selectPrevious();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        editNamesButtonActionPerformed(null);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void editNamesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editNamesButtonActionPerformed
        if (enemyNames != null){
            NamesEditorForm namesEditor = new NamesEditorForm(enemyNames);
            namesEditor.setLocationRelativeTo(null);
            namesEditor.setVisible(true);
        }else{
            ExceptionUtils.showError(this, "Enemy names couldn't be loaded using the guide with this rom");
        }
    }//GEN-LAST:event_editNamesButtonActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        jButton2ActionPerformed(null);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        jButton1ActionPerformed(null);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JOptionPane.showMessageDialog(this,
            "<html><h2>" + TITLE + " 2021</h2><br><br>" +
            "Tool: <b>gsaurus</b><br>" +
            "Knowledge expert: <b>Red Crimson</b><br>" +
            "Backgrounds by: <b>darrenagar</b><br><br>" +
            "Acknowledgment on derived work<br>"+
            "would be appreciated but is not required<br><br>"+
            "Spawnage of Rage 2 is free software.<br>The authors can not be held responsible<br>for any illicit use of this program.<br></html>",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        exportProject(ORIGINAL_GUIDE_NAME);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        exportProject(SW_GUIDE_NAME);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        String guideFileName = askForGuideFile();
        if (guideFileName != null){
            openProject(guideFileName);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void randomizeSurvivalMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomizeSurvivalMenuItemActionPerformed
        if (rom == null || guide == null || !ReloadRandomizerSettings()){
            return;
        }
        requestFocusInWindow();
        storeUnsavedDataInJPanels();
        levels.levels.get(0).randomizeEnemiesListOne(enemyNames.address);
        reloadMap(true);
    }//GEN-LAST:event_randomizeSurvivalMenuItemActionPerformed

    private void randomizeAllStagesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomizeAllStagesMenuItemActionPerformed
       if (rom == null || guide == null || !ReloadRandomizerSettings()){
            return;
        }
        requestFocusInWindow();
        storeUnsavedDataInJPanels();
        List<LevelLoadcues> allLevels = levels.levels;
        int totalEnemies = 0;
        for (LevelLoadcues level: allLevels) {
            totalEnemies += level.getTotalEnemies();
        }
        int spawnCount = 0;
        for (LevelLoadcues level: allLevels) {
            spawnCount = level.randomizeEnemies(enemyNames.address, spawnCount, totalEnemies);
        }
        reloadMap(true);
    }//GEN-LAST:event_randomizeAllStagesMenuItemActionPerformed

    private void openSWMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openSWMenuItem1ActionPerformed
        openProject(SW_SURVIVAL_GUIDE_NAME);
    }//GEN-LAST:event_openSWMenuItem1ActionPerformed

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
                instance = new Sor2LevelEditor();
                instance.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> difficultyComboBox;
    private javax.swing.JButton editNamesButton;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JComboBox<String> levelComboBox;
    private main.MapPanel mapPanel;
    private javax.swing.JComboBox<String> numberFormatComboBox;
    private javax.swing.JPanel objectContainerPanel;
    private javax.swing.JLabel objectIndexLabel;
    private javax.swing.JMenuItem openOriginalMenuItem;
    private javax.swing.JMenuItem openSWMenuItem;
    private javax.swing.JMenuItem openSWMenuItem1;
    private javax.swing.JMenuItem randomizeAllStagesMenuItem;
    private javax.swing.JMenuItem randomizeSurvivalMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JComboBox<String> sceneComboBox;
    private javax.swing.JCheckBox showBackgroundCheckBox;
    private javax.swing.JCheckBox showEnemiesPack1CheckBox;
    private javax.swing.JCheckBox showEnemiesPack2CheckBox;
    private javax.swing.JCheckBox showGoodiesCheckBox;
    // End of variables declaration//GEN-END:variables
}
