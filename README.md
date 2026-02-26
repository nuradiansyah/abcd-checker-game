# Checker Game Project

A classic Checker (Draughts) game implementation in Java with Swing GUI.

## Features

- **Two Game Modes:**
  - Two Players (local multiplayer)
  - vs Computer (Beginner AI level)

- **Complete Checker Rules:**
  - Diagonal movement on dark squares
  - Mandatory captures
  - Multi-jump sequences
  - King promotion at opposite end
  - Kings can move/capture in both directions

- **Visual Features:**
  - Interactive 8×8 board (reuses chess board design)
  - Piece highlighting and move visualization
  - Color-coded valid moves (green) and captures (red)
  - Animated piece movements

- **Leaderboard System:**
  - Top 10 high scores
  - Score calculation based on:
    - Base score: 500 points
    - Pieces remaining: 50 points each
    - Time bonus: up to 300 points

## How to Run

### In Eclipse:
1. Import the project into Eclipse workspace
2. Navigate to: `src/com/game/checker/gui/CheckerGUIManager.java`
3. Right-click → **Run As** → **Java Application**

### Game Workflow:
1. **Mode Selection** - Choose Two Players or vs Computer
2. **Player Names** - Enter player names
3. **Color Selection** - Choose Red or Black (Red plays first)
4. **Play Game** - Click pieces to select, click valid squares to move
5. **Game Over** - View scores and leaderboard

## Checker Piece Images

The game needs 4 PNG images in `resources/images/checker/`:

1. **red-piece.png** - Regular red checker (80×80 pixels)
2. **red-king.png** - Red king with crown (80×80 pixels)
3. **black-piece.png** - Regular black checker (80×80 pixels)
4. **black-king.png** - Black king with crown (80×80 pixels)

**Note:** The game includes placeholder graphics if images are missing, so it will run without them. However, for best visual experience, add proper checker piece images.

## Project Structure

```
checker_game_project/
├── src/com/game/checker/
│   ├── gui/                    (5 classes - User Interface)
│   │   ├── CheckerGUIManager.java
│   │   ├── CheckerBoardPanel.java
│   │   ├── RoundedButton.java
│   │   └── RoundedBorder.java
│   ├── logic/                  (8 classes - Game Logic)
│   │   ├── CheckerBoard.java
│   │   ├── CheckerPiece.java
│   │   ├── CheckerColor.java
│   │   ├── CheckerMove.java
│   │   ├── CheckerGameEngine.java
│   │   ├── CheckerAI.java
│   │   ├── LeaderboardEntry.java
│   │   └── LeaderboardManager.java
│   └── resources/              (1 class - Resource Management)
│       └── CheckerImageLoader.java
├── resources/images/checker/   (Place PNG images here)
├── bin/                        (Compiled classes)
└── checker_leaderboard.dat     (Created automatically)
```

## Game Rules

### Basic Movement
- Pieces move diagonally on dark squares only
- Regular pieces move forward only
- Kings can move both forward and backward

### Capturing
- Jump over opponent's piece to capture it
- **Mandatory captures** - must capture if possible
- **Multi-jumps** - continue capturing in same turn if possible

### Winning
- Capture all opponent pieces, OR
- Block opponent from making any legal moves

### King Promotion
- Red pieces reaching row 0 become kings
- Black pieces reaching row 7 become kings
- Kings get crowned and can move in all diagonal directions

## Technical Details

- **Language:** Java (Swing GUI)
- **Board Size:** 8×8 (same as chess)
- **Starting Pieces:** 12 per player
- **AI Level:** Beginner (prioritizes captures, then random moves)

## Future Integration

This game is designed to be integrated into the game launcher web application alongside Chess and Memory games.

## Author

Created: February 2026
