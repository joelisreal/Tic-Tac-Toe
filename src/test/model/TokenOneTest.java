package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests token one which in this case is "X"
public class TokenOneTest extends  GameTokenTest {

    @BeforeEach
    public void setUp() {
        gameTok  = new TokenOne();
    }

    @Test
    public void testGetToken() {
        assertEquals(gameTok.getToken(), "X");
        assertFalse(gameTok.getToken().isEmpty());
    }
}
