package com.game.checker.logic;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LeaderboardEntry implements Serializable, Comparable<LeaderboardEntry> {
    private static final long serialVersionUID = 1L;
    
    private String playerName;
    private int score;
    private String date;
    private String mode;
    
    public LeaderboardEntry(String playerName, int score, String mode) {
        this.playerName = playerName;
        this.score = score;
        this.mode = mode;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public String getDate() { return date; }
    public String getMode() { return mode; }
    
    @Override
    public int compareTo(LeaderboardEntry other) {
        return Integer.compare(other.score, this.score);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %d points (%s) - %s", playerName, score, mode, date);
    }
}
