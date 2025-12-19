package main;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    // Simpan clip musik latar agar bisa dikontrol (looping/stop)
    private static Clip backgroundClip;

    /**
     * Memutar efek suara pendek (SFX) - Sekali putar
     */
    public static void playSound(String fileName) {
        try {
            URL url = SoundManager.class.getResource("/Asset/" + fileName);
            if (url == null) {
                System.err.println("Gagal menemukan file SFX: /Asset/" + fileName);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

        } catch (Exception e) {
            System.err.println("Error SFX: " + e.getMessage());
        }
    }

    /**
     * Memutar musik latar (BGM) - Berputar terus menerus (Loop)
     */
    public static void playBackgroundMusic(String fileName) {
        try {
            // Jika musik sedang berjalan, jangan jalankan lagi agar tidak tumpang tindih
            if (backgroundClip != null && backgroundClip.isRunning()) {
                return;
            }

            URL url = SoundManager.class.getResource("/Asset/" + fileName);
            if (url == null) {
                System.err.println("Gagal menemukan file BGM: /Asset/" + fileName);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);

            // Set agar lagu mengulang terus (LOOP)
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();

        } catch (Exception e) {
            System.err.println("Error BGM: " + e.getMessage());
        }
    }

    /**
     * Berhenti memutar musik latar jika diperlukan
     */
    public static void stopBackgroundMusic() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }
}