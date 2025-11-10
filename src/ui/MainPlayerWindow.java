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
    private JList<String> playlistList;

    public MainPlayerWindow() {
        setTitle("Mood Music Player");
        setSize(800,600);
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

        // Double-click listener for playing selected song
        playlistList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(evt.getClickCount() == 1){
                    int index = playlistList.locationToIndex(evt.getPoint());
                    if(index >= 0){
                        String song = playlistList.getModel().getElementAt(index);
                        PlayerService.playSong(song);
                    }
                }
            }
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(playlistList), BorderLayout.CENTER);
    }

    private void onDetect() {
        String text = moodField.getText();
        String mood = MoodDetector.detect(text);
        List<String> songs = PlaylistManager.getForMood(mood);

        DefaultListModel<String> model = (DefaultListModel<String>)playlistList.getModel();
        model.clear();
        for(String song : songs){
            model.addElement(song);
        }

        // Play first song automatically
        if(!songs.isEmpty()){
            PlayerService.playSong(songs.get(0));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainPlayerWindow().setVisible(true));
    }
}
