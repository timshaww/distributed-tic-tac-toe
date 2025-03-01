package com.example.tictactoe.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class GameSessionControllerTest {

    @InjectMocks
    private GameSessionController gameSessionController;

    @Mock
    private RestTemplate restTemplate;

    private Map<String, GameSession> sessions;
    private final String GAME_ENGINE_URL = "http://localhost:8080/games";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessions = new HashMap<>();
    }

    @Test
    void testCreateSession() {
        String sessionId = UUID.randomUUID().toString();
        String gameId = UUID.randomUUID().toString();

        when(restTemplate.postForObject(eq(GAME_ENGINE_URL + "/new"), isNull(), eq(String.class))).thenReturn(gameId);

        ResponseEntity<String> response = gameSessionController.createSession();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetSession_NotFound() {
        ResponseEntity<?> response = gameSessionController.getSession("non-existent-session");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session not found", response.getBody());
    }

    @Test
    void testSimulateGame_SessionNotFound() {
        ResponseEntity<?> response = gameSessionController.simulateGame("invalid-session");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session not found", response.getBody());
    }

}
