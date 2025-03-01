package com.example.tictactoe.engine;

public class MoveRequest {
    private char player;
    private int row;
    private int col;

    public MoveRequest() {}

    public MoveRequest(char player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
    }

    public char getPlayer() {
        return player;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
