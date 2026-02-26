package com.game.checker.logic;

public enum CheckerColor {
    RED, BLACK;
    
    public CheckerColor opposite() {
        return this == RED ? BLACK : RED;
    }
    
    @Override
    public String toString() {
        return this == RED ? "Red" : "Black";
    }
}
