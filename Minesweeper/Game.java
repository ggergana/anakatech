import java.util.Random;

/**
 * This is class is a representation of Minesweeper: consisting of a main class called Game and a subclass: Field.
 * The game is played on a square board and the player has to click on the cells which do not have a mine.
 * And obviously he doesn't know where the mines are. If a cell where a mine is present is clicked then he loses,
 * else he is still in the game until all safe cells are revealed.
 * There are three levels for playing this game:
 * 1. Beginner – 9 * 9 Board and 10 Mines
 * 2. Intermediate – 16 * 16 Board and 40 Mines
 * 3. Advanced – 24 * 24 Board and 99 Mines
 *
 * At the beginning of the game, the user should be able to select the level of difficulty of the game.
 * Once the level is chosen, the player should be able to enter a position on the board.
 * The first move of the player is always safe and not a mine!
 * If the user steps on a mine,  he loses the game and a proper
 * message should be displayed. Else if he steps on a safe cell and there is at least a single adjacent mine to this cell,
 * then that count is displayed on the current cell. Else if there are no adjacent mines to this cell,
 * recursively step on all the safe adjacent cells (hence reducing the time of the game-play).
 * If the user steps on a cell having no adjacent mines (in any of the surrounding eight cells)
 * then all the adjacent cells are automatically cleared.
 *
 *  The user keeps on playing until he steps on a cell having a mine (in this case the user loses)
 * or if he had clicked/stepped on all the safe cells (in this case the user wins).
 * @author Gergana Germanova
 */
public class Game {
    private Field field;
    private boolean firstMove = true;
    private int revealed;

    /**
     * The Game is started only if a valid parameter is given.
     * The Class Game initialises a field, with the given parameter.
     * @param level , a valid parameter is 0, 1 or 2
     */
    public Game(int level) {
        this.field = new Field(level);
        System.out.print(this);
    }

    /**
     * This method implements the chosen cells of the player and reveals them.
     * It is the primary method of the game. It can lead to several exits.
     * The cell is a mine, the cell is not a mine, all safe cells are open or the cell was already opened
     * Apropriate messages and actions are conducted for each case.
     * @param row , valid row is from 0 to size-1
     * @param column , valid column is from 0 to size-1
     */
    public void play(int row, int column) {
        if(this.field == null){
            System.out.print("Start new game!");
        }
        else {
            if (field.realField[row][column] == -1 && field.yourField[row][column]=="-") {
                mine(row, column);
                firstMove = false;
            } else if (field.realField[row][column] == 0 && field.yourField[row][column]=="-") {
                not_mine(row, column);
                System.out.print(this);
                firstMove = false;
            }
            else if (field.yourField[row][column] != "-" && revealed<field.size*field.size-field.mines) {
                System.out.print("You have already played this move.");
            }
            else if(revealed==field.size*field.size-field.mines){
                System.out.println("You won :). Game over! ");
                this.field = null;
            }
            else{
                this.field=null;
            }

        }
    }

    /**
     * This is a method, which is used if the chosen from the player cell is a mine.
     * It has two exists, if this is the first move, then cell is converted to a safe cell and
     * a non_mine cell is appointed to be a mine instead, starting from cell 0,0.
     * Otherwise all  mine_cells are revealed, the player lost and it is a game over.
     * @param row
     * @param column
     */
    private void mine(int row, int column) {
        if (correctFirstMove(row, column)) {
            play(row, column);
        } else {
            for (int i = 0; i < field.yourField.length; i++) {
                for (int j = 0; j < field.yourField.length; j++) {
                    if (field.realField[i][j] == -1) {
                        field.yourField[i][j] = "*";
                    }
                }
            }
            System.out.println(this);
            System.out.println("You lost :(. Game over!");
            this.field = null;
        }
    }

    /**
     * This method is a recursive method, which counts the the adjacent mine_cells to a clicked cell.
     * If the count = 0, then recursively all the adjacent 8 cells are revealed and receive the status clicked cell.
     * Respectively the method is used on them as well.
     * @param row
     * @param column
     */
    private void not_mine(int row, int column) {

        if(checkMargin(row, column)){return;}
        if(field.yourField[row][column]!="-"){ return;}
        if(sum(row,column)!=0){return;}
        if(revealed==field.size*field.size-field.mines){
            System.out.println("You won. :). Game over!");
            revealed++;
        }

        not_mine(row-1,column+1);
        not_mine(row-1,column);
        not_mine(row-1,column-1);
        not_mine(row+1,column-1);
        not_mine(row+1,column);
        not_mine(row+1,column+1);
        not_mine(row,column+1);
        not_mine(row,column-1);

    }

    /**
     * The method counts the adjacent mine_cell. If a cell is out of range, it is skipped.
     * @param row
     * @param column
     * @return sum, the count of all the mine_cells adjacent to the clicked cell
     */
    private int sum(int row, int column) {
        int sum = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (checkMargin(i + row, j + column)){ continue;}
                sum += field.realField[i+row][j+column];
            }
        }
        field.yourField[row][column]=String.valueOf(-sum);
        revealed++;
        return sum;
    }

    /**
     * The method checks, weather either of the indexes of a cell is out of range, meaning non existing on the field.
     * @param i , row
     * @param j , column
     * @return true if out of range, else return false
     */
    private boolean checkMargin(int i, int j){

        if(i<0 || j<0 || i>=field.realField.length|| j>=field.realField.length){return true;}
        else{return false;}
    }

    /**
     * Check weather this the first move of the player was a bomb and therefore corrected.
     * @param row
     * @param column
     * @return true if corrected, otherwise return false
     */
    private boolean correctFirstMove(int row,int column){
        if(firstMove){
            for (int i = 0; i < field.realField.length; i++) {
                for (int j = 0; j < field.realField.length; j++) {
                    if (field.realField[i][j] ==0) {
                        field.realField[i][j] = -1;
                        field.realField[row][column]=0;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Overwrite toString method of class Game.So it calls returns the return value of Field_to_string overwritten.
     * @return
     */
    public String toString() {
      return field.toString();
    }

    /**
     * Inner class, representing the playing field. It creates both of the two - dimensional array,
     * the one for display and the one for the functionality.The class contains also two printing methods and depending
     * on the chosen level, is the place where the number of mines and size of the field are appointed.
     */
    private class Field {
        private int size; //length or width of the field
        private int mines; // number of mine
        private int level; // level chosen from player
        private String[][] yourField; // the field, that is displayed to the player
        private int [][] realField; // the field, used for the functionality of the game

        /**
         * The sole constructor of the class. Takes the parameter and appoints the size and mine numbers in the current
         * field.
         * @param level - a number between 0 and 2. For 0 being most easiest and 2 being most difficult
         */
        private Field(int level) {
            this.level=level;
            if (this.level == 0) {
                this.mines = 10;
                this.size = 9;
            } else if (this.level == 1) {
                this.mines = 40;
                this.size = 16;
            } else if (this.level == 2) {
                this.mines = 99;
                this.size = 24;
            }
            else{
                this.size = -1; // code for an invalid parameter
            }
            check();

        }

        /**
         * The method checks weather the game was initialized with a number of the appropriate range.
         * If not, it displays a suitable message. Otherwise it creates and fills out a field with
         * dimensions, appropriate to the chosen level.
         */
        private void check(){
            if(this.size ==-1){
                System.out.print("Invalid input.");
            }

            else{
                createYours();
                createReal();
                // printInt(); For test purposes only.
            }
        }

        /**
         * The method creates the field( a two-dimensional, String array with dimensions size.
         * that is displayed to the player. At the beginning there are no mines.
         * And all cells are covered.
         */
        private void createYours() {
            yourField = new String[size][size]; // two-dimensional array
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    yourField[i][j] = "-";
                }
            }
        }

        /**
         * This method creates the field ( a two-dimensional, String array with dimensions size.
         * It uses a temporary variable controlling for the number of mines already implanted in the field.
         * It generates mine such as, it calculates as percentage the proportion mine_cells/all_cells and with this
         * probability and the assistance of the class Random, it feels out randomly with mines until the temp=mines.
         * Mines are coded with -1 and a cells are only amended, if supposed to be a mine otherwise
         * the method rests as the field is filled with zeros per initialization. Zeros = not_mine_cell.
         */
        private void createReal() {
            this.realField = new int[size][size];
            int temp = 0; // the temporary current number of the mines
            double random =(double) mines /(size * size); // proportion mine_cells/all_cells as coefficient
            Random randomGenerator =new Random();
            while(temp< mines) {
                for (int i = 0; i < realField.length; i++) {
                    for (int j = 0; j < realField.length; j++) {
                        if (randomGenerator.nextDouble()<= random && temp < mines && this.realField[i][j] ==0) {
                            this.realField[i][j] = -1;
                            temp++;
                        }
                    }
                }
            }
            /** // counting mines for test purposes
            int sum=0;
            for (int k = 0; k < realField.length; k++) {
                for (int p = 0; p < realField.length; p++) {
                    sum += this.realField[k][p];
                }
            }
            System.out.print(sum); */
        }

        /**
         * Only for test purposes, print_functionality_field.
         */
        private void printInt() {
            System.out.println("Current status of the board: ");
            System.out.print("   ");
            for (int i = 0; i < realField.length; i++) {
                System.out.print(i - 1 + "  ");
            }
            System.out.println();
            for (int i = 0; i < realField.length; i++) {
                System.out.print("  ");
                for (int j = 0; j < realField.length; j++) {
                    System.out.print(realField[i][j] + "  ");
                }
                System.out.println();
            }
        }

        /**
         * Overwrite to String method of the Class Field. It prints out the display_field.
         * @return
         */
        public String toString() {
            String myString= "Current status of the board: " + System.lineSeparator();
            myString += "   ";
            for(int i = 0; i< realField.length; i++){
                myString += (i + "  ");
            }
            myString += System.lineSeparator();
            for (int i = 0; i < yourField.length; i++) {
                myString+= (i + "  ");
                for (int j = 0; j < yourField.length; j++) {
                    myString += (yourField[i][j]+ "  " );
                }
                myString += System.lineSeparator();
            }
            return myString;
        }
    }
}


