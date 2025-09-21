/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import lib.Bank;
import lib.bank.CustomBankStrategy;
import lib.bank.FiveBytesBankStrategy;
import lib.voice.CompressedVoiceStrategy;
import lib.voice.RawVoiceStrategy;

/**
 *
 * @author Gil
 */
public class OptionsDialog extends javax.swing.JDialog {
    private static final String ADDRESS_STRING = "Address";
    private static final String SIZE_STRING = "Size";
    private static final String PITCH_STRING = "Pitch";
    private static final String REDUNDANCY_STRING = "Redundancy";
    
    private ComponentsHelper helper = new ComponentsHelper();
    
    private Gui gui;
    
    
    
    private void setTableField(byte[] table){
        StringBuilder sb = new StringBuilder();
        for(byte b : table){
            sb.append(Integer.toHexString(b&0xFF));
            sb.append(" ");
        }
        String tableText = sb.substring(0, sb.length()-1);
        helper.setField(tableField, tableText);
    }
    
    private byte[] getTableField(){
        String tableText = helper.getField(tableField);
        Scanner sc = new Scanner(tableText);
        ByteArrayOutputStream dos = new ByteArrayOutputStream();
        while (sc.hasNextInt(16)){
            dos.write(sc.nextInt(16));
        }
        return dos.toByteArray();
    }            
    
    
    public final void refresh(){
        // refresh table
        setTableField(GlobalData.table);
        // refresh table format
        boolean isCompressed = GlobalData.voiceStrategy instanceof CompressedVoiceStrategy;
        compressedBox.setSelected(isCompressed);
        embeddedBox.setEnabled(isCompressed);
        embeddedBox.setSelected(GlobalData.embeddedTable);
        tableField.setEnabled(isCompressed && !GlobalData.embeddedTable);
              
        if (!(GlobalData.bankStrategy instanceof CustomBankStrategy)) return;
        CustomBankStrategy strat = (CustomBankStrategy)GlobalData.bankStrategy;
        
        helper.setField(sizeField, strat.headerSize);
        helper.setFieldAsHex(baseField, strat.baseAddress);
        helper.setFieldAsHex(offsetField, strat.baseMinimumOffset);
        helper.setField(pitchField, strat.pitch);
        
        littleBox.setSelected(strat.littleEndian);
        globalBox.setSelected(strat.asGlobalPointer);
        
        DefaultListModel model = new DefaultListModel();
        for(int i = 0 ; i < 4 ; ++i){
            switch(strat.order[i]){
                case 0: model.addElement(ADDRESS_STRING); break;
                case 1: model.addElement(SIZE_STRING); break;
                case 2: model.addElement(PITCH_STRING); break;
                case 3: model.addElement(REDUNDANCY_STRING); break;
            }
        }
        list.setModel(model);
    }
    
    
    // --------------------------------------------------------
    // --------------------------------------------------------
    
    
     private void saveChanges() {
         // Compression
        boolean compressed = compressedBox.isSelected();
        if (compressed) GlobalData.voiceStrategy = new CompressedVoiceStrategy();
        else GlobalData.voiceStrategy = new RawVoiceStrategy();
        GlobalData.embeddedTable = embeddedBox.isSelected();
        GlobalData.table = getTableField();
        
        // Bank headers
        int headerSize = helper.getPositiveIntFromField(sizeField);
        boolean littleEndian = littleBox.isSelected();
        boolean asGlobalPointer = globalBox.isSelected();
        long baseAddress = helper.getHexFromField(baseField);
        int baseMinimumOffset = (int)helper.getHexFromField(offsetField);
        int pitch = helper.getIntFromField(pitchField);
        int[] order = new int[4];
        
        ListModel model = list.getModel();
        for(int i = 0 ; i < order.length ; ++i){
            String item = (String)model.getElementAt(i);
            if (item.equals(ADDRESS_STRING)) order[i] = 0;
            else if (item.equals(SIZE_STRING)) order[i] = 1;
            else if (item.equals(PITCH_STRING)) order[i] = 2;
            else if (item.equals(REDUNDANCY_STRING)) order[i] = 3;
        }
        int maxSize = Bank.MAX_BANK_SIZE;
        GlobalData.bankStrategy = new CustomBankStrategy(headerSize, littleEndian, asGlobalPointer, baseAddress, baseMinimumOffset, pitch, order, maxSize);
        
        dispose();
        gui.setEnabled(true);
    }
    
    
     
     private void initList(){
         DefaultListModel model = new DefaultListModel();
         model.addElement(ADDRESS_STRING);
         model.addElement(SIZE_STRING);
         model.addElement(PITCH_STRING);
         model.addElement(REDUNDANCY_STRING);
        list.setModel(model);
     }
     
     
    /**
     * Creates new form OptionsDialog
     */
    public OptionsDialog(Gui parent, boolean modal) {
        super(parent, modal);
        this.gui = parent;
        initComponents();
        initList();
        refresh();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        tablePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tableField = new javax.swing.JTextField();
        embeddedBox = new javax.swing.JCheckBox();
        compressedBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        baseField = new javax.swing.JTextField();
        downButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        sizeField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jLabel6 = new javax.swing.JLabel();
        littleBox = new javax.swing.JCheckBox();
        upButton = new javax.swing.JButton();
        offsetField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        globalBox = new javax.swing.JCheckBox();
        pitchField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.setToolTipText("Discard options");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Ok");
        jButton1.setToolTipText("Apply options");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Compression"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Table:");

        tableField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tableField.setToolTipText("Lookup table used by the decompression algorithm. Fill with 16 bytes in hexadecimal, separated by spaces");

        embeddedBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        embeddedBox.setText("Embedded Table (Streets of Rage 1 & 2)");
        embeddedBox.setToolTipText("The compression lookout table is embedded in the voices data of sor1 and sor2");
        embeddedBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                embeddedBoxActionPerformed(evt);
            }
        });

        compressedBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        compressedBox.setText("Compressed");
        compressedBox.setToolTipText("If not set it means voice representation on the ROM is as raw PCM audio");
        compressedBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compressedBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableField, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(compressedBox)
                    .addComponent(embeddedBox))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addComponent(compressedBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(embeddedBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tableField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Bank headers"));

        baseField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        baseField.setText("-1");
        baseField.setToolTipText("Address in the rom from where the header offsets refer to (hex). Set to -1 if offsets refers to the bank address itself, which is the most common situation.");

        downButton.setText("Down");
        downButton.setToolTipText("Move item down in the list");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Fixed pitch:");

        sizeField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        sizeField.setText("5");
        sizeField.setToolTipText("Size of the headers in the voices bank, in bytes (decimal)");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Header size:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Base address:");

        jScrollPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        list.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Offset", "Size", "Pitch", "Redundancy" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list.setToolTipText("Order of the information inside the bank headers");
        jScrollPane1.setViewportView(list);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Minimum offset:");

        littleBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        littleBox.setSelected(true);
        littleBox.setText("Little endian");
        littleBox.setToolTipText("Usually pointers and voice size in bank headers are little endian.");

        upButton.setText("Up");
        upButton.setToolTipText("Move item up in the list");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        offsetField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        offsetField.setText("-1");
        offsetField.setToolTipText("Use this offset (hex) if there is important data between the base address and the start of the voices data that must not be overriden. Usually there isn't, use -1 in that case");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Information order:");

        globalBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        globalBox.setText("Global pointers");
        globalBox.setToolTipText("In some cases, the pointers on the bank headders are actually global pointers, not offsets.");
        globalBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                globalBoxActionPerformed(evt);
            }
        });

        pitchField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pitchField.setText("-1");
        pitchField.setToolTipText("Some games setup the pitch for each voice in the header. For that we set this field to -1. Others doesn't, instead they use a fixed pitch or set the pitch by some other way. To simplify, we set a fixed positive value for pitch (decimal)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(baseField)
                            .addComponent(offsetField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sizeField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pitchField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(littleBox))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(downButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(globalBox)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(sizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(baseField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(offsetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(pitchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(littleBox))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(globalBox))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
        gui.setEnabled(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveChanges();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        gui.setEnabled(true);
        gui.requestFocusInWindow();
    }//GEN-LAST:event_formWindowClosed

    private void globalBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_globalBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_globalBoxActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        int selectedIndex = list.getSelectedIndex();
        DefaultListModel model = (DefaultListModel) list.getModel();
        if (selectedIndex > 0) {
            String tmp = (String) model.getElementAt(selectedIndex - 1);
            model.set(selectedIndex - 1, list.getSelectedValue());
            model.set(selectedIndex, tmp);
            list.setModel(model);
            list.setSelectedIndex(selectedIndex - 1);
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        int selectedIndex = list.getSelectedIndex();
        DefaultListModel model = (DefaultListModel) list.getModel();
        if (selectedIndex != -1 && selectedIndex < 2) {
            String tmp = (String) model.getElementAt(selectedIndex + 1);
            model.set(selectedIndex + 1, list.getSelectedValue());
            model.set(selectedIndex, tmp);
            list.setModel(model);
            list.setSelectedIndex(selectedIndex + 1);
        }
    }//GEN-LAST:event_downButtonActionPerformed

    private void embeddedBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_embeddedBoxActionPerformed
        tableField.setEnabled(!embeddedBox.isSelected());
    }//GEN-LAST:event_embeddedBoxActionPerformed

    private void compressedBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compressedBoxActionPerformed
        boolean compressed = compressedBox.isSelected();
        if (compressed) tableField.setEnabled(!embeddedBox.isSelected());
        else tableField.setEnabled(false);
        embeddedBox.setEnabled(compressed);
    }//GEN-LAST:event_compressedBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField baseField;
    private javax.swing.JCheckBox compressedBox;
    private javax.swing.JButton downButton;
    private javax.swing.JCheckBox embeddedBox;
    private javax.swing.JCheckBox globalBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list;
    private javax.swing.JCheckBox littleBox;
    private javax.swing.JTextField offsetField;
    private javax.swing.JTextField pitchField;
    private javax.swing.JTextField sizeField;
    private javax.swing.JTextField tableField;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
   
}
