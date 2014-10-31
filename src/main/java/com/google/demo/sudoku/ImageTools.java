package com.google.demo.sudoku;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * Various helpers for image manipulations.
 */
public class ImageTools {

    /**
     * Converts an OpenCV matrix to an image.
     * @param m the matrix you want to convert.
     * @return the equivalent java image.
     */
    public static BufferedImage mat2Image(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    /**
     * Load an image (matrix) from an inputstream (as an image in png or jpg format for example).
     *
     * @param inputStream the inputstream you want to read the image from.
     * @return the OpenCV matrix
     * @throws IOException if something happened on the inputstream.
     */
    public static Mat loadImg(InputStream inputStream) throws IOException {
        byte[] temporaryImageInMemory = readStream(inputStream);
        Mat outputImage = Highgui.imdecode(new MatOfByte(temporaryImageInMemory), Highgui.IMREAD_GRAYSCALE);
        return outputImage;
    }

    /**
     * Helper to read an inputstrean in a byte array.
     *
     * @param stream the stream to read.
     * @return the read bytearray.
     * @throws IOException if something happened on the inputstream.
     */
    private static byte[] readStream(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];

        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }

    /**
     * Compute the mathematical center of a matrix of 4 points.
     * @param contour the matrix of points you want to find the center for.
     * @return a couple of coordinates as an array [x, y].
     */
    private static double[] computeCenter(MatOfPoint contour) {
        double[] center = new double[2];
        for (int i = 0; i < 4; i++) {
            center[0] += contour.get(i, 0)[0];
            center[1] += contour.get(i, 0)[1];
        }
        center[0] /= 4.0;
        center[1] /= 4.0;
        return center;
    }

    /**
     * Given a contour and an image, assuming the contour is flat square, rectify the image perpective,
     * crop and resize it.
     *
     * @param image the original image on which the contour is.
     * @param contour the contour of a flat object.
     * @param size the final size of the image you want as a result.
     *             (as it is a square it will be its height and width).
     * @return new image representing the flat and full frame representation of the square.
     */
    public static Mat correctPerspectiveAndCrop(Mat image, MatOfPoint contour, double size) {

        double[] center = computeCenter(contour);

        double[] topleft = null;
        double[] topright = null;
        double[] bottomleft = null;
        double[] bottomright = null;

        for (int i = 0; i < 4; i++) {
            double[] point = contour.get(i, 0);
            boolean left = point[0] < center[0];
            boolean top = point[1] < center[1];

            if (top && left) {
                topleft = point;
            } else if (top && !left) {
                topright = point;
            } else if (!top && left) {
                bottomleft = point;
            } else if (!top && !left) {
                bottomright = point;
            }
        }

        // compute the transformation matrices to flatten the puzzle out on a size * size square.
        Mat src_mat = new Mat(4, 1, CvType.CV_32FC2);
        src_mat.put(0, 0, topleft[0], topleft[1], topright[0], topright[1], bottomright[0], bottomright[1], bottomleft[0], bottomleft[1]);

        Mat dst_mat = new Mat(4, 1, CvType.CV_32FC2);
        dst_mat.put(0, 0, 0.0, 0.0, size, 0.0, size, size, 0.0, size);
        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(src_mat, dst_mat);

        Mat result = new Mat();
        Imgproc.warpPerspective(image, result, perspectiveTransform, new Size(size, size));
        return result;
    }

    /**
     * Helper to transform a rectangle to a contour.
     * @param r a rectangle.
     * @return its corresponding contour.
     */
    public static MatOfPoint rect2Contour(Rect r) {
        return new MatOfPoint(
                new Point(r.x, r.y),
                new Point(r.x + r.width, r.y),
                new Point(r.x + r.width, r.y + r.height),
                new Point(r.x, r.y + r.height)
        );

    }
}
