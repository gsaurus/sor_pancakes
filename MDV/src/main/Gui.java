/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import lib.*;




/**
 *
 * @author Gil
 */
public class Gui extends javax.swing.JFrame{
    
    private static final String VERSION = "0.8";
    // TODO: ask save when loading new bank or exiting, or opening other rom
    
    private static final String TITLE = "MDV " + VERSION;
    
    private String currentDirectory;
    private JFileChooser romChooser;
    private JFileChooser soundChooser;
    private JFileChooser guideChooser;        
    
    private ComponentsHelper helper;
    
    private JComponent focusedComponent;
    private int copiedId;
    
    private File guiFile;
    
    private boolean askingSave;
         
    
    
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
        soundChooser.addChoosableFileFilter(new FileFilter(){
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return
                        filename.endsWith(".wav") || filename.endsWith(".au")
                     || filename.endsWith(".aif") || filename.endsWith(".aiff")
                     || filename.endsWith(".aifc");
            }
            @Override
            public String getDescription() {
                return "Sound file (wav, au, aif, aiff, aifc)";
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
    }
          
    
    
    private void voicePitchChanged() {
        int index = voicesList.getSelectedIndex();
        if (index == -1) return;
        if (!helper.aquire(voicePitchField)) return; // in use, ignore input        
        Voice voice = GlobalData.bank.getVoice(index);
        int newPitch = helper.getIntFromField(voicePitchField, 0, 255);
        if (newPitch == ComponentsHelper.INVALID_INT){
            voicePitchField.setBackground(Color.red);
        } else{
            voicePitchField.setBackground(Color.white);
            if (newPitch != voice.getPitch()){
                GlobalData.bank.setVoicePitch(index, newPitch);
                updateMenusEnablings();
            }
        }
        helper.release(voicePitchField);
    }
    
    
    private void setupFields(){       
        voicePitchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                voicePitchChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                voicePitchChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                voicePitchChanged();
            }
        });
    }
    
    private void preInit() {       
        helper = new ComponentsHelper();
        currentDirectory = new File(".").getAbsolutePath();
        romChooser = new JFileChooser(currentDirectory);        
        soundChooser = new JFileChooser(currentDirectory);
        guideChooser = new JFileChooser(currentDirectory + "/" + GlobalData.GUIDES_DIR);
        guideChooser.setDialogTitle("Open voices guide");
        askingSave = false;
        
        try {            
            Image icon = ImageIO.read(new File("icon.png"));
            this.setIconImage(icon);
        } catch (IOException ex) { /* Ignore */ }
    }
    
    private void postInit(){
        setupFileChoosers();
        setupFields();                
    }

    /**
     * Creates new form Gui
     */
    public Gui() {        
        preInit();      
        initComponents();
        setVisible(true);
        postInit();  
    }
    
    
    private void openRom() {
        if (!checkModifications()) return;
        romChooser.setDialogTitle("Open a Sega Megadrive/Genesis ROM");
        int returnVal = romChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = romChooser.getSelectedFile();
            if (file.canRead()){
                GlobalData.fullRomName = file.getAbsolutePath();            
                GlobalData.romName = file.getName();
                setTitle(TITLE + " - "  + GlobalData.romName);
                ComponentsHelper.setEnable(mainPanel, GlobalData.fullRomName != null);
                updateEnablings();
                guiFile = GlobalData.guessGuide();
                reloadMenu.setEnabled(guiFile != null);
                if (guiFile != null){
                    updateBankCombo();                    
                }else{
                    GlobalData.bank = null;                    
                    bankCombo.removeAllItems();
                    updateEnablings();
                }
                refresh();
            }else{
                ComponentsHelper.showError("File \"" + file.getName() + "\" cannot be read");
            }
        }
    }     
    
    
    private void loadGuide(){        
        if (!checkModifications()) return;
        if (guiFile.canRead()){
            try {
                GlobalData.readGuide(guiFile);
            } catch (Exception ex) {
                ex.printStackTrace();
                ComponentsHelper.showError("Unable to read guide from file \" " + guiFile.getName() + "\"");
            }
            updateBankCombo();
            refresh();
        }else{
            ComponentsHelper.showError("File \"" + guiFile.getName() + "\" cannot be read");
        }
        reloadMenu.setEnabled(guiFile != null);
    }
    
    private void openGuide() {
        if (!checkModifications()) return;
        int returnVal = guideChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            guiFile = guideChooser.getSelectedFile();
            loadGuide();
        }
    }  
    
    
    private void updateEnablings() {  
        updateMenusEnablings();
        updateVoicesList();
        updateVoiceFields();
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
        bankPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        bankAddressField = new javax.swing.JTextField();
        bankCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        bankSizeField = new javax.swing.JTextField();
        listPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        voicesList = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        pasteButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        spaceLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        soundPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        voiceSizeField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        voicePitchField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        voiceDurationField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        autoBox = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        guideMenu = new javax.swing.JMenuItem();
        reloadMenu = new javax.swing.JMenuItem();
        saveMenu = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        copyMenu = new javax.swing.JMenuItem();
        pasteMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Megadrive Voice editor");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setName("guiFrame");
        setResizable(false);
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

        bankPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Voices Bank"));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Bank:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Bank address:");

        bankAddressField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bankAddressField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankAddressField.setText("60000");
        bankAddressField.setToolTipText("Address of the bank in the ROM, in hexadecimal");
        bankAddressField.setEnabled(false);

        bankCombo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bankCombo.setToolTipText("Choose a bank provided by the guide");
        bankCombo.setEnabled(false);
        bankCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankComboActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Number of voices:");

        bankSizeField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bankSizeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankSizeField.setText("20");
        bankSizeField.setToolTipText("Number of voices in the bank");
        bankSizeField.setEnabled(false);

        voicesList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        voicesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        voicesList.setEnabled(false);
        voicesList.setVisibleRowCount(6);
        voicesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                voicesListValueChanged(evt);
            }
        });
        voicesList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                voicesListFocusGained(evt);
            }
        });
        voicesList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                voicesListKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(voicesList);

        jButton4.setText("Up");
        jButton4.setToolTipText("Move voice up in the list");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Down");
        jButton5.setToolTipText("Move voice down in the list");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton6.setText("Replace");
        jButton6.setToolTipText("Replace\\nvoice from an external sound file");
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Copy");
        jButton7.setToolTipText("Clone the selected voice");
        jButton7.setEnabled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        pasteButton.setText("Paste");
        pasteButton.setToolTipText("Replace the selected voice with the cloned voice");
        pasteButton.setEnabled(false);
        pasteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout listPanelLayout = new javax.swing.GroupLayout(listPanel);
        listPanel.setLayout(listPanelLayout);
        listPanelLayout.setHorizontalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pasteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        listPanelLayout.setVerticalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listPanelLayout.createSequentialGroup()
                .addComponent(jButton4)
                .addGap(1, 1, 1)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addGap(1, 1, 1)
                .addComponent(pasteButton)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Free space:");

        spaceLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spaceLabel.setForeground(new java.awt.Color(51, 204, 0));
        spaceLabel.setText("-");

        jButton1.setText("Load");
        jButton1.setToolTipText("Load voices bank at the provided address with the provided number of voices");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bankPanelLayout = new javax.swing.GroupLayout(bankPanel);
        bankPanel.setLayout(bankPanelLayout);
        bankPanelLayout.setHorizontalGroup(
            bankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bankPanelLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spaceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(bankPanelLayout.createSequentialGroup()
                .addGroup(bankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(bankPanelLayout.createSequentialGroup()
                        .addGroup(bankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(bankPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bankCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(bankPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bankAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bankPanelLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bankSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(listPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        bankPanelLayout.setVerticalGroup(
            bankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bankPanelLayout.createSequentialGroup()
                .addGroup(bankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(bankCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(bankAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(bankSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bankPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spaceLabel))
                .addContainerGap())
        );

        soundPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Voice"));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Voice size:");

        voiceSizeField.setEditable(false);
        voiceSizeField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        voiceSizeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        voiceSizeField.setText("0");
        voiceSizeField.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Pitch:");

        voicePitchField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        voicePitchField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        voicePitchField.setText("0");
        voicePitchField.setEnabled(false);
        voicePitchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                voicePitchFieldFocusGained(evt);
            }
        });
        voicePitchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                voicePitchFieldKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("bytes");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Duration");

        voiceDurationField.setEditable(false);
        voiceDurationField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        voiceDurationField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        voiceDurationField.setText("0");
        voiceDurationField.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("seconds");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(voiceSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(voiceDurationField, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                    .addComponent(voicePitchField))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11))))
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(voiceSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(voiceDurationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(voicePitchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Export"));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("As Sound");
        jButton2.setToolTipText("Export voice in WAV format");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("ROM format");
        jButton3.setToolTipText("Export voice in the format used by the ROM");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3))
        );

        jButton9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton9.setText("Play");
        jButton9.setToolTipText("Play the selected voice");
        jButton9.setEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        autoBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        autoBox.setText("Autoplay");
        autoBox.setToolTipText("Play voice when sliding on the voices list");
        autoBox.setEnabled(false);
        autoBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout soundPanelLayout = new javax.swing.GroupLayout(soundPanel);
        soundPanel.setLayout(soundPanelLayout);
        soundPanelLayout.setHorizontalGroup(
            soundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(soundPanelLayout.createSequentialGroup()
                .addGroup(soundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, soundPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(soundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(soundPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(autoBox)))
                .addGap(41, 41, 41))
        );
        soundPanelLayout.setVerticalGroup(
            soundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(soundPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(bankPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(soundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bankPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
            .addComponent(soundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Open ROM");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        guideMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        guideMenu.setText("Open Guide");
        guideMenu.setEnabled(false);
        guideMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guideMenuActionPerformed(evt);
            }
        });
        jMenu1.add(guideMenu);

        reloadMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        reloadMenu.setText("Reload Guide");
        reloadMenu.setEnabled(false);
        reloadMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadMenuActionPerformed(evt);
            }
        });
        jMenu1.add(reloadMenu);

        saveMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenu.setText("Save");
        saveMenu.setEnabled(false);
        saveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenu);
        jMenu1.add(jSeparator2);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem5.setText("Exit");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        editMenu.setText("Edit");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItem6.setText("Options");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        editMenu.add(jMenuItem6);
        editMenu.add(jSeparator3);

        copyMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyMenu.setText("Copy Voice");
        copyMenu.setEnabled(false);
        copyMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuActionPerformed(evt);
            }
        });
        editMenu.add(copyMenu);

        pasteMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteMenu.setText("Paste Voice");
        pasteMenu.setEnabled(false);
        pasteMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenuActionPerformed(evt);
            }
        });
        editMenu.add(pasteMenu);

        jMenuBar1.add(editMenu);

        jMenu3.setText("Help");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem3.setText("About");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        if (askSaveRom())
            System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        
        
    }//GEN-LAST:event_formMouseWheelMoved

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        openRom();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO: check modifications, ask save
        System.exit(0);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        JOptionPane.showMessageDialog(this,
            TITLE + "\n" +
            "© Gil Costa 2012\n\n" +
            "Acknowledgment on derived work is not required but would be appreciated\n\n"+
            TITLE + " is free software.The author can not be held responsible\nfor any illicit use of this program.\n",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!checkModifications()) return;
        long address = helper.getHexFromField(bankAddressField);
        if (address == ComponentsHelper.INVALID_LONG){
            ComponentsHelper.showWarning("Please type a valid bank address, in hexadecimal");
        }        
        int numVoices = helper.getPositiveIntFromField(bankSizeField);
        if (numVoices == ComponentsHelper.INVALID_INT){
            ComponentsHelper.showWarning("Please type a valid number of voices, in decimal");
        }
        loadBank(address, numVoices);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void voicesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_voicesListValueChanged
        updateVoiceFields();
        if (autoBox.isSelected()) playVoice();
    }//GEN-LAST:event_voicesListValueChanged

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        playVoice();
        restoreFocus();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void voicesListFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_voicesListFocusGained
        focusedComponent = voicesList;
    }//GEN-LAST:event_voicesListFocusGained

    private void voicePitchFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_voicePitchFieldFocusGained
        focusedComponent = voicePitchField;
    }//GEN-LAST:event_voicePitchFieldFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        restoreFocus();
        exportSound();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        restoreFocus();
        exportCompressed();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        restoreFocus();
        swapUp();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        restoreFocus();
        swapDown();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        restoreFocus();
        Voice voice = inportVoice();
        if (voice != null){
            GlobalData.bank.setVoice(voicesList.getAnchorSelectionIndex(), voice);
            updateVoiceFields();
            updateSpaceLabel();
            updateMenusEnablings();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        restoreFocus();
        copyVoice();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void pasteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteButtonActionPerformed
        restoreFocus();
        pasteVoice();
        updateMenusEnablings();
    }//GEN-LAST:event_pasteButtonActionPerformed

    private void voicesListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_voicesListKeyPressed
        switch (evt.getKeyCode()){
            case KeyEvent.VK_ENTER: playVoice(); break;            
        }
    }//GEN-LAST:event_voicesListKeyPressed

    private void voicePitchFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_voicePitchFieldKeyPressed
        if (voicePitchField.getBackground() != Color.red && evt.getKeyCode() == KeyEvent.VK_ENTER)
            playVoice();
    }//GEN-LAST:event_voicePitchFieldKeyPressed

    private void copyMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuActionPerformed
        copyVoice();
    }//GEN-LAST:event_copyMenuActionPerformed

    private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
        saveBank();
    }//GEN-LAST:event_saveMenuActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        OptionsDialog options = new OptionsDialog(this, true);
        options.setLocationRelativeTo(null);
        options.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void pasteMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenuActionPerformed
        pasteVoice();
    }//GEN-LAST:event_pasteMenuActionPerformed

    private void guideMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guideMenuActionPerformed
        openGuide();
    }//GEN-LAST:event_guideMenuActionPerformed

    private void bankComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankComboActionPerformed
        int itemId = bankCombo.getSelectedIndex();
        if (itemId >= 0){
            if (!checkModifications()) return;
            GlobalData.readBankFromGuide(itemId);
            helper.setFieldAsHex(bankAddressField, GlobalData.bankAddress);
            helper.setField(bankSizeField, GlobalData.bank.getNumVoices());
            refresh();
        }
    }//GEN-LAST:event_bankComboActionPerformed

    private void reloadMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadMenuActionPerformed
        loadGuide();
    }//GEN-LAST:event_reloadMenuActionPerformed

    private void autoBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoBoxActionPerformed
        restoreFocus();
    }//GEN-LAST:event_autoBoxActionPerformed

    
    private void exportSound(){
        int index = voicesList.getSelectedIndex();
        if (index == -1) return;
        soundChooser.setDialogTitle("Export sound");
        int returnVal = soundChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = soundChooser.getSelectedFile();
            if (!file.exists() || file.canWrite()){
                Voice voice = GlobalData.bank.getVoice(index);
                try {
                    voice.writeToWave(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ComponentsHelper.showError("Failed to save voice to " + file.getName());
                }
            }else{
                ComponentsHelper.showError("File " + file.getName() + " cannot be written");
            }
        }
    }
    
    private void exportCompressed(){
        int index = voicesList.getSelectedIndex();
        if (index == -1) return;
        romChooser.setDialogTitle("Export compressed data as a binary file");
        int returnVal = romChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = romChooser.getSelectedFile();
            if (!file.exists() || file.canWrite()){
                Voice voice = GlobalData.bank.getVoice(index);
                try {
                    voice.writeToBinary(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ComponentsHelper.showError("Failed to save data to " + file.getName());
                }
            }else{
                ComponentsHelper.showError("File " + file.getName() + " cannot be written");
            }
        }
    }
    
    
    private void saveBank(){
        GlobalData.writeBank();
        updateMenusEnablings();
    }
    
    
    private Voice inportVoice(){
        int index = voicesList.getSelectedIndex();
        if (index == -1) return null;
        soundChooser.setDialogTitle("Inport sound");
        int returnVal = soundChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = soundChooser.getSelectedFile();
            if (file.canRead()){
                Voice voice = GlobalData.bank.getVoice(index);
                DefaultListModel model = (DefaultListModel)voicesList.getModel();
                model.setElementAt(file.getName(), index);
                return GlobalData.readVoiceFromFile(file, voice.getPitch());
            }else{
                ComponentsHelper.showError("File " + file.getName() + " cannot be read");
                return null;
            }
        }
        return null;
    }
    

    private void copyVoice(){
        copiedId = voicesList.getSelectedIndex();
        updateMenusEnablings();
    }
    
    private void pasteVoice(){
        int index = voicesList.getSelectedIndex();
        if (copiedId == index) return;
        GlobalData.bank.cloneVoice(copiedId, index);
        DefaultListModel model = (DefaultListModel)voicesList.getModel();
        model.set(index, model.get(copiedId));
        updateVoiceFields();
        updateSpaceLabel();
    }
    
    private void swapVoices(int id1, int id2){
        DefaultListModel model = (DefaultListModel)voicesList.getModel();
        String temp = (String)model.get(id1);
        model.set(id1, model.get(id2));
        model.set(id2, temp);
        GlobalData.bank.swapVoices(id1, id2);
        voicesList.setSelectedIndex(id2);
        updateMenusEnablings();
    }
    // move voice up
    private void swapUp(){
        int index = voicesList.getSelectedIndex();
        if (index == -1) return;
        int target = index-1;
        if (target < 0) target = GlobalData.bank.getNumVoices()-1;
        swapVoices(index, target);
    }
    // move voice down
    private void swapDown(){
        int index = voicesList.getSelectedIndex();
        if (index == -1) return;
        int target = index+1;
        if (target >= GlobalData.bank.getNumVoices()) target = 0;
        swapVoices(index, target);
    }
    
    
    
    
    
    private void playVoice(){
        int index = voicesList.getSelectedIndex();
        if (index == -1) return;
        Voice voice = GlobalData.bank.getVoice(index);
        try {
            voice.play();
        } catch (Exception ex) {
            ex.printStackTrace();
            ComponentsHelper.showError("Unable to play voice");
        }        
    }
    
    private void restoreFocus(){
        if (focusedComponent != null)
            focusedComponent.requestFocusInWindow();
    }
    
    private void refresh(){
        updateVoicesList();
        updateSpaceLabel();
        copiedId = -1;
        updateMenusEnablings();
    }
    
    private void loadBank(long address, int numVoices) {
        if(!GlobalData.readBank(address, numVoices)) return;
        // bank was read, fill fields        
        refresh();
    }
    
    
    private void updateSpaceLabel(){
        int freeSpace = GlobalData.getBankFreeSpace();
        if (freeSpace < 0) spaceLabel.setForeground(Color.red);
        else spaceLabel.setForeground(Color.black);
        spaceLabel.setText(freeSpace/1000.f + " Kb");
    }
    
    
    private void updateBankCombo(){
        int numBanks = GlobalData.getNumBanks();
        bankCombo.removeAllItems();
        for (int i = 0 ; i < numBanks ; ++i){
            bankCombo.addItem("Bank " + i);
        }
    }
    
    private void updateVoicesList(){
        int selectedId = voicesList.getSelectedIndex();
        DefaultListModel model = new DefaultListModel();
        Bank bank = GlobalData.bank;
        ComponentsHelper.setEnable(listPanel, bank != null);
        if (bank == null){
            voicesList.setModel(model);
            return;
        }
        for(int i = 0 ; i < bank.getNumVoices() ; ++i){
            // TODO: use string from guide
            model.addElement("voice " + i);
        }
        voicesList.setModel(model);
        if (selectedId < 0) selectedId = 0;
        else if (selectedId >= bank.getNumVoices()) selectedId = bank.getNumVoices()-1;
        voicesList.setSelectedIndex(selectedId);
    }
    
    private void updateVoiceFields(){
        int index = voicesList.getSelectedIndex();
        if (index == -1 || GlobalData.bank == null){
            clearVoiceFields();
        }else{
            ComponentsHelper.setEnable(soundPanel, true);
            Voice voice = GlobalData.bank.getVoice(index);
            voiceSizeField.setText(voice.getSizeInBytes() + "");
            try {
                voiceDurationField.setText((voice.getDurationInMiliseconds()/1000.)+"");
            } catch (Exception ex) {
                ex.printStackTrace();
                voiceDurationField.setText("unknown");
            }
            helper.setField(voicePitchField,voice.getPitch());
        }
    }
    
    
    private void clearVoiceFields(){
        voiceSizeField.setText("0");
        voiceDurationField.setText("0");
        helper.setField(voicePitchField,0);
        ComponentsHelper.setEnable(soundPanel, false);
    }
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            // nothing...
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui();
            }
        });
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoBox;
    private javax.swing.JTextField bankAddressField;
    private javax.swing.JComboBox bankCombo;
    private javax.swing.JPanel bankPanel;
    private javax.swing.JTextField bankSizeField;
    private javax.swing.JMenuItem copyMenu;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem guideMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPanel listPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton pasteButton;
    private javax.swing.JMenuItem pasteMenu;
    private javax.swing.JMenuItem reloadMenu;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JPanel soundPanel;
    private javax.swing.JLabel spaceLabel;
    private javax.swing.JTextField voiceDurationField;
    private javax.swing.JTextField voicePitchField;
    private javax.swing.JTextField voiceSizeField;
    private javax.swing.JList voicesList;
    // End of variables declaration//GEN-END:variables

    private void updateMenusEnablings() {
        guideMenu.setEnabled(GlobalData.bank != null);
        reloadMenu.setEnabled(GlobalData.guide != null);
        saveMenu.setEnabled(GlobalData.bank != null);
        copyMenu.setEnabled(GlobalData.bank != null && voicesList.getSelectedIndex() != -1);
        pasteMenu.setEnabled(copiedId >= 0);
        pasteButton.setEnabled(copiedId >= 0);
    }

    private boolean checkModifications() {
        if (GlobalData.bank == null) return true;
        if (askingSave) return false;
        askingSave = true;
        Bank bank = GlobalData.bank;
        if (bank.wasModified()){
            int res = JOptionPane.showConfirmDialog(null, "Bank was modified. Save changes?", "Bank modified", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.YES_OPTION){
                saveBank();
            }else if (res == JOptionPane.CANCEL_OPTION) return false;
            bank.ignoreModifications();
        }
        askingSave = false;
        return true;
    }
   

}
