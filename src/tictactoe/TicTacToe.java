/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author OluwasegunAjayi
 */
public class TicTacToe {
    
    //private fields
    //This fields are initialized
    private int m;//set the value of m needed for victory
    private int n;//set the length of the board
    private List<List<Integer>> playedPositions;//replicate the board showing all played positions
    private List<List<Integer>> positionsWorth;//setup the worth of each position
    
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
        // TODO code application logic here
    }
    
}
