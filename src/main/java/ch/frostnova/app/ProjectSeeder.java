package ch.frostnova.app;


import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by pwalser on 02.01.2017.
 */
public class ProjectSeeder {

    private final static File TEMPLATES_DIR = new File("templates");
    private final static List<String> FILTER_FILE_SUFFIXES = Arrays.asList("txt", "md", "xml", "java", "gradle");

    private final static Pattern SIMPLE_IDENTIFIER = Pattern.compile("\\p{Alnum}+([\\.\\-_]\\p{Alnum}+)*");
    private final static Pattern VERSION = Pattern.compile("(\\d+(?:\\.\\d+)*.*)");
    private final static Pattern JAVA_PACKAGE_NAME = Pattern.compile("([\\p{L}\\p{Sc}_][\\p{L}\\p{N}\\p{Sc}_]*\\.)*[\\p{L}\\p{Sc}_][\\p{L}\\p{N}\\p{Sc}_]*");

    public static void main(String[] args) throws IOException {

        System.out.println(TEMPLATES_DIR.getAbsolutePath());
        new ProjectSeeder();
    }

    public ProjectSeeder() throws IOException {

        List<String> availableTemplates = getAvailableTemplates();
        if (availableTemplates.isEmpty()) {
            System.err.println("No templates available");
            return;
        }
        System.out.println("Available templates:");
        availableTemplates.forEach(t -> System.out.println("- " + t));

        String template = null;
        while (!availableTemplates.contains(template)) {
            template = promptParameter("Choose template");
        }
        String projectGroup = promptParameter("Project group", "org.test", SIMPLE_IDENTIFIER);
        String projectName = promptParameter("Project name", "some-project", SIMPLE_IDENTIFIER);
        String projectDescription = promptParameter("Project description", projectName);
        String projectVersion = promptParameter("Project version", "1.0.0-SNAPSHOT", VERSION);

        String suggestedBasePackage = projectGroup + "." + projectName;
        suggestedBasePackage = suggestedBasePackage.replaceAll("[^\\w]", ".");
        String basePackage = promptParameter("Base package", suggestedBasePackage, JAVA_PACKAGE_NAME);
        String outputDir = promptParameter("Base output dir", new File("..").getAbsolutePath());

        Map<String, String> parameters = new HashMap<>();
        parameters.put("projectGroup", projectGroup);
        parameters.put("projectName", projectName);
        parameters.put("projectDescription", projectDescription);
        parameters.put("projectVersion", projectVersion);
        parameters.put("basePackage", basePackage);
        parameters.put("basePackagePath", basePackage.replace(".", "/"));

        seedProject(new File(TEMPLATES_DIR, template), new File(outputDir, projectName), parameters);
    }

    private static List<String> getAvailableTemplates() {
        File[] files = TEMPLATES_DIR.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        return Stream.of(files)
                .filter(File::isDirectory)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    private static String promptParameter(String prompt) {
        return promptParameter(prompt, null);
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
            if (pattern != null && !pattern.matcher(input).matches()) {
                System.out.println("  Invalid format, please try again");
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
        System.out.println("Project created at: " + outputDir.getAbsolutePath());
    }

    private void process(File templateDir, File outputDir, String sourcePath, Map<String, String> replacements) throws IOException {
        File file = new File(templateDir, sourcePath);
        File target = new File(outputDir, replaceAll(sourcePath, replacements));


        if (file.isDirectory()) {
            System.out.println(">> create:  " + target.getAbsolutePath());
            target.mkdirs();
            String[] paths = file.list();
            if (paths != null) {
                for (String path : paths) {
                    process(templateDir, outputDir, sourcePath + "/" + path, replacements);
                }
            }
        } else {
            if (FILTER_FILE_SUFFIXES.stream().anyMatch(s -> target.getName().endsWith("." + s))) {
                target.getParentFile().mkdirs();
                System.out.println(">> process: " + target.getAbsolutePath());
                copyText(file, target, replacements);
            } else {
                System.out.println(">> copy:    " + target.getAbsolutePath());
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
