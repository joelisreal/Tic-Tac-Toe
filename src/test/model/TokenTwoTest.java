package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests token two which in this case is "O"
public class TokenTwoTest extends GameTokenTest {

    @BeforeEach
    public void setUp() {
        gameTok = new TokenTwo();
    }

    @Test
    public void testGetToken() {
        assertEquals(gameTok.getToken(), "O");
        assertFalse(gameTok.getToken().isEmpty());
    }
}
