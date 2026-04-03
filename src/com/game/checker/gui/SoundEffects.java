package com.game.checker.gui;

import javax.sound.sampled.*;

/**
 * Sound effects generator for the Checker Game.
 * Uses programmatic sound generation to create simple audio feedback.
 */
public class SoundEffects {
    
    private static final float SAMPLE_RATE = 44100f;
    
    /**
     * Play a sound effect in a separate thread to avoid blocking the UI.
     */
    private static void playSound(byte[] soundData) {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                
                if (!AudioSystem.isLineSupported(info)) {
                    return;
                }
                
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(format, soundData, 0, soundData.length);
                clip.start();
                
                // Wait for the clip to finish
                Thread.sleep(clip.getMicrosecondLength() / 1000);
                clip.close();
            } catch (Exception e) {
                // Silently ignore sound errors
            }
        }).start();
    }
    
    /**
     * Generate a simple tone.
     */
    private static byte[] generateTone(double frequency, int durationMs) {
        int numSamples = (int) ((durationMs / 1000.0) * SAMPLE_RATE);
        byte[] samples = new byte[numSamples];
        
        for (int i = 0; i < numSamples; i++) {
            double angle = 2.0 * Math.PI * i / (SAMPLE_RATE / frequency);
            samples[i] = (byte) (Math.sin(angle) * 127.0);
        }
        
        return samples;
    }
    
    /**
     * Generate a tone with envelope (fade in/out).
     */
    private static byte[] generateToneWithEnvelope(double frequency, int durationMs) {
        int numSamples = (int) ((durationMs / 1000.0) * SAMPLE_RATE);
        byte[] samples = new byte[numSamples];
        
        int attackSamples = numSamples / 10;
        int releaseSamples = numSamples / 5;
        
        for (int i = 0; i < numSamples; i++) {
            double angle = 2.0 * Math.PI * i / (SAMPLE_RATE / frequency);
            double envelope = 1.0;
            
            // Attack
            if (i < attackSamples) {
                envelope = (double) i / attackSamples;
            }
            // Release
            else if (i > numSamples - releaseSamples) {
                envelope = (double) (numSamples - i) / releaseSamples;
            }
            
            samples[i] = (byte) (Math.sin(angle) * 127.0 * envelope);
        }
        
        return samples;
    }
    
    /**
     * Play sound for a normal piece move.
     * DISABLED - Sound effects removed.
     */
    public static void playMoveSound() {
        // Sound disabled
    }
    
    /**
     * Play sound for capturing an opponent's piece.
     * DISABLED - Sound effects removed.
     */
    public static void playCaptureSound() {
        // Sound disabled
    }
    
    /**
     * Play sound when a piece becomes a king.
     * DISABLED - Sound effects removed.
     */
    public static void playKingPromotionSound() {
        // Sound disabled
    }
    
    /**
     * Play sound for winning the game.
     * DISABLED - Sound effects removed.
     */
    public static void playWinSound() {
        // Sound disabled
    }
    
    /**
     * Play sound for losing the game.
     * DISABLED - Sound effects removed.
     */
    public static void playLoseSound() {
        // Sound disabled
    }
}
