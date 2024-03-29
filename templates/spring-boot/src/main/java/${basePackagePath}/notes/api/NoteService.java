package ${basePackage}.notes.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Note service contract.
 */
public interface NoteService {

    Note get(long id);

    Note save(@NotNull @Valid Note note);

    List<Note> list();

    void delete(long id);

}