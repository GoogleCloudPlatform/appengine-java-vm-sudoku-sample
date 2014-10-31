package com.google.demo.sudoku;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvKNearest;


/**
 * Sudoku reader OCRs an image and read the digits from a picture of a sudoku.
 */
public class SudokuReader {

    /**
     * Number of samples in the training fine.
     */
    private static final int NB_SAMPLES = 120;

    /**
     * Size of the intermediate image.
     */
    private static final int INTERMEDIATE_NB_LINES = 480;

    /**
     * Granularity of the image processing.
     */
    private static final Size KERNEL_SIZE = new Size(3, 3);

    /**
     * Samples used for the training data.
     */
    private static final String SAMPLES_DATA = "/com/google/demo/sudoku/samples.data";

    /**
     * Responses for the training data.
     */
    public static final String RESPONSES_DATA = "/com/google/demo/sudoku/responses.data";

    static {
        try {
            Mat samples = loadData(SAMPLES_DATA, NB_SAMPLES, 100);
            Mat responses = loadData(RESPONSES_DATA, NB_SAMPLES, 1);
            model = new CvKNearest(samples, responses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CvKNearest model;

    // The resized image from the original.
    Mat originalImg = new Mat();

    // Those are all the intermediate steps recorded for troubleshooting/demonstration purposes.
    Mat borderDetectionImg; // The original image with edges detection for border detection.
    MatOfPoint mainContour;  // the main contour of the sudoku.
    java.util.List<MatOfPoint> digitContours = new java.util.ArrayList<>();
    java.util.List<MatOfPoint> digitBoundingBoxes = new java.util.ArrayList<>();
    Mat flatImg; // The original image with the perspective corrected.
    Mat digitSpottingImg; // The flat image with edges detection locating characters.
    Mat ocrImg; // The digitSpotting image with edges detection for character recognition.

    /**
     * Make a reader for the given image.
     * @param image the image you want to read the sudoku from.
     */
    public SudokuReader(Mat image) {
        // Resize it to a manageable size.
        Imgproc.resize(image, originalImg, new Size((image.width() * INTERMEDIATE_NB_LINES) / image.height(), INTERMEDIATE_NB_LINES));
    }

    /**
     * Load learning data from the given resource (a flat file of double numbers).
     *
     * @param resource the java resource you want to read the data from.
     * @param rows number of expected rows.
     * @param cols number of expected columns.
     * @return the matrice with the data loaded.
     * @throws IOException
     */
    private static Mat loadData(String resource, int rows, int cols) throws IOException {
        Mat mat = new Mat(rows, cols, CvType.CV_32F);
        BufferedReader reader = new BufferedReader(new InputStreamReader(SudokuReader.class.getResourceAsStream(resource)));
        String line;

        int row = 0;
        int col = 0;

        while ((line = reader.readLine()) != null) {
            String[] entries = line.split(" ");
            for (int i = 0; i < entries.length; i++) {
                mat.put(row, col, Double.parseDouble(entries[i]));
                col++;
                if (col == cols) {
                    col = 0;
                    row++;
                }
            }
        }

        reader.close();
        return mat;
    }

    /**
     * Enhance an image for border detection.
     * @param image the source image.
     * @return the image with borders enhanced.
     */
    private static Mat enhanceBorderDetect(Mat image) {
        Mat result = image.clone();
        Imgproc.GaussianBlur(result, result, KERNEL_SIZE, 0.0);
        Imgproc.dilate(result, result, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, KERNEL_SIZE));
        Imgproc.adaptiveThreshold(result, result, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 2);
        return result;
    }


    /**
     * Entry point that will OCR the image.
     * @return the recognized grid.
     * @throws CVError if it failed to find a sudoku in the image.
     */
    public byte[][] read() throws CVError {

        borderDetectionImg = enhanceBorderDetect(originalImg);
        mainContour = findMainContour(borderDetectionImg);
        if (mainContour == null) {
            throw new CVError("Could not find the main contour");
        }

        flatImg = ImageTools.correctPerspectiveAndCrop(originalImg, mainContour, originalImg.height());
        return findDigits();
    }

    /**
     * Get the intermediate rectified image.
     * @return the rectified image.
     */
    public Mat getFlatImg() {
        return flatImg;
    }

    /**
     * Find the outer contour of the sudoku.
     *
     * @param image the image in which you want to find the sudoku.
     * @return the coordinates of the 4 corners of the sudoku.
     */
    private MatOfPoint findMainContour(Mat image) {
        java.util.List<MatOfPoint> contours = new java.util.ArrayList<>();
        Imgproc.findContours(image.clone(), contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE); // clone because it is destructive.

        double minimum_area = originalImg.total() / 5.0;

        MatOfPoint bestContour = null;
        double bestArea = 0.0;
        nextContour:
        for (MatOfPoint contour : contours) {
            MatOfPoint2f contourf = new MatOfPoint2f(contour.toArray());
            double length = Imgproc.arcLength(contourf, true);

            MatOfPoint2f polygonf = new MatOfPoint2f();
            Imgproc.approxPolyDP(contourf, polygonf, 0.01 * length, true);

            double area = Imgproc.contourArea(polygonf);

            if (area < minimum_area) { // get a large enough area
                continue;
            }

            if (polygonf.total() != 4) { // in the shape of a 4 sided polygon
                continue;
            }

            MatOfPoint polygon = new MatOfPoint(polygonf.toArray());
            if (!Imgproc.isContourConvex(polygon)) { // not convex
                continue;
            }

            // Remove stuff way too close to the border to be natural.
            for (int i = 0; i < polygon.total(); i++) {
                int x = (int) polygon.get(i, 0)[0];
                int y = (int) polygon.get(i, 0)[1];
                if (x >= image.width() - 2 || x <= 2) {
                    continue nextContour;
                }
                if (y >= image.height() - 2 || y <= 2) {
                    continue nextContour;
                }
            }

            if (bestContour == null || area < bestArea) {
                bestContour = polygon;
                bestArea = area;
            }
        }
        return bestContour;
    }


    /**
     * Blur and threshold to recognize the areas where digits are.
     * @param image the image you want to transform.
     * @return an enhanced copy.
     */
    private Mat enhanceForDigitSpotting(Mat image) {
        Mat result = image.clone();
        Imgproc.GaussianBlur(result, result, KERNEL_SIZE, 3.0);
        Imgproc.adaptiveThreshold(result, result, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 2);
        return result;
    }

    /**
     * Improve the digit images for OCR.
     * @param image the image on which you want to OCR digits.
     * @return an enhanced copy.
     */
    private Mat enhanceForOCR(Mat image) {
        Mat result = image.clone();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, KERNEL_SIZE);
        Imgproc.erode(result, result, kernel);
        Imgproc.dilate(result, result, kernel);
        return result;
    }

    /**
     * Find the closest match for a digit in image.
     * @param image the digit image.
     * @return the digit.
     * @throws CVError if it was too far off for recognition.
     */
    private byte ocrDigit(Mat image) throws CVError {
        Mat results = new Mat();
        Imgproc.resize(image, image, new Size(10, 10));
        Mat input = new Mat(1, 100, CvType.CV_32F);
        for (int i = 0; i < 100; i++) {
            input.put(0, i, image.get(i / 10, i % 10)[0]);
        }
        model.find_nearest(input, 1, results, new Mat(), new Mat());
        byte resulting_digit = (byte) results.get(0, 0)[0];
        if (resulting_digit == 0) {
            throw new CVError("Cannot read digit");
        }
        return resulting_digit;
    }

    /**
     * Find the digits in the intermediate image.
     * @return the sudoku grid.
     * @throws CVError if it could not file the digits.
     */
    private byte[][] findDigits() throws CVError {
        byte[][] decoded = new byte[9][9];

        digitSpottingImg = enhanceForDigitSpotting(flatImg);
        java.util.List<MatOfPoint> contours = new java.util.ArrayList<>();
        Imgproc.findContours(digitSpottingImg.clone(), contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE); // clone because it is destructive.

        ocrImg = enhanceForOCR(digitSpottingImg);

        int approxSquareSize = ocrImg.width() / 9;
        int approxSquareArea = approxSquareSize * approxSquareSize;

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);

            // it needs to be large enough
            if (area < approxSquareArea / 50 || area > approxSquareArea / 2) {
                continue;
            }

            Rect roi = Imgproc.boundingRect(contour);

            // it cannot be bigger than a square on a dimension.
            if (roi.height > approxSquareSize || roi.width > approxSquareSize) {
                continue;
            }

            // it needs to be large enough on one dimension.
            if (roi.height < approxSquareSize / 3 && roi.width < approxSquareSize / 3) {
                continue;
            }

            // margins we consider to be too close to the delimiting lines.
            int marginLeft = approxSquareSize / 10;
            int marginRight = (approxSquareSize * 9) / 10;

            // relative position in the square
            int relx = roi.x % approxSquareSize;
            int rely = roi.y % approxSquareSize;

            if (relx > marginRight || relx < marginLeft || rely > marginRight || rely < marginLeft) {
                continue;
            }


            Mat digitImage = ocrImg.submat(roi);
            int gridx = (int) ((roi.x + roi.width / 2.0) / approxSquareSize);
            int gridy = (int) ((roi.y + roi.height / 2.0) / approxSquareSize);
            decoded[gridy][gridx] = ocrDigit(digitImage);
            digitContours.add(contour);
            digitBoundingBoxes.add(ImageTools.rect2Contour(roi));

        }


        return decoded;
    }

    public class CVError extends Exception {
        public CVError(String message) {
            super(message);
        }
    }

    public void showDebug() {
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadless()) {
            DebugTools.displayMat("Original", originalImg);
            DebugTools.displayContourOnMat("Main Contour", borderDetectionImg, mainContour);
            DebugTools.displayMat("Flatten Image", flatImg);
            DebugTools.displayContoursOnMat("Digit Contours", digitSpottingImg, digitContours);
            DebugTools.displayContoursOnMat("ROI for ORC", ocrImg, digitBoundingBoxes);
        }
    }

}
