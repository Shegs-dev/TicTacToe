/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

/**
 *This method creates the object to get most recent move(s)
 * @author OluwasegunAjayi
 */
public class LastPlay {
    
    //private fields
    private String moveId;
    private String gameId;
    private String teamId;
    private String move;
    private String symbol;
    private String moveX;
    private String moveY;

    public String getMoveId() {
        return moveId;
    }

    public void setMoveId(String moveId) {
        this.moveId = moveId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMoveX() {
        return moveX;
    }

    public void setMoveX(String moveX) {
        this.moveX = moveX;
    }

    public String getMoveY() {
        return moveY;
    }

    public void setMoveY(String moveY) {
        this.moveY = moveY;
    }
    
}
