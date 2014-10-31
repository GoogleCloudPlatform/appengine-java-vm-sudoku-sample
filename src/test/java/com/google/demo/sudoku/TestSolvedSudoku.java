package com.google.demo.sudoku;

import org.junit.Test;

import java.util.logging.Logger;

import static com.google.demo.sudoku.SolvedSudoku.grid2str;
import static com.google.demo.sudoku.SolvedSudoku.str2grid;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestSolvedSudoku {
    private static final Logger log = Logger.getLogger(TestSolvedSudoku.class.getName());

    @Test
    public void testToString() {

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

        System.out.println(grid2str(problem));
        assertEquals(
                "003107400040000070100090008900806002006070100400502006700020003020000010004903200",
                grid2str(problem)
        );
    }

    @Test
    public void testFromString() {

        byte[][] want = new byte[][]{
                {0, 0, 3, 1, 0, 7, 4, 0, 0},
                {0, 4, 0, 0, 0, 0, 0, 7, 0},
                {1, 0, 0, 0, 9, 0, 0, 0, 8},
                {9, 0, 0, 8, 0, 6, 0, 0, 2},
                {0, 0, 6, 0, 7, 0, 1, 0, 0},
                {4, 0, 0, 5, 0, 2, 0, 0, 6},
                {7, 0, 0, 0, 2, 0, 0, 0, 3},
                {0, 2, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 4, 9, 0, 3, 2, 0, 0}};

        byte[][] got = str2grid("003107400040000070100090008900806002006070100400502006700020003020000010004903200");
        assertArrayEquals(got, want);
    }
}
