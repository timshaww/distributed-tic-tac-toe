package com.example.tictactoe.session;

// Represents a game session
public class GameSession {
    private final String sessionId;
    private final String gameId;

    public GameSession(String sessionId, String gameId) {
        this.sessionId = sessionId;
        this.gameId = gameId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getGameId() {
        return gameId;
    }
}
