package ${basePackage}.api.service;

import ${basePackage}.api.model.Note;

import java.util.List;

/**
 * Note service contract
 */
public interface NoteService {

    Note get(long id);

    Note save(Note note);

    List<Note> list();

    void delete(long id);

}
