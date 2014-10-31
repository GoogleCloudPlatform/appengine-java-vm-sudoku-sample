package com.google.demo.sudoku;

import org.junit.Ignore;
import org.junit.Test;
import org.opencv.core.Core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TestReader {

    private static final Logger log = Logger.getLogger(TestSolver.class.getName());
    private static HashMap<String, byte[][]> testCases = new HashMap<>();

    static {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (Exception e) {
            log.severe("Crashed loading: " + Core.NATIVE_LIBRARY_NAME);
            log.severe(System.getProperty("java.library.path"));
        }
    }

    static {
        testCases.put("0.png", new byte[][]{
                {0, 0, 3, 1, 0, 7, 4, 0, 0,},
                {0, 4, 0, 0, 0, 0, 0, 7, 0,},
                {1, 0, 0, 0, 9, 0, 0, 0, 8,},
                {9, 0, 0, 8, 0, 6, 0, 0, 2,},
                {0, 0, 6, 0, 7, 0, 1, 0, 0,},
                {4, 0, 0, 5, 0, 2, 0, 0, 6,},
                {7, 0, 0, 0, 2, 0, 0, 0, 3,},
                {0, 2, 0, 0, 0, 0, 0, 1, 0,},
                {0, 0, 4, 9, 0, 3, 2, 0, 0,},
        });

        testCases.put("1.jpg", new byte[][]{
                {0, 0, 9, 2, 0, 0, 1, 0, 0,},
                {0, 0, 6, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 4, 0, 8,},
                {6, 1, 0, 0, 0, 0, 2, 0, 0,},
                {0, 0, 0, 3, 9, 0, 6, 7, 0,},
                {0, 5, 0, 1, 6, 0, 0, 4, 0,},
                {0, 4, 0, 0, 2, 1, 0, 0, 0,},
                {0, 9, 0, 0, 8, 0, 0, 6, 0,},
                {0, 0, 0, 9, 0, 7, 0, 0, 4,},
        });

        testCases.put("2.jpg", new byte[][]{
                {0, 0, 7, 0, 2, 0, 0, 0, 0,},
                {9, 3, 0, 0, 6, 0, 0, 0, 0,},
                {0, 0, 0, 3, 5, 0, 0, 1, 0,},
                {8, 7, 0, 0, 0, 0, 0, 5, 0,},
                {1, 0, 0, 6, 7, 3, 0, 8, 2,},
                {0, 0, 0, 0, 0, 4, 0, 0, 0,},
                {0, 0, 0, 1, 0, 6, 0, 0, 3,},
                {7, 0, 0, 8, 0, 0, 0, 0, 4,},
                {5, 0, 1, 0, 0, 0, 0, 0, 0,},
        });

        testCases.put("3.jpg", new byte[][]{
                {7, 1, 8, 4, 9, 5, 3, 2, 6,},
                {2, 5, 3, 8, 6, 1, 7, 4, 9,},
                {9, 4, 6, 3, 7, 2, 1, 5, 8,},
                {1, 2, 4, 9, 5, 3, 6, 8, 7,},
                {3, 8, 7, 1, 4, 6, 2, 9, 5,},
                {5, 6, 9, 2, 8, 7, 4, 3, 1,},
                {6, 9, 5, 7, 3, 4, 8, 1, 2,},
                {4, 7, 2, 5, 1, 8, 9, 6, 3,},
                {8, 3, 1, 6, 2, 9, 5, 7, 4,},
        });

        testCases.put("4.jpg", new byte[][]{
                {6, 0, 0, 1, 0, 0, 0, 0, 0,},
                {3, 0, 0, 9, 0, 0, 0, 0, 0,},
                {0, 0, 0, 4, 0, 0, 5, 8, 0,},
                {0, 0, 9, 3, 6, 5, 0, 0, 0,},
                {0, 0, 4, 0, 0, 0, 2, 0, 3,},
                {1, 0, 0, 0, 0, 7, 0, 0, 9,},
                {0, 5, 2, 0, 0, 0, 6, 0, 0,},
                {9, 8, 0, 0, 0, 4, 0, 0, 0,},
                {0, 0, 0, 6, 0, 9, 4, 0, 0,},
        });

        testCases.put("5.jpg", new byte[][]{
                {0, 0, 9, 2, 0, 0, 1, 0, 0,},
                {0, 0, 6, 0, 0, 0, 0, 0, 0,},
                {0, 0, 0, 0, 0, 0, 4, 0, 8,},
                {6, 1, 0, 0, 0, 0, 2, 0, 0,},
                {0, 0, 0, 3, 9, 0, 6, 7, 0,},
                {0, 5, 0, 1, 6, 0, 0, 4, 0,},
                {0, 4, 0, 0, 2, 1, 0, 0, 0,},
                {0, 9, 0, 0, 8, 0, 0, 6, 0,},
                {0, 0, 0, 9, 0, 7, 0, 0, 4,},
        });

        testCases.put("6.jpg", new byte[][]{
                {0, 0, 0, 0, 0, 0, 8, 0, 4,},
                {0, 0, 2, 0, 0, 0, 0, 0, 0,},
                {0, 5, 3, 1, 2, 0, 0, 0, 0,},
                {0, 0, 0, 0, 9, 6, 0, 0, 0,},
                {7, 0, 0, 0, 1, 0, 6, 0, 0,},
                {1, 0, 0, 0, 5, 3, 0, 0, 0,},
                {0, 0, 2, 0, 7, 0, 0, 8, 6,},
                {6, 1, 0, 0, 0, 0, 0, 5, 3,},
                {4, 0, 0, 6, 8, 0, 0, 0, 0,},
        });
    }


    @Test @Ignore
    public void testReading() throws SudokuReader.CVError, IOException {
        for (Map.Entry<String, byte[][]> entry : testCases.entrySet()) {
            InputStream is = TestReader.class.getResourceAsStream(entry.getKey());
            SudokuReader sr = new SudokuReader(ImageTools.loadImg(is));
            byte[][] got = sr.read();
            byte[][] want = entry.getValue();
            if (Arrays.deepEquals(got, want)) {
                log.info(entry.getKey() + " -> OK");
            } else {
                log.severe(entry.getKey() + " -> NOK");
                log.severe("Want:" + DebugTools.fancyGrid(want));
                log.severe("Got:" + DebugTools.fancyGrid(got));
                sr.showDebug();
            }
        }

    }

}

