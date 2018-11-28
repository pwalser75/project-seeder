package ${basePackage}.service;

import ${basePackage}.api.Note;
import ${basePackage}.api.NotesService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Notes service singleton
 *
 * @author wap
 * @since 28.11.2018
 */
public class NotesServiceImpl implements NotesService {

    private final Map<Long, Note> notes = new HashMap<>();
    private final static AtomicInteger sequence = new AtomicInteger();

    @Override
    public Note get(long id) throws NoSuchElementException {
        Note note = notes.get(id);
        if (note == null) {
            throw new NoSuchElementException("No note with id " + id + " exists");
        }
        return note;
    }

    @Override
    public List<Note> list() {
        List<Note> sortedNotes = new LinkedList<>(notes.values());
        sortedNotes.sort(Comparator.comparing(Note::getId));
        return sortedNotes;
    }

    @Override
    public Note create(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note is required");
        }
        if (note.getText() == null || note.getText().trim().length() == 0) {
            throw new IllegalArgumentException("Note text required");
        }
        note.setId(sequence.incrementAndGet());
        note.setCreationTime(LocalDateTime.now());
        notes.put(note.getId(), note);
        return note;
    }

    @Override
    public void update(Note note) throws IllegalArgumentException {
        if (note == null) {
            throw new IllegalArgumentException("Note is required");
        }
        if (note.getText() == null || note.getText().trim().length() == 0) {
            throw new IllegalArgumentException("Note text required");
        }
        Note noteToUpdate = get(note.getId());
        noteToUpdate.setText(note.getText());
    }

    @Override
    public void remove(long id) {
        notes.remove(id);
    }
}
