/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

/**
 *
 * @author OluwasegunAjayi
 */
public class Proxy {
    
    //private fields
    private static final String baseURL = "https://www.notexponential.com/aip2pgaming/api/index.php";
    private static final String apiKey = "14c2160caeaa2190f06d";
    private static final String userID = "1057";
    private static final String gameID = "0000";//put the gameId here
    private static final String teamID = "1111";//put the teamId here
    private static final String count = "1";//get the count for most recent moves

    public static String getGameID() {
        return gameID;
    }

    public static String getTeamID() {
        return teamID;
    }

    public static String getCount() {
        return count;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getUserID() {
        return userID;
    }

    public static String getBaseURL() {
        return baseURL;
    }
    
}
