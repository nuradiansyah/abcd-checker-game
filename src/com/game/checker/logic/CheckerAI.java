package com.game.checker.logic;

import java.util.List;
import java.util.Random;

public class CheckerAI {
    private Random random;
    
    public CheckerAI() {
        this.random = new Random();
    }
    
    public CheckerMove getBestMove(CheckerGameEngine engine, CheckerColor aiColor) {
        List<CheckerMove> validMoves = engine.getAllValidMoves(aiColor);
        
        if (validMoves.isEmpty()) {
            return null;
        }
        
        // Beginner AI: Prioritize captures, otherwise random move
        List<CheckerMove> captureMoves = validMoves.stream()
            .filter(CheckerMove::isCapture)
            .collect(java.util.stream.Collectors.toList());
        
        if (!captureMoves.isEmpty()) {
            return captureMoves.get(random.nextInt(captureMoves.size()));
        }
        
        return validMoves.get(random.nextInt(validMoves.size()));
    }
}
