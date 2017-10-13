package ch.frostnova.app;

import ch.frostnova.app.xml.XMLNode;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A project template (based on a template.xml)
 *
 * @author pwalser
 * @since 13.10.2017.
 */
public class ProjectTemplate {

    private final String name;
    private final String description;
    private final List<Parameter> parameters = new LinkedList<>();
    private final File templateDir;

    public static void main(String[] args) throws Exception {
        ProjectTemplate template = new ProjectTemplate(new File("templates/simple"));
        System.out.println("Name: " + template.getName());
        System.out.println("Description: " + template.getDescription());
        System.out.println("Parameters:");
        template.parameters.stream().map(Object::toString).map(s -> "- " + s).forEach(System.out::println);
    }

    /**
     * Constructor
     *
     * @param directory directory containing the template (template.xml and files)
     */
    public ProjectTemplate(File directory) {

        if (directory==null || !directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " does not exist, or is not a directory");
        }
        templateDir=directory;
        File xmlFile = new File(directory, "template.xml");
        if (!xmlFile.exists() || !xmlFile.isFile() || !xmlFile.canRead()) {
            throw new IllegalArgumentException(xmlFile.getAbsolutePath() + " cannot be read");
        }
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            XMLNode root = new XMLNode(doc.getDocumentElement());
            verify(root.getName().equals("template"), "Expected <template>");

            XMLNode nameNode = root.getChild("name");
            XMLNode descriptionNode = root.getChild("description");
            XMLNode parameterNodes = root.getChild("parameters");

            this.name = nameNode != null ? nameNode.getTrimmedText() : null;
            this.description = descriptionNode != null ? descriptionNode.getTrimmedText() : null;
            if (parameterNodes != null) {
                for (XMLNode parameterNode : parameterNodes.getChildren("parameter")) {
                    parameters.add(new Parameter(parameterNode.getAttribute("name"),
                            parameterNode.getAttribute("label"),
                            ParameterType.valueOf(parameterNode.getAttribute("type")),
                            parameterNode.getAttribute("default")));
                }
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Template loading failed: " + ex.getMessage());
        }
    }

    private void verify(boolean test, String message) {
        if (!test) {
            throw new IllegalArgumentException(message);
        }
    }

    public File getTemplateDir() {
        return templateDir;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Parameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public enum ParameterType {
        identifier("\\p{Alnum}+([\\.\\-_]\\p{Alnum}+)*"),
        version("(\\d+(?:\\.\\d+)*.*)"),
        text(".+"),
        javaPackage("([\\p{L}\\p{Sc}_][\\p{L}\\p{N}\\p{Sc}_]*\\.)*[\\p{L}\\p{Sc}_][\\p{L}\\p{N}\\p{Sc}_]*");

        Pattern pattern;

        ParameterType(String regex) {
            pattern = Pattern.compile(regex);
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    public static class Parameter {

        private final String name;
        private final String label;
        private final ParameterType type;
        private final String defaultValue;

        public Parameter(String name, String label, ParameterType type, String defaultValue) {
            this.name = name;
            this.label = label;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }

        public ParameterType getType() {
            return type;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("name= " + name);
            builder.append(", ");
            builder.append("label= " + label);
            builder.append(", ");
            builder.append("type= " + type);
            builder.append(", ");
            builder.append("default= " + defaultValue);

            return builder.toString();
        }
    }
}