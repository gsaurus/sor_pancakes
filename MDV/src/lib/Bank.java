/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import lib.bank.BankStrategy;
import lib.voice.VoiceStrategy;

/**
 *
 * @author Gil
 */
public class Bank {
    public static final int MAX_BANK_SIZE = 0x8000;
    
    private byte[] table;
    private boolean embedded;
    private Voice[] voices;
    
    private boolean modified;
    
    private HashMap<Voice, Integer> voiceIds;
    private int nextVoiceId;
    
    // Private Constructor
    private Bank(int numSounds, byte[] table){
        this.nextVoiceId = 0;
        this.voices = new Voice[numSounds];
        this.table = table;
        this.embedded = table == null;
        this.voiceIds = new HashMap<Voice, Integer>();
        this.modified = false;
    }
    
    // Read bank from ROM
    static Bank read(RandomAccessFile rom, long address, int numSounds, byte[] table, BankStrategy bankStrategy, VoiceStrategy voiceStrategy) throws IOException {
        Bank bank = new Bank(numSounds, table);
        rom.seek(address);
        TreeMap<Long, Voice> voicesRead = new TreeMap<Long, Voice>(); // used to find clones
        for (int i = 0 ; i < numSounds ; ++i){
            VoiceInfo voiceInfo = bankStrategy.read(rom,address, i);                                    
            if (voicesRead.containsKey(voiceInfo.address)){                
                // voice already read, it is a clone
                final Voice originalVoice = voicesRead.get(voiceInfo.address);
                bank.voices[i] = new Voice(originalVoice, voiceInfo.pitch);
                bank.voiceIds.put(bank.voices[i], bank.voiceIds.get(originalVoice));
                continue;
            }    
//            System.out.println("read: " + Long.toHexString(voiceInfo.address) + ", " + Long.toHexString(voiceInfo.size));
            long voiceAddress = voiceInfo.address; // used for clone control only
            
            // if embedded table, read table and update header            
            if (bank.embedded){
                bank.table = new byte[16];
                rom.seek(voiceInfo.address);
                for (int j = 0 ; j < 16 ; ++j){
                    bank.table[j] = rom.readByte();
                }
                voiceInfo.address += 16;
                voiceInfo.size = Math.max(voiceInfo.size-16, 0);
            }
            
            // read audio
            bank.voices[i] = Voice.read(rom, bank.table, voiceInfo, voiceStrategy);
            bank.voiceIds.put(bank.voices[i], bank.nextVoiceId++);
            voicesRead.put(voiceAddress, bank.voices[i]);
        }
//        System.out.println();
        return bank;
    }
    
    
    // Write bank to rom
    protected void write(RandomAccessFile rom, long bankAddress, BankStrategy bankStrategy) throws IOException {
        rom.seek(bankAddress);
        int numVoices = voices.length;
        // next address is used to write each voice one after another
        int headerSize = bankStrategy.getHeaderSize()*numVoices;
        long nextAddress = 0;
        HashMap<Integer,Long> addresses = new HashMap<Integer,Long>();
        for (int i = 0 ; i < numVoices ; ++i){
            
            // Get voice
            Voice voice = voices[i];  
            // Get header info
            VoiceInfo info = new VoiceInfo();
            info.address = nextAddress;
            info.size = voice.getSizeInBytes();
            info.pitch = voice.getPitch();
            if (embedded) info.size += 16;
            int voiceId = voiceIds.get(voice);
            if (addresses.containsKey(voiceId)){
                info.address = addresses.get(voiceId);
                bankStrategy.write(rom, i, info, bankAddress, headerSize);
                continue;
            }   
            addresses.put(voiceId, info.address);
            // Write header
            long finalAddress = bankStrategy.write(rom, i, info, bankAddress, headerSize);
//            System.out.println("write: " + Long.toHexString(finalAddress) + ", "+ Long.toHexString(info.size));
            
            // Write Voice
            if (embedded){
                // write embedded table first
                rom.seek(finalAddress);
                rom.write(table);
                // update address so voice write at the correct place
                finalAddress += table.length;
                voice.write(rom, finalAddress);                
            }
            voice.write(rom, finalAddress);
            nextAddress += info.size;
        }
        modified = false;
//        System.out.println();
    }
    
//    TODO: write voice at the address provided by bankStrategy.write (which will return the final address and not the written one)
//    use that same address to write the table 16 bytes
//    keep nextAddress safe from those modifications
        
    
    // Compute the total size of the bank
    public int getSizeInBytes(BankStrategy bankStrategy){
        int numVoices = voices.length;        
        int total = 0;        
        TreeSet<Integer> ids = new TreeSet<Integer>();
        for (int i = 0 ; i < numVoices ; ++i){
            Voice voice = voices[i];
            int id = voiceIds.get(voice);
            if (!ids.contains(id)){
                ids.add(id);
                if (embedded) total += table.length;
                total += voice.getSizeInBytes();
            }
        }
        total += bankStrategy.getFirstVoiceOffset(numVoices);
        return total;
    }
    
    
    // Get the number of voices of the bank
    public int getNumVoices(){
        return voices.length;
    }
    
    
    // Get voice at index id
    public Voice getVoice(int id){
        return voices[id];
    }
    
    
    // Replace voice at index id
    public void setVoice(int id, Voice voice){
        voiceIds.remove(voices[id]);
        voices[id] = voice;
        voiceIds.put(voices[id], nextVoiceId++);
        modified = true;
    }
    
    // Create a clone of src into target
    public void cloneVoice(int src, int target){
        Voice srcVoice = voices[src];
        Voice targetVoice = voices[target];
        int srcId = voiceIds.get(srcVoice);
        if (srcId == voiceIds.get(targetVoice)) return;
        setVoice(target, new Voice(srcVoice, srcVoice.getPitch()));
        voiceIds.put(voices[target], srcId);
    }
    
    // swap two voices
    public void swapVoices(int id1, int id2){
        Voice temp = voices[id1];
        voices[id1] = voices[id2];
        voices[id2] = temp;
        modified = true;
    }

    
    public byte[] getTable() {
        return table;
    }

    
    public boolean wasModified(){
        return modified;
    }

    public void ignoreModifications() {
        modified = false;
    }

    public void setVoicePitch(int index, int newPitch) {
        voices[index].setPitch(newPitch);
        modified = true;
    }
    
}
