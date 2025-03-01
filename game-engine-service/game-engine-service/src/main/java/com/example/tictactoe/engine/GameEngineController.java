package com.example.tictactoe.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameEngineController {

    // Store the games in a map.
    private final Map<String, Game> games;

    public GameEngineController() {
        this.games = new HashMap<>();
    }

    // Constructor for test injection
    public GameEngineController(Map<String, Game> games) {
        this.games = games;
    }

    // Make a move in the game.
    @PostMapping("/{gameId}/move")
    public ResponseEntity<?> makeMove(@PathVariable String gameId, @RequestBody MoveRequest move) {
        System.out.println("\u001B[34m" + "Received move request: " + move + "\u001B[0m");

        // Get the game from the map.
        Game game = games.get(gameId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        // Make the move.
        if (!game.makeMove(move.getPlayer(), move.getRow(), move.getCol())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid move");
        }
        return ResponseEntity.ok(game);
    }

    // Get the current state of the game.
    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGameState(@PathVariable String gameId) {
        System.out.println("\u001B[32m" + "Received request for game state: " + gameId + "\u001B[0m");

        // Get the game from the map.
        Game game = games.get(gameId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }
        return ResponseEntity.ok(game);
    }

    // Create a new game.
    @PostMapping("/new")
    public ResponseEntity<String> createGame() {
        System.out.println("\u001B[36m" + "Received request to create a new game" + "\u001B[0m");

        // Generate a unique game ID.
        String gameId = UUID.randomUUID().toString();

        // Store the new game in the map.
        games.put(gameId, new Game());
        return ResponseEntity.ok(gameId);
    }
}
