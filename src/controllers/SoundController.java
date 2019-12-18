package controllers;

import java.util.HashMap;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundController {

    private HashMap<String, String> mediaMap = new HashMap<String, String>();

    public void addMedia(String name, String mediaURL) {
        mediaMap.put(name, String.valueOf(getClass().getResource(mediaURL)));
    }

    public void play(String mediaName) {
        new AudioClip(mediaMap.getOrDefault(mediaName, null)).play();
    }
}
