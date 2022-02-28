package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Abstract class for tokens
public abstract class GameTokenTest {

    protected GameToken gameTok;
    @BeforeEach
    public abstract void setUp();

    @Test
    public abstract void testGetToken();
}
