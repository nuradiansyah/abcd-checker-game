package com.game.checker.gui;

import com.game.checker.logic.*;
import com.game.checker.resources.CheckerImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.Timer;

public class CheckerBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private transient CheckerGameEngine engine;
    private JButton[][] boardButtons;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private transient List<CheckerMove> currentValidMoves;
    private transient CheckerAI ai;
    private transient CheckerAIIntermediate intermediateAI;
    private boolean isComputerMode;
    private CheckerColor computerColor;
    private CapturedPiecesPanel capturedPiecesPanel;
    private boolean gameOverHandled = false;
    private String aiLevel;
    
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final Color SELECTED_COLOR = new Color(255, 255, 0, 150);
    private static final Color VALID_MOVE_COLOR = new Color(0, 255, 0, 100);
    private static final Color CAPTURE_MOVE_COLOR = new Color(255, 0, 0, 100);
    
    
    public CheckerBoardPanel(CheckerGameEngine engine, boolean isComputerMode, CheckerColor computerColor, CapturedPiecesPanel capturedPiecesPanel) {
        this(engine, isComputerMode, computerColor, capturedPiecesPanel, "BEGINNER");
    }
    
    public CheckerBoardPanel(CheckerGameEngine engine, boolean isComputerMode, CheckerColor computerColor, CapturedPiecesPanel capturedPiecesPanel, String aiLevel) {
        this.engine = engine;
        this.isComputerMode = isComputerMode;
        this.computerColor = computerColor;
        this.ai = new CheckerAI();
        this.intermediateAI = new CheckerAIIntermediate();
        this.boardButtons = new JButton[8][8];
        this.capturedPiecesPanel = capturedPiecesPanel;
        this.aiLevel = aiLevel;
        
        setLayout(new GridLayout(8, 8));
        initializeBoard();
        updateBoard();
        
        if (isComputerMode && engine.getCurrentPlayer() == computerColor) {
            SwingUtilities.invokeLater(this::makeComputerMove);
        }
    }
    
    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton() {
                    private static final long serialVersionUID = 1L;
                    
                    @Override
                    protected void paintComponent(Graphics g) {
                        g.setColor(getBackground());
                        g.fillRect(0, 0, getWidth(), getHeight());
                        super.paintComponent(g);
                    }
                };
                square.setPreferredSize(new Dimension(80, 80));
                square.setFocusPainted(false);
                square.setOpaque(true);
                square.setBorderPainted(false);
                square.setContentAreaFilled(false);
                
                if ((row + col) % 2 == 0) {
                    square.setBackground(LIGHT_SQUARE);
                } else {
                    square.setBackground(DARK_SQUARE);
                }
                
                final int r = row;
                final int c = col;
                
                square.addActionListener(e -> handleSquareClick(r, c));
                boardButtons[row][col] = square;
                add(square);
            }
        }
    }
    
    private void handleSquareClick(int row, int col) {
        if (isComputerMode && engine.getCurrentPlayer() == computerColor) {
            return;
        }
        
        CheckerPiece clickedPiece = engine.getBoard().getPiece(row, col);
        
        if (selectedRow == -1) {
            if (clickedPiece != null && clickedPiece.getColor() == engine.getCurrentPlayer()) {
                selectedRow = row;
                selectedCol = col;
                currentValidMoves = engine.getValidMovesForPiece(row, col);
                updateBoard();
            }
        } else {
            CheckerMove selectedMove = findMove(selectedRow, selectedCol, row, col);
            
            if (selectedMove != null) {
                // Check if piece will become king
                CheckerPiece movingPiece = engine.getBoard().getPiece(selectedRow, selectedCol);
                boolean willBecomeKing = false;
                if (movingPiece != null && !movingPiece.isKing()) {
                    int targetRow = selectedMove.getToRow();
                    if ((movingPiece.getColor() == CheckerColor.RED && targetRow == 7) ||
                        (movingPiece.getColor() == CheckerColor.BLACK && targetRow == 0)) {
                        willBecomeKing = true;
                    }
                }
                
                // Play appropriate sound
                if (selectedMove.isCapture()) {
                    SoundEffects.playCaptureSound();
                } else {
                    SoundEffects.playMoveSound();
                }
                
                engine.executeMove(selectedMove);
                
                // Play king promotion sound if applicable
                if (willBecomeKing) {
                    SoundEffects.playKingPromotionSound();
                }
                
                engine.switchPlayer();
                clearSelection();
                updateBoard();
                checkGameOver();
                
                if (isComputerMode && !engine.isGameOver()) {
                    SwingUtilities.invokeLater(this::makeComputerMove);
                }
            } else if (clickedPiece != null && clickedPiece.getColor() == engine.getCurrentPlayer()) {
                selectedRow = row;
                selectedCol = col;
                currentValidMoves = engine.getValidMovesForPiece(row, col);
                updateBoard();
            } else {
                clearSelection();
                updateBoard();
            }
        }
    }
    
    private void makeComputerMove() {
        Timer timer = new Timer(500, e -> {
            CheckerMove computerMove;
            if ("INTERMEDIATE".equals(aiLevel)) {
                computerMove = intermediateAI.getBestMove(engine, computerColor);
            } else {
                computerMove = ai.getBestMove(engine, computerColor);
            }
            if (computerMove != null) {
                // Check if piece will become king
                CheckerPiece movingPiece = engine.getBoard().getPiece(computerMove.getFromRow(), computerMove.getFromCol());
                boolean willBecomeKing = false;
                if (movingPiece != null && !movingPiece.isKing()) {
                    int targetRow = computerMove.getToRow();
                    if ((movingPiece.getColor() == CheckerColor.RED && targetRow == 7) ||
                        (movingPiece.getColor() == CheckerColor.BLACK && targetRow == 0)) {
                        willBecomeKing = true;
                    }
                }
                
                // Play appropriate sound
                if (computerMove.isCapture()) {
                    SoundEffects.playCaptureSound();
                } else {
                    SoundEffects.playMoveSound();
                }
                
                engine.executeMove(computerMove);
                
                // Play king promotion sound if applicable
                if (willBecomeKing) {
                    SoundEffects.playKingPromotionSound();
                }
                
                engine.switchPlayer();
                updateBoard();
                checkGameOver();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private CheckerMove findMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (currentValidMoves == null) return null;
        
        for (CheckerMove move : currentValidMoves) {
            if (move.getFromRow() == fromRow && move.getFromCol() == fromCol &&
                move.getToRow() == toRow && move.getToCol() == toCol) {
                return move;
            }
        }
        return null;
    }
    
    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        currentValidMoves = null;
    }
    
    public void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = boardButtons[row][col];
                CheckerPiece piece = engine.getBoard().getPiece(row, col);
                
                if ((row + col) % 2 == 0) {
                    square.setBackground(LIGHT_SQUARE);
                } else {
                    square.setBackground(DARK_SQUARE);
                }
                
                if (row == selectedRow && col == selectedCol) {
                    square.setBackground(SELECTED_COLOR);
                }
                
                if (currentValidMoves != null) {
                    for (CheckerMove move : currentValidMoves) {
                        if (move.getToRow() == row && move.getToCol() == col) {
                            if (move.isCapture()) {
                                square.setBackground(CAPTURE_MOVE_COLOR);
                            } else {
                                square.setBackground(VALID_MOVE_COLOR);
                            }
                        }
                    }
                }
                
                if (piece != null) {
                    String imageName = piece.getColor() == CheckerColor.RED ? "red-" : "black-";
                    imageName += piece.isKing() ? "king.png" : "piece.png";
                    square.setIcon(CheckerImageLoader.loadImage(imageName));
                } else {
                    square.setIcon(null);
                }
            }
        }
        
        if (capturedPiecesPanel != null) {
            capturedPiecesPanel.refreshCapturedPieces();
        }
    }
    
    private void checkGameOver() {
        if (gameOverHandled || !engine.isGameOver()) {
            return;
        }
        gameOverHandled = true;
    }
}
