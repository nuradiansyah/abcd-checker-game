package com.game.checker.gui;

import com.game.checker.logic.*;
import com.game.checker.resources.CheckerImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CheckerBoardPanel extends JPanel {
    private CheckerGameEngine engine;
    private JButton[][] boardButtons;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<CheckerMove> currentValidMoves;
    private CheckerAI ai;
    private boolean isComputerMode;
    private CheckerColor computerColor;
    
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final Color SELECTED_COLOR = new Color(255, 255, 0, 150);
    private static final Color VALID_MOVE_COLOR = new Color(0, 255, 0, 100);
    private static final Color CAPTURE_MOVE_COLOR = new Color(255, 0, 0, 100);
    
    public CheckerBoardPanel(CheckerGameEngine engine, boolean isComputerMode, CheckerColor computerColor) {
        this.engine = engine;
        this.isComputerMode = isComputerMode;
        this.computerColor = computerColor;
        this.ai = new CheckerAI();
        
        setLayout(new GridLayout(8, 8));
        boardButtons = new JButton[8][8];
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
                engine.executeMove(selectedMove);
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
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        CheckerMove computerMove = ai.getBestMove(engine, computerColor);
        if (computerMove != null) {
            engine.executeMove(computerMove);
            engine.switchPlayer();
            updateBoard();
            checkGameOver();
        }
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
    }
    
    private void checkGameOver() {
        if (engine.isGameOver()) {
            CheckerColor winner = engine.getWinner();
            String message = winner + " wins!";
            JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
