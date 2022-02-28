package persistence;

import exceptions.BoardPositionOccupiedException;
import model.Board;
import model.BoardRoom;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            BoardRoom br = new BoardRoom("My board room");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyBoardRoom() {
        try {
            BoardRoom b = new BoardRoom("My board room");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyBoardroom.json");
            writer.open();
            writer.write(b);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBoardroom.json");
            b = reader.read();
            assertEquals("My board room", b.getName());
            assertEquals(0, b.numBoards());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralBoardRoom() {
        try {
            BoardRoom b = new BoardRoom("My board room");
            Board boardOne = new Board();
            Board boardTwo = new Board();
            boardOne.addXs(1, 0);
            boardOne.setPlayerTurn("X");
            boardTwo.addOs(2, 2);
            boardTwo.setPlayerTurn("O");
            boardOne.setBoardName("BoardOne");
            boardTwo.setBoardName("BoardTwo");
            boardOne.nextTurn();
            boardTwo.nextTurn();
            b.addBoard(boardOne);
            b.addBoard(boardTwo);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBoardRoom.json");
            writer.open();
            writer.write(b);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBoardRoom.json");
            b = reader.read();
            assertEquals("My board room", b.getName());
            List<Board> boards = b.getBoards();
            assertEquals(2, boards.size());
            checkBoard("O", boards.get(0), "BoardOne", boards.get(0).getGameBoard());
            checkBoard("X",boards.get(1), "BoardTwo", boards.get(1).getGameBoard());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (BoardPositionOccupiedException e) {
            System.out.println("Position occupied. Please select another.");
        }
    }
}