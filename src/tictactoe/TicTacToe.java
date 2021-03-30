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
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MultivaluedMap;

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
    
    //This method gets the last play
    public List<LastPlay> getLastPlay(){
        try {
            Client client = Client.create();
	    WebResource webResource2 = client.resource(Proxy.getBaseURL());
	    ClientResponse response2 = webResource2.queryParam("type", "moves")
                    .queryParam("gameId", Proxy.getGameID())
                    .queryParam("count", Proxy.getCount())
                    .header("x-api-key", Proxy.getApiKey())
                    .header("userId", Proxy.getUserID())
                    .accept("application/json").get(ClientResponse.class);
	    if (response2.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
	    }
 
	    String response = response2.getEntity(String.class);
	    //System.out.println(response);
            
            System.out.println("Response : "+response);
      	    List<LastPlay> lastPlayList = new ArrayList<>();
            JsonParser parser = new JsonParser();
      	    JsonElement responseElement = parser.parse(response);
            JsonObject jsonObj = responseElement.getAsJsonObject();
      	    JsonArray array = jsonObj.get("moves").getAsJsonArray();
            if(array.size() > 0){
                LastPlay lastPlay = new LastPlay();
                JsonElement playObj = array.get(0);
                JsonObject jsonObj1 = playObj.getAsJsonObject();
                lastPlay.setGameId(jsonObj1.get("gameId").toString());
                lastPlay.setMove(jsonObj1.get("move").toString());
                lastPlay.setMoveId(jsonObj1.get("moveId").toString());
                lastPlay.setMoveX(jsonObj1.get("moveX").toString());
                lastPlay.setMoveY(jsonObj1.get("moveY").toString());
                lastPlay.setSymbol(jsonObj1.get("symbol").toString());
                lastPlay.setTeamId(jsonObj1.get("teamId").toString());
                lastPlayList.add(lastPlay);
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
            Client client = Client.create();
	    WebResource webResource = client.resource(Proxy.getBaseURL());
            MultivaluedMap formData = new MultivaluedMapImpl();
            formData.add("teamId", move.getTeamId());
            formData.add("move", move.getMove());
            formData.add("type", move.getType());
            formData.add("gameId", move.getGameId());
            ClientResponse response = webResource.type("application/x-www-form-urlencoded")
                    .header("x-api-key", Proxy.getApiKey())
                    .header("userId", Proxy.getUserID())
                    .post(ClientResponse.class, formData);
	    if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	    }
 
	    String resp = response.getEntity(String.class);
	    System.out.println("Response "+resp);
            
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
        AdversarialSearch aSearch = new AdversarialSearch();
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
                PlayPosition lastPlayConverted = new PlayPosition();
                lastPlayConverted.setX(Integer.parseInt(gameLastPlay.get(0).getMoveX()));
                lastPlayConverted.setY(Integer.parseInt(gameLastPlay.get(0).getMoveY()));
                if(gameLastPlay.get(0).getTeamId().equalsIgnoreCase(Proxy.getTeamID())){//My team played last so my opponent turn
                    lastPlayConverted.setPlayer(true);
                }else{
                    //My team plays now
                    lastPlayConverted.setPlayer(false);
                }
                play = aSearch.minimax(target, playedPositions, positionsWorth, lastPlayConverted, boardSize);
                //When victory is attained
                if(play == null){
                    System.out.println("Victory Has Been Attained");
                    break;
                }
            }
            
            if(move.getMove() != null && !move.getMove().isEmpty()){
                String currMove = play.getX() + "," + play.getY();
                move.setMove(currMove);
                
                System.out.println("Move response " + ttt.makeMove(move));
            }
            
            countPlay++;
        }
        
        System.out.println("End TicTacToe Using Adversarial Search");
        
    }
    
}
