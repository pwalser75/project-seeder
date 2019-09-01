package ch.frostnova.app.util;


import java.io.*;
import java.util.Map;

public final class FileCopy {

    private FileCopy() {

    }

    public static void copyText(final File src, final File dst, final Map<String, String> replacements) throws IOException {

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(src)))) {
            try (final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst)))) {

                boolean firstLine = true;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!firstLine) {
                        writer.write("\n");
                    }
                    firstLine = false;
                    writer.write(StringUtil.replaceAll(line, replacements));
                }
                writer.flush();
            }
        }
    }

    public static void copyBinary(final File src, final File dst) throws IOException {
        final byte[] buffer = new byte[0xFFF];
        try (final BufferedInputStream in = new BufferedInputStream(new FileInputStream(src.getCanonicalFile()))) {
            try (final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dst.getCanonicalFile()))) {
                int read;
                while ((read = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, read);
                }
                out.flush();
            }
        }
    }
}
