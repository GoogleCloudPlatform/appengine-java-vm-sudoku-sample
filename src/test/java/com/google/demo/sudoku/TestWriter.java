package com.google.demo.sudoku;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.imageio.ImageIO;



public class TestWriter {
    private static final Logger log = Logger.getLogger(TestWriter.class.getName());

    public void TestOverwrite(String[] args) throws IOException {

        byte[][] original = new byte[][]{
                {0, 0, 3, 1, 0, 7, 4, 0, 0},
                {0, 4, 0, 0, 0, 0, 0, 7, 0},
                {1, 0, 0, 0, 9, 0, 0, 0, 8},
                {9, 0, 0, 8, 0, 6, 0, 0, 2},
                {0, 0, 6, 0, 7, 0, 1, 0, 0},
                {4, 0, 0, 5, 0, 2, 0, 0, 6},
                {7, 0, 0, 0, 2, 0, 0, 0, 3},
                {0, 2, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 4, 9, 0, 3, 2, 0, 0}};

        byte[][] solution = new byte[][]{
                {2, 6, 0, 0, 4, 0, 0, 2, 1},
                {5, 0, 8, 2, 6, 5, 3, 0, 9},
                {0, 7, 9, 3, 0, 8, 5, 6, 0},
                {0, 1, 2, 0, 1, 0, 7, 3, 0},
                {3, 5, 0, 4, 0, 9, 0, 4, 5},
                {0, 8, 7, 0, 3, 0, 8, 9, 0},
                {0, 3, 1, 6, 0, 1, 6, 5, 0},
                {6, 0, 5, 7, 5, 4, 9, 0, 4},
                {8, 9, 0, 0, 8, 0, 0, 8, 7}};

        InputStream is = TestWriter.class.getResourceAsStream("sudoku_flat.png");
        BufferedImage img = ImageIO.read(is);
        SudokuWriter.write(img, original, solution);

        /*
        // To inspect it visually...
        ImageIcon icon = new ImageIcon(img);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        */
    }
}
