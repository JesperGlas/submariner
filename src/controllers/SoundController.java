package controllers;

import java.util.HashMap;

import javafx.scene.media.AudioClip;

public class SoundController {

    private HashMap<String, String> mediaMap = new HashMap<String, String>();

    public void addMedia(String name, String mediaURL) {
        mediaMap.put(name, String.valueOf(getClass().getResource(mediaURL)));
    }

    public void play(String mediaName) {
        new AudioClip(mediaMap.getOrDefault(mediaName, null)).play();
    }
}
