package org.cis120.minesweeper;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */


import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;

/**
 * This class is a model for TicTacToe.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Minesweeper {

    private Square[][] board;
    private int flagCount;
    private int uncoveredCnt;
    private boolean gameOver;
    private int bombCount;

    /**
     * Constructor sets up game state.
     */
    public Minesweeper() {
        reset();
    }

    /**
     *
     *
     * @param i row to flip flag
     * @param j olumn to flip flag
     * @return void
     */
    public boolean flipFlag(int i, int j) {
        int visibility = board[i][j].getVisibility();
        if (gameOver == false) {
            if (visibility == 2) {
                board[i][j].setVisibility(0);
                flagCount++;

            } else if (visibility == 0) {
                if (flagCount == 0) {
                    return false;
                }
                board[i][j].setVisibility(2);
                flagCount--;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method to propagate the reveal of an empty square
     * @param board - the board that its propagating on
     * @param i - row of the source
     * @param j - column of the source
     */
    public void propagate(Square[][] board, int i , int j) {
        // base case is: if the board is already shown, the method does nothing
        if (board[i][j].getVisibility() == 1) {
            return;
        } else {
            board[i][j].setVisibility(1);
            uncoveredCnt++;
        }

        if (board[i][j].getValue() == 0) {
            if (i - 1 >= 0 && j - 1 >= 0 && board[i-1][j-1].getVisibility() != -1) {
                propagate(board, i-1, j-1);
            }
            if (i - 1 >= 0 && board[i-1][j].getVisibility() == 0) {
                propagate(board, i - 1, j);
            }
            if (i - 1 >= 0 && j + 1 < 20 && board[i-1][j+1].getVisibility() != -1) {
                propagate(board, i - 1, j+1);
            }
            if (j - 1 >= 0 && board[i][j-1].getVisibility() != -1) {
                propagate(board, i, j-1);
            }
            if (j + 1 < 20 && board[i][j+1].getVisibility() != -1) {
                propagate(board, i, j+1);
            }
            if (i + 1 < 20 && j - 1 >= 0 && board[i+1][j-1].getVisibility() != -1) {
                propagate(board, i + 1, j-1);
            }
            if (i + 1 < 20 && board[i+1][j].getVisibility() != -1) {
                propagate(board, i + 1, j);
            }
            if (i + 1 < 20 && j + 1 < 20 && board[i+1][j+1].getVisibility() != -1) {
                propagate(board, i + 1, j+1);
            }
        }

    }

    /**
     * @param i row to show
     * @param j column to show
     * @return void
     */
    public void show(int i, int j) {
        int value = board[i][j].getValue();
        int visibility = board[i][j].getVisibility();
        if (gameOver == false) {
            if (visibility == 0) {
                // if was hidden, show and endgame/propagate if necessary
                if (value == -1) {
                    System.out.println("Bomb");
                    board[i][j].setVisibility(1);
                    gameOver = true;
                    revealAllSquares();
                } else if (value == 0) {
                    // propagate discover if square value is 0
                    propagate(board, i, j);
                } else {
                    // show the number if square value > 0
                    uncoveredCnt++;
                    board[i][j].setVisibility(1);
                }
            } else if (visibility == 2) {
                // if was flag, left click turns removes flag
                board[i][j].setVisibility(0);
                flagCount++;
            }
        }
    }

    /**
     * Reveals all the squares in the board
     */
    public void revealAllSquares() {
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0 ; j < 20 ; j++) {
                board[i][j].setVisibility(1);
            }
        }
    }

    /**
     * check win condition
     */
    public boolean checkWin() {
        return bombCount + uncoveredCnt == 400;
    }

    /**
     * Helper method to increment the values of squares adjacent to a bomb
     */
    public void incrementAdjacentSquares(int i, int j) {
        if (i-1 >=0 && j-1 >= 0) {
            board[i-1][j-1].incrementValueIfNotBomb();
        }
        if (i-1 >= 0) {
            board[i-1][j].incrementValueIfNotBomb();
        }
        if (i-1 >= 0 && j+1 < 20) {
            board[i-1][j+1].incrementValueIfNotBomb();
        }
        if (j-1 >= 0) {
            board[i][j-1].incrementValueIfNotBomb();
        }
        if (j+1 < 20) {
            board[i][j+1].incrementValueIfNotBomb();
        }
        if (i+1 < 20 && j-1 >= 0) {
            board[i+1][j-1].incrementValueIfNotBomb();
        }
        if (i+1 < 20) {
            board[i+1][j].incrementValueIfNotBomb();
        }
        if (i+1 < 20 && j+1 < 20) {
            board[i+1][j+1].incrementValueIfNotBomb();
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new Square[20][20];
        flagCount = 0;
        uncoveredCnt = 0;

        // Populate the array randomly with bombs of probability 1/7
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0 ; j < 20; j++) {
                boolean isBomb = (Math.random() * 7 < 1);
                board[i][j] = isBomb ? new Square(i, j, -1) : new Square(i, j, 0);
                flagCount = isBomb ? ++flagCount : flagCount;
            }
        }

        // Update the values of squares adjacent to bombs accordingly
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0 ; j < 20; j++) {
                if (board[i][j].getValue() == -1) {
                    incrementAdjacentSquares(i, j);
                }
            }
        }
        bombCount = flagCount;
        gameOver = false;
    }

    public void removeAllFlags(Square[][] board) {
        for (int i = 0 ; i < 20 ; i++) {
            for (int j = 0 ; j < 20; j++) {
                int visibility = board[i][j].getVisibility();
                if (visibility == 2) {
                    board[i][j].setVisibility(0);
                    flagCount++;
                }
            }
        }
    }

    /**
     * Solve the game using standard human techniques
     * still probability based
     */
    public LinkedList<Square> solve(Method updateProbabilityMethod){
        LinkedList<Square> tries = new LinkedList<>();
        try {
            // removes all flags since algorithms don't care about flags
            removeAllFlags(board);

            Square[][] cloneBoard = new Square[20][20];
            float[][] probability = new float[20][20];
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    Square s = board[i][j];
                    cloneBoard[i][j] = new Square(i, j, s.getValue(), s.getVisibility());
                }
            }
            int presolveUncoveredCnt = uncoveredCnt;
            while (uncoveredCnt + bombCount < 400) {
                // update probability first before any decision is made
                Object[] parameters = new Object[2];
                parameters[0] = cloneBoard;
                parameters[1] = probability;
                updateProbabilityMethod.invoke(this, parameters);

                int choiceI = 0;
                int choiceJ = 0;
                float choiceProbability = 2;
                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 20; j++) {
                        if (cloneBoard[i][j].getVisibility() == 0) {
                            if (probability[i][j] < choiceProbability) {
                                choiceI = i;
                                choiceJ = j;
                                choiceProbability = probability[choiceI][choiceJ];
                            } else if (probability[i][j] == choiceProbability) {
                                int r = (int) (Math.random() * 11);
                                if (r < 2) {
                                    choiceI = i;
                                    choiceJ = j;
                                    choiceProbability = probability[choiceI][choiceJ];
                                }
                            }
                        }
                    }
                }
                // choose this square
                int value = cloneBoard[choiceI][choiceJ].getValue();
                tries.add(cloneBoard[choiceI][choiceJ]);
                probability[choiceI][choiceJ] = 1;
                if (value == -1) {
                    break;
                } else if (value == 0) {
                    // propagate discover if square value is 0
                    propagate(cloneBoard, choiceI, choiceJ);
                } else {
                    // show the number if square value > 0
                    cloneBoard[choiceI][choiceJ].setVisibility(1);
                    uncoveredCnt++;

                }

            }
            uncoveredCnt = presolveUncoveredCnt;
            return tries;
        } catch(Exception e) {
            System.out.println("An error occurred in trying to solve in sub method" + e);
            return tries;
        }
    }

    /**
     * Helper method for updateProbabilityLikePro
     */
    public float getAdjacentBombProbabilityLikePro(Square[][] board, int i, int j) {
        // guaranteed to only be called on shown, non-bomb squares
        float unshownCnt = 0.0f;
        int knownBombCnt = 0;
        if (i-1 >=0 && j-1 >= 0) {
            if (board[i-1][j-1].getVisibility() == 0) {
                unshownCnt++;
            } else if (board[i-1][j-1].getVisibility() == 1 && board[i-1][j-1].getValue() == -1) {
                knownBombCnt++;
            }
        }
        if (i-1 >= 0 ) {
            if (board[i-1][j].getVisibility() == 0) {
                unshownCnt++;
            } else if (board[i-1][j].getVisibility() == 1 && board[i-1][j].getValue() == -1) {
                knownBombCnt++;
            }
        }
        if (i-1 >= 0 && j+1 < 20 ) {
            if (board[i-1][j+1].getVisibility() == 0) {
                unshownCnt++;
            } else if (board[i-1][j+1].getVisibility() == 1 && board[i-1][j+1].getValue() == -1) {
                knownBombCnt++;
            }
        }
        if (j-1 >= 0) {
            if (board[i][j-1].getVisibility() == 0) {
                unshownCnt++;
            } else if (board[i][j-1].getVisibility() == 1 && board[i][j-1].getValue() == -1) {
                knownBombCnt++;
            }
        }
        if (j+1 < 20 ) {
            if (board[i][j+1].getVisibility() == 0) {
                unshownCnt++;
            } else if (board[i][j+1].getVisibility() == 1 && board[i][j+1].getValue() == -1) {
                knownBombCnt++;
            }
        }
        if (i+1 < 20 && j-1 >= 0) {
            if (board[i+1][j-1].getVisibility() == 0) {
                unshownCnt++;
            } else if (board[i+1][j-1].getVisibility() == 1 && board[i+1][j-1].getValue() == -1) {
                knownBombCnt++;
            }
        }
        if (i+1 < 20) {
            if (board[i+1][j].getVisibility() == 0) {
                unshownCnt++;
            } else if (board[i+1][j].getVisibility() == 1 && board[i+1][j].getValue() == -1) {
                knownBombCnt++;
            }
        }
        if (i+1 < 20 && j+1 < 20) {
            if (board[i+1][j+1].getVisibility() == 0) {
                unshownCnt++;
            } else if (board[i+1][j+1].getVisibility() == 1 && board[i+1][j+1].getValue() == -1) {
                knownBombCnt++;
            }
        }
        return (board[i][j].getValue()-knownBombCnt) / unshownCnt;
    }

    /**
     * update probability from board
     */
    public void updateProbabilityLikePro(Square[][] board, float[][] probability) {
        for (int i = 0 ; i < 20 ; i++) {
            for (int j = 0 ; j < 20; j++) {
                if (board[i][j].getVisibility() == 1) continue;
                float probabilityOfBeingBomb = 1.0f/10;
                boolean guaranteedNotBomb = false;
                if (i-1 >=0 && j-1 >= 0 && board[i-1][j-1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikePro(board, i-1, j-1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                    if (tempBombProbability == 0) guaranteedNotBomb = true;
                }
                if (i-1 >= 0 && board[i-1][j].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikePro(board, i-1, j);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                    if (tempBombProbability == 0) guaranteedNotBomb = true;
                }
                if (i-1 >= 0 && j+1 < 20 && board[i-1][j+1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikePro(board, i-1, j+1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                    if (tempBombProbability == 0) guaranteedNotBomb = true;
                }
                if (j-1 >= 0 && board[i][j-1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikePro(board, i, j-1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                    if (tempBombProbability == 0) guaranteedNotBomb = true;
                }
                if (j+1 < 20 && board[i][j+1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikePro(board, i, j+1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                    if (tempBombProbability == 0) guaranteedNotBomb = true;
                }
                if (i+1 < 20 && j-1 >= 0 && board[i+1][j-1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikePro(board, i+1, j-1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                    if (tempBombProbability == 0) guaranteedNotBomb = true;
                }
                if (i+1 < 20 && board[i+1][j].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikePro(board, i+1, j);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                    if (tempBombProbability == 0) guaranteedNotBomb = true;
                }
                if (i+1 < 20 && j+1 < 20 && board[i+1][j+1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikePro(board, i+1, j+1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                    if (tempBombProbability == 0) guaranteedNotBomb = true;
                }

                probability[i][j] = probabilityOfBeingBomb;
                if (probability[i][j] == 1.0f) {
                    // since we know this is a bomb, we pretend it is visible
                    board[i][j].setVisibility(1);
                }
                if (guaranteedNotBomb) {
                    probability[i][j] = 0;
                }
            }
        }
    }

    /**
     * Solve the game using standard human techniques
     * still probability based
     */
    public LinkedList<Square> solveLikePro() {
        LinkedList<Square> tries = new LinkedList<>();
        try {
            if (gameOver || checkWin()){
                return new LinkedList<>();
            }
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Square[][].class;
            parameterTypes[1] = float[][].class;
            Method updateProbabilityMethod = Minesweeper.class.getMethod("updateProbabilityLikePro", parameterTypes);
            tries = solve(updateProbabilityMethod);
        } catch (Exception e) {
            System.out.println("An error occurred in trying to solve " + e);
        } finally {
            return tries;
        }
    }

    /**
     * Helper method for updateProbabilityLikeMonkey
     */
    public float getAdjacentBombProbabilityLikeAmateur(Square[][] board, int i, int j) {
        // guaranteed to only be called on shown, non-bomb squares
        float unshownCnt = 0.0f;
        if (i-1 >=0 && j-1 >= 0 && board[i-1][j-1].getVisibility() == 0) {
            unshownCnt ++;
        }
        if (i-1 >= 0 && board[i-1][j].getVisibility() == 0) {
            unshownCnt++;
        }
        if (i-1 >= 0 && j+1 < 20 && board[i-1][j+1].getVisibility() == 0) {
            unshownCnt++;
        }
        if (j-1 >= 0 && board[i][j-1].getVisibility() == 0) {
            unshownCnt++;
        }
        if (j+1 < 20 && board[i][j+1].getVisibility() == 0) {
            unshownCnt++;
        }
        if (i+1 < 20 && j-1 >= 0 && board[i+1][j-1].getVisibility() == 0) {
            unshownCnt++;
        }
        if (i+1 < 20 && board[i+1][j].getVisibility() == 0) {
            unshownCnt++;
        }
        if (i+1 < 20 && j+1 < 20 && board[i+1][j+1].getVisibility() == 0) {
            unshownCnt++;
        }
        return (board[i][j].getValue() / unshownCnt);
    }

    /**
     * update probability from board
     */
    public void updateProbabilityLikeAmateur(Square[][] board, float[][] probability) {
        for (int i = 0 ; i < 20 ; i++) {
            for (int j = 0 ; j < 20; j++) {
                if (board[i][j].getVisibility() == 1) continue;
                float probabilityOfBeingBomb = 1.0f/10;
                if (i-1 >=0 && j-1 >= 0 && board[i-1][j-1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikeAmateur(board, i-1, j-1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                }
                if (i-1 >= 0 && board[i-1][j].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikeAmateur(board, i-1, j);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                }
                if (i-1 >= 0 && j+1 < 20 && board[i-1][j+1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikeAmateur(board, i-1, j+1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                }
                if (j-1 >= 0 && board[i][j-1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikeAmateur(board, i, j-1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                }
                if (j+1 < 20 && board[i][j+1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikeAmateur(board, i, j+1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                }
                if (i+1 < 20 && j-1 >= 0 && board[i+1][j-1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikeAmateur(board, i+1, j-1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                }
                if (i+1 < 20 && board[i+1][j].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikeAmateur(board, i+1, j);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                }
                if (i+1 < 20 && j+1 < 20 && board[i+1][j+1].getVisibility() == 1) {
                    float tempBombProbability = getAdjacentBombProbabilityLikeAmateur(board, i+1, j+1);
                    probabilityOfBeingBomb = Math.max(probabilityOfBeingBomb, tempBombProbability);
                }

                probability[i][j] = probabilityOfBeingBomb;
            }
        }
    }

    /**
     * Solve the game using standard human techniques
     * still probability based
     */
    public LinkedList<Square> solveLikeAmateur() {
        LinkedList<Square> tries = new LinkedList<>();
        try {
            if (gameOver || checkWin()){
                return new LinkedList<>();
            }
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Square[][].class;
            parameterTypes[1] = float[][].class;
            Method updateProbabilityMethod = Minesweeper.class.getMethod("updateProbabilityLikeAmateur", parameterTypes);
            tries = solve(updateProbabilityMethod);
        } catch (Exception e) {
            System.out.println("An error occurred in trying to solve " + e);
        } finally {
            return  tries;
        }
    }

    public LinkedList<Square> solveLikeMonkey() {
        // remove all flags
        removeAllFlags(board);

        LinkedList<Square> clone = new LinkedList<>();
        for (int i = 0 ; i < 20 ; i++) {
            for (int j = 0 ; j < 20; j++) {
                if (board[i][j].getVisibility()==0) {
                    Square s = board[i][j];
                    clone.add(new Square(i, j, s.getValue(), s.getVisibility()));
                }
            }
        }
        Collections.shuffle(clone);
        int include = 0 ;
        for (int i = 0 ; i < clone.size() ; i++) {
            include++;
            if (clone.get(i).getValue() == -1) {
                break;
            }
        }
        LinkedList<Square> tries = new LinkedList<>();
        for (int i = 0 ; i < include; i++) {
            tries.add(clone.get(i));
        }
        return tries;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param i row to retrieve
     * @param j column to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public Square getSquare(int i, int j) {
        return board[i][j];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getFlagCount() {
        return flagCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(flagCount+"\n");
        sb.append(uncoveredCnt+"\n");
        sb.append(gameOver+"\n");
        sb.append(bombCount+"\n");
        for (int i = 0 ; i < 20 ; i++) {
            for (int j = 0 ; j < 20 ; j++) {
                sb.append(board[i][j]+";");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Square[][] getBoard() {
        return this.board;
    }

    public int getBombCount() {
        return bombCount;
    }

    public int getUncoveredCnt() {
        return uncoveredCnt;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setUncoveredCnt(int uncoveredCnt) {
        this.uncoveredCnt = uncoveredCnt;
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Minesweeper t = new Minesweeper();

        t.show(1, 1);
        System.out.println(t);

        t.show(2, 1);
        System.out.println(t);

    }
}
