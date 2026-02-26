package com.game.checker.logic;

public class CheckerPiece {
    private CheckerColor color;
    private boolean isKing;
    
    public CheckerPiece(CheckerColor color) {
        this.color = color;
        this.isKing = false;
    }
    
    public CheckerColor getColor() {
        return color;
    }
    
    public boolean isKing() {
        return isKing;
    }
    
    public void makeKing() {
        this.isKing = true;
    }
    
    @Override
    public String toString() {
        String piece = color == CheckerColor.RED ? "R" : "B";
        return isKing ? piece + "K" : piece;
    }
}
