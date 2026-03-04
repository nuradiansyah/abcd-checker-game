package com.game.checker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.game.checker.logic.CheckerGameEngine;
import com.game.checker.logic.CheckerPiece;
import com.game.checker.resources.CheckerImageLoader;

public class CapturedPiecesPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final CheckerGameEngine engine;
    private final JPanel redCapturedPanel;
    private final JPanel blackCapturedPanel;

    public CapturedPiecesPanel(CheckerGameEngine engine) {
        this.engine = engine;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 245, 250));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 120), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Title
        JLabel titleLabel = new JLabel("Captured Pieces");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 80));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // Red pieces captured (by opponent)
        JPanel redLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        redLabelPanel.setBackground(new Color(245, 245, 250));
        redLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel redLabel = new JLabel("\uD83D\uDD34 Red Pieces:");
        redLabel.setFont(new Font("Arial", Font.BOLD, 14));
        redLabel.setForeground(new Color(60, 60, 60));
        redLabelPanel.add(redLabel);
        add(redLabelPanel);
        add(Box.createRigidArea(new Dimension(0, 5)));

        redCapturedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        redCapturedPanel.setBackground(new Color(255, 255, 255));
        redCapturedPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        redCapturedPanel.setPreferredSize(new Dimension(180, 160));
        redCapturedPanel.setMinimumSize(new Dimension(180, 160));
        redCapturedPanel.setMaximumSize(new Dimension(180, 160));
        add(redCapturedPanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        // Black pieces captured (by opponent)
        JPanel blackLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        blackLabelPanel.setBackground(new Color(245, 245, 250));
        blackLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel blackLabel = new JLabel("⚫ Black Pieces:");
        blackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        blackLabel.setForeground(new Color(60, 60, 60));
        blackLabelPanel.add(blackLabel);
        add(blackLabelPanel);
        add(Box.createRigidArea(new Dimension(0, 5)));

        blackCapturedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        blackCapturedPanel.setBackground(new Color(255, 255, 255));
        blackCapturedPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        blackCapturedPanel.setPreferredSize(new Dimension(180, 160));
        blackCapturedPanel.setMinimumSize(new Dimension(180, 160));
        blackCapturedPanel.setMaximumSize(new Dimension(180, 160));
        add(blackCapturedPanel);

        // Set preferred size for the whole panel
        setPreferredSize(new Dimension(220, 480));
    }

    /**
     * Refresh the display of captured pieces from the current game state.
     */
    public void refreshCapturedPieces() {
        redCapturedPanel.removeAll();
        blackCapturedPanel.removeAll();

        List<CheckerPiece> capturedRed = engine.getBoard().getCapturedRedPieces();
        List<CheckerPiece> capturedBlack = engine.getBoard().getCapturedBlackPieces();

        // Display captured red pieces
        for (CheckerPiece piece : capturedRed) {
            String imageName = "red-" + (piece.isKing() ? "king.png" : "piece.png");
            JLabel pieceLabel = new JLabel(CheckerImageLoader.loadScaledImage(imageName, 30));
            redCapturedPanel.add(pieceLabel);
        }

        // Display captured black pieces
        for (CheckerPiece piece : capturedBlack) {
            String imageName = "black-" + (piece.isKing() ? "king.png" : "piece.png");
            JLabel pieceLabel = new JLabel(CheckerImageLoader.loadScaledImage(imageName, 30));
            blackCapturedPanel.add(pieceLabel);
        }

        redCapturedPanel.revalidate();
        redCapturedPanel.repaint();
        blackCapturedPanel.revalidate();
        blackCapturedPanel.repaint();
    }
}
