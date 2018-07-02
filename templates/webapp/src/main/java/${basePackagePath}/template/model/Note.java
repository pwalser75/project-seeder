package ${basePackage}.template.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Note data model
 *
 * @author ${user}
 * @since ${date}
 */
public class Note {

    private final static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime timestamp;
    private String text;

    public Note(String text) {
        this.timestamp = LocalDateTime.now();
        this.text = text != null ? text.trim() : "-";
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DATE_TIME_FORMAT);
    }

    public String getText() {
        return text;
    }
}
