/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.Voice;
import lib.Bank;
import lib.Rom;
import lib.bank.BankStrategy;
import lib.bank.FiveBytesBankStrategy;
import lib.voice.VoiceStrategy;
import lib.voice.CompressedVoiceStrategy;

/**
 *
 * @author Gil
 */
public class GlobalData {    
    public static final String GUIDES_DIR = "Guides/";    
    public static final byte[] DEFAULT_TABLE = {
         0x00,  0x01,  0x02,  0x04,  0x08,  0x10,  0x20,  0x40,
        -0x80, -0x01, -0x02, -0x04, -0x08, -0x10, -0x20, -0x40
    };
        
    public static boolean embeddedTable;
    public static byte[] table = DEFAULT_TABLE;   
    
    public static Bank bank;        // bank in use
    public static long bankAddress;
    
    public static Guide guide;
    
    public static VoiceStrategy voiceStrategy = new CompressedVoiceStrategy();
    public static BankStrategy bankStrategy = new FiveBytesBankStrategy();
    
    public static String fullRomName;
    public static String romName;
    
    
    
    private static Rom openRom() {
        if (fullRomName == null) return null;
        try {
            return new Rom(fullRomName);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            ComponentsHelper.showError("Access denied to rom \"" + romName + "\"");
        }
        return null;
    }
    
    static boolean readBank(long address, int numVoices) {
        Rom rom = openRom();
        if (rom == null) return false;
        try {
            if (embeddedTable)
                bank = rom.readBank(address, numVoices, null, bankStrategy, voiceStrategy);
            else bank = rom.readBank(address, numVoices, table, bankStrategy, voiceStrategy);
            bankAddress = address;
            rom.close();
            return true;
        } catch (IOException ex) {
            try { rom.close(); } catch (IOException ex1) { ex1.printStackTrace(); }
            ex.printStackTrace();
            ComponentsHelper.showError("Unable to load voices bank at address " + Long.toHexString(address) + ", with " + numVoices + " voices");
        }
        return false;
    }
    
    
    static boolean writeBank() {
        Rom rom = openRom();
        if (rom == null) return false;
        try {
            rom.writeBank(bank, bankAddress, bankStrategy);
            rom.close();
            return true;
        } catch (IOException ex) {
            try { rom.close(); } catch (IOException ex1) { ex1.printStackTrace(); }
            ex.printStackTrace();
            ComponentsHelper.showError("Unable to write voices bank at address " + Long.toHexString(bankAddress));
        }
        return false;
    }
    
    
    public static Voice readVoiceFromFile(File file, int pitch) {
        try {
            return Voice.readFromWave(file, bank.getTable(), pitch, voiceStrategy);
        } catch (Exception ex) {
            ComponentsHelper.showError(file.getName() + " is not a valid sound");
            ex.printStackTrace();
            return null;
        }
    }
    
    
    private static int getMaxSize(){
         return Bank.MAX_BANK_SIZE;
    }
    
    public static int getBankFreeSpace() {
        if (bank == null) return getMaxSize();
        return bankStrategy.getMaxSize() - bank.getSizeInBytes(bankStrategy);
    }    
    
    
    public static boolean isTableEmbedded(){
        return embeddedTable;
    } 
    
    
    public static void readGuide(File guideFile) throws Exception{
        guide = new Guide(guideFile);  
        readBankFromGuide(0);
    }
    
    
    public static int getNumBanks(){
        if (guide == null) return 0;
        return guide.getNumBanks();
    }
    
    public static void readBankFromGuide(int bankId){
        if (guide == null) return;
        embeddedTable = guide.isEmbedded(bankId);
        if (!embeddedTable)
        table = guide.getTable(bankId);

        bankAddress = guide.getAddress(bankId);
        int numVoices = guide.getNumVoices(bankId);

        voiceStrategy = guide.getVoiceStrategy(bankId);
        bankStrategy = guide.getBankStrategy(bankId);
        
        readBank(bankAddress, numVoices);
    }
    
    
    private static String processRomName(String name){
        int last = name.length()-1;
        while (last != 0 && name.charAt(last) == ' ') --last;
        String theName = name.substring(0,last+1).toLowerCase();
        // clear duplicated spaces
        StringBuilder bs = new StringBuilder();                
        char[] chars = theName.toCharArray();
        boolean wasSpace = chars[0] == ' ';
        bs.append(chars[0]);
        for (int i = 1; i < chars.length ; ++i){
            char c = chars[i];
            if (c == ' '){
                if (!wasSpace) bs.append(c);
                wasSpace = true;
            }else{
                wasSpace = false;
                bs.append(c);
            }
        }
        return bs.toString();
    }
    
    private static File guessGuide(String name){
        String theName = processRomName(name);
        File dir = new File(GUIDES_DIR);
        File[] files = dir.listFiles();
        for (File f: files){
            if (f.getName().toLowerCase().startsWith(theName)){
                return f;
            }
        }
        return null;
    }

    public static File guessGuide() {
        Rom rom = openRom();
        String name1, name2;
        try {            
            name1 = rom.readRomName1();
            name2 = rom.readRomName2();
            rom.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            try { rom.close(); } catch (IOException ex1) { ex1.printStackTrace(); }
            ComponentsHelper.showWarning("\"" + romName + "\" doesn't seem to be a valid rom");
            return null;
        }
        char c1 = name1.charAt(0);
        File file = null;
        if (c1 == romName.toLowerCase().charAt(0)){
            file = guessGuide(name1);
        } else file = guessGuide(name2);
        if (file != null){
            try {
                readGuide(file);
                return file;
            } catch (Exception ex) {
                ex.printStackTrace();                
            }
        }
        return null;
    }
                               
}
