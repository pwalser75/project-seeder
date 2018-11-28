package ${basePackage};

import freemarker.template.Template;
import freemarker.template.TemplateException;
import ${basePackage}.template.FreemarkerConfig;
import ${basePackage}.template.model.Movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Template servlet - uses Freemarker to render a data model with a freemarker template (*.ftl)
 */
@WebServlet("/template")
public class TemplateServlet extends HttpServlet {

    private Template template;

    @Override
    public void init() throws ServletException {
        template = new FreemarkerConfig().loadTemplate("movie.ftl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter writer = resp.getWriter();
        Movie movie = Movie.example();
        try {
            template.process(movie, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }
    }
}
