package ${basePackage};

import freemarker.template.Template;
import freemarker.template.TemplateException;
import ${basePackage}.api.Note;
import ${basePackage}.api.NotesService;
import ${basePackage}.service.ServiceRegistry;
import ${basePackage}.template.FreemarkerConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notes servlet - allows to post notes
 */
@WebServlet("/notes")
public class NotesServlet extends HttpServlet {

    private NotesService notesService;
    private Template template;

    @Override
    public void init() throws ServletException {
        notesService = ServiceRegistry.getInstance().get(NotesService.class);
        template = new FreemarkerConfig().loadTemplate("notes.ftl");
    }

    /**
     * Get the notes HTML page, with a list of notes and a form to submit new notes.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<Note> notes = notesService.list();

        PrintWriter writer = resp.getWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("notes", notes);

        try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }
    }

    /**
     * Post a new note (parameter: text), and redirect to GET afterwards.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String text = req.getParameter("text");
        Note note = new Note();
        note.setText(text);
        notesService.create(note);

        // send a redirect to refresh the page
        resp.sendRedirect(req.getRequestURI());
    }

}
