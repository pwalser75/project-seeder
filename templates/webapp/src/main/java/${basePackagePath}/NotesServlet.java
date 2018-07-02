package ${basePackage};

import freemarker.template.Template;
import freemarker.template.TemplateException;
import ${basePackage}.template.FreemarkerConfig;
import ${basePackage}.template.model.Note;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Notes servlet - allows to post notes
 *
 * @author ${user}
 * @since ${date}
 */
@WebServlet("/notes")
public class NotesServlet extends HttpServlet {

    private Template template;

    @Override
    public void init() throws ServletException {
        template = new FreemarkerConfig().loadTemplate("notes.ftl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<Note> notes = getNotes(req.getSession());

        PrintWriter writer = resp.getWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("notes", notes);

        try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String text = req.getParameter("text");
        if (text != null && text.trim().length() > 0) {
            List<Note> notes = getNotes(req.getSession());
            notes.add(new Note(text));
        }
        resp.sendRedirect(req.getRequestURI());
    }

    /**
     * Get the list of notes from the session (or create a list, if it does not exist yet).
     *
     * @param session the session
     * @return list of notes associated with the session
     */
    private List<Note> getNotes(HttpSession session) {
        List<Note> notes = (List<Note>) session.getAttribute("notes-list");
        if (notes == null) {
            notes = new LinkedList<>();
            session.setAttribute("notes-list", notes);
        }
        return notes;
    }
}
