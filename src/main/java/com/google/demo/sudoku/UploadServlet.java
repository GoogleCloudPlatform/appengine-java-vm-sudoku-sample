package com.google.demo.sudoku;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;


/**
 * UploadServlet, the entry point to upload a sudoku image to read.
 */
@WebServlet(
        name = "upload",
        urlPatterns = {"/upload"}
)
public class UploadServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(UploadServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Got Hello world.");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1> Hello world ! </h1>");
    } 
}
