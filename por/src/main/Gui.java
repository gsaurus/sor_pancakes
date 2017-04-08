/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import lib.Action;
import lib.*;




/**
 *
 * @author Gil
 */
public class Gui extends javax.swing.JFrame{
    
    private static final String VERSION = "1.3";
    
    private static final String TITLE = "Palettes of Rage " + VERSION;
    
    private ImagePanel imagePanel;
    private String currentDirectory;
    private JFileChooser romChooser;
    private JFileChooser guideChooser;
    
    private ColorsManager manager;
    private Guide guide;
    private Clipboard clipboard;
    private long address;
    private int offset;
    
    private JPanel[] colorPanels;
    private Border selectionBorder;
    
    private String romName;
    
    private boolean gensFieldAction;
    private boolean rgbFieldAction;
    private boolean addressFieldAction;
    private boolean comboFieldAction;
    private boolean colorChooserAction;
    private boolean updateAction;
    
    
    private boolean actionAlreadyInCourse(){
        return manager == null || gensFieldAction || rgbFieldAction || addressFieldAction || comboFieldAction || updateAction || colorChooserAction;
    }
    
    
    private void checkModifications(){
        if (manager == null) return;
        if (manager.hasPendingModifications()){
            setTitle(TITLE + " - " + romName + " *");
        }else setTitle(TITLE + " - "  + romName);
    }
    private void enteringGens(){
        if (actionAlreadyInCourse()) return;
        gensFieldAction = true;
        gensEntered();
        gensFieldAction = false;
    }
    private void enteringRgb(){
        if (actionAlreadyInCourse()) return;
        rgbFieldAction = true;
        rgbEntered();
        rgbFieldAction = false;
    }
    private void enteringAddress(){
        if (actionAlreadyInCourse()) return;
        addressFieldAction = true;
        addressEntered();
        addressFieldAction = false;
    }  
    private void enteringCombo(){
        if (actionAlreadyInCourse()) return;
        comboFieldAction = true;
        paletteComboSelectionChanged(paletteCombo.getSelectedIndex());
        comboFieldAction = false;
    }
    private void pickingColor(){
        if (actionAlreadyInCourse()) return;
        colorChooserAction = true;
        colorPicked();
        colorChooserAction = false;
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
        
        
        colorChooser.getSelectionModel().addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent event) {
                pickingColor();
            }            
        });
        
    }
    private void preInitComponents(){
        address = Rom.ROM_START_ADDRESS;
        try {            
            Image icon = ImageIO.read(new File("icon.png"));
            this.setIconImage(icon);
        } catch (IOException ex) {
            // whatever
        }
        
        
        currentDirectory = new File(".").getAbsolutePath();
        romChooser = new JFileChooser(currentDirectory);
        romChooser.setDialogTitle("Open ROM file to edit");
        guideChooser = new JFileChooser(currentDirectory + "/" + Guide.GUIDES_DIR);
        guideChooser.setDialogTitle("Open palettes guide");
        
        clipboard = new Clipboard();
        
        imagePanel = new ImagePanel();
    }        
    private void postInitComponents(){
        initColorPanels();
        rgbField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                enteringRgb();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                enteringRgb();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                enteringRgb();
            }
        });
        gensField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                enteringGens();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                enteringGens();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                enteringGens();
            }
        });
        addressField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                enteringAddress();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                enteringAddress();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                enteringAddress();
            }
        });
        
        romChooser.addChoosableFileFilter(new FileFilter(){
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return filename.endsWith(".bin");
            }
            public String getDescription() {
                return "*.bin";
            }            
        });
        
        guideChooser.addChoosableFileFilter(new FileFilter(){
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return filename.endsWith(".txt");
            }
            public String getDescription() {
                return "*.txt";
            }            
        });
        
        scrollPane.setWheelScrollingEnabled(false);
        
    }
    
    private void appInitialization(){
        // TODO: check ini file
        openRom();
        openGuide();
        setOffset(0);
        addressChanged();
    }       
    
    private void showError(String text){
        String message;
        String title;
        if (manager == null){
            title = "No rom opened";
            message = "You must open a ROM";
        }else{
            title = "Wops!";
            message = text;
        }
        JOptionPane.showMessageDialog(this,
            message, title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    

    /**
     * Creates new form Gui
     */
    public Gui() {        
        preInitComponents();      
        initComponents();
        postInitComponents();  
        setVisible(true);
        appInitialization();
    }
    
    
    private void setColor(int offset, Color color){
        if (manager == null) return;
        manager.setColor(offset, Palette.correct(color));
        checkModifications();
        updateSelectedColor();
    }
    
    
    // TODO: on setup, make "conversations" with GUI
    private void setupColorsManager(String romFile){
        try {
            manager = new ColorsManager(romFile);
        } catch (IOException ex) {
            showError("ROM file not found");
        }
    }    
    private void setupGuide(File guideFile){
        updateAction = true;
        paletteCombo.removeAllItems();
        try {
            guide = new Guide(guideFile);
        } catch (FileNotFoundException ex) {
            guide = null;
            showError("Guide file not found");
        }
        if (guide != null){
            int size = guide.size();
            for (int i = 0 ; i < size ; ++i){
                paletteCombo.addItem(guide.getName(i));
            }
        }
        paletteCombo.addItem("Custom");
        paletteCombo.setSelectedIndex(0);
        paletteComboSelectionChanged(0);
        updateAction = false;
    }

    
    
    private boolean askSaveRom(){
        if (manager == null) return true;
        if (manager.hasPendingModifications()){
            int option = JOptionPane.showConfirmDialog(this,
                "The palette was modified.\n"
                + "Save changes?",
                "File modified",
                JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (option == JOptionPane.YES_OPTION){
                // save
                try {
                    manager.save();
                } catch (IOException ex) {
                    showError("Unable to save rom");
                    return false;
                }
            }else if (option != JOptionPane.NO_OPTION){
                // cancel or close, return false
                return false;
            }            
        }
        checkModifications();
        return true;
    }   
    private void openRom(){
        // close rom first
        if (!askSaveRom()) return;
        
        // open rom
        int returnVal = romChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = romChooser.getSelectedFile();
            romName = file.getName();
            setTitle(TITLE + " - "  + romName);
            setupColorsManager(file.getAbsolutePath());
            address = 0;
            paletteComboSelectionChanged(paletteCombo.getSelectedIndex());
        }
    }    
    private void openGuide(){
        int returnVal = guideChooser.showOpenDialog(this);        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = guideChooser.getSelectedFile();
            setupGuide(file);
        }
    }
    
    
    private void paletteComboSelectionChanged(int selection){
        if (guide != null && selection != guide.size()){
            long newAddress = guide.getAddress(selection);
            if (newAddress != address){
                address = newAddress;                
                
                { // don't even ask what this is... Revalidate etc didn't work
                    JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
                    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                    int x = horizontalScrollBar.getValue();
                    int y = verticalScrollBar.getValue();
                    imagePanel.setImage(guide.getImage(selection));
                    horizontalScrollBar.setValue(x+1);
                    horizontalScrollBar.setValue(x);
                    verticalScrollBar.setValue(y+1);
                    verticalScrollBar.setValue(y);
                }
                
                addressField.setText(Guide.longToHex(address));
                addressEntered();
                //addressChanged();
            }
        }
    }
    
    
    private void addressChanged(){
        if (manager != null){
            try{
                manager.openPalette(address, guide.getType());
                for (int i = 0 ; i < 16 ; ++i){
                    Color awtColor = manager.getColor(i);
                    colorPanels[i].setBackground(awtColor);
                    imagePanel.replaceColor(i,awtColor);
                }                
            }catch(Exception e){
                e.printStackTrace();
                showError("Invalid address");
                return;
            }
            checkModifications();
            updateSelectedColor();
        } else{
            for (int i = 0 ; i < 16 ; ++i){
                colorPanels[i].setBackground(java.awt.Color.white);
            }
        }
    }   
    
    private void updateSelectedColor(){
        if (manager == null) return;
        updateAction = true;
        Color color;        
        color = manager.getColor(offset);
        if (color == null) return;
        try{ imagePanel.replaceColor(offset,color);}catch(Exception e){}
        try{ colorPanels[offset].setBackground(color);}catch(Exception e){}
        try{ gensField.setText(Integer.toHexString(Palette.convertToGens(color.getRGB())));}catch(Exception e){}
        try{ gensField.setBackground(java.awt.Color.white);}catch(Exception e){}
        try{ rgbField.setText(color.getRed() + " " +color.getGreen() + " " + color.getBlue());}catch(Exception e){}
        try{ rgbField.setBackground(java.awt.Color.white);}catch(Exception e){}
        try{ colorChooser.setColor(color);}catch(Exception e){}
        try{ colorChooser.repaint();}catch(Exception e){}
        try{ rgbField.repaint();}catch(Exception e){}
        try{ gensField.repaint();}catch(Exception e){}
        updateAction = false;
    }
    
    
    private static double lum(java.awt.Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return .299*r + .587*g + .114*b;
    }
    
    private void setOffset(int newOffset){
        if (newOffset != offset){
            colorPanels[offset].setBorder(null);
            offset = newOffset;
            colorPanels[offset].setBorder(selectionBorder);
            updateSelectedColor();
        }
    }
    
    private void addressEntered(){
        String newAddressText = addressField.getText();
        long newAddress = Guide.hexToLong(newAddressText);
        if (manager == null || newAddress == -1){
            addressField.setBackground(java.awt.Color.red);
        }else{
            addressField.setBackground(java.awt.Color.white);
            if (!matchCombo(newAddress)){
                paletteCombo.setSelectedIndex(paletteCombo.getItemCount()-1);
            } // TODO: else if not same as current combo, set combo
            address = newAddress;
            addressChanged();
        }
    }
    
    
    private void rgbEntered(){
        String rgbText = rgbField.getText();
        Scanner sc = new Scanner(rgbText);
        Color color = null;
        try{
            int r,g,b;
            r = sc.nextInt(); g = sc.nextInt(); b = sc.nextInt();
            color = new Color(r,g,b);
        }catch(Exception e){}
        if (color != null){
            try{ rgbField.setBackground(java.awt.Color.white); } catch(Exception e){}
            setColor(offset, color);
        }else{
            try{ rgbField.setBackground(java.awt.Color.red); } catch(Exception e){}
        }
    }
    
    
    private void gensEntered(){
        String gensText = gensField.getText();
        gensText = gensText.toLowerCase();
        Color color = new Color(Palette.convertToRgb(Short.parseShort(gensText, 16)));
        if (color != null){
            try{ gensField.setBackground(java.awt.Color.white); }catch(Exception e){}
            try{ setColor(offset, color); }catch(Exception e){}
        }else{
            try{ gensField.setBackground(java.awt.Color.red); }catch(Exception e){}
        }
    }
    
    
    private void colorPicked(){
        java.awt.Color color = colorChooser.getColor();
        setColor(offset, color);
    }
    
    
    
    private void applyAction(Action action) {
        updateAction = true;
        int newOffset = action.getOffset();
        if (offset != newOffset){
            setOffset(newOffset);
        }else{
            updateSelectedColor();
        }
        checkModifications();        
        updateAction = false;
    }
    
    
    private void scaleImagePanel(float scale){
        // Save the previous coordinates
        float oldZoom = imagePanel.getScale();
        float newZoom = oldZoom+scale;
        Rectangle oldView = scrollPane.getViewport().getViewRect();
        // resize the panel for the new zoom
        imagePanel.setScale(newZoom);
        // calculate the new view position
        Point newViewPos = new Point();
        newViewPos.x = (int)(Math.max(0, (oldView.x + oldView.width / 2) * newZoom / oldZoom - oldView.width / 2));
        newViewPos.y = (int)(Math.max(0, (oldView.y + oldView.height / 2) * newZoom / oldZoom - oldView.height / 2));
        scrollPane.getViewport().setViewPosition(newViewPos);
    }
    
    
    
    private boolean matchCombo(long newAddress) {
        if (guide == null) return false;
        for (int i = 0 ; i < guide.size() ; ++i)
            if (newAddress == guide.getAddress(i)) return true;
        return false;
    }        
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        paletteCombo = new javax.swing.JComboBox();
        addressField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        colorsPanel = new javax.swing.JPanel();
        colorPanel1 = new javax.swing.JPanel();
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
        colorChooser = new javax.swing.JColorChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        rgbField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        gensField = new javax.swing.JTextField();
        scrollPane = new javax.swing.JScrollPane(imagePanel);
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        openRomMenu = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        openGuideMenu = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        copyPalMenu = new javax.swing.JMenuItem();
        copyColorMenu = new javax.swing.JMenuItem();
        pasteColorMenu = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        findMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Palettes of Rage");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setName("guiFrame");
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

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Palette:");

        paletteCombo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paletteCombo.setMaximumRowCount(16);
        paletteCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Custom" }));
        paletteCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paletteComboActionPerformed(evt);
            }
        });
        paletteCombo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paletteComboKeyPressed(evt);
            }
        });

        addressField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        addressField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        addressField.setText("200");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Address:");

        colorsPanel.setPreferredSize(new java.awt.Dimension(256, 64));

        colorPanel1.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
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
            .addGap(0, 26, Short.MAX_VALUE)
        );
        colorPanel1Layout.setVerticalGroup(
            colorPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        colorPanel2.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel2.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel2Layout.setVerticalGroup(
            colorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel3.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel3.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel3Layout.setVerticalGroup(
            colorPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel4.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel4.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel4Layout.setVerticalGroup(
            colorPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel5.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel5.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel5Layout.setVerticalGroup(
            colorPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel6.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel6.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel6Layout.setVerticalGroup(
            colorPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel7.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel7.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel7Layout.setVerticalGroup(
            colorPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel8.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel8.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel8Layout.setVerticalGroup(
            colorPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel9.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel9.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel9Layout.setVerticalGroup(
            colorPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel10.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel10.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel10Layout.setVerticalGroup(
            colorPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel11.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel11.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel11Layout.setVerticalGroup(
            colorPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel12.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel12.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel12Layout.setVerticalGroup(
            colorPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel13.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel13.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel13.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel13Layout.setVerticalGroup(
            colorPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel14.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel14.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel14.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel14Layout.setVerticalGroup(
            colorPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel15.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel15.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel15.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel15Layout.setVerticalGroup(
            colorPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel16.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel16.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        colorPanel16.setMinimumSize(new java.awt.Dimension(16, 16));
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
            .addGap(0, 32, Short.MAX_VALUE)
        );
        colorPanel16Layout.setVerticalGroup(
            colorPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorsPanelLayout = new javax.swing.GroupLayout(colorsPanel);
        colorsPanel.setLayout(colorsPanelLayout);
        colorsPanelLayout.setHorizontalGroup(
            colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPanelLayout.createSequentialGroup()
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(colorsPanelLayout.createSequentialGroup()
                        .addComponent(colorPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(colorsPanelLayout.createSequentialGroup()
                        .addComponent(colorPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        colorsPanelLayout.setVerticalGroup(
            colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPanelLayout.createSequentialGroup()
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(colorPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        colorChooser.setMinimumSize(new java.awt.Dimension(429, 32));
        colorChooser.setPreferredSize(new java.awt.Dimension(429, 32));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("RGB:");

        rgbField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        rgbField.setText("255 255 255");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Genesis:");

        gensField.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        gensField.setText("0fff");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gensField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rgbField, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(gensField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(rgbField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paletteCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(colorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(paletteCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(colorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
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

        jMenuItem4.setText("Close Rom");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("Save");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);
        jMenu1.add(jSeparator4);

        openGuideMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        openGuideMenu.setText("Open Guide");
        openGuideMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openGuideMenuActionPerformed(evt);
            }
        });
        jMenu1.add(openGuideMenu);
        jMenu1.add(jSeparator1);

        exitMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenu);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        copyPalMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        copyPalMenu.setText("Copy Palette");
        copyPalMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyPalMenuActionPerformed(evt);
            }
        });
        jMenu2.add(copyPalMenu);

        copyColorMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyColorMenu.setText("Copy Color");
        copyColorMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyColorMenuActionPerformed(evt);
            }
        });
        jMenu2.add(copyColorMenu);

        pasteColorMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteColorMenu.setText("Paste");
        pasteColorMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteColorMenuActionPerformed(evt);
            }
        });
        jMenu2.add(pasteColorMenu);
        jMenu2.add(jSeparator3);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Reset Zoom");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        findMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        findMenu.setText("Find Palette");
        findMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findMenuActionPerformed(evt);
            }
        });
        jMenu2.add(findMenu);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");

        jMenuItem2.setText("About");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openRomMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openRomMenuActionPerformed
        openRom();
        addressChanged();
    }//GEN-LAST:event_openRomMenuActionPerformed

    private void copyPalMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyPalMenuActionPerformed
        ArrayList<Color> colors = new ArrayList<Color>(16);
        for (int i = 0 ; i < 16 ; ++i){
            colors.add(manager.getColor(i));
        }
        clipboard.set(colors);
        pasteColorMenu.setEnabled(true);
    }//GEN-LAST:event_copyPalMenuActionPerformed

    private void openGuideMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openGuideMenuActionPerformed
        openGuide();
        addressChanged();
    }//GEN-LAST:event_openGuideMenuActionPerformed

    private void exitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuActionPerformed
        if (askSaveRom())
            System.exit(0);
    }//GEN-LAST:event_exitMenuActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (askSaveRom())
            System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void paletteComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paletteComboActionPerformed
        enteringCombo();
    }//GEN-LAST:event_paletteComboActionPerformed

    private void colorPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel1MousePressed
        setOffset(0);
    }//GEN-LAST:event_colorPanel1MousePressed

    private void colorPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel2MousePressed
        setOffset(1);
    }//GEN-LAST:event_colorPanel2MousePressed

    private void colorPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel3MousePressed
        setOffset(2);
    }//GEN-LAST:event_colorPanel3MousePressed

    private void colorPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel4MousePressed
        setOffset(3);
    }//GEN-LAST:event_colorPanel4MousePressed

    private void colorPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel5MousePressed
        setOffset(4);
    }//GEN-LAST:event_colorPanel5MousePressed

    private void colorPanel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel6MousePressed
        setOffset(5);
    }//GEN-LAST:event_colorPanel6MousePressed

    private void colorPanel7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel7MousePressed
        setOffset(6);
    }//GEN-LAST:event_colorPanel7MousePressed

    private void colorPanel8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel8MousePressed
        setOffset(7);
    }//GEN-LAST:event_colorPanel8MousePressed

    private void colorPanel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel9MousePressed
        setOffset(8);
    }//GEN-LAST:event_colorPanel9MousePressed

    private void colorPanel10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel10MousePressed
        setOffset(9);
    }//GEN-LAST:event_colorPanel10MousePressed

    private void colorPanel11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel11MousePressed
        setOffset(10);
    }//GEN-LAST:event_colorPanel11MousePressed

    private void colorPanel12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel12MousePressed
        setOffset(11);
    }//GEN-LAST:event_colorPanel12MousePressed

    private void colorPanel13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel13MousePressed
        setOffset(12);
    }//GEN-LAST:event_colorPanel13MousePressed

    private void colorPanel14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel14MousePressed
        setOffset(13);
    }//GEN-LAST:event_colorPanel14MousePressed

    private void colorPanel15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel15MousePressed
        setOffset(14);
    }//GEN-LAST:event_colorPanel15MousePressed

    private void colorPanel16MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel16MousePressed
        setOffset(15);
    }//GEN-LAST:event_colorPanel16MousePressed

    private void copyColorMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyColorMenuActionPerformed
        clipboard.set(manager.getColor(offset));
        pasteColorMenu.setEnabled(true);
    }//GEN-LAST:event_copyColorMenuActionPerformed

    private void pasteColorMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteColorMenuActionPerformed
        List<Color> colors = clipboard.get();
        if (colors == null || colors.isEmpty()) return;
        if (colors.size() == 1){
            setColor(offset, colors.get(0));            
        }else{
            for (int i = 0 ; i < colors.size() ; ++i){
                setColor(i, colors.get(i));
            }
            addressChanged();
        }
    }//GEN-LAST:event_pasteColorMenuActionPerformed

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        scaleImagePanel(-0.2f*evt.getWheelRotation());
//        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
//        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
//        int x = horizontalScrollBar.getValue();
//        int y = verticalScrollBar.getValue();
//        float scale = -0.2f*evt.getWheelRotation();
//        imagePanel.scale(scale);
//        horizontalScrollBar.setValue(x+1);
//        horizontalScrollBar.setValue(x);
//        verticalScrollBar.setValue(y+1);
//        verticalScrollBar.setValue(y);
//        horizontalScrollBar.setValue((int)(x+x*scale/2));
//        verticalScrollBar.setValue((int)(y+y*scale/2));
        
    }//GEN-LAST:event_formMouseWheelMoved

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        imagePanel.setScale(1.f);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JOptionPane.showMessageDialog(this,
            "Palettes of Rage " + VERSION + "\n" +
            "© Gil Costa 2012\n\n" +
            "Acknowledgment on derived work\n"+
            "would be appreciated but is not required\n\n"+
            "PoR is free software. The author can not be held responsible\nfor any illicit use of this program.\n",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void paletteComboKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paletteComboKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_LEFT){
            if (offset == 0) setOffset(15);
            else setOffset(offset-1);
        }else if (evt.getKeyCode() == KeyEvent.VK_RIGHT){
            if (offset == 15) setOffset(0);
            else setOffset(offset+1);
        }
    }//GEN-LAST:event_paletteComboKeyPressed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (askSaveRom()){
            manager = null;
            addressChanged();
            imagePanel.setImage(null);
            romName = "";
            setTitle(TITLE);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (manager != null){
            try{
                manager.save();
            }catch(IOException e){
                showError("Unable to save rom");
            }
            checkModifications();
        }            
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void findMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findMenuActionPerformed
        if (manager != null){
            try{
                long newAddress = manager.findPalette(address, guide.getType());
                addressField.setText(Guide.longToHex(newAddress));
                addressEntered();                              
            }catch(Exception e){
                e.printStackTrace();
                showError("Find got nothing");
                return;
            }
            checkModifications();
            updateSelectedColor();
        } else{
            for (int i = 0 ; i < 16 ; ++i){
                colorPanels[i].setBackground(java.awt.Color.white);
            }
        }
    }//GEN-LAST:event_findMenuActionPerformed



    
    
    
    
    
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
    private javax.swing.JTextField addressField;
    private javax.swing.JColorChooser colorChooser;
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
    private javax.swing.JPanel colorsPanel;
    private javax.swing.JMenuItem copyColorMenu;
    private javax.swing.JMenuItem copyPalMenu;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JMenuItem findMenu;
    private javax.swing.JTextField gensField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JMenuItem openGuideMenu;
    private javax.swing.JMenuItem openRomMenu;
    private javax.swing.JComboBox paletteCombo;
    private javax.swing.JMenuItem pasteColorMenu;
    private javax.swing.JTextField rgbField;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

   

    

}
