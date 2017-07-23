package ${basePackage}.persistence.entity;

import ch.frostnova.persistence.api.entity.BaseMetadataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "note")
public class NoteEntity extends BaseMetadataEntity<Long> {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -1166461789114073327L;

    @Column(name = "text", unique = false, length = 2048, nullable = false)
    private String text;

    private LocalDate creationDate;
    private LocalDate lastModifiedDate;

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return getId() + ": " + getText();
    }

}
