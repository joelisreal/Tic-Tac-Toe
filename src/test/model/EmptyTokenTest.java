package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

// Tests empty token
public class EmptyTokenTest extends  GameTokenTest {

    @BeforeEach
    public void setUp() {
        gameTok  = new EmptyToken();
    }

    @Test
    public void testGetToken() {
        assertEquals(gameTok.getToken(), "∅");
        assertFalse(gameTok.getToken().isEmpty());
    }
}
