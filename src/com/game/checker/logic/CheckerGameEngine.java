package com.game.checker.logic;

import java.util.ArrayList;
import java.util.List;

public class CheckerGameEngine {
    private CheckerBoard board;
    private CheckerColor currentPlayer;
    
    public CheckerGameEngine() {
        board = new CheckerBoard();
        currentPlayer = CheckerColor.RED;
    }
    
    public CheckerBoard getBoard() {
        return board;
    }
    
    public CheckerColor getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void switchPlayer() {
        currentPlayer = currentPlayer.opposite();
    }
    
    public List<CheckerMove> getAllValidMoves(CheckerColor color) {
        List<CheckerMove> captures = new ArrayList<>();
        List<CheckerMove> normalMoves = new ArrayList<>();
        
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                CheckerPiece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() == color) {
                    List<CheckerMove> pieceMoves = getValidMovesForPiece(row, col);
                    for (CheckerMove move : pieceMoves) {
                        if (move.isCapture()) {
                            captures.add(move);
                        } else {
                            normalMoves.add(move);
                        }
                    }
                }
            }
        }
        
        return captures.isEmpty() ? normalMoves : captures;
    }
    
    public List<CheckerMove> getValidMovesForPiece(int row, int col) {
        List<CheckerMove> moves = new ArrayList<>();
        CheckerPiece piece = board.getPiece(row, col);
        
        if (piece == null) return moves;
        
        List<CheckerMove> captureMoves = getCaptureMoves(row, col, piece);
        if (!captureMoves.isEmpty()) {
            return captureMoves;
        }
        
        int[] directions = piece.isKing() ? 
            new int[]{-1, 1} : 
            (piece.getColor() == CheckerColor.RED ? new int[]{-1} : new int[]{1});
        
        for (int rowDir : directions) {
            for (int colDir : new int[]{-1, 1}) {
                if (piece.isKing()) {
                    // Flying king: slide multiple squares along the diagonal
                    int newRow = row + rowDir;
                    int newCol = col + colDir;
                    while (board.isValidPosition(newRow, newCol) && board.isEmpty(newRow, newCol)) {
                        moves.add(new CheckerMove(row, col, newRow, newCol));
                        newRow += rowDir;
                        newCol += colDir;
                    }
                } else {
                    int newRow = row + rowDir;
                    int newCol = col + colDir;
                    if (board.isValidPosition(newRow, newCol) && board.isEmpty(newRow, newCol)) {
                        moves.add(new CheckerMove(row, col, newRow, newCol));
                    }
                }
            }
        }
        
        return moves;
    }
    
    private List<CheckerMove> getCaptureMoves(int row, int col, CheckerPiece piece) {
        List<CheckerMove> captures = new ArrayList<>();
        
        int[] directions = piece.isKing() ? 
            new int[]{-1, 1} : 
            (piece.getColor() == CheckerColor.RED ? new int[]{-1} : new int[]{1});
        
        for (int rowDir : directions) {
            for (int colDir : new int[]{-1, 1}) {
                if (piece.isKing()) {
                    // Flying king capture: travel along diagonal, find an enemy piece,
                    // then land on any empty square beyond it
                    int scanRow = row + rowDir;
                    int scanCol = col + colDir;
                    
                    // Slide along the diagonal until we hit something
                    while (board.isValidPosition(scanRow, scanCol) && board.isEmpty(scanRow, scanCol)) {
                        scanRow += rowDir;
                        scanCol += colDir;
                    }
                    
                    // Check if we found an enemy piece
                    if (board.isValidPosition(scanRow, scanCol)) {
                        CheckerPiece jumpedPiece = board.getPiece(scanRow, scanCol);
                        if (jumpedPiece != null && jumpedPiece.getColor() != piece.getColor()) {
                            int jumpRow = scanRow;
                            int jumpCol = scanCol;
                            // Land on any empty square beyond the captured piece
                            int landRow = jumpRow + rowDir;
                            int landCol = jumpCol + colDir;
                            while (board.isValidPosition(landRow, landCol) && board.isEmpty(landRow, landCol)) {
                                CheckerMove move = new CheckerMove(row, col, landRow, landCol, jumpRow, jumpCol);
                                captures.add(move);
                                landRow += rowDir;
                                landCol += colDir;
                            }
                        }
                    }
                } else {
                    // Regular piece: can only jump one square over an adjacent enemy
                    int jumpRow = row + rowDir;
                    int jumpCol = col + colDir;
                    int landRow = row + (2 * rowDir);
                    int landCol = col + (2 * colDir);
                    
                    if (board.isValidPosition(landRow, landCol)) {
                        CheckerPiece jumpedPiece = board.getPiece(jumpRow, jumpCol);
                        
                        if (jumpedPiece != null && 
                            jumpedPiece.getColor() != piece.getColor() && 
                            board.isEmpty(landRow, landCol)) {
                            
                            CheckerMove move = new CheckerMove(row, col, landRow, landCol, jumpRow, jumpCol);
                            captures.add(move);
                        }
                    }
                }
            }
        }
        
        return captures;
    }
    
    public boolean executeMove(CheckerMove move) {
        CheckerPiece piece = board.getPiece(move.getFromRow(), move.getFromCol());
        if (piece == null) return false;
        
        board.setPiece(move.getToRow(), move.getToCol(), piece);
        board.removePiece(move.getFromRow(), move.getFromCol());
        
        for (int[] captured : move.getCapturedPositions()) {
            board.capturePiece(captured[0], captured[1]);
        }
        
        if (shouldPromoteToKing(piece, move.getToRow())) {
            piece.makeKing();
        }
        
        return true;
    }
    
    private boolean shouldPromoteToKing(CheckerPiece piece, int row) {
        if (piece.isKing()) return false;
        
        if (piece.getColor() == CheckerColor.RED && row == 0) {
            return true;
        }
        if (piece.getColor() == CheckerColor.BLACK && row == 7) {
            return true;
        }
        return false;
    }
    
    public boolean isGameOver() {
        return getAllValidMoves(currentPlayer).isEmpty();
    }
    
    public CheckerColor getWinner() {
        if (isGameOver()) {
            return currentPlayer.opposite();
        }
        return null;
    }
    
    public int countPieces(CheckerColor color) {
        int count = 0;
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                CheckerPiece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() == color) {
                    count++;
                }
            }
        }
        return count;
    }
}
