package main;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lib.Manager;
import lib.map.Palette;
import main.Gui;
import main.ImagePanel;
import main.PaletteImagePanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gil
 */
public class PaletteEditor extends javax.swing.JDialog {
    
    private JPanel[] colorPanels;
    private Border selectionBorder;
    private int selectedColor;
    private PaletteImagePanel imagePanel;
    private boolean colorNotBeingEdited;
    
    private Manager manager;
    private Gui gui;

    /**
     * Creates new form PaletteEditor
     */
    public PaletteEditor(Gui gui, Manager manager, BufferedImage img) {
        this.gui = gui;
        this.manager = manager;        
        initComponents();
        initColorComponents();
        imagePanel.setImage(img, manager.getPalette());
        updatePalettePanels();
    }
    
    private void initColorComponents(){
        imagePanel = new PaletteImagePanel();
        forImgPanel.add(imagePanel);
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
                if (colorNotBeingEdited) return;
                pickingColor();
            }            
        });
        
        addressField.setText(Long.toHexString(manager.getPaletteAddress()));
        addressField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                addressChanged();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                addressChanged();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                addressChanged();
            }
        });
    }  
    
    private long getHexFromField(JTextField field){
         String text = field.getText();
         if (text.isEmpty()) return -1;
         try{
            return Long.parseLong(text, 16);
         }catch(Exception e){
            return -1;
        }
     }
    
    
    private void updatePalettePanels(){        
        Palette palette = manager.getPalette();
        if (palette == null){
            palette = new Palette();
        }
        for (int i = 0 ; i < 16 ; ++i){
            colorPanels[i].setBackground(new Color(palette.getColor(i)));
        }
        colorNotBeingEdited = true;
        colorChooser.setColor(colorPanels[selectedColor].getBackground());
        colorNotBeingEdited = false; 
    }
    
    
    private void addressChanged(){
        long address = getHexFromField(addressField);
        if (address > 0){
            try {
                manager.readPalette(address);
                Palette pal = manager.getPalette();
                for (int i = 0 ; i < 16 ; ++i){
                    imagePanel.replaceColor(i, new Color(pal.getColor(i)));
                }
                updatePalettePanels();
            } catch (Exception ex) {
                Logger.getLogger(PaletteEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
    private void pickingColor(){
        Color color = Palette.correct(colorChooser.getColor());
        Palette pal = manager.getPalette();
        if (color.getRGB() == pal.getColor(selectedColor)) return;
        pal.setColor(selectedColor, color.getRGB());
        try { imagePanel.replaceColor(selectedColor,color); }catch(Exception e){}
        colorPanels[selectedColor].setBackground(new Color(pal.getColor(selectedColor)));        
    }
    
    private void colorPanelPressed(int id){
        JPanel panel = colorPanels[id];
        colorNotBeingEdited = true;
        colorChooser.setColor(panel.getBackground());
        colorNotBeingEdited = false;
        colorPanels[selectedColor].setBorder(null);
        selectedColor = id;
        colorPanels[selectedColor].setBorder(selectionBorder);        
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
        colorsPanel1 = new javax.swing.JPanel();
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
        colorPanel1 = new javax.swing.JPanel();
        colorChooser = new javax.swing.JColorChooser();
        jLabel1 = new javax.swing.JLabel();
        addressField = new javax.swing.JTextField();
        forImgPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        colorsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Palette", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N
        colorsPanel1.setPreferredSize(new java.awt.Dimension(256, 64));

        colorPanel2.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel2Layout.setVerticalGroup(
            colorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel3.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel3Layout.setVerticalGroup(
            colorPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel4.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel4Layout.setVerticalGroup(
            colorPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel5.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel5Layout.setVerticalGroup(
            colorPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel6.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel6Layout.setVerticalGroup(
            colorPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel7.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel7Layout.setVerticalGroup(
            colorPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel8.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel8Layout.setVerticalGroup(
            colorPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel9.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel9Layout.setVerticalGroup(
            colorPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel10.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel10Layout.setVerticalGroup(
            colorPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel11.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel11Layout.setVerticalGroup(
            colorPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel12.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel12Layout.setVerticalGroup(
            colorPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel13.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel13.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel13Layout.setVerticalGroup(
            colorPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel14.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel14.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel14Layout.setVerticalGroup(
            colorPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel15.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel15.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel15Layout.setVerticalGroup(
            colorPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel16.setBackground(new java.awt.Color(255, 255, 255));
        colorPanel16.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel16Layout.setVerticalGroup(
            colorPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        colorPanel1.setBackground(new java.awt.Color(255, 255, 255));
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        colorPanel1Layout.setVerticalGroup(
            colorPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorsPanel1Layout = new javax.swing.GroupLayout(colorsPanel1);
        colorsPanel1.setLayout(colorsPanel1Layout);
        colorsPanel1Layout.setHorizontalGroup(
            colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, colorsPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(colorPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(colorPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(colorPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(colorPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(colorPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(colorPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(colorPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(colorPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)))
        );
        colorsPanel1Layout.setVerticalGroup(
            colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPanel1Layout.createSequentialGroup()
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(colorsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        colorChooser.setMinimumSize(new java.awt.Dimension(429, 32));
        colorChooser.setPreferredSize(new java.awt.Dimension(429, 32));

        jLabel1.setText("Address");

        addressField.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        addressField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        addressField.setText("200");
        addressField.setMargin(new java.awt.Insets(1, 1, 1, 1));
        addressField.setMinimumSize(new java.awt.Dimension(6, 16));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(colorChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorsPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout forImgPanelLayout = new javax.swing.GroupLayout(forImgPanel);
        forImgPanel.setLayout(forImgPanelLayout);
        forImgPanelLayout.setHorizontalGroup(
            forImgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 253, Short.MAX_VALUE)
        );
        forImgPanelLayout.setVerticalGroup(
            forImgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forImgPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(forImgPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void colorPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel2MousePressed
        colorPanelPressed(1);
    }//GEN-LAST:event_colorPanel2MousePressed

    private void colorPanel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel3MousePressed
        colorPanelPressed(2);
    }//GEN-LAST:event_colorPanel3MousePressed

    private void colorPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel4MousePressed
        colorPanelPressed(3);
    }//GEN-LAST:event_colorPanel4MousePressed

    private void colorPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel5MousePressed
        colorPanelPressed(4);
    }//GEN-LAST:event_colorPanel5MousePressed

    private void colorPanel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel6MousePressed
        colorPanelPressed(5);
    }//GEN-LAST:event_colorPanel6MousePressed

    private void colorPanel7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel7MousePressed
        colorPanelPressed(6);
    }//GEN-LAST:event_colorPanel7MousePressed

    private void colorPanel8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel8MousePressed
        colorPanelPressed(7);
    }//GEN-LAST:event_colorPanel8MousePressed

    private void colorPanel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel9MousePressed
        colorPanelPressed(8);
    }//GEN-LAST:event_colorPanel9MousePressed

    private void colorPanel10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel10MousePressed
        colorPanelPressed(9);
    }//GEN-LAST:event_colorPanel10MousePressed

    private void colorPanel11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel11MousePressed
        colorPanelPressed(10);
    }//GEN-LAST:event_colorPanel11MousePressed

    private void colorPanel12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel12MousePressed
        colorPanelPressed(11);
    }//GEN-LAST:event_colorPanel12MousePressed

    private void colorPanel13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel13MousePressed
        colorPanelPressed(12);
    }//GEN-LAST:event_colorPanel13MousePressed

    private void colorPanel14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel14MousePressed
        colorPanelPressed(13);
    }//GEN-LAST:event_colorPanel14MousePressed

    private void colorPanel15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel15MousePressed
        colorPanelPressed(14);
    }//GEN-LAST:event_colorPanel15MousePressed

    private void colorPanel16MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel16MousePressed
        colorPanelPressed(15);
    }//GEN-LAST:event_colorPanel16MousePressed

    private void colorPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanel1MousePressed
        colorPanelPressed(0);
    }//GEN-LAST:event_colorPanel1MousePressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            manager.savePalette();
        } catch (Exception ex) {
            Logger.getLogger(PaletteEditor.class.getName()).log(Level.SEVERE, null, ex);
        }        
        gui.hardRefresh();
        gui.updatePalettePanels();
        gui.setEnabled(true);
    }//GEN-LAST:event_formWindowClosing


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
    private javax.swing.JPanel colorsPanel1;
    private javax.swing.JPanel forImgPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
