package ${basePackage}.api;

import java.util.List;
import java.util.NoSuchElementException;

public interface NotesService {

    Note get(long id) throws NoSuchElementException;

    List<Note> list();

    Note create(Note note) throws IllegalArgumentException;

    void update(Note note) throws IllegalArgumentException;

    void remove(long id);
}
