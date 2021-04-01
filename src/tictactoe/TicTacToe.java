/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

/**
 *
 * @author OluwasegunAjayi
 */
public class TicTacToe {
    
    //private fields
    //This fields are initialized
    private static int m;//set the value of m needed for victory
    private static int n;//set the length of the board
    private static List<List<Integer>> playedPositions;//replicate the board showing all played positions
    private static List<List<Integer>> positionsWorth;//setup the worth of each position
    
    //Initialising values in the constructor
    public TicTacToe(int m, int n){
        this.m = m;
        this.n = n;
        playedPositions = new ArrayList<>();
        positionsWorth = new ArrayList<>();
        for(int i = 0; i < n; i++){
            List<Integer> pPositions = new ArrayList<>();
            List<Integer> pWorth = new ArrayList<>();
            for(int j = 0; j < n; j++){
                pPositions.add(0);
                pWorth.add(0);
            }
            playedPositions.add(pPositions);
            positionsWorth.add(pWorth);
        }
    }
    
    //Call this method if playing first
    public PlayPosition playFirst(){
        int mid = n/2;
        PlayPosition predictedPosition = new PlayPosition();
        predictedPosition.setPlayer(true);
        predictedPosition.setX(mid);
        predictedPosition.setY(mid);
        
        return predictedPosition;
    }
    
    
    //This method returns the predicted play of opposition (minimum value) if play is optimal
    public PlayPosition minimax(PlayPosition currPlay){
        
        PlayPosition predictedPlay = this.evaluationFunction(currPlay);
        if(predictedPlay == null){
            System.out.println("Victory Has Been Attained");
            return predictedPlay;
        }
        if(predictedPlay.isPlayer()) System.out.println("My Player To Play Maximum Move");
        else System.out.println("My Opponent To Play My Minimum Move"+predictedPlay.getX());
        
        return predictedPlay;
    }
    
    //This method evaluates the play on the board and suggests the best play for player and opponent
    public PlayPosition evaluationFunction(PlayPosition currPlay){
        int i = 0;
        int min = m;
        int minPosX = currPlay.getX();
        int minPosY = currPlay.getY();
        boolean playerStatus = false;
        //Update current play on played positions
        List<Integer> getPlayedPositionsRow = new ArrayList<>();
        if(playedPositions.get(currPlay.getX()) != null){
            getPlayedPositionsRow = playedPositions.get(currPlay.getX());
            getPlayedPositionsRow.add(currPlay.getY(), 1);
        }else{
            getPlayedPositionsRow.add(currPlay.getY(), 1);
        }
        playedPositions.add(currPlay.getX(), getPlayedPositionsRow);
                
        //Rather than check the entire boardwhich is not possible the bigger the board grows, we will only check for the surrounding eight directions 
        //with respect to how big m (not n) is
        while(i < m){
            //There are eight possible directions from any state on the TicTacToe board
            //Each direction have its x and y positions declared in the arrays directionX and directionY
            int directionX[] = new int[]{(minPosX-(i+1)), (minPosX-(i+1)), (minPosX-(i+1)), (minPosX), (minPosX), (minPosX+(i+1)), (minPosX+(i+1)), (minPosX+(i+1)) };
            int directionY[] = new int[]{(minPosY-(i+1)), (minPosY), (minPosY+(i+1)), (minPosY-(i+1)), (minPosY+(i+1)), (minPosY-(i+1)), (minPosY), (minPosY+(i+1)) };
            for(int dir = 0; dir < 8; dir++){
                int value = 0;
                int valueX = 0;
                //Checking if the position selected to evaluate exists on the board
                if(this.exists(directionX[dir], directionY[dir])){
                    System.out.println("1"+playerStatus);
                    if(currPlay.isPlayer()){//If my player played last and opponent to play now
                        System.out.println("2");
                        if(playedPositions.get(directionX[dir]).get(directionY[dir]) == 0){
                            value = positionsWorth.get(directionX[dir]).get(directionY[dir]) + 1;
                            if (value > m) {//check if victory has been attained
                                return new PlayPosition();
                            }
                            positionsWorth.get(directionX[dir]).add(directionY[dir], value);
                            //checking if the position is the minimum value returns for my player and if the position has not been played
                            if (value < min && playedPositions.get(directionX[dir]).get(directionY[dir]) == 0) {
                                //This is the mini part of minimax
                                System.out.println("4");
                                minPosX = directionX[dir];
                                minPosY = directionY[dir];
                                playerStatus = false;
                            }
                        }
                    }else{//If my opponent played last and I am to play now
                        System.out.println("3");
                        if(playedPositions.get(directionX[dir]).get(directionY[dir]) == 0){
                            value = positionsWorth.get(directionX[dir]).get(directionY[dir]) - 1;
                            System.out.println("13" + value);
                            if (value < -m) {//check if victory has been attained
                                System.out.println("13" + value);
                                return new PlayPosition();
                            }
                            if (value == -m) {
                                minPosX = directionX[dir];
                                minPosY = directionY[dir];
                                playerStatus = true;
                                break;
                            }
                            positionsWorth.get(directionX[dir]).add(directionY[dir], value);
                            //checking if the position is the maximum value returns for my player and if the position has not been played
                            if (value > -min && playedPositions.get(directionX[dir]).get(directionY[dir]) == 0) {
                                System.out.println("5");
                                //This is the maxi part of minimax
                                minPosX = directionX[dir];
                                minPosY = directionY[dir];
                                playerStatus = true;
                            }
                        }
                        
                    }
                }
            }
            
            i++;
        }
        System.out.println("xxx"+playerStatus);
        PlayPosition predictedPosition = new PlayPosition();
        predictedPosition.setPlayer(playerStatus);
        predictedPosition.setX(minPosX);
        predictedPosition.setY(minPosY);
        
        return predictedPosition;
    }
    
    //Method to check if a position exists on the board
    private boolean exists(int x, int y){
        if(x >= 0 && x < n && y >= 0 && y < n) return true;
        return false;
    }
    
    
    //This method gets the last play
    public List<LastPlay> getLastPlay(){
        try {
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            WebTarget target = client.target(Proxy.getBaseURL());
            String response = target.queryParam("type", "moves")
                    .queryParam("gameId", Proxy.getGameID())
                    .queryParam("count", Proxy.getCount())
                    .request()
                    .header("x-api-key", Proxy.getApiKey())
                    .header("userId", Proxy.getUserID())
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            
	    /**if (response2.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
	    }
 
	    String response = response2.toString();**/
	    //System.out.println(response);
            
            System.out.println("Response : "+response);
      	    List<LastPlay> lastPlayList = new ArrayList<>();
            JsonParser parser = new JsonParser();
      	    JsonElement responseElement = parser.parse(response);
            JsonObject jsonObj = responseElement.getAsJsonObject();
            String code = jsonObj.get("code").getAsString();
            if(code.equals("OK")){
                JsonArray array = jsonObj.get("moves").getAsJsonArray();
                System.out.println("Here"+array.size());
                if (array.size() > 0) {
                    LastPlay lastPlay = new LastPlay();
                    JsonElement playObj = array.get(0);
                    JsonObject jsonObj1 = playObj.getAsJsonObject();
                    System.out.println("Herev"+jsonObj1.get("teamId").getAsString());
                    lastPlay.setGameId(jsonObj1.get("gameId").getAsString());
                    lastPlay.setMove(jsonObj1.get("move").getAsString());
                    lastPlay.setMoveId(jsonObj1.get("moveId").getAsString());
                    lastPlay.setMoveX(jsonObj1.get("moveX").getAsString());
                    lastPlay.setMoveY(jsonObj1.get("moveY").getAsString());
                    lastPlay.setSymbol(jsonObj1.get("symbol").getAsString());
                    lastPlay.setTeamId(jsonObj1.get("teamId").getAsString());
                    lastPlayList.add(lastPlay);
                }
            }
            
            return lastPlayList;
	} catch (Exception e) {
	    e.printStackTrace();
            return null;
	}
    }
    
    //This method makes a move
    public int makeMove(Move move){
        try {
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            WebTarget target = client.target(Proxy.getBaseURL());
            Form form =new Form();
            form.param("teamId", move.getTeamId());
            form.param("move", move.getMove());
            form.param("type", move.getType());
            form.param("gameId", move.getGameId());
            Response response2 = target.request()
                    .header("x-api-key", Proxy.getApiKey())
                    .header("userId", Proxy.getUserID())
                    .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED),Response.class);
            
	    if (response2.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
	    }
 
	    String response = response2.toString();
	    System.out.println("Response "+response);
            
            return 1;
	} catch (Exception e) {
	    e.printStackTrace();
            return 0;
	}
    }
    
    /**
     * To run this game with this algorithm
     * 1. If you are to play first then call the method playFirst to get the position on the board to play and call the move API to make move
     * 2. If you are not the first to play then call the method minimax in adversarial search class. Remember that n is the dimension of the board and 
     * m is the number of consecutive positions that gives a victory
     * 3. After every play of the game (whether player or opponent) you should call the API on slide 31 to get the last move (set count as 0) and then 
     * perform process 2 as above.
     */

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("TicTacToe Using Adversarial Search");
        //Default board settings - Please change if different as there is no API to get it easily
        int boardSize = 12;
        int target = 6;
        TicTacToe ttt = new TicTacToe(target, boardSize);
        //AdversarialSearch aSearch = new AdversarialSearch();
        int numberOfPlays = boardSize * boardSize;
        int countPlay = 0;
        while(countPlay < numberOfPlays){//This is determined by victory or there is no more place to play on the board
            //Checking if anyone has played
            List<LastPlay> gameLastPlay = ttt.getLastPlay();
            PlayPosition play = new PlayPosition();
            Move move = new Move();
            move.setGameId(Proxy.getGameID());
            move.setTeamId(Proxy.getTeamID());
            move.setType("move");
            if(gameLastPlay == null || gameLastPlay.isEmpty()){
                //Play first
                play = ttt.playFirst();
            }else{
                //Regular play
                System.out.println("Now");
                PlayPosition lastPlayConverted = new PlayPosition();
                lastPlayConverted.setX(Integer.parseInt(gameLastPlay.get(0).getMoveX()));
                lastPlayConverted.setY(Integer.parseInt(gameLastPlay.get(0).getMoveY()));
                if(gameLastPlay.get(0).getTeamId().equalsIgnoreCase(Proxy.getTeamID())){//My team played last so my opponent turn
                    lastPlayConverted.setPlayer(true);
                }else{
                    //My team plays now
                    lastPlayConverted.setPlayer(false);
                }
                play = ttt.minimax(lastPlayConverted);
                //When victory is attained
                if(play == null){
                    System.out.println("Victory Has Been Attained");
                    break;
                }
            }
            
            if(play.isPlayer()){
                String currMove = play.getX() + "," + play.getY();
                move.setMove(currMove);
                
                System.out.println("Move response " + ttt.makeMove(move));
            }
            
            countPlay++;
        }
        
        System.out.println("End TicTacToe Using Adversarial Search");
        
    }
    
}
