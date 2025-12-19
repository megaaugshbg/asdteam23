package main;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    public static void playSound(String fileName) {
        try {
            URL url = SoundManager.class.getResource("/Asset/" + fileName);

            if (url == null) {
                System.err.println("Gagal menemukan file: /Asset/" + fileName);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

        } catch (Exception e) {
            System.err.println("Error saat memutar suara: " + e.getMessage());
            e.printStackTrace();
        }
    }
}