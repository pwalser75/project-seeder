package ${basePackage}.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Note {

    @JsonProperty("id")
    private long id;

    @JsonProperty("created-on")
    private LocalDateTime creationTime;

    @JsonProperty("text")
    private String text;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
