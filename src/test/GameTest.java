package test;

import monkeysweeper.*;
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
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        try {
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    Square square = board[i][j];
                    assertEquals(square.getI(), i);
                    assertEquals(square.getJ(), j);
                    assertEquals(0, square.getVisibility());
                }
            }
            Assertions.assertTrue(monkeysweeper.getFlagCount() > 0);
            Assertions.assertEquals(monkeysweeper.getBombCount(), monkeysweeper.getFlagCount());
        } catch (Exception e) {
            System.out.println("board has incorrect size");
        }
    }

    @Test
    public void testGetAdjacentBombLikeProCenter() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        Assertions.assertEquals(1.0f / 8, monkeysweeper.getAdjacentBombProbabilityLikePro(board, 1, 1));
        board[1][1].setValue(4);
        Assertions.assertEquals(4.0f / 8, monkeysweeper.getAdjacentBombProbabilityLikePro(board, 1, 1));
        board[1][1].setValue(2);
        board[1][2].setVisibility(1);
        board[0][0].setVisibility(1);
        board[0][1].setVisibility(1);
        board[0][2].setVisibility(1);
        board[1][0].setVisibility(1);
        board[2][0].setVisibility(1);
        // 2-1, and 2-2 is left unvisible;
        Assertions.assertEquals(1.0f, monkeysweeper.getAdjacentBombProbabilityLikePro(board, 1, 1));

    }

    @Test
    public void testGetAdjacentBombLikeProEdgeAndCorner() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        Assertions.assertEquals(1.0f / 3, monkeysweeper.getAdjacentBombProbabilityLikePro(board, 0, 0));
        Assertions.assertEquals(1.0f / 5, monkeysweeper.getAdjacentBombProbabilityLikePro(board, 1, 0));
    }

    @Test
    public void testUpdateProbabilityLikeProGuaranteedSafe() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        float[][] probability = new float[20][20];
        board[0][0].setVisibility(1);
        board[0][1].setValue(-1);
        board[0][1].setVisibility(1);
        monkeysweeper.updateProbabilityLikePro(board, probability);

        // we know that 1-0 and 1-1 are not bombs
        Assertions.assertEquals(0f, probability[1][0]);
        Assertions.assertEquals(0f, probability[1][1]);
    }

    @Test
    public void testUpdateProbabilityLikeProGuaranteedBomb() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        float[][] probability = new float[20][20];
        board[0][0].setVisibility(1);
        board[0][1].setVisibility(1);
        board[1][1].setVisibility(1);
        monkeysweeper.updateProbabilityLikePro(board, probability);
        // we know that 1-0 is guaranteed to be bomb, deduced from 0-0's value being 1
        Assertions.assertEquals(1f, probability[1][0]);

        board[0][0].setValue(2);
        board[0][1].setVisibility(0);
        monkeysweeper.updateProbabilityLikePro(board, probability);
        // we know that 1-0 is guaranteed to be bomb, deduced from 0-0's value being 2
        Assertions.assertEquals(1f, probability[1][0]);
    }

    @Test
    public void testSolveLikePro() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();

        LinkedList<Square> ll = monkeysweeper.solveLikePro();
        Assertions.assertTrue(ll.size() > 0);
        for (Square s : ll) {
            // shown squares are not picked
            assertEquals(board[s.getI()][s.getJ()].getVisibility(), 0);
            monkeysweeper.show(s.getI(), s.getJ());
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                assertEquals(1, board[i][j].getVisibility());
            }
        }
    }

    @Test
    public void testSolveLikeAmateur() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();

        LinkedList<Square> ll = monkeysweeper.solveLikeAmateur();
        Assertions.assertTrue(ll.size() > 0);
        for (Square s : ll) {
            // shown squares are not picked
            assertEquals(board[s.getI()][s.getJ()].getVisibility(), 0);
            monkeysweeper.show(s.getI(), s.getJ());
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                assertEquals(1, board[i][j].getVisibility());
            }
        }
    }

    @Test
    public void testSolveLikeMonkey() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        // solve like monkey has approximately 0 chance of winning ever
        for (int i = 0; i < 100; i++) {
            monkeysweeper.reset();
            Square[][] board = monkeysweeper.getBoard();

            LinkedList<Square> ll = monkeysweeper.solveLikeMonkey();
            Assertions.assertTrue(ll.size() > 0);
            for (Square s : ll) {
                // shown squares are not picked
                monkeysweeper.show(s.getI(), s.getJ());
            }

            Assertions.assertTrue(monkeysweeper.isGameOver());
            Assertions.assertFalse(monkeysweeper.checkWin());
        }
    }

    @Test
    public void testSolveLikeMonkeyNoBombShouldClickAllSquaresAndWin() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        monkeysweeper.setBombCount(0);
        LinkedList<Square> ll = monkeysweeper.solveLikeMonkey();
        Assertions.assertEquals(ll.size(), 400);
        for (Square s : ll) {
            monkeysweeper.show(s.getI(), s.getJ());
        }
        Assertions.assertTrue(monkeysweeper.checkWin());
    }

    @Test
    public void testSolveLikeAmateurNoBombShouldClickAllSquaresAndWin() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(1);
            }
        }
        monkeysweeper.setBombCount(0);
        LinkedList<Square> ll = monkeysweeper.solveLikeAmateur();
        Assertions.assertEquals(ll.size(), 400);
        for (Square s : ll) {
            monkeysweeper.show(s.getI(), s.getJ());
        }
        Assertions.assertTrue(monkeysweeper.checkWin());
    }

    @Test
    public void testSolveLikeMonkeyAllBombsShouldWinVacuously() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(-1);
            }
        }
        monkeysweeper.setBombCount(400);
        LinkedList<Square> ll = monkeysweeper.solveLikeMonkey();
        Assertions.assertEquals(ll.size(), 0);
        Assertions.assertTrue(monkeysweeper.checkWin());
    }

    @Test
    public void testSolveLikeAmateurAllBombsShouldWinVacuously() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(-1);
            }
        }
        monkeysweeper.setBombCount(400);
        LinkedList<Square> ll = monkeysweeper.solveLikeAmateur();
        Assertions.assertEquals(ll.size(), 0);
        Assertions.assertTrue(monkeysweeper.checkWin());
    }

    @Test
    public void testSolveLikeProAllBombsShouldLose() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(-1);
            }
        }
        monkeysweeper.setBombCount(400);
        LinkedList<Square> ll = monkeysweeper.solveLikePro();
        Assertions.assertEquals(ll.size(), 0);
        Assertions.assertTrue(monkeysweeper.checkWin());
    }

    @Test
    public void testCheckWin() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (board[i][j].getValue() != -1) {
                    monkeysweeper.setUncoveredCnt(monkeysweeper.getUncoveredCnt() + 1);
                }
            }
        }
        Assertions.assertTrue(monkeysweeper.checkWin());

        monkeysweeper.reset();
        Assertions.assertFalse(monkeysweeper.checkWin());
    }

    @Test
    public void testIncrementAdjacentSquare() {
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(0);
            }
        }
        monkeysweeper.incrementAdjacentSquares(1, 1);
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
        Monkeysweeper monkeysweeper = new Monkeysweeper();
        Square[][] board = monkeysweeper.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j].setValue(0);
            }
        }
        monkeysweeper.incrementAdjacentSquares(0, 0);
        assertEquals(board[0][1].getValue(), 1);
        assertEquals(board[1][1].getValue(), 1);
        assertEquals(board[1][0].getValue(), 1);

        monkeysweeper.incrementAdjacentSquares(19, 18);
        assertEquals(board[19][19].getValue(), 1);
        assertEquals(board[18][19].getValue(), 1);
        assertEquals(board[18][18].getValue(), 1);
        assertEquals(board[18][17].getValue(), 1);
        assertEquals(board[19][17].getValue(), 1);
    }

}
