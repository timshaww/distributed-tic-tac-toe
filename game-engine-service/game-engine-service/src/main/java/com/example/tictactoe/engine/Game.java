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

    private void printBoard() {
        System.out.println("Current Board State:");
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public boolean makeMove(char player, int row, int col) {
        System.out.println("Player " + player + " is making a move at [" + row + "][" + col + "]");

        if (!status.equals("IN_PROGRESS") || board[row][col] != '-' || player != currentPlayer) {
            System.out.println("Invalid move: status=" + status + ", cell=" + board[row][col] + ", currentPlayer=" + currentPlayer);
            return false;
        }

        board[row][col] = player;
        printBoard();

        if (checkWin(player)) {
            status = player == 'X' ? "X_WINS" : "O_WINS";
            System.out.println("Player " + player + " WINS!");
        } else {
            boolean full = isBoardFull();  // Capture result
            if (full) {
                status = "DRAW";  // Ensure this is updated
                System.out.println("Game ended in a DRAW.");
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                System.out.println("No winner yet, switching to player " + currentPlayer);
            }
        }
        return true;
    }

    private boolean checkWin(char player) {
        System.out.println("Checking win condition for player " + player);

        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                System.out.println("Player " + player + " wins by row " + i);
                return true;
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                System.out.println("Player " + player + " wins by column " + i);
                return true;
            }
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            System.out.println("Player " + player + " wins by main diagonal.");
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            System.out.println("Player " + player + " wins by anti-diagonal.");
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        System.out.println("Checking if board is full...");
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
                if (cell == '-') {
                    System.out.println("\nBoard is NOT full.");
                    return false;
                }
            }
            System.out.println();
        }
        System.out.println("Board is FULL.");
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public String getStatus() {
        return status;
    }
}
