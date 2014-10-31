package com.google.demo.sudoku;

import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class TestSolver {
    private static final Logger log = Logger.getLogger(TestSolver.class.getName());

    @Test
    public void testSolving() {

        byte[][] problem = new byte[][]{
                {0, 0, 3, 1, 0, 7, 4, 0, 0},
                {0, 4, 0, 0, 0, 0, 0, 7, 0},
                {1, 0, 0, 0, 9, 0, 0, 0, 8},
                {9, 0, 0, 8, 0, 6, 0, 0, 2},
                {0, 0, 6, 0, 7, 0, 1, 0, 0},
                {4, 0, 0, 5, 0, 2, 0, 0, 6},
                {7, 0, 0, 0, 2, 0, 0, 0, 3},
                {0, 2, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 4, 9, 0, 3, 2, 0, 0}};

        byte[][] want = new byte[][]{
                {5, 6, 3, 1, 8, 7, 4, 2, 9,},
                {8, 4, 9, 2, 3, 5, 6, 7, 1,},
                {1, 7, 2, 6, 9, 4, 3, 5, 8,},
                {9, 5, 1, 8, 4, 6, 7, 3, 2,},
                {2, 8, 6, 3, 7, 9, 1, 4, 5,},
                {4, 3, 7, 5, 1, 2, 8, 9, 6,},
                {7, 9, 8, 4, 2, 1, 5, 6, 3,},
                {3, 2, 5, 7, 6, 8, 9, 1, 4,},
                {6, 1, 4, 9, 5, 3, 2, 8, 7,},
        };

        log.info("problem:\n" + DebugTools.fancyGrid(problem));
        byte[][] got = new SudokuSolver(problem).solve();
        log.info("solution:\n" + DebugTools.fancyGrid(got));
        assertArrayEquals(got, want);
    }

    @Test
    public void testUnSolvable() {
        byte[][] problem = new byte[][]{
                {0, 3, 3, 1, 0, 7, 4, 0, 0},
                {0, 4, 0, 0, 0, 0, 0, 7, 0},
                {1, 0, 0, 0, 9, 0, 0, 0, 8},
                {9, 0, 0, 8, 0, 6, 0, 0, 2},
                {0, 0, 6, 0, 7, 0, 1, 0, 0},
                {4, 0, 0, 5, 0, 2, 0, 0, 6},
                {7, 0, 0, 0, 2, 0, 0, 0, 3},
                {0, 2, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 4, 9, 0, 3, 2, 0, 0}};
        byte[][] got = new SudokuSolver(problem).solve();
        assertNull(got);
    }
}
