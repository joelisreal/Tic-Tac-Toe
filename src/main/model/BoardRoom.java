package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a boardroom having a collection of boards
public class BoardRoom implements Writable {
    private String name;
    private List<Board> boards;

    // EFFECTS: constructs boardroom with a name and empty list of boards
    public BoardRoom(String name) {
        this.name = name;
        boards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: adds board to this boardroom
    public void addBoard(Board board) {
        boards.add(board);
    }

    // EFFECTS: returns an unmodifiable list of boards in this boardroom
    public List<Board> getBoards() {
        return Collections.unmodifiableList(boards);
    }

    // EFFECTS: returns number of boards in this boardroom
    public int numBoards() {
        return boards.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("boards", boardsToJson());
        return json;
    }

    // EFFECTS: returns boards in this boardroom as a JSON array
    private JSONArray boardsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Board b : boards) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }
}

