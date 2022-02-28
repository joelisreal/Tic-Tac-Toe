package persistence;

import model.BoardRoom;
import model.Board;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            BoardRoom b = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyBoardRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBoardRoom.json");
        try {
            BoardRoom br = reader.read();
            assertEquals("My board room", br.getName());
            assertEquals(0, br.numBoards());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
    @Test
    void testReaderGeneralBoardRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBoardRoom.json");
        try {
            BoardRoom b = reader.read();
            assertEquals("My board room", b.getName());
            List<Board> boards = b.getBoards();
            assertEquals(2, boards.size());
            checkBoard("X", boards.get(0), "BoardOne", boards.get(0).getGameBoard());
            checkBoard("O", boards.get(1), "BoardTwo", boards.get(1).getGameBoard());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}