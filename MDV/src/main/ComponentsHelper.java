/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;
import javax.swing.*;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Gil
 */
public class ComponentsHelper {
    
    public static final int INVALID_INT = Integer.MIN_VALUE;
    public static final long INVALID_LONG = Long.MIN_VALUE;
    
    private HashSet<JTextField> inUse = new HashSet<JTextField>();
    
    
    public static void setupField(JTextField field, DocumentListener listener){
        field.getDocument().addDocumentListener(listener);
    }    
    
    public boolean aquire(JTextField field){
        if (inUse.contains(field)) return false;
        inUse.add(field);
        return true;
    }
    
    public void release(JTextField field){
        inUse.remove(field);
    }
    
    
    public int getIntFromField(JTextField field, int lowerLim, int upperLim){
         String text = field.getText();
         if (text.isEmpty()) return INVALID_INT;
         try{
            int res = Integer.parseInt(text);
            if (res < lowerLim || res > upperLim) return INVALID_INT;
            return res;
         }catch(Exception e){
            return INVALID_INT;
        }
     }
     public int getPositiveIntFromField(JTextField field){
         return getIntFromField(field, 0, Integer.MAX_VALUE);
     }
     public String getField(JTextField field){
         return field.getText();
     }
     public int getIntFromField(JTextField field){
         String text = field.getText();
         if (text.isEmpty()) return INVALID_INT;
         try{
            return Integer.parseInt(text);
         }catch(Exception e){
            return INVALID_INT;
        }
     }
     public long getHexFromField(JTextField field){
         String text = field.getText();
         if (text.isEmpty()) return INVALID_LONG;
         try{
            return Long.parseLong(text, 16);
         }catch(Exception e){
            return INVALID_LONG;
        }
     }
     public void setField(JTextField field, String value){
         if (inUse.contains(field)) return; // in use, ignore
         if (!field.getText().equals(value)){
             inUse.add(field);
             field.setBackground(Color.white);
             field.setText(value);  
             inUse.remove(field);
         }
     }
     public void setField(JTextField field, int value){
         setField(field, Integer.toString(value));
     }
     public void setField(JTextField field, long value){
         setField(field, Long.toString(value));
     }
     public void setFieldAsHex(JTextField field, long value){
         setField(field, Long.toString(value, 16));
     }
     public void setFieldAsHex(JTextField field, int value){
         setField(field, Integer.toString(value, 16));
     }
     public void setCheck(JCheckBox check, boolean value){
         if (check.isSelected() != value)
            check.setSelected(value);
     }
     public void setComboSelection(JComboBox combo, int newIndex) {
        if (combo.getSelectedIndex() != newIndex){
            combo.setSelectedIndex(newIndex);
        }
    }
     
     
     public static void showError(String text){
        String message;
        String title;
        title = "Yikes!";
        message = text;
        JOptionPane.showMessageDialog(null,
            message, title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public static void showWarning(String text){
        String message;
        String title;
        title = "Wops!";
        message = text;
        JOptionPane.showMessageDialog(null,
            message, title,
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    
    public static void setEnable(JComponent component, boolean enabled){
        component.setEnabled(enabled);
        Component[] com = component.getComponents();  
        for (int a = 0; a < com.length; a++) {  
            if (com[a] instanceof JComponent)
                setEnable((JComponent)com[a], enabled);
            else com[a].setEnabled(enabled);
        } 
    }
    
}
