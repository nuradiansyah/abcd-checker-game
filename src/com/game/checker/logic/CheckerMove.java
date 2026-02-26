package com.game.checker.logic;

import java.util.ArrayList;
import java.util.List;

public class CheckerMove {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private boolean isCapture;
    private List<int[]> capturedPositions;
    
    public CheckerMove(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.isCapture = false;
        this.capturedPositions = new ArrayList<>();
    }
    
    public CheckerMove(int fromRow, int fromCol, int toRow, int toCol, int capturedRow, int capturedCol) {
        this(fromRow, fromCol, toRow, toCol);
        this.isCapture = true;
        this.capturedPositions.add(new int[]{capturedRow, capturedCol});
    }
    
    public int getFromRow() { return fromRow; }
    public int getFromCol() { return fromCol; }
    public int getToRow() { return toRow; }
    public int getToCol() { return toCol; }
    public boolean isCapture() { return isCapture; }
    public List<int[]> getCapturedPositions() { return capturedPositions; }
    
    public void addCapturedPosition(int row, int col) {
        capturedPositions.add(new int[]{row, col});
        isCapture = true;
    }
}
