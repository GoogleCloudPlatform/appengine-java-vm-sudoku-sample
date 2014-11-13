package com.google.demo.sudoku;

/**
 * Some debugging helpers.
 */
public class DebugTools {
    // a unicode template for a nice console rendering.
    private final static String TEMPLATE =
                "┏━━━┯━━━┯━━━┳━━━┯━━━┯━━━┳━━━┯━━━┯━━━┓\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┠───┼───┼───╂───┼───┼───╂───┼───┼───┨\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┠───┼───┼───╂───┼───┼───╂───┼───┼───┨\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┣━━━┿━━━┿━━━╋━━━┿━━━┿━━━╋━━━┿━━━┿━━━┫\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┠───┼───┼───╂───┼───┼───╂───┼───┼───┨\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┠───┼───┼───╂───┼───┼───╂───┼───┼───┨\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┣━━━┿━━━┿━━━╋━━━┿━━━┿━━━╋━━━┿━━━┿━━━┫\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┠───┼───┼───╂───┼───┼───╂───┼───┼───┨\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┠───┼───┼───╂───┼───┼───╂───┼───┼───┨\n" +
                "┃   │   │   ┃   │   │   ┃   │   │   ┃\n" +
                "┗━━━┷━━━┷━━━┻━━━┷━━━┷━━━┻━━━┷━━━┷━━━┛\n";

    /**
     * Prints the given grid on the unicode template.
     *
     * It allows you to have a readable debugging output.
     *
     * @param grid the sudoku grid.
     * @return the string representing the sudoku.
     */
    public static String fancyGrid(byte[][] grid) {
        char[] cgrid = TEMPLATE.toCharArray();

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (grid[x][y] != 0) {
                    cgrid[2 + (y * 4) + (1 + 2 * x) * 38] = Character.forDigit(grid[x][y], 10);
                }
            }
        }
        return new String(cgrid);
    }
}
