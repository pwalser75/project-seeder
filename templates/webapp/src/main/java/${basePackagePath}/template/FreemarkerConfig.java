package ${basePackage}.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Locale;

/**
 * Freemarker configuration
 *
 * @author ${user}
 * @since ${date}
 */
public class FreemarkerConfig {

    private Configuration config;

    public FreemarkerConfig() {

        config = new Configuration();
        config.setDefaultEncoding("UTF-8");
        config.setLocale(Locale.getDefault());
        config.setClassForTemplateLoading(getClass(), "/templates");
        config.setIncompatibleImprovements(new Version(2, 3, 20));
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public Template loadTemplate(String resourcePath) throws ServletException {
        try {
            return config.getTemplate(resourcePath);
        } catch (IOException ex) {
            throw new ServletException("Failed to load template: " + ex.getMessage());
        }
    }
}
