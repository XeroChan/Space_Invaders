package Space_Invaders;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class Endgame extends JPanel {
    private static final String RANKING_FILE = "ranking.txt";

    static class RankingEntry {
        String name;
        int score;

        RankingEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    JPanel endgamePanel, score;

    Endgame(int points, boolean hasWon) throws IOException {
        endgamePanel = new JPanel();
        endgamePanel.setLayout(new BorderLayout());

        String playerName = JOptionPane.showInputDialog(
                null,
                "Enter your name:",
                "Game Over",
                JOptionPane.PLAIN_MESSAGE
        );

        if (playerName == null || playerName.isBlank()) {
            playerName = "Player";
        }

        List<RankingEntry> ranking = loadRanking();
        ranking.add(new RankingEntry(playerName, points));

        ranking.sort(Comparator.comparingInt((RankingEntry e) -> e.score).reversed());

        saveRanking(ranking);

        score = new JPanel();
        score.setBackground(Color.decode("#4E458C"));
        score.setLayout(new BoxLayout(score, BoxLayout.Y_AXIS));
        endgamePanel.add(score, BorderLayout.NORTH);

        JLabel resultLabel = new JLabel(hasWon ? "You win!" : "You lose!");
        resultLabel.setForeground(Color.WHITE);
        score.add(resultLabel);

        JLabel yourScore = new JLabel("Your score: " + points);
        yourScore.setForeground(Color.WHITE);
        score.add(yourScore);

        JPanel rankingPanel = new JPanel();
        rankingPanel.setBackground(Color.decode("#021226"));
        rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Ranking");
        title.setForeground(Color.WHITE);
        rankingPanel.add(title);

        for (int i = 0; i < Math.min(10, ranking.size()); i++) {
            RankingEntry entry = ranking.get(i);

            JLabel row = new JLabel((i + 1) + ". " + entry.name + " - " + entry.score);
            row.setForeground(Color.WHITE);
            rankingPanel.add(row);
        }

        endgamePanel.add(rankingPanel, BorderLayout.CENTER);
    }

    private List<RankingEntry> loadRanking() throws IOException {
        List<RankingEntry> ranking = new ArrayList<>();
        File file = new File(RANKING_FILE);

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Could not create ranking file: " + file.getAbsolutePath());
            }
            return ranking;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    ranking.add(new RankingEntry(name, score));
                }
            }
        }

        return ranking;
    }

    private void saveRanking(List<RankingEntry> ranking) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RANKING_FILE))) {
            for (RankingEntry entry : ranking) {
                writer.write(entry.name + ";" + entry.score);
                writer.newLine();
            }
        }
    }
}
