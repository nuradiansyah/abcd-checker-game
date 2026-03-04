package com.game.checker.logic;

import java.util.ArrayList;
import java.util.List;

public class CheckerBoard {
    private CheckerPiece[][] board;
    private static final int SIZE = 8;
    private List<CheckerPiece> capturedRedPieces;
    private List<CheckerPiece> capturedBlackPieces;
    
    public CheckerBoard() {
        board = new CheckerPiece[SIZE][SIZE];
        capturedRedPieces = new ArrayList<>();
        capturedBlackPieces = new ArrayList<>();
        initializeBoard();
    }
    
    private void initializeBoard() {
        // Place BLACK pieces on rows 0-2
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    board[row][col] = new CheckerPiece(CheckerColor.BLACK);
                }
            }
        }
        
        // Place RED pieces on rows 5-7
        for (int row = 5; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    board[row][col] = new CheckerPiece(CheckerColor.RED);
                }
            }
        }
    }
    
    public CheckerPiece getPiece(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }
    
    public void setPiece(int row, int col, CheckerPiece piece) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }
    
    public void removePiece(int row, int col) {
        if (isValidPosition(row, col)) {
            board[row][col] = null;
        }
    }
    
    public void capturePiece(int row, int col) {
        if (isValidPosition(row, col) && board[row][col] != null) {
            CheckerPiece piece = board[row][col];
            if (piece.getColor() == CheckerColor.RED) {
                capturedRedPieces.add(piece);
            } else {
                capturedBlackPieces.add(piece);
            }
            board[row][col] = null;
        }
    }
    
    public List<CheckerPiece> getCapturedRedPieces() {
        return capturedRedPieces;
    }
    
    public List<CheckerPiece> getCapturedBlackPieces() {
        return capturedBlackPieces;
    }
    
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
    
    public boolean isEmpty(int row, int col) {
        return isValidPosition(row, col) && board[row][col] == null;
    }
    
    public int getSize() {
        return SIZE;
    }
    
    public CheckerBoard copy() {
        CheckerBoard newBoard = new CheckerBoard();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != null) {
                    CheckerPiece original = board[row][col];
                    CheckerPiece copy = new CheckerPiece(original.getColor());
                    if (original.isKing()) {
                        copy.makeKing();
                    }
                    newBoard.setPiece(row, col, copy);
                } else {
                    newBoard.setPiece(row, col, null);
                }
            }
        }
        return newBoard;
    }
}
