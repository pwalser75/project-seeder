package ${basePackage}.service;

        import ${basePackage}.api.model.Note;
        import ${basePackage}.api.service.NoteService;
        import ${basePackage}.persistence.entity.NoteEntity;
        import ${basePackage}.persistence.repository.NoteRepository;

        import javax.ejb.Stateless;
        import javax.inject.Inject;
        import java.util.List;
        import java.util.Spliterator;
        import java.util.stream.Collectors;
        import java.util.stream.Stream;
        import java.util.stream.StreamSupport;

@Stateless
public class NoteServiceImpl implements NoteService {

    @Inject
    private NoteRepository repository;


    @Override
    public Note get(long id) {
        return convert(repository.findById(id).orElse(null));
    }

    @Override
    public Note save(Note note) {
        NoteEntity entity = new NoteEntity();
        if (note.getId() != null) {
            entity = repository.findById(note.getId()).orElse(null);
        }
        return convert(repository.save(update(entity, note)));
    }

    @Override
    public List<Note> list() {
        Spliterator<NoteEntity> spliterator = repository.findAll().spliterator();
        Stream<NoteEntity> stream = StreamSupport.stream(spliterator, false);
        return stream.map(this::convert).collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
    }

    private Note convert(NoteEntity entity) {
        if (entity == null) {
            return null;
        }
        Note dto = new Note();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setCreationDate(entity.getCreationDate());
        dto.setModificationDate(entity.getLastModifiedDate());
        return dto;
    }

    private NoteEntity update(NoteEntity entity, Note dto) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setText(dto.getText());
        return entity;
    }
}
