package model;

// Abstract class for tokens
public abstract class GameToken {
    protected String token;
    protected int val;

    public GameToken() {
        //this.token = "X";
    }

    // EFFECTS: returns the string token
    public String getToken() {
        return this.token;
    }

    // EFFECTS: returns the token's value
    public int getVal() {
        return this.val;
    }


}
