package com.example.tictactoe.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

class GameEngineControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private GameEngineController gameEngineController;

    @Mock
    private Game mockGame;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Game> games = new HashMap<>();
    private String gameId;

    @BeforeEach
    void setUp() {
        games.clear();
        MockitoAnnotations.openMocks(this);
        gameEngineController = new GameEngineController(games);
        mockMvc = MockMvcBuilders.standaloneSetup(gameEngineController).build();
    }

    @Test
    void testCreateGameAndRetrieveState() throws Exception {
        // Create a game first
        MvcResult result = mockMvc.perform(post("/games/new"))
                .andExpect(status().isOk())
                .andReturn();

        String gameId = result.getResponse().getContentAsString();

        // Ensure the game exists
        mockMvc.perform(get("/games/" + gameId))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateGame() throws Exception {
        mockMvc.perform(post("/games/new"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetGameState_GameNotFound() throws Exception {
        mockMvc.perform(get("/games/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Game not found"));
    }

    @Test
    void testMakeMove_GameNotFound() throws Exception {
        MoveRequest move = new MoveRequest('X', 0, 0);
        mockMvc.perform(post("/games/" + UUID.randomUUID() + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Game not found"));
    }

    @Test
    void testMakeMove_InvalidMove() throws Exception {
        MvcResult result = mockMvc.perform(post("/games/new"))
                .andExpect(status().isOk())
                .andReturn();

        String gameId = result.getResponse().getContentAsString();

        when(mockGame.makeMove('X', 0, 0)).thenReturn(false);
        games.put(gameId, mockGame);

        MoveRequest move = new MoveRequest('X', 0, 0);
        mockMvc.perform(post("/games/" + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid move"));
    }

    @Test
    void testMakeMove_ValidMove() throws Exception {
        MvcResult result = mockMvc.perform(post("/games/new"))
                .andExpect(status().isOk())
                .andReturn();

        String gameId = result.getResponse().getContentAsString();

        when(mockGame.makeMove('X', 0, 0)).thenReturn(true);
        games.put(gameId, mockGame);

        MoveRequest move = new MoveRequest('X', 0, 0);
        mockMvc.perform(post("/games/" + gameId + "/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk());
    }
}
