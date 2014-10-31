package com.google.demo.sudoku;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;

@Entity
public class SolvedSudoku implements Serializable{
    @Id String problem;
    String solution;

    public SolvedSudoku() {}

    public SolvedSudoku(String problem, String solution) {
        this.problem = problem;
        this.solution = solution;
    }

    /**
     * Transforms a string sudoku to 2d representation.
     *
     * @param gridstr a string representing a sudoku.
     * @return a 2d sudoku
     */
    public static byte[][] str2grid(String gridstr) {
        byte[][] response = new byte[9][9];
        byte[] from = gridstr.getBytes();
        for (int i=0;i<81;i++) {
            response[i/9][i%9] = (byte) (from[i] - '0');
        }
        return response;
    }

    /**
     * Flatten a 2d sudoku representation to a string.
     *
     * @param grid the 2d sudoku.
     * @return a string of length 81 like "143234..." representing the concatenation of all the lines of the sudoku.
     */
    public static String grid2str(byte[][]grid) {
        char[] flatten = new char[81];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                flatten[x + y * 9] = Character.forDigit(grid[y][x], 10);
            }
        }
        return new String(flatten);
    }
}
