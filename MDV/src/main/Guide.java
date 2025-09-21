/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import lib.bank.BankStrategy;
import lib.bank.CustomBankStrategy;
import lib.voice.CompressedVoiceStrategy;
import lib.voice.RawVoiceStrategy;
import lib.voice.VoiceStrategy;

class BankInfo{
    public long address;
    public int numVoices;
}

class StrategyInfo{
    public BankStrategy bankStrategy;
    public VoiceStrategy voiceStrategy;
    public byte[] table;
}

/**
 *
 * @author Gil
 */
public class Guide {    
    private List<BankInfo> banksInfo;
    private List<StrategyInfo> strategyInfo;
    
    private boolean isAllSpaces(String txt) throws Exception{
        for(char c: txt.toCharArray()){
            if (c != ' ' && c != '\t' && c != '\n' ) return false;
        }
        return true;
    }
    
    private String removeSpaces(String txt) throws Exception{
        if (txt.isEmpty()) return txt;
        int i;
        for (i = 0 ; i < txt.length() && (txt.charAt(i) == ' ' || txt.charAt(i) == '\t') ; ++i );
        return txt.substring(i);
    }
    
    private String getFirstPart(String txt) throws Exception{
        int colonId = txt.indexOf(':');
        if (colonId == -1) return "";
        return removeSpaces(txt.substring(0, colonId)).toLowerCase();
    }
    
    private String getLastPart(String txt) throws Exception{
        return removeSpaces(txt.substring(txt.indexOf(':')+1)).toLowerCase();
    }
    
    private boolean getBool(String txt) throws Exception{
        return txt.startsWith("yes");
    }
    
    private int getInt(String txt) throws Exception{
        return new Scanner(txt).nextInt();
    }
    
    private long getHex(String txt) throws Exception{
        return new Scanner(txt).nextInt(16);
    }
    
    private byte[] readTable(String tableText) throws Exception{
        Scanner sc = new Scanner(tableText);
        ByteArrayOutputStream dos = new ByteArrayOutputStream();
        while (sc.hasNextInt(16)){
            dos.write(sc.nextInt(16));
        }
        return dos.toByteArray();
    }
    
    
    private StrategyInfo readHeaderInfo(Scanner sc) throws Exception{
        StrategyInfo res = new StrategyInfo();
        int headerSize = -1;
        boolean compressed = true;
        boolean littleEndian = true;
        boolean asGlobalPointer = false;
        long baseAddress = -1;
        int baseMinimumOffset = -1;
        int pitch = -1;
        int[] order = null;
        int maxSize = -1;
        String txt;
        for(int i = 0 ; i < 9 ; ++i){
            txt = "";
            while(txt.isEmpty() || isAllSpaces(txt)) txt = sc.nextLine();            
            String key = getFirstPart(txt);
            String value = getLastPart(txt);
            if (key.equals("compressed") || key.equals("compression")){
                compressed = getBool(value);
                if (compressed){
                    res.voiceStrategy = new CompressedVoiceStrategy();
                    // get embedded and bank infos:
                    txt = "";
                    while(txt.isEmpty() || isAllSpaces(txt)) txt = sc.nextLine();
                    key = getFirstPart(txt);
                    value = getLastPart(txt);
                    if (key.equals("embedded")){
                        if (!getBool(value)){
                            res.table = readTable(sc.nextLine());
                        }
                    }
                } else res.voiceStrategy = new RawVoiceStrategy();
            }else if (key.equals("size")){
                headerSize = getInt(value);
            }else if (key.equals("base")){
                baseAddress = getHex(value);
            }else if (key.equals("offset")){
                baseMinimumOffset = (int)getHex(value);
            }else if (key.equals("pitch")){
                pitch = getInt(value);
            }else if (key.equals("little endian") || txt.equals("little")){
                littleEndian = getBool(value);
            }else if (key.equals("global ptrs") || txt.equals("global pointers") || txt.equals("global") || txt.equals("global pointer") || txt.equals("global ptr")){
                asGlobalPointer = getBool(value);
            }else if (key.equals("order")){
                Scanner sc2 = new Scanner(value);
                order = new int[4];
                for (int j = 0 ; j < 4 ; ++j){
                    String s = sc2.next().toLowerCase();
                    if (s.equals("address")) order[j] = 0;
                    else if (s.equals("size")) order[j] = 1;
                    else if (s.equals("pitch")) order[j] = 2;
                    else if (s.equals("redundancy")) order[j] = 3;
                }
            }else if (key.equals("max size") || key.equals("maximum size")){
                maxSize = (int)getHex(value);                
            }
        }
        res.bankStrategy = new CustomBankStrategy(headerSize, littleEndian, asGlobalPointer, baseAddress, baseMinimumOffset, pitch, order, maxSize);
        return res;
    }
    
    
    private List<BankInfo> readBanksInfo(Scanner sc) throws Exception {        
        String txt = "";        
        while(txt.isEmpty() || isAllSpaces(txt) || (!getFirstPart(txt).equals("banks") && !getFirstPart(txt).equals("num banks") && !getFirstPart(txt).equals("# banks") && !getFirstPart(txt).equals("number of banks"))) txt = sc.nextLine();
        int size = getInt(getLastPart(txt));
        List<BankInfo> res = new ArrayList<BankInfo>(size);
        for (int i = 0 ; i < size ; ++i){
            BankInfo info = new BankInfo();
            txt = "";
            while(txt.isEmpty() || isAllSpaces(txt)) txt = sc.nextLine();
            Scanner sc2 = new Scanner(txt);
            info.address = getHex(sc2.next());
            info.numVoices = getInt(sc2.next());
            res.add(info);
        }
        return res;
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    Guide(File file) throws Exception{
        Scanner sc = new Scanner(file);
        banksInfo = new ArrayList<BankInfo>();
        strategyInfo = new ArrayList<StrategyInfo>();
        while(sc.hasNextLine()){
            try{
                StrategyInfo headerInfo = readHeaderInfo(sc);
                List<BankInfo> values = readBanksInfo(sc);
                for (BankInfo b: values){
                    banksInfo.add(b);
                    strategyInfo.add(headerInfo);
                }
            }catch(NoSuchElementException e){
                e.printStackTrace();
            }
        }
    }
    
    
    public int getNumBanks(){
        return banksInfo.size();
    }
    
    public long getAddress(int bankId){
        return banksInfo.get(bankId).address;
    }        
    
    public int getNumVoices(int bankId){
        return banksInfo.get(bankId).numVoices;
    }   
    
    public boolean isEmbedded(int bankId){
        return strategyInfo.get(bankId).table == null;
    }
    
    public byte[] getTable(int bankId){
        return strategyInfo.get(bankId).table;
    }
    
    public BankStrategy getBankStrategy(int bankId){
        return strategyInfo.get(bankId).bankStrategy;
    }
    
    public VoiceStrategy getVoiceStrategy(int bankId){
        return strategyInfo.get(bankId).voiceStrategy;
    }
    
}
