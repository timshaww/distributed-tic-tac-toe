package com.example.tictactoe.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/sessions")
public class GameSessionController {

    // Stores the game sessions in a map
    private final Map<String, GameSession> sessions = new HashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String GAME_ENGINE_URL = "http://localhost:8080/games";

    // Creates a new game session
    @PostMapping
    public ResponseEntity<String> createSession() {
        // Generates a random session ID and creates a new game
        String sessionId = UUID.randomUUID().toString();

        // Sends a POST request to the game engine to create a new game
        String gameId = restTemplate.postForObject(GAME_ENGINE_URL + "/new", null, String.class);

        // Stores the session in the map and returns the session ID
        sessions.put(sessionId, new GameSession(sessionId, gameId));
        return ResponseEntity.ok(sessionId);
    }

    // Simulates a game session
    @PostMapping("/{sessionId}/simulate")
    public ResponseEntity<?> simulateGame(@PathVariable String sessionId) {
        System.out.println("\u001B[34m" + "Simulating game for session: " + sessionId + "\u001B[0m");
        System.out.println("Existing Sessions: " + sessions.keySet());
        System.out.println("Requested Session ID: " + sessionId + "\n\n");

        // Checks if the session exists
        if (!sessions.containsKey(sessionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
        }

        // Simulates the game by making random moves
        GameSession session = sessions.get(sessionId);
        char currentPlayer = 'X';
        Random random = new Random();

        while (true) {
            try {
                // Gets the game state from the game engine
                ResponseEntity<Game> response = restTemplate.getForEntity(GAME_ENGINE_URL + "/" + session.getGameId(),
                        Game.class);
                // Checks if the game is in progress
                if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                    break;
                }

                // Makes a random move if the game is in progress
                Game game = response.getBody();
                if (!game.getStatus().equals("IN_PROGRESS")) {
                    break;
                }

                // Generates a random move
                int row, col;
                do {
                    row = random.nextInt(3);
                    col = random.nextInt(3);
                } while (game.getBoard()[row][col] != '-');

                // Makes the move and switches the player
                MoveRequest move = new MoveRequest(currentPlayer, row, col);

                // Sends a POST request to the game engine to make a move
                restTemplate.postForObject(GAME_ENGINE_URL + "/" + session.getGameId() + "/move", move, String.class);

                // Switches the player
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';

            } catch (RestClientException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error during simulation: " + e.getMessage());
            }
        }

        // Returns the game session
        return ResponseEntity.ok(session);
    }

    // Gets the game session by ID
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSession(@PathVariable String sessionId) {
        System.out.println("\u001B[32m" + "GET request for session: " + sessionId + "\u001B[0m");
        System.out.println("Existing Sessions: " + sessions.keySet());
        System.out.println("Requested Session ID: " + sessionId + "\n\n");

        // Checks if the session exists
        if (!sessions.containsKey(sessionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
        }

        // Returns the session
        return ResponseEntity.ok(sessions.get(sessionId));
    }
}
