/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

/**
 *
 * @author Gil
 */
public class VoiceInfo {
    public long address;  // voice address
    public int size;      // compressed size in bytes
    public int pitch;     // voice pitch
    
    // Default constructor
    public VoiceInfo(){
        // nothing to do
    }

    // Constructor by fields
    public VoiceInfo(long voiceAddress, int voiceSize, int voicePitch) {
        this.address = voiceAddress;
        this.size = voiceSize;
        this.pitch = voicePitch;
    }
    
}
