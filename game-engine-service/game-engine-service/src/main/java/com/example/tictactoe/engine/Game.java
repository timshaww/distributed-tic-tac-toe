package com.example.tictactoe.engine;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
    @JsonProperty("board")
    private final char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private String status = "IN_PROGRESS";

    public Game() {

        // Initialize the board with empty cells.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    public boolean makeMove(char player, int row, int col) {

        // Check if the game is over, the cell is already occupied, or it's not the
        // player's turn.
        if (!status.equals("IN_PROGRESS") || board[row][col] != '-' || player != currentPlayer) {
            return false;
        }

        // Update the board and check if the game is over.
        board[row][col] = player;
        if (checkWin(player)) {
            status = player == 'X' ? "X_WINS" : "O_WINS";
        } else if (isBoardFull()) {
            status = "DRAW";
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
        return true;
    }

    private boolean checkWin(char player) {
        // Check rows and columns for a win.
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player)
                return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player)
                return true;
        }
        // Check diagonals for a win.
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player)
            return true;
        return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }

    private boolean isBoardFull() {
        // Check if the board is full.
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '-')
                    return false;
            }
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public String getStatus() {
        return status;
    }
}
