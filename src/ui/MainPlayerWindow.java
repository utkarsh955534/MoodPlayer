package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import service.MoodDetector;
import service.PlayerService;
import service.PlaylistManager;

public class MainPlayerWindow extends JFrame {
    private JTextField moodField;
    private JButton detectBtn;
    private JButton playBtn, pauseBtn, resumeBtn, stopBtn, nextBtn, prevBtn;
    private JList<String> playlistList;
    private List<String> currentSongs;

    public MainPlayerWindow() {
        setTitle("Mood Music Player");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel top = new JPanel(new FlowLayout());
        moodField = new JTextField(30);
        detectBtn = new JButton("Detect Mood & Play");
        detectBtn.addActionListener(e -> onDetect());
        top.add(new JLabel("Type your mood / sentence: "));
        top.add(moodField);
        top.add(detectBtn);

        playlistList = new JList<>(new DefaultListModel<>());
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        playlistList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    int index = playlistList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        String song = playlistList.getModel().getElementAt(index);
                        PlayerService.playSong(song);
                    }
                }
            }
        });

        // ---- CONTROL BUTTONS ----
        JPanel controls = new JPanel(new FlowLayout());
        prevBtn = new JButton("⏮ Prev");
        playBtn = new JButton("▶ Play");
        pauseBtn = new JButton("⏸ Pause");
        resumeBtn = new JButton("⏯ Resume");
        stopBtn = new JButton("⏹ Stop");
        nextBtn = new JButton("⏭ Next");

        controls.add(prevBtn);
        controls.add(playBtn);
        controls.add(pauseBtn);
        controls.add(resumeBtn);
        controls.add(stopBtn);
        controls.add(nextBtn);

        playBtn.addActionListener(e -> playSelected());
        pauseBtn.addActionListener(e -> PlayerService.pauseSong());
        resumeBtn.addActionListener(e -> PlayerService.resumeSong());
        stopBtn.addActionListener(e -> PlayerService.stopCurrentSong());
        nextBtn.addActionListener(e -> PlayerService.playNext());
        prevBtn.addActionListener(e -> PlayerService.playPrevious());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(playlistList), BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
    }

    private void onDetect() {
        String text = moodField.getText();
        String mood = MoodDetector.detect(text);
        currentSongs = PlaylistManager.getForMood(mood);

        DefaultListModel<String> model = (DefaultListModel<String>) playlistList.getModel();
        model.clear();
        for (String song : currentSongs) {
            model.addElement(song);
        }

        if (!currentSongs.isEmpty()) {
            playlistList.setSelectedIndex(0);
            PlayerService.playPlaylist(currentSongs);
        }
    }

    private void playSelected() {
        int index = playlistList.getSelectedIndex();
        if (index >= 0 && currentSongs != null) {
            PlayerService.playSong(currentSongs.get(index));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainPlayerWindow().setVisible(true));
    }
}
