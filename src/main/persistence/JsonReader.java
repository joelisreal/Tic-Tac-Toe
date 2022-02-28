package persistence;

import exceptions.BoardPositionOccupiedException;
import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads boardroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads boardroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public BoardRoom read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBoardRoom(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses board from JSON object and returns it
    private BoardRoom parseBoardRoom(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        BoardRoom br = new BoardRoom(name);
        addBoards(br, jsonObject);
        return br;
    }

    // MODIFIES: br
    // EFFECTS: parses boards from JSON object and adds them to boardroom
    private void addBoards(BoardRoom b, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("boards");
        for (Object json : jsonArray) {
            JSONObject nextBoard = (JSONObject) json;
            addBoard(b, nextBoard);
        }
    }

    // MODIFIES: br
    // EFFECTS: parses board from JSON object and adds it to boardroom
    private void addBoard(BoardRoom br, JSONObject jsonObject) {
        String playerTurn = jsonObject.getString("playerTurn");
        JSONArray jsonGameBoard = jsonObject.getJSONArray("gameBoard");
        String name = jsonObject.getString("name");
        Board board = new Board(jsonGameBoard.length());

        for (int i = 0; i < jsonGameBoard.length(); i++) {
            JSONArray rowJsonBoard = jsonGameBoard.getJSONArray(i);
            for (int j = 0; j < rowJsonBoard.length(); j++) {

                String rowJsonBoardToken = rowJsonBoard.getJSONObject(j).getString("token");

                if (rowJsonBoardToken.equals("X")) {
                    board.setGameBoard(j, i, new TokenOne());
                    //board.addXs(j, i);
                } else if (rowJsonBoardToken.equals("O")) {
                    board.setGameBoard(j, i, new TokenTwo());
                    //board.addOs(j, i);
                }


            }
        }
        board.setPlayerTurn(playerTurn);
        board.setBoardName(name);
        //br.addBoard(board);
        br.addBoard(board);
    }
}
