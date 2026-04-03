package com.game.checker.gui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Generates a programmatic icon for Checkers — red and black pieces on a checkered board.
 * Used as the window/taskbar icon instead of the default Java coffee icon.
 */
public class GameIconFactory {

    public static Image createIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = size / 16;
        int boardSize = size - 2 * padding;
        int cellSize = boardSize / 4;

        // Draw 4x4 checkerboard pattern
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if ((row + col) % 2 == 0) {
                    g2.setColor(new Color(240, 217, 181)); // light square
                } else {
                    g2.setColor(new Color(101, 67, 33));   // dark square
                }
                g2.fillRect(padding + col * cellSize, padding + row * cellSize, cellSize, cellSize);
            }
        }

        // Board border
        g2.setColor(new Color(60, 40, 20));
        g2.setStroke(new BasicStroke(Math.max(1, size / 32f)));
        g2.drawRect(padding, padding, boardSize, boardSize);

        // Draw a few checker pieces on dark squares
        int discMargin = cellSize / 5;
        int discSize = cellSize - 2 * discMargin;

        // Red pieces (top)
        drawPiece(g2, padding + 1 * cellSize + discMargin, padding + 0 * cellSize + discMargin, discSize, new Color(200, 30, 30));
        drawPiece(g2, padding + 3 * cellSize + discMargin, padding + 0 * cellSize + discMargin, discSize, new Color(200, 30, 30));

        // Black pieces (bottom)
        drawPiece(g2, padding + 0 * cellSize + discMargin, padding + 3 * cellSize + discMargin, discSize, new Color(40, 40, 40));
        drawPiece(g2, padding + 2 * cellSize + discMargin, padding + 3 * cellSize + discMargin, discSize, new Color(40, 40, 40));

        // One red piece in middle
        drawPiece(g2, padding + 0 * cellSize + discMargin, padding + 1 * cellSize + discMargin, discSize, new Color(200, 30, 30));

        // One black piece in middle
        drawPiece(g2, padding + 3 * cellSize + discMargin, padding + 2 * cellSize + discMargin, discSize, new Color(40, 40, 40));

        g2.dispose();
        return image;
    }

    private static void drawPiece(Graphics2D g2, int x, int y, int size, Color color) {
        g2.setColor(color);
        g2.fillOval(x, y, size, size);
        // Highlight
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillOval(x + size / 5, y + size / 6, size / 3, size / 4);
        // Border
        g2.setColor(color.darker());
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(x, y, size, size);
    }
}
