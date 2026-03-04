# Checker Game Project

A classic Checker (Draughts) game implementation in Java with Swing GUI.

## Features

- **Three Game Modes:**
  - Two Players (local multiplayer)
  - vs Computer (Beginner AI — prioritizes captures, otherwise random)
  - vs Computer (Intermediate AI — minimax with alpha-beta pruning)

- **Complete Checker Rules:**
  - Diagonal movement on dark squares
  - Mandatory captures
  - Multi-jump sequences
  - King promotion at opposite end
  - Flying kings — can move/capture multiple squares diagonally

- **Visual Features:**
  - Interactive 8×8 board with piece images
  - Piece highlighting and move visualization
  - Color-coded valid moves (green) and captures (red)
  - Captured pieces panel showing taken pieces for each side

- **Scoring & Leaderboard System:**
  - 📊 **"How to Score" button** on main menu explains the scoring formula
  - Top 10 high scores
  - Score calculation based on:
    - Base score: 500 points for winning
    - Pieces remaining: 50 points each (max 600)
    - Time bonus: up to 300 points (decreases over time)
  - Leaderboard distinguishes between game modes (Two Players, vs Computer Beginner, vs Computer Intermediate)
  - Computer wins are not saved to the leaderboard

## How to Run

### From JAR:
```bash
java -jar checker_2026-03-04.jar
```

### In Eclipse:
1. Import the project into Eclipse workspace
2. Navigate to: `src/com/game/checker/gui/CheckerGUIManager.java`
3. Right-click → **Run As** → **Java Application**

### Game Workflow:
1. **Mode Selection** — Choose Two Players, vs Computer (Beginner), or vs Computer (Intermediate)
2. **Player Names** — Enter player names
3. **Color Selection** — Choose Red or Black (Red always moves first)
4. **Play Game** — Click pieces to select, click valid squares to move
5. **Game Over** — View score breakdown and leaderboard

## 🤖 AI Implementation

### Beginner AI
- Prioritizes capture moves when available
- Otherwise selects a random legal move
- Good for learning the basics of checkers

### Intermediate AI
- **Minimax algorithm** with **alpha-beta pruning** (search depth: 6)
- Heuristic board evaluation considering:
  - **Material**: regular pieces (100 pts) vs kings (175 pts)
  - **Center control**: bonus for occupying central squares
  - **Advancement**: bonus for pieces closer to promotion
  - **Back row defense**: bonus for keeping back-row pieces as protection
  - **Mobility**: bonus for having more available moves
- Provides a challenging opponent that plans several moves ahead

## 📊 Scoring System

### Score Formula
```
Total Score = Base Score + Piece Bonus + Time Bonus
```

### Breakdown
- **Base Score**: 500 points (win only)
- **Piece Bonus**: Remaining pieces × 50 (max 12 × 50 = 600)
- **Time Bonus**: 300 − (elapsed seconds ÷ 2), minimum 0

### Example
Win with 8 pieces remaining in 4 minutes:
- Base: 500 points
- Pieces: 8 × 50 = 400 points
- Time: 300 − (240 ÷ 2) = 180 points
- **Final Score: 1,080 points**

**Theoretical maximum: 1,400 points** (win instantly with all 12 pieces)

## Checker Piece Images

The game needs 4 PNG images in `resources/images/checker/`:

1. **red-piece.png** — Regular red checker (80×80 pixels)
2. **red-king.png** — Red king with crown (80×80 pixels)
3. **black-piece.png** — Regular black checker (80×80 pixels)
4. **black-king.png** — Black king with crown (80×80 pixels)

**Note:** The game includes placeholder graphics if images are missing, so it will run without them. However, for best visual experience, add proper checker piece images.

## Project Structure

```
checker_game_project/
├── src/com/game/checker/
│   ├── gui/                        (5 classes — User Interface)
│   │   ├── CheckerGUIManager.java      (Main GUI & entry point)
│   │   ├── CheckerBoardPanel.java      (Board visualization & interaction)
│   │   ├── CapturedPiecesPanel.java    (Captured pieces display)
│   │   ├── RoundedButton.java          (Custom styled button)
│   │   └── RoundedBorder.java          (Custom rounded border)
│   ├── logic/                      (9 classes — Game Logic & AI)
│   │   ├── CheckerBoard.java           (Board state & piece management)
│   │   ├── CheckerPiece.java           (Piece with color & king status)
│   │   ├── CheckerColor.java           (RED / BLACK enum)
│   │   ├── CheckerMove.java            (Move representation with captures)
│   │   ├── CheckerGameEngine.java      (Game rules & move validation)
│   │   ├── CheckerAI.java              (Beginner AI — random with capture priority)
│   │   ├── CheckerAIIntermediate.java  (Intermediate AI — minimax + alpha-beta)
│   │   ├── LeaderboardEntry.java       (Score data model)
│   │   └── LeaderboardManager.java     (Persistent leaderboard storage)
│   └── resources/                  (1 class — Resource Management)
│       └── CheckerImageLoader.java     (Image loading & caching)
├── resources/images/checker/       (Place PNG images here)
├── META-INF/MANIFEST.MF            (JAR manifest)
├── bin/                            (Compiled classes)
├── checker_2026-03-04.jar          (Runnable JAR)
├── checker_leaderboard.dat         (Created automatically)
└── README.md                       (This file)
```

## Game Rules

### Basic Movement
- Pieces move diagonally on dark squares only
- Regular pieces move forward only
- Kings can move both forward and backward (flying kings — multiple squares)

### Capturing
- Jump over opponent's piece to capture it
- **Mandatory captures** — must capture if possible
- **Multi-jumps** — continue capturing in same turn if possible
- Flying kings can capture from distance along diagonals

### Winning
- Capture all opponent pieces, OR
- Block opponent from making any legal moves

### King Promotion
- Red pieces reaching row 0 become kings
- Black pieces reaching row 7 become kings
- Kings get crowned and can move/capture multiple squares in all diagonal directions

## Technical Details

- **Language:** Java 17+ (Swing GUI)
- **Board Size:** 8×8
- **Starting Pieces:** 12 per player
- **AI Levels:** Beginner (random + capture priority), Intermediate (minimax depth 6)
- **JAR:** `checker_2026-03-04.jar` — self-contained, no external dependencies

## Future Integration

This game is integrated into the **Game Launcher Web** application alongside Chess and Memory games. The launcher automatically finds the latest JAR file by date.

## Author

Created: February 2026
Last Updated: March 2026