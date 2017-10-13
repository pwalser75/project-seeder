package ch.frostnova.app;


import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by pwalser on 02.01.2017.
 */
public class ProjectSeeder {

    private final static File TEMPLATES_DIR = new File("templates");
    private final static List<String> FILTER_FILE_SUFFIXES = Arrays.asList("txt", "md", "xml", "java", "gradle", "ts", "js", "json", "adoc", "puml");

    public static void main(String[] args) throws IOException {

        System.out.println(TEMPLATES_DIR.getAbsolutePath());
        new ProjectSeeder();
    }

    public ProjectSeeder() throws IOException {
        projectWizard();
    }

    private void projectWizard() throws IOException {
        List<ProjectTemplate> availableTemplates = getAvailableTemplates();
        if (availableTemplates.isEmpty()) {
            System.err.println("No templates available");
            return;
        }
        System.out.println("Available templates:");
        availableTemplates.forEach(t -> System.out.println("- " + t.getName() + "\n    " + t.getDescription()));

        ProjectTemplate template = pickTemplate(availableTemplates);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("template.name", template.getName());
        parameters.put("template.description", template.getDescription());
        String projectName = promptParameter("Project name", template.getName() + "-project", ProjectTemplate.ParameterType.identifier.getPattern());
        parameters.put("projectName", projectName);

        for (ProjectTemplate.Parameter parameter : template.getParameters()) {
            String defaultValue = parameter.getDefaultValue() != null ? replaceAll(parameter.getDefaultValue(), parameters) : null;
            if (defaultValue != null && parameter.getType() == ProjectTemplate.ParameterType.javaPackage) {
                defaultValue = defaultValue.trim().replace("-", ".").replace("\\.+", ".").toLowerCase();
            }
            String value = promptParameter(parameter.getLabel(), defaultValue, parameter.getType().getPattern());
            parameters.put(parameter.getName(), value);
            if (parameter.getType() == ProjectTemplate.ParameterType.javaPackage) {
                parameters.put(parameter.getName() + "Path", value.replace(".", "/"));
            }
        }

        for (String key : parameters.keySet()) {
            System.out.println(key + " = " + parameters.get(key));
        }

        String outputDir = promptParameter("Base output dir", new File("..").getAbsolutePath());
        seedProject(template.getTemplateDir(), new File(outputDir, projectName), parameters);
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
            String templateName = promptParameter("Choose template", suggestedTemplateName);
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

    private static String promptParameter(String prompt, String defaultValue) {
        return promptParameter(prompt, defaultValue, null);
    }

    private static String promptParameter(String prompt, String defaultValue, Pattern pattern) {
        Scanner scanner = new Scanner(System.in);
        String input = null;
        while (input == null) {
            System.out.print(prompt + (defaultValue != null ? " (" + defaultValue + "): " : ": "));
            input = scanner.nextLine().trim();
            if (input.trim().length() == 0) {
                input = defaultValue;
            }
            if (input != null && pattern != null && !pattern.matcher(input).matches()) {
                System.out.println("   [!] invalid format, expected: " + pattern.pattern());
                input = null;
            }
        }
        return input;
    }

    private void seedProject(File templateDir, File outputDir, Map<String, String> replacements) throws IOException {
        System.out.println();
        System.out.println("Seeding project...");
        System.out.println("Template dir: " + templateDir.getAbsolutePath());
        System.out.println("Output dir: " + outputDir.getAbsolutePath());
        replacements.keySet().stream().sorted().forEach(k -> System.out.println(k + ": " + replacements.get(k)));

        String[] paths = templateDir.list();
        if (paths != null) {
            for (String path : paths) {
                process(templateDir, outputDir, path, replacements);
            }
        }
        System.out.println();
        System.out.println("Project created at: " + outputDir.getAbsolutePath());
    }

    private void process(File templateDir, File outputDir, String sourcePath, Map<String, String> replacements) throws IOException {
        File file = new File(templateDir, sourcePath);
        if (file.getParentFile().equals(templateDir) && file.getName().equals("template.xml")) {
            return;
        }
        File target = new File(outputDir, replaceAll(sourcePath, replacements));

        if (file.isDirectory()) {
            target.mkdirs();
            String[] paths = file.list();
            if (paths != null) {
                for (String path : paths) {
                    process(templateDir, outputDir, sourcePath + "/" + path, replacements);
                }
            }
        } else {
            System.out.print('.');
            if (FILTER_FILE_SUFFIXES.stream().anyMatch(s -> target.getName().endsWith("." + s))) {
                target.getParentFile().mkdirs();
                copyText(file, target, replacements);
            } else {
                copyBinary(file, target);
            }
        }
    }

    private void copyText(File src, File dst, Map<String, String> replacements) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(src)))) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = replaceAll(line, replacements);
                    writer.write(line + "\n");
                }
                writer.flush();
            }
        }
    }

    private void copyBinary(File src, File dst) throws IOException {

        byte[] buffer = new byte[0xFFF];
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(src))) {
            try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dst))) {
                int read;
                while ((read = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, read);
                }
                out.flush();
            }
        }
    }

    private String replaceAll(String s, Map<String, String> replacements) {
        if (s == null) {
            return s;
        }
        for (String key : replacements.keySet()) {
            s = s.replace("${" + key + "}", replacements.get(key));
        }
        return s;
    }
}
