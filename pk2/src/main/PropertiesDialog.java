/*
 * Decompiled with CFR 0_132.
 */
package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import lib.Manager;
import main.Gui;
import main.PassDoc;

public class PropertiesDialog
extends JDialog {
    private Gui gui;
    private Manager manager;
    private BufferedImage portrait;
    private JLabel imageLabel;
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JPanel jPanel1;
    private JPanel jPanel3;
    private JPasswordField jumpStatsField;
    private JTextField nameField;
    private JPasswordField powerStatsField;
    private JTextField speedField;
    private JPasswordField speedStatsField;
    private JPasswordField staminaStatsField;
    private JPasswordField techniqueStatsField;

    public PropertiesDialog(Frame parent, boolean modal) {
        super(parent, modal);
        this.initComponents();
        this.setLocationRelativeTo(parent);
        this.imageLabel.setText("");
    }

    public PropertiesDialog(Gui gui, final Manager manager) {
        this((Frame)gui, true);
        this.gui = gui;
        this.manager = manager;
        this.nameField.setDocument(new PlainDocument(){

            @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }
                str = str.toUpperCase();
                block14 : for (char c : str.toCharArray()) {
                    switch (c) {
                        case ' ': {
                            continue block14;
                        }
                        case '!': {
                            continue block14;
                        }
                        case '\"': {
                            continue block14;
                        }
                        case '\'': {
                            continue block14;
                        }
                        case '(': {
                            continue block14;
                        }
                        case ')': {
                            continue block14;
                        }
                        case ',': {
                            continue block14;
                        }
                        case '.': {
                            continue block14;
                        }
                        case '/': {
                            continue block14;
                        }
                        case '*': {
                            continue block14;
                        }
                        case '?': {
                            continue block14;
                        }
                        case '\u00a9': {
                            continue block14;
                        }
                        default: {
                            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'Z') continue block14;
                            return;
                        }
                    }
                }
                if (this.getLength() + str.length() <= manager.getNameMaxLen()) {
                    super.insertString(offset, str, a);
                }
            }
        });
        this.powerStatsField.setDocument(new PassDoc());
        this.techniqueStatsField.setDocument(new PassDoc());
        this.speedStatsField.setDocument(new PassDoc());
        this.jumpStatsField.setDocument(new PassDoc());
        this.staminaStatsField.setDocument(new PassDoc());
        this.speedField.setDocument(new PlainDocument(){

            @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }
                str = str.toUpperCase();
                for (char c : str.toCharArray()) {
                    if (c >= '0' && c <= '9') continue;
                    return;
                }
                super.insertString(offset, str, a);
            }
        });
        this.loadStuff();
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        this.jLabel1 = new JLabel();
        this.nameField = new JTextField();
        this.jLabel2 = new JLabel();
        this.speedField = new JTextField();
        this.jPanel3 = new JPanel();
        this.jLabel3 = new JLabel();
        this.jLabel4 = new JLabel();
        this.jLabel5 = new JLabel();
        this.jLabel6 = new JLabel();
        this.powerStatsField = new JPasswordField();
        this.techniqueStatsField = new JPasswordField();
        this.speedStatsField = new JPasswordField();
        this.jumpStatsField = new JPasswordField();
        this.jLabel7 = new JLabel();
        this.staminaStatsField = new JPasswordField();
        this.imageLabel = new JLabel();
        this.jButton1 = new JButton();
        this.setDefaultCloseOperation(2);
        this.setTitle("Character Properties");
        this.setModal(true);
        this.jLabel1.setFont(new Font("Tahoma", 1, 14));
        this.jLabel1.setText("Name:");
        this.nameField.setFont(new Font("Tahoma", 1, 14));
        this.jLabel2.setFont(new Font("Tahoma", 0, 14));
        this.jLabel2.setText("Walking speed:");
        this.speedField.setFont(new Font("Tahoma", 1, 14));
        this.jPanel3.setBorder(BorderFactory.createTitledBorder(null, "Stats", 0, 0, new Font("Tahoma", 1, 12), new Color(0, 0, 0)));
        this.jLabel3.setFont(new Font("Tahoma", 0, 14));
        this.jLabel3.setHorizontalAlignment(4);
        this.jLabel3.setText("Power:");
        this.jLabel4.setFont(new Font("Tahoma", 0, 14));
        this.jLabel4.setHorizontalAlignment(4);
        this.jLabel4.setText("Technique:");
        this.jLabel5.setFont(new Font("Tahoma", 0, 14));
        this.jLabel5.setHorizontalAlignment(4);
        this.jLabel5.setText("Speed:");
        this.jLabel6.setFont(new Font("Tahoma", 0, 14));
        this.jLabel6.setHorizontalAlignment(4);
        this.jLabel6.setText("Jump:");
        this.powerStatsField.setColumns(2);
        this.powerStatsField.setFont(new Font("Tahoma", 1, 14));
        this.powerStatsField.setText("abc");
        this.powerStatsField.setMinimumSize(new Dimension(28, 21));
        this.techniqueStatsField.setColumns(2);
        this.techniqueStatsField.setFont(new Font("Tahoma", 1, 14));
        this.techniqueStatsField.setText("abc");
        this.techniqueStatsField.setMinimumSize(new Dimension(28, 21));
        this.speedStatsField.setColumns(2);
        this.speedStatsField.setFont(new Font("Tahoma", 1, 14));
        this.speedStatsField.setText("abc");
        this.speedStatsField.setMinimumSize(new Dimension(28, 21));
        this.jumpStatsField.setColumns(2);
        this.jumpStatsField.setFont(new Font("Tahoma", 1, 14));
        this.jumpStatsField.setText("abc");
        this.jumpStatsField.setMinimumSize(new Dimension(28, 21));
        this.jLabel7.setFont(new Font("Tahoma", 0, 14));
        this.jLabel7.setHorizontalAlignment(4);
        this.jLabel7.setText("Stamina:");
        this.staminaStatsField.setColumns(2);
        this.staminaStatsField.setFont(new Font("Tahoma", 1, 14));
        this.staminaStatsField.setText("abc");
        this.staminaStatsField.setMinimumSize(new Dimension(28, 21));
        GroupLayout jPanel3Layout = new GroupLayout(this.jPanel3);
        this.jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(jPanel3Layout.createSequentialGroup().addGap(4, 4, 4).addComponent(this.jLabel6)).addComponent(this.jLabel5)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.speedStatsField, -2, -1, -2).addComponent(this.jumpStatsField, -2, -1, -2))).addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jLabel3, -1, -1, 32767).addComponent(this.jLabel4, -1, 69, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.techniqueStatsField, -2, -1, -2).addComponent(this.powerStatsField, -2, -1, -2))).addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addComponent(this.jLabel7, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.staminaStatsField, -2, -1, -2)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.powerStatsField, -2, -1, -2).addComponent(this.jLabel3)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel4).addComponent(this.techniqueStatsField, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.speedStatsField, -2, -1, -2).addComponent(this.jLabel5)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel6).addComponent(this.jumpStatsField, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel7).addComponent(this.staminaStatsField, -2, -1, -2))));
        this.imageLabel.setBackground(new Color(0, 0, 0));
        this.imageLabel.setText("jLabel8");
        this.imageLabel.setBorder(BorderFactory.createLineBorder(UIManager.getDefaults().getColor("Button.darkShadow")));
        this.imageLabel.setCursor(new Cursor(12));
        this.imageLabel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent evt) {
                PropertiesDialog.this.imageLabelMouseClicked(evt);
            }
        });
        this.jButton1.setFont(new Font("Tahoma", 0, 16));
        this.jButton1.setText("Ok");
        this.jButton1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                PropertiesDialog.this.jButton1ActionPerformed(evt);
            }
        });
        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.speedField, -2, 96, -2)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.imageLabel).addGap(18, 18, 18).addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.nameField, -2, 96, -2)))).addGroup(jPanel1Layout.createSequentialGroup().addGap(43, 43, 43).addComponent(this.jPanel3, -2, -1, -2))).addGap(0, 0, 32767)).addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.jButton1, -2, 79, -2).addGap(63, 63, 63)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(0, 0, 32767).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.nameField, -2, -1, -2))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.imageLabel).addGap(0, 0, 32767))).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel2).addComponent(this.speedField, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jPanel3, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton1).addContainerGap()));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanel1, -1, -1, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanel1, -2, -1, -2));                
        this.pack();
    }
    
    private void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }

    private void imageLabelMouseClicked(MouseEvent evt) {
        this.portrait = this.gui.openImageForPortrait();
        this.setPortraitImage(this.portrait);
    }

    private boolean writeName() {
        int maxLen = this.manager.getNameMaxLen();
        String newName = this.nameField.getText();
        if ((newName = newName.toUpperCase()).length() > maxLen) {
            this.gui.showError("Name can't have more than " + maxLen + " characters");
            this.nameField.setText(newName.substring(0, maxLen));
            return false;
        }
        block16 : for (char c : newName.toCharArray()) {
            switch (c) {
                case ' ': {
                    continue block16;
                }
                case '!': {
                    continue block16;
                }
                case '\"': {
                    continue block16;
                }
                case '\'': {
                    continue block16;
                }
                case '(': {
                    continue block16;
                }
                case ')': {
                    continue block16;
                }
                case ',': {
                    continue block16;
                }
                case '.': {
                    continue block16;
                }
                case '/': {
                    continue block16;
                }
                case '*': {
                    continue block16;
                }
                case '?': {
                    continue block16;
                }
                case '\u00a9': {
                    continue block16;
                }
                default: {
                    if (c >= '0' && c <= '9' || c >= 'A' && c <= 'Z') continue block16;
                    this.gui.showError("New name contains invalid characters\nAllowed chars: A-Z space ! \" ' ( ) , . / * ? \u00a9");
                    return false;
                }
            }
        }
        try {
            this.manager.writeName(newName);
        }
        catch (IOException e) {
            e.printStackTrace();
            this.gui.showError("Unable to write character name");
            return false;
        }
        return true;
    }

    private boolean writePortrait() {
        if (this.portrait != null) {
            try {
                this.manager.writePortrait(this.portrait);
            }
            catch (IOException ex) {
                this.gui.showError("Unable to write new portrait");
                return false;
            }
        }
        return true;
    }

    private boolean writeSpeed() {
        int newSpeed = this.gui.getIntFromField(this.speedField);
        try {
            this.manager.writeSpeed(newSpeed);
        }
        catch (IOException e) {
            e.printStackTrace();
            this.gui.showError("Unable to write character speed");
            return false;
        }
        return true;
    }

    private int getStats(JPasswordField field) {
        int val = field.getPassword().length - 1;
        if (val > 2) {
            val = 2;
        } else if (val < 0) {
            val = 0;
        }
        return val;
    }

    private boolean writeStats() {
        int power = this.getStats(this.powerStatsField);
        int tec = this.getStats(this.techniqueStatsField);
        int speed = this.getStats(this.speedStatsField);
        int jump = this.getStats(this.jumpStatsField);
        int stamina = this.getStats(this.staminaStatsField);
        try {
            this.manager.writePowerStats(power);
            this.manager.writeTechniqueStats(tec);
            this.manager.writeSpeedStats(speed);
            this.manager.writeJumpStats(jump);
            this.manager.writeStaminaStats(stamina);
        }
        catch (IOException e) {
            e.printStackTrace();
            this.gui.showError("Unable to write stats");
            return false;
        }
        return true;
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        if (!this.writePortrait()) {
            return;
        }
        if (!gui.isPlayableChar()) {
            this.dispose();
            return;
        }
        if (!this.writeName()) {
            return;
        }
        if (!this.writeSpeed()) {
            return;
        }        
        if (!this.writeStats()) {
            return;
        }
        this.dispose();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (!"Nimbus".equals(info.getName())) continue;
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(PropertiesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(PropertiesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(PropertiesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PropertiesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                PropertiesDialog dialog = new PropertiesDialog(new JFrame(), true);
                dialog.addWindowListener(new WindowAdapter(){

                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }

        });
    }

    private boolean readNamer() {
        String currName = "";
        try {
            currName = this.manager.readName();
        }
        catch (IOException e) {
            e.printStackTrace();
            this.gui.showError("Unable to read character name");
            return false;
        }
        while (!currName.isEmpty() && currName.endsWith(" ")) {
            currName = currName.substring(0, currName.length() - 1);
        }
        this.nameField.setText(currName);
        return true;
    }

    private boolean readSpeed() {
        int currSpeed = 0;
        try {
            currSpeed = this.manager.readSpeed();
        }
        catch (IOException e) {
            e.printStackTrace();
            this.gui.showError("Unable to read character speed");
            return false;
        }
        this.speedField.setText("" + currSpeed + "");
        return true;
    }

    private boolean readIcon() {
        BufferedImage img = null;
        try {
            img = this.manager.readPortrait();
        }
        catch (IOException e) {
            e.printStackTrace();
            this.gui.showError("Unable to read character portrait");
            return false;
        }
        this.setPortraitImage(img);
        return true;
    }

    private void setStats(JPasswordField field, int value) {
        if (value < 0) {
            value = 0;
        }
        if (value > 2) {
            value = 2;
        }
        if (value == 0) {
            field.setText("*");
        } else if (value == 1) {
            field.setText("**");
        } else {
            field.setText("***");
        }
    }

    private boolean readStats() {
        int jump;
        int speed;
        int tec;
        int power;
        int stamina;
        try {
            power = this.manager.readPowerStats();
            tec = this.manager.readTechniqueStats();
            speed = this.manager.readSpeedStats();
            jump = this.manager.readJumpStats();
            stamina = this.manager.readStaminaStats();
        }
        catch (IOException e) {
            e.printStackTrace();
            this.gui.showError("Unable to read stats");
            return false;
        }
        this.setStats(this.powerStatsField, power);
        this.setStats(this.techniqueStatsField, tec);
        this.setStats(this.speedStatsField, speed);
        this.setStats(this.jumpStatsField, jump);
        this.setStats(this.staminaStatsField, stamina);
        return true;
    }

    private void close() {
        this.dispose();
    }

    private void loadStuff() {
        if (!this.readIcon()) {
            this.close();
            return;
        }
        if (!gui.isPlayableChar()) {
            enableComponents(this, false);
            imageLabel.setEnabled(true);
            jButton1.setEnabled(true);
            return;
        }
        if (!this.readNamer()) {
            this.close();
            return;
        }
        if (!this.readSpeed()) {
            this.close();
            return;
        }        
        if (!this.readStats()) {
            this.close();
            return;
        }
    }

    private void setPortraitImage(BufferedImage img) {
        if (img == null) {
            return;
        }
        BufferedImage after = new BufferedImage(32, 32, 2);
        AffineTransform at = new AffineTransform();
        at.scale(2.0, 2.0);
        AffineTransformOp scaleOp = new AffineTransformOp(at, 1);
        after = scaleOp.filter(img, after);
        this.imageLabel.setIcon(new ImageIcon(after));
    }

}

