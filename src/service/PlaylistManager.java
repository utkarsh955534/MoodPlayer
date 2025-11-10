//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistManager {
    private static final Map<String, List<String>> playlists = new HashMap();

    public PlaylistManager() {
    }

    public static List<String> getForMood(String mood) {
        return (List)playlists.getOrDefault(mood, new ArrayList());
    }

    static {
        playlists.put("Happy", Arrays.asList("happy_song1.mp3", "happy_song2.mp3"));
        playlists.put("Sad", Arrays.asList("sad_song1.mp3", "sad_song2.mp3"));
        playlists.put("Angry", Arrays.asList("angry_song1.mp3", "angry_song2.mp3"));
        playlists.put("Calm", Arrays.asList("calm_song1.mp3", "calm_song2.mp3"));
        playlists.put("Energetic", Arrays.asList("energetic_song1.mp3", "energetic_song2.mp3"));
    }
}
