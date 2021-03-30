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
public class AdversarialSearch {
    
    //This method returns the predicted play of opposition (minimum value) if play is optimal
    public PlayPosition minimax(int m, List<List<Integer>> playedPositions, List<List<Integer>> positionsWorth, PlayPosition currPlay, 
            int n){
        
        PlayPosition predictedPlay = this.evaluationFunction(m, playedPositions, positionsWorth, currPlay, n);
        if(predictedPlay == null){
            System.out.println("Victory Has Been Attained");
            return predictedPlay;
        }
        if(predictedPlay.isPlayer()) System.out.println("My Player To Play Maximum Move");
        else System.out.println("My Opponent To Play My Minimum Move");
        
        return predictedPlay;
    }
    
    //This method evaluates the play on the board and suggests the best play for player and opponent
    public PlayPosition evaluationFunction(int m, List<List<Integer>> playedPositions, List<List<Integer>> positionsWorth, PlayPosition currPlay, 
            int n){
        int i = 0;
        int min = -m;
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
        //There are eight possible directions from any state on the TicTacToe board
        //Each direction have its x and y positions declared in the arrays directionX and directionY
        int directionX[] = new int[]{(minPosX-1), (minPosX-1), (minPosX-1), (minPosX), (minPosX), (minPosX+1), (minPosX+1), (minPosX+1) };
        int directionY[] = new int[]{(minPosY-1), (minPosY), (minPosY+1), (minPosY-1), (minPosY+1), (minPosY-1), (minPosY), (minPosY+1) };
        
        //Rather than check the entire boardwhich is not possible the bigger the board grows, we will only check for the surrounding eight directions 
        //with respect to how big m (not n) is
        while(i < m){
            for(int dir = 0; dir < 8; dir++){
                int value = 0;
                //Checking if the position selected to evaluate exists on the board
                if(this.exists(directionX[dir], directionY[dir], n)){
                    if(currPlay.isPlayer()){//If my player played last and opponent to play now
                        value = positionsWorth.get(directionX[dir]).get(directionY[dir]) + 1;
                        if(value >= m){//check if victory has been attained
                            return new PlayPosition();
                        }
                        //checking if the position is the minimum value returns for my player and if the position has not been played
                        if(value < min && playedPositions.get(directionX[dir]).get(directionY[dir]) == 0){
                            //This is the mini part of minimax
                            minPosX = directionX[dir];
                            minPosY = directionY[dir];
                            playerStatus = false;
                        }
                    }else{//If my opponent played last and I am to play now
                        value = positionsWorth.get(directionX[dir]).get(directionY[dir]) - 1;
                        if(value <= -m){//check if victory has been attained
                            return new PlayPosition();
                        }
                        //checking if the position is the maximum value returns for my player and if the position has not been played
                        if(value > min && playedPositions.get(directionX[dir]).get(directionY[dir]) == 0){
                            //This is the maxi part of minimax
                            minPosX = directionX[dir];
                            minPosY = directionY[dir];
                            playerStatus = true;
                        }
                    }
                }
            }
            
            i++;
        }
        
        PlayPosition predictedPosition = new PlayPosition();
        predictedPosition.setPlayer(playerStatus);
        predictedPosition.setX(minPosX);
        predictedPosition.setY(minPosY);
        
        return predictedPosition;
    }
    
    //Method to check if a position exists on the board
    private boolean exists(int x, int y, int n){
        if(x >= 0 && x < n && y >= 0 && y < n) return true;
        return false;
    }
    
}
