package ch.frostnova.app;

import ch.frostnova.app.util.CommandLineUtil;
import ch.frostnova.app.util.FileCopy;
import ch.frostnova.app.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by pwalser on 02.01.2017.
 */
public class ProjectSeeder {

    private final static File TEMPLATES_DIR = new File("templates");
    private final static List<String> FILTER_FILE_SUFFIXES = Arrays.asList("txt", "md", "xml", "java", "gradle", "ts", "js", "json", "yaml", "yml", "adoc", "puml", "html", "css", "gitignore");

    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final static String PARAM_USER = "user";
    private final static String PARAM_DATE = "date";
    private final static String PARAM_DATE_TIME = "datetime";
    private final static String PARAM_TEMPLATE_NAME = "template.name";
    private final static String PARAM_TEMPLATE_DESCRIPTION = "template.description";
    private final static String PARAM_PROJECT_NAME = "projectName";

    private final static String ANSI_RESET = "\u001b[0m";
    private final static String ANSI_BOLD = "\u001b[1m";
    private final static String ANSI_UNDERLINE = "\u001b[4m";
    private final static String BULLET = "\u00bb";

    public static void main(String[] args) throws IOException {
        new ProjectSeeder();
    }

    public ProjectSeeder() throws IOException {
        ProjectTemplate template = selectTemplate();
        Map<String, String> parameters = parameterize(template);
        File outputDir = selectOutputDir(parameters);
        seedProject(template, outputDir, parameters);
    }

    private ProjectTemplate selectTemplate() {

        List<ProjectTemplate> availableTemplates = getAvailableTemplates();
        if (availableTemplates.isEmpty()) {
            System.err.println("No templates available");
            System.exit(1);
        }
        System.out.println("Available templates:\n");
        availableTemplates.forEach(t -> System.out.println(BULLET + " " + ANSI_BOLD + t.getName() + ANSI_RESET
                + "\n  " + t.getDescription() + "\n"));

        return pickTemplate(availableTemplates);
    }

    private Map<String, String> parameterize(ProjectTemplate template) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_TEMPLATE_NAME, template.getName());
        parameters.put(PARAM_USER, System.getProperty("user.name"));
        parameters.put(PARAM_DATE, LocalDate.now().format(DATE_FORMATTER));
        parameters.put(PARAM_DATE_TIME, LocalDateTime.now().format(DATE_TIME_FORMATTER));
        parameters.put(PARAM_TEMPLATE_DESCRIPTION, template.getDescription());
        String projectName = CommandLineUtil.promptValue("Project name", template.getName().toLowerCase().replaceAll("\\s+", "-") + "-project", ProjectTemplate.ParameterType.identifier.getPattern());
        parameters.put(PARAM_PROJECT_NAME, projectName);

        for (ProjectTemplate.Parameter parameter : template.getParameters()) {
            String defaultValue = Optional.ofNullable(parameter.getDefaultValue()).map(v -> StringUtil.replaceAll(v, parameters)).orElse(null);
            if (defaultValue != null && parameter.getType() == ProjectTemplate.ParameterType.javaPackage) {
                defaultValue = defaultValue.trim().replace("-", ".").replace("\\.+", ".").toLowerCase();
            }
            String value = CommandLineUtil.promptValue(parameter.getLabel(), defaultValue, parameter.getType().getPattern());
            parameters.put(parameter.getName(), value);
            if (parameter.getType() == ProjectTemplate.ParameterType.javaPackage) {
                parameters.put(parameter.getName() + "Path", value.replace(".", "/"));
            }
        }
        return parameters;
    }

    private File selectOutputDir(Map<String, String> parameters) throws IOException {
        File outputDir;
        do {
            String baseOutputDir = CommandLineUtil.promptValue("Base output dir", new File("..").getCanonicalFile().getAbsolutePath());
            outputDir = new File(baseOutputDir, parameters.get(PARAM_PROJECT_NAME));
            if (outputDir.exists() && outputDir.listFiles() != null && outputDir.listFiles().length > 0) {
                System.out.println(outputDir.getAbsolutePath() + " already exists, please chose another base output directory.");
                outputDir = null;
            }
        } while (outputDir == null);
        return outputDir;
    }

    private ProjectTemplate pickTemplate(List<ProjectTemplate> availableTemplates) {
        Map<String, ProjectTemplate> templateMap = availableTemplates.stream()
                .collect(Collectors.toMap(t -> t.getName().toLowerCase(), Function.identity()));

        List<String> sortedTemplateNames = availableTemplates.stream()
                .map(ProjectTemplate::getName)
                .sorted()
                .collect(Collectors.toList());

        String suggestedTemplateName = null;
        while (true) {
            String templateName = CommandLineUtil.promptValue("Choose template", suggestedTemplateName);
            ProjectTemplate template = templateMap.get(templateName.toLowerCase());
            if (template != null) {
                return template;
            }
            suggestedTemplateName = sortedTemplateNames.stream().filter(n -> n.toLowerCase().startsWith(templateName)).findFirst().orElse(null);
        }
    }

    private static List<ProjectTemplate> getAvailableTemplates() {
        File[] files = TEMPLATES_DIR.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        return Stream.of(files)
                .filter(File::isDirectory)
                .filter(d -> new File(d, "template.xml").exists())
                .map(ProjectTemplate::new)
                .sorted(Comparator.comparing(ProjectTemplate::getName))
                .collect(Collectors.toList());
    }

    private void seedProject(ProjectTemplate projectTemplate, File outputDir, Map<String, String> replacements) throws IOException {
        System.out.println();
        System.out.println("Seeding project...");
        System.out.println("Template dir: " + projectTemplate.getTemplateDir().getAbsolutePath());
        System.out.println("Output dir: " + outputDir.getAbsolutePath());
        replacements.keySet().stream().sorted().forEach(k -> System.out.println("- " + k + ": " + replacements.get(k)));

        outputDir.mkdirs();
        String[] paths = projectTemplate.getTemplateDir().list();
        if (paths != null) {
            for (String path : paths) {
                process(projectTemplate, projectTemplate.getTemplateDir(), outputDir, path, replacements);
            }
        }
        System.out.println();
        System.out.println("Project created at: " + outputDir.getAbsolutePath());
    }

    private void process(ProjectTemplate projectTemplate, File templateDir, File outputDir, String sourcePath, Map<String, String> replacements) throws IOException {
        File file = new File(templateDir, sourcePath);
        if (file.getParentFile().equals(templateDir) && file.getName().equals("template.xml")) {
            return;
        }
        File target = new File(outputDir, StringUtil.replaceAll(sourcePath, replacements));

        if (file.isDirectory()) {
            target.mkdirs();
            String[] paths = file.list();
            if (paths != null) {
                for (String path : paths) {
                    process(projectTemplate, templateDir, outputDir, sourcePath + "/" + path, replacements);
                }
            }
        } else if (file.canRead()) {
            System.out.print('.');
            if (FILTER_FILE_SUFFIXES.stream().anyMatch(s -> target.getName().endsWith("." + s))) {
                target.getParentFile().mkdirs();
                FileCopy.copyText(file, target, replacements);
            } else {
                FileCopy.copyBinary(file, target);
            }
        }
    }
}
