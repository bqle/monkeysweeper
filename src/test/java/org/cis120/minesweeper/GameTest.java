package org.cis120.minesweeper;

import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {

    @Test
    public void testCreation() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();
        try {
            for (int i = 0 ; i < 20; i++) {
                for (int j = 0 ; j < 20 ; j++) {
                    Square square =board[i][j];
                    assertEquals(square.getI(), i);
                    assertEquals(square.getJ(), j);
                    assertEquals(0, square.getVisibility());
                }
            }
            assertTrue(minesweeper.getFlagCount() > 0);
            assertEquals(minesweeper.getBombCount(), minesweeper.getFlagCount());
        } catch(Exception e) {
            System.out.println("board has incorrect size");
        }
    }

    @Test
    public void testGetAdjacentBombLikeProCenter() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        assertEquals(1.0f/8, minesweeper.getAdjacentBombProbabilityLikePro(board,1,1));
        board[1][1].setValue(4);
        assertEquals(4.0f/8, minesweeper.getAdjacentBombProbabilityLikePro(board,1,1));
        board[1][1].setValue(2);
        board[1][2].setVisibility(1);
        board[0][0].setVisibility(1);
        board[0][1].setVisibility(1);
        board[0][2].setVisibility(1);
        board[1][0].setVisibility(1);
        board[2][0].setVisibility(1);
        // 2-1, and 2-2 is left unvisible;
        assertEquals(1.0f, minesweeper.getAdjacentBombProbabilityLikePro(board,1,1));

    }

    @Test
    public void testGetAdjacentBombLikeProEdgeAndCorner() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        assertEquals(1.0f/3, minesweeper.getAdjacentBombProbabilityLikePro(board,0,0));
        assertEquals(1.0f/5, minesweeper.getAdjacentBombProbabilityLikePro(board,1,0));
    }

    @Test
    public void testUpdateProbabilityLikeProGuaranteedSafe() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        float[][] probability = new float[20][20];
        board[0][0].setVisibility(1);
        board[0][1].setValue(-1); board[0][1].setVisibility(1);
        minesweeper.updateProbabilityLikePro(board, probability);

        // we know that 1-0 and 1-1 are not bombs
        assertEquals(0f, probability[1][0]);
        assertEquals(0f, probability[1][1]);
    }

    @Test
    public void testUpdateProbabilityLikeProGuaranteedBomb() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        float[][] probability = new float[20][20];
        board[0][0].setVisibility(1);
        board[0][1].setVisibility(1);
        board[1][1].setVisibility(1);
        minesweeper.updateProbabilityLikePro(board, probability);
        // we know that 1-0 is guaranteed to be safe, deduced from 0-0's value being 1
        assertEquals(1f, probability[1][0]);

        board[0][0].setValue(2);
        board[0][1].setVisibility(0);
        minesweeper.updateProbabilityLikePro(board, probability);
        // we know that 1-0 is guaranteed to be safe, deduced from 0-0's value being 2
        assertEquals(1f, probability[1][0]);
    }


    @Test
    public void testSolveLikePro() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();

        LinkedList<Square> ll = minesweeper.solveLikePro();
        assertTrue(ll.size() > 0);
        for (Square s : ll) {
            // shown squares are not picked
            assertEquals(board[s.getI()][s.getJ()].getVisibility(), 0);
            minesweeper.show(s.getI(), s.getJ());
        }

        for (int i = 0 ; i < 20 ;i++) {
            for (int j = 0 ; j < 20; j++) {
                assertEquals(1, board[i][j].getVisibility());
            }
        }
    }

    @Test
    public void testSolveLikeAmateur() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();

        LinkedList<Square> ll = minesweeper.solveLikeAmateur();
        assertTrue(ll.size() > 0);
        for (Square s : ll) {
            // shown squares are not picked
            assertEquals(board[s.getI()][s.getJ()].getVisibility(), 0);
            minesweeper.show(s.getI(), s.getJ());
        }

        for (int i = 0 ; i < 20 ;i++) {
            for (int j = 0 ; j < 20; j++) {
                assertEquals(1, board[i][j].getVisibility());
            }
        }
    }

    @Test
    public void testSolveLikeMonkey() {
        Minesweeper minesweeper = new Minesweeper();
        // solve like monkey has approximately 0 chance of winning ever
        for (int i = 0 ; i < 100; i++) {
            minesweeper.reset();
            Square[][] board = minesweeper.getBoard();

            LinkedList<Square> ll = minesweeper.solveLikeMonkey();
            assertTrue(ll.size() > 0);
            for (Square s : ll) {
                // shown squares are not picked
                minesweeper.show(s.getI(), s.getJ());
            }

            assertEquals(true, minesweeper.isGameOver());
            assertEquals(false, minesweeper.checkWin());
        }
    }

    @Test
    public void testCheckWin() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();
        for (int i = 0 ; i < 20 ; i++) {
            for (int j = 0 ; j < 20; j++) {
                if (board[i][j].getValue() != -1) {
                    minesweeper.setUncoveredCnt(minesweeper.getUncoveredCnt()+1);
                }
            }
        }
        assertTrue(minesweeper.checkWin());

        minesweeper.reset();
        assertFalse(minesweeper.checkWin());
    }

    @Test
    public void testIncrementAdjacentSquare() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(0);
            }
        }
        minesweeper.incrementAdjacentSquares(1,1);
        assertEquals(board[0][0].getValue(), 1);
        assertEquals(board[0][1].getValue(), 1);
        assertEquals(board[0][2].getValue(), 1);
        assertEquals(board[1][0].getValue(), 1);
        assertEquals(board[1][2].getValue(), 1);
        assertEquals(board[2][0].getValue(), 1);
        assertEquals(board[2][1].getValue(), 1);
        assertEquals(board[2][2].getValue(), 1);
    }

    @Test
    public void testIncrementAdjacentSquareEdgeAndCorner() {
        Minesweeper minesweeper = new Minesweeper();
        Square[][] board = minesweeper.getBoard();
        for (int i = 0 ; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(0);
            }
        }
        minesweeper.incrementAdjacentSquares(0,0);
        assertEquals(board[0][1].getValue(), 1);
        assertEquals(board[1][1].getValue(), 1);
        assertEquals(board[1][0].getValue(), 1);

        minesweeper.incrementAdjacentSquares(19,18);
        assertEquals(board[19][19].getValue(), 1);
        assertEquals(board[18][19].getValue(), 1);
        assertEquals(board[18][18].getValue(), 1);
        assertEquals(board[18][17].getValue(), 1);
        assertEquals(board[19][17].getValue(), 1);
    }

}
