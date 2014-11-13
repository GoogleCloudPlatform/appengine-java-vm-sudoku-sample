package com.google.demo.sudoku;

import java.awt.FlowLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Some debugging helpers.
 */
public class DebugTools {
    private static final Scalar RED = new Scalar(0, 0, 255); // yeah BGR ...
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
     * Returns a pseudo graphic representation of a digit.
     * It is used to debug OCR.
     *
     * @param mat   the matrix in which you have a submatrix to represent.
     * @param index the index at which the submatrux is.
     */
    public static String printDigit(Mat mat, int index) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                sb.append('\n');
            }
            if (mat.get(index, i)[0] > 18.0) {
                sb.append('O');
            } else {
                sb.append(' ');
            }

        }
        sb.append('\n');
        return sb.toString();
    }

    /**
     * Draw a contour in red on an image.
     *
     * @param mat the image (as a matrix with border effect).
     * @param points the points of the contour you want to draw.
     */
    private static void drawContourOnMat(Mat mat, MatOfPoint points) {
        java.util.List<MatOfPoint> polygonContour = new java.util.ArrayList<>();
        polygonContour.add(points);
        Imgproc.drawContours(mat, polygonContour, 0, RED);
    }

    /**
     * Pop up a window showing a contour on an image.
     * This is for debugging on a graphic console.
     *
     * @param title title of the window.
     * @param mat the image.
     * @param points the contour to display as red.
     */
    public static void displayContourOnMat(String title, Mat mat, MatOfPoint points) {
        Mat imgCopy = new Mat();
        Imgproc.cvtColor(mat, imgCopy, Imgproc.COLOR_GRAY2RGB);
        drawContourOnMat(imgCopy, points);
        displayMat(title, imgCopy);
    }

    /**
     * Pop up a window showing contours on an image.
     * This is for debugging on a graphic console.
     *
     * @param title title of the window.
     * @param mat the image.
     * @param contours the list of contours you want ot display.
     */
    public static void displayContoursOnMat(String title, Mat mat, java.util.List<MatOfPoint> contours) {
        Mat imgCopy = new Mat();
        Imgproc.cvtColor(mat, imgCopy, Imgproc.COLOR_GRAY2RGB);
        for (MatOfPoint contour : contours) {
            drawContourOnMat(imgCopy, contour);
        }
        displayMat(title, imgCopy);
    }

    /**
     * Display an image.
     * This is for debugging on a graphic console.
     *
     * @param title title of the window.
     * @param mat image to display as an openCV matrix.
     */
    public static void displayMat(String title, Mat mat) {
        displayImage(title, ImageTools.mat2Image(mat));
    }

    /**
     * Display an image.
     *
     * This is for debugging on a graphic console.
     * @param title the title of the window.
     * @param img the image.
     */
    public static void displayImage(String title, Image img) {
        ImageIcon icon = new ImageIcon(img);
        JFrame frame = new JFrame(title);
        frame.setLayout(new FlowLayout());
        frame.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

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

    /**
     * Helper for unit tests.
     *
     * It generate the java code that creates the given sudoku grid.
     * @param grid the sudoku grid.
     * @return a string containing the java literal for this sudoku.
     */
    public static String javaArray(byte[][] grid) {
        StringBuilder sb = new StringBuilder();
        sb.append("byte[][] want = new byte[][]{\n");
        for (int y = 0; y < 9; y++) {
            sb.append("    {");
            for (int x = 0; x < 9; x++) {
                sb.append(grid[y][x]);
                sb.append(", ");
            }
            sb.append("},\n");
        }
        sb.append("};");
        return sb.toString();
    }
}
