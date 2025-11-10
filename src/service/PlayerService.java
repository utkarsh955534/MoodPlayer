package service;

import java.io.FileInputStream;
import java.util.List;
import javazoom.jl.player.Player;

public class PlayerService {
    private static Thread currentThread = null;
    private static Player currentPlayer = null;

    public static void playSong(String song) {
        stopCurrentSong(); // stop previous song

        currentThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream("data/songs/" + song)) {
                currentPlayer = new Player(fis);
                currentPlayer.play(); // blocks until song finishes
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
                currentPlayer.close(); // this actually stops playback
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentThread != null && currentThread.isAlive()) {
            currentThread.interrupt();
        }
    }

    // Optional: sequential playlist playback
    public static void playPlaylist(List<String> songs) {
        if (songs == null || songs.isEmpty()) return;

        Thread playlistThread = new Thread(() -> {
            for (String song : songs) {
                if (Thread.currentThread().isInterrupted()) break;
                playSongAndWait(song);
            }
        });
        playlistThread.start();
    }

    private static void playSongAndWait(String song) {
        try (FileInputStream fis = new FileInputStream("data/songs/" + song)) {
            currentPlayer = new Player(fis);
            currentPlayer.play();
        } catch (Exception e) {
            System.out.println("Error playing song: " + song + " -> " + e.getMessage());
        } finally {
            currentPlayer = null;
        }
    }
}
