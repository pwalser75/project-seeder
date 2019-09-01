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

    public static void main(final String[] args) throws IOException {

        System.out.println(TEMPLATES_DIR.getAbsolutePath());
        new ProjectSeeder();
    }

    public ProjectSeeder() throws IOException {

        runProjectWizard();
    }

    private void runProjectWizard() throws IOException {
        final List<ProjectTemplate> availableTemplates = getAvailableTemplates();
        if (availableTemplates.isEmpty()) {
            System.err.println("No templates available");
            return;
        }
        System.out.println("Available templates:\n");
        availableTemplates.forEach(t -> System.out.println("* " + t.getName() + "\n  " + t.getDescription() + "\n"));

        final ProjectTemplate template = pickTemplate(availableTemplates);
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("user", System.getProperty("user.name"));
        parameters.put("date", LocalDate.now().format(DATE_FORMATTER));
        parameters.put("datetime", LocalDateTime.now().format(DATE_TIME_FORMATTER));
        parameters.put("template.name", template.getName());
        parameters.put("template.description", template.getDescription());
        final String projectName = CommandLineUtil.promptValue("Project name", template.getName().toLowerCase().replaceAll("\\s+", "-") + "-project", ProjectTemplate.ParameterType.IDENTIFIER.getPattern());
        parameters.put("projectName", projectName);

        for (final ProjectTemplate.Parameter parameter : template.getParameters()) {
            String defaultValue = Optional.ofNullable(parameter.getDefaultValue()).map(v -> StringUtil.replaceAll(v, parameters)).orElse(null);
            if (defaultValue != null && parameter.getType() == ProjectTemplate.ParameterType.JAVA_PACKAGE) {
                defaultValue = defaultValue.trim().replace("-", ".").replace("\\.+", ".").toLowerCase();
            }
            final String value = CommandLineUtil.promptValue(parameter.getLabel(), defaultValue, parameter.getType().getPattern());
            parameters.put(parameter.getName(), value);
            if (parameter.getType() == ProjectTemplate.ParameterType.JAVA_PACKAGE) {
                parameters.put(parameter.getName() + "Path", value.replace(".", "/"));
            }
        }

        File outputDir;
        do {
            final String baseOutputDir = CommandLineUtil.promptValue("Base output dir", new File("..").getCanonicalFile().getAbsolutePath());
            outputDir = new File(baseOutputDir, projectName);
            if (outputDir.exists() && outputDir.listFiles() != null && outputDir.listFiles().length > 0) {
                System.out.println(outputDir.getAbsolutePath() + " already exists, please chose another base output directory.");
                outputDir = null;
            }
        } while (outputDir == null);

        seedProject(template.getTemplateDir(), outputDir, parameters);
    }

    private ProjectTemplate pickTemplate(final List<ProjectTemplate> availableTemplates) {
        final Map<String, ProjectTemplate> templateMap = availableTemplates.stream()
                .collect(Collectors.toMap(t -> t.getName().toLowerCase(), Function.identity()));

        final List<String> sortedTemplateNames = availableTemplates.stream()
                .map(ProjectTemplate::getName)
                .sorted()
                .collect(Collectors.toList());

        String suggestedTemplateName = null;
        while (true) {
            final String templateName = CommandLineUtil.promptValue("Choose template", suggestedTemplateName);
            final ProjectTemplate template = templateMap.get(templateName.toLowerCase());
            if (template != null) {
                return template;
            }
            suggestedTemplateName = sortedTemplateNames.stream().filter(n -> n.toLowerCase().startsWith(templateName)).findFirst().orElse(null);
        }
    }

    private static List<ProjectTemplate> getAvailableTemplates() {
        final File[] files = TEMPLATES_DIR.listFiles();
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

    private void seedProject(final File templateDir, final File outputDir, final Map<String, String> replacements) throws IOException {
        System.out.println();
        System.out.println("Seeding project...");
        System.out.println("Template dir: " + templateDir.getAbsolutePath());
        System.out.println("Output dir: " + outputDir.getAbsolutePath());
        replacements.keySet().stream().sorted().forEach(k -> System.out.println("- " + k + ": " + replacements.get(k)));

        outputDir.mkdirs();
        final String[] paths = templateDir.list();
        if (paths != null) {
            for (final String path : paths) {
                process(templateDir, outputDir, path, replacements);
            }
        }
        System.out.println();
        System.out.println("Project created at: " + outputDir.getAbsolutePath());
    }

    private void process(final File templateDir, final File outputDir, final String sourcePath, final Map<String, String> replacements) throws IOException {
        final File file = new File(templateDir, sourcePath);
        if (file.getParentFile().equals(templateDir) && file.getName().equals("template.xml")) {
            return;
        }
        final File target = new File(outputDir, StringUtil.replaceAll(sourcePath, replacements));

        if (file.isDirectory()) {
            target.mkdirs();
            final String[] paths = file.list();
            if (paths != null) {
                for (final String path : paths) {
                    process(templateDir, outputDir, sourcePath + "/" + path, replacements);
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
