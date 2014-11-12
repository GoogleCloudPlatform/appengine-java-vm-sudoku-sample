package com.google.demo.sudoku;

import com.googlecode.objectify.ObjectifyService;
import org.apache.commons.codec.binary.Base64OutputStream;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;


/**
 * UploadServlet, the entry point to upload a sudoku text to read.
 */
@MultipartConfig(
        maxRequestSize = 10_000_000
)
@WebServlet(
        name = "upload",
        urlPatterns = {"/upload"}
)
public class UploadServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(UploadServlet.class.getName());

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Handles the file upload.
     *
     * @param request  the request.
     * @param response the response.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Part mimeImage = request.getPart("text_file");
        String problem_str = convertStreamToString(mimeImage.getInputStream());
        log.info("Problem [" + problem_str + "]");

        byte[][] problem = SolvedSudoku.str2grid(problem_str);
        byte[][] solution = null;

        // TODO Solve / Store and Cache.

        // Feed the response.
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter output = response.getWriter();
        output.println(DebugTools.fancyGrid(solution));
        output.close();
    }
}
