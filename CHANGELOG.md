# Changelog — Checker Game

All notable changes to this project will be documented in this file.  
JAR releases are stored in the `releases/` folder.

---

## [2026-03-05] — checker_2026-03-05.jar

### Changed
- Recompiled with latest source (no functional changes — rebuild for consistency across all game projects).

---

## [2026-03-04] — checker_2026-03-04.jar

### Added
- Intermediate AI using minimax with alpha-beta pruning (depth 6).
- Heuristic board evaluation (material, center control, advancement, back row defense, mobility).
- "How to Score" button on main menu.
- Leaderboard distinguishes between game modes (Two Players, Beginner, Intermediate).
- Scoring system: base score (500) + piece bonus (remaining × 50) + time bonus (up to 300).

### Improved
- Captured pieces panel showing taken pieces for each side.
- Color-coded valid moves (green) and captures (red).

---

## [2026-02-26] — checker_2026-02-26.jar

### Added
- Initial release of Checker (Draughts) game.
- Two Players mode (local multiplayer).
- vs Computer (Beginner) AI — prioritizes captures, otherwise random.
- Complete checker rules: diagonal movement, mandatory captures, multi-jump sequences, king promotion, flying kings.
- Interactive 8×8 board with piece images and move visualization.
- Basic leaderboard with persistent storage.
