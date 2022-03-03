package com.game.engine.audio;

import java.io.BufferedInputStream;
import java.io.IOException;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip {

    private Clip clip = null;
    private FloatControl gainControl;

    public SoundClip(String path) {
        try {                  //ais stands for audio input stream. 
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(SoundClip.class.getResourceAsStream(path)));
            AudioFormat baseFormat = ais.getFormat();

            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

            AudioInputStream decodedais = AudioSystem.getAudioInputStream(decodeFormat, ais);

            clip = AudioSystem.getClip();
            //Decoded audio input stream
            clip.open(decodedais);
            //I can control the volume through the gain. It is adjusted in decibels, meaning that i can only change the volume relative to the
            //current volume.
            gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // e.printStackTrace();
        }

    }

    //Some methods that just help with making java sound effects easier
    public void play(){
        if(clip == null){
            return;
        }
        stop();
        clip.setFramePosition(0);
        while(!clip.isRunning()){
            clip.start();
        }
    }
    //searches to a location in microseconds in the song
    public void seek(long microseconds){
        if(clip == null){
            return;
        }
        stop();
        clip.setMicrosecondPosition(microseconds);
        while(!clip.isRunning()){
            clip.start();
        }
    }
//stops it
    public void stop(){
        if(clip.isRunning()){
            clip.stop();
        }
    }

    public void close(){
        stop();
        clip.drain();
        clip.close();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        play();
    }

    //the value is an volume ADJUSTMENT in decibels. Not a volume percentage or value.
    public void setVolume(float value){
        gainControl.setValue(value);
    }

    public boolean isRunning(){
        return clip.isRunning();
    }

    

}
