/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.io.*;
import javax.sound.sampled.*;
import lib.voice.VoiceStrategy;
import org.tritonus.sampled.convert.PCM2PCMConversionProvider;
import org.tritonus.sampled.convert.SampleRateConversionProvider;


/**
 *
 * @author Gil Costa
 */
public class Voice {
    private static final int PITCH_MULTIPLIER = 0xB0; // TODO: this

    private final byte[] rawData;
    private final byte[] compressedData;
    private int pitch; // to use on playback    
    
    private Clip clip; // playable clip   
    
    // Constructor with raw and compressed data, and pitch for playback
    private Voice(byte[] rawData, byte[] compressedData, int pitch){
        this.rawData = rawData;
        this.compressedData = compressedData;
        this.pitch = pitch;
    }
    
    // Sharing constructor
    public Voice(Voice other, int pitch){
        this.rawData = other.rawData;
        this.compressedData = other.compressedData;
        this.pitch = pitch;
    }

    
    //--------------------------------------------------------------------------
    
    
    // Read from ROM
    protected static Voice read(RandomAccessFile rom, byte[] table, VoiceInfo info, VoiceStrategy compression) throws IOException {       
        // go to voice address
        rom.seek(info.address);
        // read compressed data from rom
        byte[] compressedData = new byte[info.size];
        int readBytes = rom.read(compressedData);
        if (readBytes != info.size) throw new IOException("Unable to read " + info.size + " bytes (total read: " + readBytes + " bytes)");        
        // decompress        
        byte[] rawData = compression.decompress(compressedData, table);
        // create and return audio
        return new Voice(rawData, compressedData, info.pitch);
    }        
    
    // Write to ROM
    protected void write(RandomAccessFile rom, long address) throws IOException {
        rom.seek(address);
        rom.write(compressedData);
    }
    
    
    //--------------------------------------------------------------------------
    
    
    // Import from WAV file
    public static Voice readFromWave(File file, byte[] table, int pitch, VoiceStrategy compression) throws Exception{
        int playPitch = pitch*PITCH_MULTIPLIER; // TODO:...
        // open stream
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        AudioFormat sourceFormat = ais.getFormat();
        AudioFormat targetFormat;
        
        // convert sample rate
        targetFormat = new AudioFormat(11025-playPitch, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), false, false);
        SampleRateConversionProvider prov = new SampleRateConversionProvider();
        if (prov.isConversionSupported(sourceFormat, targetFormat)){
            ais = prov.getAudioInputStream(targetFormat, ais);
        }else{
            System.out.println("sample rate convertion failed");
        }
        // convert channels and sample size
        sourceFormat = ais.getFormat();
        targetFormat = new AudioFormat(11025-playPitch, 8, 1, false, false);
        PCM2PCMConversionProvider prov2 = new PCM2PCMConversionProvider();
        if (prov2.isConversionSupported(sourceFormat, targetFormat)){
            ais = prov2.getAudioInputStream(targetFormat, ais);            
        }else{
            System.out.println("channels and sample size convertion failed");
        }     
        
        // open dataline
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, targetFormat);
        SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceLine.open(targetFormat);
        sourceLine.start();

        // read data into array
        ByteArrayOutputStream dos = new ByteArrayOutputStream();
        int nBytesRead = 0;
        byte[] abData = new byte[128000];
        while (nBytesRead != -1) {
            nBytesRead = ais.read(abData, 0, abData.length);            
            if (nBytesRead > 0) {
                for (int i = 0 ; i < nBytesRead ; ++i)
                    dos.write(abData[i]);
            }
        }
        sourceLine.drain();
        sourceLine.close();

        // construct Audio
        byte[] rawData = dos.toByteArray();
        // compress        
        byte[] compressedData = compression.compress(rawData, table);
        
        rawData = compression.decompress(compressedData, table);
        
        // create and return audio
        return new Voice(rawData, compressedData, pitch);
    }
    
    // Export as WAV file
    public void writeToWave(File outputFile)throws IOException{
        int playPitch = pitch*PITCH_MULTIPLIER; // TODO...
            
        // create the audio stream
        ByteArrayInputStream dis = new ByteArrayInputStream(rawData);
        AudioFormat format = new AudioFormat(11025 - playPitch, 8, 1, false, false);
        AudioInputStream ais = new AudioInputStream(dis, format, rawData.length);            

        // write to file
        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
        if (AudioSystem.isFileTypeSupported(fileType, ais)) {
            AudioSystem.write(ais, fileType, outputFile);
        }
    }
    
    
    public void writeToBinary(File file)throws IOException{
        FileOutputStream dos = new FileOutputStream(file);
        dos.write(rawData);
    }
    
        
    //--------------------------------------------------------------------------
    
    
    // Get the size of the compressed data, in bytes
    public int getSizeInBytes(){
        return compressedData.length;
    }
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    
    
    
    // Create clip if it's null/invalidated
    private void checkClip() throws LineUnavailableException, IOException{
        if (clip == null){
            //int playPitch = (int)((1-pitch/20.0)*8160-4080);
//            int playPitch = 0x286+0x26*pitch;
//            int playPitch = 2816;
            int playPitch = (int)((1-pitch/25.0)*4400-4400);
            
            // create the audio stream
            ByteArrayInputStream dis = new ByteArrayInputStream(rawData);
            AudioFormat format = new AudioFormat(11025 + playPitch, 8, 1, false, false);
            AudioInputStream ais = new AudioInputStream(dis, format, rawData.length);                                    
           
            // open the clip
            DataLine.Info info = new DataLine.Info(Clip.class, format);            
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);         
        }
    }         
    
    // Stop and clear clip
    private void invalidateClip(){
        if (clip != null){
            clip.stop();
            clip = null;
        }
    }  
    
    // Change the pitch of the clip
    public void setPitch(int pitch){
        invalidateClip();
        this.pitch = pitch;
    }
    
    // Play the clip
    public void play() throws LineUnavailableException, IOException{
        checkClip();
        // rewind and play clip
        clip.setFramePosition(0);
        clip.start();
    }  
    
    // Get the duration of the clip, in miliseconds
    public long getDurationInMiliseconds() throws LineUnavailableException, IOException{
        checkClip();
        return clip.getMicrosecondLength()/1000;
    }

    // Get the pitch of the voice
    public int getPitch() {
        return pitch;
    }   
            
    
}
