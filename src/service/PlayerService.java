package service;

import java.io.FileInputStream;
import java.util.List;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import javazoom.jl.player.advanced.PlaybackEvent;

public class PlayerService {
    private static Thread currentThread = null;
    private static AdvancedPlayer currentPlayer = null;
    private static boolean paused = false;
    private static int pausedFrame = 0;
    private static String currentSong = null;
    private static List<String> currentPlaylist = null;
    private static int currentIndex = -1;

    public static void playSong(String song) {
        stopCurrentSong(); // Stop any current playback
        currentSong = song;
        paused = false;
        currentThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream("data/songs/" + song)) {
                currentPlayer = new AdvancedPlayer(fis);
                currentPlayer.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        if (!paused && currentPlaylist != null && currentIndex < currentPlaylist.size() - 1) {
                            playNext();
                        }
                    }
                });
                currentPlayer.play();
            } catch (Exception e) {
                System.out.println("Error playing song: " + song + " -> " + e.getMessage());
            } finally {
                currentPlayer = null;
            }
        });
        currentThread.start();
    }

    public static void stopCurrentSong() {
        try {
            if (currentPlayer != null) {
                currentPlayer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentThread != null && currentThread.isAlive()) {
            currentThread.interrupt();
        }
        paused = false;
        pausedFrame = 0;
    }

    public static void pauseSong() {
        if (currentPlayer != null) {
            paused = true;
            stopCurrentSong();
            System.out.println("Paused at frame " + pausedFrame);
        }
    }

    public static void resumeSong() {
        if (paused && currentSong != null) {
            paused = false;
            playSong(currentSong);
        }
    }

    public static void playPlaylist(List<String> songs) {
        if (songs == null || songs.isEmpty()) return;
        currentPlaylist = songs;
        currentIndex = 0;
        playSong(songs.get(0));
    }

    public static void playNext() {
        if (currentPlaylist == null || currentIndex >= currentPlaylist.size() - 1) return;
        currentIndex++;
        playSong(currentPlaylist.get(currentIndex));
    }

    public static void playPrevious() {
        if (currentPlaylist == null || currentIndex <= 0) return;
        currentIndex--;
        playSong(currentPlaylist.get(currentIndex));
    }
}
