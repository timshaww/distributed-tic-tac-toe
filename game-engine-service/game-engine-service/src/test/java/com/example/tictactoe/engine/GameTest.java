package com.example.tictactoe.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(); // Initialize a new game before each test
    }

    @Test
    void testInitialBoardState() {
        char[][] board = game.getBoard();
        for (char[] row : board) {
            for (char cell : row) {
                assertEquals('-', cell, "Board should be initialized with '-'");
            }
        }
    }

    @Test
    void testValidMove() {
        assertTrue(game.makeMove('X', 0, 0), "X should be able to place a move at (0,0)");
        assertEquals('X', game.getBoard()[0][0], "Board should have 'X' at (0,0)");
    }

    @Test
    void testInvalidMoveOnOccupiedCell() {
        game.makeMove('X', 1, 1);
        assertFalse(game.makeMove('O', 1, 1), "O should not be able to move to an occupied cell");
    }

    @Test
    void testWinCondition() {
        game.makeMove('X', 0, 0);
        game.makeMove('O', 1, 0);
        game.makeMove('X', 0, 1);
        game.makeMove('O', 1, 1);
        game.makeMove('X', 0, 2); // X wins

        assertEquals("X_WINS", game.getStatus(), "Game status should be WIN when a player wins");
    }

    @Test
    void testDrawCondition() {
        game.makeMove('X', 0, 0);
        game.makeMove('O', 0, 1);
        game.makeMove('X', 0, 2);
        game.makeMove('O', 1, 1);
        game.makeMove('X', 1, 0);
        game.makeMove('O', 1, 2);
        game.makeMove('X', 2, 1);
        game.makeMove('O', 2, 0);
        game.makeMove('X', 2, 2); // Board full, resulting in a draw

        assertEquals("DRAW", game.getStatus(), "Game should be a draw when the board is full");
    }
}
