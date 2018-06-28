package ${basePackage};

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Hello servlet
 *
 * @author ${user}
 * @since ${date}
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter writer = new PrintWriter(resp.getWriter());

        writer.write("<html>");
        writer.write("<head>");
        writer.write("<title>Example Servlet</title>");
        writer.write("</head>");
        writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
        writer.write("<body>");
        writer.write("<h1>Hello Servlet</h1>");

        writer.write("Current time is: " + LocalDateTime.now());
        writer.write("</body>");
        writer.write("</html>");
    }
}
