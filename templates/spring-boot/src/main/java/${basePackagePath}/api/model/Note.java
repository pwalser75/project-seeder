package ${basePackage}.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for Note
 */
@ApiModel("Note")
public class Note implements Serializable {

    @ApiModelProperty(notes = "identifier (generated)")
    @JsonProperty("id")
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 2048)
    @ApiModelProperty(notes = "text of the note, up to 2048 characters")
    @JsonProperty("text")
    private String text;

    @ApiModelProperty(notes = "creation date (generated)")
    @JsonProperty("creationDate")
    @PastOrPresent
    private LocalDateTime creationDate;

    @ApiModelProperty(notes = "last modification date (generated)")
    @JsonProperty("modificationDate")
    @PastOrPresent
    private LocalDateTime modificationDate;

    public Note() {
        //
    }

    public Note(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + id + ": " + text;
    }
}
