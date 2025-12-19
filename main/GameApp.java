package main;

import javax.swing.SwingUtilities;

public class GameApp {
    public static void main(String[] args) {
        // --- 1. JALANKAN MUSIK DI AWAL ---
        // Ganti "sound4.wav" dengan nama file lagu utama Anda
        SoundManager.playBackgroundMusic("sound4.wav");

        // --- 2. JALANKAN UI ---
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}