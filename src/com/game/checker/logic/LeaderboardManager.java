package com.game.checker.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardManager {
    private static final String LEADERBOARD_FILE = "checker_leaderboard.dat";
    private static final int MAX_ENTRIES = 10;
    
    public static void addScore(LeaderboardEntry entry) {
        List<LeaderboardEntry> leaderboard = loadLeaderboard();
        leaderboard.add(entry);
        Collections.sort(leaderboard);
        
        if (leaderboard.size() > MAX_ENTRIES) {
            leaderboard = leaderboard.subList(0, MAX_ENTRIES);
        }
        
        saveLeaderboard(leaderboard);
    }
    
    @SuppressWarnings("unchecked")
    public static List<LeaderboardEntry> loadLeaderboard() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LEADERBOARD_FILE))) {
            return (List<LeaderboardEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private static void saveLeaderboard(List<LeaderboardEntry> leaderboard) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LEADERBOARD_FILE))) {
            oos.writeObject(leaderboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
