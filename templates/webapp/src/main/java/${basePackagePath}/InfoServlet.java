package ${basePackage};

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.Principal;
import java.util.Enumeration;

/**
 * Info servlet - prints request information
 */
@WebServlet("/info")
public class InfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter writer = new PrintWriter(resp.getWriter());

        writer.write("<html>");
        writer.write("<head>");
        writer.write("<title>Example Servlet</title>");
        writer.write("</head>");
        writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
        writer.write("<body>");
        writer.write("<h4>Example Servlet</h4>");

        Principal userPrincipal = req.getUserPrincipal();
        String userName = userPrincipal != null ? userPrincipal.getName() : "unknown visitor";
        writer.write("<h1>Welcome, " + userName + ".</h1>");
        writer.write("<h3>Information</h3>");
        writer.write("<ul>");
        writeListEntry(writer, "RequestURL", req.getRequestURL() + "");
        writeListEntry(writer, "RequestURI", req.getRequestURI());
        writeListEntry(writer, "AuthType", req.getAuthType());
        writeListEntry(writer, "Method", req.getMethod());
        writeListEntry(writer, "RequestedSessionId", req.getRequestedSessionId());
        writeListEntry(writer, "ServletPath", req.getServletPath());
        writeListEntry(writer, "Locale", req.getLocale() + "");
        writeListEntry(writer, "RemoteAddr", req.getRemoteAddr());
        writer.write("</ul>");

        writer.write("<h3>Headers</h3>");
        writer.write("<ul>");
        Enumeration<String> headers = req.getHeaderNames();
        if (!headers.hasMoreElements()) {
            writer.write("none");
        }
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            writeListEntry(writer, header, req.getHeader(header));
        }
        writer.write("</ul>");

        writer.write("<h3>Request Parameters</h3>");
        writer.write("<ul>");
        Enumeration<String> parameters = req.getParameterNames();
        if (!parameters.hasMoreElements()) {
            writer.write("none");
        }
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            writeListEntry(writer, parameter, req.getParameter(parameter));
        }
        writer.write("</ul>");

        writer.write("<h3>Cookies</h3>");
        writer.write("<ul>");
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                writeListEntry(writer, cookie.getName(), cookie.getValue());
            }
        }
        writer.write("</ul>");

        writer.write("<h3>Example REST service</h3>");
        writer.write("<a href=\"api/weather\">Example Web Service Endpoint (<code>GET /api/weather</code>)</a>");

        writer.write("</body>");
        writer.write("</html>");

        writer.flush();
    }

    private void writeListEntry(Writer writer, String label, String text) throws IOException {
        writer.write("<li><b>" + label + "</b>: " + text + "</li>");
    }
}
