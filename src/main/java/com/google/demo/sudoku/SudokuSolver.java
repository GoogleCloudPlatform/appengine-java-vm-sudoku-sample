package com.google.demo.sudoku;

/**
 * A sudoku solver.
 */
public class SudokuSolver {

    private final byte[][] problem;

    /**
     * Make a sudoku solver for the given problem.
     * @param problem the problem to solve.
     */
    public SudokuSolver(byte[][] problem) {
        this.problem = problem;
    }

    /**
     * Solve the problem.
     *
     * @return the solved problem if a solution is found. null otherwise.
     */
    public byte[][] solve() {
        int length = problem.length;
        byte[][] response = new byte[length][problem[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(problem[i], 0, response[i], 0, problem[i].length);
        }
        if (solveGrid(0, 0, response)) {
            return response;
        }
        return null;
    }

    /**
     * Recursively solve the sudoku.
     *
     * @param x the x position to explore.
     * @param y the y position to explore.
     * @param grid the sudoku grid to work on (border effect).
     * @return true if this solution is possible.
     */
    private static boolean solveGrid(int x, int y, byte[][] grid) {
        if (x == 9) {
            x = 0;
            if (++y == 9)
                return true;
        }
        if (grid[x][y] != 0)  // skip filled cells
            return solveGrid(x + 1, y, grid);

        for (byte digit = 1; digit <= 9; ++digit) {
            if (isMovePossible(x, y, digit, grid)) {
                grid[x][y] = digit;
                if (solveGrid(x + 1, y, grid))
                    return true;
            }
        }
        grid[x][y] = 0; // this is a dead end, reset this move and try something else.
        return false;
    }

    /**
     * Check if the given move on the sudoku grid respects the rules.
     *
     * @param x x coordinate to consider.
     * @param y y coordinate to consider.
     * @param digit digit to try to put at x, y
     * @param grid the sudoku grid.
     * @return true if the move is possible, false otherwise.
     */
    private static boolean isMovePossible(int x, int y, byte digit, byte[][] grid) {
        int boxRowOffset = (x / 3) * 3;
        int boxColOffset = (y / 3) * 3;
        for (int k = 0; k < 3; ++k)
            for (int m = 0; m < 3; ++m)
                if (digit == grid[boxRowOffset + k][boxColOffset + m])
                    return false;

        for (int k = 0; k < 9; ++k)
            if (digit == grid[k][y])
                return false;

        for (int k = 0; k < 9; ++k)
            if (digit == grid[x][k])
                return false;
        return true;
    }
}
