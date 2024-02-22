package edu.uob;

import java.util.ArrayList;

public class OXOModel {
    private ArrayList<ArrayList<OXOPlayer>> outerList;
    private OXOPlayer[] players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;
    OXOController gameController;

    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int MAX_SIZE = 9;
    public static final int MIN_SIZE = 3;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        outerList = new ArrayList<>();
        for (int row = 0; row < numberOfRows; row++){
            ArrayList<OXOPlayer> rowList = new ArrayList<>();
            for(int col = 0; col < numberOfColumns; col++){
                rowList.add(null);
            }
            outerList.add(rowList);
        }
        players = new OXOPlayer[NUMBER_OF_PLAYERS];
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void addPlayer(OXOPlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players[number];
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return outerList.size();
    }

    public int getNumberOfColumns() {
        return outerList.get(0).size(); //add a check to see if outerList is not empty
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        int currentNumberRows = getNumberOfRows();
        int currentNumberColumns = getNumberOfColumns();
        if(rowNumber >= 0 && colNumber >= 0 && rowNumber < currentNumberRows && colNumber < currentNumberColumns){
            return outerList.get(rowNumber).get(colNumber);
        }
        return null;
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        int currentNumberRows = getNumberOfRows();
        int currentNumberColumns = getNumberOfColumns();
        if(rowNumber >= 0 && colNumber >= 0 && rowNumber < currentNumberRows && colNumber < currentNumberColumns) {
            outerList.get(rowNumber).set(colNumber, player);
        }
    }

    public void addRow(){  //I'm copy and pasting some code from above here
        int currentNumberRows = getNumberOfRows();
        int currentNumberColumns = getNumberOfColumns();
        if (currentNumberRows < MAX_SIZE){
            ArrayList<OXOPlayer> rowList = new ArrayList<>();
            outerList.add(rowList);
            for(int col = 0; col < currentNumberColumns; col++){
                rowList.add(null);
            }
            if(isGameDrawn()){
                setGameDrawn(false);
            }
        }
        else {
            System.out.println("Maximum number of rows reached");
        }
    }

    public void addColumn(){
        int currentNumberRows = getNumberOfRows();
        int currentNumberColumns = getNumberOfColumns();
        if (currentNumberColumns < MAX_SIZE){
            for(int row = 0; row < currentNumberRows; row++) {
                outerList.get(row).add(null);
            }
            if(isGameDrawn()){
                setGameDrawn(false);
            }
        }
        else {
            System.out.println("Maximum number of columns reached");
        }
    }

    public void removeColumn(){
        int currentMaxColumn = getNumberOfColumns();
        int currentMaxRow = getNumberOfRows();
        if (columnIsEmpty(currentMaxColumn - 1) && currentMaxColumn > MIN_SIZE){
            for (int row = 0; row < currentMaxRow; row++){
                outerList.get(row).remove(currentMaxColumn - 1);
            }
        }
        if(currentMaxColumn == MIN_SIZE){
            System.out.println("Minimum number of columns reached");
        }
    }

    public void removeRow(){
        int currentMaxRow = getNumberOfRows();
        if (rowIsEmpty(currentMaxRow - 1) && currentMaxRow > MIN_SIZE){
            outerList.remove(currentMaxRow - 1);
        }
        if(currentMaxRow == MIN_SIZE){
            System.out.println("Minimum number of rows reached");
        }
    }

    public boolean rowIsEmpty(int rowIndex){
        int numberOfColumns = getNumberOfColumns();
        int emptyCount = 0;
        for(int column = 0; column < numberOfColumns; column++){
            if (outerList.get(rowIndex).get(column) != null){
                emptyCount++;
            }
        }
        return emptyCount <= 0;
    }

    public boolean columnIsEmpty(int columnIndex){
        int numberOfRows = getNumberOfRows();
        int emptyCount = 0;
        for(int row = 0; row < numberOfRows; row++){
            if (outerList.get(row).get(columnIndex) != null){
                emptyCount++;
            }
        }
        return emptyCount <= 0;
    }

    public char getCurrentLetter(int rowIndex, int columnIndex){
        char currentLetter = 'A';
        if(outerList.get(rowIndex).get(columnIndex) != null){
            currentLetter = outerList.get(rowIndex).get(columnIndex).getPlayingLetter();
        }
        return currentLetter;
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn(boolean isDrawn) {
        gameDrawn = isDrawn;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

}
