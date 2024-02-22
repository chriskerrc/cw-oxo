package edu.uob;

public class OXOController {
    OXOModel gameModel;
    OXOPlayer gamePlayer;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException { //make this method smaller
        if(gameModel.getWinner() != null){
            return;
        }
        OXOPlayer player1 = gameModel.getPlayerByNumber(0);
        OXOPlayer player2 = gameModel.getPlayerByNumber(1);
        OXOPlayer currentPlayerObject;
        int rowIndex = rowCommandToIndex(command);
        int columnIndex = columnCommandToIndex(command);
        int currentPlayerNumber = gameModel.getCurrentPlayerNumber();
        if (currentPlayerNumber == 0) {
            currentPlayerObject = player1;
        }
        else {
            currentPlayerObject = player2;
        }
        int currentNumberRows = gameModel.getNumberOfRows();
        int currentNumberColumns = gameModel.getNumberOfColumns();
        if(rowIndex >= 0 && rowIndex < currentNumberRows && columnIndex >= 0 && columnIndex < currentNumberColumns) {
            gameModel.setCellOwner(rowIndex, columnIndex, currentPlayerObject);
        }
        checkForWin(rowIndex, columnIndex, currentPlayerObject);
        if(isGameStalemate()){
            gameModel.setGameDrawn(true);
        }
        currentPlayerNumber = switchPlayerNumber(currentPlayerNumber);
        gameModel.setCurrentPlayerNumber(currentPlayerNumber);
    }

    public int rowCommandToIndex(String command){
        char rowChar = command.charAt(0);
        char upperRowChar = Character.toUpperCase(rowChar);
        return upperRowChar - 'A';
    }

    public int columnCommandToIndex(String command){
        char columnChar = command.charAt(1);
        return Character.getNumericValue(columnChar) - 1;
    }

    public int switchPlayerNumber(int currentNumber) {
        if (currentNumber == 0){
            return 1;
        }
        if (currentNumber == 1){
            return 0;
        }
        return -1;
    }

    public void addRow() {
        if(gameModel.getWinner()==null) {
            gameModel.addRow();
        }
    }
    public void removeRow() {
        if(gameModel.getWinner()==null) {
            gameModel.removeRow();
        }
        if(isGameStalemate()){
            gameModel.setGameDrawn(true);
        }
    }
    public void addColumn() {
        if(gameModel.getWinner()==null) {
            gameModel.addColumn();
        }
    }
    public void removeColumn() {
        if(gameModel.getWinner()==null) {
            gameModel.removeColumn();
        }
        if(isGameStalemate()){
            gameModel.setGameDrawn(true);
        }
    }
    public void increaseWinThreshold() {}
    public void decreaseWinThreshold() {}
    public void reset() {
        int currentNumberRows = gameModel.getNumberOfRows();
        int currentNumberColumns = gameModel.getNumberOfColumns();
        for(int row = 0; row < currentNumberRows; row++) {
            for(int col = 0; col < currentNumberColumns; col++) {
                gameModel.setCellOwner(row, col, null);
            }
        }
        gameModel.setCurrentPlayerNumber(0);
        gameModel.setWinner(null);

    }


    public char getCurrentPlayingCharacter() {
        int currentPlayerNumber = gameModel.getCurrentPlayerNumber();
        char currentLetter = 'A';
        if(currentPlayerNumber == 0){
            currentLetter = 'X';
        }
        if(currentPlayerNumber == 1){
            currentLetter = 'O';
        }
        return currentLetter;
    }

    public void checkForWin(int rowIndex, int columnIndex, OXOPlayer player){
        if(isHorizontalWin(rowIndex)){
            gameModel.setWinner(player);
        }
        if(isVerticalWin(columnIndex)){
            gameModel.setWinner(player);
        }
        if(isDiagonalWin(rowIndex, columnIndex)){
            gameModel.setWinner(player);
        }
    }

    public boolean isHorizontalWin(int row) {
        char currentPlayingLetter = getCurrentPlayingCharacter();
        int currentNumberColumns = gameModel.getNumberOfColumns();
        for (int column = 0; column < currentNumberColumns; column++) {
            if (gameModel.getCurrentLetter(row, column) == currentPlayingLetter) {
                if (lookAheadHorizontal(row, column, currentPlayingLetter)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean lookAheadHorizontal(int row, int column, char currentPlayingLetter){
        int currentPlayingLetterCounter = 0;
        int winThreshold = gameModel.getWinThreshold();
        for(int currentColumn = column; currentColumn < gameModel.getNumberOfColumns(); currentColumn++){
            if(gameModel.getCurrentLetter(row, currentColumn) == currentPlayingLetter){
                currentPlayingLetterCounter++;
            }
            else{
                break;
            }
        }
        return currentPlayingLetterCounter == winThreshold;
    }

    public boolean isVerticalWin(int column) {
        char currentPlayingLetter = getCurrentPlayingCharacter();
        int currentNumberRows = gameModel.getNumberOfRows();
        for (int row = 0; row < currentNumberRows; row++) {
            if (gameModel.getCurrentLetter(row, column) == currentPlayingLetter) {
                if (lookAheadVertical(row, column, currentPlayingLetter)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean lookAheadVertical(int row, int column, char currentPlayingLetter){
        int currentPlayingLetterCounter = 0;
        int winThreshold = gameModel.getWinThreshold();
        for(int currentRow = row; currentRow < gameModel.getNumberOfRows(); currentRow++){
            if(gameModel.getCurrentLetter(currentRow, column) == currentPlayingLetter){
                currentPlayingLetterCounter++;
            }
            else{
                break;
            }
        }
        return currentPlayingLetterCounter == winThreshold;
    }

    public boolean isDiagonalWin(int row, int column){
        int bottomLeftRow = getDiagonalStartCell(row, column, true);
        int topLeftRow = getDiagonalStartCell(row, column, false);
        return isDiagonalPositiveRun(bottomLeftRow) || isDiagonalNegativeRun(topLeftRow);
    }

    //to do: reduce shared code across positive and negative diagonal

    public int getDiagonalStartCell(int row, int column, boolean isPositive){
        if(isPositive){
            while (column >= 0 && row < gameModel.getNumberOfRows()) {
                row++;
                column--;
            }
            return row-1;
        }
        while (column >= 0 && row >= 0) {
            row--;
            column--;
        }
        return row+1;

    }

    public boolean isDiagonalPositiveRun(int bottomLeftRow) {
        int row = bottomLeftRow;
        int column = 0;
        while (row > -1 && row < gameModel.getNumberOfRows() && column < gameModel.getNumberOfColumns()){
            if (isDiagonalPositiveRunSmallBoard(row, column)) {
                return true;
            }
            else {
                row--;
                column++;
            }
        }
        return false;
    }

    public boolean isDiagonalPositiveRunSmallBoard(int row, int column){
        char currentPlayingLetter = getCurrentPlayingCharacter();
        int currentPlayingLetterCounter = 0;
        int winThreshold = gameModel.getWinThreshold();
        while (row > -1 && row < gameModel.getNumberOfRows() && column < gameModel.getNumberOfColumns()) {
            if (gameModel.getCurrentLetter(row, column) == currentPlayingLetter) {
                currentPlayingLetterCounter++;
            } else {
                break;
            }
            row--;
            column++;
        }
        return currentPlayingLetterCounter == winThreshold;
    }

    public boolean isDiagonalNegativeRun(int topLeftRow) {
        int row = topLeftRow;
        int column = 0;
        while (row > -1 && row < gameModel.getNumberOfRows() && column < gameModel.getNumberOfColumns()){
            if (isDiagonalNegativeRunSmallBoard(row, column)) {
                return true;
            }
            else {
                row++;
                column++;
            }
        }
        return false;
    }

    public boolean isDiagonalNegativeRunSmallBoard(int row, int column){
        char currentPlayingLetter = getCurrentPlayingCharacter();
        int currentPlayingLetterCounter = 0;
        int winThreshold = gameModel.getWinThreshold();
        while (row > -1 && row < gameModel.getNumberOfRows() && column < gameModel.getNumberOfColumns()) {
            if (gameModel.getCurrentLetter(row, column) == currentPlayingLetter) {
                currentPlayingLetterCounter++;
            } else {
                break;
            }
            row++;
            column++;
        }
        return currentPlayingLetterCounter == winThreshold;
    }

    public boolean isGameStalemate(){
        int currentNumberRows = gameModel.getNumberOfRows();
        int currentNumberColumns = gameModel.getNumberOfColumns();
        int nullCellCounter = 0;
        for(int row = 0; row < currentNumberRows; row++){
            for(int column = 0; column < currentNumberColumns; column++){
                if(gameModel.getCellOwner(row, column)==null){
                    nullCellCounter++;
                }
            }
        }
        return nullCellCounter == 0 && gameModel.getWinner() == null;
    }
}
