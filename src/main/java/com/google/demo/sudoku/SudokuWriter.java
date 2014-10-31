package com.google.demo.sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * SudokuWrite is a class made to rewrite a Sudoku back on the rectified version
 * of the sudoku image.
 */
public class SudokuWriter {

  private static final Logger log = Logger.getLogger(UploadServlet.class.getName());
  private static final String FONT_NAME = "/com/google/demo/sudoku/danielbd.ttf";
  private static Font baseFont;

  static {
    try {
      baseFont = Font.createFont(Font.TRUETYPE_FONT, SudokuWriter.class.getResourceAsStream(FONT_NAME));
    } catch (FontFormatException e) {
      log.severe("Unable to load font " + FONT_NAME + " (" + e + ")");
    } catch (IOException e) {
      log.severe("Unable to load font " + FONT_NAME + " (" + e + ")");
    }
  }

  /**
   * Try to find the best font fitting a square.
   *
   * @param pixelHeight the maximum pixel height you want the font to be.
   * @param target the graphic context on which you will render the font.
   * @return the font derived from baseFont that fit height.
   */
  private static Font findFont(int pixelHeight, Graphics2D target) {
    float size = 0f;
    int pixelSize = 0;
    Font font = null;
    while (pixelSize < pixelHeight) {
      size += 1f;
      font = baseFont.deriveFont(size);
      pixelSize = target.getFontMetrics(font).getHeight();
    }
    return font;
  }

  /**
   * Writes a solution on a rectified sudoku image.
   *
   * @param image the image on which you want to write the solution on.
   * @param original the original numbers from the sudoku with 0 marking the
   * places you can write on.
   * @param solution the solution of the sudoku.
   */
  public static void write(BufferedImage image, byte[][] original, byte[][] solution) {
    int w = image.getWidth();
    int h = image.getHeight();
    float cellw = w / 9f;
    float cellh = h / 9f;

    Graphics2D g = image.createGraphics();
    g.setColor(new Color(80, 80, 80));
    Font font = findFont((int) cellh, g);
    g.setFont(font);
    FontMetrics metrics = g.getFontMetrics(font);

    for (int y = 0; y < 9; y++) {
      for (int x = 0; x < 9; x++) {
        int digit = solution[y][x];
        if (original[y][x] == 0) {
          String str = String.valueOf(digit);
          int chrw = metrics.stringWidth(str);
          int chrh = metrics.getHeight();
          int descent = metrics.getDescent();
          g.drawString(str, x * cellw + (cellw - chrw) / 2, (y + 1) * cellh + (cellh - chrh - descent) / 2);
        }

      }
    }
  }
}
