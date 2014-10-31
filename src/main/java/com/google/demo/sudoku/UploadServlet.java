package com.google.demo.sudoku;

import com.googlecode.objectify.ObjectifyService;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.opencv.core.Core;
import org.opencv.core.Mat;

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
import java.util.Collections;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;


/**
 * UploadServlet, the entry point to upload a sudoku image to read.
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

    private static Cache cache = null;

    static {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (Exception e) {
            log.severe("Crashed loading: " + Core.NATIVE_LIBRARY_NAME);
            log.severe(System.getProperty("java.library.path"));
        }

        // Register Objectify stuff.
        ObjectifyService.register(SolvedSudoku.class);

        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
            // ...
        }

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
        Part mimeImage = request.getPart("image_file");
        InputStream input = mimeImage.getInputStream();
        Mat puzzleImage = ImageTools.loadImg(input);

        try {
            // OCR the image.
            SudokuReader sr = new SudokuReader(puzzleImage);

            // read the OCR'ed grid.
            byte[][] problem = sr.read();

            // get the rectified image of the sudoku.
            BufferedImage imageResponse = ImageTools.mat2Image(sr.getFlatImg());

            // we will use the problem itself as an index.
            String id = SolvedSudoku.grid2str(problem);

            log.info("Problem [" + id + "]");
            byte[][] solution;

            // First lookup in memcache.
            SolvedSudoku solved = (SolvedSudoku) cache.get(id);

            if (solved == null) {
                // Then lookup in the datastore.
                solved = ofy().load().type(SolvedSudoku.class).id(id).now();
                if (solved == null) {
                    // Then solve it.
                    log.info("It is a new one, solving it. [" + id + "]");
                    solution = new SudokuSolver(problem).solve();
                    solved = new SolvedSudoku(id, SolvedSudoku.grid2str(solution));

                    // Store it.
                    log.info("Store it. [" + id + "]");
                    ofy().save().entity(solved).now();
                } else {
                    log.info("This solution was in DB. [" + id + "] -> [" + solved.solution + "]");
                }
                cache.put(id, solved);
            } else {
                log.info("This solution was cached. [" + id + "] -> [" + solved.solution + "]");
            }

            solution = SolvedSudoku.str2grid(solved.solution);

            // Write back the solution on the rectified image.
            SudokuWriter.write(imageResponse, problem, solution);

            // Feed the response.
            response.setContentType("image/png");
            ServletOutputStream output = response.getOutputStream();
            Base64OutputStream base64Output = new Base64OutputStream(output);
            ImageIO.write(imageResponse, "png", ImageIO.createImageOutputStream(base64Output));
            output.close();
        } catch (Throwable error) {
            log.warning("Could not solve the puzzle: " + error.getMessage());
            response.setContentType("text/plain; charset=UTF-8");
            PrintWriter output = response.getWriter();
            output.println(error.getMessage());
            output.close();
        }
    }
}
