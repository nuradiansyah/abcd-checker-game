package com.game.checker.logic;

import java.util.List;

/**
 * Intermediate-level Checker AI that uses minimax with alpha-beta pruning
 * and a heuristic board evaluation function.
 * 
 * Evaluation considers:
 * - Material count (pieces remaining)
 * - Kings vs regular pieces
 * - Board position (center control, advancement)
 * - Mobility (number of available moves)
 */
public class CheckerAIIntermediate {

    private static final int MAX_DEPTH = 6;

    // Piece values
    private static final int PIECE_VALUE = 100;
    private static final int KING_VALUE = 175;

    // Positional bonuses
    private static final int CENTER_BONUS = 10;
    private static final int ADVANCE_BONUS = 5;
    private static final int BACK_ROW_BONUS = 15;
    private static final int MOBILITY_BONUS = 3;

    /**
     * Chooses the best move for the given AI color using minimax with alpha-beta pruning.
     */
    public CheckerMove getBestMove(CheckerGameEngine engine, CheckerColor aiColor) {
        List<CheckerMove> validMoves = engine.getAllValidMoves(aiColor);

        if (validMoves.isEmpty()) {
            return null;
        }

        // If only one move, return it immediately
        if (validMoves.size() == 1) {
            return validMoves.get(0);
        }

        CheckerMove bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (CheckerMove move : validMoves) {
            // Simulate the move on a cloned engine
            CheckerGameEngine clonedEngine = cloneEngine(engine);
            clonedEngine.executeMove(move);
            clonedEngine.switchPlayer();

            int score = minimax(clonedEngine, MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, aiColor);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Minimax algorithm with alpha-beta pruning.
     */
    private int minimax(CheckerGameEngine engine, int depth, int alpha, int beta, boolean isMaximizing, CheckerColor aiColor) {
        // Base case: reached depth limit or game over
        if (depth == 0 || engine.isGameOver()) {
            return evaluate(engine, aiColor);
        }

        CheckerColor currentColor = isMaximizing ? aiColor : aiColor.opposite();
        List<CheckerMove> validMoves = engine.getAllValidMoves(currentColor);

        if (validMoves.isEmpty()) {
            return evaluate(engine, aiColor);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (CheckerMove move : validMoves) {
                CheckerGameEngine clonedEngine = cloneEngine(engine);
                clonedEngine.executeMove(move);
                clonedEngine.switchPlayer();

                int eval = minimax(clonedEngine, depth - 1, alpha, beta, false, aiColor);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (CheckerMove move : validMoves) {
                CheckerGameEngine clonedEngine = cloneEngine(engine);
                clonedEngine.executeMove(move);
                clonedEngine.switchPlayer();

                int eval = minimax(clonedEngine, depth - 1, alpha, beta, true, aiColor);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }
            return minEval;
        }
    }

    /**
     * Evaluates the board position from the perspective of the AI color.
     * Positive scores favor the AI, negative scores favor the opponent.
     */
    private int evaluate(CheckerGameEngine engine, CheckerColor aiColor) {
        CheckerColor opponentColor = aiColor.opposite();
        CheckerBoard board = engine.getBoard();

        int aiScore = 0;
        int opponentScore = 0;

        int aiPieces = 0;
        int opponentPieces = 0;

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                CheckerPiece piece = board.getPiece(row, col);
                if (piece == null) continue;

                int pieceScore = piece.isKing() ? KING_VALUE : PIECE_VALUE;

                // Positional bonuses
                pieceScore += getPositionalBonus(piece, row, col);

                if (piece.getColor() == aiColor) {
                    aiScore += pieceScore;
                    aiPieces++;
                } else {
                    opponentScore += pieceScore;
                    opponentPieces++;
                }
            }
        }

        // Mobility bonus
        List<CheckerMove> aiMoves = engine.getAllValidMoves(aiColor);
        List<CheckerMove> opponentMoves = engine.getAllValidMoves(opponentColor);
        aiScore += aiMoves.size() * MOBILITY_BONUS;
        opponentScore += opponentMoves.size() * MOBILITY_BONUS;

        // If opponent has no moves, AI wins — very high score
        if (opponentMoves.isEmpty() && opponentPieces > 0) {
            return 10000;
        }
        // If AI has no moves, AI loses — very low score
        if (aiMoves.isEmpty() && aiPieces > 0) {
            return -10000;
        }

        return aiScore - opponentScore;
    }

    /**
     * Returns positional bonuses for a piece based on its location.
     */
    private int getPositionalBonus(CheckerPiece piece, int row, int col) {
        int bonus = 0;

        // Center control: columns 2-5 and rows 2-5 are more valuable
        if (col >= 2 && col <= 5 && row >= 2 && row <= 5) {
            bonus += CENTER_BONUS;
        }

        if (!piece.isKing()) {
            // Advancement bonus: reward pieces moving towards promotion
            if (piece.getColor() == CheckerColor.RED) {
                // RED moves upward (from row 7 to row 0)
                bonus += (7 - row) * ADVANCE_BONUS;
            } else {
                // BLACK moves downward (from row 0 to row 7)
                bonus += row * ADVANCE_BONUS;
            }
        }

        // Back row defense: keeping pieces on the back row protects against kings
        if (!piece.isKing()) {
            if (piece.getColor() == CheckerColor.RED && row == 7) {
                bonus += BACK_ROW_BONUS;
            } else if (piece.getColor() == CheckerColor.BLACK && row == 0) {
                bonus += BACK_ROW_BONUS;
            }
        }

        return bonus;
    }

    /**
     * Creates a deep clone of the game engine for simulation purposes.
     */
    private CheckerGameEngine cloneEngine(CheckerGameEngine original) {
        CheckerGameEngine cloned = new CheckerGameEngine();
        CheckerBoard originalBoard = original.getBoard();
        CheckerBoard clonedBoard = cloned.getBoard();

        // Clear the default board
        for (int row = 0; row < clonedBoard.getSize(); row++) {
            for (int col = 0; col < clonedBoard.getSize(); col++) {
                clonedBoard.removePiece(row, col);
            }
        }

        // Copy pieces from original board
        for (int row = 0; row < originalBoard.getSize(); row++) {
            for (int col = 0; col < originalBoard.getSize(); col++) {
                CheckerPiece piece = originalBoard.getPiece(row, col);
                if (piece != null) {
                    CheckerPiece newPiece = new CheckerPiece(piece.getColor());
                    if (piece.isKing()) {
                        newPiece.makeKing();
                    }
                    clonedBoard.setPiece(row, col, newPiece);
                }
            }
        }

        // Set the current player to match the original
        while (cloned.getCurrentPlayer() != original.getCurrentPlayer()) {
            cloned.switchPlayer();
        }

        return cloned;
    }
}
