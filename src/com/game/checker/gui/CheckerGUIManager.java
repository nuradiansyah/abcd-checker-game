package com.game.checker.gui;

import com.game.checker.logic.*;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class CheckerGUIManager extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private String gameMode;
    private String aiLevel;
    private String player1Name;
    private String player2Name;
    private CheckerColor playerColor;
    private Instant startTime;
    
    private transient CheckerGameEngine engine;
    private CheckerBoardPanel boardPanel;
    private CapturedPiecesPanel capturedPiecesPanel;
    private boolean gameOverHandled = false;
    
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);
    private static final Color BUTTON_COLOR = new Color(41, 128, 185);
    private static final Color BUTTON_HOVER = new Color(52, 152, 219);
    
    
    public CheckerGUIManager() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        SwingUtilities.invokeLater(() -> {
            setTitle("Checker Game");
            setSize(1050, 850);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            
            mainPanel.add(createModeSelectionPanel(), "MODE_SELECTION");
            
            add(mainPanel);
            setVisible(true);
        });
    }
    
    private void showPlayerNamePanel() {
        // Remove any existing PLAYER_NAME panel and rebuild it for the current game mode
        for (Component comp : mainPanel.getComponents()) {
            if ("PLAYER_NAME".equals(comp.getName())) {
                mainPanel.remove(comp);
                break;
            }
        }
        JPanel playerNamePanel = createPlayerNamePanel();
        playerNamePanel.setName("PLAYER_NAME");
        mainPanel.add(playerNamePanel, "PLAYER_NAME");
        cardLayout.show(mainPanel, "PLAYER_NAME");
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private JPanel createModeSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("CHECKER GAME");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Select Game Mode");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        RoundedButton twoPlayerButton = new RoundedButton("Two Players", BUTTON_COLOR, BUTTON_HOVER);
        twoPlayerButton.setPreferredSize(new Dimension(300, 60));
        twoPlayerButton.setMaximumSize(new Dimension(300, 60));
        twoPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        twoPlayerButton.addActionListener(e -> {
            gameMode = "TWO_PLAYER";
            showPlayerNamePanel();
        });
        
        RoundedButton computerButton = new RoundedButton("vs Computer (Beginner)", BUTTON_COLOR, BUTTON_HOVER);
        computerButton.setPreferredSize(new Dimension(300, 60));
        computerButton.setMaximumSize(new Dimension(300, 60));
        computerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        computerButton.addActionListener(e -> {
            gameMode = "COMPUTER";
            aiLevel = "BEGINNER";
            showPlayerNamePanel();
        });
        
        RoundedButton computerIntermediateButton = new RoundedButton("vs Computer (Intermediate)", new Color(230, 126, 34), new Color(211, 84, 0));
        computerIntermediateButton.setPreferredSize(new Dimension(300, 60));
        computerIntermediateButton.setMaximumSize(new Dimension(300, 60));
        computerIntermediateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        computerIntermediateButton.addActionListener(e -> {
            gameMode = "COMPUTER_INTERMEDIATE";
            aiLevel = "INTERMEDIATE";
            showPlayerNamePanel();
        });
        
        RoundedButton leaderboardButton = new RoundedButton("View Leaderboard", new Color(46, 204, 113), new Color(39, 174, 96));
        leaderboardButton.setPreferredSize(new Dimension(300, 60));
        leaderboardButton.setMaximumSize(new Dimension(300, 60));
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.addActionListener(e -> showLeaderboard());
        
        RoundedButton scoringInfoButton = new RoundedButton("📊 How to Score", new Color(155, 89, 182), new Color(142, 68, 173));
        scoringInfoButton.setPreferredSize(new Dimension(300, 60));
        scoringInfoButton.setMaximumSize(new Dimension(300, 60));
        scoringInfoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoringInfoButton.addActionListener(e -> showScoringInfoDialog());
        
        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(subtitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 50)));
        panel.add(twoPlayerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(computerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(computerIntermediateButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(leaderboardButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(scoringInfoButton);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private boolean isComputerMode() {
        return "COMPUTER".equals(gameMode) || "COMPUTER_INTERMEDIATE".equals(gameMode);
    }
    
    private JPanel createPlayerNamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("Enter Player Names");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel player1Label = new JLabel("Player 1 Name:");
        player1Label.setFont(new Font("Arial", Font.PLAIN, 18));
        player1Label.setForeground(Color.WHITE);
        player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField player1Field = new JTextField(20);
        player1Field.setMaximumSize(new Dimension(300, 40));
        player1Field.setFont(new Font("Arial", Font.PLAIN, 16));
        player1Field.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel player2Label = new JLabel("Player 2 Name:");
        player2Label.setFont(new Font("Arial", Font.PLAIN, 18));
        player2Label.setForeground(Color.WHITE);
        player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField player2Field = new JTextField(20);
        player2Field.setMaximumSize(new Dimension(300, 40));
        player2Field.setFont(new Font("Arial", Font.PLAIN, 16));
        player2Field.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel colorLabel = new JLabel("Choose Your Color:");
        colorLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        colorLabel.setForeground(Color.WHITE);
        colorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(PRIMARY_COLOR);
        colorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        RoundedButton redButton = new RoundedButton("Play as Red", new Color(231, 76, 60), new Color(192, 57, 43));
        redButton.setPreferredSize(new Dimension(150, 50));
        redButton.addActionListener(e -> {
            player1Name = player1Field.getText().trim();
            if (isComputerMode()) {
                player2Name = "Computer";
                playerColor = CheckerColor.RED;
            } else {
                player2Name = player2Field.getText().trim();
            }
            
            if (player1Name.isEmpty() || (!isComputerMode() && player2Name.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Please enter all player names!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isComputerMode()) {
                playerColor = CheckerColor.RED;
            }
            startGame();
        });
        
        RoundedButton blackButton = new RoundedButton("Play as Black", new Color(30, 30, 30), new Color(50, 50, 50));
        blackButton.setPreferredSize(new Dimension(150, 50));
        blackButton.addActionListener(e -> {
            player1Name = player1Field.getText().trim();
            if (isComputerMode()) {
                player2Name = "Computer";
                playerColor = CheckerColor.BLACK;
            } else {
                player2Name = player2Field.getText().trim();
            }
            
            if (player1Name.isEmpty() || (!isComputerMode() && player2Name.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Please enter all player names!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isComputerMode()) {
                playerColor = CheckerColor.BLACK;
            }
            startGame();
        });
        
        colorPanel.add(redButton);
        colorPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        colorPanel.add(blackButton);
        
        RoundedButton backButton = new RoundedButton("Back", new Color(149, 165, 166), new Color(127, 140, 141));
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.setMaximumSize(new Dimension(200, 50));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MODE_SELECTION"));
        
        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(player1Label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(player1Field);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        if (gameMode == null || gameMode.equals("TWO_PLAYER")) {
            panel.add(player2Label);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(player2Field);
            panel.add(Box.createRigidArea(new Dimension(0, 30)));
        }
        
        panel.add(colorLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(colorPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(backButton);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void startGame() {
        startTime = Instant.now();
        engine = new CheckerGameEngine();
        gameOverHandled = false;
        
        mainPanel.removeAll();
        mainPanel.add(createModeSelectionPanel(), "MODE_SELECTION");
        mainPanel.add(createGamePanel(), "GAME");
        
        cardLayout.show(mainPanel, "GAME");
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(PRIMARY_COLOR);
        
        JLabel infoLabel = new JLabel(String.format("%s (Red) vs %s (Black)", player1Name, player2Name));
        infoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        infoLabel.setForeground(Color.WHITE);
        
        RoundedButton quitButton = new RoundedButton("Quit Game", new Color(231, 76, 60), new Color(192, 57, 43));
        quitButton.setPreferredSize(new Dimension(150, 40));
        quitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit Game", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                mainPanel.removeAll();
                mainPanel.add(createModeSelectionPanel(), "MODE_SELECTION");
                cardLayout.show(mainPanel, "MODE_SELECTION");
            }
        });
        
        topPanel.add(infoLabel);
        topPanel.add(quitButton);
        
        boolean computerMode = isComputerMode();
        CheckerColor computerColor = computerMode ? playerColor.opposite() : null;
        
        capturedPiecesPanel = new CapturedPiecesPanel(engine);
        
        boardPanel = new CheckerBoardPanel(engine, computerMode, computerColor, capturedPiecesPanel, aiLevel != null ? aiLevel : "BEGINNER") {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                checkGameOverInGUI();
            }
        };
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(PRIMARY_COLOR);
        centerPanel.add(boardPanel);
        
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(PRIMARY_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        rightPanel.add(capturedPiecesPanel);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void checkGameOverInGUI() {
        if (gameOverHandled || !engine.isGameOver()) {
            return;
        }
        gameOverHandled = true;
        SwingUtilities.invokeLater(() -> {
                CheckerColor winner = engine.getWinner();
                String winnerName = "";
                
                if (!isComputerMode()) {
                    winnerName = (winner == CheckerColor.RED) ? player1Name : player2Name;
                } else {
                    if ((winner == CheckerColor.RED && playerColor == CheckerColor.RED) ||
                        (winner == CheckerColor.BLACK && playerColor == CheckerColor.BLACK)) {
                        winnerName = player1Name;
                    } else {
                        winnerName = "Computer";
                    }
                }
                
                Duration duration = Duration.between(startTime, Instant.now());
                int timeBonus = Math.max(0, 300 - (int)(duration.getSeconds() / 2));
                int piecesLeft = engine.countPieces(winner);
                int pieceBonus = piecesLeft * 50;
                int totalScore = 500 + pieceBonus + timeBonus;
                
                String message = String.format(
                    "%s wins!\n\nScore Breakdown:\n" +
                    "Base Score: 500\n" +
                    "Pieces Remaining: %d × 50 = %d\n" +
                    "Time Bonus: %d\n" +
                    "Total Score: %d",
                    winnerName, piecesLeft, pieceBonus, timeBonus, totalScore
                );
                
                JOptionPane.showMessageDialog(this, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
                
                if (!winnerName.equals("Computer")) {
                    String modeLabel;
                    if (!isComputerMode()) {
                        modeLabel = "Two Players";
                    } else if ("INTERMEDIATE".equals(aiLevel)) {
                        modeLabel = "vs Computer (Intermediate)";
                    } else {
                        modeLabel = "vs Computer (Beginner)";
                    }
                    LeaderboardEntry entry = new LeaderboardEntry(winnerName, totalScore, modeLabel);
                    LeaderboardManager.addScore(entry);
                }
                
                int choice = JOptionPane.showConfirmDialog(this, "Would you like to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    mainPanel.removeAll();
                    mainPanel.add(createModeSelectionPanel(), "MODE_SELECTION");
                    cardLayout.show(mainPanel, "MODE_SELECTION");
                } else {
                    mainPanel.removeAll();
                    mainPanel.add(createModeSelectionPanel(), "MODE_SELECTION");
                    cardLayout.show(mainPanel, "MODE_SELECTION");
                }
            });
    }
    
    private void showLeaderboard() {
        java.util.List<LeaderboardEntry> entries = LeaderboardManager.loadLeaderboard();
        
        if (entries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No scores yet. Play some games!", "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder("<html><body style='width: 500px; font-family: Arial;'>");
        sb.append("<h2 style='text-align: center; color: #2c3e50;'>🏆 TOP 10 LEADERBOARD 🏆</h2>");
        sb.append("<table style='width: 100%; border-collapse: collapse;'>");
        sb.append("<tr style='background-color: #34495e; color: white;'>");
        sb.append("<th style='padding: 10px; text-align: left;'>Rank</th>");
        sb.append("<th style='padding: 10px; text-align: left;'>Player</th>");
        sb.append("<th style='padding: 10px; text-align: center;'>Score</th>");
        sb.append("<th style='padding: 10px; text-align: center;'>Mode</th>");
        sb.append("</tr>");
        
        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry entry = entries.get(i);
            String bgColor = (i % 2 == 0) ? "#ecf0f1" : "#ffffff";
            sb.append(String.format("<tr style='background-color: %s;'>", bgColor));
            sb.append(String.format("<td style='padding: 8px;'>#%d</td>", i + 1));
            sb.append(String.format("<td style='padding: 8px;'>%s</td>", entry.getPlayerName()));
            sb.append(String.format("<td style='padding: 8px; text-align: center;'><b>%d</b></td>", entry.getScore()));
            sb.append(String.format("<td style='padding: 8px; text-align: center;'>%s</td>", entry.getMode()));
            sb.append("</tr>");
        }
        
        sb.append("</table></body></html>");
        
        JOptionPane.showMessageDialog(this, sb.toString(), "Leaderboard", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void showScoringInfoDialog() {
        String message =
            "📊 HOW SCORING WORKS 📊\n\n" +
            "Your final score is calculated based on:\n\n" +
            "🎯 BASE SCORE (Win Only):\n" +
            "   • Winning a game = 500 base points\n" +
            "   • Only the winner receives a score\n\n" +
            "♟️ PIECES REMAINING BONUS:\n" +
            "   • Each remaining piece = 50 points\n" +
            "   • Maximum 12 pieces × 50 = 600 points\n" +
            "   • Dominate the board for more points!\n\n" +
            "⚡ TIME BONUS:\n" +
            "   • Faster wins = Higher bonus!\n" +
            "   • Maximum 300 bonus points\n" +
            "   • Formula: 300 - (seconds ÷ 2)\n" +
            "   • Win in 2 min = +240 bonus\n" +
            "   • Win in 5 min = +150 bonus\n" +
            "   • After 10 min, time bonus = 0\n\n" +
            "💡 EXAMPLE:\n" +
            "   • Win with 8 pieces left in 4 minutes\n" +
            "   • Base: 500 points\n" +
            "   • Pieces: 8 × 50 = 400 points\n" +
            "   • Time: 300 - (240 ÷ 2) = 180 points\n" +
            "   • Final Score = 1,080 points\n\n" +
            "🏆 TIPS TO MAXIMIZE YOUR SCORE:\n" +
            "   • Capture opponent pieces while keeping yours\n" +
            "   • Win quickly for the time bonus\n" +
            "   • Use kings strategically for efficient captures\n\n" +
            "📝 NOTE:\n" +
            "   • Computer wins are not saved to the leaderboard\n" +
            "   • Only the TOP 10 scores are kept!";
        
        JOptionPane.showMessageDialog(
            this,
            message,
            "How to Score",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckerGUIManager());
    }
}
