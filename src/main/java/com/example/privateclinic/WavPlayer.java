package com.example.privateclinic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class WavPlayer {
    public static void playSound(String gender_id) {
        String url ="src/main/resources/com/example/privateclinic/Sounds/"+gender_id;
        File file = new File(url);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            // Đợi cho clip phát hết
            while (!clip.isRunning()) {
                Thread.sleep(1000);
            }
            while (clip.isRunning()) {
                Thread.sleep(1000);
            }

            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
